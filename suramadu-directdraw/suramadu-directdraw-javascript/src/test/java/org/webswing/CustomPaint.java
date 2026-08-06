package org.webswing;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

public class CustomPaint implements Paint {
  private TexturePaint texturePaint;

  public CustomPaint() {}

  private TexturePaint getTexture() {
    if (texturePaint == null) {
      BufferedImage buf = new BufferedImage(30, 30, BufferedImage.TYPE_INT_ARGB);
      Graphics2D g2d = buf.createGraphics();
      g2d.setClip(null);
      g2d.setColor(Color.black);
      g2d.fillRect(0, 0, 30, 30);
      g2d.setColor(Color.orange);
      g2d.setStroke(new ZigzagStroke(new BasicStroke(4), 15, 5));
      g2d.drawLine(0, 15, 30, 15);
      g2d.dispose();
      texturePaint = new TexturePaint(buf, new Rectangle2D.Double(0, 0, 30, 30));
    }
    return texturePaint;
  }

  /**
   * {@inheritDoc}
   */
  public int getTransparency() {
    return getTexture().getTransparency();
  }

  /**
   * {@inheritDoc}
   */
  public PaintContext createContext(ColorModel cm, Rectangle deviceBounds, Rectangle2D userBounds,
      AffineTransform transform, RenderingHints hints) {
    return getTexture().createContext(cm, deviceBounds, userBounds, transform, hints);
  }
}
