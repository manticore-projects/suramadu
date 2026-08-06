package org.webswing.server.api.services.sessionpool.impl;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.Constants;
import org.webswing.model.adminconsole.in.*;
import org.webswing.model.adminconsole.out.*;
import org.webswing.model.appframe.out.SimpleEventMsgOut;
import org.webswing.model.common.in.ConnectionHandshakeMsgIn;
import org.webswing.server.api.GlobalUrlHandler;
import org.webswing.server.api.base.PrimaryUrlHandler;
import org.webswing.server.api.model.InstanceManagerStatus;
import org.webswing.server.api.services.application.AppPathHandler;
import org.webswing.server.api.services.security.login.SecuredPathHandler;
import org.webswing.server.api.services.sessionpool.ServerSessionPoolConnector;
import org.webswing.server.api.services.sessionpool.SessionPoolHolderService;
import org.webswing.server.api.services.sessionpool.loadbalance.LoadBalanceResolver;
import org.webswing.server.api.services.sessionpool.loadbalance.RoundRobinLoadBalanceResolver;
import org.webswing.server.api.services.swinginstance.ConnectedSwingInstance;
import org.webswing.server.api.services.swinginstance.SwingInstanceInfo;
import org.webswing.server.api.services.websocket.*;
import org.webswing.server.api.services.websocket.impl.AdminConsoleBrowserMirrorWebSocketConnectionImpl;
import org.webswing.server.common.datastore.WebswingDataStoreModule;
import org.webswing.server.common.model.SecuredPathConfig;
import org.webswing.server.common.model.SecuredPathConfig.SessionMode;
import org.webswing.server.common.model.meta.VariableSetName;
import org.webswing.server.common.model.security.WebswingAction;
import org.webswing.server.common.service.security.impl.WebswingSecuritySubject;
import org.webswing.server.common.service.stats.logger.InstanceStats;
import org.webswing.server.common.util.JwtUtil;
import org.webswing.server.common.util.LoggerStatisticsUtil;
import org.webswing.server.common.util.VariableSubstitutor;
import org.webswing.server.model.exception.WsException;
import org.webswing.server.model.exception.WsInitException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Singleton
public class SessionPoolHolderServiceImpl implements SessionPoolHolderService {

  private static final Logger log = LoggerFactory.getLogger(SessionPoolHolderServiceImpl.class);

  private static final long SYNC_MESSAGE_TIMEOUT = TimeUnit.SECONDS.toMillis(3);

  private final GlobalUrlHandler globalHandler;
  private final WebSocketService webSocketService;

  private LoadBalanceResolver loadBalanceResolver = new RoundRobinLoadBalanceResolver();

  private Map<String, ServerSessionPoolConnector> sessionPools =
      Collections.synchronizedMap(new HashMap<>());
  private AdminConsoleWebSocketConnection adminConsole;

  private Map<String, PrimaryWebSocketConnection> reconnectWaiting =
      Collections.synchronizedMap(new HashMap<>());

  @Inject
  public SessionPoolHolderServiceImpl(GlobalUrlHandler globalHandler,
      WebSocketService webSocketService) {
    this.globalHandler = globalHandler;
    this.webSocketService = webSocketService;
  }

  @Override
  public void start() throws WsInitException {}

  @Override
  public void stop() {
    synchronized (sessionPools) {
      sessionPools.clear();
    }
  }

  @Override
  public void registerSessionPool(ServerSessionPoolConnector sessionPool) {
    synchronized (sessionPools) {
      if (sessionPools.containsKey(sessionPool.getId())) {
        throw new IllegalArgumentException("Session pool already registered!");
      }
      sessionPools.put(sessionPool.getId(), sessionPool);
    }
    loadBalanceResolver.registerSessionPool(sessionPool);
  }

  @Override
  public void unregisterSessionPool(ServerSessionPoolConnector sessionPool) {
    synchronized (sessionPools) {
      sessionPools.remove(sessionPool.getId());
    }
    loadBalanceResolver.unregisterSessionPool(sessionPool);
  }

  @Override
  public boolean registerAdminConsole(AdminConsoleWebSocketConnection connection) {
    if (adminConsole == null) {
      adminConsole = connection;

      List<RegisterInstanceMsgOut> registerInstances = new ArrayList<>();
      for (ServerSessionPoolConnector pool : sessionPools.values()) {
        pool.getAllConnectedInstances().forEach(instance -> registerInstances.add(
            new RegisterInstanceMsgOut(instance.getPathMapping(), instance.getInstanceId(), true)));
      }
      if (!registerInstances.isEmpty()) {
        registerInstancesWithAdminConsole(registerInstances);
      }
      return true;
    }

    return false;
  }

  @Override
  public void unregisterAdminConsole(AdminConsoleWebSocketConnection connection) {
    adminConsole = null;

    // disconnect all mirror connections
    for (ServerSessionPoolConnector pool : sessionPools.values()) {
      pool.getAllConnectedInstances()
          .forEach(instance -> instance.disconnectMirroredWebSession(false));
    }
  }

