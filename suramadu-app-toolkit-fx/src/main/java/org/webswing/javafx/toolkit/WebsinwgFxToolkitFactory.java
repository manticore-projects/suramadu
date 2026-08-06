package org.webswing.javafx.toolkit;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Pixels;
import com.sun.javafx.font.FontFactory;
import com.sun.prism.Graphics;
import com.sun.prism.web.WebRTTextureWrapper;
import org.webswing.Constants;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public abstract class WebsinwgFxToolkitFactory {
  private static WebsinwgFxToolkitFactory factory;

  public static WebsinwgFxToolkitFactory getFactory() {
    if (factory == null) {
      String name = System.getProperty(Constants.SWING_FX_TOOLKIT_FACTORY);
      Class<?> cls = null;
      try {
        cls = Class.forName(name);
      } catch (ClassNotFoundException e) {
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        if (cl != null) {
          try {
            cls = cl.loadClass(name);
          } catch (final ClassNotFoundException e1) {
            throw new RuntimeException("Webswing Fx Toolkit Factory class not found: " + name, e);
          }
        }
      }
      try {
        if (cls != null) {
          factory = (WebsinwgFxToolkitFactory) cls.getConstructor().newInstance();
        }
      } catch (final ReflectiveOperationException e) {
        throw new RuntimeException("Could not create Fx Toolkit Factory : " + name, e);
      }
    }
    return factory;
  }

  public abstract Graphics createWebPrismGraphicsWrapper(Graphics original,
      WebRTTextureWrapper textureWrapper);

  public abstract Pixels createPixels(int width, int height, ByteBuffer data);

  public abstract Pixels createPixels(int width, int height, IntBuffer data);

  public abstract FontFactory createWebFontFactory(FontFactory fontFactory);

  public abstract Application createApplication();
}
