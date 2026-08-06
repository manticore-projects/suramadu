package org.webswing.common;

import java.awt.Cursor;

public enum WindowActionType {
  cursorChanged, close, minimize, maximize, dock, undock, move, resizeBottom(
      Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR)), resizeTop(
          Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR)), resizeRight(
              Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR)), resizeLeft(
                  Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR)), resizeUniBottomRight(
                      Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR)), resizeUniTopLeft(
                          Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR)), resizeUniTopRight(
                              Cursor.getPredefinedCursor(
                                  Cursor.SW_RESIZE_CURSOR)), resizeUniBottomLeft(
                                      Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));

  private final Cursor c;

  private WindowActionType() {
    this(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
  }

  private WindowActionType(Cursor c) {
    this.c = c;
  }

  public Cursor getCursor() {
    return c;
  }

  public boolean isButtonActionType() {
    return this == close || this == minimize || this == maximize || this == dock || this == undock;
  }

}
