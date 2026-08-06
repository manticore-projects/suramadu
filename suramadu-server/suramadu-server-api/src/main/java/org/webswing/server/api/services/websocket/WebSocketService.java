package org.webswing.server.api.services.websocket;

import org.webswing.server.api.base.WebswingService;
import org.webswing.server.api.services.application.AppPathHandler;

public interface WebSocketService extends WebswingService {

  void registerPathHandler(String path, AppPathHandler appPathHandler);

  void unregisterPathHandler(String path);

  AppPathHandler getAppPathHandler(String path);

}
