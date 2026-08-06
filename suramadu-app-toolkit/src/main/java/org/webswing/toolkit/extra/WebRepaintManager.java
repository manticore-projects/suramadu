package org.webswing.toolkit.extra;

import org.webswing.toolkit.util.Util;

import javax.swing.JComponent;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class WebRepaintManager extends RepaintManager {

  private RepaintManager delegate;
  private final Map<Container, Rectangle> dirty = new HashMap<Container, Rectangle>();

  public WebRepaintManager(RepaintManager delegate) {
    if (delegate != null) {
      this.delegate = delegate;
    }
  }

  public void setDelegate(RepaintManager delegate) {
    if (delegate != null) {
      this.delegate = delegate;
    }
  }

  @Override
  public void addDirtyRegion(JComponent c, int x, int y, int w, int h) {
    addDirtyRegionPrivate(c, x, y, w, h);
  }

  @Override
  public void addDirtyRegion(Window window, int x, int y, int w, int h) {
    addDirtyRegionPrivate(window, x, y, w, h);
  }

  private void addDirtyRegionPrivate(Container c, int x, int y, int w, int h) {
    /* Special cases we don't have to bother with. */
    if ((w <= 0) || (h <= 0) || (c == null)) {
      return;
    }

    if ((c.getWidth() <= 0) || (c.getHeight() <= 0)) {
      return;
    }

    synchronized (delegate) {
      Rectangle r = dirty.get(c);
      if (r != null) {
        SwingUtilities.computeUnion(x, y, w, h, r);
      } else {
        dirty.put(c, new Rectangle(x, y, w, h));
      }
    }
    Util.getWebToolkit().getPaintDispatcher().notifyNewDirtyRegionQueued();
  }

  @Override
  public Rectangle getDirtyRegion(JComponent aComponent) {
    Rectangle r;
    synchronized (delegate) {
      r = dirty.get(aComponent);
    }
    if (r == null) {
      return new Rectangle(0, 0, 0, 0);
    } else {
      return new Rectangle(r);
    }
  }

  @Override
  public void markCompletelyClean(JComponent component) {
    synchronized (delegate) {
      dirty.remove(component);
    }
  }

  public static void processDirtyComponents() {
    if (!SwingUtilities.isEventDispatchThread()) {
      SwingUtilities.invokeLater(() -> processDirtyComponents());
    } else {
      if (RepaintManager.currentManager(null) instanceof WebRepaintManager) {
        ((WebRepaintManager) RepaintManager.currentManager(null)).process();
      } else {
        RepaintManager newDelegate =
            Util.getWebToolkit().getPaintDispatcher().getDefaultRepaintManager();
        WebRepaintManager webRepaintManager = new WebRepaintManager(newDelegate);
        RepaintManager.setCurrentManager(webRepaintManager);
        for (Window w : Window.getWindows()) {
          if (w.isShowing()) {
            webRepaintManager.addDirtyRegion(w, w.getX(), w.getY(), w.getWidth(), w.getHeight());
          }
        }
        webRepaintManager.process();
      }
    }
  }

  public void process() {
    synchronized (delegate) {
      for (Container c : dirty.keySet()) {
        Rectangle r = dirty.get(c);
        if (c instanceof JComponent component) {
          Panel p = Util.findHwComponentParent(component);
          if (p != null) {
            for (Component chld : p.getComponents()) {
              delegate.addDirtyRegion((JComponent) chld, 0, 0, chld.getWidth(), chld.getHeight());
            }
          } else {
            delegate.addDirtyRegion(component, r.x, r.y, r.width, r.height);
          }
        } else if (c instanceof Window window) {
          delegate.addDirtyRegion(window, r.x, r.y, r.width, r.height);
        }
      }
      dirty.clear();
    }
  }

  @Override
  public void addInvalidComponent(JComponent invalidComponent) {
    delegate.addInvalidComponent(invalidComponent);
  }

  @Override
  public void removeInvalidComponent(JComponent component) {
    delegate.removeInvalidComponent(component);
  }

  /**
   * {@inheritDoc}
   */
  public Dimension getDoubleBufferMaximumSize() {
    return delegate.getDoubleBufferMaximumSize();
  }

  /**
   * {@inheritDoc}
   */
  public Image getOffscreenBuffer(Component c, int proposedWidth, int proposedHeight) {
    return delegate.getOffscreenBuffer(c, proposedWidth, proposedHeight);
  }

  /**
   * {@inheritDoc}
   */
  public Image getVolatileOffscreenBuffer(Component c, int proposedWidth, int proposedHeight) {
    return delegate.getVolatileOffscreenBuffer(c, proposedWidth, proposedHeight);
  }

  /**
   * {@inheritDoc}
   */
  public boolean isCompletelyDirty(JComponent component) {
    return delegate.isCompletelyDirty(component);
  }

  /**
   * {@inheritDoc}
   */
  public boolean isDoubleBufferingEnabled() {
    return delegate.isDoubleBufferingEnabled();
  }

  /**
   * {@inheritDoc}
   */
  public void markCompletelyDirty(JComponent component) {
    delegate.markCompletelyDirty(component);
  }

  /**
   * {@inheritDoc}
   */
  public void paintDirtyRegions() {
    delegate.paintDirtyRegions();
  }

  /**
   * {@inheritDoc}
   */
  public void setDoubleBufferingEnabled(boolean flag) {
    delegate.setDoubleBufferingEnabled(flag);
  }

  /**
   * {@inheritDoc}
   */
  public void setDoubleBufferMaximumSize(Dimension d) {
    delegate.setDoubleBufferMaximumSize(d);
  }

  /**
   * {@inheritDoc}
   */
  public void validateInvalidComponents() {
    delegate.validateInvalidComponents();
  }

}
