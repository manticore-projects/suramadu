package org.webswing.toolkit;

import org.webswing.toolkit.util.Util;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.Serial;

public class WebCursor extends Cursor {

  @Serial
  private static final long serialVersionUID = -7369225579337480240L;
  private Image image;
  private final Point hotSpot;

  public WebCursor(Image cursor, Point hotSpot, String name) {
    super(name);
    this.image = cursor;
    this.hotSpot = hotSpot;
  }

  public BufferedImage getImage() {
    if (image instanceof BufferedImage bufferedImage) {
      return bufferedImage;
    } else {
      Util.waitForImage(image);
      BufferedImage bimg = new BufferedImage(image.getWidth(null), image.getHeight(null),
          BufferedImage.TYPE_INT_ARGB);
      Graphics g = bimg.getGraphics();
      g.drawImage(image, 0, 0, null);
      g.dispose();
      image = bimg;
      return bimg;
    }
  }

  public Point getHotSpot() {
    return hotSpot;
  }

}
