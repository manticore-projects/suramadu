package org.webswing.model.appframe.out;

import org.webswing.model.MsgOut;

import java.io.Serial;

public class WindowMoveActionMsgOut implements MsgOut {

  @Serial
  private static final long serialVersionUID = -8935899924614304993L;
  private int sx;
  private int sy;
  private int dx;
  private int dy;
  private int width;
  private int height;

  public WindowMoveActionMsgOut() {}

  public WindowMoveActionMsgOut(int sx, int sy, int dx, int dy, int width, int height) {
    super();
    this.sx = sx;
    this.sy = sy;
    this.dx = dx;
    this.dy = dy;
    this.width = width;
    this.height = height;
  }

  public int getSx() {
    return sx;
  }

  public void setSx(int sx) {
    this.sx = sx;
  }

  public int getSy() {
    return sy;
  }

  public void setSy(int sy) {
    this.sy = sy;
  }

  public int getDx() {
    return dx;
  }

  public void setDx(int dx) {
    this.dx = dx;
  }

  public int getDy() {
    return dy;
  }

  public void setDy(int dy) {
    this.dy = dy;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

}
