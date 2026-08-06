package org.webswing.toolkit;

import org.webswing.toolkit.util.Util;
import sun.awt.PeerEvent;
import sun.awt.SunToolkit;
import sun.awt.dnd.SunDropTargetContextPeer;
import sun.awt.dnd.SunDropTargetEvent;

import java.awt.Component;
import java.awt.Window;
import java.io.IOException;

@SuppressWarnings("restriction")
public class WebDropTargetContextPeer extends SunDropTargetContextPeer {

  private WebDragSourceContextPeer dragSource;

  private int dropx;
  private int dropy;
  private Window dropWindow;

  public static WebDropTargetContextPeer getWebDropTargetContextPeer() {
    return new WebDropTargetContextPeer();
  }

  @Override
  protected Object getNativeData(long paramLong) throws IOException {
    return null;
  }

  @Override
  protected void doDropDone(boolean success, int dropAction, boolean local) {
    if (dragSource != null) {
      dragSource.dragFinished(success, dropAction, dropx, dropy);
    }
    if (success) {
      Util.getWebToolkit().getWindowManager().activateWindow(dropWindow, dropx, dropy);
    }
  }

  public void handleDropMessage(Component window, int x, int y, int dropAction, int actions,
      long[] formats, long nativeCtxt) {
    dropx = x;
    dropy = y;
    dropWindow = (Window) window;
    postDropTargetEvent(window, x, y, dropAction, actions, formats, nativeCtxt, 502, false);
  }

  public int handleEnterMessage(Component window, int x, int y, int dropAction, int actions,
      long[] formats, long nativeCtxt) {
    return postDropTargetEvent(window, x, y, dropAction, actions, formats, nativeCtxt, 504, true);
  }

  public void handleExitMessage(Component window, long nativeCtxt) {
    postDropTargetEvent(window, 0, 0, 0, 0, null, nativeCtxt, 505, true);
  }

  public int handleMotionMessage(Component window, int x, int y, int dropAction, int actions,
      long[] formats, long nativeCtxt) {
    return postDropTargetEvent(window, x, y, dropAction, actions, formats, nativeCtxt, 506, true);
  }

  @Override
  protected void eventPosted(final SunDropTargetEvent paramSunDropTargetEvent) {
    if (paramSunDropTargetEvent.getID() != 502) {
      Runnable local1 = new Runnable() {

        public void run() {
          paramSunDropTargetEvent.getDispatcher().unregisterAllEvents();
        }

      };
      PeerEvent localPeerEvent = new PeerEvent(paramSunDropTargetEvent.getSource(), local1, 0L);
      SunToolkit.executeOnEventHandlerThread(localPeerEvent);
    }
  }


  public void setDragSource(WebDragSourceContextPeer dragSource) {
    this.dragSource = dragSource;
  }

}
