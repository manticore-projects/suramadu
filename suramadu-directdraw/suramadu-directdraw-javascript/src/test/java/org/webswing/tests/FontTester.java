package org.webswing.tests;

import sun.font.CompositeFont;
import sun.font.Font2D;
import sun.font.PhysicalFont;

import java.awt.Font;
import java.lang.reflect.Method;
import java.util.Locale;

public class FontTester {

  public static void main(String... args) throws Exception {
    Font font = new Font("Dialog", Font.BOLD, 12);
    describeFont(font);
  }

  private static void describeFont(Font font) throws Exception {
    Method method = font.getClass().getDeclaredMethod("getFont2D");
    method.setAccessible(true);
    Font2D f = (Font2D) method.invoke(font);

    describeFont2D(f);
  }

  private static void describeFont2D(Font2D font) {
    if (font instanceof CompositeFont) {
      System.out.println("Font '" + font.getFontName(Locale.getDefault()) + "' is composed of:");

      CompositeFont cf = (CompositeFont) font;
      for (int i = 0; i < cf.getNumSlots(); i++) {
        PhysicalFont pf = cf.getSlotFont(i);
        describeFont2D(pf);
      }
    } else {
      System.out.println("-> " + font);
    }
  }
}
