package org.webswing.server.api.services.websocket.util;

import com.google.inject.Inject;
import com.google.inject.Injector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.Constants;
import org.webswing.server.common.service.security.SecurityManagerService;
import org.webswing.server.common.service.security.impl.WebswingSecuritySubject;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.util.List;

public class BrowserWebSocketConfigurator extends ServerEndpointConfig.Configurator {

  private static final Logger log = LoggerFactory.getLogger(BrowserWebSocketConfigurator.class);

  public static final String HANDSHAKE_PARAM_FILE = "file";
  public static final String HEADER_USER_AGENT = "user-agent";
  public static final String AUTHENTICATED = "authenticated";

  @Inject
  private static Injector injector;

  @Override
  public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
    return injector.getInstance(endpointClass);
  }

  @Override
  public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request,
      HandshakeResponse response) {
    if (request.getHeaders().containsKey(HEADER_USER_AGENT)) {
      sec.getUserProperties().put(HEADER_USER_AGENT,
          request.getHeaders().get(HEADER_USER_AGENT).get(0));
    }

    WebswingSecuritySubject subject = WebswingSecuritySubject.buildFrom(request.getParameterMap());
    sec.getUserProperties().put(SecurityManagerService.SECURITY_SUBJECT, subject);

    handleParameter(Constants.HTTP_ATTR_RECORDING_FLAG, request, sec);
    handleParameter(HANDSHAKE_PARAM_FILE, request, sec);
    handleParameter(Constants.HTTP_ATTR_ARGS, request, sec);
    handleParameter(Constants.HTTP_ATTR_DEBUG_PORT, request, sec);

    sec.getUserProperties().put(AUTHENTICATED, subject != null && subject.isAuthenticated());
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
