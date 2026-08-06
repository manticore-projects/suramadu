package org.webswing.server.common.service.security.impl;

import com.google.inject.Singleton;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.Constants;
import org.webswing.server.common.service.security.SecurableService;
import org.webswing.server.common.service.security.SecurityManagerService;
import org.webswing.server.common.util.ServerUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.concurrent.ExecutionException;

@Singleton
public class SecurityManagerServiceImpl implements SecurityManagerService {
  private static final Logger log = LoggerFactory.getLogger(SecurityManagerServiceImpl.class);

  public SecurityManagerServiceImpl() {}

  @Override
  public Object secure(final SecurableService handler, final HttpServletRequest req,
      final HttpServletResponse res) {
    final HttpServletRequestWrapper request = new SessionlessHttpServletRequestWrapper(req);

    final WebswingSecuritySubject subject = WebswingSecuritySubject.buildFrom(request);

    checkIPAddress(req, subject);

    try {
      return subject.execute(() -> handler.secureServe(req, res));
    } catch (ExecutionException e) {
      log.error("Failed to execute secured handler.", e);
      throw new RuntimeException(e);
    }
  }

  private void checkIPAddress(HttpServletRequest req, WebswingSecuritySubject subject) {
    // FIXME test this works
    if (Boolean.getBoolean(Constants.LINK_COOKIE_TO_IP)) {
      String currentIp = ServerUtil.getClientIp(req);
      String sessionIp = subject.getHost();
      if (!StringUtils.equals(currentIp, sessionIp)) {
        throw new RuntimeException("IP address does not match Session host!");
      }
    }
  }

  public static class SessionlessHttpServletRequestWrapper extends HttpServletRequestWrapper {

    public SessionlessHttpServletRequestWrapper(HttpServletRequest response) {
      super(response);
    }

    public HttpSession getSession() {
      throw new UnsupportedOperationException("Session storage is not supported.");
    }

    public HttpSession getSession(boolean create) {
      throw new UnsupportedOperationException("Session storage is not supported.");
    }
  };
}
