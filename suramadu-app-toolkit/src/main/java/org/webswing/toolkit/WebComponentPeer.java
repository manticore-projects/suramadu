package org.webswing.toolkit;

import org.webswing.common.GraphicsWrapper;
import org.webswing.common.WindowActionType;
import org.webswing.dispatch.WebPaintDispatcher;
import org.webswing.model.appframe.out.CursorChangeEventMsgOut;
import org.webswing.toolkit.extra.DndEventHandler;
import org.webswing.toolkit.extra.WindowManager;
import org.webswing.toolkit.ge.WebGraphicsConfig;
import org.webswing.toolkit.util.DummyGraphics2D;
import org.webswing.toolkit.util.Services;
import org.webswing.toolkit.util.Util;
import org.webswing.util.AppLogger;
import sun.awt.AWTAccessor;
import sun.awt.PaintEventDispatcher;
import sun.awt.RepaintArea;
import sun.awt.image.OffScreenImage;
import sun.awt.image.SunVolatileImage;
import sun.awt.image.SurfaceManager;
import sun.awt.image.ToolkitImage;
import sun.java2d.InvalidPipeException;
import sun.java2d.SunGraphics2D;
import sun.java2d.SurfaceData;
import sun.java2d.pipe.Region;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import java.awt.*;
import java.awt.BufferCapabilities.FlipContents;
import java.awt.event.ComponentEvent;
import java.awt.event.PaintEvent;
import java.awt.image.*;
import java.awt.peer.ComponentPeer;
import java.awt.peer.ContainerPeer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.UUID;

@SuppressWarnings("restriction")
abstract public class WebComponentPeer implements ComponentPeer {

  private String guid = UUID.randomUUID().toString();

  public String getGuid() {
    return guid;
  }

  public BufferedImage extractBufferedImage(Rectangle sub) {
    BufferedImage safeImage = new BufferedImage(sub.width, sub.height, BufferedImage.TYPE_INT_ARGB);
    if (isInitialized()) {
      Graphics2D g = (Graphics2D) safeImage.getGraphics();
      g.drawImage(image, 0, 0, sub.width, sub.height, sub.x, sub.y, sub.x + sub.width,
          sub.y + sub.height, null);
      for (WebComponentPeer wcp : hwLayers) {
        Insets i = ((Window) this.getTarget()).getInsets();
        Rectangle b = wcp.getBounds();
        Rectangle bt = new Rectangle(b.x + i.left, b.y + i.top, b.width, b.height);
        if (bt.intersects(sub)) {
          Rectangle dst = sub.intersection(bt);
          Rectangle src = new Rectangle(dst);
          dst.translate(-sub.x, -sub.y);
          src.translate(-bt.x, -bt.y);
          g.drawImage(wcp.image, dst.x, dst.y, dst.width + dst.x, dst.height + dst.y, src.x, src.y,
              src.width + src.x, src.height + src.y, null);
        }
      }
      g.drawImage(windowDecorationImage, 0, 0, sub.width, sub.height, sub.x, sub.y,
          sub.x + sub.width, sub.y + sub.height, null);
      g.dispose();
    }
    return safeImage;
  }

  public Image extractWebImage() {
    Graphics g = webImage.getGraphics();
    if (windowDecorationImage != null
        && Services.getDirectDrawService().isDirty(windowDecorationImage)) {
      g.drawImage(windowDecorationImage, 0, 0, null);
      Services.getDirectDrawService().resetImage(windowDecorationImage);
    }
    for (WebComponentPeer wcp : hwLayers) {
      Point p = SwingUtilities.convertPoint((Component) wcp.getTarget(), new Point(0, 0),
          (Component) this.getTarget());
      g.drawImage(wcp.webImage, p.x, p.y, null);
      Services.getDirectDrawService().resetImageBeforeRepaint(wcp.webImage);
    }
    g.dispose();
    return Services.getDirectDrawService().extractWebImage(webImage);
  }

  public long getHWnd() {
    return 1;
  }

