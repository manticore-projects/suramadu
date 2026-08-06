package org.webswing.server.api.services.websocket;

import org.webswing.server.api.base.UrlHandler;

public interface WebSocketMessageListener {

  void onReady(WebSocketConnection c);

  void onMessage(WebSocketConnection connection, Object message);

  void onDisconnect(WebSocketConnection connection);

  void onTimeout(WebSocketConnection connection);

  UrlHandler getOwner();

  boolean isReady();

}
