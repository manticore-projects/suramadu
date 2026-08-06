package org.webswing.toolkit.extra;

import java.io.File;
import java.io.Serial;


public class IsolatedRootFile extends File {

  public IsolatedRootFile(String pathname) {
    super(pathname);
  }

  @Serial
  private static final long serialVersionUID = -2583310076262643707L;


  @Override
  public String getParent() {
    return null;
  }

  @Override
  public File getParentFile() {
    return null;
  }
}