  @Override
  public void handleAdminConsoleMessage(AdminConsoleFrameMsgIn frame,
      AdminConsoleWebSocketConnection connection) {
    try {
      if (frame.getRecordingRequest() != null) {
        RecordingRequestMsgIn recordingRequestMsg = frame.getRecordingRequest();
        requestRecordingByInstanceId(recordingRequestMsg.getType(), recordingRequestMsg.getPath(),
            recordingRequestMsg.getInstanceId());
      } else if (frame.getToggleStatisticsLogging() != null) {
        ToggleStatisticsLoggingMsgIn toggleStatisticsLoggingMsg =
            frame.getToggleStatisticsLogging();
        toggleStatisticsLoggingByInstanceId(toggleStatisticsLoggingMsg.getPath(),
            toggleStatisticsLoggingMsg.getInstanceId(), toggleStatisticsLoggingMsg.getEnabled());
      } else if (frame.getShutdown() != null) {
        ShutdownMsgIn shutdownMsg = frame.getShutdown();
        shutdownInstance(shutdownMsg.getPath(), shutdownMsg.getInstanceId(), shutdownMsg.isForce());
      } else if (frame.getRequestThreadDump() != null) {
        RequestThreadDumpMsgIn requestThreadDumpMsg = frame.getRequestThreadDump();
        requestInstanceThreadDump(requestThreadDumpMsg.getPath(),
            requestThreadDumpMsg.getInstanceId());
      } else if (frame.getGetThreadDump() != null) {
        GetThreadDumpMsgIn msgIn = frame.getGetThreadDump();

        AdminConsoleFrameMsgOut msgOut = new AdminConsoleFrameMsgOut();
        ThreadDumpMsgOut threadDump = findThreadDumpByInstanceId(msgIn.getPath(),
            msgIn.getInstanceId(), msgIn.getTimestamp());
        if (threadDump != null) {
          threadDump.setCorrelationId(msgIn.getCorrelationId());
        }
        msgOut.setThreadDump(threadDump);

        connection.sendMessage(msgOut);
      } else if (frame.getGetSwingSessions() != null) {
        GetSwingSessionsMsgIn msgIn = frame.getGetSwingSessions();

        AdminConsoleFrameMsgOut msgOut = new AdminConsoleFrameMsgOut();
        msgOut.setPath(msgIn.getPath());
        SwingSessionsMsgOut swingSessions = getSwingSessions(msgIn.getPath());
        swingSessions.setCorrelationId(msgIn.getCorrelationId());
        msgOut.setSwingSessions(swingSessions);

        connection.sendMessage(msgOut);
      } else if (frame.getGetInstanceCountsStatsWarnings() != null) {
        GetInstanceCountsStatsWarningsMsgIn msgIn = frame.getGetInstanceCountsStatsWarnings();

        AdminConsoleFrameMsgOut msgOut = new AdminConsoleFrameMsgOut();
        msgOut.setPath(msgIn.getPath());
        InstanceCountsStatsWarningsMsgOut icsw = getInstanceCountsStatsWarnings(msgIn.getPath());
        icsw.setCorrelationId(msgIn.getCorrelationId());
        msgOut.setInstanceCountsStatsWarnings(icsw);

        connection.sendMessage(msgOut);
      } else if (frame.getGetServerInfo() != null) {
        sendServerInfoUpdate();
      } else if (frame.getGetConfig() != null) {
        GetConfigMsgIn getConfig = frame.getGetConfig();
        try {
          String serverError = null;
          byte[] serverConfig = null;
          try {
            serverConfig = globalHandler.getConfigBytes(getConfig.getPath());
          } catch (Exception e) {
            log.error("Error getting server configuration for path [{}]", getConfig.getPath(), e);
            serverError = ExceptionUtils.getMessage(e);
          }

          AdminConsoleFrameMsgOut msgOut = new AdminConsoleFrameMsgOut();
          msgOut.setConfig(new ConfigMsgOut(serverConfig, serverError,
              isRootPath(getConfig.getPath()) ? null : getAppConfigs(getConfig.getPath()),
              getConfig.getCorrelationId()));
          connection.sendMessage(msgOut);
        } catch (Exception e) {
          log.error("Error getting configuration for path [{}]!", getConfig.getPath(), e);
        }
      } else if (frame.getGetMeta() != null) {
        GetMetaMsgIn getMeta = frame.getGetMeta();
        try {
          byte[] serverConfig = null;
          String serverError = null;
          if (getMeta.getServerConfig() != null) {
            try {
              serverConfig =
                  globalHandler.getMetaBytes(getMeta.getPath(), getMeta.getServerConfig());
            } catch (Exception e) {
              log.error("Error getting server meta configuration for path [{}]", getMeta.getPath(),
                  e);
              serverError = ExceptionUtils.getMessage(e);
            }
          }

          List<AppConfigMsgOut> appConfigs = null;
          if (!isRootPath(getMeta.getPath()) && getMeta.getAppConfigs() != null) {
            Map<String, byte[]> metaAppConfigs = new HashMap<>();
            for (AppConfigMsgIn appMetaIn : getMeta.getAppConfigs()) {
              metaAppConfigs.put(appMetaIn.getSessionPoolId(), appMetaIn.getAppConfig());
            }
            appConfigs = getAppMetas(getMeta.getPath(), metaAppConfigs);
          }

          AdminConsoleFrameMsgOut msgOut = new AdminConsoleFrameMsgOut();
          msgOut.setMeta(
              new MetaMsgOut(serverConfig, serverError, appConfigs, getMeta.getCorrelationId()));
          connection.sendMessage(msgOut);
        } catch (Exception e) {
          log.error("Error getting meta configuration for path [{}]!", getMeta.getPath(), e);
        }
      } else if (frame.getSaveConfig() != null) {
        SaveConfigMsgIn saveConfig = frame.getSaveConfig();
        try {
          boolean serverResult = true;
          String serverError = null;
          if (saveConfig.getServerConfig() != null) {
            try {
              globalHandler.saveConfig(saveConfig.getPath(), saveConfig.getServerConfig());
            } catch (Exception e) {
              log.error("Error while saving server configuration for path [{}]",
                  saveConfig.getPath(), e);
              serverResult = false;
              serverError = ExceptionUtils.getStackTrace(e);
            }
          }

          List<SaveConfigAppResultMsgOut> appResults = null;
          if (saveConfig.isSaveAppConfigs()) {
            if (!isRootPath(saveConfig.getPath())) {
              appResults = new ArrayList<>();
              Map<String, byte[]> appConfigs = new HashMap<>();
              if (saveConfig.getAppConfigs() != null) {
                for (AppConfigMsgIn appConfigIn : saveConfig.getAppConfigs()) {
                  appConfigs.put(appConfigIn.getSessionPoolId(), appConfigIn.getAppConfig());
                }
              }
              saveAppConfigs(saveConfig.getPath(), appConfigs);
            }
          }

          AdminConsoleFrameMsgOut msgOut = new AdminConsoleFrameMsgOut();
          msgOut.setSaveConfigResult(new SaveConfigResultMsgOut(serverResult, serverError,
              appResults, saveConfig.getCorrelationId()));
          connection.sendMessage(msgOut);
        } catch (Exception e) {
          log.error("Error saving configuration for path [{}]!", saveConfig.getPath(), e);
        }
      } else if (frame.getResolveConfig() != null) {
        ResolveConfigMsgIn resolve = frame.getResolveConfig();
        SecuredPathHandler handler = "/".equals(resolve.getPath()) ? globalHandler
            : globalHandler.getAppHandler(resolve.getPath());

        List<MapMsgOut> resolvedList = new ArrayList<>();

        switch (getVariableType(resolve.getType())) {
          case Basic:
          case SwingApp: {
            String resolved =
                getVariableSubstitutorByType(resolve.getType(), resolve.getUser(), handler)
                    .replace(resolve.getResolve());
            resolvedList.add(new MapMsgOut(getServerId(), resolved));
            break;
          }
          case SwingInstance: {
            List<MapMsgOut> appResolved = resolveAppConfig(resolve.getPath(), resolve.getUser(),
                resolve.getResolve(), resolve.getSessionPoolId());
            resolvedList.addAll(appResolved);
            break;
          }
        }

        AdminConsoleFrameMsgOut msgOut = new AdminConsoleFrameMsgOut();
        msgOut.setResolveConfig(new ResolveConfigMsgOut(resolvedList, resolve.getCorrelationId()));
        connection.sendMessage(msgOut);
      } else if (frame.getSearchVariables() != null) {
        SearchVariablesMsgIn searchVariables = frame.getSearchVariables();
        SecuredPathHandler handler = "/".equals(searchVariables.getPath()) ? globalHandler
            : globalHandler.getAppHandler(searchVariables.getPath());

        List<MapMsgOut> variablesList = new ArrayList<>();

        switch (getVariableType(searchVariables.getType())) {
          case Basic:
          case SwingApp: {
            Map<String, String> variables = getVariableSubstitutorByType(searchVariables.getType(),
                searchVariables.getUser(), handler).searchVariables(searchVariables.getSearch());
            if (variables != null) {
              for (Entry<String, String> entry : variables.entrySet()) {
                variablesList.add(new MapMsgOut(entry.getKey(), entry.getValue()));
              }
            }
            break;
          }
          case SwingInstance: {
            List<List<MapMsgOut>> appVariables =
                searchAppVariables(searchVariables.getPath(), searchVariables.getUser(),
                    searchVariables.getSearch(), searchVariables.getSessionPoolId());
            if (appVariables != null) {
              for (List<MapMsgOut> list : appVariables) {
                variablesList.addAll(list);
              }
            }
            break;
          }
        }

        AdminConsoleFrameMsgOut msgOut = new AdminConsoleFrameMsgOut();
        msgOut.setSearchVariables(
            new SearchVariablesMsgOut(variablesList, searchVariables.getCorrelationId()));
        connection.sendMessage(msgOut);
      } else if (frame.getCreateApp() != null) {
        globalHandler.createConfig(frame.getCreateApp().getPath());
      } else if (frame.getRemoveApp() != null) {
        globalHandler.removeConfig(frame.getRemoveApp().getPath());
      } else if (frame.getStartApp() != null) {
        String path = frame.getStartApp().getPath();
        AppPathHandler swingManager = globalHandler.getAppHandler(path);
        if (!swingManager.isEnabled()) {
          swingManager.initConfiguration();
        }
      } else if (frame.getStopApp() != null) {
        String path = frame.getStopApp().getPath();
        AppPathHandler swingManager = globalHandler.getAppHandler(path);
        if (swingManager.isEnabled()) {
          swingManager.disable();
        }
      } else if (frame.getMirrorFrame() != null) {
        MirrorFrameMsgIn mirror = frame.getMirrorFrame();

        ConnectedSwingInstance instance = findInstanceByInstanceId(mirror.getInstanceId());

        if (instance == null) {
          log.error("Could not find instance [{}] for mirror connection message!",
              mirror.getInstanceId());
        } else {
          if (mirror.isConnect()) {
            try {
              MirrorWebSocketConnection mirrorConn =
                  new AdminConsoleBrowserMirrorWebSocketConnectionImpl(this, webSocketService,
                      instance, mirror.getSessionId(), mirror.getToken());
              instance.connectMirroredWebSession(mirrorConn);
            } catch (Exception e) {
              log.error("Error connecting mirror!", e);
            }
          } else if (mirror.isDisconnect()) {
            instance.disconnectMirroredWebSession(mirror.getSessionId(), true);
          } else {
            instance.handleBrowserMirrorMessage(mirror.getFrame());
          }
        }
      } else if (frame.getManageSessionPool() != null) {
        ManageSessionPoolMsgIn manageSessionPool = frame.getManageSessionPool();
        ServerSessionPoolConnector sp = sessionPools.get(manageSessionPool.getSessionPoolId());
        if (sp != null) {
          sp.handleManageSessionPool(manageSessionPool);
        }
      }
    } catch (WsException e) {
      log.error("Error while handling message from admin console!", e);
    }
  }

