package org.webswing.server.services.sessionpool.impl;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import org.webswing.Constants;
import org.webswing.model.adminconsole.in.ManageSessionPoolMsgIn;
import org.webswing.model.adminconsole.out.SessionPoolAppMsgOut;
import org.webswing.model.adminconsole.out.SessionPoolInfoMsgOut;
import org.webswing.server.api.services.sessionpool.ServerSessionPoolConnector;
import org.webswing.server.api.services.sessionpool.SessionPoolHolderService;
import org.webswing.server.api.services.stat.StatisticsLoggerService;
import org.webswing.server.api.services.swinginstance.SwingInstanceFactory;
import org.webswing.server.api.services.swinginstance.holder.SwingInstanceHolderFactory;
import org.webswing.server.common.model.SwingConfig;
import org.webswing.server.common.service.config.ConfigurationService;
import org.webswing.server.common.service.swingprocess.ProcessStartupParams;
import org.webswing.server.model.exception.WsException;
import org.webswing.server.model.exception.WsInitException;
import org.webswing.server.services.config.impl.LocalSessionPoolConfigurationServiceImpl;
import org.webswing.sessionpool.api.base.SessionPoolService;
import org.webswing.sessionpool.api.service.startup.SessionPoolStartupService;
import org.webswing.sessionpool.api.service.startup.impl.SessionPoolStartupServiceImpl;
import org.webswing.sessionpool.api.service.swingprocess.SwingProcessService;
import org.webswing.sessionpool.api.service.swingprocess.impl.SwingProcessServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LocalSessionPoolConnector extends ServerSessionPoolConnector {

  private LocalSessionPool localSessionPool;
  private SessionPoolStartupService startup;

  @Inject
  public LocalSessionPoolConnector(SwingInstanceFactory swingInstanceFactory,
      SwingInstanceHolderFactory swingInstanceHolderService, StatisticsLoggerService loggerService,
      SessionPoolHolderService sessionPoolHolderService) throws WsInitException {
    super(swingInstanceFactory, swingInstanceHolderService, loggerService,
        sessionPoolHolderService);

    init();
  }

  private void init() throws WsInitException {
    Injector injector = Guice.createInjector(new AbstractModule() {
      @Override
      public void configure() {
        Multibinder<SessionPoolService> serviceBinder =
            Multibinder.newSetBinder(binder(), SessionPoolService.class);

        bind(SessionPoolStartupService.class).to(SessionPoolStartupServiceImpl.class);

        serviceBinder.addBinding().to(SwingProcessServiceImpl.class);
        serviceBinder.addBinding().to(LocalSessionPoolConfigurationServiceImpl.class);

        bind(SwingProcessService.class).to(SwingProcessServiceImpl.class);
        bind(new TypeLiteral<ConfigurationService<SwingConfig>>() {})
            .to(LocalSessionPoolConfigurationServiceImpl.class);
      }
    });

    SwingProcessService processService = injector.getInstance(SwingProcessService.class);
    ConfigurationService<SwingConfig> configService =
        injector.getInstance(new Key<ConfigurationService<SwingConfig>>() {});

    this.startup = injector.getInstance(SessionPoolStartupService.class);
    this.startup.start();

    System.setProperty(Constants.SESSION_POOL_ID, getId());

    localSessionPool = new LocalSessionPool(this, processService, configService);
  }

  @Override
  public boolean isCluster() {
    return false;
  }

  @Override
  public void killAll(String path) {
    localSessionPool.killAll(path);
  }

  @Override
  public void kill(String instanceId, int delayMs) {
    localSessionPool.kill(instanceId, delayMs);
  }

  @Override
  protected void createProcess(String connectionId, String ownerId, String userId,
      Map<String, String> userAttributes, String userIp, String userLocale, String userTimeZone,
      String customArgs, ProcessStartupParams startupParams) throws WsException {
    localSessionPool.createProcess(userId, userAttributes, userIp, userLocale, userTimeZone,
        customArgs, startupParams);
  }

  @Override
  protected boolean acceptsPath(String path) {
    // local session pool accepts all paths
    return true;
  }

  @Override
  public byte[] getAppConfig(String path) throws Exception {
    return localSessionPool.getAppConfig(path);
  }

  @Override
  public byte[] getAppMeta(String path, byte[] config) throws Exception {
    return localSessionPool.getAppMeta(path, config);
  }

  @Override
  public void saveConfig(String path, byte[] config) throws Exception {
    localSessionPool.saveConfig(path, config);
  }

  @Override
  public String resolveConfig(String path, String user, String resolve) {
    return localSessionPool.resolveConfig(path, user, resolve);
  }

  @Override
  public Map<String, String> searchVariables(String path, String user, String search)
      throws Exception {
    return localSessionPool.searchVariables(path, user, search);
  }

  @Override
  public SessionPoolInfoMsgOut getSessionPoolInfoMsg() {
    List<String> connectedServers = new ArrayList<>();
    connectedServers.add(System.getProperty(Constants.WEBSWING_SERVER_ID));

    // in local session pool all instances are connected to this server and session pool
    List<SessionPoolAppMsgOut> appInstances = new ArrayList<>();
    for (String path : localSessionPool.getPaths()) {
      if ("/".equals(path)) {
        continue;
      }
      int instances = !instanceHolders.containsKey(path) ? 0
          : instanceHolders.get(path).getRunningInstacesCount();
      appInstances.add(new SessionPoolAppMsgOut(path, instances));
    }

    return new SessionPoolInfoMsgOut(getId(), getMaxInstances(), getPriority(), false,
        connectedServers, appInstances);
  }


  public void destroy() {
    if (this.startup != null) {
      this.startup.stop();
    }
  }

  @Override
  public void handleManageSessionPool(ManageSessionPoolMsgIn manageSessionPool) {
    // nothing
  }

  @Override
  public boolean isDrainMode() {
    return false;
  }
}
