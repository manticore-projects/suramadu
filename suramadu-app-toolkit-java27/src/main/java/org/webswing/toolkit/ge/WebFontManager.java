package org.webswing.toolkit.ge;

import sun.awt.FontConfiguration;
import sun.font.SunFontManager;

@SuppressWarnings("restriction")
public class WebFontManager extends SunFontManager {

  @Override
  protected FontConfiguration createFontConfiguration() {
    return createFontConfiguration(false, false);
  }

  @Override
  public FontConfiguration createFontConfiguration(boolean arg0, boolean arg1) {
    return new WebFontConfiguration(this);
  }

  @Override
  public String[] getDefaultPlatformFont() {
    // dummy value
    String[] info = new String[2];
    info[0] = "Dialog";
    info[1] = "/dialog.ttf";
    return info;
  }

  @Override
  protected String getFontPath(boolean arg0) {
    return "";
  }

  @Override
  protected void registerFontDirs(String arg0) {
    // do nothing
  }
}
