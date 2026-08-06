package org.webswing.server.services.security.extension.api;

import org.webswing.server.services.security.extension.accessmapping.AccessMappingSecurityExtension;

public enum BuiltInModuleExtensions {
  AccessMapping(AccessMappingSecurityExtension.class.getName());

  private final String className;

  BuiltInModuleExtensions(String className) {
    this.className = className;
  }

  public String getClassName() {
    return className;
  }

  public static String getExtensionClassName(String name) {
    try {
      BuiltInModuleExtensions builtInExtensions = BuiltInModuleExtensions.valueOf(name);
      return builtInExtensions.getClassName();
    } catch (Exception e) {
      return name;
    }
  }
}
