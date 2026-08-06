// Copyright 2000-2007, FreeHEP
// Modification to original: merged with AbstractVectorGraphicsIO
package org.webswing.toolkit.util;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.Map;


public class DummyGraphics2D extends Graphics2D {


  private Color backgroundColor;

  private Stroke currentStroke;

  private Color currentColor;

  private Paint currentPaint;

  private Font currentFont;

  private final RenderingHints hints;

  private Composite currentComposite;

  private Shape userClip;

  private final AffineTransform currentTransform;

  public DummyGraphics2D() {

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

  @Override
  public Font getFont() {
    return currentFont;
  }

  @Override
  public void clearRect(int x, int y, int width, int height) {}

  @Override
  public void drawLine(int x1, int y1, int x2, int y2) {}

  @Override
  public void drawRect(int x, int y, int width, int height) {}

  @Override
  public void fillRect(int x, int y, int width, int height) {}

  @Override
  public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {}

  @Override
  public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {}

  @Override
  public void drawOval(int x, int y, int width, int height) {}

  @Override
  public void fillOval(int x, int y, int width, int height) {}

  @Override
  public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {}

  @Override
  public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {}

  @Override
  public void translate(int x, int y) {
    translate((double) x, (double) y);
  }

  @Override
  public void drawString(String str, int x, int y) {}

  @Override
  public void drawString(String str, float x, float y) {}

  @Override
  public void drawString(AttributedCharacterIterator iterator, int x, int y) {}

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
  public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {}

  @Override
  public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {}

  @Override
  public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {}

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

  @Override
  public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
    return true;
  }

  @Override
  public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer) {
    return true;
  }

  @Override
  public boolean drawImage(Image img, int x, int y, int width, int height, Color bgcolor,
      ImageObserver observer) {
    return true;
  }

  @Override
  public boolean drawImage(Image img, int x, int y, Color bgcolor, ImageObserver observer) {
    return true;
  }

  @Override
  public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2,
      int sy2, ImageObserver observer) {
    return true;
  }

  @Override
  public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2,
      int sy2, Color bgcolor, ImageObserver observer) {
    return true;
  }

  @Override
  public boolean drawImage(Image img, AffineTransform xform, ImageObserver obs) {
    return true;
  }

  @Override
  public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y) {}

  @Override
  public void drawRenderableImage(RenderableImage img, AffineTransform xform) {}

  @Override
  public void drawRenderedImage(RenderedImage img, AffineTransform xform) {}


  @Override
  public void drawGlyphVector(GlyphVector g, float x, float y) {}

  @Override
  public void drawString(AttributedCharacterIterator iterator, float x, float y) {}

  @Override
  public AffineTransform getTransform() {
    return new AffineTransform(currentTransform);
  }

  @Override
  public void setTransform(AffineTransform Tx) {
    if (currentTransform.equals(Tx)) {
      return;
    }
    currentTransform.setTransform(Tx);
  }

  @Override
  public void transform(AffineTransform Tx) {
    currentTransform.concatenate(Tx);
  }

  @Override
  public void translate(double tx, double ty) {
    currentTransform.translate(tx, ty);
  }

  @Override
  public void rotate(double theta) {
    currentTransform.rotate(theta);
  }

  @Override
  public void scale(double sx, double sy) {
    currentTransform.scale(sx, sy);
  }

  @Override
  public void shear(double shx, double shy) {
    currentTransform.shear(shx, shy);
  }

  @Override
  public Shape getClip() {
    return userClip;
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
    userClip = clip;
  }

  @Override
  public void clip(Shape s) {
    userClip = s;
  }

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
  }

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
  }

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
  }

  @Override
  public void draw(Shape s) {}

  @Override
  public void fill(Shape s) {}

  @Override
  public GraphicsConfiguration getDeviceConfiguration() {
    return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
        .getDefaultConfiguration();
  }

  @Override
  public Graphics create() {
    return this;
  }

  @Override
  public void setPaintMode() {}

  @Override
  public void setXORMode(Color c1) {}

  @Override
  public void copyArea(int x, int y, int width, int height, int dx, int dy) {}

  @Override
  public void dispose() {}
}
