package org.webswing.server.services.security.modules.configtest;

import org.webswing.server.services.security.api.WebswingSecurityModuleProvider;

import java.util.Arrays;
import java.util.List;

public class TestSecurityModuleProvider implements WebswingSecurityModuleProvider {
  @Override
  public List<String> getSecurityModuleClassNames() {
    return Arrays
        .asList("org.webswing.server.services.security.modules.configtest.TestSecurityModule");
  }
}
