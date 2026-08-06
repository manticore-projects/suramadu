package org.webswing.javafx.toolkit;

import com.sun.glass.events.KeyEvent;
import com.sun.glass.ui.Clipboard;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.View;
import com.sun.javafx.geom.RectBounds;
import com.sun.prism.web.WebTextureWrapper;
import javafx.stage.Stage;
import org.webswing.ext.services.ToolkitFXService;
import org.webswing.javafx.toolkit.adaper.WindowAdapter;
import org.webswing.javafx.toolkit.util.WebFxUtil;
import org.webswing.toolkit.util.Services;
import org.webswing.toolkit.util.Util;
import org.webswing.util.AppLogger;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;
import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.InvocationEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * Created by vikto on 01-Mar-17.
 */
public class WebFxView extends View {

  BufferedImage image;
  BufferedImage tmp;

  JComponent canvas;

  @Override
  protected void _enableInputMethodEvents(long ptr, boolean enable) {}

  @Override
  protected long _create(Map capabilities) {
    canvas = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        if (image != null) {
          g.drawImage(image, 0, 0, null);
        }
      }

    };
    canvas.setSize(new Dimension(1, 1));
    canvas.setFocusTraversalKeysEnabled(false);
    notifyResize(canvas.getSize().width, canvas.getSize().height);
    canvas.addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent componentEvent) {
        Dimension size = canvas.getSize();
        notifyResize(size.width, size.height);
      }
    });
    canvas.addMouseListener(new MouseListener() {
      @Override
      public void mouseClicked(MouseEvent e) {
        // syntetic
      }

      @Override
      public void mousePressed(MouseEvent e) {
        handleMouseEvent(e);
      }

      @Override
      public void mouseReleased(MouseEvent e) {
        handleMouseEvent(e);
      }

      @Override
      public void mouseEntered(MouseEvent e) {
        handleMouseEvent(e);
      }

      @Override
      public void mouseExited(MouseEvent e) {
        handleMouseEvent(e);
      }
    });
    canvas.addMouseMotionListener(new MouseMotionListener() {
      @Override
      public void mouseDragged(MouseEvent e) {
        handleMouseEvent(e);
      }

      @Override
      public void mouseMoved(MouseEvent e) {
        handleMouseEvent(e);
      }
    });
    canvas.addMouseWheelListener(new MouseWheelListener() {
      @Override
      public void mouseWheelMoved(MouseWheelEvent e) {
        handleMouseEvent(e);
      }
    });

    canvas.addKeyListener(new KeyListener() {
      @Override
      public void keyTyped(java.awt.event.KeyEvent e) {
        handleKeyEvent(e);
      }

      @Override
      public void keyPressed(java.awt.event.KeyEvent e) {
        handleKeyEvent(e);
      }

      @Override
      public void keyReleased(java.awt.event.KeyEvent e) {
        handleKeyEvent(e);
      }
    });
    return 1L;
  }

  private void handleKeyEvent(java.awt.event.KeyEvent e) {
    EventHandler eventHandler = getEventHandler();
    if (eventHandler == null) {
      return;
    }
    long time = getTime(e);
    int action = mapAction(e);
    int keyCode = mapKeyCode(e);
    int modifiers = mapModifiers(e.getModifiersEx());
    char[] chars = new char[] {e.getKeyChar()};
    eventHandler.handleKeyEvent(this, time, action, keyCode, chars, modifiers);
  }

  private int mapKeyCode(java.awt.event.KeyEvent e) {
    return e.getKeyCode();
  }

  private int mapAction(java.awt.event.KeyEvent e) {
    switch (e.getID()) {
      case java.awt.event.KeyEvent.KEY_PRESSED:
        return KeyEvent.PRESS;
      case java.awt.event.KeyEvent.KEY_RELEASED:
        return KeyEvent.RELEASE;
      case java.awt.event.KeyEvent.KEY_TYPED:
        return KeyEvent.TYPED;
    }
    return KeyEvent.PRESS;
  }

  private void handleMouseEvent(MouseEvent e) {
    EventHandler eventHandler = getEventHandler();
    if (eventHandler == null) {
      return;
    }
    long time = getTime(e);
    int type = mapType(e.getID());
    if (type != 0) {
      int button = mapButton(e.getButton());
      button =
          type == com.sun.glass.events.MouseEvent.MOVE ? com.sun.glass.events.MouseEvent.BUTTON_NONE
              : button;
      int modifiers = mapModifiers(e.getModifiersEx());
      if (e.getID() == MouseEvent.MOUSE_WHEEL) {
        MouseWheelEvent we = (MouseWheelEvent) e;
        eventHandler.handleScrollEvent(this, time, e.getX(), e.getY(), e.getXOnScreen(),
            e.getYOnScreen(), 0, -we.getPreciseWheelRotation(), modifiers, 1, 1, 1, 1, 1, 1);
      } else {
        if (Util.getWebToolkit().getEventDispatcher().isJavaFXdragStarted()) {
          if (e.getID() != MouseEvent.MOUSE_WHEEL) {
            if (e.getButton() == MouseEvent.BUTTON1 && e.getID() == MouseEvent.MOUSE_RELEASED) {
              try {
                int currentAction = notifyDragOver(e.getX(), e.getY(), e.getXOnScreen(),
                    e.getYOnScreen(), mapDropAction(e.getModifiersEx()));
                if (currentAction != Clipboard.ACTION_NONE) {
                  notifyDragDrop(e.getX(), e.getY(), e.getXOnScreen(), e.getYOnScreen(),
                      currentAction);
                } else {
                  notifyDragEnd(currentAction);
                }
              } catch (Exception ex) {
                AppLogger.debug("Exception on event", ex);
              } finally {
                Util.getWebToolkit().getEventDispatcher().setJavaFXdragStarted(false);
                setDragCursor(-1);
              }
            } else if ((e.getModifiersEx()
                & MouseEvent.BUTTON1_DOWN_MASK) == MouseEvent.BUTTON1_DOWN_MASK
                && e.getID() == MouseEvent.MOUSE_DRAGGED) {
              try {
                int cursorid = notifyDragOver(e.getX(), e.getY(), e.getXOnScreen(),
                    e.getYOnScreen(), mapDropAction(e.getModifiersEx()));
                setDragCursor(cursorid);
              } catch (Exception ex) {
                AppLogger.debug("Exception on event", ex);
                setDragCursor(0);
              }
            } else if (e.getID() == MouseEvent.MOUSE_EXITED) {
              setDragCursor(-1);
              notifyDragLeave();
            } else if (e.getID() == MouseEvent.MOUSE_ENTERED) {
              notifyDragEnter(e.getX(), e.getY(), e.getXOnScreen(), e.getYOnScreen(),
                  mapDropAction(e.getModifiersEx()));
            } else {
              notifyDragEnd(mapDropAction(e.getModifiersEx()));
              Util.getWebToolkit().getEventDispatcher().setJavaFXdragStarted(false);
              setDragCursor(-1);
            }
          }
        } else {
          eventHandler.handleMouseEvent(this, time, type, button, e.getX(), e.getY(),
              e.getXOnScreen(), e.getYOnScreen(), modifiers, e.isPopupTrigger(), false);
          if (button == com.sun.glass.events.MouseEvent.BUTTON_RIGHT
              && type == com.sun.glass.events.MouseEvent.DOWN) {
            eventHandler.handleMenuEvent(this, e.getX(), e.getY(), e.getXOnScreen(),
                e.getYOnScreen(), false);
          }
          if (Util.getWebToolkit().getEventDispatcher().isJavaFXdragStarted()) {// drag initiated by
                                                                                // this event
            notifyDragStart(button, e.getX(), e.getY(), e.getXOnScreen(), e.getYOnScreen());
          }
        }
      }
    }
  }

  private long getTime(InputEvent e) {
    return System.nanoTime() - ((System.currentTimeMillis() - e.getWhen()) * 1000000);
  }

  private void setDragCursor(int action) {
    try {
      if (getWindow() != null && getWindow() instanceof WebWindow) {
        Cursor c = null;
        if (action == Clipboard.ACTION_NONE) {
          c = Cursor.getSystemCustomCursor("Invalid.32x32");
        }
        if (action == Clipboard.ACTION_COPY) {
          c = Cursor.getSystemCustomCursor("CopyDrop.32x32");
        }
        if (action == Clipboard.ACTION_MOVE) {
          c = Cursor.getSystemCustomCursor("MoveDrop.32x32");
        }
        WebWindow w = (WebWindow) getWindow();
        w.setDragCursor(c);
      }
    } catch (AWTException e) {
    }
  }

  private int mapModifiers(int swingMod) {
    int modifiers = 0;
    if ((swingMod & MouseEvent.BUTTON1_DOWN_MASK) != 0) {
      modifiers |= KeyEvent.MODIFIER_BUTTON_PRIMARY;
    }
    if ((swingMod & MouseEvent.BUTTON2_DOWN_MASK) != 0) {
      modifiers |= KeyEvent.MODIFIER_BUTTON_MIDDLE;
    }
    if ((swingMod & MouseEvent.BUTTON3_DOWN_MASK) != 0) {
      modifiers |= KeyEvent.MODIFIER_BUTTON_SECONDARY;
    }
    if ((swingMod & MouseEvent.ALT_DOWN_MASK) != 0) {
      modifiers |= KeyEvent.MODIFIER_ALT;
    }
    if ((swingMod & MouseEvent.CTRL_DOWN_MASK) != 0) {
      modifiers |= KeyEvent.MODIFIER_CONTROL;
    }
    if ((swingMod & MouseEvent.SHIFT_DOWN_MASK) != 0) {
      modifiers |= KeyEvent.MODIFIER_SHIFT;
    }
    if ((swingMod & MouseEvent.META_DOWN_MASK) != 0) {
      modifiers |= KeyEvent.MODIFIER_WINDOWS;
    }
    return modifiers;
  }

  private int mapDropAction(int swingMod) {
    int action = Clipboard.ACTION_MOVE;
    if ((swingMod & MouseEvent.CTRL_DOWN_MASK) != 0) {
      action = Clipboard.ACTION_COPY;
    }
    return action;
  }

  private int mapButton(int swingButton) {
    switch (swingButton) {
      case MouseEvent.NOBUTTON:
        return com.sun.glass.events.MouseEvent.BUTTON_NONE;
      case MouseEvent.BUTTON1:
        return com.sun.glass.events.MouseEvent.BUTTON_LEFT;
      case MouseEvent.BUTTON2:
        return com.sun.glass.events.MouseEvent.BUTTON_OTHER;
      case MouseEvent.BUTTON3:
        return com.sun.glass.events.MouseEvent.BUTTON_RIGHT;
    }
    return com.sun.glass.events.MouseEvent.BUTTON_NONE;
  }

  private int mapType(int swingType) {
    switch (swingType) {
      case MouseEvent.MOUSE_PRESSED:
        return com.sun.glass.events.MouseEvent.DOWN;
      case MouseEvent.MOUSE_RELEASED:
        return com.sun.glass.events.MouseEvent.UP;
      case MouseEvent.MOUSE_DRAGGED:
        return com.sun.glass.events.MouseEvent.DRAG;
      case MouseEvent.MOUSE_MOVED:
        return com.sun.glass.events.MouseEvent.MOVE;
      case MouseEvent.MOUSE_ENTERED:
        return com.sun.glass.events.MouseEvent.ENTER;
      case MouseEvent.MOUSE_EXITED:
        return com.sun.glass.events.MouseEvent.EXIT;
      case MouseEvent.MOUSE_WHEEL:
        return com.sun.glass.events.MouseEvent.WHEEL;
    }
    return 0;
  }

  @Override
  protected long _getNativeView(long ptr) {
    return 1L;
  }

  @Override
  protected int _getX(long ptr) {
    return 0;
  }

  @Override
  protected int _getY(long ptr) {
    return 0;
  }

  @Override
  protected void _setParent(long ptr, long parentPtr) {}

  @Override
  protected boolean _close(long ptr) {
    return false;
  }

  @Override
  protected void _scheduleRepaint(long ptr) {
    if (getWindow() != null) {
      Window win = ((WebWindow) getWindow()).w.getThis();
      win.repaint();
    }
  }

  @Override
  protected void _begin(long ptr) {

  }

  @Override
  protected void _end(long ptr) {

  }

  @Override
  protected int _getNativeFrameBuffer(long ptr) {
    return 0;
  }

  @Override
  protected void _uploadPixels(long ptr, Pixels pixels) {
    if (getWindow() != null) {
      if (Util.isTestMode()) {
        lookupStages();
      }

      Window win = ((WebWindow) getWindow()).w.getThis();
      RepaintManager rm = RepaintManager.currentManager(win);
      Set<RectBounds> tmpBounds =
          WebTextureWrapper.dirtyAreaTracker.popDirtyAreasForBuffer(pixels.getPixels());
      BufferedImage previous = this.image;
      this.image = WebFxUtil.pixelsToImage(this.tmp, pixels);
      if (previous != null && this.image != null && previous.getWidth() == this.image.getWidth()
          && previous.getHeight() == this.image.getHeight()
          && previous.getType() == this.image.getType()) {

        long start = System.currentTimeMillis();
        Set<RectBounds> diff = WebFxUtil.findUpdateAreas(this.image, previous, tmpBounds);
        AppLogger.debug("WebFxView#_uploadPixels: Compared in "
            + (System.currentTimeMillis() - start) + "ms, diff: " + diff.size());

        for (RectBounds r : diff) {
          rm.addDirtyRegion(win, (int) Math.floor(r.getMinX()) + win.getInsets().left,
              (int) Math.floor(r.getMinY()) + win.getInsets().top, (int) Math.ceil(r.getWidth()),
              (int) Math.ceil(r.getHeight()));
        }

        // recycle previous image
        this.tmp = previous;
      } else {
        win.repaint();
      }

    }
  }

  private void lookupStages() {
    try {
      AWTEvent event = EventQueue.getCurrentEvent();
      if (event instanceof InvocationEvent) {
        ToolkitFXService toolkitFXService = Services.getToolkitFXService();

        if (toolkitFXService == null) {
          return;
        }

        try {
          Field f = event.getClass().getDeclaredField("runnable");
          f.setAccessible(true);
          Runnable runnable = (Runnable) f.get(event);
          Field arg = runnable.getClass().getDeclaredField("arg$1");
          arg.setAccessible(true);

          Object sceneState = arg.get(runnable);
          Method getScene =
              Class.forName("com.sun.javafx.tk.quantum.SceneState").getDeclaredMethod("getScene");
          getScene.setAccessible(true);

          Object glassScene = getScene.invoke(sceneState);
          Method getStage =
              Class.forName("com.sun.javafx.tk.quantum.GlassScene").getDeclaredMethod("getStage");
          getStage.setAccessible(true);

          Object glassStage = getStage.invoke(glassScene);
          Field fxStage =
              Class.forName("com.sun.javafx.tk.quantum.WindowStage").getDeclaredField("fxStage");
          fxStage.setAccessible(true);

          Stage stage = (Stage) fxStage.get(glassStage);
          toolkitFXService.registerStage(stage);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException
            | IllegalAccessException | NoSuchMethodException | InvocationTargetException
            | ClassNotFoundException e) {
          AppLogger.warn("Exception while looking for Stages!", e);
        }
      }
    } catch (Exception e) {
      AppLogger.error("Exception while looking for Stages!", e);
    }
  }

  @Override
  protected boolean _enterFullscreen(long ptr, boolean animate, boolean keepRatio,
      boolean hideCursor) {
    return false;
  }

  @Override
  protected void _exitFullscreen(long ptr, boolean animate) {

  }

  @Override
  protected void notifyDragStart(int button, int x, int y, int xAbs, int yAbs) {
    super.notifyDragStart(button, x, y, xAbs, yAbs);
  }

  protected void setupWindow(WindowAdapter w, Rectangle contentBounds) {
    canvas.setSize(contentBounds.getSize());
    image = null;
    tmp = null;
    w.getContentPane().add(canvas);
    SwingUtilities.invokeLater(() -> canvas.requestFocus());
  }

}
