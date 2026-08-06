package org.webswing.model.appframe.out;

import org.webswing.model.MsgOut;

import java.io.Serial;

public class PlaybackInfoMsgOut implements MsgOut {
  @Serial
  private static final long serialVersionUID = -2332725867134258277L;
  private int current;
  private int total;

  public int getCurrent() {
    return current;
  }

  public void setCurrent(int current) {
    this.current = current;
  }

  public int getTotal() {
    return total;
  }

  public void setTotal(int total) {
    this.total = total;
  }

}
