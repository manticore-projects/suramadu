package org.webswing.server.api.services.websocket.impl;

import com.google.inject.Inject;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.Constants;
import org.webswing.model.Msg;
import org.webswing.model.appframe.out.AppFrameMsgOut;
import org.webswing.model.appframe.out.SimpleEventMsgOut;
import org.webswing.model.appframe.out.StartApplicationMsgOut;
import org.webswing.model.browser.in.BrowserToServerFrameMsgIn;
import org.webswing.model.browser.out.ServerToBrowserFrameMsgOut;
import org.webswing.model.common.in.ConnectionHandshakeMsgIn;
import org.webswing.server.api.services.application.AppPathHandler;
import org.webswing.server.api.services.sessionpool.SessionPoolHolderService;
import org.webswing.server.api.services.swinginstance.ConnectedSwingInstance;
import org.webswing.server.api.services.websocket.PrimaryWebSocketConnection;
import org.webswing.server.api.services.websocket.WebSocketService;
import org.webswing.server.api.services.websocket.WebSocketUserInfo;
import org.webswing.server.api.services.websocket.util.BrowserWebSocketConfigurator;
import org.webswing.server.api.util.SecurityUtil;
import org.webswing.server.common.model.security.WebswingAction;
import org.webswing.server.common.service.security.AbstractWebswingUser;
import org.webswing.server.common.service.security.SecurityManagerService;
import org.webswing.server.common.service.security.impl.WebswingSecuritySubject;
import org.webswing.server.common.service.stats.StatisticsLogger;
import org.webswing.server.common.util.ProtoMapper;
import org.webswing.server.common.util.ServerUtil;
import org.webswing.server.model.exception.WsException;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.*;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint(value = "/{appPath}/async/swing-bin",
    configurator = BrowserWebSocketConfigurator.class)
