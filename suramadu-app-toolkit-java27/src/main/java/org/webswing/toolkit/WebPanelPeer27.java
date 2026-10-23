package org.webswing.toolkit;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Panel;
import java.awt.event.FocusEvent;

import sun.awt.SunToolkit;

public class WebPanelPeer27 extends WebPanelPeer {

  public WebPanelPeer27(Panel panel) {
    super(panel);
  }

  @Override
  public boolean requestFocus(Component lightweightChild, boolean temporary,
      boolean focusedWindowChangeAllowed, long time, FocusEvent.Cause cause) {
    return WebToolkit27.requestFocus(target, lightweightChild, temporary,
        focusedWindowChangeAllowed, time, cause);
  }

  /**
   * JDK 27: overrides the inherited WebComponentPeer.postEvent(), which still calls
   * SunToolkit.targetToAppContext()/postEvent(AppContext, AWTEvent). sun.awt.AppContext was removed
   * from the JDK, so the inherited body would throw NoSuchMethodError on first use; this override
   * keeps it from ever being resolved. Works because the JDK-27 peers live in the same runtime
   * package (same classloader) as WebComponentPeer.
   */
  @Override
  void postEvent(AWTEvent paramAWTEvent) {
    SunToolkit.postEvent(paramAWTEvent);
  }
}
