package org.webswing.toolkit.ge;

import org.webswing.toolkit.util.Services;
import org.webswing.toolkit.util.Util;
import sun.awt.image.OffScreenImage;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.VolatileImage;
import java.awt.image.WritableRaster;

@SuppressWarnings("restriction")
public class WebGraphicsConfig extends BufferedImageGraphicsConfig {

  WebScreenDevice device;
  private final int width;
  private final int height;

  public static WebGraphicsConfig getWebGraphicsConfig(int width, int height) {
    return new WebGraphicsConfig(width, height);
  }

  private WebGraphicsConfig(int width, int height) {
    super(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB), null);
    this.width = width;
    this.height = height;
  }


  @Override
  public GraphicsDevice getDevice() {
    if (device == null) {
      device = new WebScreenDevice(super.getDevice(), this);
    }
    return device;
  }

  public Image createAcceleratedImage(Component target, int paramInt1, int paramInt2) {
    ColorModel localColorModel = getColorModel(2);
    WritableRaster localWritableRaster =
        localColorModel.createCompatibleWritableRaster(paramInt1, paramInt2);
    return new OffScreenImage(target, localColorModel, localWritableRaster,
        localColorModel.isAlphaPremultiplied());
  }

  @Override
  public VolatileImage createCompatibleVolatileImage(int width, int height, ImageCapabilities caps,
      int transparency) throws AWTException {
    if (Util.isDD()) {
      return Services.getDirectDrawService().createVolatileImage(width, height, caps, transparency);
    } else {
      return super.createCompatibleVolatileImage(width, height, caps, transparency);
    }
  }

  public Rectangle getBounds() {
    return new Rectangle(0, 0, this.width, this.height);
  }

  public boolean isTranslucencyCapable() {
    return true;
  }

}
