package org.webswing.server.api.services.websocket.util;

import com.google.inject.Inject;
import com.google.inject.Injector;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.util.List;

public class ApplicationWebSocketConfigurator extends ServerEndpointConfig.Configurator {

  public static final String HEADER_USER_AGENT = "user-agent";
  public static final String ATTR_INSTANCE_ID = "instanceId";
  public static final String ATTR_SESSION_POOL_ID = "sessionPoolId";
  public static final String ATTR_RECONNECT = "reconnect";

  @Inject
  private static Injector injector;

  @Override
  public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
    return injector.getInstance(endpointClass);
  }

  @Override
  public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request,
      HandshakeResponse response) {
    handleParameter(ATTR_INSTANCE_ID, request, sec);
    handleParameter(ATTR_SESSION_POOL_ID, request, sec);
    handleParameter(ATTR_RECONNECT, request, sec);
  }

  private void handleParameter(String paramName, HandshakeRequest request,
      ServerEndpointConfig sec) {
    if (request.getParameterMap() == null) {
      return;
    }
    if (!request.getParameterMap().containsKey(paramName)) {
      return;
    }

    List<String> paramValues = request.getParameterMap().get(paramName);

    if (paramValues == null || paramValues.isEmpty()) {
      return;
    }

    sec.getUserProperties().put(paramName, paramValues.get(0));
  }

}
