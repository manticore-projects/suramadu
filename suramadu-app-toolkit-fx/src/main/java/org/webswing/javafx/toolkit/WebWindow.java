package org.webswing.javafx.toolkit;

import com.sun.glass.ui.Cursor;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.Screen;
import com.sun.glass.ui.View;
import com.sun.glass.ui.Window;
import javafx.stage.Modality;
import org.webswing.javafx.toolkit.adaper.JDialogAdapter;
import org.webswing.javafx.toolkit.adaper.JFrameAdapter;
import org.webswing.javafx.toolkit.adaper.JWindowAdapter;
import org.webswing.javafx.toolkit.adaper.WindowAdapter;
import org.webswing.javafx.toolkit.util.WebFxUtil;
import org.webswing.util.AppLogger;

import javax.swing.SwingUtilities;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vikto on 01-Mar-17.
 */
public class WebWindow extends Window {

  private static final Map<Long, WebWindow> windowRegister = new HashMap<>();

  WindowAdapter w;
  private java.awt.Cursor dragCursor;
  private java.awt.Cursor originalCursor;

  WebWindow(Window owner, Screen screen, int styleMask) {
    super(owner, screen, styleMask);
  }

  public static WebWindow find(java.awt.Window w) {
    if (w != null) {
      int id = System.identityHashCode(w);
      return windowRegister.get((long) id);
    }
    return null;
  }

  @Override
  protected long _createWindow(long ownerPtr, long screenPtr, int mask) {
    WindowAdapter parent =
        windowRegister.containsKey(ownerPtr) ? windowRegister.get(ownerPtr).w : null;

    boolean titled = (mask & Window.TITLED) != 0;
    boolean utility = (mask & Window.UTILITY) != 0;
    boolean popup = (mask & Window.POPUP) != 0;
    boolean minimizable = (mask & Window.MINIMIZABLE) != 0;
    boolean maximizable = (mask & Window.MAXIMIZABLE) != 0;
    boolean probablyDialog = !popup && !utility && !minimizable && !maximizable && parent != null;

    if (utility || probablyDialog) {
      w = new JDialogAdapter((java.awt.Window) parent, titled);
      tryResolveModality();
    } else if (popup) {
      w = new JWindowAdapter((java.awt.Window) parent);
    } else {
      w = new JFrameAdapter(titled);
    }

    w.addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent componentEvent) {
        notifyResize(com.sun.glass.events.WindowEvent.RESIZE, getContentBounds().width,
            getContentBounds().height);
        if (getView() != null && getView() instanceof WebFxView wv) {
          wv.canvas.setSize(getContentBounds().getSize());
        }
      }

