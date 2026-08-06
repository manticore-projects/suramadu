package org.webswing.server.common.service.security.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.Constants;
import org.webswing.server.common.service.security.AbstractWebswingUser;
import org.webswing.server.common.service.security.AuthenticatedWebswingUser;
import org.webswing.server.common.service.security.WebswingLoginSessionTokenClaim;
import org.webswing.server.common.service.security.WebswingTokenClaim;
import org.webswing.server.common.util.JwtUtil;
import org.webswing.server.common.util.ServerUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class WebswingSecuritySubject {

  private static final Logger log = LoggerFactory.getLogger(WebswingSecuritySubject.class);

  protected static final ThreadLocal<WebswingSecuritySubject> subjects =
      new ThreadLocal<WebswingSecuritySubject>();

  protected WebswingTokenClaim webswingClaim = new WebswingTokenClaim();
  private WebswingLoginSessionTokenClaim loginSessionWebswingClaim =
      new WebswingLoginSessionTokenClaim();

  private enum SubjectType {
    access, transfer
  }

  public static WebswingSecuritySubject get() {
    return subjects.get();
  }

  public static WebswingSecuritySubject buildFrom(HttpServletRequestWrapper request) {
    String token = ServerUtil.extractBearerToken(request);

    WebswingSecuritySubject subject = getFromToken(token, SubjectType.access);

    String host = ServerUtil.getClientIp(request);
    subject.setHost(host);

    return subject;
  }

  public static WebswingSecuritySubject buildFrom(String token) {
    return getFromToken(token, SubjectType.access);
  }

  public static WebswingSecuritySubject buildAndSetTransferSubjectFrom(HttpServletRequest request) {
    String token =
        ServerUtil.parseTokenFromCookie(request, Constants.WEBSWING_SESSION_TRANSFER_TOKEN);

    WebswingSecuritySubject subject = getFromToken(token, SubjectType.transfer);
    subjects.set(subject);
    return subject;
  }

  public static WebswingSecuritySubject buildFrom(Map<String, List<String>> map) {
    String token = ServerUtil.extractBearerToken(map);

    return getFromToken(token, SubjectType.access);
  }

  private static WebswingSecuritySubject getFromToken(String token, SubjectType subjectType) {
    WebswingSecuritySubject result = new WebswingSecuritySubject();

    if (token == null) {
      return result;
    }

    try {
      Map<String, Object> claims = null;
      if (subjectType == SubjectType.transfer) {
        claims = JwtUtil.parseTransferTokenClaims(token); // read and validate
      } else {
        claims = JwtUtil.parseAccessTokenClaims(token); // read and validate
      }

      if (claims == null) {
        // invalid token
        return result;
      }

      String webswingClaim = (String) claims.get(Constants.JWT_CLAIM_WEBSWING);

      if (StringUtils.isBlank(webswingClaim)) {
        log.error("Empty webswing claim in token [{}]!", token);
        return null;
      }

      result.webswingClaim = JwtUtil.deserializeWebswingClaim(webswingClaim);
    } catch (Exception e) {
      log.error("Failed to parse JWT token [{}]!", token, e);
    }

    return result;
  }

  public WebswingSecuritySubject() {}

  <V> V execute(Callable<V> callable) throws ExecutionException {
    // FIXME made this method public to be able to create a different implementation of
    // SecurityManagerService
    try {
      subjects.set(this);
      return callable.call();
    } catch (Throwable t) {
      throw new ExecutionException(t);
    } finally {
      subjects.remove();
    }
  }

  public void initLoginSession(HttpServletRequest req) {
    try {
      String loginSessionToken =
          ServerUtil.parseTokenFromCookie(req, Constants.WEBSWING_SESSION_LOGIN_SESSION_TOKEN);
      if (StringUtils.isBlank(loginSessionToken)) {
        return;
      }

      Map<String, Object> claims = JwtUtil.parseLoginSessionTokenClaims(loginSessionToken);
      if (claims == null) {
        return;
      }

      String loginSessionClaim = (String) claims.get(Constants.JWT_CLAIM_WEBSWING_LOGIN_SESSION);

      if (StringUtils.isBlank(loginSessionClaim)) {
        return;
      }

      this.loginSessionWebswingClaim =
          JwtUtil.deserializeWebswingLoginSessionClaim(loginSessionClaim);
    } catch (Exception e) {
      // ignore
      log.debug("Could not parse login session token!", e);
    }
  }

  private Map<String, AbstractWebswingUser> getUserMap() {
    return webswingClaim.getUserMap();
  }

  private void setHost(String host) {
    webswingClaim.setHost(host);
  }

  public String getHost() {
    return webswingClaim.getHost();
  }

  public Object getAttribute(String attributeName) {
    if (loginSessionWebswingClaim != null && loginSessionWebswingClaim.getAttributes() != null) {
      if (loginSessionWebswingClaim.getAttributes().containsKey(attributeName)) {
        return loginSessionWebswingClaim.getAttributes().get(attributeName);
      }
    }
    return webswingClaim.getAttributes().get(attributeName);
  }

  public void setAttribute(String attributeName, Object value) {
    if (loginSessionWebswingClaim != null && loginSessionWebswingClaim.getAttributes() != null) {
      if (value == null) {
        loginSessionWebswingClaim.getAttributes().remove(attributeName);
      } else {
        loginSessionWebswingClaim.getAttributes().put(attributeName, value);
      }
      return;
    }
    webswingClaim.getAttributes().put(attributeName, value);
  }

  public boolean isAuthenticated() {
    return !getUserMap().isEmpty();
  }

  public AbstractWebswingUser getUserForSecuredPath(String securedPath) {
    return getUserMap().get(securedPath);
  }

  public void login(HttpServletResponse resp, String securedPath,
      AuthenticatedWebswingUser resolvedUser) {
    getUserMap().put(securedPath, new AbstractWebswingUser(resolvedUser));

    // copy attributes from login session to webswing claim
    if (loginSessionWebswingClaim != null && loginSessionWebswingClaim.getAttributes() != null) {
      webswingClaim.getAttributes().putAll(loginSessionWebswingClaim.getAttributes());
    }

    String serializedWebswingClaim = JwtUtil.serializeWebswingClaim(webswingClaim);
    ServerUtil.writeTokens(resp, serializedWebswingClaim, false);

    ServerUtil.clearLoginTokenFromCookies(resp);
  }

  public void logout(HttpServletResponse resp, String securedPath) {
    getUserMap().remove(securedPath);

    if (!getUserMap().isEmpty()) {
      String serializedWebswingClaim = JwtUtil.serializeWebswingClaim(webswingClaim);
      ServerUtil.writeTokens(resp, serializedWebswingClaim, true);
    } else {
      ServerUtil.clearTokensFromCookies(resp);
    }
  }

  public void saveLoginSession(HttpServletResponse resp) {
    ServerUtil.writeLoginSessionToken(resp,
        JwtUtil.serializeWebswingLoginSessionClaim(loginSessionWebswingClaim));
  }

  public static String fixClaimForAdminConsole(String webswingClaim, String servletPrefix) {
    try {
      WebswingTokenClaim acWebswingClaim = JwtUtil.deserializeWebswingClaim(webswingClaim);
      if (acWebswingClaim.getUserMap() != null) {
        Map<String, AbstractWebswingUser> updatedUsers = new HashMap<>();

        Iterator<Entry<String, AbstractWebswingUser>> it =
            acWebswingClaim.getUserMap().entrySet().iterator();
        while (it.hasNext()) {
          Entry<String, AbstractWebswingUser> entry = it.next();
          String path = entry.getKey();
          if (path.startsWith(servletPrefix)) {
            String newPath = path.substring(path.indexOf(servletPrefix) + servletPrefix.length());
            updatedUsers.put(newPath, entry.getValue());
            it.remove();
          }
        }

        acWebswingClaim.getUserMap().putAll(updatedUsers);
      }
      return JwtUtil.serializeWebswingClaim(acWebswingClaim);
    } catch (Exception e) {
      log.error("Could not deserialize webswing token claim!", e);
    }
    return webswingClaim;
  }

}
