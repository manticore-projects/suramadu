package org.webswing.server.api.services.security.login;

import org.webswing.server.common.model.SecuredPathConfig;
import org.webswing.server.services.security.api.WebswingSecurityModule;

public interface SecuredPathHandler {

  WebswingSecurityModule get();

  SecuredPathConfig getConfig();

  void initConfiguration();

  String getPathMapping();

  boolean isEnabled();
}
