package org.webswing.server.services.security.modules.anonym;

import org.webswing.Constants;
import org.webswing.server.common.service.security.AuthenticatedWebswingUser;
import org.webswing.server.services.security.api.WebswingAuthenticationException;
import org.webswing.server.services.security.api.WebswingSecurityModuleConfig;
import org.webswing.server.services.security.modules.AbstractSecurityModule;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class AnonymSecurityModule extends AbstractSecurityModule<WebswingSecurityModuleConfig> {
  public static final String anonymUserName = "anonym";

  public AnonymSecurityModule(WebswingSecurityModuleConfig config) {
    super(config);
  }

  @Override
  protected AuthenticatedWebswingUser authenticate(HttpServletRequest request)
      throws WebswingAuthenticationException {
    Map<String, Object> loginRequest = getLoginRequest(request);
    String userId = loginRequest == null ? anonymUserName
        : (String) loginRequest.getOrDefault(Constants.HTTP_PARAM_SECURITY_TOKEN_HEADER,
            anonymUserName);
    logSuccess(request, userId + (anonymUserName.equals(userId) ? "" : "(" + anonymUserName + ")"));
    return new AnonymWebswingUser(userId);
  }

  @Override
  protected void serveLoginPage(HttpServletRequest request, HttpServletResponse response,
      WebswingAuthenticationException exception) throws IOException {}

  @Override
  protected void serveLoginPartial(HttpServletRequest request, HttpServletResponse response,
      WebswingAuthenticationException exception) throws IOException {}

  @Override
  public void doLogout(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    sendPartialHtml(request, response, "logoutPartial.html", null);
  }
}
