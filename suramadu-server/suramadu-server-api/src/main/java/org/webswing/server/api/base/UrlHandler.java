package org.webswing.server.api.base;

import org.webswing.server.api.services.security.login.SecuredPathHandler;
import org.webswing.server.common.model.security.WebswingAction;
import org.webswing.server.common.service.security.AbstractWebswingUser;
import org.webswing.server.model.exception.WsException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UrlHandler {

  void init();

  void destroy();

  boolean serve(HttpServletRequest req, HttpServletResponse res) throws WsException;

  void registerFirstChildUrlHandler(UrlHandler handler);

  void registerChildUrlHandler(UrlHandler handler);

  void removeChildUrlHandler(UrlHandler Handler);

  ServletContext getServletContext();

  long getLastModified(HttpServletRequest req);

  String getPathMapping();

  String getFullPathMapping();

  String getSecuredPath();

  UrlHandler getRootHandler();

  AbstractWebswingUser getUser();

  void checkPermission(WebswingAction action) throws WsException;

  void checkMasterPermission(WebswingAction action) throws WsException;

  SecuredPathHandler getSecurityProvider();

}
