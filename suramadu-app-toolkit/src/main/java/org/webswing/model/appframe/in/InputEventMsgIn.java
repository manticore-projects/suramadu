package org.webswing.model.appframe.in;

import org.webswing.model.MsgIn;

import java.io.Serial;

public class InputEventMsgIn implements MsgIn {

  @Serial
  private static final long serialVersionUID = -6578454357790442182L;
  private KeyboardEventMsgIn key;
  private MouseEventMsgIn mouse;
  private WindowFocusMsgIn focus;

  public KeyboardEventMsgIn getKey() {
    return key;
  }

  public void setKey(KeyboardEventMsgIn key) {
    this.key = key;
  }

  public MouseEventMsgIn getMouse() {
    return mouse;
  }

  public void setMouse(MouseEventMsgIn mouse) {
    this.mouse = mouse;
  }

  public WindowFocusMsgIn getFocus() {
    return focus;
  }

  public void setFocus(WindowFocusMsgIn focus) {
    this.focus = focus;
  }
}