  @Override
  public boolean issueAdminConsoleAccessToken(String accessId, String acLoginToken,
      String servletPrefix) {
    if (adminConsole == null) {
      log.warn("Cannot issue admin console access token, no connection!");
      return false;
    }

    AdminConsoleFrameMsgOut msgOut = new AdminConsoleFrameMsgOut();
    msgOut.setAccessTokenCreated(
        createAdminConsoleAccessToken(acLoginToken, accessId, servletPrefix));

    adminConsole.sendMessage(msgOut);

    return true;
  }

  @Override
  public AdminConsoleWebSocketConnection getAdminConsoleConnection() {
    return adminConsole;
  }

  private AccessTokenCreatedMsgOut createAdminConsoleAccessToken(String acLoginToken,
      String accessId, String servletPrefix) {
    AccessTokenCreatedMsgOut atcMsg = new AccessTokenCreatedMsgOut();

    if (StringUtils.isBlank(acLoginToken)) {
      return atcMsg;
    }

    Map<String, Object> claims = JwtUtil.parseAdminConsoleLoginTokenClaims(acLoginToken);
    if (claims == null) {
      return atcMsg;
    }

    String webswingClaim = (String) claims.get(Constants.JWT_CLAIM_WEBSWING);

    if (StringUtils.isBlank(webswingClaim)) {
      return atcMsg;
    }

    // replace servlet context path in userMap secured paths

    webswingClaim = WebswingSecuritySubject.fixClaimForAdminConsole(webswingClaim, servletPrefix);

    // copy claims body to new token

    String refreshToken = JwtUtil.createAdminConsoleRefreshToken(webswingClaim);

    atcMsg.setRefreshToken(refreshToken);
    atcMsg.setAccessId(accessId);
    atcMsg.setExpiration(
        System.currentTimeMillis() + Long.getLong(Constants.JWT_ADMIN_CONSOLE_ACCESSID_EXPIRATION,
            Constants.JWT_ADMIN_CONSOLE_ACCESSID_EXPIRATION_DEFAULT));

    return atcMsg;
  }

