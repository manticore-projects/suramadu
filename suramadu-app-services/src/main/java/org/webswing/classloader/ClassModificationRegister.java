package org.webswing.classloader;

import org.webswing.Constants;
import org.webswing.util.AppLogger;

import java.io.*;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class ClassModificationRegister {
  private static FileLock lock;
  private final Set<String> unmodifiedClassSet = new HashSet<>();
  private PrintWriter writer;

  public ClassModificationRegister() {
    // read set of unmodified classes from file
    readUnmodified();

    try {
      if (acquireLock()) {
        writer = new PrintWriter(
            new BufferedWriter(new FileWriter(getClassListFile(), StandardCharsets.UTF_8, true)));
        AppLogger.info("ClassModificationRegister: lock acquired.");
      }
    } catch (IOException ex) {
      AppLogger.error("ClassModificationRegister: Failed to create unmodified class register map. "
          + ex.getMessage());
    }
  }

  private boolean acquireLock() throws IOException {
    if (lock == null) {
      RandomAccessFile lockFile = new RandomAccessFile(getClassListLockFile(), "rw");
      FileChannel channel = lockFile.getChannel();
      lock = channel.tryLock();
    }
    return lock != null;
  }

  private void readUnmodified() {
    Scanner sc = null;
    try {
      sc = new Scanner(getClassListFile(), StandardCharsets.UTF_8);
      while (sc.hasNextLine()) {
        unmodifiedClassSet.add(sc.nextLine());
      }
    } catch (IOException ignore) {
      // do nothing. file not created yet
    } finally {
      if (sc != null) {
        sc.close();
      }
    }
  }

  File getClassListFile() {
    String blacklistFileName =
        System.getProperty(Constants.SWING_START_SYS_PROP_CLASS_MODIFICATION_BLACKLIST);
    if (blacklistFileName != null) {
      return new File(blacklistFileName);
    } else {
      String tempDir = System.getProperty(Constants.TEMP_DIR_PATH);
      String applicationName = URLEncoder.encode(
          System.getProperty(Constants.SWING_START_SYS_PROP_APP_ID), StandardCharsets.UTF_8);
      return new File(URI.create(tempDir + applicationName + ".unmodified_classes"));
    }
  }

  File getClassListLockFile() {
    return new File(getClassListFile().getAbsolutePath() + ".lock");
  }

  public boolean canSkipModification(String className) {
    return unmodifiedClassSet.contains(className);
  }

  public void notifyClassLoaded(String className) {
    unmodifiedClassSet.remove(className);
  }

  public void setModificationState(String className, boolean modified) {
    if (!modified && writer != null) {
      writer.println(className);
      writer.flush();
    }
  }

}
