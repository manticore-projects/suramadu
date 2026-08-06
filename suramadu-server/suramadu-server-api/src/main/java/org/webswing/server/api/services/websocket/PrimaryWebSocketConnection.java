package org.webswing.server.api.services.websocket;

import org.webswing.model.common.in.ConnectionHandshakeMsgIn;

public interface PrimaryWebSocketConnection extends BrowserWebSocketConnection {

  String uuid();

  boolean isRecordingFlagged();

  ConnectionHandshakeMsgIn getReconnectHandshake();

  WebSocketUserInfo getUserInfo();

  String getPath();
}
