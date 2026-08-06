package org.webswing.toolkit;

import org.webswing.toolkit.util.Util;

import java.awt.Cursor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.InvalidDnDOperationException;
import java.util.Map;

@SuppressWarnings("restriction")
public class WebDragSourceContextPeer extends sun.awt.dnd.SunDragSourceContextPeer {

  private static final WebDragSourceContextPeer theInstance = new WebDragSourceContextPeer(null);

  public WebDragSourceContextPeer(DragGestureEvent paramDragGestureEvent) {
    super(paramDragGestureEvent);
  }

  static WebDragSourceContextPeer createDragSourceContextPeer(
      DragGestureEvent paramDragGestureEvent) throws InvalidDnDOperationException {
    theInstance.setTrigger(paramDragGestureEvent);
    return theInstance;
  }

  @SuppressWarnings("rawtypes")
  @Override
  protected void startDrag(Transferable paramTransferable, long[] paramArrayOfLong, Map paramMap) {
    WebDropTargetContextPeer.setCurrentJVMLocalSourceTransferable(paramTransferable);

    Util.getWebToolkit().getEventDispatcher().dragStart(this, paramTransferable,
        getTrigger().getSourceAsDragGestureRecognizer().getSourceActions(), paramArrayOfLong);
  }

  @Override
  protected void setNativeCursor(long paramLong, Cursor paramCursor, int paramInt) {

  }

  public void dragEnter(int dropAction, int eventModifiers, int x, int y) {
    postDragSourceDragEvent(dropAction, eventModifiers, x, y, 1);
  }

  public void dragMotion(int dropAction, int eventModifiers, int x, int y) {
    postDragSourceDragEvent(dropAction, eventModifiers, x, y, 2);
  }

  public void dragExit2(int x, int y) {
    dragExit(x, y);
  }

  public void dragMouseMoved(int dropAction, int eventModifiers, int x, int y) {
    postDragSourceDragEvent(dropAction, eventModifiers, x, y, 6);
  }

  public void dragOperationChanged(int dropAction, int eventModifiers, int x, int y) {
    postDragSourceDragEvent(dropAction, eventModifiers, x, y, 3);
  }

  public void dragFinished(boolean success, int dropAction, int x, int y) {
    dragDropFinished(success, dropAction, x, y);
  }
}
