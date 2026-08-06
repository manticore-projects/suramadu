package org.webswing.server.common.datastore.impl;

import org.webswing.Constants;
import org.webswing.server.common.datastore.WebswingDataStoreModuleConfig;
import org.webswing.server.common.model.meta.ConfigField;
import org.webswing.server.common.model.meta.ConfigFieldDefaultValueString;
import org.webswing.server.common.model.meta.ConfigFieldOrder;
import org.webswing.server.common.model.meta.ConfigFieldVariables;

@ConfigFieldOrder({"threadDumpsFolder", "recordingsFolder", "transferFolder"})
public interface FileSystemDataStoreModuleConfig extends WebswingDataStoreModuleConfig {

  @ConfigField(label = "Thread Dumps Folder",
      description = "Folder to be used to store application thread dumps. This folder must be accessible by application and server.")
  @ConfigFieldVariables
  @ConfigFieldDefaultValueString("${" + Constants.ROOT_DIR_PATH + "}/datastore/dumps")
  String getThreadDumpsFolder();

  @ConfigField(label = "Recordings Folder",
      description = "Folder to be used to store application recordings. This folder must be accessible by application and server.")
  @ConfigFieldVariables
  @ConfigFieldDefaultValueString("${" + Constants.ROOT_DIR_PATH + "}/datastore/recordings")
  String getRecordingsFolder();

  @ConfigField(label = "Transfer Folder",
      description = "Folder to be used to store application uploads and downloads. This folder must be accessible by application and server.")
  @ConfigFieldVariables
  @ConfigFieldDefaultValueString("${" + Constants.ROOT_DIR_PATH + "}/datastore/transfer")
  String getTransferFolder();

}
