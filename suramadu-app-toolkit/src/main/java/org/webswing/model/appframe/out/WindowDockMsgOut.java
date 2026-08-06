package org.webswing.model.appframe.out;

import org.webswing.model.MsgOut;

import java.io.Serial;

public class WindowDockMsgOut implements MsgOut {

  @Serial
  private static final long serialVersionUID = -5473405345399477117L;

  private String windowId;

  public WindowDockMsgOut() {}

  public String getWindowId() {
    return windowId;
  }

  public void setWindowId(String windowId) {
    this.windowId = windowId;
  }

}
