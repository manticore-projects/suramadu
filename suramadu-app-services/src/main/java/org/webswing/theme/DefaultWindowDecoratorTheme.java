package org.webswing.theme;

import org.webswing.common.WindowActionType;
import org.webswing.common.WindowDecoratorTheme;
import org.webswing.model.appframe.out.AccessibilityMsgOut;
import org.webswing.toolkit.api.component.Dockable;
import org.webswing.toolkit.util.Util;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.LinearGradientPaint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;

/**
 * Pure Java2D / Swing window decorator. No bitmaps, no XPM, no themerc, and no runtime dependency
 * on FlatLaf — everything is resolved from system properties with hardcoded Manticore brand
 * defaults.
 *
 * <h3>Configuration (system properties)</h3>
 *
 * <pre>
 *   org.webswing.window.theme.primaryColor       (Color)  active title bar     default #030146 (navy)
 *   org.webswing.window.theme.secondaryColor     (Color)  inactive title bar   default #9e9e9e (gray)
 *   org.webswing.window.theme.accentColor        (Color)  brand accent (hover) default #ff420e (orange)
 *   org.webswing.window.theme.contentBgColor     (Color)  fill for chrome margin so it visually
 *                                                         matches the content pane                default #ffffff (white)
 *   org.webswing.window.theme.titleHeight        (int)                          default 22
 *   org.webswing.window.theme.borderWidth        (int)    visible 1 px outline default 1
 *   org.webswing.window.theme.titleFont          (String) e.g. "Roboto Medium" default SansSerif 12
 * </pre>
 *
 * <h3>FlatLaf integration (optional)</h3> If your app uses FlatLaf with
 * {@code JFrame.setDefaultLookAndFeelDecorated(true)}, the JFrame title bar is rendered by FlatLaf,
 * not this decorator. Call {@link #applyToUIManager()} once after
 * {@code UIManager.setLookAndFeel(...)} to push the same brand colours into FlatLaf's
 * {@code TitlePane.*} keys so the outer frame matches the Webswing-decorated dialogs. The helper
 * itself doesn't import any FlatLaf class — it just sets standard UIManager keys, harmless on other
 * LAFs.
 */
public class DefaultWindowDecoratorTheme implements WindowDecoratorTheme {

  // ---- Property names ------------------------------------------------------
  private static final String PROP_PRIMARY = "org.webswing.window.theme.primaryColor";
  private static final String PROP_SECONDARY = "org.webswing.window.theme.secondaryColor";
  private static final String PROP_ACCENT = "org.webswing.window.theme.accentColor";
  private static final String PROP_CONTENT_BG = "org.webswing.window.theme.contentBgColor";
  private static final String PROP_TITLE_HEIGHT = "org.webswing.window.theme.titleHeight";
  private static final String PROP_BORDER_WIDTH = "org.webswing.window.theme.borderWidth";
  private static final String PROP_TITLE_FONT = "org.webswing.window.theme.titleFont";

  // ---- Hardcoded fallbacks (Manticore brand) -------------------------------
  private static final Color FALLBACK_PRIMARY = new Color(0x030146);
  private static final Color FALLBACK_SECONDARY = new Color(0x9e9e9e);
  private static final Color FALLBACK_ACCENT = new Color(0xff420e);
  private static final Color FALLBACK_CONTENT_BG = Color.WHITE;
  private static final Color FALLBACK_FG_LIGHT = new Color(0xf0f0f0);
  private static final Color FALLBACK_FG_DARK = new Color(0x202020);

  // ---- Resolved theme values (sysprop > hardcoded; resolved once at ctor) --
  private final Color primaryColor;
  private final Color secondaryColor;
  private final Color accentColor;
  private final Color contentBg;
  private final String titleFontName; // null => use SansSerif fallback

  // ---- Geometry ------------------------------------------------------------
  private final int titleHeight;
  private final int borderWidth;
  /**
   * Effective chrome thickness on left/right/bottom — at least {@link #MIN_HIT_ZONE} px so Webswing
   * actually delivers mouse events for resize hit-testing. Visually only the outermost
   * {@link #borderWidth} pixels are painted in the border colour; the gap is filled with
   * {@link #contentBg} and is invisible.
   */
  private final int chromeMargin;

