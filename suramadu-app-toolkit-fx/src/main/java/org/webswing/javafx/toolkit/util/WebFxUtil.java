package org.webswing.javafx.toolkit.util;

import com.sun.glass.ui.Pixels;
import com.sun.javafx.geom.RectBounds;
import org.webswing.util.AppLogger;
import org.webswing.util.NamedThreadFactory;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by vikto on 06-Mar-17.
 */
public class WebFxUtil {

  private static final int SQ = 45;
  private static final int threadCount = Runtime.getRuntime().availableProcessors();
  private static final ExecutorService processorPool =
      Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(),
          NamedThreadFactory.getInstance("Webswing JavaFx Pixel processor"));

  public static BufferedImage pixelsToImage(BufferedImage image, Pixels pixels) {
    Pixels p = pixels;
    if (p != null) {
      Buffer data = p.getPixels();
      int width = p.getWidth();
      int height = p.getHeight();
      if (p.getBytesPerComponent() == 1) {
        ByteBuffer bytes = (ByteBuffer) data;
        return paintByte(width, height, bytes, image);
      } else {
        IntBuffer ints = (IntBuffer) data;
        return paintInt(width, height, ints, image);
      }
    }
    return null;
  }

  private static BufferedImage paintInt(int width, int height, IntBuffer ints,
      BufferedImage image) {
    if (image == null || image.getWidth() != width || image.getHeight() != height
        || image.getType() != BufferedImage.TYPE_INT_ARGB) {
      image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }
    DataBufferInt dataBuf = (DataBufferInt) image.getRaster().getDataBuffer();
    ints.get(dataBuf.getData(), 0, width * height);
    return image;
  }

  private static BufferedImage paintByte(int width, int height, ByteBuffer bytes,
      BufferedImage image) {
    if (image == null || image.getWidth() != width || image.getHeight() != height
        || image.getType() != BufferedImage.TYPE_4BYTE_ABGR) {
      image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
    }
    DataBufferByte dataBuf = (DataBufferByte) image.getRaster().getDataBuffer();
    bytes.get(dataBuf.getData(), 0, width * height);
    return image;
  }

  public static Set<RectBounds> findUpdateAreas(BufferedImage image, BufferedImage previous,
      Set<RectBounds> tmpBounds) {
    int width = image.getWidth();
    int height = image.getHeight();
    List<RectBounds> toCompare = new ArrayList<>();
    for (int c = 0; c <= width / SQ; c++) {
      for (int r = 0; r <= height / SQ; r++) {
        int x = Math.min(c * SQ, width - 1);
        int y = Math.min(r * SQ, height - 1);
        int w = Math.min(x + SQ, width);
        int h = Math.min(y + SQ, height);
        if (tmpBounds != null) {
          for (RectBounds bound : tmpBounds) {
            if (bound.intersects(x, y, w, h)) {
              toCompare.add(new RectBounds(x, y, w, h));
            }
          }
        } else {
          toCompare.add(new RectBounds(x, y, w, h));
        }
      }
    }
    List<Future<List<RectBounds>>> futures = new ArrayList<>();
    for (int i = 0; i < threadCount; i++) {
      final int currentoffset = i;
      futures.add(processorPool.submit(new Callable<List<RectBounds>>() {
        int offset = currentoffset;

        @Override
        public List<RectBounds> call() throws Exception {
          List<RectBounds> diff = new ArrayList<>();

          int[] imgData = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
          int[] prevData = ((DataBufferInt) previous.getRaster().getDataBuffer()).getData();
          for (int j = offset; j < toCompare.size(); j = j + threadCount) {
            RectBounds currentArea = toCompare.get(j);

            areaCompare: for (int x = (int) currentArea.getMinX(); x < currentArea.getMaxX(); x++) {
              for (int y = (int) currentArea.getMinY(); y < currentArea.getMaxY(); y++) {
                int offset = y * width + x;
                try {
                  if (imgData[offset] != prevData[offset]) {
                    diff.add(currentArea);
                    break areaCompare;
                  }
                } catch (Exception e) {
                  AppLogger.error("failed to comapare: offset" + offset + "|" + x + "|" + y + "|"
                      + width + "|" + height + "| size" + imgData.length);
                }
              }
            }
          }
          return diff;
        }
      }));
    }

    // collect results:
    Set<RectBounds> completeResult = new HashSet<>();
    for (Future<List<RectBounds>> f : futures) {
      try {
        completeResult.addAll(f.get());
      } catch (Exception e) {
        AppLogger.error("failed to compare pixels", e);
      }
    }
    return completeResult;
  }

  private WebFxUtil() {}

}
