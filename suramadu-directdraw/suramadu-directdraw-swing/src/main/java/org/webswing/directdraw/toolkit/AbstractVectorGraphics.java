// Copyright 2000-2007, FreeHEP
// Modification to original: merged with AbstractVectorGraphicsIO
package org.webswing.directdraw.toolkit;

import org.webswing.directdraw.util.DirectDrawUtils;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.TextLayout;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderContext;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.Map;

/**
 * This class implements all conversions from integer to double as well as a few other convenience
 * functions. It also handles the different drawSymbol and fillSymbol methods and print colors. The
 * drawing of framed strings is broken down to lower level methods.
 *
 * @author Simon Fischer
 * @author Mark Donszelmann
 * @author Steffen Greiffenberg
 * @author Charles Loomis
 * @version $Id: AbstractVectorGraphics.java 10516 2007-02-06 21:11:19Z duns $
 */
public abstract class AbstractVectorGraphics extends Graphics2D {

  private static final Font DEFAULT_FONT = new Font("Dialog", 0, 12);

  private Dimension size;

  private Color backgroundColor;

  private Stroke currentStroke;

  private Color currentColor;

  private Paint currentPaint;

  private Font currentFont;

  private RenderingHints hints;

  private Composite currentComposite;

  private Rectangle deviceClip;

  private Shape userClip; // Untransformed clipping Area defined by the user

  private AffineTransform currentTransform;

  private AffineTransform oldTransform = new AffineTransform(); // only for use in writeSetTransform
                                                                // to calculate the difference.

  /*
   * ================================================================================
   * 1. Constructors & Factory Methods
   * ================================================================================
   */

  /**
   * Constructs a Graphics context with the following graphics state:
   * <UL>
   * <LI>Paint: black
   * <LI>Font: Dailog, Plain, 12pt
   * <LI>Stroke: Linewidth 1.0; No Dashing; Miter Join Style; Miter Limit 10; Square Endcaps;
   * <LI>Transform: Identity
   * <LI>Composite: AlphaComposite.SRC_OVER
   * <LI>Clip: Rectangle(0, 0, size.width, size.height)
   * </UL>
   * 
   * @param size rectangle specifying the bounds of the image
   */
  protected AbstractVectorGraphics(Dimension size) {
    this.size = size;

    deviceClip = size != null ? new Rectangle(0, 0, size.width, size.height) : null;
    userClip = null;
    currentTransform = new AffineTransform();
    currentComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);
    currentStroke = new BasicStroke();

    currentColor = Color.BLACK;
    currentPaint = Color.BLACK;
    backgroundColor = Color.BLACK;
    currentFont = new Font("Dialog", Font.PLAIN, 12);

