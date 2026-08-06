package org.webswing.server.api.services.websocket;

import org.webswing.model.appframe.out.AppFrameMsgOut;

public interface RecordingWebSocketConnection extends WebSocketConnection {

  void sendMessage(AppFrameMsgOut frame);

}
