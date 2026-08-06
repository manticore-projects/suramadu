package org.webswing.server.api.services.websocket;

public interface MirrorWebSocketConnection extends BrowserWebSocketConnection {

  byte[] getUserAttributes();

  void handleBrowserMirrorMessage(byte[] frame);

  String getMirrorSessionId();

}
