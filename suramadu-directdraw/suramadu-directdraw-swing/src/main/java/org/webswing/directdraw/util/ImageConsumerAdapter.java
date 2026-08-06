package org.webswing.directdraw.util;

import java.awt.image.ColorModel;
import java.awt.image.ImageConsumer;
import java.util.Hashtable;

public class ImageConsumerAdapter implements ImageConsumer {
  public long hash = 1;

  @Override
  public void setDimensions(int width, int height) {}

  @Override
  public void setProperties(Hashtable<?, ?> props) {}

  @Override
  public void setColorModel(ColorModel model) {}

  @Override
  public void setHints(int hintflags) {}

  @Override
  public void setPixels(int x, int y, int w, int h, ColorModel model, byte[] pixels, int off,
      int scansize) {}

  @Override
  public void setPixels(int x, int y, int w, int h, ColorModel model, int[] pixels, int off,
      int scansize) {}

  @Override
  public void imageComplete(int status) {}

  public long getHash() {
    return hash;
  }
}