  // ---- Layout constants (pixels) -------------------------------------------
  private static final int BUTTON_WIDTH = 18;
  private static final int BUTTON_GLYPH = 8;
  private static final int BUTTON_SPACING = 2;
  private static final int BUTTON_RIGHT_PAD = 6;
  private static final int ICON_SIZE = 16;
  private static final int ICON_LEFT_PAD = 6;
  private static final int ICON_TEXT_GAP = 6;

  /** Minimum chrome thickness on left/right/bottom needed for usable resize hit-zones. */
  private static final int MIN_HIT_ZONE = 5;
  /** Corner uni-resize hit-zone size (square). */
  private static final int CORNER_GRAB_ZONE = 10;

  private final Insets insets;

  public DefaultWindowDecoratorTheme() {
    this.primaryColor = orDefault(parseColorOrNull(PROP_PRIMARY), FALLBACK_PRIMARY);
    this.secondaryColor = orDefault(parseColorOrNull(PROP_SECONDARY), FALLBACK_SECONDARY);
    this.accentColor = orDefault(parseColorOrNull(PROP_ACCENT), FALLBACK_ACCENT);
    this.contentBg = orDefault(parseColorOrNull(PROP_CONTENT_BG), FALLBACK_CONTENT_BG);
    this.titleFontName = System.getProperty(PROP_TITLE_FONT);

    this.titleHeight = parseIntOr(PROP_TITLE_HEIGHT, 22);
    this.borderWidth = parseIntOr(PROP_BORDER_WIDTH, 1);
    this.chromeMargin = Math.max(borderWidth, MIN_HIT_ZONE);

    this.insets = new Insets(titleHeight, chromeMargin, chromeMargin, chromeMargin);

    // One-shot startup log so configuration is verifiable. Look for this line in stdout
    // — if values aren't what you set in JAVA_OPTS, the sysprops aren't reaching the JVM
    // (commonly: an unquoted '#' in a hex code being read as a shell comment).
    System.out.println("DefaultWindowDecoratorTheme:" + " titleHeight=" + titleHeight
        + " borderWidth=" + borderWidth + " chromeMargin=" + chromeMargin + " primary="
        + hex(primaryColor) + " secondary=" + hex(secondaryColor) + " accent=" + hex(accentColor)
        + " contentBg=" + hex(contentBg) + " titleFont="
        + (titleFontName != null ? titleFontName : "<default>"));
  }

  // =========================================================================
  // WindowDecoratorTheme contract
  // =========================================================================

  @Override
  public Insets getInsets() {
    return (Insets) insets.clone();
  }

