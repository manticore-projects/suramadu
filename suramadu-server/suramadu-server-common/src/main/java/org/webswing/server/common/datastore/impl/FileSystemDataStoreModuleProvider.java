package org.webswing.server.common.datastore.impl;

import org.webswing.server.common.datastore.WebswingDataStoreModuleProvider;

import java.util.Arrays;
import java.util.List;

public class FileSystemDataStoreModuleProvider implements WebswingDataStoreModuleProvider {

  @Override
  public List<String> getDataStoreModuleClassNames() {
    return Arrays.asList(FileSystemDataStoreModule.class.getCanonicalName());
  }

}
