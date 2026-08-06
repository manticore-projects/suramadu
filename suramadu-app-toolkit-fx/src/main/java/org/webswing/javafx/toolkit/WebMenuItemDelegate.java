package org.webswing.javafx.toolkit;

import com.sun.glass.ui.MenuItem;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.delegate.MenuItemDelegate;

/**
 * Created by vikto on 28-Feb-17.
 */
public class WebMenuItemDelegate implements MenuItemDelegate {
  @Override
  public boolean createMenuItem(String title, MenuItem.Callback callback, int shortcutKey,
      int shortcutModifiers, Pixels pixels, boolean enabled, boolean checked) {
    return false;
  }

  @Override
  public boolean setTitle(String title) {
    return false;
  }

  @Override
  public boolean setCallback(MenuItem.Callback callback) {
    return false;
  }

  @Override
  public boolean setShortcut(int shortcutKey, int shortcutModifiers) {
    return false;
  }

  @Override
  public boolean setPixels(Pixels pixels) {
    return false;
  }

  @Override
  public boolean setEnabled(boolean enabled) {
    return false;
  }

  @Override
  public boolean setChecked(boolean checked) {
    return false;
  }
}
