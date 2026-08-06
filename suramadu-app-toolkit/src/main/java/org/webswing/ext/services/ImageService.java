package org.webswing.ext.services;

import org.webswing.common.WindowDecoratorTheme;

import java.awt.Image;
import java.awt.image.BufferedImage;

public interface ImageService {

  byte[] getPngImage(BufferedImage image);

  WindowDecoratorTheme getWindowDecorationTheme();

  Image readFromDataUrl(String img);
}
