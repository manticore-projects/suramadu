package org.webswing.directdraw;

import com.manticore.tools.ZPNG;
import org.apache.commons.codec.binary.Base64;
import org.webswing.directdraw.util.ImageConsumerAdapter;

import javax.imageio.ImageIO;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.awt.image.SinglePixelPackedSampleModel;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DirectDrawServicesAdapter {

  private Map<String, String> fontFileMap;

  public byte[] getPngImage(BufferedImage imageContent) {
    return ZPNG.encode(imageContent, imageContent.getColorModel().hasAlpha() ? 4 : 3, 2);
  }

  public long getSignature(byte[] data) {
    return Arrays.hashCode(data);
  }

  public String encodeBytes(byte[] bytes) {
    return Base64.encodeBase64String(bytes);
  }

  /**
   * Identity hash over dimensions + every pixel.
   *
   * <p>
   * Fast path: {@code TYPE_INT_*} {@link BufferedImage}s (the overwhelmingly common case) are
   * hashed directly from the backing {@code int[]} — no {@link java.awt.image.ImageProducer}
   * machinery, no per-pixel virtual calls, no color-model conversion. Sub-images (shared
   * {@code DataBuffer} with offset/stride) are handled via the sample-model translation.
   *
   * <p>
   * Fallback: producer-based hashing for exotic image types. NOTE the previous producer adapter
   * only hashed {@code scansize} samples PER {@code setPixels} CALL — when a producer delivered
   * several rows in one call, everything after the first row was ignored, so two images differing
   * only below the first row COLLIDED and the wrong cached image could be drawn. The fallback now
   * covers every row. (Hash values differ from previous releases; the cache is per-process, so
   * nothing persists across restarts.)
   */
  public long computeHash(Image subImage) {
    if (subImage instanceof BufferedImage bi
        && bi.getRaster().getDataBuffer() instanceof DataBufferInt dbi
        && bi.getSampleModel() instanceof SinglePixelPackedSampleModel sm) {
      final Raster r = bi.getRaster();
      final int w = bi.getWidth();
      final int h = bi.getHeight();
      final int stride = sm.getScanlineStride();
      final int[] data = dbi.getData();
      final int base =
          dbi.getOffset() - r.getSampleModelTranslateY() * stride - r.getSampleModelTranslateX();
      long hash = 31L * w + h;
      for (int y = 0; y < h; y++) {
        int idx = base + y * stride;
        for (int x = 0; x < w; x++) {
          hash = hash * 31 + data[idx + x];
        }
      }
      return hash;
    }
    ImageConsumerAdapter ic = new ImageConsumerAdapter() {
      @Override
      public void setPixels(int x, int y, int w, int h, ColorModel model, int[] pixels, int off,
          int scansize) {
        for (int row = 0; row < h; row++) {
          int s = off + row * scansize;
          for (int i = s; i < s + w; i++) {
            hash = hash * 31 + pixels[i];
          }
        }
      }

      @Override
      public void setPixels(int x, int y, int w, int h, ColorModel model, byte[] pixels, int off,
          int scansize) {
        for (int row = 0; row < h; row++) {
          int s = off + row * scansize;
          for (int i = s; i < s + w; i++) {
            hash = hash * 31 + pixels[i];
          }
        }
      }

      @Override
      public void setDimensions(int width, int height) {
        hash = hash * 31 + width;
        hash = hash * 31 + height;
      }
    };
    subImage.getSource().startProduction(ic);
    return ic.getHash();
  }


  public String getFileForFont(Font font) {
    if (font != null) {
      Map<String, String> map = getFontFileMap();
      String fontName = font.getFontName().toLowerCase();
      String file = map.get(fontName);
      return file;
    }
    return null;
  }

  private Map<String, String> getFontFileMap() {
    if (fontFileMap == null) {
      fontFileMap = new HashMap<>();
      try {
        String customFontConfigFile = System.getProperty("sun.awt.fontconfig");
        if (customFontConfigFile != null && new File(customFontConfigFile).canRead()) {
          File f = new File(customFontConfigFile);
          try (Scanner s = new Scanner(f, StandardCharsets.UTF_8)) {
            while (s.hasNextLine()) {
              String line = s.nextLine();
              if (line.startsWith("#@@")) {
                String[] values = line.substring(3).split("=");
                if (values.length == 2) {
                  fontFileMap.put(values[0].toLowerCase(), values[1]);
                }
              }

            }
          }
        }
      } catch (IOException e) {
        System.err.println("Failed to initialize font file map for DirectDraw rendering.");
        e.printStackTrace();
      }
    }
    return fontFileMap;
  }

}
