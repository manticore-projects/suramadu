package org.webswing.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.webswing.directdraw.DirectDrawServicesAdapter;
import org.webswing.services.impl.ddutil.FastDirectDrawServicesAdapter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class FastDirectDrawServicesAdaptorTest {
  FastDirectDrawServicesAdapter dd;
  private DirectDrawServicesAdapter ddOrig;

  @BeforeEach
  void setUp() throws Exception {
    dd = new FastDirectDrawServicesAdapter();
    ddOrig = new DirectDrawServicesAdapter();
  }

  @Test
  void testXXHashFromBufferedImage() {
    BufferedImage img1 = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
    Graphics g1 = img1.getGraphics();
    g1.setColor(Color.yellow);
    g1.fillRect(10, 10, 10, 10);
    g1.dispose();
    BufferedImage img2 = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
    Graphics g2 = img2.getGraphics();
    g2.setColor(Color.yellow);
    g2.fillRect(10, 10, 10, 10);
    g2.dispose();
    BufferedImage img3 = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
    Graphics g3 = img3.getGraphics();
    g3.setColor(Color.black);
    g3.fillRect(10, 10, 10, 10);
    g3.dispose();

    long h1 = ddOrig.computeHash(img1);
    long h2 = ddOrig.computeHash(img2);
    long h3 = ddOrig.computeHash(img3);
    assertEquals(h1, h2);
    assertNotEquals(h1, h3);

    h1 = dd.computeHash(img1);
    h2 = dd.computeHash(img2);
    h3 = dd.computeHash(img3);
    assertEquals(h1, h2);
    assertNotEquals(h1, h3);
  }
}
