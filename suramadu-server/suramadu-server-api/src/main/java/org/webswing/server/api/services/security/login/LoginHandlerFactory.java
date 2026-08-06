package org.webswing.server.api.services.security.login;

import org.webswing.server.api.base.UrlHandler;

public interface LoginHandlerFactory {

  LoginHandler createLoginHandler(UrlHandler parent);

  LogoutHandler createLogoutHandler(UrlHandler parent);

}
