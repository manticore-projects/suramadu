package org.webswing.toolkit;

import org.webswing.Constants;
import org.webswing.toolkit.api.component.Dockable;
import org.webswing.toolkit.extra.WindowManager;
import org.webswing.toolkit.util.Util;
import org.webswing.util.AppLogger;
import sun.java2d.InvalidPipeException;

import java.awt.*;
import java.awt.peer.WindowPeer;
import java.util.logging.Logger;

abstract public class WebWindowPeer extends WebContainerPeer implements WindowPeer {

  private static final int VALIDATE_BOUNDS_THRESHOLD = 40;

  private Boolean undecoratedOverride;
  private boolean undocked;

  //////////////////////////////////////////////////////////////////////////////////////////////
  ///////////////////// WebWindowPeer
  ////////////////////////////////////////////////////////////////////////////////////////////// Implementation//////////////////////////////////////////////////
  public WebWindowPeer(Window t) {
    super(t);
    Font font = t.getFont();
    if (font == null) {
      t.setFont(WebToolkit.defaultFont);
    }

    if (t instanceof Dockable dockable && dockable.isAutoUndock()) {
      setUndocked(true);
    }
  }

  public void toFront() {
    WindowManager.getInstance().bringToFront((Window) target);
  }

  public void toBack() {
    WindowManager.getInstance().bringToBack((Window) target);
  }

  public void setAlwaysOnTop(boolean paramBoolean) {
    WindowManager.getInstance().bringToFront((Window) target);
  }

  public void updateFocusableWindowState() {}

  public boolean requestWindowFocus() {
    Util.getWebToolkit().getWindowManager().activateWindow((Window) target);
    return true;
  }

  public void setModalBlocked(Dialog paramDialog, boolean paramBoolean) {}

  public void updateMinimumSize() {}

  public void updateIconImages() {}

  public void setOpacity(float o) {
    this.opacity = o;
  }

  public void setOpaque(boolean paramBoolean) {}

  public void repositionSecurityWarning() {}

  public void updateWindow() {}

  public void show() {
    Util.getWebToolkit().getWindowManager().activateWindow((Window) target);
  }

  public void hide() {
    Util.getWebToolkit().getWindowManager().removeWindow((Window) target);
    notifyWindowClosed();
  }

  public void setTitle(String title) {
    updateWindowDecorationImage();
  }

  public void setResizable(boolean resizeable) {}

  protected Point validate(int x, int y, int w, int h) {
    if (Boolean.getBoolean(Constants.SWING_SCREEN_VALIDATION_DISABLED) || Util.isCompositingWM()) {
      return new Point(x, y);
    }

    Point result = new Point(x, y);
    if (w == 0 && h == 0) {
      return result;
    }
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    Insets insets = this.getInsets();
    if (screen.height < insets.top || screen.width < VALIDATE_BOUNDS_THRESHOLD) {
      return result;
    }

    if (y < 0) {
      result.y = 0;
    }
    if (y > (screen.height - insets.top)) {
      result.y = screen.height - insets.top;
    }
    if (x < ((w - VALIDATE_BOUNDS_THRESHOLD) * -1)) {
      result.x = (w - VALIDATE_BOUNDS_THRESHOLD) * -1;
    }
    if (x > (screen.width - VALIDATE_BOUNDS_THRESHOLD)) {
      result.x = screen.width - VALIDATE_BOUNDS_THRESHOLD;
    }
    if ((target instanceof Frame frame) && frame.getExtendedState() == Frame.MAXIMIZED_BOTH) {
      result.x = 0;
      result.y = 0;
    }
    if (result.x != x || result.y != y) {
      ((Component) target).setLocation(result);
    }
    return result;
  }

  public void updateAlwaysOnTopState() {}

  public void setUndecoratedOverride(boolean undecorated) {
    this.undecoratedOverride = undecorated;

    if (target instanceof Window win) {
      try {
        replaceSurfaceData(win.getX(), win.getY(), win.getWidth(), win.getHeight());
      } catch (InvalidPipeException e) {
        AppLogger.error("WebWindowPeer:setUndecoratedOverride", e);
      }
    }
  }

  public Boolean getUndecoratedOverride() {
    return undecoratedOverride;
  }

  public boolean isUndocked() {
    return undocked;
  }

  public void setUndocked(boolean undocked) {
    if ("NONE".equals(Util.getDockMode())) {
      return;
    }

    this.undocked = undocked;
    updateWindowDecorationImage();
    Util.getWebToolkit().getPaintDispatcher().notifyWindowDockStateChanged();
  }

}
