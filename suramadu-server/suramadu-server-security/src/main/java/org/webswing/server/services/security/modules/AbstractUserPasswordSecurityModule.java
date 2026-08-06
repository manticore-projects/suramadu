package org.webswing.server.services.security.modules;

import org.webswing.server.common.service.security.AuthenticatedWebswingUser;
import org.webswing.server.services.security.api.WebswingAuthenticationException;
import org.webswing.server.services.security.extension.api.WebswingExtendableSecurityModuleConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Helper Abstract Security Module that implements the most common authentication method using
 * username and password. Login page (full and partial) templates are provided.
 * 
 * @param <T>
 */
public abstract class AbstractUserPasswordSecurityModule<T extends WebswingExtendableSecurityModuleConfig>
    extends AbstractExtendableSecurityModule<T> {

  public AbstractUserPasswordSecurityModule(T config) {
    super(config);
  }

  public String getPartialTemplateName() {
    return "loginPartial.html";
  }

  public String getUserName(HttpServletRequest request) {
    // Only accept credentials from POST request body to prevent
    // leakage via URL query strings, browser history, server access logs, and Referer headers
    if (!"POST".equalsIgnoreCase(request.getMethod())) {
      return null;
    }
    return request.getParameter("username");
  }

  public String getPassword(HttpServletRequest request) {
    // Only accept credentials from POST request body
    if (!"POST".equalsIgnoreCase(request.getMethod())) {
      return null;
    }
    return request.getParameter("password");
  }

  @Override
  protected AuthenticatedWebswingUser authenticate(HttpServletRequest request)
      throws WebswingAuthenticationException {
    String username = getUserName(request);
    String password = getPassword(request);
    if (username != null || password != null) {
      try {
        AuthenticatedWebswingUser user = verifyUserPassword(username, password);
        if (user != null) {
          logSuccess(request, user.getUserId());
        }
        return user;
      } catch (WebswingAuthenticationException e) {
        logFailure(request, username, e.getMessage());
        throw e;
      }
    } else {
      return null;
    }
  }

  protected void serveLoginPartial(HttpServletRequest request, HttpServletResponse response,
      WebswingAuthenticationException exception) throws IOException {
    Map<String, Object> variables = new HashMap<>();
    if (exception != null) {
      // Use a generic error message for the user-facing template to prevent
      // information leakage. The detailed message is already captured in audit logs.
      String errorMessage = exception.getLocalizedMessage();
      if (errorMessage == null || errorMessage.isEmpty()) {
        errorMessage = "Authentication failed.";
      }
      variables.put("errorMessage", errorMessage);
    }
    sendPartialHtml(request, response, getPartialTemplateName(), variables);
  }

  /**
   * Check if username and password is valid. If it is valid return an instance of
   * {@link AuthenticatedWebswingUser} otherwise throw {@link WebswingAuthenticationException}.
   *
   * @throws WebswingAuthenticationException if authentication failed.
   */
  public abstract AuthenticatedWebswingUser verifyUserPassword(String user, String password)
      throws WebswingAuthenticationException;
}
