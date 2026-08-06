package org.webswing.server.services.security.modules.noaccess;

import org.webswing.server.common.service.security.AuthenticatedWebswingUser;
import org.webswing.server.services.security.api.WebswingAuthenticationException;
import org.webswing.server.services.security.api.WebswingSecurityModuleConfig;
import org.webswing.server.services.security.modules.AbstractSecurityModule;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class NoAccessSecurityModule extends AbstractSecurityModule<WebswingSecurityModuleConfig> {

  private String msg = WebswingAuthenticationException.NO_ACCESS;

  public NoAccessSecurityModule(String msgKey, WebswingSecurityModuleConfig config) {
    super(config);
    this.msg = msgKey == null ? this.msg : msgKey;
  }

  @Override
  protected AuthenticatedWebswingUser authenticate(HttpServletRequest request)
      throws WebswingAuthenticationException {
    throw new WebswingAuthenticationException("${" + msg + "}");
  }

  @Override
  protected void serveLoginPartial(HttpServletRequest request, HttpServletResponse response,
      WebswingAuthenticationException exception) throws IOException {
    sendPartialHtml(request, response, "errorPartial.html", exception);
  }
}