  public void updateWindowDecorationImage() {
    if (target == null || !(target instanceof JDialog || target instanceof JFrame)
        || !isInitialized()) {
      return;
    }

    if (isUndecorated()) {
      // window decoration is not painted
      if (windowDecorationImage != null) {
        if (Util.isDD()) {
          Services.getDirectDrawService().resetImage(windowDecorationImage);
        }
        windowDecorationImage = null;
      }
      return;
    }

    int w;
    int h;
    if (Util.isDD()) {
      w = webImage.getWidth(null);
      h = webImage.getHeight(null);
      windowDecorationImage = Services.getDirectDrawService().createImage(w, h);
    } else {
      w = image.getWidth();
      h = image.getHeight();
      windowDecorationImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    }
    Graphics g = windowDecorationImage.getGraphics();
    Services.getImageService().getWindowDecorationTheme().paintWindowDecoration(g, target, w, h);
    g.dispose();

    Util.getWebToolkit().getPaintDispatcher().notifyWindowDecorationUpdated(this.getGuid(),
        getBounds(), ((WebWindowPeer) this).getInsets());
  }

  private boolean isInitialized() {
    if (Util.isDD()) {
      return webImage != null;
    } else {
      return image != null;
    }
  }

  public Component getHwComponentAt(int x, int y) {
    Component result = (Component) getTarget();
    for (Iterator<WebComponentPeer> i = hwLayers.iterator(); i.hasNext();) {
      WebComponentPeer wcp = i.next();
      Insets insets = ((Window) this.getTarget()).getInsets();
      if (wcp.getBounds().contains(x - getBounds().x - insets.left,
          y - getBounds().y - insets.top)) {
        result = (Component) wcp.getTarget();
      }
    }
    return result;
  }

  protected boolean isUndecorated() {
    if (target == null) {
      return true;
    }

    if (target instanceof JWindow) {
      return true;
    }

    if ((target instanceof JFrame frame && frame.isUndecorated())
        || (target instanceof JDialog dialog && dialog.isUndecorated())) {
      return true;
    }

    if (this instanceof WebWindowPeer webPeer) {
      if (webPeer.getUndecoratedOverride() != null) {
        return webPeer.getUndecoratedOverride();
      }
    }

    return false;
  }

  // ////////////////////////////////////////////////////////////////////////////////////////////
  // /////////////////// WebComponentPeer
  // Implementation//////////////////////////////////////////////////

  protected long pData; // ???
  protected boolean destroyed;
  protected Object target;
  private RepaintArea paintArea;
  private int oldWidth;
  private int oldHeight;
  private int oldX;
  private int oldY;
  private SurfaceData surfaceData;
  OffScreenImage image;
  private Image windowDecorationImage;
  private LinkedList<WebComponentPeer> hwLayers = new LinkedList<WebComponentPeer>();
  Image webImage; // directdraw
  private Color background;
  private Color foreground;
  private Font font;
  protected float opacity;
  private String currentCursor = CursorChangeEventMsgOut.DEFAULT_CURSOR;


  public WebComponentPeer(Component t) {
    this.target = t;
    this.paintArea = new RepaintArea();
    WebToolkit.registerPeer(t, this);
    if (((Component) this.target).isVisible()) {
      show();
    }

    Color localColor = ((Component) this.target).getForeground();
    if (localColor != null) {
      setForeground(localColor);
    }

    Font localFont = ((Component) this.target).getFont();
    if (localFont != null) {
      setFont(localFont);
    }
    if (!((Component) this.target).isEnabled()) {
      disable();
    }
    Rectangle localRectangle = ((Component) this.target).getBounds();
    setBounds(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height, 3);

    Color background = ((Component) this.target).getBackground();
    if (background == null) {
      background = Color.WHITE;
      ((Component) this.target).setBackground(background);
      this.setBackground(background);
    }

    Color foreground = ((Component) this.target).getForeground();
    if (foreground == null) {
      foreground = Color.BLACK;
      ((Component) this.target).setForeground(foreground);
      this.setForeground(foreground);
    }

    // JDK 21: Component.getGraphicsConfiguration() reads the private graphicsConfig
    // field directly, bypassing the peer. Override it with WebGraphicsConfig so that
    // window positioning (setLocationRelativeTo etc.) uses virtual screen bounds.
    try {
      java.lang.reflect.Field gcField = Component.class.getDeclaredField("graphicsConfig");
      gcField.setAccessible(true);
      gcField.set(this.target, getGraphicsConfiguration());
    } catch (Exception e) {
      // non-fatal — window positioning may be off
    }

  }

