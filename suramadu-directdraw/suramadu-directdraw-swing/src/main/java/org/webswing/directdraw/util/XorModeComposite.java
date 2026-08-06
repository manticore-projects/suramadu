package org.webswing.directdraw.util;

import java.awt.Color;
import java.awt.Composite;
import java.awt.CompositeContext;
import java.awt.RenderingHints;
import java.awt.image.ColorModel;

public class XorModeComposite implements Composite {
  private final Color xorColor;

  public XorModeComposite(Color xorColor) {
    this.xorColor = xorColor;
  }

  public Color getXorColor() {
    return xorColor;
  }

  @Override
  public CompositeContext createContext(ColorModel srcColorModel, ColorModel dstColorModel,
      RenderingHints hints) {
    throw new UnsupportedOperationException("not a functional composite");
  }
}
