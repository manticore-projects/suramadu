package org.webswing.toolkit;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.FocusEvent;

public class WebFramePeer11 extends WebFramePeer {

  public WebFramePeer11(Frame frame) {
    super(frame);
  }

  @Override
  public boolean requestFocus(Component lightweightChild, boolean temporary,
      boolean focusedWindowChangeAllowed, long time, FocusEvent.Cause cause) {
    return WebToolkit11.requestFocus(target, lightweightChild, temporary,
        focusedWindowChangeAllowed, time, cause);
  }
}
