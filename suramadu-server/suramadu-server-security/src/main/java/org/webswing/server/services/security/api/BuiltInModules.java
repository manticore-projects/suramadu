package org.webswing.server.services.security.api;

import org.webswing.server.services.security.modules.anonym.AnonymSecurityModule;
import org.webswing.server.services.security.modules.embeded.EmbededSecurityModule;
import org.webswing.server.services.security.modules.shiro.ShiroSecurityModule;

public enum BuiltInModules {
  INHERITED(null), NONE(AnonymSecurityModule.class.getName()), EMBEDDED(
      EmbededSecurityModule.class.getName()), SHIRO(ShiroSecurityModule.class.getName());

  private final String type;

  private BuiltInModules(String type) {
    this.type = type;

  }

  public String getType() {
    return type;
  }

  public static String getSecurityModuleClassName(String module) {
    try {
      if ("EMBEDED".equals(module)) {// keep backwards compatible with previous name after fixed
                                     // spelling (double 'D' typo )
        module = EMBEDDED.name();
      }
      BuiltInModules builtInModule = BuiltInModules.valueOf(module);
      return builtInModule.getType();
    } catch (Exception e) {
      return module;
    }
  }

}
