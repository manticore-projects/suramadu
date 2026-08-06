package org.webswing.server.api.services.websocket;

import org.webswing.model.adminconsole.out.AdminConsoleFrameMsgOut;

public interface AdminConsoleWebSocketConnection extends WebSocketConnection {

  boolean isConnected();

  void sendMessage(AdminConsoleFrameMsgOut msgOut);

}
