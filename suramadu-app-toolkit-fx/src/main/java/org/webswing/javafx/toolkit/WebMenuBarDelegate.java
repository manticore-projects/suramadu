package org.webswing.javafx.toolkit;

import com.sun.glass.ui.delegate.MenuBarDelegate;
import com.sun.glass.ui.delegate.MenuDelegate;

/**
 * Created by vikto on 28-Feb-17.
 */
public class WebMenuBarDelegate implements MenuBarDelegate {
  @Override
  public boolean createMenuBar() {
    return false;
  }

  @Override
  public boolean insert(MenuDelegate menu, int pos) {
    return false;
  }

  @Override
  public boolean remove(MenuDelegate menu, int pos) {
    return false;
  }

  @Override
  public long getNativeMenu() {
    return 0;
  }
}
