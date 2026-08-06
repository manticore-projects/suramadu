package org.webswing.toolkit.ge;

import org.webswing.Constants;
import sun.awt.FontConfiguration;

@SuppressWarnings("restriction")
public class WebGraphicsEnvironment11 extends WebGraphicsEnvironment {

  public WebGraphicsEnvironment11() {
    // Always install the X11-free font manager. Without this override, on
    // headless Linux the JVM defaults to sun.awt.X11FontManager, which opens
    // a display connection during font enumeration — fatal when DISPLAY is
    // unset on a truly headless server (no Xvfb).
    //
    // WebFontManager extends SunFontManager and discovers fonts via fontconfig
    // JNI directly, with no X11 dependency.
    System.setProperty("sun.font.fontmanager", WebFontManager.class.getName());

    if (hasFontConfiguration()) {
      System.setProperty("sun.awt.fontconfig",
          System.getProperty(Constants.SWING_START_SYS_PROP_FONT_CONFIG));
    }
  }

  public FontConfiguration createFontConfiguration(boolean b1, boolean b2) {
    return null; // not used in java8 (see WebFontManager)
  }
}
