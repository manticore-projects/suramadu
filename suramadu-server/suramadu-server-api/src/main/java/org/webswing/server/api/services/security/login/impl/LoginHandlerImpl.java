package org.webswing.server.api.services.security.login.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.server.api.base.AbstractUrlHandler;
import org.webswing.server.api.base.UrlHandler;
import org.webswing.server.api.services.security.login.LoginHandler;
import org.webswing.server.common.service.security.AbstractWebswingUser;
import org.webswing.server.common.service.security.impl.WebswingSecuritySubject;
import org.webswing.server.model.exception.WsException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginHandlerImpl extends AbstractUrlHandler implements LoginHandler {

  private static final Logger log = LoggerFactory.getLogger(LoginHandlerImpl.class);

  public LoginHandlerImpl(UrlHandler parent) {
    super(parent);
  }

  @Override
  protected String getPath() {
    return "login";
  }

  @Override
  public boolean serve(HttpServletRequest req, HttpServletResponse res) throws WsException {
    handleCorsHeaders(req, res);
    try {
      if ("OPTIONS".equals(req.getMethod())) {
        return true; // cors preflight, don't forward to security module
      }
      WebswingSecuritySubject.get().initLoginSession(req);
      login(req, res);
      return true;
    } catch (Exception e) {
      log.error("Failed to process login request. {}", getFullPathMapping(), e);
      throw new WsException("Failed to login", e);
    }
  }

  protected void login(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    AbstractWebswingUser user = getUser();
    String path = getPathInfo(req);
    if (user != null && !"/".equals(path)) {
      getSecurityProvider().get().doServeAuthenticated(user, path, req, resp);
    } else {
      try {
        getSecurityProvider().get().doLogin(req, resp, getSecuredPath());
      } catch (Exception ux) {
        log.error("Unexpected authentication error.", ux);
      }
    }
  }

}
