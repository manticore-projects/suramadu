package org.webswing.toolkit.util;

import org.webswing.Constants;
import org.webswing.component.HtmlPanelImpl;
import org.webswing.dispatch.WebPaintDispatcher;
import org.webswing.ext.services.ToolkitFXService;
import org.webswing.model.appframe.in.KeyboardEventMsgIn;
import org.webswing.model.appframe.in.KeyboardEventMsgIn.KeyEventType;
import org.webswing.model.appframe.in.MouseEventMsgIn;
import org.webswing.model.appframe.out.AppFrameMsgOut;
import org.webswing.model.appframe.out.FileDialogEventMsgOut.FileDialogEventType;
import org.webswing.model.appframe.out.WindowMsgOut;
import org.webswing.model.appframe.out.WindowMsgOut.DockMode;
import org.webswing.model.appframe.out.WindowMsgOut.WindowClassType;
import org.webswing.model.appframe.out.WindowMsgOut.WindowType;
import org.webswing.model.appframe.out.WindowPartialContentMsgOut;
import org.webswing.model.appframe.out.WindowSwitchMsgOut;
import org.webswing.toolkit.WebComponentPeer;
import org.webswing.toolkit.WebToolkit;
import org.webswing.toolkit.WebWindowPeer;
import org.webswing.toolkit.api.component.Dockable;
import org.webswing.toolkit.api.component.HtmlPanel;
import org.webswing.toolkit.api.file.WebswingFileChooserUtil;
import org.webswing.util.AppLogger;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.WritableRaster;
import java.io.*;
import java.lang.management.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class Util {

  public static byte[] convertCursor(BufferedImage img, int x, int y) {
    try {
      return convertToIco(img, x, y);
    } catch (Exception e) {
      AppLogger.error("Failed to convert cursor.", e);
    }
    return null;
  }

  private static byte[] convertToIco(BufferedImage img, int hotspotx, int hotspoty)
      throws IOException {
    byte[] imgBytes = Services.getImageService().getPngImage(img);

    ByteBuffer bytes = ByteBuffer.allocate(imgBytes.length + 22);
    bytes.order(ByteOrder.LITTLE_ENDIAN);

    bytes.putShort((short) 0);
    bytes.putShort((short) 1);
    bytes.putShort((short) 1);
    bytes.put((byte) img.getWidth());
    bytes.put((byte) img.getHeight()); // no need to multiply
    bytes.put((byte) img.getColorModel().getNumColorComponents()); // the pallet size
    bytes.put((byte) 0);
    bytes.putShort((short) hotspotx);
    bytes.putShort((short) hotspoty);
    bytes.putInt(imgBytes.length);
    bytes.putInt(22);
    bytes.put(imgBytes);
    return bytes.array();

  }

  private static final List<Integer> NO_CHAR_KEY_CODES =
      Arrays.asList(KeyEvent.VK_F1, KeyEvent.VK_F2, KeyEvent.VK_F3, KeyEvent.VK_F4, KeyEvent.VK_F5,
          KeyEvent.VK_F6, KeyEvent.VK_F7, KeyEvent.VK_F8, KeyEvent.VK_F9, KeyEvent.VK_F10,
          KeyEvent.VK_F11, KeyEvent.VK_F12, KeyEvent.VK_PRINTSCREEN, KeyEvent.VK_SCROLL_LOCK,
          KeyEvent.VK_PAUSE, KeyEvent.VK_INSERT, KeyEvent.VK_HOME, KeyEvent.VK_PAGE_DOWN,
          KeyEvent.VK_END, KeyEvent.VK_PAGE_DOWN, KeyEvent.VK_CAPS_LOCK, KeyEvent.VK_UP,
          KeyEvent.VK_LEFT, KeyEvent.VK_DOWN, KeyEvent.VK_RIGHT, KeyEvent.VK_SHIFT,
          KeyEvent.VK_CONTROL, KeyEvent.VK_ALT, KeyEvent.VK_WINDOWS, KeyEvent.VK_ALT_GRAPH);
  private static final Map<Integer, Character> CONTROL_MAP;

  static {
    CONTROL_MAP = new HashMap<Integer, Character>();
    CONTROL_MAP.put(81, '\u0011');
    CONTROL_MAP.put(69, '\u0005');
    CONTROL_MAP.put(82, '\u0012');
    CONTROL_MAP.put(89, '\u0019');
    CONTROL_MAP.put(85, '\u0015');
    CONTROL_MAP.put(73, '\u0009');
    CONTROL_MAP.put(79, '\u000F');
    CONTROL_MAP.put(80, '\u0010');
    CONTROL_MAP.put(65, '\u0001');
    CONTROL_MAP.put(83, '\u0013');
    CONTROL_MAP.put(68, '\u0004');
    CONTROL_MAP.put(71, '\u0007');
    CONTROL_MAP.put(72, '\u0008');
    CONTROL_MAP.put(74, '\n');
    CONTROL_MAP.put(76, '\u000C');
    CONTROL_MAP.put(90, '\u001A');
    CONTROL_MAP.put(88, '\u0018');
    CONTROL_MAP.put(67, '\u0003');
    CONTROL_MAP.put(86, '\u0016');
    CONTROL_MAP.put(66, '\u0002');
    CONTROL_MAP.put(77, '\r');
  }

  static {
    Properties evalProps = new Properties();
    ClassLoader cl = Util.class.getClassLoader();
    if (cl == null) {
      cl = ClassLoader.getSystemClassLoader();
    }
    try (InputStream is = cl.getResourceAsStream("toolkit.properties")) {
      evalProps.load(is);
    } catch (Exception e) {
      // ignore this exception, the file is not present in non-EVALUATION version
    }
  }

  public static int getMouseButtonsAWTFlag(int button) {
    switch (button) {
      case 1:
        return MouseEvent.BUTTON1;
      case 2:
        return MouseEvent.BUTTON2;
      case 3:
        return MouseEvent.BUTTON3;
      case 0:
        return MouseEvent.NOBUTTON;
    }
    return 0;
  }

  public static int getMouseModifiersAWTFlag(MouseEventMsgIn evt) {
    int result = 0;
    if ((evt.getButtons() & 2) == 2) {
      result = result | MouseEvent.BUTTON1_DOWN_MASK;
    }
    if ((evt.getButtons() & 4) == 4) {
      result = result | MouseEvent.BUTTON2_DOWN_MASK;
    }
    if ((evt.getButtons() & 8) == 8) {
      result = result | MouseEvent.BUTTON3_DOWN_MASK;
    }
    if (evt.isCtrl()) {
      result = result | MouseEvent.CTRL_DOWN_MASK;
    }
    if (evt.isAlt()) {
      result = result | MouseEvent.ALT_DOWN_MASK;
    }
    if (evt.isShift()) {
      result = result | MouseEvent.SHIFT_DOWN_MASK;
    }
    if (evt.isMeta()) {
      result = result | MouseEvent.META_DOWN_MASK;
    }
    return result;
  }

  public static void savePngImage(BufferedImage imageContent, String name) {
    try {
      OutputStream os = new FileOutputStream(name);
      ImageOutputStream ios = ImageIO.createImageOutputStream(os);
      ImageIO.write(imageContent, "png", ios);
      ios.close();
      os.close();
    } catch (IOException e) {
      AppLogger.error("Util:savePngImage", e);
    }
  }

  public static int getKeyModifiersAWTFlag(KeyboardEventMsgIn event) {
    int modifiers = 0;
    if (event.isAlt()) {
      modifiers = modifiers | KeyEvent.ALT_MASK;
    }
    if (event.isCtrl()) {
      modifiers = modifiers | KeyEvent.CTRL_MASK;
    }
    if (event.isShift()) {
      modifiers = modifiers | KeyEvent.SHIFT_MASK;
    }
    if (event.isAltgr()) {
      modifiers = modifiers | KeyEvent.ALT_GRAPH_MASK;
    }
    if (event.isMeta()) {
      modifiers = modifiers | KeyEvent.META_MASK;
    }
    if (Util.getKeyType(event.getType()) == KeyEvent.KEY_TYPED && event.isAlt() && event.isCtrl()
        && !event.isShift() && !event.isMeta()) {
      modifiers = KeyEvent.ALT_GRAPH_MASK;
    }

    return modifiers;
  }

  public static int getKeyType(KeyEventType type) {
    switch (type) {
      case keydown:
        return KeyEvent.KEY_PRESSED;
      case keypress:
        return KeyEvent.KEY_TYPED;
      case keyup:
        return KeyEvent.KEY_RELEASED;
    }
    return 0;
  }

  public static char getKeyCharacter(KeyboardEventMsgIn event) {
    if (NO_CHAR_KEY_CODES.contains(event.getKeycode())) {
      return KeyEvent.CHAR_UNDEFINED;
    } else if (event.isCtrl() && !event.isAlt() && !event.isMeta() && !event.isShift()
        && !event.isMeta() && CONTROL_MAP.containsKey(event.getKeycode())) {
      return CONTROL_MAP.get(event.getKeycode());
    } else {
      return (char) event.getCharacter();
    }
  }

  public static BufferedImage deepCopy(BufferedImage bi) {
    ColorModel cm = bi.getColorModel();
    boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
    WritableRaster raster = bi.copyData(null);
    return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
  }

  public static Rectangle[] concatRectangleArrays(Rectangle[] A, Rectangle[] B) {
    int aLen = A.length;
    int bLen = B.length;
    Rectangle[] C = new Rectangle[aLen + bLen];
    System.arraycopy(A, 0, C, 0, aLen);
    System.arraycopy(B, 0, C, aLen, bLen);
    return C;
  }

  public static WebToolkit getWebToolkit() {
    return (WebToolkit) Toolkit.getDefaultToolkit();
  }

  public static WebWindowPeer findWindowPeerById(String id) {
    for (Window w : Window.getWindows()) {
      Object peer = WebToolkit.targetToPeer(w);
      if (peer != null && peer instanceof WebWindowPeer windowPeer) {
        if (windowPeer.getGuid().equals(id)) {
          return windowPeer;
        }

      }
    }
    return null;
  }

  public static Window findWindowById(String id) {
    for (Window w : Window.getWindows()) {
      Object peer = WebToolkit.targetToPeer(w);
      if (peer != null && peer instanceof WebWindowPeer windowPeer) {
        if (windowPeer.getGuid().equals(id)) {
          return w;
        }

      }
    }
    return null;
  }

  public static HtmlPanel findHtmlPanelById(String id) {
    return getWebToolkit().getPaintDispatcher().findHtmlPanelById(id);
  }

  public static WebComponentPeer getPeerForTarget(Object paramObject) {
    WebComponentPeer localWObjectPeer = (WebComponentPeer) WebToolkit.targetToPeer(paramObject);
    return localWObjectPeer;
  }

  public static Window[] getAllWindows() {
    List<Window> windows = new ArrayList<>(Arrays.asList(Window.getWindows()));
    for (Iterator<Window> i = windows.iterator(); i.hasNext();) {
      Window w = i.next();
      if (w.getClass().getName().contains("JLightweightFrame")) {
        i.remove();
      }
    }
    return windows.toArray(new Window[windows.size()]);
  }

  public static Map<String, Map<Integer, BufferedImage>> extractWindowImages(AppFrameMsgOut json,
      Map<String, Map<Integer, BufferedImage>> windowImages) {
    for (WindowMsgOut window : json.getWindows()) {
      WebWindowPeer w = findWindowPeerById(window.getId());
      if (WebToolkit.BACKGROUND_WINDOW_ID.equals(window.getId())) {
        windowImages.put(window.getId(), new HashMap<Integer, BufferedImage>()); // background image
                                                                                 // is handled on
                                                                                 // client
      } else {
        Map<Integer, BufferedImage> imageMap = new HashMap<Integer, BufferedImage>();
        for (int i = 0; i < window.getContent().size(); i++) {
          WindowPartialContentMsgOut wpc = window.getContent().get(i);
          imageMap.put(i, w.extractBufferedImage(new Rectangle(wpc.getPositionX(),
              wpc.getPositionY(), wpc.getWidth(), wpc.getHeight())));
        }
        windowImages.put(window.getId(), imageMap);
      }
    }
    return windowImages;
  }

  public static Map<String, Image> extractWindowWebImages(Set<String> updatedWindows,
      Map<String, Image> webImages) {
    for (String windowId : updatedWindows) {
      if (WebToolkit.BACKGROUND_WINDOW_ID.equals(windowId)) {
        continue;
      }
      WebWindowPeer w = findWindowPeerById(windowId);
      if (w != null) {
        Image webimage = w.extractWebImage();
        webImages.put(windowId, webimage);
      }
    }
    return webImages;
  }

  public static void encodeWindowImages(Map<String, Map<Integer, BufferedImage>> windowImages,
      AppFrameMsgOut json) {
    for (WindowMsgOut window : json.getWindows()) {
      if (!WebToolkit.BACKGROUND_WINDOW_ID.equals(window.getId())) {
        Map<Integer, BufferedImage> imageMap = windowImages.get(window.getId());
        for (int i = 0; i < window.getContent().size(); i++) {
          WindowPartialContentMsgOut c = window.getContent().get(i);
          if (imageMap.containsKey(i)) {
            c.setBase64Content(Services.getImageService().getPngImage(imageMap.get(i)));
          }
        }
      }
    }
  }

  public static void encodeWindowWebImages(Map<String, Image> windowWebImages,
      AppFrameMsgOut json) {
    for (WindowMsgOut window : json.getWindows()) {
      Image wi = windowWebImages.get(window.getId());
      if (wi != null) {
        window.setDirectDraw(Services.getDirectDrawService().buildWebImage(wi));
      }
    }
  }

  @SuppressWarnings("restriction")
  public static AppFrameMsgOut fillWithWindowsData(
      Map<String, Set<Rectangle>> currentAreasToUpdate) {
    Map<String, List<Rectangle>> windowNonVisibleAreas =
        getWebToolkit().getWindowManager().extractNonVisibleAreas();

    AppFrameMsgOut json = new AppFrameMsgOut();
    for (String windowId : currentAreasToUpdate.keySet()) {
      WebWindowPeer ww = Util.findWindowPeerById(windowId);
      if (ww != null || WebToolkit.BACKGROUND_WINDOW_ID.equals(windowId)) {
        WindowMsgOut window = json.getOrCreateWindowById(windowId);
        if (WebToolkit.BACKGROUND_WINDOW_ID.equals(windowId)) {
          window.setPosX(0);
          window.setPosY(0);
          window.setWidth(getWebToolkit().getScreenSize().width);
          window.setHeight(getWebToolkit().getScreenSize().height);
        } else {
          Point location = ww.getLocationOnScreen();
          window.setPosX(location.x);
          window.setPosY(location.y);
          window.setWidth(ww.getBounds().width);
          window.setHeight(ww.getBounds().height);
          if (ww.getTarget() instanceof Frame) {
            window.setTitle(((Frame) ww.getTarget()).getTitle());
          }
          if (ww.getTarget() instanceof Component) {
            window.setName(((Component) ww.getTarget()).getName());
          }
        }
        List<Rectangle> toPaint =
            joinRectangles(getGrid(new ArrayList<Rectangle>(currentAreasToUpdate.get(windowId)),
                windowNonVisibleAreas.get(windowId)));
        createPartialContentMsgs(window, toPaint);
      }
    }
    return json;
  }

  public static AppFrameMsgOut fillWithCompositingWindowsData(
      Map<String, Set<Rectangle>> currentAreasToUpdate) {
    AppFrameMsgOut frame = new AppFrameMsgOut();
    List<String> zOrder = getWebToolkit().getWindowManager().getZOrder();
    Map<Window, List<Container>> webContainers =
        getWebToolkit().getPaintDispatcher().getRegisteredWebContainersAsMap();
    Map<Window, List<HtmlPanel>> htmlPanels =
        getWebToolkit().getPaintDispatcher().getRegisteredHtmlPanelsAsMap();
    Map<Container, Map<JComponent, List<WindowMsgOut>>> htmlWebComponentsMap = new HashMap<>();

    for (String windowId : zOrder) {
      WebWindowPeer ww = findWindowPeerById(windowId);
      if (ww == null) {
        continue;
      }

      WindowMsgOut window = new WindowMsgOut();
      window.setId(windowId);

      Point location = ww.getLocationOnScreen();
      window.setBounds(location.x, location.y, ww.getBounds().width, ww.getBounds().height);
      if (ww.getTarget() instanceof Frame) {
        window.setTitle(((Frame) ww.getTarget()).getTitle());
        window.setState(((Frame) ww.getTarget()).getExtendedState());
      }
      if (ww.getTarget() instanceof Dialog) {
        window.setTitle(((Dialog) ww.getTarget()).getTitle());
      }
      if (ww.getTarget() instanceof Component) {
        window.setName(((Component) ww.getTarget()).getName());
      }

      fillClassType(ww.getTarget(), window);
      fillDockMode(ww.getTarget(), window);
      window.setDockState(
          ww.isUndocked() ? WindowMsgOut.DockState.undocked : WindowMsgOut.DockState.docked);

      boolean modalBlocked = ww.getTarget() instanceof Window
          && getWebToolkit().getWindowManager().isBlockedByModality((Window) ww.getTarget(), false);
      window.setModalBlocked(modalBlocked);

      if (!isDD()) {
        Set<Rectangle> rects = currentAreasToUpdate.get(window.getId());
        window.setContent(Collections.emptyList());

        if (rects != null) {
          List<Rectangle> toPaint = joinRectangles(getGrid(new ArrayList<>(rects), null));
          createPartialContentMsgs(window, toPaint);
        }
      }

      if (ww.getTarget() instanceof Window) {
        window.setOwnerId(findWindowOwner(((Window) ww.getTarget()).getOwner(), zOrder));
      }

      if (htmlPanels.containsKey(ww.getTarget())) {
        handleHtmlPanels(htmlPanels, ww, window, frame, htmlWebComponentsMap);
      }

      if (webContainers.containsKey(ww.getTarget())) {
        handleWebContainers(webContainers.get(ww.getTarget()), window, frame, htmlWebComponentsMap);
      }

      frame.getWindows().add(window);
    }
    return frame;
  }

  private static void createPartialContentMsgs(WindowMsgOut window, List<Rectangle> toPaint) {
    List<WindowPartialContentMsgOut> partialContentList =
        new ArrayList<WindowPartialContentMsgOut>();
    for (Rectangle r : toPaint) {
      if (r.x < window.getWidth() && r.y < window.getHeight()) {
        WindowPartialContentMsgOut content =
            new WindowPartialContentMsgOut(r.x, r.y, Math.min(r.width, window.getWidth() - r.x),
                Math.min(r.height, window.getHeight() - r.y));
        partialContentList.add(content);
      }
    }
    window.setContent(partialContentList);
  }

  private static void handleHtmlPanels(Map<Window, List<HtmlPanel>> htmlPanels, WebWindowPeer ww,
      WindowMsgOut window, AppFrameMsgOut frame,
      Map<Container, Map<JComponent, List<WindowMsgOut>>> htmlWebComponentsMap) {
    for (HtmlPanel htmlPanel : htmlPanels.get(ww.getTarget())) {
      if (!htmlPanel.isShowing()) {
        continue;
      }

      Rectangle visibleRect = htmlPanel.getVisibleRect();
      Dimension size =
          new Dimension(Math.max(0, htmlPanel.getWidth()), Math.max(0, htmlPanel.getHeight()));
      if (visibleRect.width == 0 || visibleRect.height == 0) {
        size.width = 0;
        size.height = 0;
      }

      WindowMsgOut htmlWin = new WindowMsgOut(System.identityHashCode(htmlPanel) + "",
          htmlPanel.getName(), htmlPanel.getLocationOnScreen(), size.width, size.height,
          WindowType.html, window.isModalBlocked(), window.getId());
      if (!isDD()) {
        htmlWin.setContent(Collections.emptyList());
      }

      if (htmlPanel instanceof HtmlPanelImpl impl) {
        Container container = impl.getWebContainer();
        JComponent component = impl.getWebComponent();
        if (container != null && component != null) {
          // if HtmlPanel has a registered container, process later when web containers are
          // processed (see handleWebContainers method)
          if (!htmlWebComponentsMap.containsKey(container)) {
            htmlWebComponentsMap.put(container, new HashMap<>());
          }
          if (!htmlWebComponentsMap.get(container).containsKey(component)) {
            htmlWebComponentsMap.get(container).put(component, new ArrayList<>());
          }
          htmlWebComponentsMap.get(container).get(component).add(htmlWin);
          continue;
        }
      }

      frame.getWindows().add(htmlWin);
    }
  }

  private static void handleWebContainers(List<Container> containers, WindowMsgOut window,
      AppFrameMsgOut frame,
      Map<Container, Map<JComponent, List<WindowMsgOut>>> htmlWebComponentsMap) {
    // sort according to hierarchy
    Collections.sort(containers, (o1, o2) -> {
      return o1.isAncestorOf(o2) ? 1 : (o2.isAncestorOf(o1) ? -1 : 0);
    });

    for (Container container : containers) {
      if (!container.isShowing()) {
        continue;
      }

      Rectangle containerBounds =
          new Rectangle(container.getLocationOnScreen(), container.getSize());
      if (container instanceof JComponent component) {
        Rectangle visibleRect = component.getVisibleRect();
        // if container is inside a scrollpane, visibleRect has non-0 x/y
        // we have to add the visibleRect location to the container.getLocationOnScreen to
        // compensate for the scroll offset
        containerBounds.x += visibleRect.x;
        containerBounds.y += visibleRect.y;
        // restrict the container size by its visible rect size
        containerBounds.width = Math.min(containerBounds.width, visibleRect.width);
        containerBounds.height = Math.min(containerBounds.height, visibleRect.height);
      }

      // handle wrapper
      WindowMsgOut containerWin =
          new WindowMsgOut(System.identityHashCode(container) + "", container.getName(),
              containerBounds.getLocation(), containerBounds.width, containerBounds.height,
              WindowType.internalWrapper, window.isModalBlocked(), window.getId());
      if (!isDD()) {
        containerWin.setContent(Collections.emptyList());
      }

      if (window.getInternalWindows() == null) {
        window.setInternalWindows(new ArrayList<>());
      }
      window.getInternalWindows().add(containerWin);

      // handle child components
      Map<JComponent, List<WindowMsgOut>> htmlComponents = htmlWebComponentsMap.get(container);
      for (Component c : container.getComponents()) {
        if (!c.isShowing()) {
          continue;
        }

        if (htmlComponents != null && htmlComponents.containsKey(c)) {
          // if component contains a registered HtmlPanel
          List<WindowMsgOut> htmlWins = htmlComponents.get(c);
          htmlWins.forEach(htmlWin -> {
            htmlWin.setOwnerId(containerWin.getId());
            htmlWin.setType(WindowType.internalHtml);
            window.getInternalWindows().add(htmlWin);
          });
        }

        WindowMsgOut componentWin = new WindowMsgOut(System.identityHashCode(c) + "", c.getName(),
            c.getLocationOnScreen(), c.getBounds().width, c.getBounds().height, WindowType.internal,
            containerWin.isModalBlocked(), containerWin.getId());

        if (!isDD()) {
          componentWin.setContent(Collections.emptyList());
        }

        window.getInternalWindows().add(componentWin);
      }
    }
  }

  private static void fillClassType(Object windowTarget, WindowMsgOut window) {
    if (windowTarget instanceof JFrame) {
      window.setClassType(WindowClassType.JFrame);
      return;
    }
    if (windowTarget instanceof JDialog) {
      window.setClassType(WindowClassType.JDialog);
      return;
    }
    if (windowTarget instanceof JWindow) {
      window.setClassType(WindowClassType.JWindow);
      return;
    }
    if (windowTarget instanceof Frame) {
      window.setClassType(WindowClassType.Frame);
      return;
    }
    if (windowTarget instanceof Dialog) {
      window.setClassType(WindowClassType.Dialog);
      return;
    }
    if (windowTarget instanceof Window) {
      window.setClassType(WindowClassType.Window);
      return;
    }
  }

  private static void fillDockMode(Object windowTarget, WindowMsgOut window) {
    // must be a Window
    if (!(windowTarget instanceof Window)) {
      window.setDockMode(DockMode.none);
      return;
    }

    // must not be a JWindow
    if (windowTarget instanceof JWindow) {
      window.setDockMode(DockMode.none);
      return;
    }
    switch (getDockMode()) {
      case "ALL":
        if (windowTarget instanceof Dockable dockable1) {
          window.setDockMode(dockable1.isAutoUndock() ? DockMode.autoUndock : DockMode.dockable);
        } else {
          window.setDockMode(DockMode.dockable);
        }
        break;
      case "MARKED":
        if (windowTarget instanceof Dockable dockable1) {
          window.setDockMode(dockable1.isAutoUndock() ? DockMode.autoUndock : DockMode.dockable);
        }
        break;
      case "NONE":
        window.setDockMode(DockMode.none);
        break;
    }
  }

  private static String findWindowOwner(Window owner, List<String> zOrder) {
    if (owner == null) {
      return null;
    }

    WebWindowPeer peer = (WebWindowPeer) WebToolkit.targetToPeer(owner);

    if (peer != null && zOrder.contains(peer.getGuid())) {
      // must be from current z-order windows, otherwise it could be SwingUtilities$SharedOwnerFrame
      return peer.getGuid();
    }

    return null;
  }

  public static Map<String, Set<Rectangle>> postponeNonShowingAreas(
      Map<String, Set<Rectangle>> currentAreasToUpdate) {
    Map<String, Set<Rectangle>> forLaterProcessing = new HashMap<String, Set<Rectangle>>();
    for (String windowId : currentAreasToUpdate.keySet()) {
      WebWindowPeer ww = Util.findWindowPeerById(windowId);
      if (ww != null) {
        if (!((Window) ww.getTarget()).isShowing()) {
          forLaterProcessing.put(windowId, currentAreasToUpdate.get(windowId));
        }
      }
    }
    for (String later : forLaterProcessing.keySet()) {
      currentAreasToUpdate.remove(later);
    }
    return forLaterProcessing;
  }

  public static boolean isWindowDecorationEvent(Window w, AWTEvent e) {
    if (e instanceof MouseEvent event && MouseEvent.MOUSE_WHEEL != e.getID() && w != null
        && w.isEnabled() && w.isShowing()) {
      return isWindowDecorationPosition((Window) w,
          new Point(event.getXOnScreen(), event.getYOnScreen()));
    }
    return false;
  }

  public static boolean isWindowDecorationPosition(Window w, Point locationOnScreen) {
    if (w != null && locationOnScreen != null) {
      Rectangle inner = w.getBounds();
      Insets i = w.getInsets();
      inner.x = i.left;
      inner.y = i.top;
      inner.width -= i.left + i.right;
      inner.height -= i.top + i.bottom;
      boolean isInInnerWindow = SwingUtilities.isRectangleContainingRectangle(inner,
          new Rectangle(locationOnScreen.x - w.getX(), locationOnScreen.y - w.getY(), 0, 0));
      boolean isInWindow = SwingUtilities.isRectangleContainingRectangle(w.getBounds(),
          new Rectangle(locationOnScreen.x, locationOnScreen.y, 0, 0));
      return !isInInnerWindow && isInWindow;
    } else {
      return false;
    }
  }

  public static Set<Rectangle> getGrid(List<Rectangle> dirtyAreas, List<Rectangle> topWindows) {
    Set<Rectangle> result = new HashSet<Rectangle>();
    Set<Integer> xLines = new TreeSet<Integer>();
    Set<Integer> yLines = new TreeSet<Integer>();
    for (Rectangle r : dirtyAreas) {
      xLines.add(r.x);
      xLines.add(r.x + r.width);
      yLines.add(r.y);
      yLines.add(r.y + r.height);
    }
    if (topWindows != null) {
      for (Rectangle r : topWindows) {
        xLines.add(r.x);
        xLines.add(r.x + r.width);
        yLines.add(r.y);
        yLines.add(r.y + r.height);
      }
    }
    Integer[] y = yLines.toArray(new Integer[yLines.size()]);
    Integer[] x = xLines.toArray(new Integer[xLines.size()]);
    for (int row = 0; row < y.length - 1; row++) {
      for (int col = 0; col < x.length - 1; col++) {
        Rectangle potential =
            new Rectangle(x[col], y[row], x[col + 1] - x[col], y[row + 1] - y[row]);
        // filter
        boolean insideDirtyAreas = false;
        for (Rectangle da : dirtyAreas) {
          if (SwingUtilities.isRectangleContainingRectangle(da, potential)) {
            insideDirtyAreas = true;
            break;
          }
        }
        if (insideDirtyAreas) {
          boolean insideTopWindow = false;
          if (topWindows != null) {
            for (Rectangle tw : topWindows) {
              if (SwingUtilities.isRectangleContainingRectangle(tw, potential)) {
                insideTopWindow = true;
                break;
              }
            }
          }
          if (!insideTopWindow) {
            result.add(potential);
          }
        }
      }
    }
    return result;
  }

  public static List<Rectangle> joinRectangles(Set<Rectangle> grid) {
    List<Rectangle> result = new ArrayList<Rectangle>();
    List<Rectangle> gridList = new ArrayList<Rectangle>(grid);
    // join by rows
    List<Rectangle> joinedRows = new ArrayList<Rectangle>();
    Collections.sort(gridList, new Comparator<Rectangle>() {

      @Override
      public int compare(Rectangle o1, Rectangle o2) {
        if (o1.y > o2.y) {
          return 1;
        } else if (o1.y == o2.y) {
          if (o1.x > o2.x) {
            return 1;
          } else {
            return -1;
          }
        } else {
          return -1;
        }
      }
    });
    Rectangle current = null;
    for (Rectangle r : gridList) {
      if (current == null) {
        current = r;
      } else {
        if (current.y == r.y && current.height == r.height && current.x + current.width == r.x) {// is
          // joinable
          // on
          // row
          current.width += r.width;
        } else {
          joinedRows.add(current);
          current = r;
        }
      }
    }
    if (current != null) {
      joinedRows.add(current);
    }
    // join by cols
    Collections.sort(joinedRows, new Comparator<Rectangle>() {

      @Override
      public int compare(Rectangle o1, Rectangle o2) {
        if (o1.x > o2.x) {
          return 1;
        } else if (o1.x == o2.x) {
          if (o1.y > o2.y) {
            return 1;
          } else {
            return -1;
          }
        } else {
          return -1;
        }
      }
    });
    Rectangle currentX = null;
    for (Rectangle r : joinedRows) {
      if (currentX == null) {
        currentX = r;
      } else {
        if (currentX.x == r.x && currentX.width == r.width && currentX.y + currentX.height == r.y) {// is
                                                                                                    // joinable
                                                                                                    // on
                                                                                                    // row
          currentX.height += r.height;
        } else {
          result.add(currentX);
          currentX = r;
        }
      }
    }
    if (currentX != null) {
      result.add(currentX);
    }
    return result;
  }

  public static void resetWindowsGC(int width, int height) {
    List<Window> windows = new ArrayList<Window>(Arrays.asList(Window.getWindows())); // to avoid
                                                                                      // concurent-modification-exception
    for (Window w : windows) {
      try {
        Class<?> windowClazz = w.getClass();
        while (windowClazz != Window.class && windowClazz != null) {
          windowClazz = windowClazz.getSuperclass();
        }
        if (windowClazz != null) {
          try {
            Method m = windowClazz.getDeclaredMethod("resetGC");
            m.setAccessible(true);
            m.invoke(w);
          } catch (Exception e) {
            // do nothing (java7 don't have this method)
          }
        }
        RepaintManager.currentManager(w).setDoubleBufferMaximumSize(new Dimension(width, height));
      } catch (Exception e) {
        AppLogger.error("Util:resetWindowsGC", e);
      }
    }
  }

  public static JFileChooser discoverFileChooser(WebWindowPeer windowPeer) {
    Window w = (Window) windowPeer.getTarget();
    return discoverFileChooser(w);
  }

  public static JFileChooser discoverFileChooser(Window w) {
    if (w instanceof JDialog dialog) {
      Container pane = dialog.getContentPane();
      if (pane != null) {
        Component[] coms = pane.getComponents();
        if (coms != null && coms.length > 0 && coms[0] instanceof JFileChooser chooser) {
          chooser.putClientProperty(WebswingFileChooserUtil.CUSTOM_FILE_CHOOSER, false);
          return chooser;
        }
      }
    }
    return getWebToolkit().getPaintDispatcher().findRegisteredFileChooser(w);
  }

  public static String resolveUploadFilename(File currentDir, String fileName) {
    if (!existsFilename(currentDir, fileName)) {
      return fileName;
    } else {
      int i = fileName.lastIndexOf('.');
      String base = i > 0 ? fileName.substring(0, i) : fileName;
      String ext = i > 0 ? fileName.substring(i) : null;
      int next = 1;
      while (true) {
        String nextFN = base + " " + next + ext;
        if (!existsFilename(currentDir, nextFN)) {
          return nextFN;
        }
        next++;
      }
    }
  }

  public static boolean existsFilename(File currentDir, String fileName) {
    if (currentDir != null && currentDir.exists() && currentDir.isDirectory()) {
      for (File f : currentDir.listFiles()) {
        if (f.getName().equals(fileName)) {
          return true;
        }
      }
    }
    return false;
  }

  public static boolean isDD() {
    boolean startDD =
        Boolean.valueOf(System.getProperty(Constants.SWING_START_SYS_PROP_DIRECTDRAW, "false"));
    boolean supportedDD = Boolean
        .valueOf(System.getProperty(Constants.SWING_START_SYS_PROP_DIRECTDRAW_SUPPORTED, "true"));
    return startDD && supportedDD;
  }

  public static boolean isCompositingWM() {
    return Boolean
        .valueOf(System.getProperty(Constants.SWING_START_SYS_PROP_COMPOSITING_WM, "false"));
  }

  public static boolean isTestMode() {
    return Boolean.valueOf(System.getProperty(Constants.SWING_START_SYS_PROP_TEST_MODE, "false"));
  }

  public static String getDockMode() {
    if (isTouchMode() || !isCompositingWM()) {
      return "NONE";
    }
    return System.getProperty(Constants.SWING_START_SYS_PROP_DOCK_MODE, "NONE");
  }

  public static boolean isWindowUndocked(Window window) {
    WebWindowPeer peer = (WebWindowPeer) WebToolkit.targetToPeer(window);
    if (peer == null) {
      throw new IllegalArgumentException("Cannot find web window peer!");
    }

    return peer.isUndocked();
  }

  public static void toggleWindowDock(Window window, boolean undock) {
    WebWindowPeer peer = (WebWindowPeer) WebToolkit.targetToPeer(window);
    if (peer == null) {
      throw new IllegalArgumentException("Cannot find web window peer!");
    }

    peer.setUndocked(undock);
  }

  public static boolean isTouchMode() {
    return Boolean.valueOf(System.getProperty(Constants.SWING_START_SYS_PROP_TOUCH_MODE, "false"));
  }

  public static boolean isAccessibilityEnabled() {
    return Boolean
        .valueOf(System.getProperty(Constants.SWING_START_SYS_PROP_ACCESSIBILITY_ENABLED, "false"));
  }

  public static void repaintAllWindow() {
    synchronized (WebPaintDispatcher.webPaintLock) {
      for (Window w : Util.getAllWindows()) {
        if (w.isShowing()) {
          final Object peer = WebToolkit.targetToPeer(w);
          if (peer != null && peer instanceof WebWindowPeer windowPeer) {
            windowPeer.updateWindowDecorationImage();
            RepaintManager.currentManager(null).addDirtyRegion(w, 0, 0, w.getWidth(),
                w.getHeight());
          }
        }
      }
    }
  }

  public static Panel findHwComponentParent(JComponent c) {
    for (Container p = c.getParent(); p != null; p = p.getParent()) {
      if (p instanceof Panel panel) {
        return panel;
      }
    }
    return null;
  }

  public static AWTEvent createKeyEvent(Component src, int type, long when, int modifiers,
      int keycode, char character, int keyLocationStandard) {
    KeyEvent e = new KeyEvent(src, type, when, modifiers, keycode, character, keyLocationStandard);
    try {
      Field f = KeyEvent.class.getDeclaredField("extendedKeyCode");
      f.setAccessible(true);
      f.set(e, keycode);
    } catch (Exception e1) {
      AppLogger.error("Failed to update extendedKeyCode of KeyEvent", e);
    }
    return e;
  }

  public static FileDialogEventType getFileChooserEventType(JFileChooser fileChooserDialog) {
    if (Boolean.getBoolean(Constants.SWING_START_SYS_PROP_ISOLATED_FS)) {
      if (Boolean.TRUE
          .equals(fileChooserDialog.getClientProperty(WebswingFileChooserUtil.CUSTOM_FILE_CHOOSER))
          || fileChooserDialog
              .getClientProperty(WebswingFileChooserUtil.ALLOW_DELETE_OVERRIDE) != null
          || fileChooserDialog
              .getClientProperty(WebswingFileChooserUtil.ALLOW_UPLOAD_OVERRIDE) != null
          || fileChooserDialog
              .getClientProperty(WebswingFileChooserUtil.ALLOW_DOWNLOAD_OVERRIDE) != null) {
        return FileDialogEventType.Open;
      }
      if (Boolean.getBoolean(Constants.SWING_START_SYS_PROP_TRANSPARENT_FILE_OPEN)
          && fileChooserDialog.getDialogType() == JFileChooser.OPEN_DIALOG) {
        return FileDialogEventType.AutoUpload;
      }
      if (Boolean.getBoolean(Constants.SWING_START_SYS_PROP_TRANSPARENT_FILE_SAVE)
          && fileChooserDialog.getDialogType() == JFileChooser.SAVE_DIALOG) {
        return FileDialogEventType.AutoSave;
      }
    }
    return FileDialogEventType.Open;
  }

  public static String getFileChooserSelection(JFileChooser fileChooserDialog) {
    StringBuilder sb = new StringBuilder();
    if (fileChooserDialog.isMultiSelectionEnabled()) {
      if (fileChooserDialog.getSelectedFiles() != null) {
        for (File f : fileChooserDialog.getSelectedFiles()) {
          sb.append(f.getName()).append(",");
        }
        return sb.length() > 0 ? sb.substring(0, sb.length() - 1) : "";
      }
    } else {
      if (fileChooserDialog.getSelectedFile() != null) {
        return fileChooserDialog.getSelectedFile().getName();
      }
    }
    return "";
  }

  public static WebComponentPeer getPeer(Component comp) {
    if (comp == null) {
      return null;
    }
    try {
      Field peer = Component.class.getDeclaredField("peer");
      peer.setAccessible(true);
      return (WebComponentPeer) peer.get(comp);
    } catch (Exception e) {
      AppLogger.error("Failed to read peer of component " + comp, e);
      return null;
    }
  }

  public static void waitForImage(Image i) {
    Graphics imageLoaderG = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB).getGraphics();
    ImageObserver observer = new ImageObserver() {
      @Override
      public synchronized boolean imageUpdate(Image img, int infoflags, int x, int y, int width,
          int height) {
        if ((infoflags & ImageObserver.ALLBITS) == ImageObserver.ALLBITS
            || (infoflags & ImageObserver.FRAMEBITS) == ImageObserver.FRAMEBITS) {
          notifyAll();
        }
        return true;
      }
    };

    synchronized (observer) {
      if (!imageLoaderG.drawImage(i, 0, 0, observer)) {
        try {
          observer.wait(300);
        } catch (InterruptedException e) {
          // ignore
        }
      }
    }
  }

  public static File getTimestampedTransferFolder(String marker) {
    String path = System.getProperty(Constants.SWING_START_SYS_PROP_TRANSFER_DIR,
        System.getProperty("user.dir") + "/upload");
    path = path.split(File.pathSeparator)[0];
    File timestampFoleder = new File(path, marker + System.currentTimeMillis());
    timestampFoleder.mkdirs();
    return timestampFoleder;
  }

  @SuppressWarnings("restriction")
  public static void resetWindowsPosition(int oldWidht, int oldHeight) {
    for (Window w : Util.getAllWindows()) {
      WebWindowPeer peer = (WebWindowPeer) WebToolkit.targetToPeer(w);
      if (peer != null) {
        Dimension current = Util.getWebToolkit().getScreenSize();
        if (peer.getTarget() instanceof JFrame) {
          JFrame frame = (JFrame) peer.getTarget();
          // maximized window - auto resize
          if (frame.getExtendedState() == Frame.MAXIMIZED_BOTH && !Util.isCompositingWM()) {
            w.setLocation(0, 0);
            w.setBounds(0, 0, current.width, current.height);
          }
        } else {
          Rectangle b = w.getBounds();
          peer.setBounds(b.x, b.y, b.width, b.height, 0);
        }
      }
    }
  }

  public static boolean isFXWindow(Window w) {
    ToolkitFXService toolkitFXService = Services.getToolkitFXService();

    if (toolkitFXService == null) {
      return false;
    }

    return toolkitFXService.isFXWindow(w);
  }

  public static <T extends Component> Map<Window, List<T>> toWindowMapSynced(Set<T> componentSet) {
    Map<Window, List<T>> map = new HashMap<>();
    Set<T> keys = componentSet;
    for (T key : keys) {
      Window win = SwingUtilities.getWindowAncestor(key);
      if (!map.containsKey(win)) {
        map.put(win, new ArrayList<>());
      }
      map.get(win).add(key);
    }
    return map;
  }

  public static <T> T instantiateClass(Class<T> ifc, String classNameProp,
      String fallbackClassName) {
    return instantiateClass(ifc, classNameProp, fallbackClassName, null);
  }

  public static <T> T instantiateClass(Class<T> ifc, String classNameProp, String fallbackClassName,
      ClassLoader cl) {
    String implClassName = System.getProperty(classNameProp, fallbackClassName);
    if (cl == null) {
      cl = ifc.getClassLoader();
    }
    if (cl == null) {
      cl = ClassLoader.getSystemClassLoader();
    }
    Class<?> implclass = null;
    try {
      implclass = cl.loadClass(implClassName);
    } catch (ClassNotFoundException e) {
      AppLogger.error(ifc.getSimpleName() + ": Implementation class not found", e);
      try {
        implclass = cl.loadClass(fallbackClassName);
      } catch (ClassNotFoundException e1) {
        AppLogger.fatal(
            ifc.getSimpleName() + ": Fatal error:Default implementation class not found.", e1);
        return null;
      }
    }
    if (ifc.isAssignableFrom(implclass)) {
      try {
        return (T) implclass.getDeclaredConstructor().newInstance();
      } catch (Exception e) {
        AppLogger.fatal(ifc.getSimpleName() + ": exception when creating instance of "
            + implclass.getCanonicalName(), e);
      }
    } else {
      AppLogger.fatal(
          ifc.getSimpleName() + ": Fatal error: Implementation is not assignable to base class:"
              + implclass.getCanonicalName());
    }
    return null;
  }

  public static String saveThreadDump(String reason) {
    final StringBuilder dump = new StringBuilder();
    final ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
    final ThreadInfo[] threadInfos =
        threadMXBean.getThreadInfo(threadMXBean.getAllThreadIds(), true, true);
    for (ThreadInfo threadInfo : threadInfos) {
      if (threadInfo != null) {
        dump.append(threadInfoToString(threadInfo));
        dump.append("\n");
      }
    }

    try {
      String instanceId = System.getProperty(Constants.SWING_START_SYS_PROP_INSTANCE_ID);
      String timestamp = new SimpleDateFormat("yy.MM.dd.HH.mm.ss").format(new Date());
      String dumpName = "ThreadDump-" + timestamp + "-"
          + URLEncoder.encode(instanceId + "-" + reason, "UTF-8") + ".txt";

      try (InputStream is =
          new ByteArrayInputStream(dump.toString().getBytes(StandardCharsets.UTF_8))) {
        Services.getDataStoreService().storeData("threadDump", dumpName, is, false);
      }

      return dumpName;
    } catch (Exception e) {
      AppLogger.error("Failed to save thread dump. ", e);
    }

    return null;
  }

  private static String threadInfoToString(ThreadInfo ti) {
    StringBuilder sb = new StringBuilder(
        "\"" + ti.getThreadName() + "\"" + " Id=" + ti.getThreadId() + " " + ti.getThreadState());
    if (ti.getLockName() != null) {
      sb.append(" on ").append(ti.getLockName());
    }
    if (ti.getLockOwnerName() != null) {
      sb.append(" owned by \"").append(ti.getLockOwnerName()).append("\" Id=")
          .append(ti.getLockOwnerId());
    }
    if (ti.isSuspended()) {
      sb.append(" (suspended)");
    }
    if (ti.isInNative()) {
      sb.append(" (in native)");
    }
    sb.append('\n');
    int i = 0;
    StackTraceElement[] stackTrace = ti.getStackTrace();
    for (; i < stackTrace.length; i++) {
      StackTraceElement ste = stackTrace[i];
      sb.append("\tat ").append(ste.toString());
      sb.append('\n');
      if (i == 0 && ti.getLockInfo() != null) {
        Thread.State ts = ti.getThreadState();
        switch (ts) {
          case BLOCKED:
            sb.append("\t-  blocked on ").append(ti.getLockInfo());
            sb.append('\n');
            break;
          case WAITING, TIMED_WAITING:
            sb.append("\t-  waiting on ").append(ti.getLockInfo());
            sb.append('\n');
            break;
          default:
        }
      }

      MonitorInfo[] lockedMonitors = ti.getLockedMonitors();
      for (MonitorInfo mi : lockedMonitors) {
        if (mi.getLockedStackDepth() == i) {
          sb.append("\t-  locked ").append(mi);
          sb.append('\n');
        }
      }
    }
    if (i < stackTrace.length) {
      sb.append("\t...");
      sb.append('\n');
    }

    LockInfo[] locks = ti.getLockedSynchronizers();
    if (locks.length > 0) {
      sb.append("\n\tNumber of locked synchronizers = " + locks.length);
      sb.append('\n');
      for (LockInfo li : locks) {
        sb.append("\t- ").append(li);
        sb.append('\n');
      }
    }
    sb.append('\n');
    return sb.toString();
  }

  public static List<WindowSwitchMsgOut> getWindowSwitchList() {
    List<WindowSwitchMsgOut> list = new ArrayList<>();

    List<String> zOrder = getWebToolkit().getWindowManager().getZOrder();

    for (String windowId : zOrder) {
      WebWindowPeer ww = findWindowPeerById(windowId);
      if (ww == null) {
        continue;
      }

      WindowSwitchMsgOut window = new WindowSwitchMsgOut();
      window.setId(windowId);

      if (ww.getTarget() instanceof Frame) {
        window.setTitle(((Frame) ww.getTarget()).getTitle());
      } else if (ww.getTarget() instanceof Dialog) {
        window.setTitle(((Dialog) ww.getTarget()).getTitle());
      }

      window.setModalBlocked(ww.getTarget() instanceof Window && getWebToolkit().getWindowManager()
          .isBlockedByModality((Window) ww.getTarget(), false));

      list.add(window);
    }

    return list;
  }

  public static boolean isApplet() {
    return Boolean.getBoolean(Constants.SWING_START_SYS_PROP_IS_APPLET);
  }

  public static boolean isSessionLoggingEnabled() {
    return Boolean.getBoolean(Constants.SWING_START_SYS_PROP_SESSION_LOGGING_ENABLED);
  }

  public static String getDataStoreConfigString() {
    return System.getProperty(Constants.SWING_START_SYS_PROP_DATA_STORE_CONFIG);
  }

  public static Thread async(Runnable runnable) {
    Thread thread = new Thread(runnable);
    thread.start();
    return thread;
  }

  private Util() {}
}
