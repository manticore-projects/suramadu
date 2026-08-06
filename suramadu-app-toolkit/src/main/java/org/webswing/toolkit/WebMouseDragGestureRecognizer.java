package org.webswing.toolkit;

import sun.awt.dnd.SunDragSourceContextPeer;

import java.awt.Component;
import java.awt.Point;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.MouseDragGestureRecognizer;
import java.awt.event.MouseEvent;
import java.io.Serial;

@SuppressWarnings("restriction")
public class WebMouseDragGestureRecognizer extends MouseDragGestureRecognizer {

  @Serial
  private static final long serialVersionUID = 2473988169935151977L;
  protected static int motionThreshold;

  protected WebMouseDragGestureRecognizer(DragSource ds, Component c, int act,
      DragGestureListener dgl) {
    super(ds, c, act, dgl);
  }

  protected int mapDragOperationFromModifiers(MouseEvent mouseEvent) {
    int i = mouseEvent.getModifiersEx();
    int j = i & 0x1C00;
    if ((j != 1024) && (j != 2048) && (j != 4096)) {
      return 0;
    }
    return SunDragSourceContextPeer.convertModifiersToDropAction(i, getSourceActions());
  }

  public void mousePressed(MouseEvent mouseEvent) {
    this.events.clear();

    if (mapDragOperationFromModifiers(mouseEvent) != 0) {
      try {
        motionThreshold = DragSource.getDragThreshold();
      } catch (Exception localException) {
        motionThreshold = 5;
      }
      appendEvent(mouseEvent);
    }
  }

  public void mouseReleased(MouseEvent mouseEvent) {
    this.events.clear();
  }

  public void mouseEntered(MouseEvent mouseEvent) {
    this.events.clear();
  }

  public void mouseExited(MouseEvent mouseEvent) {
    if (!this.events.isEmpty()) {
      int i = mapDragOperationFromModifiers(mouseEvent);
      if (i == 0) {
        this.events.clear();
      }
    }
  }

  public void mouseDragged(MouseEvent mouseEvent) {
    if (!this.events.isEmpty()) {
      int i = mapDragOperationFromModifiers(mouseEvent);
      if (i == 0) {
        return;
      }
      MouseEvent localMouseEvent = (MouseEvent) this.events.get(0);
      Point localPoint1 = localMouseEvent.getPoint();
      Point localPoint2 = mouseEvent.getPoint();
      int j = Math.abs(localPoint1.x - localPoint2.x);
      int k = Math.abs(localPoint1.y - localPoint2.y);
      if ((j > motionThreshold) || (k > motionThreshold)) {
        fireDragGestureRecognized(i, ((MouseEvent) getTriggerEvent()).getPoint());
      } else {
        appendEvent(mouseEvent);
      }
    }
  }

}