  private boolean isRootPath(String path) {
    return "/".equals(path);
  }

  @Override
  public void sendServerInfoUpdate() {
    if (adminConsole == null) {
      return;
    }

    AdminConsoleFrameMsgOut msgOut = new AdminConsoleFrameMsgOut();
    List<ApplicationInfoMsgOut> appInfos = new ArrayList<>();

    InstanceManagerStatus globalIMS = globalHandler.getStatus();
    appInfos.add(new ApplicationInfoMsgOut("/", null, null, globalHandler.isEnabled(), 0,
        new InstanceManagerStatusMsgOut(globalIMS.getStatus().name(), globalIMS.getError(),
            globalIMS.getErrorDetails())));

    globalHandler.getApplications().forEach(appHandler -> {
      PrimaryUrlHandler handler = (PrimaryUrlHandler) appHandler;
      InstanceManagerStatus appIMS = handler.getStatus();

      appInfos.add(new ApplicationInfoMsgOut(appHandler.getPathMapping(),
          appHandler.getConfig().getName(), appHandler.getIconAsBytes(), appHandler.isEnabled(),
          appHandler.getConfig().getMaxClients(), new InstanceManagerStatusMsgOut(
              appIMS.getStatus().name(), appIMS.getError(), appIMS.getErrorDetails())));
    });

    Integer instances = 0;
    Integer users = 0;

    List<SessionPoolInfoMsgOut> spInfos = new ArrayList<>();

    for (ServerSessionPoolConnector pool : sessionPools.values()) {
      instances += pool.getRunningConnectedInstances(null);
      users += pool.getConnectedConnectedInstances(null);
      spInfos.add(pool.getSessionPoolInfoMsg());
    }

    msgOut.setServerInfo(
        new ServerInfoMsgOut(getServerId(), appInfos, spInfos, instances, users, isCluster()));
    adminConsole.sendMessage(msgOut);
  }

