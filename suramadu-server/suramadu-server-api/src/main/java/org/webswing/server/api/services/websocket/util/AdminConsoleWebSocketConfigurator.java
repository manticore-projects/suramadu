package org.webswing.server.api.services.websocket.util;

import com.google.inject.Inject;
import com.google.inject.Injector;

import javax.websocket.server.ServerEndpointConfig;

public class AdminConsoleWebSocketConfigurator extends ServerEndpointConfig.Configurator {

  @Inject
  private static Injector injector;

  @Override
  public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
    return injector.getInstance(endpointClass);
  }

}