      @Override
      public void componentMoved(ComponentEvent componentEvent) {
        notifyMove(getContentBounds().x, getContentBounds().y);
      }
    });

    w.addWindowListener(new java.awt.event.WindowAdapter() {
      @Override
      public void windowIconified(WindowEvent windowEvent) {
        notifyResize(com.sun.glass.events.WindowEvent.MINIMIZE, getContentBounds().width,
            getContentBounds().height);
      }

      @Override
      public void windowDeiconified(WindowEvent windowEvent) {
        notifyResize(com.sun.glass.events.WindowEvent.RESTORE, getContentBounds().width,
            getContentBounds().height);
      }

      @Override
      public void windowClosing(WindowEvent windowEvent) {
        notifyClose();
      }

      @Override
      public void windowClosed(WindowEvent windowEvent) {
        close();
        notifyDestroy();
      }
    });

    w.addWindowFocusListener(new java.awt.event.WindowAdapter() {
      @Override
      public void windowGainedFocus(WindowEvent windowEvent) {
        notifyFocus(com.sun.glass.events.WindowEvent.FOCUS_GAINED);
        if (getWebView() != null) {
          getWebView().canvas.requestFocusInWindow();
        }
      }

      @Override
      public void windowLostFocus(WindowEvent windowEvent) {
        notifyFocus(com.sun.glass.events.WindowEvent.FOCUS_LOST);
      }
    });

    int id = System.identityHashCode(w);
    windowRegister.put((long) id, this);
    return id;
  }

  private void tryResolveModality() {
    SwingUtilities.invokeLater(() -> {
      try {
        Class<?> windowStageClass = Class.forName("com.sun.javafx.tk.quantum.WindowStage");
        Method findWindowStageMethod =
            windowStageClass.getDeclaredMethod("findWindowStage", Window.class);
        findWindowStageMethod.setAccessible(true);
        Object windowStage = findWindowStageMethod.invoke(null, WebWindow.this);
        if (windowStage != null) {
          Field modalityField = windowStageClass.getDeclaredField("modality");
          modalityField.setAccessible(true);
          Modality modality = (Modality) modalityField.get(windowStage);
          if (modality != null) {
            if (w instanceof JDialogAdapter adapter) {
              adapter.setModalityType(
                  modality == Modality.APPLICATION_MODAL ? ModalityType.APPLICATION_MODAL
                      : ModalityType.MODELESS);
            }
          }
        }
      } catch (Exception e) {
        // ignore
        AppLogger.warn("Could not resolve modality for WebWindow.");
      }
    });
  }

  private WebFxView getWebView() {
    return (WebFxView) getView();
  }

  @Override
  protected long _createChildWindow(long parent) {
    return _createWindow(parent, 1, Window.TITLED);
  }

  @Override
  protected boolean _close(long ptr) {
    if (w != null) {
      if (w.isShowing()) {
        Toolkit.getDefaultToolkit().getSystemEventQueue()
            .postEvent(new WindowEvent(w.getThis(), WindowEvent.WINDOW_CLOSING));
      }
      windowRegister.remove((long) System.identityHashCode(w));
      w.dispose();
      w = null;
    }
    return true;
  }

  @Override
  protected boolean _setView(long ptr, View view) {
    w.getContentPane().removeAll();
    if (view instanceof WebFxView wv) {
      wv.setupWindow(w, getContentBounds());
    }
    return true;
  }

  @Override
  protected boolean _setMenubar(long ptr, long menubarPtr) {
    return false;
  }

  @Override
  protected boolean _minimize(long ptr, boolean minimize) {
    return false;
  }

  @Override
  protected boolean _maximize(long ptr, boolean maximize, boolean wasMaximized) {
    return false;
  }

  @Override
  protected int _getEmbeddedX(long ptr) {
    return getContentBounds().x;
  }

  @Override
  protected int _getEmbeddedY(long ptr) {
    return getContentBounds().y;
  }

  @Override
  protected void _setBounds(long ptr, int x, int y, boolean xSet, boolean ySet, int w, int h,
      int cw, int ch, float xGravity, float yGravity) {
    if (this.w != null) {
      Rectangle rect = getContentBounds();
      if (xSet) {
        rect.x = x;
      }
      if (ySet) {
        rect.y = y;
      }
      boolean hSet = false;
      boolean wSet = false;
      if (w != -1) {
        wSet = true;
        rect.width = w;
      } else {
        if (cw != -1) {
          wSet = true;
          rect.width = cw;
        }
      }
      if (h != -1) {
        hSet = true;
        rect.height = h;
      } else {
        if (ch != -1) {
          hSet = true;
          rect.height = ch;
        }
      }
      Insets i = this.w.getInsets();
      if (wSet || hSet) {
        this.w.setBounds(rect.x - i.left, rect.y - i.top, rect.width + i.left + i.right,
            rect.height + i.top + i.bottom);
        notifyResize(com.sun.glass.events.WindowEvent.RESTORE, getContentBounds().width,
            getContentBounds().height);
      } else {
        this.w.setLocation(rect.x - i.left, rect.y - i.top);
      }
      notifyMove(getContentBounds().x, getContentBounds().y);
    }
  }

  @Override
  protected boolean _setVisible(long ptr, boolean visible) {
    w.setVisible(visible);
    Rectangle b = getContentBounds();
    notifyMove(b.x, b.y);
    notifyResize(com.sun.glass.events.WindowEvent.RESTORE, b.width, b.height);
    return true;
  }

  @Override
  protected boolean _setResizable(long ptr, boolean resizable) {
    w.setResizable(resizable);
    return true;
  }

  @Override
  protected boolean _requestFocus(long ptr, int event) {
    w.requestFocus();
    return true;
  }

  @Override
  protected void _setFocusable(long ptr, boolean isFocusable) {
    w.setFocusable(isFocusable);
  }

  @Override
  protected boolean _grabFocus(long ptr) {
    return false;
  }

  @Override
  protected void _ungrabFocus(long ptr) {

  }

  @Override
  protected boolean _setTitle(long ptr, String title) {
    w.setTitle(title);
    return true;
  }

  @Override
  protected void _setLevel(long ptr, int level) {

  }

  @Override
  protected void _setAlpha(long ptr, float alpha) {

  }

  @Override
  protected boolean _setBackground(long ptr, float r, float g, float b) {
    return false;
  }

  @Override
  protected void _setEnabled(long ptr, boolean enabled) {
    w.setEnabled(enabled);
  }

  @Override
  protected boolean _setMinimumSize(long ptr, int width, int height) {
    w.setMinimumSize(new Dimension(width, height));
    return true;
  }

  @Override
  protected boolean _setMaximumSize(long ptr, int width, int height) {
    w.setMaximumSize(new Dimension(width, height));
    return true;
  }

  @Override
  protected void _setIcon(long ptr, Pixels pixels) {
    w.setIconImages(Collections.singletonList(WebFxUtil.pixelsToImage(null, pixels)));
  }

  protected void setDragCursor(java.awt.Cursor c) {
    if (c != null) {
      if (this.dragCursor == null) {
        this.originalCursor = w.getContentPane().getCursor();
      }
      this.dragCursor = c;
      w.getContentPane().setCursor(c);
    } else {
      this.dragCursor = null;
      w.getContentPane().setCursor(this.originalCursor);
      this.originalCursor = null;
    }
  }

  @Override
  protected void _setCursor(long ptr, Cursor cursor) {
    if (cursor instanceof WebFxCursor wfxCursor) {
      if (dragCursor != null) {
        this.originalCursor = wfxCursor.c;
      } else {
        w.getContentPane().setCursor(wfxCursor.c);
      }
    }
  }

  @Override
  protected void _toFront(long ptr) {
    w.toFront();
  }

  @Override
  protected void _toBack(long ptr) {
    w.toBack();
  }

  @Override
  protected void _enterModal(long ptr) {
    w.setModal(true);
  }

  @Override
  protected void _enterModalWithWindow(long dialog, long window) {
    w.setModal(true);
  }

  @Override
  protected void _exitModal(long ptr) {
    w.setModal(false);
  }

  @Override
  protected void _requestInput(long ptr, String text, int type, double width, double height,
      double Mxx, double Mxy, double Mxz, double Mxt, double Myx, double Myy, double Myz,
      double Myt, double Mzx, double Mzy, double Mzz, double Mzt) {

  }

  @Override
  protected void _releaseInput(long ptr) {

  }

  public Rectangle getContentBounds() {
    Rectangle bounds = w.getBounds();
    Insets insets = w.getInsets();
    return new Rectangle(bounds.x + insets.left, bounds.y + insets.top,
        bounds.width - insets.right - insets.left, bounds.height - insets.top - insets.bottom);
  }
}
