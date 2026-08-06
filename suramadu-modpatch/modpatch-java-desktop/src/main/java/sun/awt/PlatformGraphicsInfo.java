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
 * This patch replaces {@code createGE()} with a version that returns
 * {@link org.webswing.toolkit.ge.WebGraphicsEnvironment11} directly, so the X11 path is never taken
 * at all.
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

  private static final String WEB_GE_CLASS = "org.webswing.toolkit.ge.WebGraphicsEnvironment11";
  private static final String WEB_TOOLKIT_CLASS = "org.webswing.toolkit.WebToolkit11";

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
   * Safety net only. Webswing sets {@code awt.toolkit} explicitly in {@code Main.java}, so
   * {@code Toolkit.getDefaultToolkit()} normally never reaches this method. If it ever does (some
   * startup ordering change in a future JDK), instantiate WebToolkit11 directly.
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
   * This is consulted by {@code sun.font.FontManagerFactory.getInstance()} when the
   * {@code sun.font.fontmanager} system property is not set OR (newer JDK versions) when the
   * factory ignores the property entirely. Returning the Webswing font manager here closes the last
   * X11 dependency in the AWT initialisation chain.
   */
  public static String getDefaultFontManagerClassName() {
    return "org.webswing.toolkit.ge.WebFontManager";
  }
}
