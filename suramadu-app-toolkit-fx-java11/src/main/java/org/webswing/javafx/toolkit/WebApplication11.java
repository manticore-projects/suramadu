package org.webswing.javafx.toolkit;

import com.sun.glass.ui.GlassRobot;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.Screen;

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.IntBuffer;

public class WebApplication11 extends AbstractWebApplication {
  @Override
  protected Screen[] staticScreen_getScreens() {
    try {
      // long nativePtr,
      //
      // int depth,
      // int x,
      // int y,
      // int width,
      // int height,
      //
      // int platformX,
      // int platformY,
      // int platformWidth,
      // int platformHeight,
      //
      // int visibleX,
      // int visibleY,
      // int visibleWidth,
      // int visibleHeight,
      //
      // int resolutionX,
      // int resolutionY,
      //
      // float platformScaleX,
      // float platformScaleY,
      // float outputScaleX,
      // float outputScaleY
      Constructor<Screen> screenConstructor = Screen.class.getDeclaredConstructor(long.class,
          int.class, int.class, int.class, int.class, int.class, int.class, int.class, int.class,
          int.class, int.class, int.class, int.class, int.class, int.class, int.class, float.class,
          float.class, float.class, float.class);
      screenConstructor.setAccessible(true);

      Rectangle b = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
          .getDefaultConfiguration().getBounds();
      int depth = 24;
      int dpi = 72;
      Screen[] screens = new Screen[1];
      screens[0] = screenConstructor.newInstance(1L, depth, b.x, b.y, b.width, b.height, b.x, b.y,
          b.width, b.height, b.x, b.y, b.width, b.height, dpi, dpi, 1.0f, 1.0f, 1.0f, 1.0f);
      return screens;
    } catch (NoSuchMethodException | InvocationTargetException | InstantiationException
        | IllegalAccessException e) {
      throw new RuntimeException("Unable to construct a Screen", e);
    }
  }

  @Override
  public Pixels createPixels(int width, int height, IntBuffer data, float v, float v1) {
    return new WebPixels11(width, height, data, v, v1);
  }

  @Override
  public GlassRobot createRobot() {
    return null;
  }

}