public class BrowserWebSocketConnectionImpl extends AbstractWebSocketConnection
    implements PrimaryWebSocketConnection {

  private static final Logger log = LoggerFactory.getLogger(BrowserWebSocketConnectionImpl.class);

  private ProtoMapper protoMapper = new ProtoMapper(ProtoMapper.PROTO_PACKAGE_SERVER_BROWSER_FRAME,
      ProtoMapper.PROTO_PACKAGE_SERVER_BROWSER_FRAME);
  private ProtoMapper appFrameProtoMapper = new ProtoMapper(ProtoMapper.PROTO_PACKAGE_APPFRAME_OUT,
      ProtoMapper.PROTO_PACKAGE_APPFRAME_IN);
  private WebSocketService webSocketService;
  private SessionPoolHolderService sessionPoolHolderService;
  private AppPathHandler appPathHandler;

  private WebSocketUserInfo webSocketUserInfo;
  private AbstractWebswingUser user;
  private AbstractWebswingUser masterUser;
  private String path;

  private ConnectedSwingInstance instance;

  private ConnectionHandshakeMsgIn reconnectHandshake;

  @Inject
  public BrowserWebSocketConnectionImpl(WebSocketService webSocketService,
      SessionPoolHolderService sessionPoolHolderService) {
    this.webSocketService = webSocketService;
    this.sessionPoolHolderService = sessionPoolHolderService;
  }

  @OnOpen
  public void onOpen(Session session, EndpointConfig config, @PathParam("appPath") String appPath) {
    super.onOpen(session, config);

    this.path = "/" + appPath;
    this.appPathHandler = webSocketService.getAppPathHandler(this.path);
    this.user = SecurityUtil.resolveUser(getSecuritySubject(), appPathHandler);
    this.masterUser =
        SecurityUtil.resolveUser(getSecuritySubject(), appPathHandler.getRootHandler());
    this.webSocketUserInfo = new WebSocketUserInfo(getUserId(), getClientBrowser(), getCustomArgs(),
        getDebugPort(), getRemoteAddr(), getClientOS()/*, getLogoutHandle()*/);

    if (!(Boolean) session.getUserProperties().get(BrowserWebSocketConfigurator.AUTHENTICATED)
        || this.user == null) {
      disconnect("Unauthorized access!");
      return;
    }

    try {
      if (hasPermission(WebswingAction.websocket_connect)) {
        try {
          AppFrameMsgOut appFrame = new AppFrameMsgOut();
          appFrame.setStartApplication(new StartApplicationMsgOut());

          ServerToBrowserFrameMsgOut msgOut = new ServerToBrowserFrameMsgOut();
          msgOut.setAppFrameMsgOut(appFrameProtoMapper.encodeProto(appFrame));

          sendMessage(msgOut);
        } catch (IOException e) {
          log.error("Could not encode proto message for browser [{}]!", session.getId(), e);
        }
      } else {
        ServerToBrowserFrameMsgOut msgOut = new ServerToBrowserFrameMsgOut();
        msgOut.setAppFrameMsgOut(
            appFrameProtoMapper.encodeProto(SimpleEventMsgOut.unauthorizedAccess.buildMsgOut()));

        sendMessage(msgOut);
        disconnect("Unauthorized access!");
        return;
      }
    } catch (IOException e) {
      log.error("Could not encode proto message for browser [{}]!", session.getId(), e);
    }
  }

  @OnMessage
  public void onMessage(Session session, byte[] bytes, boolean last) {
    try {
      Pair<Msg, Integer> frameWithLength = super.getCompleteMessage(bytes, last);
      if (frameWithLength == null) {
        // incomplete
        return;
      }
      BrowserToServerFrameMsgIn frame = (BrowserToServerFrameMsgIn) frameWithLength.getKey();

      boolean currentInstance = instance != null && uuid().equals(instance.getConnectionId());

      if (frame.getHandshake() != null) {
        if (currentInstance) {
          // resend handshake to instance
          instance.handleBrowserMessage(frame);
        } else {
          // instance not yet connected, try connect with handshake
          // this also handles reconnect
          // register handshake for possible reconnect
          reconnectHandshake = frame.getHandshake();
          appPathHandler.connectView(frame.getHandshake(), this);
        }
        return;
      }

      if (instance == null) {
        // ignore
        return;
      }

      instance.handleBrowserMessage(frame);

      sessionPoolHolderService.logStatValue(instance.getInstanceId(), path,
          StatisticsLogger.INBOUND_SIZE_METRIC, frameWithLength.getValue());
    } catch (IOException e) {
      log.error("Could not decode proto message from browser [{}]!", session.getId(), e);
    }
  }

  @OnMessage
  public void onPong(Session session, PongMessage pongMessage) {
    super.onPong(session, pongMessage, log);
  }

  @OnClose
  public void onClose(Session session, CloseReason closeReason) {
    if (session != null) {
      log.info("Websocket to browser closed, session [{}]{}", session.getId(),
          closeReason != null
              ? ", close code [" + closeReason.getCloseCode().getCode() + "], reason ["
                  + closeReason.getReasonPhrase() + "]!"
              : "");
      if (instance != null) {
        instance.browserDisconnected(session.getId());
      }
    }

    if (reconnectHandshake != null) {
      sessionPoolHolderService.unregisterReconnect(this);
    }
  }

  @OnError
  public void onError(Session session, Throwable t) {
    log.error("Websocket error from browser connection, session [{}], instanceId [{}] {}",
        session == null ? null : session.getId(),
        instance == null ? null : instance.getInstanceId(), t.getMessage());
    log.debug(t.getMessage(), t);
  }

  @Override
  public void disconnect(String reason) {
    if (session != null && session.isOpen()) {
      try {
        session.close(new CloseReason(CloseCodes.NORMAL_CLOSURE, reason));
      } catch (IOException e) {
        log.error("Failed to disconnect browser connection, session [{}], instanceId [{}] {}",
            session.getId(), instance == null ? null : instance.getInstanceId(), e.getMessage());
        log.debug(e.getMessage(), e);
      }
    }
  }

  @Override
  public void sendMessage(AppFrameMsgOut frame) {
    try {
      ServerToBrowserFrameMsgOut msgOut = new ServerToBrowserFrameMsgOut();
      msgOut.setAppFrameMsgOut(appFrameProtoMapper.encodeProto(frame));
      sendMessage(msgOut);
    } catch (IOException e) {
      log.error("Could not encode AppFrameMsgOut for session [{}]!", session.getId(), e);
    }
  }

  @Override
  public void sendMessage(ServerToBrowserFrameMsgOut msgOut) {
    try {
      byte[] encoded = protoMapper.encodeProto(msgOut);
      super.sendMessage(encoded);

      if (instance == null) {
        // not yet connected
        return;
      }
      sessionPoolHolderService.logStatValue(instance.getInstanceId(), path,
          StatisticsLogger.OUTBOUND_SIZE_METRIC, encoded.length);
    } catch (IOException e) {
      log.error("Failed to send msg to browser, session [{}], instanceId [{}] {}",
          session == null ? null : session.getId(),
          instance == null ? null : instance.getInstanceId(), e.getMessage());
      log.debug(e.getMessage(), e);
    }
  }

  @Override
  protected Msg decodeIncomingMessage(byte[] bytes) throws IOException {
    return protoMapper.decodeProto(bytes, BrowserToServerFrameMsgIn.class);
  }

  @Override
  public void instanceConnected(ConnectedSwingInstance instance) {
    this.instance = instance;

    try {
      AppFrameMsgOut appFrame = new AppFrameMsgOut();
      appFrame.setInstanceId(instance.getInstanceId());

      ServerToBrowserFrameMsgOut msgOut = new ServerToBrowserFrameMsgOut();
      msgOut.setAppFrameMsgOut(appFrameProtoMapper.encodeProto(appFrame));

      sendMessage(msgOut);
    } catch (IOException e) {
      log.error("Could not encode proto message for browser [{}]!", session.getId(), e);
    }
  }

  @Override
  public String uuid() {
    return session.getId();
  }

  @Override
  public WebSocketUserInfo getUserInfo() {
    return webSocketUserInfo;
  }

  @Override
  public AbstractWebswingUser getUser() {
    return user;
  }

  private String getRemoteAddr() {
    WebswingSecuritySubject subject = getSecuritySubject();

    if (subject != null) {
      String ip = (String) subject.getHost();

      if (ip != null) {
        return ip;
      }
    }

    return "Unknown";
  }

  @Override
  public String getUserId() {
    AbstractWebswingUser user = getUser();
    return user != null ? user.getUserId() : "null";
  }

  @Override
  public String getPath() {
    return path;
  }

  @Override
  public boolean hasPermission(WebswingAction action) {
    return getUser() == null ? false : getUser().isPermitted(action.name());
  }

  @Override
  public void checkPermissionLocalOrMaster(WebswingAction a) throws WsException {
    try {
      checkPermission(user, a);
    } catch (WsException e) {
      checkPermission(masterUser, a);
    }
  }

  @Override
  public ConnectionHandshakeMsgIn getReconnectHandshake() {
    return reconnectHandshake;
  }

  private void checkPermission(AbstractWebswingUser checkUser, WebswingAction action)
      throws WsException {
    if (checkUser != null) {
      if (checkUser.isPermitted(action.name())) {
        return;
      }
    }
    throw new WsException(
        "User '" + checkUser + "' is not allowed to execute action '" + action + "'",
        HttpServletResponse.SC_UNAUTHORIZED);
  }

  @Override
  public boolean isRecordingFlagged() {
    return Boolean
        .parseBoolean((String) session.getUserProperties().get(Constants.HTTP_ATTR_RECORDING_FLAG));
  }

  private String getCustomArgs() {
    String args = (String) session.getUserProperties().get(Constants.HTTP_ATTR_ARGS);
    return args != null ? args : "";
  }

  private int getDebugPort() {
    String debugPort = (String) session.getUserProperties().get(Constants.HTTP_ATTR_DEBUG_PORT);
    try {
      return Integer.parseInt(debugPort);
    } catch (NumberFormatException e) {
      return 0;
    }
  }

  private String getClientBrowser() {
    return ServerUtil.getClientBrowser(
        (String) session.getUserProperties().get(BrowserWebSocketConfigurator.HEADER_USER_AGENT));
  }

  private String getClientOS() {
    return ServerUtil.getClientOs(
        (String) session.getUserProperties().get(BrowserWebSocketConfigurator.HEADER_USER_AGENT));
  }

  private WebswingSecuritySubject getSecuritySubject() {
    if (session == null) {
      return null;
    }
    return (WebswingSecuritySubject) session.getUserProperties()
        .get(SecurityManagerService.SECURITY_SUBJECT);
  }

}
