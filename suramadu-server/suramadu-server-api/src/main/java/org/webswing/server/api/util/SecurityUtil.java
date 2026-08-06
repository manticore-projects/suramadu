package org.webswing.server.api.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.server.api.base.UrlHandler;
import org.webswing.server.common.model.security.WebswingAction;
import org.webswing.server.common.service.security.AbstractWebswingUser;
import org.webswing.server.common.service.security.impl.WebswingSecuritySubject;

public class SecurityUtil {

  private static final Logger log = LoggerFactory.getLogger(SecurityUtil.class);

  public static final String CLIENT_IP_SESSION_ATTR = "webswingClientIp";

  public static AbstractWebswingUser getUser(UrlHandler urlHandler) {
    WebswingSecuritySubject subject = WebswingSecuritySubject.get();
    return resolveUser(subject, urlHandler);
  }

  public static AbstractWebswingUser resolveUser(WebswingSecuritySubject subject,
      UrlHandler handler) {
    String securedPath = handler.getSecuredPath();
    if (subject != null && securedPath != null) {
      AbstractWebswingUser user = subject.getUserForSecuredPath(securedPath);
      if (user == null) {
        AbstractWebswingUser masteruser =
            subject.getUserForSecuredPath(handler.getRootHandler().getSecuredPath());
        if (masteruser != null
            && masteruser.isPermitted(WebswingAction.master_admin_access.name())) {
          return masteruser;
        }
      }
      return user;
    }
    return null;
  }

  public static Object getFromSecuritySession(String attributeName) {
    WebswingSecuritySubject subject = WebswingSecuritySubject.get();
    return subject.getAttribute(attributeName);
  }

  public static void setToSecuritySession(String attributeName, Object value) {
    WebswingSecuritySubject subject = WebswingSecuritySubject.get();
    subject.setAttribute(attributeName, value);
  }

  private SecurityUtil() {}

}
