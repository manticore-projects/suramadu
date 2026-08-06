package org.webswing.server.api.services.security.login.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.server.api.base.AbstractUrlHandler;
import org.webswing.server.api.base.UrlHandler;
import org.webswing.server.api.services.security.login.LogoutHandler;
import org.webswing.server.api.services.websocket.WebSocketService;
import org.webswing.server.common.service.security.AbstractWebswingUser;
import org.webswing.server.common.service.security.impl.WebswingSecuritySubject;
import org.webswing.server.model.exception.WsException;
import org.webswing.server.services.security.api.WebswingSecurityModule;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogoutHandlerImpl extends AbstractUrlHandler implements LogoutHandler {

  private static final Logger log = LoggerFactory.getLogger(LogoutHandlerImpl.class);

  private final WebSocketService webSockets;

  public LogoutHandlerImpl(WebSocketService webSockets, UrlHandler parent) {
    super(parent);
    this.webSockets = webSockets;
  }

  @Override
  protected String getPath() {
    return "logout";
  }

  @Override
  public boolean serve(HttpServletRequest req, HttpServletResponse res) throws WsException {
    AbstractWebswingUser user;
    try {
      if ("OPTIONS".equals(req.getMethod())) {
        return true; // cors preflight, don't forward to security module
      }
      user = logout(req, res);
    } catch (Exception e) {
      log.error("Failed to logout", e);
      throw new WsException("Failed to logout", e);
    }

    WebswingSecurityModule securityModuleWrapper = getSecurityProvider().get();
    try {
      securityModuleWrapper.doLogout(req, res, user);
    } catch (Exception e) {
      log.error("Failed Logout by SecurityModule.", e);
      throw new WsException("Failed Logout by SecurityModule.", e);
    }
    return true;
  }

  protected AbstractWebswingUser logout(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    AbstractWebswingUser user = getUser();
    if (user != null) {
      WebswingSecuritySubject subject = WebswingSecuritySubject.get();
      try {
        // logout only user for the secured path (in case other users are logged in the same
        // session)
        // return updated refresh token in set-cookie
        subject.logout(resp, getSecuredPath());
      } catch (Exception e) {
        log.error("Error while logging out!", e);
      }
      return user;
    }
    return null;
  }

}
