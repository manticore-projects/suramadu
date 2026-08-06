package org.webswing.ext.services;

import java.awt.Image;
import java.awt.ImageCapabilities;
import java.awt.image.VolatileImage;

public interface DirectDrawService {

  Image createImage(int width, int height);

  Image extractWebImage(Image webImage);

  byte[] buildWebImage(Image webImage);

  void resetCache();

  boolean isDirty(Image windowDecorationImage);

  VolatileImage createVolatileImage(int width, int height, ImageCapabilities caps,
      int transparency);

  void resetImage(Image webImage);

  void resetImageBeforeRepaint(Image webImage);
}
