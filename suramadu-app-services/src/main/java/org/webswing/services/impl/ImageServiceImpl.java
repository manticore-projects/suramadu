package org.webswing.services.impl;

import com.manticore.tools.ZPNG;
import org.apache.commons.codec.binary.Base64;
import org.webswing.common.WindowDecoratorTheme;
import org.webswing.ext.services.ImageService;
import org.webswing.toolkit.util.Util;
import org.webswing.util.AppLogger;

import javax.imageio.ImageIO;
import javax.imageio.spi.IIORegistry;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ImageServiceImpl implements ImageService {

  private static ImageServiceImpl impl;
  private WindowDecoratorTheme windowDecorationTheme;


  public static ImageServiceImpl getInstance() {
    if (impl == null) {
      impl = new ImageServiceImpl();
    }
    return impl;
  }

  public ImageServiceImpl() {
    ClassLoader currentContextClassLoader = Thread.currentThread().getContextClassLoader();
    Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
    IIORegistry.getDefaultInstance().registerApplicationClasspathSpis();
    Thread.currentThread().setContextClassLoader(currentContextClassLoader);
  }

  public byte[] getPngImage(BufferedImage image) {
    if (image == null) {
      return null;
    }
    byte[] result = ZPNG.encode(image, image.getColorModel().hasAlpha() ? 4 : 3, 2);
    return result;
  }

  public WindowDecoratorTheme getWindowDecorationTheme() {
    if (windowDecorationTheme == null) {
      this.windowDecorationTheme = Util.instantiateClass(WindowDecoratorTheme.class,
          WindowDecoratorTheme.DECORATION_THEME_IMPL_PROP,
          WindowDecoratorTheme.DECORATION_THEME_IMPL_DEFAULT,
          ImageServiceImpl.class.getClassLoader());
      if (windowDecorationTheme == null) {
        System.exit(1);
      }
    }
    return windowDecorationTheme;
  }

  @Override
  public Image readFromDataUrl(String dataUrl) {
    String encodingPrefix = "base64,";
    int contentStartIndex = dataUrl.indexOf(encodingPrefix) + encodingPrefix.length();
    byte[] imageData = Base64.decodeBase64(dataUrl.substring(contentStartIndex));

    // create BufferedImage from byteArray
    BufferedImage inputImage = null;
    try {
      inputImage = ImageIO.read(new ByteArrayInputStream(imageData));
    } catch (IOException e) {
      AppLogger.error("ImageService: reading image from dataUrl failed", e);
    }
    return inputImage;
  }
}