  public boolean isObscured() {
    return false;
  }

  public boolean canDetermineObscurity() {
    return false;
  }

  public void setVisible(boolean paramBoolean) {
    if (paramBoolean) {
      show();
    } else {
      hide();
    }
  }

  public void setEnabled(boolean paramBoolean) {
    if (paramBoolean) {
      enable();
    } else {
      disable();
    }
  }

  public void paint(Graphics paramGraphics) {
    ((Component) this.target).paint(paramGraphics);
  }

  public void repaint(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    ((Component) this.target).repaint(paramLong, paramInt1, paramInt2, paramInt3, paramInt4);
  }

  public void print(Graphics paramGraphics) {
    ((Component) this.target).print(paramGraphics);
  }

  public void setBounds(int x, int y, int w, int h, int op) {
    synchronized (WebPaintDispatcher.webPaintLock) {
      Point validPosition = validate(x, y, w, h);
      boolean resized = (w != this.oldWidth) || (h != this.oldHeight);
      boolean moved = (validPosition.x != this.oldX) || (validPosition.y != this.oldY);
      if (resized) {
        ComponentEvent e = new ComponentEvent((Component) target, ComponentEvent.COMPONENT_RESIZED);
        postEvent(e);
      }
      if (moved) {
        ComponentEvent e = new ComponentEvent((Component) target, ComponentEvent.COMPONENT_MOVED);
        postEvent(e);
      }

      if ((w != this.oldWidth) || (h != this.oldHeight)) {
        try {
          replaceSurfaceData(validPosition.x, validPosition.y, w, h);
        } catch (InvalidPipeException e) {
          AppLogger.error("WebComponentPeer:setBounds", e);
        }
        notifyWindowBoundsChanged(new Rectangle(0, 0, w, h));
      }

      if ((validPosition.x != this.oldX) || (validPosition.y != this.oldY) || (w != this.oldWidth)
          || (h != this.oldHeight)) {
        if (oldWidth != 0 && oldHeight != 0 && target instanceof Window window) {
          Util.getWebToolkit().getWindowManager().requestRepaintAfterMove(window,
              new Rectangle(oldX, oldY, oldWidth, oldHeight));
        }
        this.oldX = validPosition.x;
        this.oldY = validPosition.y;
        this.oldWidth = w;
        this.oldHeight = h;
      }

    }
  }

  protected void notifyWindowBoundsChanged(Rectangle rectangle) {
    Util.getWebToolkit().getPaintDispatcher().notifyWindowBoundsChanged(getGuid(), rectangle);
  }

  protected Point validate(int x, int y, int w, int h) {
    // overriden in WebWindowPeer
    return new Point(x, y);
  }

  public void replaceSurfaceData(int x, int y, int w, int h) {
    SurfaceData localSurfaceData = null;
    synchronized (((Component) this.target).getTreeLock()) {
      synchronized (this) {
        if (Util.isDD()) {
          this.webImage = Services.getDirectDrawService().createImage(w, h);
        } else {
          if (w > 0 && h > 0) {
            WebGraphicsConfig localWebGraphicsConfig =
                (WebGraphicsConfig) getGraphicsConfiguration();
            this.image = (OffScreenImage) localWebGraphicsConfig
                .createAcceleratedImage((Component) this.target, w, h);
            localSurfaceData = this.surfaceData;
            this.surfaceData = Util.getWebToolkit()
                .webComponentPeerReplaceSurfaceData(SurfaceManager.getManager(this.image)); // java6
                                                                                            // vs
                                                                                            // java7
                                                                                            // difference
          } else {
            this.image = null;
            this.surfaceData = null;
          }

          if (localSurfaceData != null) {
            localSurfaceData.invalidate();
          }
        }
        updateWindowDecorationImage();
        repaintPeerTarget();
      }
    }
    if (localSurfaceData != null) {
      localSurfaceData.flush();
      localSurfaceData = null;
    }
  }

