package org.webswing;

import com.manticore.tools.ZPNG;
import org.apache.commons.codec.binary.Base64;
import org.webswing.directdraw.DirectDraw;
import org.webswing.directdraw.toolkit.VolatileWebImageWrapper;
import org.webswing.directdraw.toolkit.WebImage;
import org.webswing.services.impl.ddutil.FastDirectDrawServicesAdapter;
import tools.jackson.databind.ObjectWriter;
import tools.jackson.databind.json.JsonMapper;

import javax.imageio.ImageIO;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class TestJsonGenerator {
  static DirectDraw dd = new DirectDraw(new FastDirectDrawServicesAdapter());

  public static void main(String[] args) throws IOException {
    RepaintManager.setCurrentManager(new RepaintManager() {
      @Override
      public Image getVolatileOffscreenBuffer(Component c, int proposedWidth, int proposedHeight) {
        return new VolatileWebImageWrapper(c.getGraphicsConfiguration().getImageCapabilities(),
            new WebImage(dd, proposedWidth, proposedHeight));
      }

      @Override
      public Image getOffscreenBuffer(Component c, int proposedWidth, int proposedHeight) {
        return new WebImage(dd, proposedWidth, proposedHeight);
      }
    });
    ArrayList<Test> json = new ArrayList<>();

    for (String testMethod : getTestMethods()) {
      Test test = new Test();
      test.name = testMethod;
      dd.resetConstantCache();
      draw(testMethod, test, dd);
      json.add(test);
    }
    ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
    writer.writeValue(new File(args[0]), json);
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        // topFrame.dispose();
        Frame[] frames = Frame.getFrames();
        System.out.printf("frames.length=%d\n", frames.length);
        for (Frame f : frames) {
          f.setVisible(false);
          f.dispose();
        }
      }
    });
  }

  private static void draw(String testMethod, Test test, DirectDraw dd) {
    try {
      boolean success = true;
      Method m = Tests.class.getDeclaredMethod(testMethod, Graphics2D.class, int.class);
      for (int j = 0; success; j++) {
        // image
        Image i = getImage(false);
        Graphics g = i.getGraphics();
        long start = System.currentTimeMillis();
        success = (Boolean) m.invoke(null, g, j);
        if (success) {
          test.originalImg.add(encodeImage((BufferedImage) i));
          test.originalRenderTime += (int) (System.currentTimeMillis() - start);
          test.originalRenderSize += Objects.requireNonNull(getPngImage((BufferedImage) i)).length;
          g.dispose();

          // webimage
          Image wi = getImage(true);
          Graphics g2 = wi.getGraphics();
          start = System.currentTimeMillis();
          m.invoke(null, g2, j);
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          ((WebImage) wi).extractReadOnlyWebImage(true).toMessage(dd).writeTo(baos);
          test.protoImg.add(encodeBytes(baos.toByteArray()));
          test.protoRenderTime += (int) (System.currentTimeMillis() - start);
          test.protoRenderSize += baos.size();
          g2.dispose();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static Image getImage(boolean web) {
    return web ? dd.createImage(500, 100)
        : new BufferedImage(500, 100, BufferedImage.TYPE_INT_ARGB);
  }

  public static Image getImage(boolean web, int width, int height) {
    return web ? dd.createImage(width, height)
        : new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
  }

  private static final JsonMapper mapper = new JsonMapper();

  public static String encodeImage(BufferedImage window) {
    return Base64.encodeBase64String(getPngImage(window));
  }

  public static String encodeBytes(byte[] bytes) {
    return Base64.encodeBase64String(bytes);
  }

  public static byte[] getPngImage(BufferedImage imageContent) {
    return ZPNG.encode(imageContent, imageContent.getColorModel().hasAlpha() ? 4 : 3, 2);
  }

  public static String[] getTestMethods() {
    List<String> result = new ArrayList<String>();
    for (Method m : Tests.class.getDeclaredMethods()) {
      if (m.getName().endsWith("Test")) {
        result.add(m.getName());
      }
    }
    Collections.sort(result);
    return result.toArray(new String[result.size()]);
  }

}