  private boolean isCluster() {
    return !(sessionPools.size() == 1 && !sessionPools.values().iterator().next().isCluster());
  }

  private String getServerId() {
    return System.getProperty(Constants.WEBSWING_SERVER_ID);
  }

  @Override
  public WebswingDataStoreModule getDataStore(String path) {
    if (isRootPath(path)) {
      return globalHandler.getDataStore();
    }
    AppPathHandler handler = globalHandler.getAppHandler(path);
    if (handler == null) {
      return null;
    }
    return handler.getDataStore();
  }

  private SwingSessionsMsgOut getSwingSessions(String path) {
    SwingSessionsMsgOut swingSessions = new SwingSessionsMsgOut();

    List<SwingSessionMsgOut> runningSessions = new ArrayList<>();
    List<SwingSessionMsgOut> closedSessions = new ArrayList<>();

    for (ServerSessionPoolConnector pool : sessionPools.values()) {
      runningSessions.addAll(pool.getRunningConnectedInstanceSessions(path));
      closedSessions.addAll(pool.getClosedConnectedInstanceSessions(path));
    }

    swingSessions.setRunningSessions(runningSessions);
    swingSessions.setClosedSessions(closedSessions);

    return swingSessions;
  }

  private InstanceCountsStatsWarningsMsgOut getInstanceCountsStatsWarnings(String path) {
    InstanceCountsStatsWarningsMsgOut icsw = new InstanceCountsStatsWarningsMsgOut();

    int runningCount = 0;
    int closedCount = 0;
    int connectedCount = 0;
    List<StatEntryMsgOut> summaryStats = new ArrayList<>();
    List<SummaryWarningMsgOut> summaryWarnings = new ArrayList<>();

    List<InstanceStats> instanceStats = new ArrayList<>();
    Map<String, List<String>> instanceWarnings = new HashMap<>();

    for (ServerSessionPoolConnector pool : sessionPools.values()) {
      runningCount += pool.getRunningConnectedInstances(path);
      closedCount += pool.getClosedConnectedInstances(path);
      connectedCount += pool.getConnectedConnectedInstances(path);

      instanceStats.addAll(pool.getAllConnectedInstanceStats(path));
      instanceWarnings.putAll(pool.getConnectedInstanceSummaryWarnings(path));
    }

    Map<String, Map<String, Pair<BigDecimal, Integer>>> allSummaryStats =
        LoggerStatisticsUtil.mergeSummaryInstanceStats(instanceStats);
    for (Entry<String, Map<String, Pair<BigDecimal, Integer>>> entry : allSummaryStats.entrySet()) {
      StatEntryMsgOut summaryStat = new StatEntryMsgOut();
      summaryStat.setMetric(entry.getKey());

      List<MetricMsgOut> stats = new ArrayList<>();
      if (entry.getValue() != null) {
        for (Entry<String, Pair<BigDecimal, Integer>> statEntry : entry.getValue().entrySet()) {
          MetricMsgOut metric = new MetricMsgOut();
          metric.setKey(statEntry.getKey());
          metric.setValue(statEntry.getValue().getLeft().longValue());
          metric.setAggregatedCount(statEntry.getValue().getRight());
          stats.add(metric);
        }

        summaryStat.setStats(stats);
      }

      summaryStats.add(summaryStat);
    }

    for (Entry<String, List<String>> entry : instanceWarnings.entrySet()) {
      SummaryWarningMsgOut sw = new SummaryWarningMsgOut();
      sw.setInstanceId(entry.getKey());
      sw.setWarnings(entry.getValue());

      summaryWarnings.add(sw);
    }

    icsw.setRunningCount(runningCount);
    icsw.setClosedCount(closedCount);
    icsw.setConnectedCount(connectedCount);
    icsw.setSummaryStats(summaryStats);
    icsw.setSummaryWarnings(summaryWarnings);

    return icsw;
  }

  @Override
  public boolean connectApplication(ApplicationWebSocketConnection connection, boolean reconnect) {
    String instanceId = connection.getInstanceId();

    ConnectedSwingInstance instance = findInstanceByInstanceId(instanceId);
    // there should be a connected instance on this server because application should always connect
    // to correct server

    if (instance != null) {
      if (reconnect) {
        log.warn("Trying to reconnect instance [{}] to the same cluster server?", instanceId);
      }
      instance.connectApplication(connection, false);
      return true;
    }

    if (reconnect) {
      // FIXME reconnect flag may not be necessary, we could only rely on finding a browser
      // connection in reconnectWaiting
      synchronized (reconnectWaiting) {
        PrimaryWebSocketConnection browserConnection = reconnectWaiting.remove(instanceId);
        if (browserConnection == null || browserConnection.getReconnectHandshake() == null) {
          log.error(
              "Could not find browser websocket connection waiting for reconnect of instance [{}]! Disconnecting..",
              instanceId);
          return false;
        }

        ServerSessionPoolConnector sessionPool = findSessionPoolByInstanceId(instanceId);
        if (sessionPool == null) {
          // this should not happen
          log.error("Cannot find session pool to reconnect instance [{}]! Disconnecting..",
              instanceId);
          return false;
        }

        AppPathHandler appHandler = globalHandler.getAppHandler(browserConnection.getPath());

        try {
          sessionPool.reconnectInstance(instanceId, connection, browserConnection,
              browserConnection.getReconnectHandshake(), appHandler.createSwingInstanceInfo());
        } catch (WsException e) {
          log.error("Could not create connected instance for reconnecting instance [{}]!",
              instanceId, e);
          return false;
        }
        return true;
      }
    }

    log.warn("Could not find a connected instance for [{}]! Must reconnect from browser.",
        connection.getInstanceId());
    return false;
  }

