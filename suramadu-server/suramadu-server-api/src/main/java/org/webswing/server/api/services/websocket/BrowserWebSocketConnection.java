package org.webswing.server.api.services.websocket;

import org.webswing.model.appframe.out.AppFrameMsgOut;
import org.webswing.model.browser.out.ServerToBrowserFrameMsgOut;
import org.webswing.server.api.services.swinginstance.ConnectedSwingInstance;
import org.webswing.server.common.model.security.WebswingAction;
import org.webswing.server.common.service.security.AbstractWebswingUser;
import org.webswing.server.model.exception.WsException;

public interface BrowserWebSocketConnection extends WebSocketConnection {

  void sendMessage(ServerToBrowserFrameMsgOut msgOut);

  void sendMessage(AppFrameMsgOut frame);

  void disconnect(String reason);

  String getUserId();

  AbstractWebswingUser getUser();

  boolean hasPermission(WebswingAction action);

  void checkPermissionLocalOrMaster(WebswingAction websocketStartmirrorview) throws WsException;

  void instanceConnected(ConnectedSwingInstance instance);

}
