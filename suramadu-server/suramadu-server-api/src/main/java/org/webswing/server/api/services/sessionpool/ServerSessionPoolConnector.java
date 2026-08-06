package org.webswing.server.api.services.sessionpool;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.model.adminconsole.in.ManageSessionPoolMsgIn;
import org.webswing.model.adminconsole.in.RecordingRequestMsgIn;
import org.webswing.model.adminconsole.out.*;
import org.webswing.model.app.in.ServerToAppFrameMsgIn;
import org.webswing.model.app.in.ThreadDumpRequestMsgIn;
import org.webswing.model.common.in.ConnectionHandshakeMsgIn;
import org.webswing.model.common.in.ParamMsgIn;
import org.webswing.model.common.in.SimpleEventMsgIn;
import org.webswing.model.common.in.SimpleEventMsgIn.SimpleEventType;
import org.webswing.server.api.services.stat.StatisticsLoggerService;
import org.webswing.server.api.services.swinginstance.ConnectedSwingInstance;
import org.webswing.server.api.services.swinginstance.SwingInstanceFactory;
import org.webswing.server.api.services.swinginstance.SwingInstanceInfo;
import org.webswing.server.api.services.swinginstance.holder.SwingInstanceHolder;
import org.webswing.server.api.services.swinginstance.holder.SwingInstanceHolderFactory;
import org.webswing.server.api.services.websocket.ApplicationWebSocketConnection;
import org.webswing.server.api.services.websocket.PrimaryWebSocketConnection;
import org.webswing.server.api.services.websocket.WebSocketUserInfo;
import org.webswing.server.api.util.ServerApiUtil;
import org.webswing.server.common.datastore.WebswingDataStoreConfig;
import org.webswing.server.common.datastore.WebswingDataStoreModule;
import org.webswing.server.common.model.SecuredPathConfig;
import org.webswing.server.common.model.SecuredPathConfig.SessionMode;
import org.webswing.server.common.service.security.AbstractWebswingUser;
import org.webswing.server.common.service.stats.StatisticsLogger;
import org.webswing.server.common.service.stats.logger.InstanceStats;
import org.webswing.server.common.service.swingprocess.ProcessStartupParams;
import org.webswing.server.common.util.WebswingObjectMapper;
import org.webswing.server.model.exception.WsException;
import tools.jackson.core.JacksonException;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public abstract class ServerSessionPoolConnector {

  private static final Logger log = LoggerFactory.getLogger(ServerSessionPoolConnector.class);

  protected Map<String, SwingInstanceHolder> instanceHolders = new HashMap<>();
  protected Map<String, StatisticsLogger> instanceStats = new HashMap<>();

  protected SwingInstanceFactory swingInstanceFactory;
  protected SwingInstanceHolderFactory swingInstanceHolderService;
  protected StatisticsLoggerService loggerService;
  protected SessionPoolHolderService sessionPoolHolderService;

  public ServerSessionPoolConnector(SwingInstanceFactory swingInstanceFactory,
      SwingInstanceHolderFactory swingInstanceHolderService, StatisticsLoggerService loggerService,
      SessionPoolHolderService sessionPoolHolderService) {
    super();
    this.swingInstanceFactory = swingInstanceFactory;
    this.swingInstanceHolderService = swingInstanceHolderService;
    this.loggerService = loggerService;
    this.sessionPoolHolderService = sessionPoolHolderService;
  }

  /**
   * unique identifier of a session pool
   */
  public String getId() {
    return "default-pool";
  }

  /**
   * priority of this session pool
   */
  public int getPriority() {
    return 1;
  }

  /**
   * overall max instances in session pool
   */
  public int getMaxInstances() {
    return -1;
  }

  /**
   * actual overall count of instances in session pool, connected or not connected to this server
   */
  public final int getTotalInstancesInSessionPool() {
    return getInstancesRunningInSessionPool(null);
  }

  /**
   * instances that are running in session pool, may or may not be connected to a browser
   */
  public int getInstancesRunningInSessionPool(String path) {
    return getRunningConnectedInstances(path);
  }

  /**
   * instances that are running in session pool, connected to a browser
   */
  public int getInstancesRunningAndConnectedInSessionPool(String path) {
    return getConnectedConnectedInstances(path);
  }

  public final int getRunningConnectedInstances(String path) {
    if (path == null) {
      return instanceHolders.values().stream()
          .mapToInt(SwingInstanceHolder::getRunningInstacesCount).sum();
    }

    if (!instanceHolders.containsKey(path)) {
      return 0;
    }
    return instanceHolders.get(path).getRunningInstacesCount();
  }

  public final int getConnectedConnectedInstances(String path) {
    if (path == null) {
      return instanceHolders.values().stream()
          .mapToInt(SwingInstanceHolder::getConnectedInstancesCount).sum();
    }

    if (!instanceHolders.containsKey(path)) {
      return 0;
    }
    return instanceHolders.get(path).getConnectedInstancesCount();
  }

  public final int getClosedConnectedInstances(String path) {
    if (path == null) {
      return instanceHolders.values().stream().mapToInt(SwingInstanceHolder::getClosedInstacesCount)
          .sum();
    }

    if (!instanceHolders.containsKey(path)) {
      return 0;
    }
    return instanceHolders.get(path).getClosedInstacesCount();
  }

  public final List<ConnectedSwingInstance> getAllConnectedInstances() {
    List<ConnectedSwingInstance> instances = new ArrayList<>();
    instanceHolders.values().stream().forEach(ih -> instances.addAll(ih.getAllInstances()));
    return instances;
  }

  // used only by AppPathHandlerImpl.connectApplication, this returns swing instance connected to
  // the current server
  public final ConnectedSwingInstance getConnectedInstanceByInstanceId(String instanceId) {
    for (SwingInstanceHolder ih : instanceHolders.values()) {
      ConnectedSwingInstance instance = ih.findInstanceByInstanceId(instanceId);
      if (instance != null) {
        return instance;
      }
    }
    return null;
  }

  protected final ConnectedSwingInstance getClosedConnectedInstanceByInstanceId(String instanceId) {
    for (SwingInstanceHolder ih : instanceHolders.values()) {
      ConnectedSwingInstance instance = ih.findClosedInstanceByInstanceId(instanceId);
      if (instance != null) {
        return instance;
      }
    }
    return null;
  }

  public boolean hasConnectedInstanceWithInstanceId(String instanceId) {
    // search for connected instances
    for (SwingInstanceHolder ih : instanceHolders.values()) {
      if (ih.findInstanceByInstanceId(instanceId) != null) {
        return true;
      }
    }
    return false;
  }

  public final boolean accepts(String path) {
    if (!canCreateNewInstance()) {
      return false;
    }

    if (!acceptsPath(path)) {
      return false;
    }

    return true;
  }

  public final void removeConnectedSwingInstance(ConnectedSwingInstance instance, boolean force) {
    String path = instance.getPathMapping();

    if (instanceHolders.containsKey(path)) {
      instanceHolders.get(path).remove(instance, force);
    }

    if (instanceStats.containsKey(path)) {
      instanceStats.get(path).removeInstance(instance.getInstanceId());
    }

    sessionPoolHolderService.unregisterWithAdminConsole(path, instance.getInstanceId());
  }

  public boolean tryConnectSwingInstance(String path, PrimaryWebSocketConnection r,
      ConnectionHandshakeMsgIn handshake, SessionMode sessionMode, boolean stealSessionAllowed)
      throws WsException {
    if (!instanceHolders.containsKey(path)) {
      return false;
    }

    ConnectedSwingInstance instance = null;

    if (handshake.isMirrored()) {
      // mirror connects from admin console to a correct cluster server
      // if a mirror handshake ends up here it is an error
      throw new WsException("Direct mirror connection is not allowed!");
    } else {
      if (handshake.getInstanceId() != null) {
        instance = instanceHolders.get(path).findInstanceByInstanceId(handshake.getInstanceId());
      } else {
        String ownerId = ServerApiUtil.resolveOwnerIdForSessionMode(r, handshake, sessionMode);
        instance = instanceHolders.get(path).findInstanceByOwnerId(ownerId);
      }
    }

    if (instance == null) {
      return false;
    }

    instance.connectBrowser(r, handshake);
    return true;
  }

  public final void createSwingInstance(String path, PrimaryWebSocketConnection r,
      ConnectionHandshakeMsgIn handshake, SwingInstanceInfo instanceInfo) throws WsException {
    SecuredPathConfig config = instanceInfo.getConfig();

    String ownerId = ServerApiUtil.resolveOwnerIdForSessionMode(r, handshake, config);
    String instanceId = ServerApiUtil.generateInstanceId(r, handshake, path);

    instanceInfo.setSessionPoolId(getId());
    instanceInfo.setOwnerId(ownerId);
    instanceInfo.setInstanceId(instanceId);

    ConnectedSwingInstance instance = swingInstanceFactory.create(r, handshake, instanceInfo, this);

    // first register instance

    if (!instanceHolders.containsKey(path)) {
      instanceHolders.put(path, swingInstanceHolderService.createInstanceHolder());
    }
    if (!instanceStats.containsKey(path)) {
      instanceStats.put(path, loggerService.createLogger());
    }
    instanceHolders.get(path).add(instance);

    ProcessStartupParams startupParams = new ProcessStartupParams();
    startupParams.setInstanceId(instanceId);
    startupParams.setPathMapping(path);
    startupParams.setAppName(instance.getAppName());
    startupParams.setDebugPort(r.getUserInfo().getDebugPort());
    startupParams.setRecordingFlagged(r.isRecordingFlagged());
    startupParams.setAskForRecordingNeeded(instance.getConfig().isRecordingConsentRequired());
    startupParams.setAskForMirroringNeeded(instance.getConfig().isMirroringConsentRequired());
    startupParams.setScreenWidth(handshake.getDesktopWidth());
    startupParams.setScreenHeight(handshake.getDesktopHeight());
    startupParams.setParams(convertHandshakeParams(handshake.getParams()));
    startupParams.setDocumentBase(handshake.getDocumentBase());
    startupParams.setHandshakeUrl(handshake.getUrl());
    startupParams.setDirectDrawSupported(handshake.isDirectDrawSupported());
    startupParams.setAccessiblityEnabled(handshake.isAccessiblityEnabled());
    startupParams.setTouchModeEnabled(handshake.isTouchMode());
    startupParams.setDockingSupported(handshake.isDockingSupported());
    startupParams.setDataStoreConfig(serializeDataStoreConfig(instanceInfo.getDataStoreConfig()));
    startupParams.setUserId(r.getUserId());

    // now start the process
    AbstractWebswingUser user = r.getUser();
    WebSocketUserInfo info = r.getUserInfo();

    createProcess(instance.getConnectionId(), instance.getOwnerId(), user.getUserId(),
        convertUserAttributes(user.getUserAttributes()), info.getUserIp(), handshake.getLocale(),
        handshake.getTimeZone(), info.getCustomArgs(), startupParams);

    sessionPoolHolderService.registerWithAdminConsole(path, instanceId);

    instance.notifyUserConnected();
  }

  private String serializeDataStoreConfig(WebswingDataStoreConfig dataStoreConfig) {
    String configString = null;
    try {
      String serialized = WebswingObjectMapper.get().writeValueAsString(dataStoreConfig);
      configString =
          new String(Base64.getEncoder().encode(serialized.getBytes(StandardCharsets.UTF_8)),
              StandardCharsets.UTF_8);
    } catch (JacksonException e) {
      log.error("Error while serializing dataStore configuration!", e);
    }
    return configString;
  }

  private Map<String, String> convertHandshakeParams(List<ParamMsgIn> params) {
    if (params == null) {
      return null;
    }

    Map<String, String> map = new HashMap<>();

    for (ParamMsgIn param : params) {
      map.put(param.getName(), param.getValue());
    }

    return map;
  }

  private Map<String, String> convertUserAttributes(Map<String, Serializable> userAttributes) {
    if (userAttributes == null) {
      return null;
    }

    Map<String, String> map = new HashMap<>();

    for (Entry<String, Serializable> entry : userAttributes.entrySet()) {
      if (entry.getValue() != null) {
        map.put(entry.getKey(), entry.getValue().toString());
      }
    }

    return map;
  }

  public void notifyUserConnected(ConnectedSwingInstance swingInstance) {
    // remote session pool connector to override this and notify session pool, which notifies all
    // cluster servers
  }

  public void notifyUserDisconnected(ConnectedSwingInstance swingInstance) {
    // remote session pool connector to override this and notify session pool, which notifies all
    // cluster servers
  }

  public void instanceReconnecting(ConnectedSwingInstance swingInstance) {
    // remote session pool connector to override this, force remove instance from instance holder
    // instance cannot reconnect in community server
  }

  public void reconnectInstance(String instanceId, ApplicationWebSocketConnection connection,
      PrimaryWebSocketConnection browser, ConnectionHandshakeMsgIn handshake,
      SwingInstanceInfo instanceInfo) throws WsException {
    // remote session pool connector to override this and finalize the reconnect process
    // instance cannot reconnect in community server
  }

  public final void requestConnectedInstanceThreadDump(String path, String instanceId) {
    ConnectedSwingInstance instance = getConnectedInstanceByInstanceId(path, instanceId);

    if (instance == null) {
      return;
    }

    requestThreadDumpForInstance(instance);
  }

  public final void requestRecordingForConnectedInstance(
      RecordingRequestMsgIn.RecordingRequestType recordingRequestType, String path,
      String instanceId) throws WsException {
    ConnectedSwingInstance instance = getConnectedInstanceByInstanceId(path, instanceId);

    if (instance == null) {
      return;
    }

    switch (recordingRequestType) {
      case startRecording:
        instance.startRecording();
        break;
      case stopRecording:
        instance.stopRecording();
        break;
    }
  }

  public final void toggleStatisticsLoggingForConnectedInstance(String path, String instanceId,
      Boolean enabled) throws WsException {
    ConnectedSwingInstance instance = getConnectedInstanceByInstanceId(path, instanceId);

    if (instance == null) {
      return;
    }

    instance.toggleStatisticsLogging(enabled);
  }

  public final ThreadDumpMsgOut tryFindThreadDumpByInstanceIdIncludeClosed(@Nullable String path,
      String instanceId, String timestamp) {
    if (path != null) {
      if (!instanceHolders.containsKey(path)) {
        return null;
      }

      ConnectedSwingInstance instance =
          instanceHolders.get(path).findInstanceByInstanceId(instanceId);

      if (instance != null) {
        return instance.getThreadDump(timestamp);
      } else {
        ThreadDumpMsgOut dump = searchInstanceHolderClosedInstancesForThreadDump(
            instanceHolders.get(path), instanceId, timestamp);
        if (dump != null) {
          return dump;
        }
      }
    } else {
      // we don't have a path, we need to look through all instance holders
      for (SwingInstanceHolder sih : instanceHolders.values()) {
        ConnectedSwingInstance instance = sih.findInstanceByInstanceId(instanceId);
        if (instance != null) {
          return instance.getThreadDump(timestamp);
        }
      }
      // if not found, we still need to check all instance holders for closed instances
      for (SwingInstanceHolder sih : instanceHolders.values()) {
        ThreadDumpMsgOut dump =
            searchInstanceHolderClosedInstancesForThreadDump(sih, instanceId, timestamp);
        if (dump != null) {
          return dump;
        }
      }
    }

    return null;
  }

  private ThreadDumpMsgOut searchInstanceHolderClosedInstancesForThreadDump(SwingInstanceHolder sih,
      String instanceId, String timestamp) {
    for (ConnectedSwingInstance closedInstance : sih.getAllClosedInstances()) {
      // closed instances can have multiple instances for same id, need to manually check all
      if (instanceId.equals(closedInstance.getInstanceId())) {
        ThreadDumpMsgOut dump = closedInstance.getThreadDump(timestamp);
        if (dump != null) {
          return dump;
        }
      }
    }

    return null;
  }

  public final void shutdownConnectedInstance(String path, String instanceId, boolean force)
      throws WsException {
    ConnectedSwingInstance instance = getConnectedInstanceByInstanceId(path, instanceId);

    if (instance == null) {
      throw new WsException("Instance with id " + instanceId + " not found.");
    }

    if (force) {
      kill(instanceId, 0);
    } else {
      shutdown(instance);
    }
  }

  public final List<SwingSessionMsgOut> getRunningConnectedInstanceSessions(String path) {
    if (!instanceHolders.containsKey(path)) {
      return Collections.emptyList();
    }
    return instanceHolders.get(path).getAllInstances().stream()
        .map(instance -> toSwingSession(instance)).collect(Collectors.toList());
  }

  public final List<SwingSessionMsgOut> getClosedConnectedInstanceSessions(String path) {
    if (!instanceHolders.containsKey(path)) {
      return Collections.emptyList();
    }
    return instanceHolders.get(path).getAllClosedInstances().stream()
        .map(instance -> toSwingSession(instance)).collect(Collectors.toList());
  }

  public final void logStatValueForConnectedInstance(String path, String instanceId, String metric,
      Number value) {
    if (!instanceStats.containsKey(path)) {
      return;
    }

    ConnectedSwingInstance instance = getConnectedInstanceByInstanceId(instanceId);
    if (instance == null || !instance.isStatisticsLoggingEnabled()) {
      return;
    }

    instanceStats.get(path).log(instanceId, metric, value);
  }

  public final Collection<InstanceStats> getAllConnectedInstanceStats(String path) {
    if (!instanceStats.containsKey(path)) {
      return Collections.emptyList();
    }
    return instanceStats.get(path).getAllInstanceStats();
  }

  public final Map<String, List<String>> getConnectedInstanceSummaryWarnings(String path) {
    if (!instanceStats.containsKey(path)) {
      return Collections.emptyMap();
    }
    return instanceStats.get(path).getSummaryWarnings();
  }

  public final List<String> getConnectedInstanceWarnings(String path, String instanceId) {
    if (!instanceStats.containsKey(path)) {
      return Collections.emptyList();
    }

    return instanceStats.get(path).getInstanceWarnings(instanceId);
  }

  public final List<String> getConnectedInstanceWarningHistory(String path, String instanceId) {
    if (!instanceStats.containsKey(path)) {
      return Collections.emptyList();
    }

    return instanceStats.get(path).getInstanceWarningHistory(instanceId);
  }

  public final void instanceClosed(String path, String instanceId) throws Exception {
    ConnectedSwingInstance instance = getConnectedInstanceByInstanceId(path, instanceId);
    if (instance != null) {
      // connected to this server
      instance.close();
    }
  }

  public final WebswingDataStoreModule getDataStore(String path) {
    return sessionPoolHolderService.getDataStore(path);
  }

  private Map<String, Number> getConnectedInstanceMetrics(String path, String instanceId) {
    if (!instanceStats.containsKey(path)) {
      return Collections.emptyMap();
    }

    return instanceStats.get(path).getInstanceMetrics(instanceId);
  }

  private Map<String, Map<Long, Number>> getConnectedInstanceStats(String path, String instanceId) {
    if (!instanceStats.containsKey(path)) {
      return Collections.emptyMap();
    }

    return instanceStats.get(path).getInstanceStats(instanceId);
  }

  protected final ConnectedSwingInstance getConnectedInstanceByInstanceId(String path,
      String instanceId) {
    if (!instanceHolders.containsKey(path)) {
      return null;
    }

    return instanceHolders.get(path).findInstanceByInstanceId(instanceId);
  }

  private boolean canCreateNewInstance() {
    return getMaxInstances() < 0 || getTotalInstancesInSessionPool() < getMaxInstances();
  }

  private void shutdown(ConnectedSwingInstance instance) {
    ServerToAppFrameMsgIn msgIn = new ServerToAppFrameMsgIn();
    SimpleEventMsgIn simpleEventMsgIn = new SimpleEventMsgIn(SimpleEventType.killSwingAdmin);
    msgIn.setEvents(Lists.newArrayList(simpleEventMsgIn));
    instance.sendMessageToApp(msgIn);
  }

  private void requestThreadDumpForInstance(ConnectedSwingInstance instance) {
    ServerToAppFrameMsgIn frame = new ServerToAppFrameMsgIn();
    frame.setThreadDumpRequest(new ThreadDumpRequestMsgIn());
    instance.sendMessageToApp(frame);
  }

  private SwingSessionMsgOut toSwingSession(ConnectedSwingInstance instance) {
    SwingSessionMsgOut session = instance.toSwingSession();

    List<MetricMsgOut> metricsMsg = new ArrayList<>();
    Map<String, Number> metrics =
        getConnectedInstanceMetrics(instance.getPathMapping(), instance.getInstanceId());
    if (metrics != null) {
      for (Entry<String, Number> metric : metrics.entrySet()) {
        metricsMsg.add(new MetricMsgOut(metric.getKey(), metric.getValue().longValue(), 1));
      }
    }

    List<StatEntryMsgOut> statsMsg = new ArrayList<>();
    Map<String, Map<Long, Number>> stats =
        getConnectedInstanceStats(instance.getPathMapping(), instance.getInstanceId());
    if (stats != null) {
      for (Entry<String, Map<Long, Number>> entry : stats.entrySet()) {
        List<MetricMsgOut> statMetrics = new ArrayList<>();
        for (Entry<Long, Number> metricEntry : entry.getValue().entrySet()) {
          statMetrics.add(new MetricMsgOut(metricEntry.getKey().toString(),
              metricEntry.getValue().longValue(), 1));
        }
        statsMsg.add(new StatEntryMsgOut(entry.getKey(), statMetrics));
      }
    }

    session.setWarnings(
        getConnectedInstanceWarnings(instance.getPathMapping(), instance.getInstanceId()));
    session.setMetrics(metricsMsg);
    session.setStats(statsMsg);

    return session;
  }

  protected abstract void createProcess(String connectionId, String ownerId, String userId,
      Map<String, String> userAttributes, String userIp, String userLocale, String userTimeZone,
      String customArgs, ProcessStartupParams startupParams) throws WsException;

  protected abstract boolean acceptsPath(String path);

  public abstract void kill(String instanceId, int delayMs);

  public abstract void killAll(String path);

  public abstract byte[] getAppMeta(String path, byte[] config) throws Exception;

  public abstract byte[] getAppConfig(String path) throws Exception;

  public abstract void saveConfig(String path, byte[] config) throws Exception;

  public abstract SessionPoolInfoMsgOut getSessionPoolInfoMsg();

  public abstract boolean isCluster();

  public abstract String resolveConfig(String path, String user, String resolve) throws Exception;

  public abstract Map<String, String> searchVariables(String path, String user, String search)
      throws Exception;

  public abstract void handleManageSessionPool(ManageSessionPoolMsgIn manageSessionPool);

  public abstract boolean isDrainMode();
}
