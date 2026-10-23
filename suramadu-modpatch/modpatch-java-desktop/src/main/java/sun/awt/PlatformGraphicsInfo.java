package sun.awt;

import java.awt.AWTError;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;

/**
 * Webswing patched {@code sun.awt.PlatformGraphicsInfo}.
 *
 * <p>
 * Loaded via {@code --patch-module java.desktop=suramadu-jdk-patch.jar} so that JDK 21+ never
 * instantiates {@code X11GraphicsEnvironment} and never opens an X11 display connection. This is
 * what makes Webswing truly headless — no Xvfb, no real X server, no {@code DISPLAY} env var
 * required.
 *
 * <p>
 * Background: starting with JDK 21, {@code java.awt.GraphicsEnvironment$LocalGE.createGE()} was
 * simplified to unconditionally delegate to {@code PlatformGraphicsInfo.createGE()}. The system
 * property {@code java.awt.graphicsenv} is no longer consulted. On Linux the stock
 * {@code PlatformGraphicsInfo.createGE()} is hardcoded to
 * {@code return new X11GraphicsEnvironment()}, whose static initialiser calls native
 * {@code initDisplay()} and fails without a display. Webswing's prior workaround (Unsafe overwrite
 * of {@code LocalGE.INSTANCE}) only succeeded after that initDisplay had run, which required Xvfb
 * to be present.
 *
 * <p>
 * This patch replaces {@code createGE()} with a version that returns the Webswing
 * {@code WebGraphicsEnvironment} variant matching the running JDK, so the X11 path is never taken
 * at all.
 *
 * <p>
 * JDK version selection: {@code Runtime.version().feature() >= 27} selects the {@code *27} toolkit
 * variant (single-arg {@code SunToolkit.postEvent}, no {@code sun.awt.AppContext}); anything older
 * — JDK 26 included, where AppContext still exists — selects the {@code *11} variant.
 *
 * <p>
 * Requirements:
 * <ul>
 * <li>Webswing toolkit classes must be reachable from the system classloader (they normally are,
 * via {@code -Xbootclasspath/a:} in the child JVM args).</li>
 * <li>{@code sun.font.fontmanager=org.webswing.toolkit.ge.WebFontManager} must be set so that
 * {@code SunGraphicsEnvironment}'s constructor does not fall back to {@code X11FontManager}.</li>
 * </ul>
 */
public class PlatformGraphicsInfo {

  /** "27" on JDK 27+, otherwise "11". */
  private static final String VARIANT = Runtime.version().feature() >= 27 ? "27" : "11";

  private static final String WEB_GE_CLASS =
      "org.webswing.toolkit.ge.WebGraphicsEnvironment" + VARIANT;
  private static final String WEB_TOOLKIT_CLASS = "org.webswing.toolkit.WebToolkit" + VARIANT;

  /**
   * Returns a Webswing {@link GraphicsEnvironment} instead of the JDK's hardcoded
   * {@code X11GraphicsEnvironment}. Called exactly once during AWT initialisation from
   * {@code GraphicsEnvironment$LocalGE.<clinit>}.
   */
  public static GraphicsEnvironment createGE() {
    try {
      return (GraphicsEnvironment) Class
          .forName(WEB_GE_CLASS, true, ClassLoader.getSystemClassLoader()).getDeclaredConstructor()
          .newInstance();
    } catch (Throwable t) {
      throw new AWTError("Webswing JDK patch: failed to instantiate " + WEB_GE_CLASS
          + " — is the Webswing toolkit JAR on the " + "system / bootstrap classpath? Root cause: "
          + t);
    }
  }

  /**
   * On JDK 21–25 this was a safety net ({@code awt.toolkit} set by the launcher still worked). On
   * JDK 27, {@code Toolkit.getDefaultToolkit()} calls {@code PlatformGraphicsInfo.createToolkit()}
   * directly and the {@code awt.toolkit} property is dead — this method IS the toolkit selection
   * point. A {@code -Dawt.toolkit=...} in the launcher's child JVM args is ignored by the JDK.
   */
  public static Toolkit createToolkit() {
    try {
      return (Toolkit) Class.forName(WEB_TOOLKIT_CLASS, true, ClassLoader.getSystemClassLoader())
          .getDeclaredConstructor().newInstance();
    } catch (Throwable t) {
      throw new AWTError(
          "Webswing JDK patch: failed to instantiate " + WEB_TOOLKIT_CLASS + ". Root cause: " + t);
    }
  }

  /**
   * AWT calls this once at startup to decide whether {@code java.awt.headless} should default to
   * {@code true}. The stock Linux implementation returns {@code true} when {@code DISPLAY} is
   * unset. We always return {@code false}: Webswing supplies a virtual display via
   * {@link GraphicsEnvironment}, so interactive Swing components must remain usable regardless of
   * whether a real X server is reachable.
   */
  public static boolean getDefaultHeadlessProperty() {
    return false;
  }

  /**
   * Message used by AWT when reporting headless-related failures. Returned only if
   * {@link #getDefaultHeadlessProperty()} were to return {@code true}, which it doesn't — but the
   * method must exist so the patched class matches the shape of the JDK's original.
   */
  public static String getDefaultHeadlessMessage() {
    return "Webswing runs in virtual display mode; no X server is required.";
  }

  /**
   * Returns the default font-manager class name. Stock JDK 21+ on Linux returns
   * {@code "sun.awt.X11FontManager"} here, whose JNI implementation lives in {@code libawt_xawt.so}
   * and segfaults during {@code getFontPathNative()} when no X11 display is reachable.
   *
   * <p>
   * Note: on JDK 27 the stock {@code sun.font.FontManagerFactory} no longer consults any property
   * or this method — it hardcodes {@code sun.font.PlatformFontInfo.createFontManager()} (which
   * returns {@code X11FontManager} on Linux). Our patched {@code FontManagerFactory} replaces that
   * class entirely, which closes the last X11 dependency; this method is kept for older JDKs /
   * shape compatibility.
   */
  public static String getDefaultFontManagerClassName() {
    return "org.webswing.toolkit.ge.WebFontManager";
  }
}
