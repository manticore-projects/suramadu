package org.webswing.toolkit;

import org.webswing.toolkit.util.Util;

import java.awt.Point;
import java.awt.Window;
import java.awt.peer.MouseInfoPeer;


public class WebMouseInfoPeer implements MouseInfoPeer {

  public int fillPointWithCoords(Point point) {
    if (point != null) {
      Point last = Util.getWebToolkit().getEventDispatcher().getLastMousePosition();
      point.x = last.x;
      point.y = last.y;
    }
    return 0;
  }

  public boolean isWindowUnderMouse(Window w) {
    Point last = Util.getWebToolkit().getEventDispatcher().getLastMousePosition();
    Window wx = Util.getWebToolkit().getWindowManager().getVisibleWindowOnPosition(last.x, last.y);
    if (w == wx) {
      return true;
    } else {
      return false;
    }
  }

}
