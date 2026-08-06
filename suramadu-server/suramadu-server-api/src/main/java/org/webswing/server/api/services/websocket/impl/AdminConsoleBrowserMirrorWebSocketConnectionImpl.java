package org.webswing.server.api.services.websocket.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.model.adminconsole.out.AdminConsoleFrameMsgOut;
import org.webswing.model.adminconsole.out.MirrorFrameMsgOut;
import org.webswing.model.appframe.out.AppFrameMsgOut;
import org.webswing.model.appframe.out.StartApplicationMsgOut;
import org.webswing.model.browser.in.BrowserToServerFrameMsgIn;
import org.webswing.model.browser.out.ServerToBrowserFrameMsgOut;
import org.webswing.server.api.services.application.AppPathHandler;
import org.webswing.server.api.services.sessionpool.SessionPoolHolderService;
import org.webswing.server.api.services.swinginstance.ConnectedSwingInstance;
import org.webswing.server.api.services.websocket.AdminConsoleWebSocketConnection;
import org.webswing.server.api.services.websocket.MirrorWebSocketConnection;
import org.webswing.server.api.services.websocket.WebSocketService;
import org.webswing.server.api.util.SecurityUtil;
import org.webswing.server.common.model.security.WebswingAction;
import org.webswing.server.common.service.security.AbstractWebswingUser;
import org.webswing.server.common.service.security.impl.WebswingSecuritySubject;
import org.webswing.server.common.util.ProtoMapper;
import org.webswing.server.model.exception.WsException;
import tools.jackson.databind.json.JsonMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This is a proxy class between real BrowserMirrorWebSocketConnection class in Admin Console Server
 * and SwingInstance.
 */
public class AdminConsoleBrowserMirrorWebSocketConnectionImpl implements MirrorWebSocketConnection {

  private static final Logger log =
      LoggerFactory.getLogger(AdminConsoleBrowserMirrorWebSocketConnectionImpl.class);

  private final ProtoMapper protoMapper =
      new ProtoMapper(ProtoMapper.PROTO_PACKAGE_SERVER_BROWSER_FRAME,
          ProtoMapper.PROTO_PACKAGE_SERVER_BROWSER_FRAME);
  private final ProtoMapper appFrameProtoMapper = new ProtoMapper(
      ProtoMapper.PROTO_PACKAGE_APPFRAME_OUT, ProtoMapper.PROTO_PACKAGE_APPFRAME_IN);

  private final String browserSessionId;
  private final SessionPoolHolderService sessionPoolService;
  private final WebSocketService webSocketService;
  private ConnectedSwingInstance instance;
  private byte[] userAttributes;
  private AbstractWebswingUser user;
  private AbstractWebswingUser masterUser;

  private boolean connected;

  public AdminConsoleBrowserMirrorWebSocketConnectionImpl(
      SessionPoolHolderService sessionPoolService, WebSocketService webSocketService,
      ConnectedSwingInstance instance, String browserSessionId, String token) throws WsException {
    this.browserSessionId = browserSessionId;
    this.sessionPoolService = sessionPoolService;
    this.webSocketService = webSocketService;
    this.instance = instance;

    open(token);
  }

  private void open(String token) throws WsException {
    try {
      AppPathHandler appPathHandler = webSocketService.getAppPathHandler(instance.getPathMapping());
      WebswingSecuritySubject subject = WebswingSecuritySubject.buildFrom(token);

      this.user = SecurityUtil.resolveUser(subject, appPathHandler);
      this.masterUser = SecurityUtil.resolveUser(subject, appPathHandler.getRootHandler());

      if (subject == null || !subject.isAuthenticated() || this.user == null) {
        throw new IllegalStateException("Unauthorized access!");
      }

      checkPermissionLocalOrMaster(WebswingAction.websocket_startMirrorView);

      if (user.getUserAttributes() != null) {
        userAttributes = new JsonMapper().writeValueAsBytes(user.getUserAttributes());
      }

      connected = true;
    } catch (Exception e) {
      disconnect("Unauthorized access!");
      log.error("Error opening mirror connection [{}]!", instance.getInstanceId(), e);
      throw new WsException(e);
    }
  }

