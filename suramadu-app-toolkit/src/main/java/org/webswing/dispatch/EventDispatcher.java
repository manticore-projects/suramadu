package org.webswing.dispatch;

import org.webswing.model.app.in.ServerToAppFrameMsgIn;
import org.webswing.model.appframe.in.AppFrameMsgIn;
import org.webswing.toolkit.WebDragSourceContextPeer;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Point;
import java.awt.datatransfer.Transferable;

public interface EventDispatcher {

  void dispatchEvent(final AppFrameMsgIn msg);

  void dispatchEventInSwing(final Component c, final AWTEvent e);

  Point getLastMousePosition();

  boolean isDndInProgress();

  void dragStart(WebDragSourceContextPeer peer, Transferable transferable, int actions,
      long[] formats);

  boolean isJavaFXdragStarted();

  void setJavaFXdragStarted(boolean b);

  void onMessage(ServerToAppFrameMsgIn msg, AppFrameMsgIn frame);

  long getLastEventTimestamp(boolean userEventOnly);

  void resetLastEventTimestamp();

  void resetLastMessageTimestamp();
}
