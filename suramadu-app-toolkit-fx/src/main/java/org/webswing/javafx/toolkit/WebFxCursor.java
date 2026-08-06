package org.webswing.javafx.toolkit;

import com.sun.glass.ui.Cursor;
import com.sun.glass.ui.Pixels;
import org.webswing.javafx.toolkit.util.WebFxUtil;
import org.webswing.toolkit.WebCursor;

import java.awt.Point;

/**
 * Created by vikto on 06-Mar-17.
 */
public class WebFxCursor extends Cursor {
  java.awt.Cursor c;

  protected WebFxCursor(int x, int y, Pixels pixels) {
    super(x, y, pixels);
  }

  public WebFxCursor(int type) {
    super(type);
    c = getSwingCursor(type);
  }

  private static java.awt.Cursor getSwingCursor(int type) {
    switch (type) {
      case Cursor.CURSOR_DEFAULT:
        return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR);
      case Cursor.CURSOR_CROSSHAIR:
        return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.CROSSHAIR_CURSOR);
      case Cursor.CURSOR_TEXT:
        return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.TEXT_CURSOR);
      case Cursor.CURSOR_WAIT:
        return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR);
      case Cursor.CURSOR_RESIZE_SOUTHWEST:
        return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.SW_RESIZE_CURSOR);
      case Cursor.CURSOR_RESIZE_SOUTHEAST:
        return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.SE_RESIZE_CURSOR);
      case Cursor.CURSOR_RESIZE_NORTHWEST:
        return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.NW_RESIZE_CURSOR);
      case Cursor.CURSOR_RESIZE_NORTHEAST:
        return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.NE_RESIZE_CURSOR);
      case Cursor.CURSOR_RESIZE_UP:
      case Cursor.CURSOR_RESIZE_UPDOWN:
        return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.N_RESIZE_CURSOR);
      case Cursor.CURSOR_RESIZE_DOWN:
        return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.S_RESIZE_CURSOR);
      case Cursor.CURSOR_RESIZE_LEFT:
      case Cursor.CURSOR_RESIZE_LEFTRIGHT:
        return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.W_RESIZE_CURSOR);
      case Cursor.CURSOR_RESIZE_RIGHT:
        return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.E_RESIZE_CURSOR);
      case Cursor.CURSOR_OPEN_HAND:
      case Cursor.CURSOR_CLOSED_HAND:
      case Cursor.CURSOR_POINTING_HAND:
        return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR);
      case Cursor.CURSOR_MOVE:
        return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.MOVE_CURSOR);
      // Not implemented, use default cursor instead
      case Cursor.CURSOR_DISAPPEAR:
        return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR);
      case Cursor.CURSOR_NONE:
        return null;
    }

    return java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR);
  }

  @Override
  protected long _createCursor(int x, int y, Pixels pixels) {
    c = new WebCursor(WebFxUtil.pixelsToImage(null, pixels), new Point(x, y),
        "WebFXCursor#" + pixels.hashCode());
    return System.identityHashCode(pixels.hashCode());
  }


}
