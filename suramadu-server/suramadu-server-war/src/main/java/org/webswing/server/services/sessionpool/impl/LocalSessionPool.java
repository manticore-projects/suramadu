package org.webswing.server.services.sessionpool.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.Constants;
import org.webswing.server.api.model.ProcessStatusEnum;
import org.webswing.server.api.services.sessionpool.ServerSessionPoolConnector;
import org.webswing.server.api.services.swinginstance.ConnectedSwingInstance;
import org.webswing.server.common.model.SwingConfig;
import org.webswing.server.common.model.meta.ConfigContext;
import org.webswing.server.common.model.meta.MetaObject;
import org.webswing.server.common.service.config.ConfigurationService;
import org.webswing.server.common.service.swingprocess.ProcessStartupParams;
import org.webswing.server.common.util.CommonUtil;
import org.webswing.server.common.util.VariableSubstitutor;
import org.webswing.server.model.exception.WsException;
import org.webswing.sessionpool.api.service.swingprocess.ProcessExitListener;
import org.webswing.sessionpool.api.service.swingprocess.SwingProcess;
import org.webswing.sessionpool.api.service.swingprocess.SwingProcessService;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Holds process related functionality.
 */
public class LocalSessionPool {

  private static final Logger log = LoggerFactory.getLogger(LocalSessionPool.class);

  private final ServerSessionPoolConnector connector;
  private final SwingProcessService processService;
  private final ConfigurationService<SwingConfig> configService;

  public LocalSessionPool(ServerSessionPoolConnector connector, SwingProcessService processService,
      ConfigurationService<SwingConfig> configService) {
    this.connector = connector;
    this.processService = processService;
    this.configService = configService;
  }

  public void createProcess(String userId, Map<String, String> userAttributes, String userIp,
      String userLocale, String userTimeZone, String customArgs, ProcessStartupParams startupParams)
      throws WsException {
    SwingConfig config = getSwingConfig(startupParams.getPathMapping());
    VariableSubstitutor subs = VariableSubstitutor.forSwingInstance(config, userId, userAttributes,
        startupParams.getInstanceId(), userIp, userLocale, userTimeZone, customArgs);

    try {
      startupParams.setAppConfig(config);
      startupParams.setFileResolver(
          getConfigContext(startupParams.getPathMapping(), config, subs)::resolveFile);
      startupParams.setSubs(subs);
      startupParams
          .setAppConnectionSecret(System.getProperty(Constants.WEBSWING_CONNECTION_SECRET));
      startupParams.setWebsocketUrl(getWebsocketUrl(startupParams.getPathMapping()));

      SwingProcess process = processService.startProcess(startupParams);
      process.setProcessExitListener(new ProcessExitListener() {
        @Override
        public void onClose() {
          closeProcess(startupParams.getPathMapping(), startupParams.getInstanceId());
        }
      });
      // no need to set applicationExitListener in non-cluster version, because the server gets the
      // ExitMsgOut notification
      // applicationExitListener is useful in case of multiple cluster servers to notify the other
      // servers immediately that the application is exiting

      if (process.isRunning()) {
        registerProcessStatus(startupParams.getInstanceId(), process);
      }

      process.setProcessStatusListener(() -> {
        registerProcessStatus(startupParams.getInstanceId(), process);
      });
    } catch (Exception e) {
      closeProcess(startupParams.getPathMapping(), startupParams.getInstanceId());
      throw new WsException("Failed to create App instance.", e);
    }
  }

  private void registerProcessStatus(String instanceId, SwingProcess process) {
    ConnectedSwingInstance csi = connector.getConnectedInstanceByInstanceId(instanceId);

    if (csi == null) {
      return;
    }

    ProcessStatusEnum processStatus = ProcessStatusEnum.FINISHED;

    if (process.isRunning()) {
      processStatus = ProcessStatusEnum.RUNNING;
    } else if (process.isForceKilled()) {
      processStatus = ProcessStatusEnum.FORCE_KILLED;
    }

    csi.updateProcessStatus(processStatus);
  }

  private String getWebsocketUrl(String pathMapping) {
    String url = System.getProperty(Constants.SERVER_WEBSOCKET_URL);
    if (url.endsWith("/")) {
      url = url.substring(0, url.length() - 1);
    }
    return url + pathMapping + "/async/app-bin";
  }

  private SwingConfig getSwingConfig(String path) {
    return configService.getConfiguration(path);
  }

  private void closeProcess(String path, String instanceId) {
    try {
      connector.instanceClosed(path, instanceId);
    } catch (Throwable e) {
      log.error("Unexpected error while closing instance", e);
    }

    processService.closeProcess(instanceId);
  }

  public byte[] getAppConfig(String path) throws Exception {
    MetaObject config = configService.describeConfiguration(path, null, getConfigContext(path));
    JsonMapper mapper = new JsonMapper();
    return mapper.writeValueAsBytes(config);
  }

  @SuppressWarnings("unchecked")
  public byte[] getAppMeta(String path, byte[] configBytes) throws Exception {
    JsonMapper mapper = new JsonMapper();
    Map<String, Object> meta =
        mapper.readValue(configBytes, new TypeReference<Map<String, Object>>() {});
    if (meta != null && meta.get("data") != null) {
      MetaObject config = configService.describeConfiguration(path,
          (Map<String, Object>) meta.get("data"), getConfigContext(path));
      return mapper.writeValueAsBytes(config);
    } else {
      throw new IllegalArgumentException("Could not find meta data!");
    }
  }

  @SuppressWarnings("unchecked")
  public void saveConfig(String path, byte[] configBytes) throws Exception {
    JsonMapper mapper = new JsonMapper();
    Map<String, Object> config =
        mapper.readValue(configBytes, new TypeReference<Map<String, Object>>() {});
    if (config != null && config.get("data") != null) {
      configService.setConfiguration(path, (Map<String, Object>) config.get("data"));
    } else {
      throw new IllegalArgumentException("Could not find config data!");
    }
  }

  private ConfigContext getConfigContext(String path) {
    SwingConfig config = getSwingConfig(path);
    if (config == null) {
      return null;
    }

    return getConfigContext(path, config, getDefaultVariableSubstitutor(config, null));
  }

  private ConfigContext getConfigContext(String path, SwingConfig config,
      VariableSubstitutor subs) {
    return new ConfigContext() {
      @Override
      public File resolveFile(String name) {
        String home = subs.replace(config.getHomeDir());
        return CommonUtil.resolveFile(name, home, subs);
      }

      @Override
      public String replaceVariables(String string) {
        return subs.replace(string);
      }
    };
  }

  public void kill(String instanceId, int delayMs) {
    processService.kill(instanceId, delayMs);
  }

  public void killAll(String path) {
    processService.killAll(path);
  }

  public String resolveConfig(String path, String user, String resolve) {
    return getDefaultVariableSubstitutor(getSwingConfig(path), user).replace(resolve);
  }

  public Map<String, String> searchVariables(String path, String user, String search) {
    return getDefaultVariableSubstitutor(getSwingConfig(path), user).searchVariables(search);
  }

  public List<String> getPaths() {
    return configService.getPaths();
  }

  private VariableSubstitutor getDefaultVariableSubstitutor(SwingConfig config, String user) {
    String userName = user == null ? "<webswing user>" : user;
    return VariableSubstitutor.forSwingInstance(config, userName, null, "<webswing client Id>",
        "<webswing client IP address>", "<webswing client locale>", "<webswing client timezone>",
        "<webswing custom args>");
  }

}
