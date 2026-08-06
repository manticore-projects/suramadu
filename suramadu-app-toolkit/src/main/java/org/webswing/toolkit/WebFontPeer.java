package org.webswing.toolkit;

import sun.awt.PlatformFont;

@SuppressWarnings("restriction")
public class WebFontPeer extends PlatformFont {

  public WebFontPeer(String paramString, int paramInt) {
    super(paramString, paramInt);
  }

  protected char getMissingGlyphCharacter() {
    return 0;
  }

}
