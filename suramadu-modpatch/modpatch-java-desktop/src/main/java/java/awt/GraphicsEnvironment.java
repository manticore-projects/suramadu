/*
 * Webswing JDK patch for java.awt.GraphicsEnvironment.
 *
 * Source structure mirrors the OpenJDK 21+ class. Only the inner
 * LocalGE.createGE() method is modified — every public/protected method
 * preserves the exact signature of the original so that downstream code
 * compiled against the stock JDK class continues to link correctly.
 *
 * Belt-and-suspenders companion to the patched sun.awt.PlatformGraphicsInfo:
 *
 *   - PlatformGraphicsInfo.createGE()  → already returns WebGraphicsEnvironment11
 *   - GraphicsEnvironment.LocalGE.createGE() → also returns WebGraphicsEnvironment11,
 *     directly. If a future JDK ever stops calling PlatformGraphicsInfo from
 *     LocalGE, this patch still wins.
 *
 * Distributed via --patch-module java.desktop=modpatch-java.desktop.jar.
 *
 * Original copyright/license: GPLv2 with Classpath Exception (Oracle).
 */
package java.awt;

import java.awt.image.BufferedImage;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Locale;

import sun.awt.PlatformGraphicsInfo;
import sun.font.FontManager;
import sun.font.FontManagerFactory;

public abstract class GraphicsEnvironment {

  /**
   * Holder class for the singleton instance. The same lazy-initialisation pattern as stock JDK;
   * only the {@link #createGE()} method differs.
   */
  private static class LocalGE {
    static final GraphicsEnvironment INSTANCE = createGE();

    /**
     * ── Webswing modification ─────────────────────────────────────────── Stock JDK delegates
     * unconditionally to {@code PlatformGraphicsInfo.createGE()}. Our version honours
     * {@code java.awt.graphicsenv} (restoring pre-JDK 21 behaviour where the property *was* read)
     * and defaults to {@code WebGraphicsEnvironment11} when the property is unset.
     *
     * No fallback to {@code PlatformGraphicsInfo.createGE()} here: that class is ALSO patched by us
     * to return {@code WebGraphicsEnvironment11}, so a fallback would be redundant, and the
     * cross-reference between two patched classes within the same {@code --patch-module} JAR
     * confuses {@code javac}'s type resolution (it sees the stock
     * {@code java.awt.GraphicsEnvironment} as a separate type from the patched one in the same
     * compilation unit).
     */
    private static GraphicsEnvironment createGE() {
      String nm = System.getProperty("java.awt.graphicsenv",
          "org.webswing.toolkit.ge.WebGraphicsEnvironment11");
      try {
        return (GraphicsEnvironment) Class.forName(nm, true, ClassLoader.getSystemClassLoader())
            .getDeclaredConstructor().newInstance();
      } catch (Throwable t) {
        AWTError err = new AWTError("Webswing JDK patch: failed to create GraphicsEnvironment '"
            + nm + "'. Is the Webswing toolkit JAR on the boot classpath? " + "Root cause: " + t);
        err.initCause(t);
        throw err;
      }
    }
  }

  @SuppressWarnings("removal")
  private static volatile Boolean headless;
  private static volatile Boolean defaultHeadless;

  protected GraphicsEnvironment() {}

  public static GraphicsEnvironment getLocalGraphicsEnvironment() {
    return LocalGE.INSTANCE;
  }

  public static boolean isHeadless() {
    return getHeadlessProperty();
  }

  static String getHeadlessMessage() {
    if (headless == null) {
      getHeadlessProperty();
    }
    return defaultHeadless ? null
        : "\nNo X11 DISPLAY variable was set,\n" + "or no headful library support was found,\n"
            + "but this program performed an operation which requires it.\n"
            + "Webswing note: this should not happen — WebGraphicsEnvironment11 "
            + "should have been installed via the --patch-module mechanism.";
  }

  @SuppressWarnings("removal")
  private static boolean getHeadlessProperty() {
    if (headless == null) {
      AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
        String nm = System.getProperty("java.awt.headless");
        if (nm == null) {
          defaultHeadless = Boolean.valueOf(PlatformGraphicsInfo.getDefaultHeadlessProperty());
          headless = defaultHeadless;
        } else {
          headless = Boolean.valueOf(nm);
        }
        return null;
      });
    }
    return headless;
  }

  static void checkHeadless() throws HeadlessException {
    if (isHeadless()) {
      throw new HeadlessException();
    }
  }

  public boolean isHeadlessInstance() {
    return getHeadlessProperty();
  }

  public abstract GraphicsDevice[] getScreenDevices() throws HeadlessException;

  public abstract GraphicsDevice getDefaultScreenDevice() throws HeadlessException;

  public abstract Graphics2D createGraphics(BufferedImage img);

  public abstract Font[] getAllFonts();

  public abstract String[] getAvailableFontFamilyNames();

  public abstract String[] getAvailableFontFamilyNames(Locale l);

  public Point getCenterPoint() throws HeadlessException {
    Rectangle usableBounds = getDefaultScreenDevice().getDefaultConfiguration().getBounds();
    Insets insets = Toolkit.getDefaultToolkit()
        .getScreenInsets(getDefaultScreenDevice().getDefaultConfiguration());
    return new Point((usableBounds.width / 2 + usableBounds.x) - (insets.left + insets.right) / 2,
        (usableBounds.height / 2 + usableBounds.y) - (insets.top + insets.bottom) / 2);
  }

  public Rectangle getMaximumWindowBounds() throws HeadlessException {
    return getDefaultScreenDevice().getDefaultConfiguration().getBounds();
  }

  public boolean registerFont(Font font) {
    if (font == null) {
      throw new NullPointerException("font cannot be null.");
    }
    FontManager fm = FontManagerFactory.getInstance();
    return fm.registerFont(font);
  }

  public void preferLocaleFonts() {
    FontManagerFactory.getInstance().preferLocaleFonts();
  }

  public void preferProportionalFonts() {
    FontManagerFactory.getInstance().preferProportionalFonts();
  }
}
