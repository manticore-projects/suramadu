package org.webswing.server.api.services.swinginstance.impl;

import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.Constants;
import org.webswing.model.adminconsole.out.SwingSessionMsgOut;
import org.webswing.model.adminconsole.out.SwingSessionMsgOut.StatusEnum;
import org.webswing.model.app.in.ApiCallResultMsgIn;
import org.webswing.model.app.in.ApiEventMsgIn;
import org.webswing.model.app.in.ApiEventMsgIn.ApiEventType;
import org.webswing.model.app.in.ServerToAppFrameMsgIn;
import org.webswing.model.app.out.ApiCallMsgOut;
import org.webswing.model.app.out.AppToServerFrameMsgOut;
import org.webswing.model.app.out.ExitMsgOut;
import org.webswing.model.app.out.JvmStatsMsgOut;
import org.webswing.model.app.out.SessionDataMsgOut;
import org.webswing.model.app.out.ThreadDumpMsgOut;
import org.webswing.model.appframe.in.AppFrameMsgIn;
import org.webswing.model.appframe.out.AppFrameMsgOut;
import org.webswing.model.appframe.out.LinkActionMsgOut;
import org.webswing.model.appframe.out.LinkActionMsgOut.LinkActionType;
import org.webswing.model.appframe.out.SimpleEventMsgOut;
import org.webswing.model.browser.in.BrowserToServerFrameMsgIn;
import org.webswing.model.browser.out.ConnectionInfoMsgOut;
import org.webswing.model.browser.out.ServerToBrowserFrameMsgOut;
import org.webswing.model.common.in.*;
import org.webswing.model.common.in.SimpleEventMsgIn.SimpleEventType;
import org.webswing.server.api.model.ProcessStatusEnum;
import org.webswing.server.api.services.sessionpool.ServerSessionPoolConnector;
import org.webswing.server.api.services.swinginstance.ConnectedSwingInstance;
import org.webswing.server.api.services.swinginstance.SwingInstanceInfo;
import org.webswing.server.api.services.websocket.*;
import org.webswing.server.common.datastore.WebswingDataStoreModule;
import org.webswing.server.common.datastore.WebswingDataStoreType;
import org.webswing.server.common.model.SecuredPathConfig;
import org.webswing.server.common.service.security.AbstractWebswingUser;
import org.webswing.server.common.service.stats.StatisticsLogger;
import org.webswing.server.common.util.ProtoMapper;
import org.webswing.server.common.util.VariableSubstitutor;
import org.webswing.server.model.exception.WsException;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SwingInstanceImpl implements Serializable, ConnectedSwingInstance {

  @Serial
  private static final long serialVersionUID = -4640770499863974871L;

  private static final Logger log = LoggerFactory.getLogger(ConnectedSwingInstance.class);

  private final ProtoMapper appFrameInProtoMapper = new ProtoMapper(
      ProtoMapper.PROTO_PACKAGE_APPFRAME_IN, ProtoMapper.PROTO_PACKAGE_APPFRAME_OUT);
  private final ProtoMapper appFrameOutProtoMapper = new ProtoMapper(
      ProtoMapper.PROTO_PACKAGE_APPFRAME_OUT, ProtoMapper.PROTO_PACKAGE_APPFRAME_IN);

  private final String instanceId;
  private final String ownerId;
  private final String urlContext;
  private final String pathMapping;
  private final String appName;

  private PrimaryWebSocketConnection webConnection;
  private MirrorWebSocketConnection mirroredWebConnection;
  private ApplicationWebSocketConnection appConnection;

  // Stable lock objects for synchronizing access to the mutable connection fields above.
  // Synchronizing on the connection fields themselves is futile because they are reassigned —
  // see SpotBugs ML_SYNC_ON_UPDATED_FIELD.
  private final Object webConnectionLock = new Object();
  private final Object mirroredWebConnectionLock = new Object();

  private MirroringStatusEnum mirroringStatus = MirroringStatusEnum.NOT_MIRRORING;
  private Runnable doAfterMirroringAccepted;

  private final ServerSessionPoolConnector poolConnector;

  private final SecuredPathConfig config;
  private final String goodbyeUrl;

  private final Date startedAt = new Date();
  private WebSocketUserInfo lastConnection;

  private Date endedAt; // finished instances only

  private List<String> warningHistoryLog;
  private final Map<Long, ThreadDumpMsgOut> threadDumps = new ConcurrentHashMap<>();
  private SessionDataMsgOut sessionData;
  private ProcessStatusEnum processStatus;

  private final List<ServerToAppFrameMsgIn> startupMsgQueue =
      Collections.synchronizedList(new ArrayList<>());

  public SwingInstanceImpl(PrimaryWebSocketConnection websocket, ConnectionHandshakeMsgIn h,
      SwingInstanceInfo instanceInfo, ServerSessionPoolConnector serverSessionPoolConnector)
      throws WsException {
    this.poolConnector = serverSessionPoolConnector;
    this.config = instanceInfo.getConfig();
    this.ownerId = instanceInfo.getOwnerId();
    this.instanceId = instanceInfo.getInstanceId();
    this.urlContext = instanceInfo.getUrlContext();
    this.pathMapping = instanceInfo.getPathMapping();
    this.appName = instanceInfo.getConfig().getName();

    VariableSubstitutor handlerSubs = VariableSubstitutor.forSwingApp(config);
    this.goodbyeUrl = handlerSubs.replace(config.getGoodbyeUrl());

    connectPrimaryWebSession(websocket);

    logStatValue(StatisticsLogger.WEBSOCKET_CONNECTED, 1);
  }

  @Override
  public void connectApplication(ApplicationWebSocketConnection appConnection, boolean reconnect) {
    this.appConnection = appConnection;
    appConnection.instanceConnected(this);
    sendConnectionInfo();

    if (reconnect && webConnection != null) {
      synchronized (webConnectionLock) {
        // FIXME is this ok ?
        // send continue old session, because this is sent if we hit the same server on refresh
        sendDirectMessageToBrowser(webConnection,
            SimpleEventMsgOut.continueOldSession.buildMsgOut());
      }
    }

    log.info("Application websocket connected to instance. [{}]", this.instanceId);

    if (!startupMsgQueue.isEmpty()) {
      log.info("Dispatching {} queued messages.", startupMsgQueue.size());
      startupMsgQueue.forEach(this::sendMessageToApp);
      startupMsgQueue.clear();
    }
  }

  @Override
  public void connectBrowser(PrimaryWebSocketConnection r, ConnectionHandshakeMsgIn h) {
    if (h.isMirrored()) {
      // this is prevented in caller
      // Direct mirror connection is not allowed!
      return;
    }

    if (r.uuid() != null && r.uuid().equals(getConnectionId())) {
      // ignore, this is handled in BrowserWebSocketConnectionImpl
    } else {
      // continue old session?
      boolean result = connectPrimaryWebSession(r);
      if (result) {
        sendDirectMessageToBrowser(r, SimpleEventMsgOut.continueOldSession.buildMsgOut());
      } else {
        sendDirectMessageToBrowser(r, SimpleEventMsgOut.applicationAlreadyRunning.buildMsgOut());
      }
    }
  }

  @Override
  public void browserDisconnected(String connectionId) {
    if (getConnectionId() != null && getConnectionId().equals(connectionId)) {
      disconnectPrimaryWebSession("Browser disconnected.");
    }
  }

  @Override
  public void applicationDisconnected(String reason) {
    this.appConnection = null;

    if (reason == null) {
      disconnectPrimaryWebSession("Application disconnected from Server.");
      disconnectMirroredWebSession(true);
    } else if (Constants.APP_WEBSOCKET_CLOSE_REASON_RECONNECT.equals(reason)) {
      if (this.webConnection != null) {
        synchronized (webConnectionLock) {
          // session stolen
          sendDirectMessageToBrowser(this.webConnection,
              SimpleEventMsgOut.sessionStolenNotification.buildMsgOut());
        }
      }
      disconnectPrimaryWebSession("Session stolen, reconnect.");
      disconnectMirroredWebSession(true);
      poolConnector.instanceReconnecting(this);
    }
  }

  private boolean connectPrimaryWebSession(PrimaryWebSocketConnection resource) {
    if (resource == null) {
      return false;
    }

    if (this.webConnection != null && config.isAllowStealSession()) {
      // steal session
      synchronized (webConnectionLock) {
        sendDirectMessageToBrowser(this.webConnection,
            SimpleEventMsgOut.sessionStolenNotification.buildMsgOut());
        disconnectPrimaryWebSession("Session stolen.");
        poolConnector.notifyUserDisconnected(this); // call this once webConnection is already null
      }
    }

    if (this.webConnection == null) {
      this.webConnection = resource;
      logStatValue(StatisticsLogger.WEBSOCKET_CONNECTED, 1);
      notifyUserConnected();
      return true;
    }

    return false;
  }

  private void disconnectPrimaryWebSession(String reason) {
    if (this.webConnection != null) {
      synchronized (webConnectionLock) {
        notifyUserDisconnected(); // this uses webConnection
        this.lastConnection = this.webConnection.getUserInfo();
        this.lastConnection.setDisconnected();
        this.webConnection.disconnect(reason);
      }
      this.webConnection = null;
      poolConnector.notifyUserDisconnected(this); // call this once webConnection is already null
      logStatValue(StatisticsLogger.WEBSOCKET_CONNECTED, 0);
    }
  }

  @Override
  public void connectMirroredWebSession(MirrorWebSocketConnection mirror) {
    if (mirror == null) {
      return;
    }

    if (this.mirroredWebConnection != null) {
      synchronized (mirroredWebConnectionLock) {
        sendDirectMessageToBrowser(this.mirroredWebConnection,
            SimpleEventMsgOut.sessionStolenNotification.buildMsgOut());
      }
      this.mirroredWebConnection.disconnect("Mirror session stolen.");
      disconnectMirroredWebSession(false);
    }
    this.mirroredWebConnection = mirror;

    startMirroring(mirror);
  }

  @Override
  public void disconnectMirroredWebSession(boolean disconnect) {
    disconnectMirroredWebSession(null, disconnect);
    stopMirroring(this.mirroredWebConnection);
  }

  @Override
  public void disconnectMirroredWebSession(String sessionId, boolean disconnect) {
    if (this.mirroredWebConnection != null) {
      if (sessionId == null
          || StringUtils.equals(this.mirroredWebConnection.getMirrorSessionId(), sessionId)) {
        sendSimpleEvent(SimpleEventType.stopMirroring);
        notifyMirrorViewDisconnected();
        if (disconnect) {
          this.mirroredWebConnection.disconnect("Disconnected.");
        }
        this.mirroredWebConnection = null;
      }
    }
  }

  public void startMirroring(MirrorWebSocketConnection mirror) {
    mirroringStatus = MirroringStatusEnum.WAITING_FOR_MIRRORING_APPROVAL;
    sendSimpleEvent(SimpleEventType.startMirroring);
    doAfterMirroringAccepted = () -> {
      this.mirroredWebConnection = mirror;
      notifyMirrorViewConnected();
    };
  }

  public void stopMirroring(MirrorWebSocketConnection mirror) {
    mirroringStatus = MirroringStatusEnum.NOT_MIRRORING;
    this.mirroredWebConnection = null;
    sendSimpleEvent(SimpleEventType.stopMirroring);
    doAfterMirroringAccepted = () -> {
    };
  }

  private void setMirroringStatus(MirroringStatusEnum mirroringStatus) {
    if (doAfterMirroringAccepted != null && this.mirroringStatus != MirroringStatusEnum.MIRRORING
        && mirroringStatus == MirroringStatusEnum.MIRRORING) { // Mirroring accepted and started
      doAfterMirroringAccepted.run();
    }
    this.mirroringStatus = mirroringStatus;
  }

  @Override
  public void handleAppMessage(AppToServerFrameMsgOut msgOut) {
    if (msgOut.getApiCall() != null) {
      ApiCallMsgOut query = msgOut.getApiCall();
      AbstractWebswingUser currentUser = webConnection != null ? webConnection.getUser() : null;

      ServerToAppFrameMsgIn appMsg = new ServerToAppFrameMsgIn();
      ApiCallResultMsgIn resultMsg = new ApiCallResultMsgIn();
      resultMsg.setCorrelationId(query.getCorrelationId());

      switch (query.getMethod()) {
        case HasRole:
          if (currentUser != null) {
            resultMsg.setResult(currentUser.hasRole((String) query.getArgs().get(0)) + "");
          }
          break;
        case IsPermitted:
          if (currentUser != null) {
            resultMsg.setResult(currentUser.isPermitted((String) query.getArgs().get(0)) + "");
          }
          break;
        default:
          break;
      }

      appMsg.setApiCallResult(resultMsg);
      sendMessageToApp(appMsg);
    }

    if (msgOut.getJvmStats() != null) {
      JvmStatsMsgOut s = msgOut.getJvmStats();

      if (isStatisticsLoggingEnabled()) {
        double cpuUsage = s.getCpuUsage();

        logStatValue(StatisticsLogger.MEMORY_ALLOCATED_METRIC, s.getHeapSize());
        logStatValue(StatisticsLogger.MEMORY_USED_METRIC, s.getHeapSizeUsed());
        logStatValue(StatisticsLogger.CPU_UTIL_METRIC, cpuUsage);
        logStatValue(StatisticsLogger.EDT_BLOCKED_SEC_METRIC, s.getEdtPingSeconds());
      }

      if (getConfig().isMonitorEdtEnabled()) {
        if (s.getEdtPingSeconds() > Math.max(2, getConfig().getLoadingAnimationDelay())) {
          sendMessageToBrowser(SimpleEventMsgOut.applicationBusy.buildMsgOut());
        }
      }
    }

    if (msgOut.getExit() != null) {
      close();
      ExitMsgOut e = msgOut.getExit();
      poolConnector.kill(getInstanceId(), e.getWaitForExit());
    }

    if (msgOut.getThreadDump() != null) {
      ThreadDumpMsgOut e = msgOut.getThreadDump();
      threadDumps.put(e.getTimestamp(), e);
    }

    if (msgOut.getSessionData() != null) {
      this.sessionData = msgOut.getSessionData();

      setMirroringStatus(sessionData.getMirroringStatus());
    }

    if (msgOut.getAppFrameMsgOut() != null) {
      // resend app frame
      ServerToBrowserFrameMsgOut frameOut = new ServerToBrowserFrameMsgOut();
      frameOut.setAppFrameMsgOut(msgOut.getAppFrameMsgOut());

      sendMessageToBrowser(frameOut);
    }
  }

  @Override
  public void handleBrowserMessage(BrowserToServerFrameMsgIn msgIn) {
    if (msgIn.getTimestamps() != null) {
      msgIn.getTimestamps().forEach(this::processTimestampMessage);
    }

    ServerToAppFrameMsgIn frameIn = new ServerToAppFrameMsgIn();
    frameIn.setAppFrameMsgIn(msgIn.getAppFrameMsgIn());
    frameIn.setHandshake(msgIn.getHandshake());
    frameIn.setEvents(msgIn.getEvents());
    frameIn.setTimestamps(msgIn.getTimestamps());

    if (msgIn.getEvents() != null) {
      msgIn.getEvents().forEach(m -> {
        if (m.getType().equals(SimpleEventMsgIn.SimpleEventType.unload)) {
          closeBrowserConnections();
        }
      });
    }

    sendMessageToApp(frameIn);
  }

  @Override
  public void handleBrowserMirrorMessage(byte[] frame) {
    if (mirroredWebConnection != null) {
      synchronized (mirroredWebConnectionLock) {
        if (mirroringStatus == MirroringStatusEnum.MIRRORING) {
          mirroredWebConnection.handleBrowserMirrorMessage(frame);
        }
      }
    } else {
      log.warn("Mirror not connected [{}]!", getInstanceId());
    }
  }

  private void closeBrowserConnections() {
    if (webConnection != null) {
      synchronized (webConnectionLock) {
        webConnection.disconnect("Application disconnected!");
      }
    }
    if (mirroredWebConnection != null) {
      synchronized (mirroredWebConnectionLock) {
        mirroredWebConnection.disconnect("Application disconnected!");
      }
    }

    disconnectPrimaryWebSession("Application closed.");
    disconnectMirroredWebSession(false);
  }

  /**
   * No session recording, no copying to mirrored connection.
   */
  private boolean sendDirectMessageToBrowser(BrowserWebSocketConnection r, AppFrameMsgOut frame) {
    if (!r.isConnected()) {
      return false;
    }
    ServerToBrowserFrameMsgOut msgOut = encodeFrameMessage(frame);
    r.sendMessage(msgOut);
    return true;
  }

  private boolean sendMessageToBrowser(AppFrameMsgOut frame) {
    return sendMessageToBrowser(encodeFrameMessage(frame));
  }

  private ServerToBrowserFrameMsgOut encodeFrameMessage(AppFrameMsgOut frame) {
    ServerToBrowserFrameMsgOut msgOut = new ServerToBrowserFrameMsgOut();

    try {
      msgOut.setAppFrameMsgOut(appFrameOutProtoMapper.encodeProto(frame));
    } catch (IOException e) {
      log.error("Error encoding proto frame to browser, session [{}]!", webConnection.uuid(), e);
    }

    return msgOut;
  }

  public boolean sendMessageToBrowser(ServerToBrowserFrameMsgOut msgOut) {
    if (webConnection != null) {
      synchronized (webConnectionLock) {
        if (webConnection.isConnected()) {
          webConnection.sendMessage(msgOut);
        }
      }
    }
    if (mirroredWebConnection != null) {
      synchronized (mirroredWebConnectionLock) {
        if (mirroredWebConnection.isConnected()) {
          if (mirroringStatus == MirroringStatusEnum.MIRRORING) {
            mirroredWebConnection.sendMessage(msgOut);
          }
        }
      }
    }

    return true;
  }

  public boolean sendMessageToApp(ServerToAppFrameMsgIn msgIn) {
    if (!isRunning()) {
      return false;
    }

    if (appConnection == null) {
      startupMsgQueue.add(msgIn);
      return true;
    }

    if (!appConnection.isConnected()) {
      return false;
    }

    appConnection.sendMessage(msgIn);

    return true;
  }

  @Override
  public boolean sendMessageToApp(AppFrameMsgIn msgIn) {
    try {
      ServerToAppFrameMsgIn frame = new ServerToAppFrameMsgIn();
      frame.setAppFrameMsgIn(appFrameInProtoMapper.encodeProto(msgIn));
      return sendMessageToApp(frame);
    } catch (IOException e) {
      log.error("Error encoding proto frame to app!", e);
    }
    return false;
  }

  private void processTimestampMessage(TimestampsMsgIn h) {
    if (!isStatisticsLoggingEnabled()) {
      return;
    }

    if (StringUtils.isNotEmpty(h.getSendTimestamp())) {
      long currentTime = System.currentTimeMillis();
      long sendTime = Long.parseLong(h.getSendTimestamp());
      if (StringUtils.isNotEmpty(h.getRenderingTime())
          && StringUtils.isNotEmpty(h.getStartTimestamp())) {
        long renderingTime = Long.parseLong(h.getRenderingTime());
        long startTime = Long.parseLong(h.getStartTimestamp());
        logStatValue(StatisticsLogger.LATENCY_SERVER_RENDERING, sendTime - startTime);
        logStatValue(StatisticsLogger.LATENCY_CLIENT_RENDERING, renderingTime);
        logStatValue(StatisticsLogger.LATENCY, currentTime - startTime);
        logStatValue(StatisticsLogger.LATENCY_NETWORK_TRANSFER,
            currentTime - sendTime - renderingTime);
      }
    }
    if (h.getPing() > 0) {
      logStatValue(StatisticsLogger.LATENCY_PING, h.getPing());
    }
  }

  @Override
  public String getOwnerId() {
    return ownerId;
  }

  public String getInstanceId() {
    return instanceId;
  }

  public SecuredPathConfig getConfig() {
    return config;
  }

  public String getConnectionId() {
    if (webConnection != null) {
      return webConnection.uuid();
    }
    return null;
  }

  public Date getStartedAt() {
    return startedAt;
  }

  public Date getEndedAt() {
    return endedAt;
  }

  public boolean isRunning() {
    return endedAt == null;
  }

  @Override
  public void startRecording() {
    sendSimpleEvent(SimpleEventType.startRecording);
  }

  @Override
  public void stopRecording() {
    sendSimpleEvent(SimpleEventType.stopRecording);
  }

  private void sendSimpleEvent(SimpleEventType simpleEventType) {
    ServerToAppFrameMsgIn msgIn = new ServerToAppFrameMsgIn();
    SimpleEventMsgIn event = new SimpleEventMsgIn(simpleEventType);
    msgIn.setEvents(Lists.newArrayList(event));

    sendMessageToApp(msgIn);
  }

  private void logStatValue(String name, Number value) {
    if (!isStatisticsLoggingEnabled()) {
      return;
    }
    if (StringUtils.isNotEmpty(name)) {
      poolConnector.logStatValueForConnectedInstance(pathMapping, getInstanceId(), name, value);
    }
  }

  @Override
  public void logWarningHistory() {
    List<String> current = poolConnector.getConnectedInstanceWarnings(pathMapping, getInstanceId());
    if (current != null) {
      current
          .addAll(poolConnector.getConnectedInstanceWarningHistory(pathMapping, getInstanceId()));
    }
    warningHistoryLog = current;
  }

  @Override
  public org.webswing.model.adminconsole.out.ThreadDumpMsgOut getThreadDump(String timestamp) {
    try {
      ThreadDumpMsgOut dump = threadDumps.get(Long.parseLong(timestamp));
      if (dump != null) {
        return new org.webswing.model.adminconsole.out.ThreadDumpMsgOut(getInstanceId(),
            Long.parseLong(timestamp), getThreadDumpContent(dump.getDumpId()), dump.getReason());
      }
    } catch (Exception e) {
      log.error("Failed to load threaddump", e);
    }
    return null;
  }

  private String getThreadDumpContent(String dumpId) {
    WebswingDataStoreModule dataStore = poolConnector.getDataStore(pathMapping);
    if (dataStore == null) {
      return null;
    }

    try (InputStream is = dataStore.readData(WebswingDataStoreType.threadDump.name(), dumpId)) {
      return IOUtils.toString(is);
    } catch (Exception e) {
      log.error("Failed to read thread dump [{}]!", dumpId, e);
    }

    return null;
  }

  @Override
  public String getUserId() {
    synchronized (this) {
      return webConnection == null ? lastConnection.getUserId() : webConnection.getUserId();
    }
  }

  @Override
  public void close() {
    notifyExiting();

    if (config.isAutoLogout()) {
      sendMessageToBrowser(SimpleEventMsgOut.shutDownAutoLogoutNotification.buildMsgOut());
    } else {
      if (StringUtils.isNotBlank(goodbyeUrl)) {
        String url = goodbyeUrl;
        if (url.startsWith("/")) {
          url = urlContext + url;
        }
        AppFrameMsgOut result = new AppFrameMsgOut();
        result.setLinkAction(new LinkActionMsgOut(LinkActionType.redirect, url));
        sendMessageToBrowser(result);
      } else {
        sendMessageToBrowser(SimpleEventMsgOut.shutDownNotification.buildMsgOut());
      }
    }

    if (appConnection != null) {
      appConnection.disconnect("Closing instance.");
    }
  }

  private void notifyExiting() {
    endedAt = new Date();
    poolConnector.removeConnectedSwingInstance(this, false);
    logWarningHistory();
  }

  @Override
  public void notifyUserConnected() {
    sendUserApiEventMsg(ApiEventType.UserConnected, webConnection);
    poolConnector.notifyUserConnected(this);

    if (webConnection != null) {
      synchronized (webConnectionLock) {
        // this should be always true
        webConnection.instanceConnected(this);
      }

      sendConnectionInfo();
    }
  }

  private void sendConnectionInfo() {
    if (webConnection == null || appConnection == null) {
      return;
    }

    ServerToBrowserFrameMsgOut msgOut = new ServerToBrowserFrameMsgOut();
    msgOut.setConnectionInfo(
        new ConnectionInfoMsgOut(System.getProperty(Constants.WEBSWING_SERVER_ID),
            appConnection.getSessionPoolId(), config.isAutoLogout()));

    synchronized (webConnectionLock) {
      if (webConnection.isConnected()) {
        webConnection.sendMessage(msgOut);
      }
    }
  }

  private void notifyUserDisconnected() {
    sendUserApiEventMsg(ApiEventType.UserDisconnected, webConnection);
  }

  private void notifyMirrorViewConnected() {
    sendUserApiEventMsg(ApiEventType.MirrorViewConnected, mirroredWebConnection);
    mirroredWebConnection.instanceConnected(this);
  }

  private void notifyMirrorViewDisconnected() {
    sendUserApiEventMsg(ApiEventType.MirrorViewDisconnected, mirroredWebConnection);
  }

  private void sendUserApiEventMsg(ApiEventType type, PrimaryWebSocketConnection r) {
    ApiEventMsgIn event;
    if (r != null && r.getUser() != null) {
      AbstractWebswingUser connectedUser = r.getUser();
      byte[] args = null;
      if (connectedUser.getUserAttributes() != null) {
        try {
          args = new JsonMapper().writeValueAsBytes(connectedUser.getUserAttributes());
        } catch (JacksonException e) {
          log.error("Could not serialize user attributes!", e);
        }
      }

      event = new ApiEventMsgIn(type, connectedUser.getUserId(), args);
    } else {
      event = new ApiEventMsgIn(type, null, null);
    }
    ServerToAppFrameMsgIn frame = new ServerToAppFrameMsgIn();
    frame.setApiEvent(event);
    sendMessageToApp(frame);
  }

  private void sendUserApiEventMsg(ApiEventType type, MirrorWebSocketConnection r) {
    ApiEventMsgIn event;
    if (r != null) {
      event = new ApiEventMsgIn(type, r.getUserId(), r.getUserAttributes());
    } else {
      event = new ApiEventMsgIn(type, null, null);
    }
    ServerToAppFrameMsgIn frame = new ServerToAppFrameMsgIn();
    frame.setApiEvent(event);
    sendMessageToApp(frame);
  }

  @Override
  public SwingSessionMsgOut toSwingSession() {
    SwingSessionMsgOut session = new SwingSessionMsgOut();
    session.setRecordingStatus(RecordingStatusEnum.NOT_RECORDING);
    session.setMirroringStatus(MirroringStatusEnum.NOT_MIRRORING);
    if (sessionData != null) {
      session.setApplet(false);
      session.setLoggingEnabled(sessionData.isSessionLoggingEnabled());
      session.setRecordingFile(sessionData.getRecordingFile());
      session.setRecordingStatus(sessionData.getRecordingStatus());
      session.setMirroringStatus(sessionData.getMirroringStatus());
    }
    session.setApplication(appName);
    session.setApplicationPath(pathMapping);
    session.setConnected(getConnectionId() != null);
    WebSocketUserInfo info;
    if (webConnection == null) {
      info = lastConnection;
    } else {
      info = webConnection.getUserInfo();
    }
    if (info.getDisconnectedSince() != null) {
      session.setDisconnectedSince(info.getDisconnectedSince().getTime());
    }
    if (getEndedAt() != null) {
      session.setEndedAt(getEndedAt().getTime());
    }
    session.setInstanceId(getInstanceId());
    if (getStartedAt() != null) {
      session.setStartedAt(getStartedAt().getTime());
    }
    session.setStatisticsLoggingEnabled(isStatisticsLoggingEnabled());
    session.setThreadDumps(getThreadDumps());
    session.setUser(info.getUserId());
    session.setUserBrowser(info.getUserBrowser());
    session.setUserIp(info.getUserIp());
    session.setUserOs(info.getUserOs());
    if (isRunning()) {
      session.setWarningHistory(
          poolConnector.getConnectedInstanceWarningHistory(pathMapping, getInstanceId()));
    } else {
      session.setWarningHistory(warningHistoryLog);
    }

    session.setStatus(getInstanceStatus());

    return session;
  }

  private StatusEnum getInstanceStatus() {
    if (processStatus == null) {
      return StatusEnum.NOT_STARTED;
    }

    if (processStatus == ProcessStatusEnum.RUNNING) {
      if (isRunning()) {
        return StatusEnum.RUNNING;
      }
      return StatusEnum.EXITING;
    }

    if (processStatus == ProcessStatusEnum.FORCE_KILLED) {
      return StatusEnum.FORCE_KILLED;
    }

    return StatusEnum.FINISHED;
  }

  private List<org.webswing.model.adminconsole.out.ThreadDumpMsgOut> getThreadDumps() {
    List<org.webswing.model.adminconsole.out.ThreadDumpMsgOut> result = new ArrayList<>();
    for (ThreadDumpMsgOut dump : threadDumps.values()) {
      // don't send content
      result.add(new org.webswing.model.adminconsole.out.ThreadDumpMsgOut(getInstanceId(),
          dump.getTimestamp(), null, dump.getReason()));
    }
    return result;
  }

  @Override
  public boolean isStatisticsLoggingEnabled() {
    if (sessionData != null) {
      return sessionData.isStatisticsLoggingEnabled();
    }
    return true;
  }

  @Override
  public void toggleStatisticsLogging(boolean enabled) {
    sendSimpleEvent(enabled ? SimpleEventType.enableStatisticsLogging
        : SimpleEventType.disableStatisticsLogging);
  }

  public String getPathMapping() {
    return pathMapping;
  }

  @Override
  public String getAppName() {
    return appName;
  }

  @Override
  public void updateProcessStatus(ProcessStatusEnum processStatus) {
    this.processStatus = processStatus;
  }

  @Override
  public void setConnectionId(String connectionId) {
    // not implemented, connectionId is defined by actual websocket connection
  }

}
