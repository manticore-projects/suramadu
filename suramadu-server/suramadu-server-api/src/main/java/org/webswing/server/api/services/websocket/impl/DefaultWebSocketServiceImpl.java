package org.webswing.server.api.services.websocket.impl;

import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.server.api.base.WebswingService;
import org.webswing.server.api.services.application.AppPathHandler;
import org.webswing.server.api.services.websocket.WebSocketService;
import org.webswing.server.model.exception.WsInitException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class DefaultWebSocketServiceImpl implements WebswingService, WebSocketService {
  private static final Logger log = LoggerFactory.getLogger(DefaultWebSocketServiceImpl.class);

  private Map<String, AppPathHandler> handlerMap = Collections.synchronizedMap(new HashMap<>());

  public DefaultWebSocketServiceImpl() {}

  public void start() throws WsInitException {}

  public void stop() {}

  @Override
  public void registerPathHandler(String path, AppPathHandler appPathHandler) {
    handlerMap.put(path, appPathHandler);
  }

  @Override
  public void unregisterPathHandler(String path) {
    handlerMap.remove(path);
  }

  @Override
  public AppPathHandler getAppPathHandler(String path) {
    AppPathHandler appPathHandler = handlerMap.get(path);

    if (appPathHandler == null) {
      log.error("No AppPathHandler found for path [{}]!", path);
      return null;
    }

    return appPathHandler;
  }

}
