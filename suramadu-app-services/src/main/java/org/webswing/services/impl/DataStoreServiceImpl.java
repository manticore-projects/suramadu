package org.webswing.services.impl;

import org.apache.commons.io.FileUtils;
import org.webswing.ext.services.DataStoreService;
import org.webswing.server.common.datastore.DataStoreModuleWrapper;
import org.webswing.server.common.datastore.WebswingDataStoreConfig;
import org.webswing.server.common.datastore.WebswingDataStoreType;
import org.webswing.server.common.util.ConfigUtil;
import org.webswing.server.common.util.ServerUtil;
import org.webswing.server.common.util.WebswingObjectMapper;
import org.webswing.toolkit.util.Util;
import org.webswing.util.AppLogger;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.*;

public class DataStoreServiceImpl implements DataStoreService {

  private static DataStoreServiceImpl impl;

  private DataStoreModuleWrapper dataStore;

  private final Map<String, FileDescriptor> fileMap =
      new ConcurrentHashMap<String, FileDescriptor>();
  private final ScheduledExecutorService validatorService =
      Executors.newSingleThreadScheduledExecutor();

  public static DataStoreServiceImpl getInstance() {
    if (impl == null) {
      impl = new DataStoreServiceImpl();
    }
    return impl;
  }

  public DataStoreServiceImpl() {
    init();
  }

  @SuppressWarnings("unchecked")
  private void init() {
    try {
      String base64Config = Util.getDataStoreConfigString();
      Map<String, Object> config = (Map<String, Object>) WebswingObjectMapper.get()
          .readValue(Base64.getDecoder().decode(base64Config), Map.class);
      WebswingDataStoreConfig dataStoreConfig = (WebswingDataStoreConfig) ConfigUtil
          .instantiateConfig(config, WebswingDataStoreConfig.class);
      dataStore = new DataStoreModuleWrapper(dataStoreConfig);
    } catch (Exception e) {
      AppLogger.error("Could not deserialize dataStore config!", e);
    }
  }

  // @Override
  // public void moveFile(File src, File dest) throws IOException {
  // FileUtils.moveFile(src, dest);
  // }

  @Override
  public void writeStreamToFile(InputStream is, File file) throws IOException {
    FileUtils.copyInputStreamToFile(is, file);
  }

  @Override
  public InputStream readData(String type, String id) {
    return dataStore.readData(type, id);
  }

  @Override
  public boolean dataExists(String type, String id) {
    return dataStore.dataExists(type, id);
  }

  @Override
  public void storeData(String type, String id, InputStream is, boolean deleteIfExists)
      throws IOException {
    dataStore.storeData(type, id, is, deleteIfExists);
  }

  @Override
  public boolean registerData(byte[] data, String id, long validForTime, TimeUnit timeUnit,
      String validForUser, String instanceId, boolean temp) {
    FileDescriptor fd = new FileDescriptor(data, id, validForUser);
    return registerFile(fd, validForTime, timeUnit, instanceId, temp, false, null);
  }

  @Override
  public boolean registerFile(File file, String id, long validForTime, TimeUnit timeUnit,
      String validForUser, String instanceId) {
    FileDescriptor fd = new FileDescriptor(file, id, validForUser);
    return registerFile(fd, validForTime, timeUnit, instanceId, false, false, null);
  }

  @Override
  public boolean registerFileWhenReady(File file, String id, long validForTime, TimeUnit timeUnit,
      String validForUser, String instanceId, String overwriteDetails) {
    FileDescriptor fd = new FileDescriptor(file, id, validForUser);
    return registerFile(fd, validForTime, timeUnit, instanceId, false, true, overwriteDetails);
  }

  private boolean registerFile(FileDescriptor fd, long validForTime, TimeUnit timeUnit,
      String instanceId, boolean temp, boolean waitForFile, String overwriteDetails) {
    fd.waitForFile = true;
    fd.overwriteDetails = overwriteDetails;
    fd.instanceId = instanceId;
    fd.temporary = temp;

    // if file download was triggered by Desktop.open() or similar method and there is already a
    // autoDownload thread waiting for the same file, avoid double downloading same file by
    // canceling the waiting thread
    if (notifyWaitingForSameFile(fd)) {
      return false;
    }

    synchronized (fileMap) {
      fileMap.put(fd.id, fd);
    }

    if (validForTime > 0) {
      createInvalidationTask(fd, validForTime, timeUnit);
    }

    if (waitForFile) {
      // store when ready
      createWaitTask(fd);
    } else {
      // store immediately
      if (fd.file != null) {
        writeFileToDataStore(fd.id, fd.file);
      } else if (fd.data != null) {
        writeDataToDataStore(fd.id, fd.data); // fd.data = null
      }
    }

    return true;
  }

  private void createWaitTask(FileDescriptor fd) {
    fd.waitForFileTask = validatorService.scheduleAtFixedRate(new Runnable() {
      @Override
      public void run() {
        synchronized (fd) {
          if (fd.file != null && fd.file.exists()) {
            String details = fd.file.length() + "|" + fd.file.lastModified();
            if (!details.equals(fd.overwriteDetails)) {
              if (details.equals(fd.lastFileAttributes)) {
                if (!ServerUtil.isFileLocked(fd.file)) {
                  fd.waitForFile = false;
                  writeFileToDataStore(fd.id, fd.file);
                  fd.waitForFileTask.cancel(false);
                }
              } else {
                fd.lastFileAttributes = details;
              }
            }
          }
        }
      }
    }, 1, 1, TimeUnit.SECONDS);
  }

  private void createInvalidationTask(FileDescriptor fd, long validForTime, TimeUnit timeUnit) {
    // FIXME validator is running only as long as instance is running
    Future<?> invalidateTask = validatorService.schedule(new Runnable() {
      @Override
      public void run() {
        synchronized (fileMap) {
          fileMap.remove(fd.id);
        }
        if (fd.temporary) {
          try {
            dataStore.deleteData(WebswingDataStoreType.transfer.name(), fd.id);
          } catch (IOException e) {
            AppLogger.error("Failed to delete file [" + fd.id + "] from data store!", e);
          }
          if (fd.file != null) {
            fd.file.delete();
          }
        }
      }
    }, validForTime, timeUnit);
    fd.invalidateScheduleTask = invalidateTask;
  }

  private boolean notifyWaitingForSameFile(FileDescriptor newFd) {
    synchronized (fileMap) {
      for (String id : fileMap.keySet()) {
        FileDescriptor fd = fileMap.get(id);
        if (fd.file != null && newFd.file != null && fd.instanceId.equals(newFd.instanceId)
            && fd.userId.equals(newFd.userId) && fd.file.equals(newFd.file) && fd.waitForFile) {
          synchronized (fd) {
            // file is ready
            fd.waitForFile = false;
            writeFileToDataStore(id, fd.file);
            if (fd.waitForFileTask != null) {
              fd.waitForFileTask.cancel(false);
            }
          }
          return true;
        }
      }
    }
    return false;
  }

  private void writeFileToDataStore(String id, File file) {
    try (InputStream is = new FileInputStream(file)) {
      dataStore.storeData(WebswingDataStoreType.transfer.name(), id, is, true);
    } catch (Exception e) {
      AppLogger.error("Error while storing file [" + id + "] to data store!", e);
    }
  }

  private void writeDataToDataStore(String id, byte[] data) {
    try (InputStream is = new ByteArrayInputStream(data)) {
      dataStore.storeData(WebswingDataStoreType.transfer.name(), id, is, true);
    } catch (Exception e) {
      AppLogger.error("Error while storing file data [" + id + "] to data store!", e);
    }
  }

}
