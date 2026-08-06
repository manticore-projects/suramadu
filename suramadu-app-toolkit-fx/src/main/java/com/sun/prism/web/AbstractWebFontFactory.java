package com.sun.prism.web;

import com.sun.javafx.font.FontFactory;
import com.sun.javafx.font.PGFont;

public abstract class AbstractWebFontFactory implements FontFactory {
  private final FontFactory fontFactory;

  public AbstractWebFontFactory(FontFactory fontFactory) {
    this.fontFactory = fontFactory;
  }

  @Override
  public PGFont createFont(String name, float size) {
    return fontFactory.createFont(name, size);
  }

  @Override
  public PGFont createFont(String family, boolean bold, boolean italic, float size) {
    return fontFactory.createFont(family, bold, italic, size);
  }

  @Override
  public PGFont deriveFont(PGFont font, boolean bold, boolean italic, float size) {
    return fontFactory.deriveFont(font, bold, italic, size);
  }

  @Override
  public String[] getFontFamilyNames() {
    return fontFactory.getFontFamilyNames();
  }

  @Override
  public String[] getFontFullNames() {
    return fontFactory.getFontFullNames();
  }

  @Override
  public String[] getFontFullNames(String family) {
    return fontFactory.getFontFullNames(family);
  }

  @Override
  public boolean hasPermission() {
    return fontFactory.hasPermission();
  }

  @Override
  public boolean isPlatformFont(String name) {
    return fontFactory.isPlatformFont(name);
  }

  FontFactory getFontFactory() {
    return fontFactory;
  }
}
