package org.webswing.toolkit.extra;

import java.io.File;
import java.io.IOException;

public class WebswingShellFolder extends DefaultShellFolder {
  public WebswingShellFolder(DefaultShellFolder parent, File f) throws IOException {
    super(parent, f);
  }

  public WebswingShellFolder(File f) throws IOException {
    super(null, f);
  }

  @Override
  public boolean renameTo(File file) {
    if (file == null || !isSameDirectory(this, file)) {
      return false;
    }
    return super.renameTo(file);
  }

  private boolean isSameDirectory(File file1, File file2) {
    if (file1 == null || file2 == null) {
      return false;
    }
    return file1.getParentFile().getAbsolutePath().equals(file2.getParentFile().getAbsolutePath());
  }
}