  public void handleEvent(AWTEvent paramAWTEvent) {}

  public void coalescePaintEvent(PaintEvent paramPaintEvent) {
    Rectangle localRectangle = paramPaintEvent.getUpdateRect();
    this.paintArea.add(localRectangle, paramPaintEvent.getID());
  }

  private void repaintPeerTarget() {
    Component localComponent = (Component) this.target;
    Rectangle localRectangle = AWTAccessor.getComponentAccessor().getBounds(localComponent);
    if (!((Component) this.target).getIgnoreRepaint()) {
      ((Component) this.target).invalidate();
      ((Component) this.target).validate();
      PaintEvent localPaintEvent = PaintEventDispatcher.getPaintEventDispatcher().createPaintEvent(
          (Component) this.target, 0, 0, localRectangle.width, localRectangle.height);
      if (localPaintEvent != null) {
        postEvent(localPaintEvent);
      }
    }
  }

  public Point getLocationOnScreen() {
    if (getTarget() instanceof Window) {
      return getBounds().getLocation();
    } else {
      Point p = new Point(0, 0);
      SwingUtilities.convertPointToScreen(p, (Component) getTarget());
      return p;
    }
  }

  public Dimension getPreferredSize() {
    return getMinimumSize();
  }

  public Dimension getMinimumSize() {
    return ((Component) this.target).getSize();
  }

  public ColorModel getColorModel() {
    GraphicsConfiguration localGraphicsConfiguration = getGraphicsConfiguration();
    if (localGraphicsConfiguration != null) {
      return localGraphicsConfiguration.getColorModel();
    }
    return null;
  }

  public Toolkit getToolkit() {
    return Toolkit.getDefaultToolkit();
  }

  public Graphics getGraphics() {
    Color background = this.background;
    if (background == null) {
      background = SystemColor.window;
    }
    Color foreground = this.foreground;
    if (foreground == null) {
      foreground = SystemColor.windowText;
    }
    Font font = this.font;
    if (font == null) {
      font = WebToolkit.defaultFont;
    }

    Graphics2D result = null;
    if (Util.isDD()) {
      Image wi = this.webImage;
      if (wi != null) {
        result = new GraphicsWrapper((Graphics2D) wi.getGraphics(), this);
        result.setBackground(background);
        result.setColor(foreground);
        result.setFont(font);
      }
    } else {
      SurfaceData surface = this.surfaceData;
      if (surface != null) {
        result =
            new GraphicsWrapper(new SunGraphics2D(surface, foreground, background, font), this);
      } else {
        return new DummyGraphics2D();
      }
    }
    return result;
  }

  public FontMetrics getFontMetrics(Font paramFont) {
    return null;
  }

  public void dispose() {
    synchronized (WebPaintDispatcher.webPaintLock) {
      notifyWindowClosed();
      if (Util.isDD()) {
        this.webImage = null;
      } else {
        SurfaceData localSurfaceData = this.surfaceData;
        this.surfaceData = null;
        if (localSurfaceData != null) {
          localSurfaceData.invalidate();
        }
      }
      WebToolkit.targetDisposedPeer(this.target, this);
    }
  }

  protected void notifyWindowClosed() {
    Util.getWebToolkit().getPaintDispatcher().notifyWindowClosed(getGuid());
  }

  public void setForeground(Color paramColor) {
    this.foreground = paramColor;
  }

  public void setBackground(Color paramColor) {
    this.background = paramColor;

  }

  public void setFont(Font paramFont) {
    this.font = paramFont;
  }