    // Initialize the rendering hints.
    hints = new RenderingHints(null);
  }

  /**
   * Constructs a Graphics context with the following graphics state:
   * <UL>
   * <LI>Paint: The color of the component.
   * <LI>Font: The font of the component.
   * <LI>Stroke: Linewidth 1.0; No Dashing; Miter Join Style; Miter Limit 10; Square Endcaps;
   * <LI>Transform: The getDefaultTransform for the GraphicsConfiguration of the component.
   * <LI>Composite: AlphaComposite.SRC_OVER
   * <LI>Clip: The size of the component, Rectangle(0, 0, size.width, size.height)
   * </UL>
   * 
   * @param component to be used to initialize the values of the graphics state
   */
  protected AbstractVectorGraphics(Component component) {
    this.size = component.getSize();

    deviceClip = size != null ? new Rectangle(0, 0, size.width, size.height) : null;
    userClip = null;
    GraphicsConfiguration gc = component.getGraphicsConfiguration();
    currentTransform = gc != null ? gc.getDefaultTransform() : new AffineTransform();
    currentComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);
    currentStroke = new BasicStroke();

    currentColor = Color.BLACK;
    currentPaint = Color.BLACK;
    backgroundColor = Color.BLACK;
    currentFont = new Font("Dialog", Font.PLAIN, 12);

    // Initialize the rendering hints.
    hints = new RenderingHints(null);
  }

  /**
   * Constructs a sub-graphics context.
   * 
   * @param graphics context to clone from
   */
  protected AbstractVectorGraphics(AbstractVectorGraphics graphics) {
    super();
    backgroundColor = graphics.backgroundColor;
    currentColor = graphics.currentColor;
    currentPaint = graphics.currentPaint;
    currentFont = graphics.currentFont;

    size = new Dimension(graphics.size);

    deviceClip = new Rectangle(graphics.deviceClip);
    userClip = graphics.userClip;
    currentTransform = new AffineTransform(graphics.currentTransform);
    currentComposite = graphics.currentComposite;
    currentStroke = graphics.currentStroke;
    hints = graphics.hints;
  }

  @Override
  public Font getFont() {
    if (currentFont == null) {
      return DEFAULT_FONT;
    }
    return currentFont;
  }

  @Override
  public void clearRect(int x, int y, int width, int height) {
    Composite oldComposite = getComposite();
    Paint oldPaint = getPaint();
    setComposite(AlphaComposite.Src);
    setColor(getBackground());
    fillRect(x, y, width, height);
    setPaint(oldPaint);
    setComposite(oldComposite);
  }

  @Override
  public void drawLine(int x1, int y1, int x2, int y2) {
    draw(new Line2D.Double(x1, y1, x2, y2));
  }

  @Override
  public void drawRect(int x, int y, int width, int height) {
    draw(new Rectangle2D.Double(x, y, width, height));
  }

  @Override
  public void fillRect(int x, int y, int width, int height) {
    fill(new Rectangle2D.Double(x, y, width, height));
  }

  @Override
  public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
    draw(new Arc2D.Double(x, y, width, height, startAngle, arcAngle, Arc2D.OPEN));
  }

  @Override
  public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
    fill(new Arc2D.Double(x, y, width, height, startAngle, arcAngle, Arc2D.PIE));
  }

  @Override
  public void drawOval(int x, int y, int width, int height) {
    draw(new Ellipse2D.Double(x, y, width, height));
  }

  @Override
  public void fillOval(int x, int y, int width, int height) {
    fill(new Ellipse2D.Double(x, y, width, height));
  }

  @Override
  public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
    draw(new RoundRectangle2D.Double(x, y, width, height, arcWidth, arcHeight));
  }

  @Override
  public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
    fill(new RoundRectangle2D.Double(x, y, width, height, arcWidth, arcHeight));
  }

  @Override
  public void translate(int x, int y) {
    translate((double) x, (double) y);
  }

  @Override
  public void drawString(String str, int x, int y) {
    drawString(str, (double) x, (double) y);
  }

  @Override
  public void drawString(String str, float x, float y) {
    drawString(str, (double) x, (double) y);
  }

  @Override
  public void drawString(AttributedCharacterIterator iterator, int x, int y) {
    drawString(iterator, (float) x, (float) y);
  }

  // ------------------ other wrapper methods ----------------

  @Override
  public Color getBackground() {
    return backgroundColor;
  }

  @Override
  public void setBackground(Color color) {
    backgroundColor = color;
  }

  @Override
  public Color getColor() {
    return currentColor;
  }

  @Override
  public Paint getPaint() {
    return currentPaint;
  }

  @Override
  public void rotate(double theta, double x, double y) {
    translate(x, y);
    rotate(theta);
    translate(-x, -y);
  }

  @Override
  public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {
    draw(createShape(xPoints, yPoints, nPoints, false));
  }

  @Override
  public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
    draw(createShape(xPoints, yPoints, nPoints, true));
  }

  @Override
  public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
    fill(createShape(xPoints, yPoints, nPoints, true));
  }

  @Override
  public boolean hit(Rectangle rect, Shape s, boolean onStroke) {
    if (onStroke && getStroke() != null) {
      s = getStroke().createStrokedShape(s);
    }

    if (getTransform() != null) {
      s = getTransform().createTransformedShape(s);
    }

    return s.intersects(rect);
  }

  /*
   * ================================================================================ |
   * XXX 5. Drawing Methods
   * ================================================================================
   */

  /* 5.3. Images */

  @Override
  public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
    if (img != null) {
      int imageWidth = img.getWidth(observer);
      int imageHeight = img.getHeight(observer);
      return drawImage(img, x, y, x + imageWidth, y + imageHeight, 0, 0, imageWidth, imageHeight,
          null, observer);
    }
    return true;
  }

  @Override
  public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer) {
    if (img != null) {
      int imageWidth = img.getWidth(observer);
      int imageHeight = img.getHeight(observer);
      return drawImage(img, x, y, x + width, y + height, 0, 0, imageWidth, imageHeight, null,
          observer);
    }
    return true;
  }

  @Override
  public boolean drawImage(Image img, int x, int y, int width, int height, Color bgcolor,
      ImageObserver observer) {
    if (img != null) {
      int imageWidth = img.getWidth(observer);
      int imageHeight = img.getHeight(observer);
      return drawImage(img, x, y, x + width, y + height, 0, 0, imageWidth, imageHeight, bgcolor,
          observer);
    }
    return true;
  }

  @Override
  public boolean drawImage(Image img, int x, int y, Color bgcolor, ImageObserver observer) {
    if (img != null) {
      int imageWidth = img.getWidth(observer);
      int imageHeight = img.getHeight(observer);
      return drawImage(img, x, y, x + imageWidth, y + imageHeight, 0, 0, imageWidth, imageHeight,
          bgcolor, observer);
    }
    return true;
  }

  @Override
  public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2,
      int sy2, ImageObserver observer) {
    return drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null, observer);
  }

  @Override
  public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2,
      int sy2, Color bgcolor, ImageObserver observer) {
    if (img == null) {
      return true;
    }

    int srcX = Math.min(sx1, sx2);
    int srcY = Math.min(sy1, sy2);
    int srcWidth = Math.abs(sx2 - sx1);
    int srcHeight = Math.abs(sy2 - sy1);
    int width = Math.abs(dx2 - dx1);
    int height = Math.abs(dy2 - dy1);

    Rectangle2D.Float crop = null;
    if ((srcX != 0) || (srcY != 0) || (srcWidth != img.getWidth(observer))
        || (srcHeight != img.getHeight(observer))) {
      crop = new Rectangle2D.Float(srcX, srcY, srcWidth, srcHeight);
    }

    boolean flipHorizontal = (dx2 < dx1) ^ (sx2 < sx1); // src flipped
                                                        // and not dest
                                                        // flipped or
                                                        // vice versa
    boolean flipVertical = (dy2 < dy1) ^ (sy2 < sy1); // <=> source
                                                      // flipped XOR
                                                      // dest flipped

    double tx = flipHorizontal ? (double) dx2 : (double) dx1;
    double ty = flipVertical ? (double) dy2 : (double) dy1;

    double sx = (double) width / srcWidth;
    sx = flipHorizontal ? -1 * sx : sx;
    double sy = (double) height / srcHeight;
    sy = flipVertical ? -1 * sy : sy;

    return writeImage(img, new AffineTransform(sx, 0, 0, sy, tx, ty), crop, bgcolor, observer);
  }

  @Override
  public boolean drawImage(Image img, AffineTransform xform, ImageObserver obs) {
    return img == null || writeImage(img, xform, obs);
  }

  @Override
  public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y) {
    if (op == null) {
      drawImage(img, x, y, null);
    } else {
      drawImage(op.filter(img, null), x, y, null);
    }
  }

  @Override
  public void drawRenderableImage(RenderableImage img, AffineTransform xform) {
    drawRenderedImage(
        img.createRendering(new RenderContext(new AffineTransform(), getRenderingHints())), xform);
  }

  @Override
  public void drawRenderedImage(RenderedImage img, AffineTransform xform) {
    writeImage(img, xform);
  }

  protected abstract void writeImage(RenderedImage image, AffineTransform transform);

  protected abstract boolean writeImage(Image image, AffineTransform transform,
      ImageObserver observer);

  protected abstract boolean writeImage(Image image, AffineTransform transform, Rectangle2D crop,
      Color bgcolor, ImageObserver observer);

  /**
   * Draws the string at (x, y). If TEXT_AS_SHAPES is set
   * {@link #drawGlyphVector(java.awt.font.GlyphVector, float, float)} is used, otherwise
   * {@link #writeString(String, double, double)} for a more direct output of the string.
   */
  public void drawString(String string, double x, double y) {
    // something to draw?
    if (string == null || "".equals(string.trim())) {
      return;
    }
    writeString(string, x, y);
  }

  protected abstract void writeString(String string, double x, double y);

  @Override
  public void drawGlyphVector(GlyphVector g, float x, float y) {
    fill(g.getOutline(x, y));
  }

  @Override
  public void drawString(AttributedCharacterIterator iterator, float x, float y) {

    // reset to that font at the end
    Font font = getFont();

    // initial attributes, we us TextAttribute.equals() rather
    // than Font.equals() because using Font.equals() we do
    // not get a 'false' if underline etc. is changed
    Map<? extends Attribute, ?> attributes = DirectDrawUtils.getAttributes(font);

    // stores all characters which are written with the same font
    // if font is changed the buffer will be written and cleared
    // after it
    StringBuffer sb = new StringBuffer();

    for (char c = iterator.first(); c != AttributedCharacterIterator.DONE; c = iterator.next()) {

      // append c if font is not changed
      if (attributes.equals(iterator.getAttributes())) {
        sb.append(c);

      } else {
        // TextLayout does not like 0 length strings
        if (sb.length() > 0) {
          // draw sb if font is changed
          drawString(sb.toString(), x, y);

          // change the x offset for the next drawing
          TextLayout tl = new TextLayout(sb.toString(), attributes, getFontRenderContext());

          // calculate real width
          x = x + Math.max(tl.getAdvance(), (float) tl.getBounds().getWidth());
        }

        // empty sb
        sb = new StringBuffer();
        sb.append(c);

        // change the font
        attributes = iterator.getAttributes();
        setFont(new Font(attributes));
      }
    }

    // draw the rest
    if (sb.length() > 0) {
      drawString(sb.toString(), x, y);
    }

    // use the old font for the next string drawing
    setFont(font);
  }

  /*
   * ================================================================================ |
   * XXX 6. Transformations
   * ================================================================================
   */

  @Override
  public AffineTransform getTransform() {
    return new AffineTransform(currentTransform);
  }

  @Override
  public void setTransform(AffineTransform Tx) {
    if (currentTransform.equals(Tx)) {
      return;
    }
    oldTransform.setTransform(currentTransform);
    currentTransform.setTransform(Tx);
    writeSetTransform(Tx);
  }

  @Override
  public void transform(AffineTransform Tx) {
    currentTransform.concatenate(Tx);
    writeTransform(Tx);
  }

  @Override
  public void translate(double tx, double ty) {
    currentTransform.translate(tx, ty);
    writeTransform(new AffineTransform(1, 0, 0, 1, tx, ty));
  }

  @Override
  public void rotate(double theta) {
    currentTransform.rotate(theta);
    writeTransform(new AffineTransform(Math.cos(theta), Math.sin(theta), -Math.sin(theta),
        Math.cos(theta), 0, 0));
  }

  @Override
  public void scale(double sx, double sy) {
    currentTransform.scale(sx, sy);
    writeTransform(new AffineTransform(sx, 0, 0, sy, 0, 0));
  }

  @Override
  public void shear(double shx, double shy) {
    currentTransform.shear(shx, shy);
    writeTransform(new AffineTransform(1, shy, shx, 1, 0, 0));
  }

  /**
   * Writes out the transform as it needs to be concatenated to the internal transform of the output
   * format. If there is no implementation of an internal transform, then this method needs to do
   * nothing, BUT all coordinates need to be transformed by the currentTransform before being
   * written out.
   *
   * @param transform to be written
   */
  protected abstract void writeTransform(AffineTransform transform);

  /**
   * Clears any existing transformation and sets the a new one. The default implementation calls
   * writeTransform using the inverted affine transform to calculate it.
   * 
   * @param transform to be written
   */
  protected void writeSetTransform(AffineTransform transform) {
    try {
      AffineTransform deltaTransform = oldTransform.createInverse();
      deltaTransform.concatenate(transform);
      writeTransform(deltaTransform);
    } catch (NoninvertibleTransformException e) {
      handleException(e);
    }
  }

  /*
   * ================================================================================ |
   * XXX 7. Clipping
   * ================================================================================
   */

  @Override
  public Shape getClip() {
    return untransformShape(userClip);
  }

  @Override
  public Rectangle getClipBounds() {
    Shape clip = getClip();
    return clip != null ? getClip().getBounds() : null;
  }

  @Override
  public Rectangle getClipBounds(Rectangle r) {
    Rectangle bounds = getClipBounds();
    if (bounds != null) {
      r.setBounds(bounds);
    }
    return r;
  }

  @Override
  public void clipRect(int x, int y, int width, int height) {
    clip(new Rectangle(x, y, width, height));
  }

  @Override
  public void setClip(int x, int y, int width, int height) {
    setClip(new Rectangle(x, y, width, height));
  }

  @Override
  public void setClip(Shape clip) {
    changeClip(transformShape(clip));
  }

  @Override
  public void clip(Shape s) {
    s = transformShape(s);
    if (userClip != null) {
      s = intersectShapes(userClip, s);
    }
    changeClip(s);
  }

  protected void changeClip(Shape clip) {
    userClip = clip;
  }

  /**
   * Copied from SunGraphics2D
   */
  Shape intersectShapes(Shape lhs, Shape rhs) {
    return lhs instanceof Rectangle r && rhs instanceof Rectangle r1 ? r.intersection(r1)
        : (lhs instanceof Rectangle2D rd ? intersectRectShape(rd, rhs)
            : (rhs instanceof Rectangle2D rd ? intersectRectShape(rd, lhs)
                : intersectByArea(lhs, rhs)));
  }

  /**
   * Copied from SunGraphics2D
   */
  Shape intersectRectShape(Rectangle2D lhs, Shape rhs) {
    if (rhs instanceof Rectangle2D rhsRect) {
      Rectangle2D result = new Rectangle2D.Double();

      double x1 = Math.max(lhs.getMinX(), rhsRect.getMinX());
      double x2 = Math.min(lhs.getMaxX(), rhsRect.getMaxX());
      double y1 = Math.max(lhs.getMinY(), rhsRect.getMinY());
      double y2 = Math.min(lhs.getMaxY(), rhsRect.getMaxY());
      if (x2 - x1 >= 0.0D && y2 - y1 >= 0.0D) {
        result.setFrameFromDiagonal(x1, y1, x2, y2);
      } else {
        result.setFrameFromDiagonal(0.0D, 0.0D, 0.0D, 0.0D);
      }
      return result;
    } else if (lhs.contains(rhs.getBounds2D())) {
      rhs = cloneShape(rhs);
      return rhs;
    } else {
      return this.intersectByArea(lhs, rhs);
    }
  }

  /**
   * Copied from SunGraphics2D
   */
  protected static Shape cloneShape(Shape shape) {
    return new GeneralPath(shape);
  }

  /**
   * Copied from SunGraphics2D
   */
  Shape intersectByArea(Shape lhs, Shape rhs) {
    Area var5 = new Area(lhs);

    Area var6;
    if (rhs instanceof Area area) {
      var6 = area;
    } else {
      var6 = new Area(rhs);
    }

    var5.intersect(var6);
    return var5.isRectangular() ? var5.getBounds() : var5;
  }

  /*
   * ================================================================================ |
   * XXX 8. Graphics State
   * ================================================================================
   */

  /* 8.1. stroke/linewidth */

  @Override
  public Stroke getStroke() {
    return currentStroke;
  }

  @Override
  public void setStroke(Stroke s) {
    if (s.equals(currentStroke)) {
      return;
    }
    currentStroke = s;
    writeStroke(s);
  }

  public abstract void writeStroke(Stroke stroke);

  /* 8.2 Paint */

  @Override
  public void setColor(Color c) {
    if (c == null) {
      return;
    }

    if (c.equals(getPaint())) {
      return;
    }

    currentColor = c;
    currentPaint = c;
    writePaint(c);
  }

  @Override
  public void setPaint(Paint paint) {
    if (paint == null) {
      return;
    }

    if (paint.equals(getPaint())) {
      return;
    }

    if (paint instanceof Color color) {
      currentColor = color;
    }

    currentPaint = paint;
    writePaint(paint);
  }

  protected abstract void writePaint(Paint paint);

  /* 8.3. font */

  @Override
  public FontRenderContext getFontRenderContext() {
    boolean antialias = RenderingHints.VALUE_TEXT_ANTIALIAS_ON
        .equals(getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING));
    boolean fractions = RenderingHints.VALUE_FRACTIONALMETRICS_ON
        .equals(getRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS));
    return new FontRenderContext(new AffineTransform(), antialias, fractions);
  }

  @Override
  @SuppressWarnings("deprecation")
  public FontMetrics getFontMetrics(Font f) {
    return Toolkit.getDefaultToolkit().getFontMetrics(f);
  }

  /* 8.4. rendering hints */

  @Override
  public RenderingHints getRenderingHints() {
    return (RenderingHints) hints.clone();
  }

  @Override
  public void addRenderingHints(Map<?, ?> hints) {
    this.hints.putAll(hints);
  }

  @Override
  public void setRenderingHints(Map<?, ?> hints) {
    this.hints.clear();
    this.hints.putAll(hints);
  }

  @Override
  public Object getRenderingHint(RenderingHints.Key hintKey) {
    return hints.get(hintKey);
  }

  @Override
  public void setRenderingHint(RenderingHints.Key hintKey, Object hintValue) {
    // extra protection, failed on under MacOS X 10.2.6, jdk 1.4.1_01-39/14
    if ((hintKey == null) || (hintValue == null)) {
      return;
    }
    hints.put(hintKey, hintValue);
  }

  @Override
  public void setFont(Font font) {
    if (font == null || currentFont.equals(font)) {
      return;
    }
    currentFont = font;
    writeFont(currentFont);
  }

  protected abstract void writeFont(Font font);

  /*
   * ================================================================================
   * XXX 9. AUXILIARY
   * ================================================================================
   */

  /**
   * Handles an exception which has been caught. Dispatches exception to writeWarning for
   * UnsupportedOperationExceptions and writeError for others
   */
  protected void handleException(Exception e) {
    e.printStackTrace();
  }

  @Override
  public Composite getComposite() {
    return currentComposite;
  }

  @Override
  public void setComposite(Composite composite) {
    if (composite == null || composite.equals(getComposite())) {
      return;
    }
    currentComposite = composite;
    writeComposite(composite);
  }

  protected abstract void writeComposite(Composite composite);

  /**
   * Creates a polyline/polygon shape from a set of points. Needs a bias!
   * 
   * @param xPoints X coordinates of the polyline.
   * @param yPoints Y coordinates of the polyline.
   * @param nPoints number of points of the polyline.
   * @param close is shape closed
   */
  protected Shape createShape(int[] xPoints, int[] yPoints, int nPoints, boolean close) {
    GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
    if (nPoints > 0) {
      path.moveTo(xPoints[0], yPoints[0]);
      int lastX = xPoints[0];
      int lastY = yPoints[0];
      if (close && (Math.abs(xPoints[nPoints - 1] - lastX) < 1)
          && (Math.abs(yPoints[nPoints - 1] - lastY) < 1)) {
        nPoints--;
      }
      for (int i = 1; i < nPoints; i++) {
        if ((Math.abs(xPoints[i] - lastX) > 1) || (Math.abs(yPoints[i] - lastY) > 1)) {
          path.lineTo(xPoints[i], yPoints[i]);
          lastX = xPoints[i];
          lastY = yPoints[i];
        }
      }
      if (close) {
        path.closePath();
      }
    }
    return path;
  }

  protected Shape transformShape(AffineTransform at, Shape s) {
    if (s == null) {
      return null;
    }
    return at.createTransformedShape(s);
  }

  protected Shape transformShape(Shape s) {
    return transformShape(currentTransform, s);
  }

  protected Shape untransformShape(Shape s) {
    if (s == null) {
      return null;
    }
    try {
      return transformShape(currentTransform.createInverse(), s);
    } catch (NoninvertibleTransformException e) {
      return null;
    }
  }
}
