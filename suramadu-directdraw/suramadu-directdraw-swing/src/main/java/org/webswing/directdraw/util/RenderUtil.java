package org.webswing.directdraw.util;

import org.webswing.directdraw.model.DrawConstant;
import org.webswing.directdraw.model.DrawInstruction;
import org.webswing.directdraw.model.GlyphListConst.StringConstValue;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RenderUtil {

  public static BufferedImage render(List<DrawInstruction> newInstructions, Dimension size) {
    BufferedImage result = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
    render(result, newInstructions);
    return result;
  }

  public static RenderContext createRenderContext(BufferedImage result) {
    return new RenderContext(result);

  }

  public static RenderContext createRenderContext(BufferedImage fallbackImage,
      RenderingHints hints) {
    return new RenderContext(fallbackImage, hints);
  }

  public static class RenderContext {
    private final RenderingHints hints;
    private Map<Integer, Graphics2D> map = new HashMap<Integer, Graphics2D>();
    private Graphics2D currentGraphics;
    private BufferedImage result;

    RenderContext(BufferedImage image) {
      this(image, null);
    }

    public RenderContext(BufferedImage image, RenderingHints hints) {
      this.result = image;
      this.hints = hints;
    }

    public void interpret(DrawInstruction di) {
      switch (di.getInstruction()) {
        case GRAPHICS_CREATE:
          currentGraphics = iprtGraphicsCreate(result, di, map, hints);
          break;
        case GRAPHICS_DISPOSE:
          Graphics2D graphics = map.remove(getValue(0, di, Integer.class));
          if (graphics != null) {
            graphics.dispose();
          }
          break;
        case GRAPHICS_SWITCH:
          currentGraphics = map.get(getValue(0, di, Integer.class));
          break;
        case FILL:
          iprtFill(currentGraphics, di);
          break;
        case DRAW:
          iprtDraw(currentGraphics, di);
          break;
        case DRAW_STRING:
          iprtDrawString(currentGraphics, di);
          break;
        case DRAW_WEBIMAGE:
          iprtDrawWebImage(currentGraphics, di);
          break;
        case DRAW_IMAGE:
          iprtDrawImage(currentGraphics, di);
          break;
        case COPY_AREA:
          iprtCopyArea(currentGraphics, di, result);
          break;
        case SET_COMPOSITE:
          iprtSetComposite(currentGraphics, di);
          break;
        case SET_FONT:
          iprtSetFont(currentGraphics, di);
          break;
        case SET_PAINT:
          iprtSetPaint(currentGraphics, di);
          break;
        case SET_STROKE:
          iprtSetStroke(currentGraphics, di);
          break;
        case TRANSFORM:
          iprtTransform(currentGraphics, di);
          break;
        case DRAW_GLYPH_LIST:
          iprtDrawGlyphList(currentGraphics, di);
          break;
        default:
          break;
      }
    }

    public void interpret(List<DrawInstruction> instructions) {
      for (DrawInstruction di : instructions) {
        interpret(di);
      }
    }

    public void dispose() {
      for (Graphics2D g2d : map.values()) {
        g2d.dispose();
      }
    }
  }

  public static BufferedImage render(BufferedImage result, List<DrawInstruction> instructions) {
    RenderContext ctx = new RenderContext(result);
    ctx.interpret(instructions);
    ctx.dispose();
    return result;
  }

  private static void iprtDrawImage(Graphics2D g, DrawInstruction di) {
    BufferedImage image = getImage(0, di);
    AffineTransform transform = getValue(1, di);
    Rectangle2D crop = getValue(2, di);
    Color bgcolor = getValue(3, di);
    Shape clip = getValue(4, di);
    g.setClip(clip);
    AffineTransform original = g.getTransform();
    if (transform != null) {
      g.transform(transform);
    }
    if (crop == null) {
      g.drawImage(image, 0, 0, bgcolor, null);
    } else {
      g.drawImage(image, 0, 0, (int) crop.getWidth(), (int) crop.getHeight(), (int) crop.getX(),
          (int) crop.getY(), (int) crop.getMaxX(), (int) crop.getMaxY(), bgcolor, null);
    }
    g.setTransform(original);
  }

  private static void iprtDrawWebImage(Graphics2D g, DrawInstruction di) {
    BufferedImage image = di.getImage();
    AffineTransform transform = getValue(0, di);
    Rectangle2D crop = getValue(1, di);
    Color bgColor = getValue(2, di);
    Shape clip = getShape(3, di);
    g.setClip(clip);
    AffineTransform original = g.getTransform();
    if (transform != null) {
      g.transform(transform);
    }
    if (crop == null) {
      g.drawImage(image, 0, 0, bgColor, null);
    } else {
      g.drawImage(image, 0, 0, (int) crop.getWidth(), (int) crop.getHeight(), (int) crop.getX(),
          (int) crop.getY(), (int) crop.getMaxX(), (int) crop.getMaxY(), bgColor, null);
    }
    g.setTransform(original);
  }

  private static void iprtDrawString(Graphics2D g, DrawInstruction di) {
    int[] points = getValue(1, di);
    Shape clip = getShape(2, di);
    g.setClip(clip);
    g.drawString(getValue(0, di, String.class), points[0], points[1]);
  }

  private static void iprtDrawGlyphList(Graphics2D g, DrawInstruction di) {
    StringConstValue strVal = getValue(0, di);
    Shape clip = getShape(1, di);
    g.setClip(clip);
    g.setFont(strVal.getFont());
    g.setTransform(strVal.getTransform());
    g.drawString(strVal.getString(), (float) strVal.getX(), (float) strVal.getY());
  }

  private static void iprtCopyArea(Graphics2D g, DrawInstruction di, BufferedImage result) {
    int[] points = getValue(0, di);
    // result is both source and destination here. Drawing an image onto its own raster corrupts
    // overlapping regions (the blit reads pixels it has already overwritten in the same pass),
    // which smears the leading band on any scroll-style copyArea. Read from a stable snapshot so
    // source and destination are distinct rasters; output is otherwise identical.
    BufferedImage src =
        new BufferedImage(result.getWidth(), result.getHeight(), BufferedImage.TYPE_INT_ARGB);
    Graphics2D sg = src.createGraphics();
    sg.drawImage(result, 0, 0, null);
    sg.dispose();
    g.setClip(getShape(1, di));
    AffineTransform original = g.getTransform();
    g.setTransform(new AffineTransform(1, 0, 0, 1, 0, 0));
    g.clipRect(points[0], points[1], points[2], points[3]);
    g.translate(points[4], points[5]);
    g.drawImage(src, 0, 0, null);
    g.setTransform(original);
  }

  private static void iprtDraw(Graphics2D g, DrawInstruction di) {
    if (di.getArg(1) != DrawConstant.NULL_CONST) {
      g.setClip(getShape(1, di));
    }
    if (di.getArg(0) != DrawConstant.NULL_CONST) {
      g.draw(getShape(0, di));
    }
  }

  private static void iprtFill(Graphics2D g, DrawInstruction di) {
    g.setClip(getShape(1, di));
    g.fill(getShape(0, di));
  }

  private static void iprtSetComposite(Graphics2D currentg, DrawInstruction di) {
    Composite composite = getValue(0, di, Composite.class);
    if (composite instanceof AlphaComposite) {
      currentg.setComposite(composite);
    } else if (composite instanceof XorModeComposite modeComposite) {
      currentg.setXORMode(modeComposite.getXorColor());
    }
  }

  private static void iprtSetFont(Graphics2D currentg, DrawInstruction di) {
    currentg.setFont(getValue(0, di, Font.class));
  }

  private static void iprtTransform(Graphics2D currentg, DrawInstruction di) {
    currentg.transform(getValue(0, di, AffineTransform.class));
  }

  private static void iprtSetStroke(Graphics2D currentg, DrawInstruction di) {
    currentg.setStroke(getValue(0, di, Stroke.class));
  }

  private static void iprtSetPaint(Graphics2D currentg, DrawInstruction di) {
    currentg.setPaint(getPaint(0, di));
  }

  private static Graphics2D iprtGraphicsCreate(BufferedImage result, DrawInstruction di,
      Map<Integer, Graphics2D> gmap, RenderingHints hints) {
    AffineTransform transform = getValue(1, di);
    Stroke stroke = getValue(2, di);
    Composite composite = getValue(3, di);
    Paint paint = getPaint(4, di);
    Font font = getValue(5, di);
    Graphics2D g = result.createGraphics();
    if (hints != null) {
      g.setRenderingHints(hints);
    }
    if (transform != null) {
      g.setTransform(transform);
    }
    if (stroke != null) {
      g.setStroke(stroke);
    }
    if (composite != null) {
      g.setComposite(composite);
    }
    if (paint != null) {
      g.setPaint(paint);
    }
    if (font != null) {
      g.setFont(font);
    }
    gmap.put(getValue(0, di, Integer.class), g);
    return g;
  }

  @SuppressWarnings({"unchecked"})
  private static <T> T getValue(int index, DrawInstruction instruction, Class<? extends T> cls) {
    return (T) instruction.getArg(index).getValue();
  }

  @SuppressWarnings("unchecked")
  private static <T> T getValue(int index, DrawInstruction instruction) {
    return (T) instruction.getArg(index).getValue();
  }

  private static Paint getPaint(int index, DrawInstruction instruction) {
    return getValue(index, instruction);
  }

  private static Shape getShape(int index, DrawInstruction instruction) {
    return getValue(index, instruction);
  }

  public static BufferedImage getImage(int index, DrawInstruction instruction) {
    try {
      return ImageIO.read(new ByteArrayInputStream((byte[]) getValue(index, instruction)));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static ImageObserver waitForImage(Image i) {
    try {
      Graphics imageLoaderG = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB).getGraphics();
      ImageObserver observer = new ImageObserver() {
        @Override
        public synchronized boolean imageUpdate(Image img, int infoflags, int x, int y, int width,
            int height) {
          if ((infoflags & (ImageObserver.ALLBITS | ImageObserver.FRAMEBITS)) != 0) {
            notifyAll();
          }
          return true;
        }
      };

      synchronized (observer) {
        if (!imageLoaderG.drawImage(i, 0, 0, observer)) {
          try {
            observer.wait(300);
          } catch (InterruptedException e) {
            // ignore
          }
        }
      }
      return observer;
    } catch (Exception e) {
      return null;
    }
  }

  private RenderUtil() {}
}
