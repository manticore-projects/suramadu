package org.webswing.model.app.out;

import org.webswing.model.MsgOut;

import java.io.Serial;

public class ExitMsgOut implements MsgOut {

  @Serial
  private static final long serialVersionUID = -8007742149401885272L;

  private int waitForExit;

  public int getWaitForExit() {
    return waitForExit;
  }

  public void setWaitForExit(int waitForExit) {
    this.waitForExit = waitForExit;
  }

}