  @Override
  public void registerReconnect(String instanceId, PrimaryWebSocketConnection r) {
    synchronized (reconnectWaiting) {
      if (!reconnectWaiting.containsKey(instanceId)) {
        reconnectWaiting.put(instanceId, r);
        return;
      }

      if (reconnectWaiting.get(instanceId) == r) {
        // same connection to be registered multiple times - should not happen
        return;
      }

      // different connection to be registered, disconnect previous
      reconnectWaiting.get(instanceId).disconnect("Another connection waiting for reconnect!");
      reconnectWaiting.put(instanceId, r);
    }
  }

  @Override
  public void unregisterReconnect(PrimaryWebSocketConnection r) {
    synchronized (reconnectWaiting) {
      Iterator<Entry<String, PrimaryWebSocketConnection>> it =
          reconnectWaiting.entrySet().iterator();
      while (it.hasNext()) {
        if (it.next().getValue() == r) {
          it.remove();
          break;
        }
      }
    }
  }

  @Override
  public void destroy(String path) {
    killAll(path);
  }

  @Override
  public void killAll(String path) {
    sessionPools.values().forEach(pool -> pool.killAll(path));
  }

  @Override
  public void connectView(String path, ConnectionHandshakeMsgIn handshake,
      PrimaryWebSocketConnection r, SwingInstanceInfo instanceInfo) throws WsException {
    SecuredPathConfig config = instanceInfo.getConfig();

    if (handshake.isMirrored()) {
      throw new WsException("Direct mirror connection is not allowed!");
    }

    // check if there is a room for another instance
    int maxClients = instanceInfo.getConfig().getMaxClients();
    if (maxClients >= 0) {
      int runningConnectedInstances = (int) sessionPools.values().stream()
          .map(sp -> sp.getInstancesRunningAndConnectedInSessionPool(path))
          .collect(Collectors.summarizingInt(i -> i)).getSum();
      if (runningConnectedInstances >= maxClients) {
        log.warn("Can not start new session of {}. Maximum number of clients reached [{}].", path,
            maxClients);
        if (r.isConnected()) {
          r.sendMessage(SimpleEventMsgOut.tooManyClientsNotification.buildMsgOut());
        }
        return;
      }
    }

    if (!connectSwingInstance(path, r, handshake, config.getSessionMode(),
        config.isAllowStealSession())) {
      if (handshake.getInstanceId() != null) {
        r.sendMessage(SimpleEventMsgOut.reconnectInstanceNotFound.buildMsgOut());
        return;
      }
      startSwingInstance(path, r, handshake, instanceInfo);
    }
  }

  private boolean connectSwingInstance(String path, PrimaryWebSocketConnection r,
      ConnectionHandshakeMsgIn h, SessionMode sessionMode, boolean stealSessionAllowed)
      throws WsException {
    for (ServerSessionPoolConnector pool : sessionPools.values()) {
      if (pool.tryConnectSwingInstance(path, r, h, sessionMode, stealSessionAllowed)) {
        return true;
      }
    }
    return false;
  }

  private void startSwingInstance(String path, PrimaryWebSocketConnection r,
      ConnectionHandshakeMsgIn h, SwingInstanceInfo instanceInfo) throws WsException {
    if (h.isMirrored()) {
      throw new WsException("Direct mirror connection is not allowed!");
    }

    if (!r.hasPermission(WebswingAction.websocket_startSwingApplication)) {
      throw new WsException("Authorization error: User " + r.getUser()
          + " is not authorized to connect to application " + instanceInfo.getConfig().getName()
          + (h.isMirrored() ? " [Mirrored view only available for admin role]" : ""));
    }

    try {
      if (!createInstance(path, h, r, instanceInfo)) {
        if (r.isConnected()) {
          r.sendMessage(SimpleEventMsgOut.tooManyClientsNotification.buildMsgOut());
        }
      }
    } catch (WsException e) {
      throw new WsException("Failed to create Application instance.", e);
    }
  }

  private boolean createInstance(String path, ConnectionHandshakeMsgIn h,
      PrimaryWebSocketConnection r, SwingInstanceInfo instanceInfo) throws WsException {
    ServerSessionPoolConnector pool =
        loadBalanceResolver.resolveLoadBalance(path, instanceInfo.getConfig());

    if (pool == null) {
      return false;
    }

    pool.createSwingInstance(path, r, h, instanceInfo);

    return true;
  }

