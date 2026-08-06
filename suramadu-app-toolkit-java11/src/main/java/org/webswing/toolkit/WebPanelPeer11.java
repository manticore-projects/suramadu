package org.webswing.toolkit;

import java.awt.Component;
import java.awt.Panel;
import java.awt.event.FocusEvent;

public class WebPanelPeer11 extends WebPanelPeer {

  public WebPanelPeer11(Panel panel) {
    super(panel);
  }

  @Override
  public boolean requestFocus(Component lightweightChild, boolean temporary,
      boolean focusedWindowChangeAllowed, long time, FocusEvent.Cause cause) {
    return WebToolkit11.requestFocus(target, lightweightChild, temporary,
        focusedWindowChangeAllowed, time, cause);
  }
}
