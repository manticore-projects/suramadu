package org.webswing.dispatch;

import org.webswing.Constants;
import org.webswing.model.app.in.ServerToAppFrameMsgIn;
import org.webswing.model.appframe.in.*;
import org.webswing.model.common.in.ConnectionHandshakeMsgIn;
import org.webswing.model.common.in.SimpleEventMsgIn;
import org.webswing.toolkit.WebDragSourceContextPeer;
import org.webswing.toolkit.extra.DndEventHandler;
import org.webswing.toolkit.jslink.WebJSObject;
import org.webswing.toolkit.util.Util;
import org.webswing.util.AppLogger;
import org.webswing.util.NamedThreadFactory;
import sun.awt.UngrabEvent;

import javax.swing.SwingUtilities;
import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public abstract class AbstractEventDispatcher implements EventDispatcher {

  private final AtomicLong lastMessageTimestamp = new AtomicLong(System.currentTimeMillis());
  private final AtomicLong lastUserInputTimestamp = new AtomicLong(System.currentTimeMillis());

  private MouseEvent lastMouseEvent;
  private WebEventDispatcher.MouseEventInfo lastMousePressEvent;
  private final Point lastMousePosition = new Point();
  private final AtomicBoolean javaFXdragStarted = new AtomicBoolean(false);
  private Component lastEnteredWindow;
  private final DndEventHandler dndHandler;
  private final HashMap<String, String> uploadMap = new HashMap<String, String>();
  private final ExecutorService eventDispatcher = Executors
      .newSingleThreadExecutor(NamedThreadFactory.getInstance("Webswing Event Dispatcher"));
  // release char map derives the event char for keyrelease event from previous keypressed events
  // (keycode=char)
  private final HashMap<Integer, Integer> releaseCharMap = new HashMap<Integer, Integer>();

  private static final long DOUBLE_CLICK_MAX_DELAY =
      Long.getLong(Constants.SWING_START_SYS_PROP_DOUBLE_CLICK_DELAY, 750);
  private static final int CLICK_TOLERANCE = 2;

  // these keycodes are assigned to different keys in browser
  protected static final List<Integer> NON_STANDARD_KEY_CODES = Arrays.asList(KeyEvent.VK_KP_DOWN,
      KeyEvent.VK_KP_UP, KeyEvent.VK_KP_RIGHT, KeyEvent.VK_KP_LEFT);
  protected static final Map<Integer, Integer> CONVERTED_KEY_CODES =
      new HashMap<Integer, Integer>();

  static {
    CONVERTED_KEY_CODES.put(45, KeyEvent.VK_INSERT); // Insert 155
    CONVERTED_KEY_CODES.put(46, KeyEvent.VK_DELETE); // Delete 127
    CONVERTED_KEY_CODES.put(93, KeyEvent.VK_CONTEXT_MENU); // Context Menu 525
    CONVERTED_KEY_CODES.put(189, KeyEvent.VK_MINUS); // Minus 45
    CONVERTED_KEY_CODES.put(187, KeyEvent.VK_EQUALS); // Equals 61
    CONVERTED_KEY_CODES.put(219, KeyEvent.VK_OPEN_BRACKET); // Open Bracket 91
    CONVERTED_KEY_CODES.put(221, KeyEvent.VK_CLOSE_BRACKET); // Close Bracket 93
    CONVERTED_KEY_CODES.put(186, KeyEvent.VK_SEMICOLON); // Semicolon 59
    CONVERTED_KEY_CODES.put(220, KeyEvent.VK_BACK_SLASH); // Back Slash 92
    CONVERTED_KEY_CODES.put(226, KeyEvent.VK_BACK_SLASH); // Back Slash 92
    CONVERTED_KEY_CODES.put(188, KeyEvent.VK_COMMA); // Comma 44
    CONVERTED_KEY_CODES.put(190, KeyEvent.VK_PERIOD); // Period 46
    CONVERTED_KEY_CODES.put(191, KeyEvent.VK_SLASH); // Slash 47
  }

  public AbstractEventDispatcher() {
    this.dndHandler = new DndEventHandler();
  }

  public void onMessage(ServerToAppFrameMsgIn msgIn, AppFrameMsgIn frame) {
    try {
      resetLastMessageTimestamp();
      if (msgIn != null) {
        if (msgIn.getThreadDumpRequest() != null) {
          Util.getWebToolkit().getSessionWatchdog().requestThreadDump();
        }
        if (msgIn.getApiEvent() != null) {
          Util.getWebToolkit().processApiEvent(msgIn.getApiEvent());
        }
        if (msgIn.getHandshake() != null) {
          dispatchHandshakeEvent(msgIn.getHandshake());
        }
        if (msgIn.getEvents() != null) {
          msgIn.getEvents().forEach(this::dispatchMessage);
        }
        msgIn.getTimestamps();
        if (frame != null) {
          if (frame.getJavaRequest() != null) {
            WebJSObject.evaluateJava(frame.getJavaRequest());
          }
          dispatchEvent(frame);
        }
      }
    } catch (Exception e) {
      AppLogger.error("AbstractEventDispatcher.onMessage", e);
    }
  }

  @Override
  public void resetLastEventTimestamp() {
    lastUserInputTimestamp.getAndSet(System.currentTimeMillis());
  }

  @Override
  public void resetLastMessageTimestamp() {
    lastMessageTimestamp.getAndSet(System.currentTimeMillis());
  }

  public void dispatchEvent(final AppFrameMsgIn msg) {
    AppLogger.debug("AbstractEventDispatcher.dispatchEvent:", msg);
    eventDispatcher.submit(() -> {
      try {
        if (msg.getEvents() != null) {
          msg.getEvents().forEach(inputEvent -> {
            if (inputEvent.getMouse() != null) {
              resetLastEventTimestamp();
              dispatchMouseEvent(inputEvent.getMouse());
            }
            if (inputEvent.getKey() != null) {
              resetLastEventTimestamp();
              dispatchKeyboardEvent(inputEvent.getKey());
            }
            if (inputEvent.getFocus() != null) {
              windowFocusEvent(inputEvent.getFocus());
            }
          });
        }
        if (msg.getPaste() != null) {
          handlePasteEvent(msg.getPaste());
        }
        if (msg.getCopy() != null) {
          handleCopyEvent(msg.getCopy());
        }
        if (msg.getSelected() != null) {
          handleFileSelectionEvent(msg.getSelected());
        }
        if (msg.getUpload() != null) {
          handleUploadEvent(msg.getUpload());
        }
        if (msg.getWindow() != null) {
          handleWindowEvent(msg.getWindow());
        }
        if (msg.getAudio() != null) {
          handleAudioEvent(msg.getAudio());
        }
        if (msg.getAction() != null) {
          handleActionEvent(msg.getAction());
        }
      } catch (Throwable e) {
        AppLogger.error("Failed to process event.", e);
      }
    });
  }

  protected abstract void windowFocusEvent(WindowFocusMsgIn event);

  protected abstract void dispatchMouseEvent(MouseEventMsgIn event);

  protected abstract void dispatchKeyboardEvent(KeyboardEventMsgIn event);

  protected abstract void dispatchHandshakeEvent(ConnectionHandshakeMsgIn handshake);

  protected abstract void dispatchMessage(SimpleEventMsgIn event);

  protected abstract void handlePasteEvent(PasteEventMsgIn event);

  protected abstract void handleCopyEvent(CopyEventMsgIn event);

  protected abstract void handleFileSelectionEvent(FilesSelectedEventMsgIn event);

  protected abstract void handleUploadEvent(UploadEventMsgIn event);

  protected abstract void handleWindowEvent(WindowEventMsgIn event);

  protected abstract void handleAudioEvent(AudioEventMsgIn event);

  protected abstract void handleActionEvent(ActionEventMsgIn event);

  public void dispatchEventInSwing(final Component c, final AWTEvent e) {
    Window w = (Window) (c instanceof Window ? c : SwingUtilities.windowForComponent(c));
    if (w.isEnabled()) {
      AppLogger.debug("WebEventDispatcher.dispatchEventInSwing:postSystemQueue", e);
      dispatchEnterExitEvents(w, e);
      Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(e);
    }
  }

  public void dragStart(WebDragSourceContextPeer peer, Transferable transferable, int actions,
      long[] formats) {
    getDndHandler().dragStart(peer, transferable, actions, formats);
  }

  public Point getLastMousePosition() {
    return lastMousePosition;
  }


  protected void dispatchPasteEvent(boolean special) {
    KeyboardEventMsgIn event = new KeyboardEventMsgIn();
    event.setType(KeyboardEventMsgIn.KeyEventType.keydown);
    event.setCharacter(KeyEvent.VK_V);
    event.setKeycode(KeyEvent.VK_V); // 'v'
    event.setCtrl(true);
    event.setShift(special);
    dispatchKeyboardEvent(event);
    event.setType(KeyboardEventMsgIn.KeyEventType.keyup);
    dispatchKeyboardEvent(event);
  }

  protected void dispatchCopyEvent() {
    KeyboardEventMsgIn event = new KeyboardEventMsgIn();
    event.setType(KeyboardEventMsgIn.KeyEventType.keydown);
    event.setCharacter(KeyEvent.VK_C); // 'c'
    event.setKeycode(KeyEvent.VK_C);
    event.setCtrl(true);
    dispatchKeyboardEvent(event);
    event.setType(KeyboardEventMsgIn.KeyEventType.keyup);
    dispatchKeyboardEvent(event);
  }

  protected void dispatchCutEvent() {
    KeyboardEventMsgIn event = new KeyboardEventMsgIn();
    event.setType(KeyboardEventMsgIn.KeyEventType.keydown);
    event.setCharacter(KeyEvent.VK_X);
    event.setKeycode(KeyEvent.VK_X); // 'x'
    event.setCtrl(true);
    dispatchKeyboardEvent(event);
    event.setType(KeyboardEventMsgIn.KeyEventType.keyup);
    dispatchKeyboardEvent(event);
  }

  protected void dispatchKeyEventInSwing(final Component c, final AWTEvent e) {
    Window w = (Window) (c instanceof Window ? c : SwingUtilities.windowForComponent(c));
    if (w.isEnabled()) {
      if (isDndInProgress()) {
        getDndHandler().processMouseEvent(w, e);
      } else {
        dispatchEventInSwing(c, e);
      }
      Util.getWebToolkit().getPaintDispatcher().notifyAccessibilityInfoUpdate();
    }
  }

  protected void dispatchMouseEventInSwing(final Component c, final MouseEvent e,
      boolean relatedToPreviousEvent) {
    Window w = (Window) (c instanceof Window ? c : SwingUtilities.windowForComponent(c));
    if (w.isEnabled()) {
      w.setCursor(w.getCursor()); // force cursor update
      Util.getWebToolkit().getPaintDispatcher().notifyAccessibilityInfoUpdate(c, e.getXOnScreen(),
          e.getYOnScreen());
      if ((!relatedToPreviousEvent && Util.isWindowDecorationEvent(w, e))
          || Util.getWebToolkit().getWindowManager().isLockedToWindowDecorationHandler()) {
        AppLogger.debug("WebEventDispatcher.dispatchEventInSwing:windowManagerHandle", e);
        if (e.getID() == MouseEvent.MOUSE_PRESSED) {
          dispatchEventInSwing(c, new UngrabEvent(c));
        }
        Util.getWebToolkit().getWindowManager().handleWindowDecorationEvent(w, (MouseEvent) e);
      } else if (isDndInProgress()) {
        getDndHandler().processMouseEvent(w, e);
      } else {
        dispatchEventInSwing(c, e);
      }
    }
  }


  public boolean isDndInProgress() {
    return dndHandler.isDndInProgress();
  }

  protected DndEventHandler getDndHandler() {
    return dndHandler;
  }

  protected int computeClickCount(int x, int y, int button, boolean isPressed, int timeMilis) {
    if (isPressed) {
      if (isNearLastMousePressEvent(x, y) && lastMousePressEvent.type == MouseEvent.MOUSE_CLICKED
          && lastMousePressEvent.button == button) {
        if (timeMilis - lastMousePressEvent.time < getDoubleClickMaxDelay()) {
          return lastMousePressEvent.clickcount + 1;
        }
      }
    } else {
      if (lastMousePressEvent != null && lastMousePressEvent.type == MouseEvent.MOUSE_PRESSED
          && lastMousePressEvent.button == button) {
        return lastMousePressEvent.clickcount;
      }
    }
    return 1;
  }

  protected boolean isNearLastMousePressEvent(int x, int y) {
    return lastMousePressEvent != null && Math.abs(lastMousePressEvent.x - x) < getClickTolerance()
        && Math.abs(lastMousePressEvent.y - y) < getClickTolerance();
  }

  protected long getDoubleClickMaxDelay() {
    return DOUBLE_CLICK_MAX_DELAY;
  }

  protected int getClickTolerance() {
    return CLICK_TOLERANCE;
  }

  protected HashMap<String, String> getFileUploadMap() {
    return uploadMap;
  }

  protected HashMap<Integer, Integer> getReleaseCharMap() {
    return releaseCharMap;
  }

  protected MouseEvent getLastMouseEvent() {
    return lastMouseEvent;
  }

  protected void setLastMouseEvent(MouseEvent lastMouseEvent) {
    this.lastMouseEvent = lastMouseEvent;
  }

  protected boolean relatedToLastEvent(MouseEventMsgIn event) {
    return lastMouseEvent != null
        && (lastMouseEvent.getID() == MouseEvent.MOUSE_DRAGGED
            || lastMouseEvent.getID() == MouseEvent.MOUSE_PRESSED)
        && ((event.getType() == MouseEventMsgIn.MouseEventType.mousemove && event.getButtons() != 0)
            || (event.getType() == MouseEventMsgIn.MouseEventType.mouseup));
  }

  protected void setLastMousePosition(int x, int y) {
    lastMousePosition.x = x;
    lastMousePosition.y = y;
  }

  protected Component getLastEnteredWindow() {
    return lastEnteredWindow;
  }

  protected void setLastEnteredWindow(Component lastEnteredWindow) {
    this.lastEnteredWindow = lastEnteredWindow;
  }

  protected void setLastMousePressEvent(MouseEvent e, int timeMilis) {
    lastMousePressEvent = MouseEventInfo.get(e, timeMilis);
  }


  public boolean isJavaFXdragStarted() {
    return javaFXdragStarted.get();
  }

  public void setJavaFXdragStarted(boolean b) {
    javaFXdragStarted.getAndSet(b);
  }


  protected void dispatchEnterExitEvents(Window w, AWTEvent e) {
    if (e instanceof MouseEvent oe && getLastEnteredWindow() != w) {
      dispatchExitEvent(oe.getWhen(), oe.getModifiersEx() | oe.getModifiers(), oe.getX(), oe.getY(),
          oe.getXOnScreen(), oe.getYOnScreen());
      if (w != null) {
        MouseEvent enterEvent = new MouseEvent(w, MouseEvent.MOUSE_ENTERED, oe.getWhen(),
            oe.getModifiersEx() | oe.getModifiers(), oe.getX(), oe.getY(), oe.getXOnScreen(),
            oe.getYOnScreen(), oe.getClickCount(), oe.isPopupTrigger(), oe.getButton());
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(enterEvent);
      }
      setLastEnteredWindow(w);
    }
  }

  protected void dispatchExitEvent(long when, int mod, int x, int y, int absX, int absY) {
    if (getLastEnteredWindow() != null && getLastEnteredWindow().isShowing()) {
      MouseEvent exitEvent = new MouseEvent(getLastEnteredWindow(), MouseEvent.MOUSE_EXITED, when,
          mod, x, y, absX, absY, 0, false, 0);
      Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(exitEvent);
      setLastEnteredWindow(null);
    }
  }

  @Override
  public long getLastEventTimestamp(boolean userEventOnly) {
    return userEventOnly ? lastUserInputTimestamp.get() : lastMessageTimestamp.get();
  }



  protected static class MouseEventInfo {
    final int x;
    final int y;
    final int type;
    final int button;
    final int clickcount;
    final int time;

    private MouseEventInfo(MouseEvent e, int time) {
      this.x = e.getX();
      this.y = e.getY();
      this.type = e.getID();
      this.clickcount = e.getClickCount();
      this.button = e.getButton();
      this.time = time;
    }

    public static MouseEventInfo get(MouseEvent e, int time) {
      return new MouseEventInfo(e, time);
    }
  }
}
