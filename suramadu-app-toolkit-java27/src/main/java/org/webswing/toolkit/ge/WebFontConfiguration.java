package org.webswing.toolkit.ge;

import sun.awt.FontConfiguration;
import sun.font.SunFontManager;

import java.nio.charset.Charset;


@SuppressWarnings("restriction")
public class WebFontConfiguration extends FontConfiguration {

  public WebFontConfiguration(SunFontManager sfm) {
    super(sfm);
    init();
  }

  static Charset utf = new sun.nio.cs.UTF_32();

  @Override
  public String getFallbackFamilyName(String paramString1, String paramString2) {
    return paramString2;
  }

  @Override
  protected void initReorderMap() {

  }

  @Override
  protected String getEncoding(String paramString1, String paramString2) {
    return "default";
  }

  @Override
  protected Charset getDefaultFontCharset(String paramString) {
    return utf;
  }

  @Override
  protected String getFaceNameFromComponentFontName(String paramString) {
    return paramString;
  }

  @Override
  protected String getFileNameFromComponentFontName(String paramString) {
    return paramString;
  }

}
