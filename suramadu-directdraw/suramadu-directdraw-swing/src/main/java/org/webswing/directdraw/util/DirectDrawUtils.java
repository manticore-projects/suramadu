package org.webswing.directdraw.util;

import org.webswing.directdraw.DirectDraw;
import org.webswing.directdraw.model.DrawConstant;
import org.webswing.directdraw.model.DrawInstruction;
import org.webswing.directdraw.model.TransformConst;
import org.webswing.directdraw.proto.Directdraw.DrawInstructionProto.InstructionProto;
import sun.java2d.SunGraphics2D;
import sun.java2d.loops.FontInfo;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.*;
import java.io.File;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.*;
import java.util.List;

@SuppressWarnings("restriction")
public class DirectDrawUtils {

  public static final Map<String, String> WEB_FONTS = Map.of("Dialog", "sans-serif", "DialogInput",
      "monospace", "Serif", "serif", "SansSerif", "sans-serif", "Monospaced", "monospace");
  private static final String DELIMITER = "|";

  /**
   * Per-thread font-measuring graphics. Previously a single shared static {@link SunGraphics2D} was
   * mutated ({@code setFont}) and read ({@code getFontInfo}) without synchronisation, so concurrent
   * callers raced on its font state and could observe a {@link FontInfo} for the wrong font. Each
   * thread now gets its own 1x1 helper, which is correct and contention-free.
   */
  private static final ThreadLocal<SunGraphics2D> sgHelper = ThreadLocal.withInitial(() -> {
    SunGraphics2D sg =
        (SunGraphics2D) new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics();
    sg.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    return sg;
  });

  public static FontInfo getFontInfo(Font font) {
    SunGraphics2D sg = sgHelper.get();
    sg.setFont(font);
    return sg.getFontInfo();
  }

  /**
   * there is a bug in the jdk 1.6 which makes Font.getAttributes() not work correctly. The method
   * does not return all values. What we dow here is using the old JDK 1.5 method.
   *
   * @param font font
   *
   * @return Attributes of font
   */
  // @SuppressWarnings({ "rawtypes", "unchecked" })
  public static Map<? extends Attribute, ?> getAttributes(Font font) {
    Map<Attribute, Object> result = new HashMap<Attribute, Object>(7, (float) 0.9);
    result.put(TextAttribute.TRANSFORM, font.getTransform());
    result.put(TextAttribute.FAMILY, font.getName());
    result.put(TextAttribute.SIZE, font.getSize2D());
    result.put(TextAttribute.WEIGHT, (font.getStyle() & Font.BOLD) != 0 ? TextAttribute.WEIGHT_BOLD
        : TextAttribute.WEIGHT_REGULAR);
    result.put(TextAttribute.POSTURE,
        (font.getStyle() & Font.ITALIC) != 0 ? TextAttribute.POSTURE_OBLIQUE
            : TextAttribute.POSTURE_REGULAR);
    result.put(TextAttribute.SUPERSCRIPT, 0);
    result.put(TextAttribute.WIDTH, 1f);
    return result;
  }