  @Override
  public void logStatValue(String instanceId, String path, String metric, Number value) {
    ServerSessionPoolConnector pool = findSessionPoolByInstanceId(instanceId);

    if (pool == null) {
      return;
    }

    pool.logStatValueForConnectedInstance(path, instanceId, metric, value);
  }

  @Override
  public void unregisterWithAdminConsole(String path, String instanceId) {
    registerInstanceWithAdminConsole(path, instanceId, false);
  }

  @Override
  public void registerWithAdminConsole(String path, String instanceId) {
    registerInstanceWithAdminConsole(path, instanceId, true);
  }

  private void registerInstancesWithAdminConsole(List<RegisterInstanceMsgOut> instances) {
    if (adminConsole != null) {
      AdminConsoleFrameMsgOut msgOut = new AdminConsoleFrameMsgOut();
      msgOut.setRegisterInstances(instances);
      adminConsole.sendMessage(msgOut);
    }
  }

  private void registerInstanceWithAdminConsole(String path, String instanceId, boolean connected) {
    if (adminConsole != null) {
      AdminConsoleFrameMsgOut msgOut = new AdminConsoleFrameMsgOut();
      msgOut.setRegisterInstances(
          Lists.newArrayList(new RegisterInstanceMsgOut(path, instanceId, connected)));
      adminConsole.sendMessage(msgOut);
    }
  }

  private void requestRecordingByInstanceId(
      RecordingRequestMsgIn.RecordingRequestType recordingRequestType, String path,
      String instanceId) throws WsException {
    ServerSessionPoolConnector pool = findSessionPoolByInstanceId(instanceId);

    if (pool == null) {
      return;
    }

    pool.requestRecordingForConnectedInstance(recordingRequestType, path, instanceId);
  }

  private void toggleStatisticsLoggingByInstanceId(String path, String instanceId, Boolean enabled)
      throws WsException {
    ServerSessionPoolConnector pool = findSessionPoolByInstanceId(instanceId);

    if (pool == null) {
      return;
    }

    pool.toggleStatisticsLoggingForConnectedInstance(path, instanceId, enabled);
  }

  private List<AppConfigMsgOut> getAppConfigs(String path) {
    List<AppConfigMsgOut> appConfigs = callSessionPoolsSync(null, sessionPool -> {
      try {
        byte[] config = sessionPool.getAppConfig(path);
        return new AppConfigMsgOut(config, sessionPool.getId());
      } catch (Exception e) {
        log.error("Error while getting config from session pool [{}] for path [{}]!",
            sessionPool.getId(), path, e);
        return new AppConfigMsgOut(ExceptionUtils.getStackTrace(e));
      }
    });

    return appConfigs;
  }

  private List<MapMsgOut> resolveAppConfig(String path, String user, String resolve,
      String sessionPoolId) {
    if (StringUtils.isNotBlank(sessionPoolId)) {
      ServerSessionPoolConnector pool = findSessionPoolById(sessionPoolId);

      if (pool == null) {
        return Collections.emptyList();
      }

      try {
        return Lists
            .newArrayList(new MapMsgOut(pool.getId(), pool.resolveConfig(path, user, resolve)));
      } catch (Exception e) {
        log.error("Error while resolving from session pool [{}] for path [{}]!", pool.getId(), path,
            e);
        return Lists.newArrayList(new MapMsgOut(pool.getId(), null));
      }
    }

    List<MapMsgOut> appResolved = callSessionPoolsSync(null, sessionPool -> {
      try {
        String resolved = sessionPool.resolveConfig(path, user, resolve);
        return new MapMsgOut(sessionPool.getId(), resolved);
      } catch (Exception e) {
        log.error("Error while resolving from session pool [{}] for path [{}]!",
            sessionPool.getId(), path, e);
        return new MapMsgOut(sessionPool.getId(), null);
      }
    });

    return appResolved;
  }

  private List<List<MapMsgOut>> searchAppVariables(String path, String user, String search,
      String sessionPoolId) {
    if (StringUtils.isNotBlank(sessionPoolId)) {
      ServerSessionPoolConnector pool = findSessionPoolById(sessionPoolId);

      if (pool == null) {
        return Collections.emptyList();
      }

      try {
        List<MapMsgOut> list = new ArrayList<>();
        for (Entry<String, String> entry : pool.searchVariables(path, user, search).entrySet()) {
          list.add(new MapMsgOut(entry.getKey(), entry.getValue()));
        }

        List<List<MapMsgOut>> searchList = new ArrayList<>();
        searchList.add(list);
        return searchList;
      } catch (Exception e) {
        log.error("Error while resolving from session pool [{}] for path [{}]!", pool.getId(), path,
            e);
        return Collections.emptyList();
      }
    }

    List<List<MapMsgOut>> appVariables = callSessionPoolsSync(null, sessionPool -> {
      try {
        Map<String, String> variables = sessionPool.searchVariables(path, user, search);
        if (variables == null) {
          return null;
        }
        List<MapMsgOut> list = new ArrayList<>();
        for (Entry<String, String> entry : variables.entrySet()) {
          list.add(new MapMsgOut(entry.getKey(), entry.getValue()));
        }
        return list;
      } catch (Exception e) {
        log.error("Error while searching variables from session pool [{}] for path [{}]!",
            sessionPool.getId(), path, e);
        return null;
      }
    });

    return appVariables;
  }

