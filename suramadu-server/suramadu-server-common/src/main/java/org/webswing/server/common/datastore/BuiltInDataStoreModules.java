package org.webswing.server.common.datastore;

import org.webswing.server.common.datastore.impl.FileSystemDataStoreModule;

public enum BuiltInDataStoreModules {
  INHERITED(null), FILESYSTEM(FileSystemDataStoreModule.class.getName());

  private final String type;

  private BuiltInDataStoreModules(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }

  public static String getDataStoreModuleClassName(String module) {
    try {
      BuiltInDataStoreModules builtInModule = BuiltInDataStoreModules.valueOf(module);
      return builtInModule.getType();
    } catch (Exception e) {
      return module;
    }
  }

}
