package org.webswing.server.api.services.websocket;

import org.webswing.model.app.in.ServerToAppFrameMsgIn;
import org.webswing.server.api.services.swinginstance.ConnectedSwingInstance;

public interface ApplicationWebSocketConnection extends WebSocketConnection {

  String getInstanceId();

  void sendMessage(ServerToAppFrameMsgIn msgIn, boolean logStats);

  void sendMessage(ServerToAppFrameMsgIn msgIn);

  void disconnect(String reason);

  void instanceConnected(ConnectedSwingInstance instance);

  String getSessionPoolId();

  boolean isConnected();

}