  public void updateCursorImmediately() {
    if (Util.getWebToolkit().getEventDispatcher().isDndInProgress()) {
      notifyCursorUpdate(null, DndEventHandler.getCurrentDropTargetCursorName());
    } else {
      Point location = Util.getWebToolkit().getEventDispatcher().getLastMousePosition();
      if (location != null && target instanceof Window window) {
        Window windowOnLocation =
            WindowManager.getInstance().getVisibleWindowOnPosition(location.x, location.y);
        if (window == windowOnLocation || Util.isCompositingWM()) {
          if (WindowManager.getInstance().isBlockedByModality(window, false)) {
            notifyCursorUpdate(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR), null);
          } else if (Util.isWindowDecorationPosition(window, location)) {
            WindowActionType wat = Services.getImageService().getWindowDecorationTheme().getAction(
                window, new Point(location.x - window.getX(), location.y - window.getY()));
            notifyCursorUpdate(wat.getCursor(), null);
          } else {
            Component component = SwingUtilities.getDeepestComponentAt(window,
                location.x - window.getX(), location.y - window.getY());
            component = component == null ? window : component;
            notifyCursorUpdate(component.getCursor(), null);
          }
        }
      }
    }
  }

  private void notifyCursorUpdate(Cursor c, Cursor overridenCursor) {
    Util.getWebToolkit().getPaintDispatcher().notifyCursorUpdate(c, overridenCursor,
        this.getGuid());
  }

  public boolean isFocusable() {
    return false;
  }

  public Image createImage(ImageProducer paramImageProducer) {
    return new ToolkitImage(paramImageProducer);
  }

  public Image createImage(int paramInt1, int paramInt2) {
    WebGraphicsConfig localwebGraphicsConfig = (WebGraphicsConfig) getGraphicsConfiguration();
    return localwebGraphicsConfig.createAcceleratedImage((Component) this.target, paramInt1,
        paramInt2);
  }

  public VolatileImage createVolatileImage(int paramInt1, int paramInt2) {
    return new SunVolatileImage((Component) this.target, paramInt1, paramInt2);
  }

  public boolean prepareImage(Image paramImage, int paramInt1, int paramInt2,
      ImageObserver paramImageObserver) {
    return getToolkit().prepareImage(paramImage, paramInt1, paramInt2, paramImageObserver);
  }

  public int checkImage(Image paramImage, int paramInt1, int paramInt2,
      ImageObserver paramImageObserver) {
    return getToolkit().checkImage(paramImage, paramInt1, paramInt2, paramImageObserver);
  }

  public GraphicsConfiguration getGraphicsConfiguration() {
    return ((WebToolkit) getToolkit()).getGraphicsConfig();
  }

  public boolean handlesWheelScrolling() {
    return false;
  }

  public void createBuffers(int paramInt, BufferCapabilities paramBufferCapabilities)
      throws AWTException {
    throw new AWTException("The operation requested is not supported");
  }

  public Image getBackBuffer() {
    return null;
  }

  public void flip(int paramInt1, int paramInt2, int paramInt3, int paramInt4,
      FlipContents paramFlipContents) {}

  public void destroyBuffers() {}

  public void reparent(ContainerPeer paramContainerPeer) {}

  public boolean isReparentSupported() {
    return false;
  }

  public void layout() {}

  public Rectangle getBounds() {
    return ((Component) this.target).getBounds();
  }

  public void applyShape(Region paramRegion) {}

  public Dimension preferredSize() {
    return getPreferredSize();
  }

  public Dimension minimumSize() {
    return getMinimumSize();
  }

  public void show() {}

  public void hide() {}

  public void enable() {}

  public void disable() {}

  public void reshape(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {}

  void postEvent(AWTEvent paramAWTEvent) {
    WebToolkit.postEvent(WebToolkit.targetToAppContext(this.target), paramAWTEvent);
  }

  public Object getTarget() {
    return target;
  }

  public void setZOrder(ComponentPeer above) {
    // do nothing
  }

  public boolean updateGraphicsData(GraphicsConfiguration gc) {
    return Util.getWebToolkit().webConpoenentPeerUpdateGraphicsData();
  }

  protected void removeHwLayer(WebComponentPeer peer) {
    synchronized (WebPaintDispatcher.webPaintLock) {
      hwLayers.remove(peer);
    }
  }

  protected void addHwLayer(WebComponentPeer peer) {
    synchronized (WebPaintDispatcher.webPaintLock) {
      hwLayers.add(peer);
    }
  }

  public String getCurrentCursor() {
    return currentCursor;
  }

  public void setCurrentCursor(String currentCursor) {
    this.currentCursor = currentCursor;
  }
}
