package sun.font;

import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * Webswing patched {@code sun.font.FontManagerFactory}.
 *
 * <p>
 * Loaded via {@code --patch-module java.desktop=modpatch-java.desktop.jar} (the same patch JAR as
 * the patched {@code sun.awt.PlatformGraphicsInfo}).
 *
 * <p>
 * Background: the stock JDK 21+ {@code FontManagerFactory} hardcodes the default font-manager class
 * in a {@code static} initialiser:
 * 
 * <pre>
 * DEFAULT_CLASS = "sun.awt.X11FontManager"; // on Linux
 * </pre>
 * 
 * and only consults the {@code sun.font.fontmanager} system property as an override. In practice,
 * setting that property at JVM startup has been observed not to take effect on JDK 26 (the X11
 * default is still selected). Even when the property *is* honoured, the choice of default matters
 * because some code paths instantiate the default class directly.
 *
 * <p>
 * This patched version defaults to {@code org.webswing.toolkit.ge.WebFontManager} on every platform
 * — there is no Webswing scenario where the JDK's native, X11-dependent font managers should be
 * used. {@code sun.font.fontmanager} is still honoured as an explicit override, in case a
 * deployment needs to swap in a different implementation (testing, debugging, custom font
 * handling).
 *
 * <p>
 * Behavioural difference from the stock class:
 * <ul>
 * <li>{@code DEFAULT_CLASS} is {@code org.webswing.toolkit.ge.WebFontManager} on all platforms (was
 * {@code X11FontManager} / {@code Win32FontManager} / {@code CFontManager}).</li>
 * <li>All other behaviour is identical: lazy synchronised init, property override,
 * {@code InternalError} on instantiation failure.</li>
 * </ul>
 */
public final class FontManagerFactory {

  /** Webswing's X11-free font manager. Used regardless of host platform. */
  private static final String DEFAULT_CLASS = "org.webswing.toolkit.ge.WebFontManager";

  private static FontManager instance = null;

  @SuppressWarnings("removal")
  public static synchronized FontManager getInstance() {
    if (instance != null) {
      return instance;
    }
    instance = AccessController.doPrivileged(new PrivilegedAction<FontManager>() {
      public FontManager run() {
        try {
          String fmClassName = System.getProperty("sun.font.fontmanager", DEFAULT_CLASS);
          ClassLoader cl = ClassLoader.getSystemClassLoader();
          return (FontManager) Class.forName(fmClassName, true, cl).getDeclaredConstructor()
              .newInstance();
        } catch (Exception e) {
          InternalError err = new InternalError("Webswing JDK patch: failed to load font manager. "
              + "Expected " + DEFAULT_CLASS + " on the boot classpath. " + "Root cause: " + e);
          err.initCause(e);
          throw err;
        }
      }
    });
    return instance;
  }
}
