package org.webswing.model.appframe.out;

import org.webswing.model.MsgOut;

import java.io.Serial;

public class CursorChangeEventMsgOut implements MsgOut {

  @Serial
  private static final long serialVersionUID = -5374921187295359488L;

  public static final String DEFAULT_CURSOR = "default";
  public static final String HAND_CURSOR = "pointer";
  public static final String CROSSHAIR_CURSOR = "crosshair";
  public static final String MOVE_CURSOR = "move";
  public static final String TEXT_CURSOR = "text";
  public static final String WAIT_CURSOR = "progress";
  public static final String EW_RESIZE_CURSOR = "e-resize";
  public static final String NS_RESIZE_CURSOR = "n-resize";
  public static final String SLASH_RESIZE_CURSOR = "ne-resize";
  public static final String BACKSLASH_RESIZE_CURSOR = "se-resize";
  public static final String NOT_ALLOWED_CURSOR = "not-allowed";

  private String cursor;
  private byte[] b64img;
  private String curFile;
  private int x;
  private int y;
  private String winId;

  public CursorChangeEventMsgOut() {}

  public CursorChangeEventMsgOut(String cursor) {
    super();
    this.cursor = cursor;
  }

  public String getCursor() {
    return cursor;
  }

  public void setCursor(String cursor) {
    this.cursor = cursor;
  }

  public byte[] getB64img() {
    return b64img;
  }

  public void setB64img(byte[] b64img) {
    this.b64img = b64img;
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public String getCurFile() {
    return curFile;
  }

  public void setCurFile(String curFile) {
    this.curFile = curFile;
  }

  public String getWinId() {
    return winId;
  }

  public void setWinId(String winId) {
    this.winId = winId;
  }
}
