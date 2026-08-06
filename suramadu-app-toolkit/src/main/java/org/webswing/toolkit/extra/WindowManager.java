package org.webswing.toolkit.extra;

import org.webswing.common.WindowActionType;
import org.webswing.dispatch.WebPaintDispatcher;
import org.webswing.model.appframe.out.AccessibilityMsgOut;
import org.webswing.model.appframe.out.CursorChangeEventMsgOut;
import org.webswing.toolkit.FocusEventCause;
import org.webswing.toolkit.util.Services;
import org.webswing.toolkit.util.Util;

import javax.swing.SwingUtilities;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.util.Date;
import java.util.List;
import java.util.Map;

@SuppressWarnings("restriction")
public class WindowManager {

  private static WindowManager singleton;
  protected WindowHierarchyTree zorder = new WindowHierarchyTree();
  protected Window activeWindow;
  private WindowEventHandler eventhandler = new WindowEventHandler();
  private String currentCursor = CursorChangeEventMsgOut.DEFAULT_CURSOR;

  protected WindowManager() {}

  public static WindowManager getInstance() {
    if (singleton == null) {
      singleton = new WindowManager();
    }
    return singleton;
  }

  public void bringToFront(Window w) {
    synchronized (Util.getWebToolkit().getTreeLock()) {
      synchronized (WebPaintDispatcher.webPaintLock) {
        // dont do anything if window is disabled
        if (w != null && !w.isEnabled()) {
          return;
        }
        // do not allow clearing activeWindow if file chooser is active
        if (Util.discoverFileChooser(activeWindow) != null && w == null) {
          return;
        }

        if ((w == null || w.isFocusableWindow()) && activeWindow != w) {
          Window oldActiveWindow = activeWindow;
          activeWindow = w;
          if (activeWindow != null) {
            WindowEvent gainedFocusWindowEvent =
                new WindowEvent(activeWindow, WindowEvent.WINDOW_GAINED_FOCUS, activeWindow, 0, 0);
            Util.getWebToolkit().getEventDispatcher().dispatchEventInSwing(activeWindow,
                gainedFocusWindowEvent);
            Util.getWebToolkit().getPaintDispatcher().notifyWindowActivated(activeWindow);
          }
          if (oldActiveWindow != null) {
            Util.getWebToolkit().getPaintDispatcher().notifyWindowDeactivated(oldActiveWindow);
          }
          if (Util.discoverFileChooser(oldActiveWindow) != null) {
            Util.getWebToolkit().getPaintDispatcher().notifyFileDialogHidden();
          }
          if (Util.discoverFileChooser(activeWindow) != null) {
            Util.getWebToolkit().getPaintDispatcher().notifyFileDialogActive(activeWindow);
          }
        }
        if (w != null) {
          zorder.bringToFront(w);
        }
      }
    }
  }

  public void removeWindow(Window target) {
    synchronized (WebPaintDispatcher.webPaintLock) {
      zorder.removeWindow(target);
      if (target == activeWindow) {
        activeWindow = null;
      }
    }
  }

  public void bringToBack(Window w) {}

  public Window getActiveWindow() {
    return activeWindow;
  }

  public boolean isWindowActive(Window w) {
    if (activeWindow == w) {
      return true;
    } else {
      return false;
    }
  }

  public void activateWindow(Window w) {
    activateWindow(w, 0, 0);
  }

  public boolean activateWindow(Window w, Component newFocusOwner, int x, int y, boolean tmp,
      boolean focusedWindowChangeAllowed, FocusEventCause cause) {
    boolean success = false;
    boolean newWindow = false;

    if (!zorder.contains(w)) {
      if (w.getClass().getName().contains("JLightweightFrame")) {
        return false;
      }
      zorder.addWindow(w);
      newWindow = true;
    }

    if (isBlockedByModality(w, newWindow)) {
      return false;
    }

    // dont allow activation of disabled windows
    if (!w.isEnabled()) {
      return false;
    }

    if (focusedWindowChangeAllowed || activeWindow == w) {
      if (w.isFocusableWindow()) {
        success = deliverFocus(w, newFocusOwner, tmp, cause);
      }

      if (SwingUtilities.isRectangleContainingRectangle(
          new Rectangle(0, 0, w.getWidth(), w.getHeight()), new Rectangle(x, y, 0, 0))) {
        bringToFront(w);
      } else {
        bringToFront(null);
      }
    }

    return success;
  }

