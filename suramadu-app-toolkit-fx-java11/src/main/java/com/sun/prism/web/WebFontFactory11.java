package com.sun.prism.web;

import com.sun.javafx.font.FontFactory;
import com.sun.javafx.font.PGFont;

import java.io.InputStream;

public class WebFontFactory11 extends AbstractWebFontFactory {

  public WebFontFactory11(FontFactory fontFactory) {
    super(fontFactory);
  }

  @Override
  public PGFont[] loadEmbeddedFont(String name, InputStream stream, float size, boolean register,
      boolean all) {
    return getFontFactory().loadEmbeddedFont(name, stream, size, register, all);
  }

  @Override
  public PGFont[] loadEmbeddedFont(String name, String path, float size, boolean register,
      boolean all) {
    return getFontFactory().loadEmbeddedFont(name, path, size, register, all);
  }
}
