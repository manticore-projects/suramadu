package org.webswing.dispatch;

import org.webswing.model.app.out.AppToServerFrameMsgOut;
import org.webswing.model.appframe.out.AppFrameMsgOut;
import org.webswing.model.appframe.out.WindowMoveActionMsgOut;
import org.webswing.toolkit.WebComponentPeer;
import org.webswing.toolkit.WebToolkit;
import org.webswing.toolkit.WebWindowPeer;
import org.webswing.toolkit.api.component.HtmlPanel;
import org.webswing.toolkit.extra.WebRepaintManager;
import org.webswing.toolkit.extra.WindowManager;
import org.webswing.toolkit.util.Util;
import org.webswing.util.AppLogger;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class WebPaintDispatcher extends AbstractPaintDispatcher {
  private volatile WindowMoveActionMsgOut moveAction;

  public WebPaintDispatcher() {
    Runnable sendUpdate = () -> {
      try {
        if (!isClientReadyToReceiveOrResetAfterTimedOut()) {
          return;
        }

        AppToServerFrameMsgOut msgOut = new AppToServerFrameMsgOut();

        AppFrameMsgOut frame;
        Map<String, Map<Integer, BufferedImage>> windowImages = null;
        Map<String, Image> windowWebImages = null;
        Map<String, Set<Rectangle>> currentAreasToUpdate;
        synchronized (Util.getWebToolkit().getTreeLock()) {
          synchronized (webPaintLock) {

            WebRepaintManager.processDirtyComponents();

            currentAreasToUpdate = popProcessableDirtyAreas();

            if (currentAreasToUpdate.isEmpty() && !hasMoveAction()) {
              return;
            }

            frame = Util.fillWithWindowsData(currentAreasToUpdate);

            if (Util.isDD()) {
              windowWebImages =
                  Util.extractWindowWebImages(currentAreasToUpdate.keySet(), new HashMap<>());
            } else {
              windowImages = Util.extractWindowImages(frame, new HashMap<>());
            }

            fillMoveAction(frame);
            setClientNotReady();
          }
        }
        AppLogger.trace("contentSender:paintJson", frame);
        if (Util.isDD()) {
          AppLogger.trace("contentSender:pngWebImageEncodingStart", frame.hashCode());
          Util.encodeWindowWebImages(windowWebImages, frame);
          AppLogger.trace("contentSender:pngWebImageEncodingDone", frame.hashCode());
        } else {
          AppLogger.trace("contentSender:pngEncodingStart", frame.hashCode());
          Util.encodeWindowImages(windowImages, frame);
          AppLogger.trace("contentSender:pngEncodingDone", frame.hashCode());
        }

        frame.setDirectDraw(Util.isDD());
        frame.setCompositingWM(Util.isCompositingWM());
        frame.setSendTimestamp("" + System.currentTimeMillis());

        sendObject(msgOut, frame);
      } catch (Throwable e) {
        AppLogger.error("contentSender:error", e);
      }
    };
    Integer delay = Integer.getInteger("webswing.drawDelayMs", 33);
    getExecutorService().scheduleWithFixedDelay(sendUpdate, delay, delay, TimeUnit.MILLISECONDS);
  }

  public void notifyWindowAreaRepainted(String guid, Rectangle repaintedArea) {
    addDirtyArea(guid, repaintedArea);
  }

  public void notifyWindowAreaVisible(String guid, Rectangle visibleArea) {
    addDirtyArea(guid, visibleArea);
  }

  public void notifyWindowBoundsChanged(String guid, Rectangle newBounds) {
    // TODO: do we need this? addDirtyArea(guid, newBounds, true);
  }

  public void notifyWindowActivated(Window activeWindow) {
    WebComponentPeer activeWindowPeer = (WebComponentPeer) WebToolkit.targetToPeer(activeWindow);
    activeWindowPeer.updateWindowDecorationImage();
    addDirtyArea(activeWindow);
  }

  public void notifyWindowDeactivated(Window oldActiveWindow) {
    WebComponentPeer oldActiveWindowPeer =
        (WebComponentPeer) WebToolkit.targetToPeer(oldActiveWindow);
    oldActiveWindowPeer.updateWindowDecorationImage();
    addDirtyArea(oldActiveWindow);
  }

  public void notifyWindowZOrderChanged(Window w) {
    w.repaint();
    addDirtyArea(w);
  }

  public void notifyWindowMaximized(JFrame target) {
    final JFrame f = target;
    f.setLocation(0, 0);
    final Dimension originalSize = f.getSize();
    final Dimension newSize = Toolkit.getDefaultToolkit().getScreenSize();
    if (!originalSize.equals(newSize)) {
      SwingUtilities.invokeLater(() -> f.setSize(newSize));
    }
  }

  public void notifyBackgroundAreaVisible(Rectangle toRepaint) {
    addDirtyArea(WebToolkit.BACKGROUND_WINDOW_ID, toRepaint);
  }

  public void notifyWindowMoved(Window w, int zIndex, Rectangle from, Rectangle to) {
    // just to notify that a window was moved, the moving handled by client
    if (zIndex == 0 && w.getWidth() == from.width && w.getHeight() == from.height) {
      synchronized (webPaintLock) {
        if (!hasMoveAction()) {
          setMoveAction(
              new WindowMoveActionMsgOut(from.x, from.y, to.x, to.y, from.width, from.height));
          notifyRepaintOffScreenAreas(w, getMoveAction());
        } else if (getMoveAction().getDx() == from.x && getMoveAction().getDy() == from.y
            && getMoveAction().getWidth() == from.width
            && getMoveAction().getHeight() == from.height) {
          getMoveAction().setDx(to.x);
          getMoveAction().setDy(to.y);
          notifyRepaintOffScreenAreas(w, getMoveAction());
        } else {
          addDirtyArea(w);
        }
      }
    } else {
      addDirtyArea(w);
    }
  }

  @SuppressWarnings("restriction")
  private void notifyRepaintOffScreenAreas(Window w, WindowMoveActionMsgOut m) {
    Rectangle screen = new Rectangle(Util.getWebToolkit().getScreenSize());
    Rectangle before = new Rectangle(m.getSx(), m.getSy(), m.getWidth(), m.getHeight());
    Rectangle after = new Rectangle(m.getDx(), m.getDy(), m.getWidth(), m.getHeight());
    int xdiff = m.getSx() - m.getDx();
    int ydiff = m.getSy() - m.getDy();
    Rectangle[] invisibleBefore = SwingUtilities.computeDifference(before, screen);
    if (invisibleBefore.length != 0) {
      for (Rectangle r : invisibleBefore) {
        r.setLocation(r.x - xdiff, r.y - ydiff);
      }
      Rectangle[] invisibleAfter = SwingUtilities.computeDifference(after, screen);
      List<Rectangle> toRepaint = Util.joinRectangles(
          Util.getGrid(Arrays.asList(invisibleBefore), Arrays.asList(invisibleAfter)));
      WebWindowPeer peer = (WebWindowPeer) WebToolkit.targetToPeer(w);
      for (Rectangle r : toRepaint) {
        r.setLocation(r.x - w.getX(), r.y - w.getY());
        addDirtyArea(peer.getGuid(), r);
      }
    }
  }

  public void registerWebContainer(Container container) {
    throw new UnsupportedOperationException(
        "Only supported when Composition Window Manager is enabled");
  }

  public Map<Window, List<Container>> getRegisteredWebContainersAsMap() {
    throw new UnsupportedOperationException(
        "Only supported when Composition Window Manager is enabled");
  }

  public void registerHtmlPanel(HtmlPanel hp) {
    throw new UnsupportedOperationException(
        "Only supported when Composition Window Manager is enabled");
  }

  public Map<Window, List<HtmlPanel>> getRegisteredHtmlPanelsAsMap() {
    throw new UnsupportedOperationException(
        "Only supported when Composition Window Manager is enabled");
  }

  public HtmlPanel findHtmlPanelById(String id) {
    throw new UnsupportedOperationException(
        "Only supported when Composition Window Manager is enabled");
  }

  @Override
  public void notifyWindowDockStateChanged() {
    throw new UnsupportedOperationException(
        "Only supported when Composition Window Manager is enabled");
  }

  private void fillMoveAction(AppFrameMsgOut json) {
    if (moveAction != null) {
      json.setMoveAction(moveAction);
      moveAction = null;
    }
  }

  protected boolean hasMoveAction() {
    return moveAction != null;
  }

  protected WindowMoveActionMsgOut getMoveAction() {
    return moveAction;
  }

  protected void setMoveAction(WindowMoveActionMsgOut moveAction) {
    this.moveAction = moveAction;
  }

  @Override
  protected String getCurrentCursor(String winId) {
    return WindowManager.getInstance().getCurrentCursor();
  }

  @Override
  protected void setCurrentCursor(String winId, String cursor) {
    WindowManager.getInstance().setCurrentCursor(cursor);
  }

}