  public boolean isBlockedByModality(Window w, boolean newWindow) {
    // if active window is in modal branch and requested window is not modalExclude type
    if (zorder.isInModalBranch(activeWindow) && !(w instanceof sun.awt.ModalExclude)
        && w.getModalExclusionType() == Dialog.ModalExclusionType.NO_EXCLUDE) {
      // if fullModal (not document_modal) branch
      if (zorder.isInFullModalBranch(activeWindow)) {
        // don't allow activation outside modal dialog ancestor's tree
        if (!(isModal(w) && newWindow) && !zorder.isInSameModalBranch(activeWindow, w)) {
          return true;
        }
      } else {// if in document_modal branch
        // don't allow activation in same window branch
        if (zorder.isParent(w, activeWindow)) {
          return true;
        }
      }
    }

    return false;
  }

  public boolean deliverFocus(Component hwComponent, Component newFocusOwner, boolean tmp,
      FocusEventCause cause) {
    boolean success = false;
    if (newFocusOwner != null && newFocusOwner.isFocusable()) {
      int result = Util.getWebToolkit().shouldNativelyFocusHeavyweight(hwComponent, newFocusOwner,
          tmp, true, new Date().getTime(), cause);
      switch (result) {
        case 1:
          success = true;
          break;
        case 2:
          Util.getWebToolkit().deliverFocus(hwComponent, newFocusOwner, tmp, true,
              new Date().getTime(), cause);
          success = true;
          break;
        default:
          break;
      }
    }
    return success;
  }

  protected boolean isModal(Window w) {
    return (w instanceof Dialog d) && d.isModal();
  }

  public void activateWindow(final Window w, final int x, final int y) {
    activateWindow(w, null, x, y, false, true, FocusEventCause.NATIVE_SYSTEM);
  }

  public Window getVisibleWindowOnPosition(int x, int y) {
    Window positionWin = zorder.getVisibleWindowOnPosition(x, y);
    if (positionWin == null) {
      positionWin = activeWindow;
    }
    return positionWin;
  }

  @SuppressWarnings("deprecation")
  public Component getVisibleComponentOnPosition(int x, int y, String winId) {
    Component result = null;
    Window positionWin;
    if (winId != null && winId.length() > 0) {
      positionWin = Util.findWindowById(winId);
    } else {
      positionWin = zorder.getVisibleWindowOnPosition(x, y);
    }
    if (positionWin != null) {
      result = Util.getPeer(positionWin).getHwComponentAt(x, y);
    }
    return result;
  }

  public Map<String, List<Rectangle>> extractNonVisibleAreas() {
    return zorder.extractNonVisibleAreas();
  }

  public List<String> getZOrder() {
    return zorder.getZOrder();
  }

  public void requestRepaintAfterMove(Window w, Rectangle originalPosition) {
    zorder.requestRepaintAfterMove(w, originalPosition);
  }

  public void handleWindowDecorationEvent(Window w, MouseEvent e) {
    Point mousePointer = new Point(e.getX(), e.getY());
    WindowActionType wat =
        Services.getImageService().getWindowDecorationTheme().getAction(w, mousePointer);
    eventhandler.handle(wat, e);

    if (wat.isButtonActionType()) {
      AccessibilityMsgOut accessibilityMsg =
          Services.getImageService().getWindowDecorationTheme().getAccessible(w, wat, mousePointer);
      if (accessibilityMsg != null) {
        Util.getWebToolkit().getPaintDispatcher().notifyAccessibilityInfoUpdate(accessibilityMsg);
      }
    }
  }

  public boolean isLockedToWindowDecorationHandler() {
    return eventhandler.isEventHandlingLocked();
  }

  public Window getLockedToWindow() {
    return eventhandler.getLockedToWindow();
  }

  public String getCurrentCursor() {
    return currentCursor;
  }

  public void setCurrentCursor(String currentCursor) {
    this.currentCursor = currentCursor;
  }

}