  @Override
  public void paintWindowDecoration(Graphics g, Object window, int w, int h) {
    Graphics2D g2 = (Graphics2D) g.create();
    try {
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
          RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
      g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
          RenderingHints.VALUE_FRACTIONALMETRICS_ON);
      g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

      boolean active = window != null
          && window.equals(Util.getWebToolkit().getWindowManager().getActiveWindow());
      boolean isDialog = window instanceof Dialog;

      Color bg = active ? primaryColor : secondaryColor;
      Color fg = preferredForeground(bg);
      Color borderColor = darken(bg, 0.15f);

      paintChrome(g2, w, h, bg, borderColor, contentBg, active, isDialog);

      // ---- Window icon ----
      Image icon = getIcon(window);
      int textX = ICON_LEFT_PAD;
      if (icon != null) {
        int iconY = (titleHeight - ICON_SIZE) / 2;
        // Bicubic interpolation for crisp downscaling of typical 32×32 / 48×48 icons to 16×16.
        Object oldInterp = g2.getRenderingHint(RenderingHints.KEY_INTERPOLATION);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.drawImage(icon, ICON_LEFT_PAD, iconY, ICON_SIZE, ICON_SIZE, null);
        if (oldInterp != null) {
          g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, oldInterp);
        }
        // Subtle 1 px border so the icon remains visible when its colours
        // blend into the title-bar background (e.g. same-hue branding icons).
        // Uses the title-bar foreground at ~31 % opacity — adapts automatically
        // to light-fg-on-dark (active/navy) and dark-fg-on-light (inactive/grey).
        g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));
        g2.setColor(new Color(fg.getRed(), fg.getGreen(), fg.getBlue(), 80));
        g2.drawRect(ICON_LEFT_PAD, iconY, ICON_SIZE - 1, ICON_SIZE - 1);
        textX += ICON_SIZE + ICON_TEXT_GAP;
      }

      // ---- Title text ----
      String title = nullToEmpty(getTitle(window));
      g2.setFont(titleFont());
      g2.setColor(fg);
      FontMetrics fm = g2.getFontMetrics();
      int textY = (titleHeight - fm.getHeight()) / 2 + fm.getAscent();
      Shape oldClip = g2.getClip();
      int textRightLimit = computeButtonAreaStart(window, w) - 4;
      g2.clipRect(textX, 0, Math.max(0, textRightLimit - textX), titleHeight);
      g2.drawString(title, textX, textY);
      g2.setClip(oldClip);

      // ---- Buttons ----
      paintButtons(g2, window, w, fg);
    } finally {
      g2.dispose();
    }
  }

  // =========================================================================
  // Painting helpers
  // =========================================================================

  private void paintChrome(Graphics2D g2, int w, int h, Color bg, Color borderColor,
      Color contentBg, boolean active, boolean isDialog) {
    // 1. Title bar — Murrine-style 2-stop vertical gradient: lighter at the top,
    // slightly darker at the bottom. HSL-based lighten/darken keeps the gradient
    // perceptually consistent across both navy (active) and grey (inactive),
    // where naive RGB interpolation would flatten on saturated dark colours.
    Color topShade = lighten(bg, 0.15f);
    Color bottomShade = darken(bg, 0.05f);
    LinearGradientPaint titleGradient = new LinearGradientPaint(0f, 0f, 0f, titleHeight,
        new float[] {0f, 1f}, new Color[] {topShade, bottomShade});
    g2.setPaint(titleGradient);
    g2.fillRect(0, 0, w, titleHeight);

    // 2. Side and bottom chrome margins. Webswing only delivers mouse events for
    // resize hit-testing inside the chrome (i.e. inside the insets). chromeMargin
    // reserves >= 5 px so the hit-zones are usable; we paint that gap in contentBg
    // so it visually merges with the content pane.
    if (chromeMargin > 0) {
      g2.setColor(contentBg);
      g2.fillRect(0, titleHeight, chromeMargin, h - titleHeight - chromeMargin); // left
      g2.fillRect(w - chromeMargin, titleHeight, chromeMargin, h - titleHeight - chromeMargin); // right
      g2.fillRect(0, h - chromeMargin, w, chromeMargin); // bottom

      // 2b. Dialog-only 2-pixel shadow accent at the outer edge of the chrome margin.
      if (isDialog && chromeMargin > borderWidth) {
        paintDialogShadow(g2, w, h);
      }
    }

    // 4. Outer perimeter border — 1 px on all four sides, square corners.
    if (borderWidth > 0) {
      g2.setColor(borderColor);
      g2.fillRect(0, 0, w, borderWidth); // top
      g2.fillRect(0, h - borderWidth, w, borderWidth); // bottom
      g2.fillRect(0, 0, borderWidth, h); // left
      g2.fillRect(w - borderWidth, 0, borderWidth, h); // right
    }
  }

  /**
   * Soft drop-shadow simulation for dialogs. Instead of fading across the entire chrome margin
   * (which reads as a thick grey border), the shadow is concentrated into a 2-pixel band
   * immediately inside the outer 1-px border. The inner part of the chrome margin stays pure
   * {@code contentBg}, so the visible result is "white padding around content + soft dark accent at
   * the edge" — much closer to a real elevation shadow than a uniform inset gradient.
   */
  private void paintDialogShadow(Graphics2D g2, int w, int h) {
    int extent = Math.min(2, chromeMargin - borderWidth);
    if (extent <= 0)
      return;

    Color clear = new Color(0, 0, 0, 0);
    Color shadow = new Color(0, 0, 0, 80); // ~31% black at the edge pixel

    // Right edge — shadow band just inside the right border
    int rxInner = w - borderWidth - extent;
    int rxOuter = w - borderWidth;
    g2.setPaint(new LinearGradientPaint(rxInner, 0, rxOuter, 0, new float[] {0f, 1f},
        new Color[] {clear, shadow}));
    g2.fillRect(rxInner, titleHeight, extent, h - titleHeight - borderWidth);

    // Left edge — mirror
    int lxOuter = borderWidth;
    int lxInner = borderWidth + extent;
    g2.setPaint(new LinearGradientPaint(lxOuter, 0, lxInner, 0, new float[] {0f, 1f},
        new Color[] {shadow, clear}));
    g2.fillRect(lxOuter, titleHeight, extent, h - titleHeight - borderWidth);

    // Bottom edge — shadow band just above the bottom border
    int byInner = h - borderWidth - extent;
    int byOuter = h - borderWidth;
    g2.setPaint(new LinearGradientPaint(0, byInner, 0, byOuter, new float[] {0f, 1f},
        new Color[] {clear, shadow}));
    g2.fillRect(borderWidth, byInner, w - 2 * borderWidth, extent);
  }

  private void paintButtons(Graphics2D g2, Object window, int w, Color fg) {
    g2.setColor(fg);
    g2.setStroke(new BasicStroke(1.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

    int x = w - BUTTON_RIGHT_PAD;

    if (isCloseButtonVisible(window)) {
      x -= BUTTON_WIDTH;
      paintCloseGlyph(g2, x);
    }
    if (isMinMaxButtonVisible(window)) {
      x -= BUTTON_SPACING + BUTTON_WIDTH;
      paintMaximizeGlyph(g2, x, isMaximized(window));
      x -= BUTTON_SPACING + BUTTON_WIDTH;
      paintMinimizeGlyph(g2, x);
    }
    if (isDockButtonVisible(window) && window instanceof Window win) {
      x -= BUTTON_SPACING + BUTTON_WIDTH;
      if (Util.isWindowUndocked(win)) {
        paintDockGlyph(g2, x);
      } else {
        paintUndockGlyph(g2, x);
      }
    }
  }

  private int btnCx(int xLeft) {
    return xLeft + BUTTON_WIDTH / 2;
  }

  private int btnCy() {
    return titleHeight / 2;
  }

  private void paintCloseGlyph(Graphics2D g2, int x) {
    int cx = btnCx(x), cy = btnCy();
    int r = BUTTON_GLYPH / 2;
    g2.drawLine(cx - r, cy - r, cx + r, cy + r);
    g2.drawLine(cx - r, cy + r, cx + r, cy - r);
  }

  private void paintMaximizeGlyph(Graphics2D g2, int x, boolean restore) {
    int cx = btnCx(x), cy = btnCy();
    int r = BUTTON_GLYPH / 2;
    if (restore) {
      g2.drawRect(cx - r + 1, cy - r, 2 * r - 2, 2 * r - 2);
      g2.drawRect(cx - r, cy - r + 1, 2 * r - 2, 2 * r - 2);
    } else {
      g2.drawRect(cx - r, cy - r, 2 * r, 2 * r);
    }
  }

  private void paintMinimizeGlyph(Graphics2D g2, int x) {
    int cx = btnCx(x), cy = btnCy();
    int r = BUTTON_GLYPH / 2;
    g2.drawLine(cx - r, cy + r, cx + r, cy + r);
  }

  private void paintUndockGlyph(Graphics2D g2, int x) {
    // Up-arrow above a bar — "lift out".
    int cx = btnCx(x), cy = btnCy();
    g2.drawLine(cx - 4, cy + 3, cx + 4, cy + 3);
    g2.drawLine(cx, cy - 4, cx, cy + 1);
    g2.drawLine(cx, cy - 4, cx - 2, cy - 2);
    g2.drawLine(cx, cy - 4, cx + 2, cy - 2);
  }

  private void paintDockGlyph(Graphics2D g2, int x) {
    // Down-arrow into a bar — "drop back in".
    int cx = btnCx(x), cy = btnCy();
    g2.drawLine(cx - 4, cy + 3, cx + 4, cy + 3);
    g2.drawLine(cx, cy - 4, cx, cy + 1);
    g2.drawLine(cx, cy + 1, cx - 2, cy - 1);
    g2.drawLine(cx, cy + 1, cx + 2, cy - 1);
  }

  // =========================================================================
  // Hit-test rectangles — geometry MUST mirror paintButtons exactly.
  // =========================================================================

  private int computeButtonAreaStart(Object window, int w) {
    int x = w - BUTTON_RIGHT_PAD;
    if (isCloseButtonVisible(window))
      x -= BUTTON_WIDTH;
    if (isMinMaxButtonVisible(window))
      x -= 2 * (BUTTON_SPACING + BUTTON_WIDTH);
    if (isDockButtonVisible(window))
      x -= BUTTON_SPACING + BUTTON_WIDTH;
    return x;
  }

  private Rectangle btnRect(int xLeft) {
    return new Rectangle(xLeft, 0, BUTTON_WIDTH, titleHeight);
  }

  private Rectangle getCloseRect(Window w) {
    int x = w.getWidth() - BUTTON_RIGHT_PAD - BUTTON_WIDTH;
    return btnRect(x);
  }

  private Rectangle getMaximizeRect(Window w) {
    int x = w.getWidth() - BUTTON_RIGHT_PAD;
    if (isCloseButtonVisible(w))
      x -= BUTTON_WIDTH;
    x -= BUTTON_SPACING + BUTTON_WIDTH;
    return btnRect(x);
  }

  private Rectangle getHideRect(Window w) {
    int x = w.getWidth() - BUTTON_RIGHT_PAD;
    if (isCloseButtonVisible(w))
      x -= BUTTON_WIDTH;
    x -= BUTTON_SPACING + BUTTON_WIDTH; // maximize slot
    x -= BUTTON_SPACING + BUTTON_WIDTH; // hide slot
    return btnRect(x);
  }

  private Rectangle getDockUndockRect(Window w) {
    int x = w.getWidth() - BUTTON_RIGHT_PAD;
    if (isCloseButtonVisible(w))
      x -= BUTTON_WIDTH;
    if (isMinMaxButtonVisible(w))
      x -= 2 * (BUTTON_SPACING + BUTTON_WIDTH);
    x -= BUTTON_SPACING + BUTTON_WIDTH;
    return btnRect(x);
  }

  // =========================================================================
  // Action / accessibility — semantics preserved from the bitmap impl.
  // =========================================================================

  @Override
  public WindowActionType getAction(Window w, Point e) {
    Rectangle ep = new Rectangle(e.x, e.y, 1, 1);
    Insets i = w.getInsets();

    if (isDockButtonVisible(w)
        && SwingUtilities.isRectangleContainingRectangle(getDockUndockRect(w), ep)) {
      return Util.isWindowUndocked(w) ? WindowActionType.dock : WindowActionType.undock;
    }
    if (isMinMaxButtonVisible(w)) {
      if (SwingUtilities.isRectangleContainingRectangle(getHideRect(w), ep)) {
        return WindowActionType.minimize;
      }
      if (SwingUtilities.isRectangleContainingRectangle(getMaximizeRect(w), ep)) {
        return WindowActionType.maximize;
      }
    }
    if (isCloseButtonVisible(w)
        && SwingUtilities.isRectangleContainingRectangle(getCloseRect(w), ep)) {
      return WindowActionType.close;
    }

    if (canResize(w)) {
      // Corners — 10 px square zones, win over edge zones below.
      if (e.x < CORNER_GRAB_ZONE && e.y < CORNER_GRAB_ZONE)
        return WindowActionType.resizeUniTopLeft;
      if (e.x > w.getWidth() - CORNER_GRAB_ZONE && e.y < CORNER_GRAB_ZONE)
        return WindowActionType.resizeUniTopRight;
      if (e.x < CORNER_GRAB_ZONE && e.y > w.getHeight() - CORNER_GRAB_ZONE)
        return WindowActionType.resizeUniBottomLeft;
      if (e.x > w.getWidth() - CORNER_GRAB_ZONE && e.y > w.getHeight() - CORNER_GRAB_ZONE)
        return WindowActionType.resizeUniBottomRight;
      // Edges — hit-zones match the chrome margin (= insets) so Webswing actually
      // delivers events for the entire reserved chrome area.
      if (e.x > w.getWidth() - chromeMargin)
        return WindowActionType.resizeRight;
      if (e.x < chromeMargin)
        return WindowActionType.resizeLeft;
      if (e.y > w.getHeight() - chromeMargin)
        return WindowActionType.resizeBottom;
      if (e.y < chromeMargin)
        return WindowActionType.resizeTop;
    }

    if (e.y < i.top)
      return WindowActionType.move;
    return WindowActionType.cursorChanged;
  }

  @Override
  public AccessibilityMsgOut getAccessible(Window window, WindowActionType action,
      Point mousePointer) {
    if (!action.isButtonActionType())
      return null;

    AccessibilityMsgOut result = new AccessibilityMsgOut();
    result.setId(System.identityHashCode(window) + "-" + action.name());
    result.setRole("decorationbutton"); // not a real ARIA role

    List<String> states = new ArrayList<>();
    states.add("ENABLED");
    result.setStates(states);

    Rectangle rect = null;
    switch (action) {
      case dock, undock -> {
        rect = getDockUndockRect(window);
        result.setText("accessibility.window.button.toggleDock");
      }
      case close -> {
        rect = getCloseRect(window);
        result.setText("accessibility.window.button.close");
      }
      case maximize -> {
        rect = getMaximizeRect(window);
        if (window instanceof Frame frame
            && (frame.getExtendedState() & Frame.MAXIMIZED_BOTH) != 0) {
          result.setText("accessibility.window.button.restore");
        } else {
          result.setText("accessibility.window.button.maximize");
        }
      }
      case minimize -> {
        rect = getHideRect(window);
        result.setText("accessibility.window.button.minimize");
      }
      default -> {
        /* nothing */ }
    }

    if (rect != null) {
      Point loc = window.getLocationOnScreen();
      result.setScreenX(loc.x + rect.x);
      result.setScreenY(loc.y + rect.y);
      result.setWidth(rect.width);
      result.setHeight(rect.height);
    }
    return result;
  }

  // =========================================================================
  // Visibility & resize predicates — preserved verbatim from the bitmap impl.
  // =========================================================================

  public boolean isMinMaxButtonVisible(Object w) {
    if (w instanceof Window window && Util.isWindowUndocked(window))
      return false;
    return (w instanceof Frame f) && f.isResizable();
  }

  public boolean isCloseButtonVisible(Object w) {
    if (w instanceof Window window && Util.isWindowUndocked(window))
      return false;
    return true;
  }

  public boolean canResize(Window w) {
    if (Util.isWindowUndocked(w))
      return false;
    return (w instanceof Dialog d && d.isResizable())
        || ((w instanceof Frame f) && f.isResizable());
  }

  private boolean isDockButtonVisible(Object w) {
    return switch (Util.getDockMode()) {
      case "ALL" -> true;
      case "MARKED" -> w instanceof Dockable;
      default -> false;
    };
  }

  // =========================================================================
  // Optional FlatLaf bridge — call once at startup, after UIManager.setLookAndFeel(...)
  // =========================================================================

  /**
   * Pushes the resolved brand colours into FlatLaf's {@code TitlePane.*} and
   * {@code Component.accentColor} UIManager keys. Useful when JFrames are decorated by FlatLaf (via
   * {@code JFrame.setDefaultLookAndFeelDecorated(true)}) so the outer frame chrome matches
   * Webswing-decorated dialogs.
   *
   * <p>
   * Reads the same sysprops as the decorator; falls back to brand defaults. No FlatLaf classes are
   * imported — this just sets standard UIManager keys, so it is harmless on LAFs that don't read
   * them.
   *
   * <pre>
   * FlatLightLaf.setup();
   * DefaultWindowDecoratorTheme.applyToUIManager();
   * JFrame.setDefaultLookAndFeelDecorated(true);
   * </pre>
   */
  public static void applyToUIManager() {
    Color primary = orDefault(parseColorOrNull(PROP_PRIMARY), FALLBACK_PRIMARY);
    Color secondary = orDefault(parseColorOrNull(PROP_SECONDARY), FALLBACK_SECONDARY);
    Color accent = orDefault(parseColorOrNull(PROP_ACCENT), FALLBACK_ACCENT);

    Color primaryFg = preferredForeground(primary);
    Color secondaryFg = preferredForeground(secondary);

    UIManager.put("TitlePane.background", primary);
    UIManager.put("TitlePane.inactiveBackground", secondary);
    UIManager.put("TitlePane.foreground", primaryFg);
    UIManager.put("TitlePane.inactiveForeground", secondaryFg);
    UIManager.put("TitlePane.borderColor", darken(primary, 0.15f));
    UIManager.put("TitlePane.embeddedForeground", primaryFg);

    UIManager.put("TitlePane.buttonHoverBackground", lighten(primary, 0.15f));
    UIManager.put("TitlePane.buttonPressedBackground", lighten(primary, 0.25f));
    UIManager.put("TitlePane.closeHoverBackground", accent);
    UIManager.put("TitlePane.closeHoverForeground", Color.WHITE);
    UIManager.put("TitlePane.closePressedBackground", darken(accent, 0.10f));
    UIManager.put("TitlePane.closePressedForeground", Color.WHITE);

    UIManager.put("Component.accentColor", accent);

    System.out.println("DefaultWindowDecoratorTheme.applyToUIManager:" + " primary=" + hex(primary)
        + " secondary=" + hex(secondary) + " accent=" + hex(accent));
  }

  // =========================================================================
  // Utilities
  // =========================================================================

  private static String getTitle(Object o) {
    if (o instanceof Frame f)
      return f.getTitle();
    if (o instanceof Dialog d)
      return d.getTitle();
    return null;
  }

  private static Image getIcon(Object o) {
    if (o instanceof Frame f)
      return f.getIconImage();
    if (o instanceof Dialog d) {
      List<Image> images = d.getIconImages();
      if (!images.isEmpty())
        return images.get(0);
    }
    return null;
  }

  private static boolean isMaximized(Object o) {
    return o instanceof Frame f && (f.getExtendedState() & Frame.MAXIMIZED_BOTH) != 0;
  }

  private Font titleFont() {
    if (titleFontName != null && !titleFontName.isBlank()) {
      return new Font(titleFontName, Font.BOLD, 12);
    }
    return new Font(Font.SANS_SERIF, Font.BOLD, 12);
  }

  /** Pick a foreground colour (light or dark) based on background luminance — WCAG style. */
  private static Color preferredForeground(Color bg) {
    double lum = 0.2126 * sRgbToLin(bg.getRed()) + 0.7152 * sRgbToLin(bg.getGreen())
        + 0.0722 * sRgbToLin(bg.getBlue());
    return lum < 0.4 ? FALLBACK_FG_LIGHT : FALLBACK_FG_DARK;
  }

  private static double sRgbToLin(int v) {
    double s = v / 255.0;
    return s <= 0.03928 ? s / 12.92 : Math.pow((s + 0.055) / 1.055, 2.4);
  }

  /**
   * Lightens a colour by raising its HSL lightness component. HSL is used (rather than naive RGB
   * interpolation toward white) because raising L preserves hue and saturation — on a deep
   * saturated colour like navy {@code #030146}, RGB interpolation barely shifts the dominant
   * channel and desaturates the result, leaving a gradient that's invisible on dark backgrounds.
   * Adjusting L instead makes the difference perceptually consistent across light and dark colours.
   */
  private static Color lighten(Color c, float amount) {
    float[] hsl = rgbToHsl(c);
    hsl[2] = Math.min(1f, hsl[2] + amount);
    return hslToRgb(hsl[0], hsl[1], hsl[2], c.getAlpha());
  }

  /** Darkens a colour by lowering its HSL lightness component. */
  private static Color darken(Color c, float amount) {
    float[] hsl = rgbToHsl(c);
    hsl[2] = Math.max(0f, hsl[2] - amount);
    return hslToRgb(hsl[0], hsl[1], hsl[2], c.getAlpha());
  }

  /** RGB → HSL. Returns {@code [h, s, l]} all in [0, 1]. */
  private static float[] rgbToHsl(Color c) {
    // Compare in int-space so SpotBugs doesn't flag the equality tests as FE
    // (floating-point equality). Conceptually safe even in float — Math.max
    // returns one of its inputs verbatim — but ints are clearer either way.
    int ri = c.getRed();
    int gi = c.getGreen();
    int bi = c.getBlue();
    int maxI = Math.max(ri, Math.max(gi, bi));
    int minI = Math.min(ri, Math.min(gi, bi));

    float l = (maxI + minI) / (2f * 255f);
    float h = 0f, s = 0f;
    if (maxI != minI) {
      float r = ri / 255f;
      float g = gi / 255f;
      float b = bi / 255f;
      float max = maxI / 255f;
      float min = minI / 255f;
      float d = max - min;
      s = (l > 0.5f) ? d / (2f - max - min) : d / (max + min);
      if (maxI == ri)
        h = (g - b) / d + (g < b ? 6f : 0f);
      else if (maxI == gi)
        h = (b - r) / d + 2f;
      else
        h = (r - g) / d + 4f;
      h /= 6f;
    }
    return new float[] {h, s, l};
  }

  /**
   * HSL → RGB. {@code h, s, l} in [0, 1], {@code alpha} in [0, 255].
   *
   * <p>
   * No achromatic shortcut: when {@code s == 0}, the general formula gives {@code q = l},
   * {@code p = l}, and {@link #hueToRgb} returns {@code l} on every branch — the result is
   * identical, so an {@code if (s == 0f)} branch would just be a SpotBugs FE finding for no reason.
   */
  private static Color hslToRgb(float h, float s, float l, int alpha) {
    float q = (l < 0.5f) ? l * (1f + s) : l + s - l * s;
    float p = 2f * l - q;
    float r = hueToRgb(p, q, h + 1f / 3f);
    float g = hueToRgb(p, q, h);
    float b = hueToRgb(p, q, h - 1f / 3f);
    return new Color(clamp255(Math.round(r * 255f)), clamp255(Math.round(g * 255f)),
        clamp255(Math.round(b * 255f)), alpha);
  }

  private static float hueToRgb(float p, float q, float t) {
    if (t < 0f)
      t += 1f;
    if (t > 1f)
      t -= 1f;
    if (t < 1f / 6f)
      return p + (q - p) * 6f * t;
    if (t < 1f / 2f)
      return q;
    if (t < 2f / 3f)
      return p + (q - p) * (2f / 3f - t) * 6f;
    return p;
  }

  private static int clamp255(int v) {
    return Math.max(0, Math.min(255, v));
  }

  private static Color orDefault(Color c, Color fallback) {
    return c != null ? c : fallback;
  }

  private static Color parseColorOrNull(String prop) {
    String v = System.getProperty(prop);
    if (v == null || v.isBlank())
      return null;
    try {
      return Color.decode(v.trim());
    } catch (NumberFormatException ex) {
      System.err.println("DefaultWindowDecoratorTheme: cannot parse " + prop + "=\"" + v + "\" — "
          + ex.getMessage() + " (falling back to default)");
      return null;
    }
  }

  private static int parseIntOr(String prop, int fallback) {
    String v = System.getProperty(prop);
    if (v == null || v.isBlank())
      return fallback;
    try {
      return Integer.parseInt(v.trim());
    } catch (NumberFormatException ignored) {
      return fallback;
    }
  }

  private static String nullToEmpty(String s) {
    return s == null ? "" : s;
  }

  private static String hex(Color c) {
    return c == null ? "<null>"
        : String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
  }
}
