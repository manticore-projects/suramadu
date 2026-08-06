package org.webswing.server.common.datastore;

import java.io.IOException;
import java.io.InputStream;

public interface WebswingDataStoreModule {

  // FIXME should contain init and destroy ? when config is reloaded

  InputStream readData(String type, String id) throws IOException;

  InputStream readData(String type, String id, long timeoutMillis) throws IOException;

  void storeData(String type, String id, InputStream is, boolean deleteIfExists) throws IOException;

  boolean dataExists(String type, String id);

  void deleteData(String type, String id) throws IOException;

}
