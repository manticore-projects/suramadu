package org.webswing.toolkit;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.FocusEvent;

public class WebWindowPeer11 extends WebWindowPeer {

  public WebWindowPeer11(Window t) {
    super(t);
  }


  @Override
  public boolean requestFocus(Component lightweightChild, boolean temporary,
      boolean focusedWindowChangeAllowed, long time, FocusEvent.Cause cause) {
    return WebToolkit11.requestFocus(target, lightweightChild, temporary,
        focusedWindowChangeAllowed, time, cause);
  }
}
