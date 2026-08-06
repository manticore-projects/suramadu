package com.sun.glass.ui.web;

import com.sun.glass.ui.*;
import com.sun.glass.ui.delegate.ClipboardDelegate;
import com.sun.glass.ui.delegate.MenuBarDelegate;
import com.sun.glass.ui.delegate.MenuDelegate;
import com.sun.glass.ui.delegate.MenuItemDelegate;
import org.webswing.javafx.toolkit.*;

public class WebPlatformFactory extends PlatformFactory {

  @Override
  public Application createApplication() {
    return WebsinwgFxToolkitFactory.getFactory().createApplication();
  }

  @Override
  public MenuBarDelegate createMenuBarDelegate(MenuBar menubar) {
    return new WebMenuBarDelegate();
  }

  @Override
  public MenuDelegate createMenuDelegate(Menu menu) {
    return new WebMenuDelegate();
  }

  @Override
  public MenuItemDelegate createMenuItemDelegate(MenuItem menuItem) {
    return new WebMenuItemDelegate();
  }

  @Override
  public ClipboardDelegate createClipboardDelegate() {
    return new WebClipboardDelegate();
  }
}
