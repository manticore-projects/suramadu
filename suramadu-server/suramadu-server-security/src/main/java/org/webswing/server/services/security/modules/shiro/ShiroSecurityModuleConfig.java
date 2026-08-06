package org.webswing.server.services.security.modules.shiro;

import org.webswing.Constants;
import org.webswing.server.common.model.meta.*;
import org.webswing.server.services.security.extension.api.WebswingExtendableSecurityModuleConfig;

public interface ShiroSecurityModuleConfig extends WebswingExtendableSecurityModuleConfig {

  @ConfigField(label = "File", description = "Path pointing to Shiro INI file.")
  @ConfigFieldDefaultValueString("${" + Constants.ROOT_DIR_PATH + "}/shiro.ini")
  @ConfigFieldVariables(VariableSetName.SwingApp)
  String getFile();

  @ConfigField(label = "Hot Reload",
      description = "If enabled, the module will monitor the INI file for changes and reload the security configuration without restarting the server.")
  @ConfigFieldDefaultValueBoolean(true)
  boolean isHotReload();
}
