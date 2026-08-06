package org.webswing.server.api.services.security.modules;

import org.webswing.server.services.security.api.SecurityContext;
import org.webswing.server.services.security.api.WebswingSecurityConfig;
import org.webswing.server.services.security.api.WebswingSecurityModule;

public interface SecurityModuleFactory {

  WebswingSecurityModule create(SecurityContext context, WebswingSecurityConfig config);

  WebswingSecurityModule createNoAccess(String msgKey, SecurityContext context,
      WebswingSecurityConfig config);
}
