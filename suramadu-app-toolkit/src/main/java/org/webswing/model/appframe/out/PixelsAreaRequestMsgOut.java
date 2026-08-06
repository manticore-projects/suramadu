package org.webswing.model.appframe.out;

import org.webswing.model.MsgOut;
import org.webswing.model.SyncMsg;

import java.io.Serial;

public class PixelsAreaRequestMsgOut implements MsgOut, SyncMsg {
  @Serial
  private static final long serialVersionUID = -7414037919462952330L;

  private String correlationId;
  private int x;
  private int y;
  private int w;
  private int h;

  public String getCorrelationId() {
    return correlationId;
  }

  public void setCorrelationId(String correlationId) {
    this.correlationId = correlationId;
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

  public int getW() {
    return w;
  }

  public void setW(int w) {
    this.w = w;
  }

  public int getH() {
    return h;
  }

  public void setH(int h) {
    this.h = h;
  }
}
