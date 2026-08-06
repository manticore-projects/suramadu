package org.webswing.javafx.toolkit;

import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.delegate.MenuDelegate;
import com.sun.glass.ui.delegate.MenuItemDelegate;

/**
 * Created by vikto on 28-Feb-17.
 */
public class WebMenuDelegate implements MenuDelegate {
  @Override
  public boolean createMenu(String title, boolean enabled) {
    return false;
  }

  @Override
  public boolean setTitle(String title) {
    return false;
  }

  @Override
  public boolean setEnabled(boolean enabled) {
    return false;
  }

  @Override
  public boolean setPixels(Pixels pixels) {
    return false;
  }

  @Override
  public boolean insert(MenuDelegate menu, int pos) {
    return false;
  }

  @Override
  public boolean insert(MenuItemDelegate item, int pos) {
    return false;
  }

  @Override
  public boolean remove(MenuDelegate menu, int pos) {
    return false;
  }

  @Override
  public boolean remove(MenuItemDelegate item, int pos) {
    return false;
  }
}
