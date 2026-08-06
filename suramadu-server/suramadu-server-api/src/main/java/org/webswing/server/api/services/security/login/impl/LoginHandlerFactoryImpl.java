package org.webswing.server.api.services.security.login.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.webswing.server.api.base.UrlHandler;
import org.webswing.server.api.services.security.login.LoginHandler;
import org.webswing.server.api.services.security.login.LoginHandlerFactory;
import org.webswing.server.api.services.security.login.LogoutHandler;
import org.webswing.server.api.services.websocket.WebSocketService;

@Singleton
public class LoginHandlerFactoryImpl implements LoginHandlerFactory {

  private final WebSocketService webSockets;

  @Inject
  public LoginHandlerFactoryImpl(WebSocketService webSockets) {
    this.webSockets = webSockets;
  }

  public LoginHandler createLoginHandler(UrlHandler parent) {
    return new LoginHandlerImpl(parent);
  }

  public LogoutHandler createLogoutHandler(UrlHandler parent) {
    return new LogoutHandlerImpl(webSockets, parent);
  }
}
