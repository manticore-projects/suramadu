package org.webswing.ext.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public interface DataStoreService {

  InputStream readData(String type, String id);

  void storeData(String type, String id, InputStream is, boolean deleteIfExists) throws IOException;

  boolean registerFile(File file, String id, long validForTime, TimeUnit timeUnit,
      String validForUser, String instanceId);

  boolean registerFileWhenReady(File file, String id, long validForTime, TimeUnit timeUnit,
      String validForUser, String instanceId, String overwriteDetails);

  boolean registerData(byte[] data, String id, long validForTime, TimeUnit timeUnit,
      String validForUser, String instanceId, boolean temp);

  boolean dataExists(String type, String id);

  void writeStreamToFile(InputStream is, File file) throws IOException;

  // void moveFile(File src, File dest) throws IOException;

  public class FileDescriptor {

    public String id;
    public String instanceId;
    public File file;
    public byte[] data;
    public Future<?> invalidateScheduleTask;
    public String userId;
    public boolean temporary;
    public boolean waitForFile;
    public String lastFileAttributes;
    public Future<?> waitForFileTask;
    public String overwriteDetails;

    public FileDescriptor(File file, String id, String userId) {
      super();
      this.id = id;
      this.file = file;
      this.userId = userId;
    }

    public FileDescriptor(byte[] data, String id, String userId) {
      super();
      this.id = id;
      this.data = data;
      this.userId = userId;
    }

  }

}
