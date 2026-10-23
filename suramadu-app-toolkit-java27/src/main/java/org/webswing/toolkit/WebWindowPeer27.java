package org.webswing.toolkit;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Window;
import java.awt.event.FocusEvent;

import sun.awt.SunToolkit;

public class WebWindowPeer27 extends WebWindowPeer {

  public WebWindowPeer27(Window t) {
    super(t);
  }

  @Override
  public boolean requestFocus(Component lightweightChild, boolean temporary,
      boolean focusedWindowChangeAllowed, long time, FocusEvent.Cause cause) {
    return WebToolkit27.requestFocus(target, lightweightChild, temporary,
        focusedWindowChangeAllowed, time, cause);
  }

  /**
   * New abstract method on {@code java.awt.peer.WindowPeer} since the base toolkit was written
   * (added for per-window GC selection on multi-display setups). Webswing has a single virtual
   * screen/device, so the requested configuration is always the appropriate one. The contract
   * requires a non-null result for a non-null argument; the fallback covers callers passing null.
   */
  @Override
  public GraphicsConfiguration getAppropriateGraphicsConfiguration(GraphicsConfiguration gc) {
    return gc != null ? gc
        : GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
            .getDefaultConfiguration();
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
