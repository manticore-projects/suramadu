package org.webswing.toolkit;

import org.webswing.toolkit.util.Util;

import javax.swing.JFrame;
import java.awt.Frame;
import java.awt.MenuBar;
import java.awt.Rectangle;
import java.awt.peer.FramePeer;

abstract public class WebFramePeer extends WebWindowPeer implements FramePeer {

  private int state;

  public WebFramePeer(Frame t) {
    super(t);
    state = t.getExtendedState();
  }

  public void setMenuBar(MenuBar paramMenuBar) {}

  public void setState(int paramInt) {
    state = paramInt;
    if (state == Frame.MAXIMIZED_BOTH) {
      Util.getWebToolkit().getPaintDispatcher().notifyWindowMaximized((JFrame) target);
    }
  }

  public int getState() {
    return state;
  }

  public void setMaximizedBounds(Rectangle paramRectangle) {}

  public void setBoundsPrivate(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {}

  public Rectangle getBoundsPrivate() {
    return null;
  }

  public void emulateActivation(boolean b) {

  }

  @Override
  public void show() {
    super.show();
    setState(state); // maximize if necessary
  }

}
