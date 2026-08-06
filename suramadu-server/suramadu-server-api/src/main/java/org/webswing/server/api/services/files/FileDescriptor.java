package org.webswing.server.api.services.files;

import java.io.File;
import java.util.concurrent.Future;

class FileDescriptor {

  protected String instanceId;
  protected File file;
  protected Future<?> invalidateScheduleTask;
  protected String userId;
  protected boolean temporary;
  protected boolean waitForFile;
  protected String lastFileAttributes;
  protected Future<?> waitForFileTask;
  protected String overwriteDetails;

  public FileDescriptor(File file, String userId) {
    super();
    this.file = file;
    this.userId = userId;
  }
}