  public static BufferedImage createBufferedImage(Image image, ImageObserver observer, Color bkg) {
    if ((bkg == null) && (image instanceof BufferedImage bufferedImage)) {
      return bufferedImage;
    }
    BufferedImage bufferedImage =
        new BufferedImage(image.getWidth(observer), image.getHeight(observer),
            bkg == null ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);

    Graphics g = bufferedImage.getGraphics();
    if (bkg == null) {
      g.drawImage(image, 0, 0, observer);
    } else {
      g.drawImage(image, 0, 0, bkg, observer);
    }
    return bufferedImage;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static BufferedImage createBufferedImage(RenderedImage img) {
    if (img instanceof BufferedImage image) {
      return image;
    }
    ColorModel cm = img.getColorModel();
    int width = img.getWidth();
    int height = img.getHeight();
    WritableRaster raster = cm.createCompatibleWritableRaster(width, height);
    boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
    Hashtable properties = new Hashtable();
    String[] keys = img.getPropertyNames();
    if (keys != null) {
      for (int i = 0; i < keys.length; i++) {
        properties.put(keys[i], img.getProperty(keys[i]));
      }
    }
    BufferedImage result = new BufferedImage(cm, raster, isAlphaPremultiplied, properties);
    img.copyData(raster);
    return result;
  }

  public static int findFirstVisibleIndex(String s, double x, double y, Shape clip,
      FontMetrics fm) {
    if (clip == null) {
      return 0;
    }
    final double[] cum = cumulativeAdvances(s, fm);
    final Rectangle2D clipBounds = clip.getBounds2D();
    final double yTop = y - fm.getAscent();
    final double h = fm.getDescent() + fm.getAscent();
    final int len = s.length();
    int idx = 0;
    int idxMin = 0;
    int idxMax = len;
    while (true) {
      VisibilityHints hints = getVisibilityHintsForIndex(idx, len, cum, x, yTop, h, clipBounds);
      if (hints.leftVisible) {
        idxMax = idx - 1;
      } else {
        if (hints.indexVisible) {
          return idx;
        } else {
          if (hints.rightVisible) {
            idxMin = idx + 1;
          } else {
            return len; // invalid option
          }
        }
      }
      idx = idxMin + (idxMax - idxMin) / 2;
    }
  }

  public static int findLastVisibleIndex(int firstIndex, String s, double x, double y, Shape clip,
      FontMetrics fm) {
    final int len = s.length();
    if (clip == null || firstIndex == len) {
      return len;
    }
    final double[] cum = cumulativeAdvances(s, fm);
    final Rectangle2D clipBounds = clip.getBounds2D();
    final double yTop = y - fm.getAscent();
    final double h = fm.getDescent() + fm.getAscent();
    int idx = len;
    int idxMin = firstIndex;
    int idxMax = len;

    while (true) {
      VisibilityHints hints = getVisibilityHintsForIndex(idx, len, cum, x, yTop, h, clipBounds);
      if (hints.rightVisible) {
        idxMin = idx + 1;
      } else {
        if (hints.indexVisible) {
          return Math.min(idx + 1, len);
        } else {
          if (hints.leftVisible) {
            idxMax = idx - 1;
          } else {
            return len; // invalid option
          }
        }
      }
      idx = idxMin + (idxMax - idxMin) / 2;
    }
  }

  /**
   * Cumulative glyph advances as doubles: {@code cum[i]} is the x-advance from the string origin to
   * the start of glyph {@code i} (so {@code cum[0] == 0} and {@code cum[s.length()]} is the full
   * advance). Taken from a {@link GlyphVector}, i.e. the true fractional positions the text is laid
   * out at, computed once per string (O(n)).
   *
   * <p>
   * This deliberately does not sum per-character {@link FontMetrics#charWidth}: under fractional
   * metrics each char's advance rounds independently, so the running sum drifts from the real
   * layout by up to ~half a pixel per character and can push the last glyph just past a tight clip
   * edge, dropping a trailing character. Double precision from the glyph vector has no such drift.
   *
   * <p>
   * Falls back to exact per-prefix {@link FontMetrics#stringWidth} only when the glyph count does
   * not match the character count (complex shaping / surrogate pairs), where indexing the glyph
   * vector by character index would be wrong; such strings are short in practice.
   */
  private static double[] cumulativeAdvances(String s, FontMetrics fm) {
    final int len = s.length();
    double[] cum = new double[len + 1];
    GlyphVector gv = fm.getFont().createGlyphVector(fm.getFontRenderContext(), s);
    if (gv.getNumGlyphs() == len) {
      for (int i = 0; i <= len; i++) {
        cum[i] = gv.getGlyphPosition(i).getX();
      }
    } else {
      for (int i = 1; i <= len; i++) {
        cum[i] = fm.stringWidth(s.substring(0, i));
      }
    }
    return cum;
  }

  /**
   * O(1) visibility hint for a glyph index, using precomputed cumulative advances and the clip's
   * bounding box. Glyph spans are tested against the clip's {@link Rectangle2D} bounds rather than
   * the clip shape itself: this method only decides which glyphs are worth serialising, and the
   * true clip is re-applied at render time (see {@code RenderUtil.iprtDrawString}), so testing the
   * bounds is a safe over-approximation (bounds contains shape, so no visible glyph is ever
   * dropped) while avoiding {@code Path2D.contains} / {@code rectCrossings}, which dominated the
   * EDT under DirectDraw.
   */
  private static VisibilityHints getVisibilityHintsForIndex(int index, int len, double[] cum,
      double x, double yTop, double h, Rectangle2D clipBounds) {
    VisibilityHints result = new VisibilityHints();

    double wL = cum[index]; // advance of s[0..index)
    double xL = x;
    if (clipBounds.contains(xL, yTop, wL, h) || clipBounds.intersects(xL, yTop, wL, h)) {
      result.leftVisible = true;
    }

    int iEnd = Math.min(index + 1, len);
    double wI = cum[iEnd] - cum[index]; // advance of s[index]
    wI = wI == 0 ? 0.0001 : wI; // clip.contains always returns false if wI is 0 (causing accent
    // thai
    // chars not render on top of last char)
    double xI = xL + wL;
    if (clipBounds.contains(xI, yTop, wI, h) || clipBounds.intersects(xI, yTop, wI, h)) {
      result.indexVisible = true;
    }

    double wR = cum[len] - cum[iEnd]; // advance of s[index+1..end)
    double xR = xI + wI;
    if (clipBounds.contains(xR, yTop, wR, h) || clipBounds.intersects(xR, yTop, wR, h)) {
      result.rightVisible = true;
    }
    return result;
  }

  static class VisibilityHints {
    public boolean leftVisible;
    public boolean rightVisible;
    public boolean indexVisible;
  }

  private static class GraphicsStatus {
    AffineTransform tx;
    DrawConstant<?> stroke;
    DrawConstant<?> composite;
    DrawConstant<?> paint;
    DrawConstant<?> font;

    void reset() {
      tx = null;
      stroke = null;
      composite = null;
      paint = null;
      font = null;
    }
  }

  public static void optimizeInstructions(DirectDraw ctx, List<DrawInstruction> instructions) {

    // step 1. group consequent transformations
    final GraphicsStatus mergedStatus = new GraphicsStatus();
    DrawInstruction graphicsCreate = null;
    Map<Integer, DrawInstruction> graphicsCreateMap = new HashMap<Integer, DrawInstruction>();
    List<DrawInstruction> newInstructions = new ArrayList<DrawInstruction>();
    for (DrawInstruction current : instructions) {
      if (current.getInstruction().equals(InstructionProto.TRANSFORM)) {
        if (mergedStatus.tx == null) {
          mergedStatus.tx = ((TransformConst) current.getArg(0)).getValue();
        } else {
          mergedStatus.tx.concatenate(((TransformConst) current.getArg(0)).getValue());
        }
      } else if (current.getInstruction().equals(InstructionProto.SET_STROKE)) {
        mergedStatus.stroke = current.getArg(0);
      } else if (current.getInstruction().equals(InstructionProto.SET_COMPOSITE)) {
        mergedStatus.composite = current.getArg(0);
      } else if (current.getInstruction().equals(InstructionProto.SET_PAINT)) {
        mergedStatus.paint = current.getArg(0);
      } else if (current.getInstruction().equals(InstructionProto.SET_FONT)) {
        mergedStatus.font = current.getArg(0);
      } else if (current.getInstruction().equals(InstructionProto.GRAPHICS_CREATE)
          || current.getInstruction().equals(InstructionProto.GRAPHICS_SWITCH)) {
        boolean isGraphicsCreateInst =
            current.getInstruction().equals(InstructionProto.GRAPHICS_CREATE);
        if (graphicsCreate != null) {
          if (!equalStatus(graphicsCreate, mergedStatus)) {
            graphicsCreate = createGraphics(ctx, graphicsCreate.getArg(0), mergedStatus);
            mergedStatus.reset();
          }
          graphicsCreateMap.put(graphicsCreate.getArg(0).getId(), graphicsCreate);
        } else if (isGraphicsCreateInst) {
          setGraphicsStatus(ctx, newInstructions, mergedStatus);
          mergedStatus.reset();
        }
        graphicsCreate =
            isGraphicsCreateInst ? current : graphicsCreateMap.get(current.getArg(0).getId());
        if (graphicsCreate != null) {
          mergedStatus.tx = ((TransformConst) graphicsCreate.getArg(1)).getValue();
          mergedStatus.stroke = graphicsCreate.getArg(2);
          mergedStatus.composite = graphicsCreate.getArg(3);
          mergedStatus.paint = graphicsCreate.getArg(4);
          mergedStatus.font = graphicsCreate.getArg(5);
        } else {
          // if graphics switch instruction and the create instruction already in result array
          // then add all status change for old graphics and add the switch inst
          setGraphicsStatus(ctx, newInstructions, mergedStatus);
          mergedStatus.reset();
          newInstructions.add(current);
        }
      } else if (current.getInstruction().equals(InstructionProto.GRAPHICS_DISPOSE)) {
        mergedStatus.reset();
        if (graphicsCreate == null) {
          newInstructions.add(current);
        } else {
          graphicsCreate = null;
        }
      } else {
        if (graphicsCreate != null) {
          if (!equalStatus(graphicsCreate, mergedStatus)) {
            graphicsCreate = createGraphics(ctx, graphicsCreate.getArg(0), mergedStatus);
          }
          newInstructions.add(graphicsCreate);
          graphicsCreateMap.remove(graphicsCreate.getArg(0).getId());
          graphicsCreate = null;
        } else {
          setGraphicsStatus(ctx, newInstructions, mergedStatus);
        }
        mergedStatus.reset();
        newInstructions.add(current);
      }
    }
    // if transform is last instruction, it will be omitted from the result
    instructions.clear();
    instructions.addAll(newInstructions);
  }

  private static boolean equalStatus(DrawInstruction graphicsCreate, GraphicsStatus status) {
    return equals(((TransformConst) graphicsCreate.getArg(1)).getValue(), status.tx)
        && equals(graphicsCreate.getArg(2), status.stroke)
        && equals(graphicsCreate.getArg(3), status.composite)
        && equals(graphicsCreate.getArg(4), status.paint)
        && equals(graphicsCreate.getArg(5), status.font);
  }

  private static boolean equals(Object a, Object b) {
    return Objects.equals(a, b);
  }

  private static DrawInstruction createGraphics(DirectDraw ctx, DrawConstant<?> id,
      GraphicsStatus status) {
    return ctx.getInstructionFactory().createGraphics(id, new TransformConst(ctx, status.tx),
        status.stroke, status.composite, status.paint, status.font);
  }

  private static void setGraphicsStatus(DirectDraw ctx, List<DrawInstruction> instructions,
      GraphicsStatus status) {
    if (status.tx != null && !status.tx.isIdentity()) {
      instructions.add(ctx.getInstructionFactory().transform(status.tx));
    }
    if (status.stroke != null) {
      instructions.add(new DrawInstruction(InstructionProto.SET_STROKE, status.stroke));
    }
    if (status.composite != null) {
      instructions.add(new DrawInstruction(InstructionProto.SET_COMPOSITE, status.composite));
    }
    if (status.paint != null) {
      instructions.add(new DrawInstruction(InstructionProto.SET_PAINT, status.paint));
    }
    if (status.font != null) {
      instructions.add(new DrawInstruction(InstructionProto.SET_FONT, status.font));
    }
  }

  public static int hashCode(double value) {
    long bits = Double.doubleToLongBits(value);
    return (int) (bits ^ (bits >>> 32));
  }

  public static int hashCode(float value) {
    return Float.floatToIntBits(value);
  }

  public static int hashCode(boolean value) {
    return value ? 1231 : 1237;
  }

  public static String fontInfoDescriptor(Font f, FontInfo fi) {
    StringBuilder sb = new StringBuilder(f.getFontName());
    sb.append(DELIMITER).append(f.getStyle());
    sb.append(DELIMITER).append(Arrays.toString(fi.devTx));
    sb.append(DELIMITER).append(Arrays.toString(fi.glyphTx));
    sb.append(DELIMITER).append(fi.pixelHeight);
    sb.append(DELIMITER);
    return sb.toString();
  }

  public static byte[] toPNG(DirectDraw ctx, byte[] gray, int w, int h) {
    int[] imagePixels = new int[w * h];
    for (int i = 0; i < imagePixels.length; i++) {
      imagePixels[i] = gray[i] << 24;
    }
    BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    image.setRGB(0, 0, w, h, imagePixels, 0, w);
    image.getRGB(0, 0, w, h, null, 0, w);
    return ctx.getServices().getPngImage(image);

  }

  public static String fontNameFromFile(String fileName, Font font) {
    if (fileName != null) {
      File f = new File(fileName);
      if (f.exists()) {
        if (Boolean.getBoolean(DirectDraw.FONTS_PROVIDED)) {
          return new File(fileName).getName();
        } else {
          String name = fileName.hashCode() + new File(fileName).getName();
          name = name.length() > 20 ? name.substring(0, 20) : name; // IE will ignore the font if
          // name is longer than 31 chars
          return name;
        }
      } else {
        return fileName;
      }
    } else {
      return DirectDrawUtils.WEB_FONTS.get(font.getFamily());
    }
  }

  private DirectDrawUtils() {}
}
