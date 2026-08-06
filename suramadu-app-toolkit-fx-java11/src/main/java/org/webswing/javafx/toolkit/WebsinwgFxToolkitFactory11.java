package org.webswing.javafx.toolkit;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Pixels;
import com.sun.javafx.font.FontFactory;
import com.sun.prism.Graphics;
import com.sun.prism.web.WebFontFactory11;
import com.sun.prism.web.WebPrismGraphicsWrapper11;
import com.sun.prism.web.WebRTTextureWrapper;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class WebsinwgFxToolkitFactory11 extends WebsinwgFxToolkitFactory {
  @Override
  public Graphics createWebPrismGraphicsWrapper(Graphics original,
      WebRTTextureWrapper textureWrapper) {
    return new WebPrismGraphicsWrapper11(original, textureWrapper);
  }

  @Override
  public Pixels createPixels(int width, int height, ByteBuffer data) {
    return new WebPixels11(width, height, data);
  }

  @Override
  public Pixels createPixels(int width, int height, IntBuffer data) {
    return new WebPixels11(width, height, data);
  }

  @Override
  public FontFactory createWebFontFactory(FontFactory fontFactory) {
    return new WebFontFactory11(fontFactory);
  }

  @Override
  public Application createApplication() {
    return new WebApplication11();
  }
}
