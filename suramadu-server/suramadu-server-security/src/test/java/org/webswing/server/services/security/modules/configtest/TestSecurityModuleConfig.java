package org.webswing.server.services.security.modules.configtest;

import org.webswing.server.common.model.meta.ConfigField;
import org.webswing.server.services.security.api.WebswingSecurityModuleConfig;

public interface TestSecurityModuleConfig extends WebswingSecurityModuleConfig {

  @ConfigField(label = "Test Configuration")
  ExampleForm getExampleForm();
}
