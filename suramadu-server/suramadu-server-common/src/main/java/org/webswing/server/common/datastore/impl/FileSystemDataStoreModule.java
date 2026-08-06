package org.webswing.server.common.datastore.impl;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.server.common.datastore.AbstractDataStoreModule;
import org.webswing.server.common.datastore.WebswingDataStoreType;
import org.webswing.server.common.util.VariableSubstitutor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.*;

public class FileSystemDataStoreModule
    extends AbstractDataStoreModule<FileSystemDataStoreModuleConfig> {

  private static final Logger log = LoggerFactory.getLogger(FileSystemDataStoreModule.class);
  private static final long TIMEOUT_RETRY_MILLIS = 1000;

  private final VariableSubstitutor subs = VariableSubstitutor.basic();

  private final ScheduledExecutorService retryService = Executors.newScheduledThreadPool(10);

  public FileSystemDataStoreModule(FileSystemDataStoreModuleConfig config) {
    super(config);
  }

  @Override
  public InputStream readData(String type, String id) throws IOException {
    String filePath = getFolderPath(getDataType(type));

    if (filePath == null) {
      log.warn("Could not find dataStore path for type [{}]!", type);
      return null;
    }

    filePath = filePath + File.separator + id;

    File file = new File(filePath);
    file = file.getAbsoluteFile();

    return new FileInputStream(file);
  }

  @Override
  public InputStream readData(String type, String id, long timeoutMillis) throws IOException {
    InputStream is = null;
    try {
      is = readData(type, id);
      if (is != null) {
        return is;
      }
      throw new IllegalStateException("Data [" + id + "] not ready!");
    } catch (Exception e) {
      // ignore
    }

    long timeoutEnd = System.currentTimeMillis() + timeoutMillis;

    do {
      long now = System.currentTimeMillis();
      if (timeoutEnd < now) {
        return is;
      }

      is = retryReadData(type, id, Math.min(TIMEOUT_RETRY_MILLIS, timeoutEnd - now));
    } while (is == null);

    return is;
  }

  private InputStream retryReadData(String type, String id, long timeout) {
    Future<InputStream> future = retryService.schedule(new Callable<InputStream>() {
      public InputStream call() throws Exception {
        return readData(type, id);
      }
    }, timeout, TimeUnit.MILLISECONDS);

    try {
      return future.get();
    } catch (Exception e) {
      // ignore
    }

    return null;
  }

  @Override
  public void storeData(String type, String id, InputStream is, boolean deleteIfExists)
      throws IOException {
    String filePath = getFolderPath(getDataType(type));

    if (filePath == null) {
      log.warn("Could not find dataStore path for type [{}]!", type);
      return;
    }

    File file = new File(filePath);
    if (!file.exists()) {
      file.mkdirs();
    }

    file = new File(filePath + File.separator + id);
    file = file.getAbsoluteFile();
    if (file.exists() && deleteIfExists) {
      file.delete();
    }

    file.createNewFile();

    try (FileOutputStream fos = new FileOutputStream(file)) {
      IOUtils.copy(is, fos);
    }
  }

  @Override
  public boolean dataExists(String type, String id) {
    String filePath = getFolderPath(getDataType(type));

    if (filePath == null) {
      log.warn("Could not find dataStore path for type [{}]!", type);
      return false;
    }

    File file = new File(filePath);
    return file.exists();
  }

  @Override
  public void deleteData(String type, String id) throws IOException {
    String filePath = getFolderPath(getDataType(type));

    if (filePath == null) {
      log.warn("Could not find dataStore path for type [{}]!", type);
      return;
    }

    File file = new File(filePath);
    if (file.exists()) {
      file.delete();
    }
  }

  private String getFolderPath(WebswingDataStoreType type) {
    switch (type) {
      case recording:
        return subs.replace(getConfig().getRecordingsFolder());
      case threadDump:
        return subs.replace(getConfig().getThreadDumpsFolder());
      case transfer:
        return subs.replace(getConfig().getTransferFolder());
      case unknown:
        return null;
    }
    return null;
  }

  private WebswingDataStoreType getDataType(String type) {
    try {
      WebswingDataStoreType dataType = WebswingDataStoreType.valueOf(type);
      return dataType;
    } catch (IllegalArgumentException e) {
      log.warn("Could not resolve data store data type [{}]!", type, e);
    }
    return WebswingDataStoreType.unknown;
  }

}