  private List<AppConfigMsgOut> getAppMetas(String path, Map<String, byte[]> configs) {
    List<AppConfigMsgOut> appConfigs = callSessionPoolsSync(configs.keySet(), sessionPool -> {
      try {
        byte[] meta = sessionPool.getAppMeta(path, configs.get(sessionPool.getId()));
        return new AppConfigMsgOut(meta, sessionPool.getId());
      } catch (Exception e) {
        log.error("Error while getting meta from session pool [{}] for path [{}]!",
            sessionPool.getId(), path, e);
        return new AppConfigMsgOut(ExceptionUtils.getStackTrace(e));
      }
    });

    return appConfigs;
  }

  private List<SaveConfigAppResultMsgOut> saveAppConfigs(String path, Map<String, byte[]> configs) {
    List<SaveConfigAppResultMsgOut> appConfigs = callSessionPoolsSync(null, sessionPool -> {
      try {
        sessionPool.saveConfig(path, configs.get(sessionPool.getId()));
        return new SaveConfigAppResultMsgOut(true, sessionPool.getId());
      } catch (Exception e) {
        log.error("Error while saving app config to session pool [{}] for path [{}]!",
            sessionPool.getId(), path, e);
        return new SaveConfigAppResultMsgOut(false, sessionPool.getId(),
            ExceptionUtils.getStackTrace(e));
      }
    });

    return appConfigs;
  }

  private <T> List<T> callSessionPoolsSync(Collection<String> sessionPoolIds,
      Function<ServerSessionPoolConnector, T> task) {
    List<T> results = Collections.synchronizedList(new ArrayList<>());

    List<Thread> threads = new ArrayList<>();
    for (ServerSessionPoolConnector pool : sessionPools.values()) {
      if (sessionPoolIds != null && !sessionPoolIds.contains(pool.getId())) {
        log.warn("Could not find session pool [{}] for sync call!", pool.getId());
        continue;
      }
      Thread t = new Thread(() -> {
        try {
          T result = task.apply(pool);
          if (result != null) {
            synchronized (results) {
              results.add(result);
            }
          }
        } catch (Exception e) {
          log.error("Unhandled exception while in sync session pool call!", e);
        }
      });
      t.start();
      threads.add(t);
    }

    for (Thread t : threads) {
      try {
        t.join(SYNC_MESSAGE_TIMEOUT);
      } catch (InterruptedException e) {
      }
    }

    return results;
  }

  private ThreadDumpMsgOut findThreadDumpByInstanceId(String path, String instanceId,
      String timestamp) throws WsException {
    for (ServerSessionPoolConnector pool : sessionPools.values()) {
      ThreadDumpMsgOut threadDump =
          pool.tryFindThreadDumpByInstanceIdIncludeClosed(path, instanceId, timestamp);
      if (threadDump != null) {
        return threadDump;
      }
    }

    return null;
  }

  private void shutdownInstance(String path, String instanceId, boolean force) throws WsException {
    ServerSessionPoolConnector pool = findSessionPoolByInstanceId(instanceId);

    if (pool == null) {
      return;
    }

    pool.shutdownConnectedInstance(path, instanceId, force);
  }

  private void requestInstanceThreadDump(String path, String instanceId) {
    ServerSessionPoolConnector pool = findSessionPoolByInstanceId(instanceId);

    if (pool == null) {
      return;
    }

    pool.requestConnectedInstanceThreadDump(path, instanceId);
  }

  private ServerSessionPoolConnector findSessionPoolByInstanceId(String instanceId) {
    for (ServerSessionPoolConnector pool : sessionPools.values()) {
      if (pool.hasConnectedInstanceWithInstanceId(instanceId)) {
        return pool;
      }
    }
    return null;
  }

  private ServerSessionPoolConnector findSessionPoolById(String sessionPoolId) {
    for (ServerSessionPoolConnector pool : sessionPools.values()) {
      if (pool.getId().equals(sessionPoolId)) {
        return pool;
      }
    }
    return null;
  }

  private ConnectedSwingInstance findInstanceByInstanceId(String instanceId) {
    ServerSessionPoolConnector pool = findSessionPoolByInstanceId(instanceId);

    if (pool == null) {
      return null;
    }

    return pool.getConnectedInstanceByInstanceId(instanceId);
  }

  private VariableSetName getVariableType(String type) {
    try {
      return VariableSetName.valueOf(type);
    } catch (Exception e) {
      log.error("Could not resolve variable substitutor type [{}], falling back to basic!", type,
          e);
      return VariableSetName.Basic;
    }
  }

  private VariableSubstitutor getVariableSubstitutorByType(String type, String user,
      SecuredPathHandler handler) {
    VariableSetName variableType = getVariableType(type);

    switch (variableType) {
      case SwingInstance:
        throw new IllegalArgumentException(
            "Cannot create swing instance variable substitutor outside session pool!");
      case SwingApp:
        return VariableSubstitutor.forSwingApp(handler.getConfig());
      case Basic:
      default:
        return VariableSubstitutor.basic();
    }
  }

}
