package org.webswing.server.common.service.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface SecurityManagerService {
  public static final String SECURITY_SUBJECT = "webswingSecuritySubject";

  Object secure(SecurableService handler, HttpServletRequest req, HttpServletResponse res);

}