  @Override
  public void sendMessage(ServerToBrowserFrameMsgOut msgOut) {
    AdminConsoleWebSocketConnection ac = sessionPoolService.getAdminConsoleConnection();
    if (ac == null || !ac.isConnected()) {
      log.debug("Cannot send mirror message to admin console, not connected [{}]!",
          getInstanceId());
      return;
    }

    try {
      byte[] encoded = protoMapper.encodeProto(msgOut);

      AdminConsoleFrameMsgOut acMsgOut = new AdminConsoleFrameMsgOut();
      MirrorFrameMsgOut mirrorFrame = new MirrorFrameMsgOut();
      mirrorFrame.setFrame(encoded);
      mirrorFrame.setInstanceId(getInstanceId());
      mirrorFrame.setSessionId(browserSessionId);
      acMsgOut.setMirrorFrame(mirrorFrame);

      ac.sendMessage(acMsgOut);
    } catch (IOException e) {
      log.error("Error sending mirror msg to admin console [{}]!", getInstanceId(), e);
    }
  }

  @Override
  public void sendMessage(AppFrameMsgOut frame) {
    try {
      ServerToBrowserFrameMsgOut msgOut = new ServerToBrowserFrameMsgOut();
      msgOut.setAppFrameMsgOut(appFrameProtoMapper.encodeProto(frame));
      sendMessage(msgOut);
    } catch (IOException e) {
      log.error("Could not encode AppFrameMsgOut for admin console mirror msg [{}]!",
          getInstanceId(), e);
    }
  }

  @Override
  public void handleBrowserMirrorMessage(byte[] frame) {
    if (instance == null) {
      return;
    }

    try {
      BrowserToServerFrameMsgIn msgIn =
          protoMapper.decodeProto(frame, BrowserToServerFrameMsgIn.class);

      if (msgIn.getHandshake() != null) {
        // do not resend handshake from mirror
        // TODO we could probably remove sending handshake from browser for mirror connections,
        // since we don't use it anymore
        // it is enough for the browser to open mirror connection without a handshake
        return;
      }

      instance.handleBrowserMessage(msgIn);
    } catch (IOException e) {
      log.error("Could not decode BrowserToServerFrameMsgIn from admin console mirror msg [{}]!",
          getInstanceId(), e);
    }
  }

  @Override
  public void disconnect(String reason) {
    String instanceId = getInstanceId();
    connected = false;
    this.instance = null;

    AdminConsoleWebSocketConnection ac = sessionPoolService.getAdminConsoleConnection();
    if (ac == null || !ac.isConnected()) {
      log.error("Cannot send mirror message to admin console, not connected [{}]!",
          getInstanceId());
      return;
    }

    AdminConsoleFrameMsgOut acMsgOut = new AdminConsoleFrameMsgOut();
    MirrorFrameMsgOut mirrorFrame = new MirrorFrameMsgOut();
    mirrorFrame.setInstanceId(instanceId);
    mirrorFrame.setSessionId(browserSessionId);
    mirrorFrame.setDisconnect(true);
    acMsgOut.setMirrorFrame(mirrorFrame);

    ac.sendMessage(acMsgOut);
  }

  @Override
  public String getMirrorSessionId() {
    return browserSessionId;
  }

  @Override
  public String getUserId() {
    AbstractWebswingUser user = getUser();
    String userId = user != null ? user.getUserId() : "null";
    return userId;
  }

  @Override
  public byte[] getUserAttributes() {
    return userAttributes;
  }

  @Override
  public boolean hasPermission(WebswingAction action) {
    return getUser() != null && getUser().isPermitted(action.name());
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
  public AbstractWebswingUser getUser() {
    return user;
  }

  @Override
  public void instanceConnected(ConnectedSwingInstance instance) {
    AppFrameMsgOut appFrame = new AppFrameMsgOut();
    appFrame.setStartApplication(new StartApplicationMsgOut());
    appFrame.setInstanceId(instance.getInstanceId());
    sendMessage(appFrame);
  }

  @Override
  public boolean isConnected() {
    return connected;
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

  private String getInstanceId() {
    return instance == null ? null : instance.getInstanceId();
  }

}
