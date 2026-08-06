package org.webswing.toolkit;

import org.webswing.toolkit.ge.WebGraphicsEnvironment;
import org.webswing.toolkit.util.Util;
import org.webswing.util.AppLogger;
import sun.awt.LightweightFrame;
import sun.awt.SunToolkit;
import sun.awt.datatransfer.DataTransferer;
import sun.awt.image.SurfaceManager;
import sun.java2d.SurfaceData;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.peer.FramePeer;
import java.awt.peer.KeyboardFocusManagerPeer;
import java.lang.reflect.Method;

public class WebToolkit11 extends WebToolkit {
  private KeyboardFocusManagerPeer kfmp;

  @Override
  WebFramePeer createWebFramePeer(Frame frame) throws HeadlessException {
    return new WebFramePeer11(frame);
  }

  @Override
  WebDialogPeer createWebDialogPeer(Dialog paramDialog) {
    return new WebDialogPeer11(paramDialog);
  }

  @Override
  WebWindowPeer createWebWindowPeer(Window paramWindow) {
    return new WebWindowPeer11(paramWindow);
  }

  @Override
  WebPanelPeer createWebPanelPeer(Panel panel) {
    return new WebPanelPeer11(panel);
  }

  @Override
  WebFileDialogPeer createWebFileDialogPeer(FileDialog paramFileDialog) {
    return new WebFileDialogPeer11(paramFileDialog);
  }

  @Override
  public void displayChanged() {
    EventQueue.invokeLater(new Runnable() {

      public void run() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        WebGraphicsEnvironment wge = (WebGraphicsEnvironment) ge;
        wge.displayChanged();
      }
    });
  }

  @Override
  public KeyboardFocusManagerPeer getKeyboardFocusManagerPeer() throws HeadlessException {
    if (kfmp == null) {
      kfmp = new WebKeyboardFocusManagerPeer();
    }
    return kfmp;
  }

  @Override
  protected boolean syncNativeQueue(long paramLong) {
    return false;
  }

  @Override
  public boolean webConpoenentPeerUpdateGraphicsData() {
    ((WebGraphicsEnvironment) GraphicsEnvironment.getLocalGraphicsEnvironment()).displayChanged();
    return true;
  }

  @Override
  public SurfaceData webComponentPeerReplaceSurfaceData(SurfaceManager mgr) {
    return mgr.getPrimarySurfaceData();
  }

  @Override
  public int shouldNativelyFocusHeavyweight(Component heavyweight, Component descendant,
      boolean temporary, boolean focusedWindowChangeAllowed, long time, FocusEventCause cause) {
    try {
      Method m2 = KeyboardFocusManager.class.getDeclaredMethod("shouldNativelyFocusHeavyweight",
          Component.class, Component.class, Boolean.TYPE, Boolean.TYPE, Long.TYPE,
          FocusEvent.Cause.class);
      m2.setAccessible(true);
      Integer result2 = (Integer) m2.invoke(null, heavyweight, descendant, temporary,
          focusedWindowChangeAllowed, time, FocusEvent.Cause.valueOf(cause.name()));
      return result2;
    } catch (Exception e) {
      AppLogger.debug(
          "Failed to invoke processSynchronousLightweightTransfer on KeyboardFocusManager. Check your java version.",
          e);
      return 0;
    }
  }

  @Override
  public boolean deliverFocus(Component heavyweight, Component descendant, boolean temporary,
      boolean focusedWindowChangeAllowed, long time, FocusEventCause cause) {
    if (heavyweight == null) {
      heavyweight = descendant;
    }

    Component c = Util.getWebToolkit().getWindowManager().getActiveWindow().getFocusOwner();
    FocusEvent focusEvent;
    if ((c != null) && !c.isDisplayable()) {
      c = null;
    }
    if (c != null) {
      focusEvent = new FocusEvent(c, FocusEvent.FOCUS_LOST, false, heavyweight,
          FocusEvent.Cause.valueOf(cause.name()));

      SunToolkit.postEvent(SunToolkit.targetToAppContext(c), focusEvent);
    }

    focusEvent = new FocusEvent(heavyweight, FocusEvent.FOCUS_GAINED, false, c,
        FocusEvent.Cause.valueOf(cause.name()));

    SunToolkit.postEvent(SunToolkit.targetToAppContext(heavyweight), focusEvent);
    return true;
  }

  @Override
  public DataTransferer getDataTransferer() {
    return WebDataTransfer.getInstanceImpl();
  }

  @Override
  public FramePeer createLightweightFrame(LightweightFrame arg0) throws HeadlessException {
    return null;
  }

  @Override
  public boolean isTaskbarSupported() {
    return true;
  }

  @Override
  public Dimension getScreenSize() {
    return new Dimension(screenWidth, screenHeight);
  }

  public static boolean requestFocus(Object target, Component paramComponent, boolean temporary,
      boolean focusedWindowChangeAllowed, long time, FocusEvent.Cause paramCause) {
    if (target instanceof Window window) {
      return Util.getWebToolkit().getWindowManager().activateWindow(window, paramComponent, 0, 0,
          temporary, focusedWindowChangeAllowed, FocusEventCause.getValue(paramCause));
    } else if (target instanceof Panel panel) {
      return Util.getWebToolkit().getWindowManager().deliverFocus(panel, paramComponent, temporary,
          FocusEventCause.getValue(paramCause));
    } else {
      return false;
    }
  }
}
