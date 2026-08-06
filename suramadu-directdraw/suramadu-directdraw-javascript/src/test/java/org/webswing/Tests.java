package org.webswing;

import org.webswing.directdraw.DirectDrawServicesAdapter;
import org.webswing.directdraw.toolkit.WebGraphics;
import org.webswing.directdraw.toolkit.WebImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Tests {

  @SuppressWarnings("unused")
  public static boolean t00DrawLineTest(Graphics2D g, int repeat) {
    if (repeat != 0) {
      return false;
    }
    g.setColor(Color.red);
    g.setStroke(new BasicStroke(7, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 2.5f,
        new float[] {3, 15, 40, 15}, 10));
    g.drawPolyline(new int[] {20, 20, 100, 100}, new int[] {5, 50, 50, 95}, 4);
    g.setColor(Color.green);
    g.setStroke(new BasicStroke(7, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 2.5f,
        new float[] {3, 15, 40, 15}, 50));
    g.drawPolyline(new int[] {120, 120, 200, 200}, new int[] {5, 50, 50, 95}, 4);
    g.setColor(Color.blue);
    g.setStroke(new BasicStroke(7, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL, 2.5f,
        new float[] {3, 15, 40, 15}, 100));
    g.drawPolyline(new int[] {220, 220, 300, 300}, new int[] {5, 50, 50, 95}, 4);

    g.setColor(Color.green);
    g.setStroke(new BasicStroke(7, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 2.5f));
    g.drawPolyline(new int[] {320, 320, 340, 340}, new int[] {20, 80, 20, 80}, 4);
    g.setStroke(new BasicStroke(7, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f));
    g.drawPolyline(new int[] {360, 360, 380, 380}, new int[] {20, 80, 20, 80}, 4);

    g.setStroke(new ZigzagStroke(
        new BasicStroke(7, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f), 10, 5));
    g.drawLine(420, 10, 420, 90);
    return true;
  }

  public static boolean t01DrawImageTest(Graphics2D g, int repeat) throws IOException {
    if (repeat != 0) {
      return false;
    }
    BufferedImage image = ImageIO.read(Tests.class.getClassLoader().getResource("ws.png"));
    g.drawImage(image, 10, 10, 180, 80, 25, 25, 100, 100, null);
    g.drawImage(image, 200, 10, 380, 80, 100, 100, 25, 25, null);
    return true;
  }

  public static boolean t02FillRectTest(Graphics2D g, int repeat) {
    if (repeat != 0) {
      return false;
    }
    Color c = Color.ORANGE;
    Color c1 = Color.red;
    Color c2 = Color.green;
    Color c3 = Color.blue;
    Color c4 = Color.yellow;
    g.setColor(c);
    g.draw(new Arc2D.Double(new Rectangle2D.Double(0, 0, 48, 30), 15, 250, Arc2D.OPEN));
    g.draw(new Arc2D.Double(new Rectangle2D.Double(0, 33, 48, 30), 15, 250, Arc2D.CHORD));
    g.draw(new Arc2D.Double(new Rectangle2D.Double(0, 66, 48, 30), 15, 250, Arc2D.PIE));
    g.setColor(c1);
    g.fill(new Arc2D.Double(new Rectangle2D.Double(50, 0, 48, 30), 15, 250, Arc2D.OPEN));
    g.fill(new Arc2D.Double(new Rectangle2D.Double(50, 33, 48, 30), 15, 250, Arc2D.CHORD));
    g.fill(new Arc2D.Double(new Rectangle2D.Double(50, 66, 48, 30), 15, 250, Arc2D.PIE));
    g.setColor(c2);
    g.draw(new Rectangle2D.Double(100, 0, 48, 48));
    g.fill(new Rectangle2D.Double(100, 50, 48, 48));
    g.setColor(c3);
    g.draw(new RoundRectangle2D.Double(150, 0, 48, 48, 20, 20));
    g.fill(new RoundRectangle2D.Double(150, 50, 48, 48, 20, 20));
    g.setColor(c4);
    g.draw(new Ellipse2D.Double(200, 0, 148, 48));
    g.fill(new Ellipse2D.Double(200, 50, 148, 48));
    return true;
  }

  public static boolean t03TransformTest(Graphics2D g, int repeat) throws IOException {
    if (repeat != 0) {
      return false;
    }
    BufferedImage image = ImageIO.read(Tests.class.getClassLoader().getResource("ws.png"));
    g.rotate(Math.PI / 4, 50, 50);
    g.drawImage(image, 0, 0, 100, 100, null);
    g.rotate(-(Math.PI / 4), 50, 50);
    g.translate(100, 0);
    g.scale(1.5, 1.5);
    g.drawImage(image, 0, 0, 100, 100, null);
    g.scale(0.5, 0.5);
    g.drawImage(image, 0, 0, 100, 100, null);
    g.translate(250, 0);
    g.rotate(-(Math.PI / 4), 50, 100);
    g.drawImage(image, 0, 0, 100, 100, null);
    return true;
  }

  @SuppressWarnings("unused")
  public static boolean t04TexturePaintTest(Graphics2D g, int repeat) throws IOException {
    if (repeat != 0) {
      return false;
    }
    BufferedImage image = ImageIO.read(Tests.class.getClassLoader().getResource("ws.png"));
    g.setPaint(Color.orange);
    g.fillRect(0, 0, 500, 100);
    g.setPaint(new TexturePaint(image, new Rectangle2D.Double(50, 50, 48, 48)));
    g.fill(new RoundRectangle2D.Double(0, 0, 200, 100, 40, 40));
    g.setStroke(new BasicStroke(17, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 2.5f,
        new float[] {3, 20, 40, 20}, 10));
    g.drawPolyline(new int[] {220, 270, 320, 370, 420, 470}, new int[] {5, 95, 5, 95, 5, 95}, 6);
    return true;
  }

  @SuppressWarnings("unused")
  public static boolean t05LinearGradientTest(Graphics2D g, int repeat) {
    if (repeat != 0) {
      return false;
    }
    g.setPaint(new GradientPaint(new Point2D.Float(0, 0), Color.green, new Point2D.Float(100, 30),
        Color.blue));
    g.fillRect(0, 0, 100, 100);
    g.setColor(Color.BLACK);
    g.drawRect(0, 0, 100, 100);
    float[] fractions = new float[] {0f, 0.5f, 1f};
    Color[] colors = new Color[] {Color.BLUE, Color.green, Color.yellow};
    g.setPaint(new LinearGradientPaint(new Point2D.Float(200, 100), new Point2D.Float(150, 50),
        fractions, colors, CycleMethod.REFLECT));
    g.fillRect(100, 0, 100, 100);
    g.setColor(Color.BLACK);
    g.drawRect(100, 0, 100, 100);
    g.setPaint(new LinearGradientPaint(new Point2D.Float(900, 20), new Point2D.Float(900, 0),
        fractions, colors, CycleMethod.REFLECT));
    g.fillRect(200, 0, 150, 100);
    g.setColor(Color.BLACK);
    g.drawRect(200, 0, 150, 100);
    g.setPaint(new LinearGradientPaint(new Point2D.Float(1050, 0), new Point2D.Float(1000, 0),
        fractions, colors, CycleMethod.REFLECT));
    g.fillRect(350, 0, 150, 100);
    g.setColor(Color.BLACK);
    g.drawRect(350, 0, 150, 100);
    return true;
  }

  @SuppressWarnings("unused")
  public static boolean t06RadialGradientTest(Graphics2D g, int repeat) {
    if (repeat != 0) {
      return false;
    }
    float[] fractions = new float[] {0, 0.5f, 1};
    Color[] colors = new Color[] {Color.white, Color.red, Color.black};
    g.setPaint(new RadialGradientPaint(new Point2D.Float(25, 25), 25, fractions, colors));
    g.fillOval(0, 0, 50, 50);
    g.setPaint(new RadialGradientPaint(new Point2D.Float(25, 75), 25, new Point2D.Float(50, 100),
        fractions, colors, CycleMethod.NO_CYCLE));
    g.fillOval(0, 50, 50, 50);
    g.setPaint(new RadialGradientPaint(new Point2D.Float(75, 25), 25, new Point2D.Float(85, 35),
        fractions, colors, CycleMethod.NO_CYCLE));
    g.fillOval(50, 0, 50, 50);
    g.setPaint(new RadialGradientPaint(new Point2D.Float(75, 75), 25, new Point2D.Float(50, 50),
        fractions, colors, CycleMethod.NO_CYCLE));
    g.fillOval(50, 50, 50, 50);
    g.setPaint(new RadialGradientPaint(new Point2D.Float(150, 50), 25, fractions, colors,
        CycleMethod.REFLECT));
    g.fillOval(100, 0, 100, 100);
    g.setPaint(new RadialGradientPaint(new Point2D.Float(250, 50), 25, fractions, colors,
        CycleMethod.REPEAT));
    g.fillOval(200, 0, 100, 100);
    g.setPaint(new RadialGradientPaint(new Point2D.Float(350, 50), 25, new Point2D.Float(340, 35),
        fractions, colors, CycleMethod.REFLECT));
    g.fillOval(300, 0, 100, 100);
    g.setPaint(new RadialGradientPaint(new Point2D.Float(450, 50), 25, new Point2D.Float(460, 65),
        fractions, colors, CycleMethod.REPEAT));
    g.fillOval(400, 0, 100, 100);
    return true;
  }

  @SuppressWarnings("unused")
  public static boolean t07ClipTest(Graphics2D g, int repeat) {
    if (repeat != 0) {
      return false;
    }
    g.setColor(Color.orange);
    g.setClip(new Arc2D.Double(new Rectangle2D.Double(0, 0, 98, 48), 15, 250, Arc2D.OPEN));
    g.fillRect(0, 0, 500, 100);
    g.setColor(Color.red);
    g.setClip(new Arc2D.Double(new Rectangle2D.Double(0, 50, 98, 48), 15, 250, Arc2D.PIE));
    g.fillRect(0, 0, 500, 100);

    g.setColor(Color.red);
    g.setClip(new Rectangle2D.Double(100, 0, 98, 98));
    g.fillRect(0, 0, 500, 100);
    g.setColor(Color.green);
    g.clip(new Rectangle2D.Double(100, 20, 98, 48));
    g.fillRect(0, 0, 500, 100);

    g.setClip(new Rectangle2D.Double(200, 0, 98, 98));
    g.fillRect(0, 0, 500, 100);
    g.setColor(Color.red);
    g.clip(new RoundRectangle2D.Double(200, 0, 98, 98, 40, 40));
    g.fillRect(0, 0, 500, 100);
    g.setColor(Color.blue);
    g.clip(new Ellipse2D.Double(200, 0, 248, 98));
    g.fillRect(0, 0, 500, 100);
    return true;
  }

  @SuppressWarnings("unused")
  public static boolean t08StringsTest(Graphics2D vg, int repeat) {
    if (repeat != 0) {
      return false;
    }
    vg.setColor(Color.white);
    vg.fillRect(0, 0, 500, 100);

    vg.setColor(Color.black);
    Font serif = new Font("Serif", Font.PLAIN, 12);
    Font sansserif = new Font("SansSerif", Font.PLAIN, 12);
    Font dialog = new Font("Dialog", Font.PLAIN, 12);
    Font monospaced = new Font("Monospaced", Font.PLAIN, 12);

    // Font standard = new Font("TimesRoman", Font.PLAIN, 12);
    // Font standard = new Font("Impact", Font.PLAIN, 12);
    Font standard = serif;

    // Font standard = new Font("Lucida Times Unicode", Font.PLAIN, 12);
    // Font embedded = new Font("ZapfChancery", Font.PLAIN, 12);
    // Font embedded = new Font("Impact", Font.PLAIN, 12);
    Font embedded = new Font("Arial", Font.PLAIN, 12);
    // Font standard = new Font("Lucida Sans", Font.PLAIN, 12);
    // Font embedded = new Font("Monotype Corsiva", Font.PLAIN, 12);
    // Font embedded = new Font("Lucida Sans", Font.PLAIN, 12);
    // Font embedded = new Font("ZapfDingbats", Font.PLAIN, 12);

    String test = "test";
    vg.setFont(standard);
    vg.drawString(test, 10, 10);

    vg.setFont(embedded);
    vg.drawString(test, 10, 25);
    Font symbol = new Font("Symbol", Font.PLAIN, 12);
    vg.setFont(symbol);
    vg.drawString("ABC abc 123 .,!", 10, 40);

    vg.setFont(new Font("ZapfDingbats", Font.PLAIN, 12));
    vg.drawString("ABC abc 123 .,!", 10, 55);

    String ucs = "\u03b1 \u03b2 \u03b3 \u263a \u2665 \u2729 \u270c ";
    vg.setFont(serif);
    vg.drawString(ucs, 10, 70);
    vg.setFont(sansserif);
    vg.drawString(ucs, 10, 85);
    vg.setFont(dialog);
    vg.drawString(ucs, 150, 70);
    vg.setFont(monospaced);
    vg.drawString(ucs, 150, 85);

    String text = "Webswing";

    Font font = vg.getFont();
    double fw = 200 / 120.0;
    double fh = 100 / 120.0;
    for (int i = 1; i < 36; i++) {
      AffineTransform t = AffineTransform.getRotateInstance(Math.toRadians(10 * i));
      double s = 1.0 + i / 20.0;
      t.scale(fw / s, fh / s);
      if (i < 10) {
        Graphics2D ng = (Graphics2D) vg.create();
        ng.setTransform(AffineTransform.getRotateInstance(Math.toRadians(10 * i), 300, 40));
        ng.setFont(embedded);
        ng.drawString(ucs, 300, 40);
        ng.dispose();
      } else {
        vg.setFont(i < 15 ? embedded.deriveFont(t) : font.deriveFont(t));
        vg.drawString(text, 300, 40);
      }
    }

    vg.setColor(Color.BLUE);
    vg.setFont(font.deriveFont(AffineTransform.getScaleInstance(fw, fh)));
    vg.drawString(text, 400, 50);
    return true;
  }

  @SuppressWarnings("unused")
  public static boolean t09JButtonTest(Graphics2D g, int repeat) {
    if (repeat > 0) {
      return false;
    }
    JPanel scrP = new JPanel();
    scrP.add(new JLabel("test"));
    JScrollPane scroll = new JScrollPane(scrP);
    scroll.setPreferredSize(new Dimension(400, 80));
    scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    printJComponentHelper(g, scroll);
    return true;
  }

  @SuppressWarnings("unused")
  public static boolean t10CopyAreaTest(Graphics2D g, int repeat) {
    if (repeat != 0) {
      return false;
    }
    Color c = Color.ORANGE;
    g.setColor(c);
    g.fill(new Arc2D.Double(new Rectangle2D.Double(0, 0, 48, 30), 15, 250, Arc2D.OPEN));

    g.copyArea(0, 0, 50, 50, 0, 50);

    g.clipRect(50, 0, 25, 25);
    g.copyArea(0, 0, 50, 50, 50, 0);

    g.setClip(0, 0, 500, 100);

    g.translate(25, 25);
    g.copyArea(-25, -25, 50, 50, 100, 0);

    Image i = TestJsonGenerator.getImage(g instanceof WebGraphics);
    Graphics2D gx = (Graphics2D) i.getGraphics();
    gx.setColor(Color.blue);
    gx.fill(new Arc2D.Double(new Rectangle2D.Double(0, 0, 48, 30), 15, 250, Arc2D.OPEN));
    gx.copyArea(10, 10, 50, 50, 0, 50);
    gx.dispose();
    g.translate(150, -25);
    g.drawImage(i, 0, 0, null);
    return true;
  }

  @SuppressWarnings("unused")
  public static boolean t11MultiLevelGraphicsTest(Graphics2D g, int repeat) {
    if (repeat != 0) {
      return false;
    }
    g.setColor(Color.black);
    g.fillArc(100, 0, 50, 50, 0, 360);

    Graphics g2 = g.create(250, 50, 250, 50);
    g2.setColor(Color.red);
    g2.fillArc(0, 0, 50, 50, 0, 360);

    g.setColor(Color.orange);
    g.translate(50, -15);
    g.rotate(Math.PI / 4);
    g.scale(2, 2);
    g.fillRect(0, 0, 50, 50);

    g2.setColor(Color.blue);
    g2.translate(10, 10);
    g2.fillRect(0, 0, 20, 20);

    g.setColor(Color.yellow);
    g.translate(25, 25);
    g.scale(0.4, 0.4);
    g.rotate(Math.PI / 8);
    g.translate(10, 10);
    g.fillRect(0, 0, 20, 20);

    g2.dispose();

    g.setColor(Color.red);
    g.fillRect(10, 10, 10, 10);
    return true;
  }

  @SuppressWarnings("unused")
  public static boolean t12DrawWebImageTest(Graphics2D g, int repeat) {
    if (repeat != 0) {
      return false;
    }
    Image i = TestJsonGenerator.getImage(g instanceof WebGraphics);
    Graphics2D gx = (Graphics2D) i.getGraphics();
    t02FillRectTest(gx, 0);
    gx.dispose();
    g.setClip(new Arc2D.Double(new Rectangle2D.Double(0, 0, 500, 100), 15, 360, Arc2D.OPEN));
    g.translate(500, 100);
    g.rotate(Math.PI);
    g.drawImage(i, 0, 0, 100, 100, 400, 0, 500, 100, null);
    g.drawImage(i, 100, 0, 200, 100, 300, 0, 400, 100, null);
    g.drawImage(i, 200, 0, 300, 100, 200, 0, 300, 100, null);
    g.drawImage(i, 300, 0, 400, 100, 100, 0, 200, 100, null);
    g.drawImage(i, 400, 0, 500, 100, 0, 0, 100, 100, null);
    g.setColor(new Color(100, 100, 100, 100));
    g.fillRect(0, 0, 500, 100);
    return true;
  }

  @SuppressWarnings("unused")
  public static boolean t13ImageCacheTest(Graphics2D g, int repeat) throws IOException {
    if (repeat > 3) {
      return false;
    }
    switch (repeat) {
      case 0:
        t01DrawImageTest(g, 0);
        break;
      case 1:
        t03TransformTest(g, 0);
        break;
      case 2:
        t01DrawImageTest(g, 0);
        break;
      case 3:
        t03TransformTest(g, 0);
        break;
    }
    return true;
  }

  @SuppressWarnings("unused")
  public static boolean t14ImageCacheTest(Graphics2D g, int repeat) throws IOException {
    if (repeat > 1) {
      return false;
    }
    BufferedImage image = ImageIO.read(Tests.class.getClassLoader().getResource("ws.png"));
    BufferedImage a = ImageIO.read(Tests.class.getClassLoader().getResource("rollover_c.gif"));
    BufferedImage b = ImageIO.read(Tests.class.getClassLoader().getResource("rollover.gif"));
    DirectDrawServicesAdapter sa = new DirectDrawServicesAdapter();
    long h1 = sa.computeHash(a);
    long h2 = sa.computeHash(b);
    assert h1 != h2;
    switch (repeat) {
      case 0:
        g.drawImage(image, 0, 0, null);
        g.drawImage(b, 100, 0, null);
        g.drawImage(a, 150, 0, null);
        break;
      case 1:
        g.drawImage(image, 200, 0, null);
        g.drawImage(a, 300, 0, null);
        g.drawImage(b, 350, 0, null);
        break;
    }
    return true;
  }

  @SuppressWarnings("unused")
  public static boolean t15MultipleCopyAreaTestTest(Graphics2D g, int repeat) throws IOException {
    if (repeat != 0) {
      return false;
    }
    BufferedImage image = ImageIO.read(Tests.class.getClassLoader().getResource("ws.png"));
    g.drawImage(image, 0, 0, null);
    g.copyArea(0, 0, 120, 100, 130, 0);
    g.drawImage(image, 0, 30, null);
    g.copyArea(0, 0, 120, 100, 260, 0);
    g.drawImage(image, 0, 60, null);
    g.copyArea(0, 0, 120, 100, 390, 0);
    return true;
  }

  @SuppressWarnings("unused")
  public static boolean t16CompositeModesTest(Graphics2D g, int repeat) throws IOException {
    if (repeat != 0) {
      return false;
    }

    compositeTestOp(g, 0, 0, AlphaComposite.Src);
    compositeTestOp(g, 50, 0, AlphaComposite.SrcOver);
    compositeTestOp(g, 100, 0, AlphaComposite.SrcIn);
    compositeTestOp(g, 150, 0, AlphaComposite.SrcOut);
    compositeTestOp(g, 200, 0, AlphaComposite.SrcAtop);
    compositeTestOp(g, 250, 0, AlphaComposite.Xor);

    compositeTestOp(g, 0, 50, AlphaComposite.Dst);
    compositeTestOp(g, 50, 50, AlphaComposite.DstOver);
    compositeTestOp(g, 100, 50, AlphaComposite.DstIn);
    compositeTestOp(g, 150, 50, AlphaComposite.DstOut);
    compositeTestOp(g, 200, 50, AlphaComposite.DstAtop);
    compositeTestOp(g, 250, 50, AlphaComposite.Clear);

    return true;
  }

  @SuppressWarnings("unused")
  public static boolean t17SemitransparentCompositeModesTest(Graphics2D g, int repeat)
      throws IOException {
    if (repeat != 0) {
      return false;
    }

    compositeTestOp(g, 0, 0, AlphaComposite.SRC, 0.5f);
    compositeTestOp(g, 50, 0, AlphaComposite.SRC_OVER, 0.5f);
    compositeTestOp(g, 100, 0, AlphaComposite.SRC_IN, 0.5f);
    compositeTestOp(g, 150, 0, AlphaComposite.SRC_OUT, 0.5f);
    compositeTestOp(g, 200, 0, AlphaComposite.SRC_ATOP, 0.5f);
    compositeTestOp(g, 250, 0, AlphaComposite.XOR, 0.5f);

    compositeTestOp(g, 0, 50, AlphaComposite.DST, 0.5f);
    compositeTestOp(g, 50, 50, AlphaComposite.DST_OVER, 0.5f);
    compositeTestOp(g, 100, 50, AlphaComposite.DST_IN, 0.5f);
    compositeTestOp(g, 150, 50, AlphaComposite.DST_OUT, 0.5f);
    compositeTestOp(g, 200, 50, AlphaComposite.DST_ATOP, 0.5f);
    compositeTestOp(g, 250, 50, AlphaComposite.CLEAR, 0.5f);

    return true;
  }

  @SuppressWarnings("unused")
  public static boolean t18TransparentCompositeModesTest(Graphics2D g, int repeat)
      throws IOException {
    if (repeat != 0) {
      return false;
    }

    compositeTestOp(g, 0, 0, AlphaComposite.SRC, 0f);
    compositeTestOp(g, 50, 0, AlphaComposite.SRC_OVER, 0f);
    compositeTestOp(g, 100, 0, AlphaComposite.SRC_IN, 0f);
    compositeTestOp(g, 150, 0, AlphaComposite.SRC_OUT, 0f);
    compositeTestOp(g, 200, 0, AlphaComposite.SRC_ATOP, 0f);
    compositeTestOp(g, 250, 0, AlphaComposite.XOR, 0f);

    compositeTestOp(g, 0, 50, AlphaComposite.DST, 0f);
    compositeTestOp(g, 50, 50, AlphaComposite.DST_OVER, 0f);
    compositeTestOp(g, 100, 50, AlphaComposite.DST_IN, 0f);
    compositeTestOp(g, 150, 50, AlphaComposite.DST_OUT, 0f);
    compositeTestOp(g, 200, 50, AlphaComposite.DST_ATOP, 0f);
    compositeTestOp(g, 250, 50, AlphaComposite.CLEAR, 0f);

    return true;
  }

  private static void compositeTestOp(Graphics2D g, int x, int y, int compositeRule, float alpha) {
    compositeTestOp(g, x, y, AlphaComposite.getInstance(compositeRule, alpha));
  }

  private static void compositeTestOp(Graphics2D g, int x, int y, AlphaComposite c) {
    g.setPaint(
        new GradientPaint(new Point(x, y), Color.yellow, new Point(x + 50, y + 50), Color.green));
    g.fillRect(x, y, 50, 50);

    Image img = TestJsonGenerator.getImage(g instanceof WebGraphics, 50, 50);
    Graphics2D imgGraphics = (Graphics2D) img.getGraphics();
    imgGraphics.setColor(new Color(0, 0, 255));
    imgGraphics.fillRect(0, 0, 25, 25);
    imgGraphics.setComposite(c);
    imgGraphics.setColor(new Color(255, 0, 0));
    imgGraphics.fillOval(10, 10, 25, 25);

    g.drawImage(img, x, y, null);
    g.setColor(Color.black);
    g.drawString("" + c.getRule(), x + 30, y + 40);
  }

  @SuppressWarnings("unused")
  public static boolean t19CompositeImagesTest(Graphics2D g, int repeat) throws IOException {
    if (repeat != 0) {
      return false;
    }

    compositeImagesTestOp(g, 0, 0, AlphaComposite.Src);
    compositeImagesTestOp(g, 50, 0, AlphaComposite.SrcOver);
    compositeImagesTestOp(g, 100, 0, AlphaComposite.SrcIn);
    compositeImagesTestOp(g, 150, 0, AlphaComposite.SrcOut);
    compositeImagesTestOp(g, 200, 0, AlphaComposite.SrcAtop);
    compositeImagesTestOp(g, 250, 0, AlphaComposite.Xor);

    compositeImagesTestOp(g, 0, 50, AlphaComposite.Dst);
    compositeImagesTestOp(g, 50, 50, AlphaComposite.DstOver);
    compositeImagesTestOp(g, 100, 50, AlphaComposite.DstIn);
    compositeImagesTestOp(g, 150, 50, AlphaComposite.DstOut);
    compositeImagesTestOp(g, 200, 50, AlphaComposite.DstAtop);
    compositeImagesTestOp(g, 250, 50, AlphaComposite.Clear);

    return true;
  }

  @SuppressWarnings("unused")
  public static boolean t20SemitransparentCompositeImagesTest(Graphics2D g, int repeat)
      throws IOException {
    if (repeat != 0) {
      return false;
    }

    compositeImagesTestOp(g, 0, 0, AlphaComposite.SRC, 0.5f);
    compositeImagesTestOp(g, 50, 0, AlphaComposite.SRC_OVER, 0.5f);
    compositeImagesTestOp(g, 100, 0, AlphaComposite.SRC_IN, 0.5f);
    compositeImagesTestOp(g, 150, 0, AlphaComposite.SRC_OUT, 0.5f);
    compositeImagesTestOp(g, 200, 0, AlphaComposite.SRC_ATOP, 0.5f);
    compositeImagesTestOp(g, 250, 0, AlphaComposite.XOR, 0.5f);

    compositeImagesTestOp(g, 0, 50, AlphaComposite.DST, 0.5f);
    compositeImagesTestOp(g, 50, 50, AlphaComposite.DST_OVER, 0.5f);
    compositeImagesTestOp(g, 100, 50, AlphaComposite.DST_IN, 0.5f);
    compositeImagesTestOp(g, 150, 50, AlphaComposite.DST_OUT, 0.5f);
    compositeImagesTestOp(g, 200, 50, AlphaComposite.DST_ATOP, 0.5f);
    compositeImagesTestOp(g, 250, 50, AlphaComposite.CLEAR, 0.5f);

    return true;
  }

  @SuppressWarnings("unused")
  public static boolean t21TransparentCompositeImagesTest(Graphics2D g, int repeat)
      throws IOException {
    if (repeat != 0) {
      return false;
    }

    compositeImagesTestOp(g, 0, 0, AlphaComposite.SRC, 0f);
    compositeImagesTestOp(g, 50, 0, AlphaComposite.SRC_OVER, 0f);
    compositeImagesTestOp(g, 100, 0, AlphaComposite.SRC_IN, 0f);
    compositeImagesTestOp(g, 150, 0, AlphaComposite.SRC_OUT, 0f);
    compositeImagesTestOp(g, 200, 0, AlphaComposite.SRC_ATOP, 0f);
    compositeImagesTestOp(g, 250, 0, AlphaComposite.XOR, 0f);

    compositeImagesTestOp(g, 0, 50, AlphaComposite.DST, 0f);
    compositeImagesTestOp(g, 50, 50, AlphaComposite.DST_OVER, 0f);
    compositeImagesTestOp(g, 100, 50, AlphaComposite.DST_IN, 0f);
    compositeImagesTestOp(g, 150, 50, AlphaComposite.DST_OUT, 0f);
    compositeImagesTestOp(g, 200, 50, AlphaComposite.DST_ATOP, 0f);
    compositeImagesTestOp(g, 250, 50, AlphaComposite.CLEAR, 0f);

    return true;
  }

  private static void compositeImagesTestOp(Graphics2D g, int x, int y, int compositeRule,
      float alpha) {
    compositeImagesTestOp(g, x, y, AlphaComposite.getInstance(compositeRule, alpha));
  }

  private static void compositeImagesTestOp(Graphics2D g, int x, int y, AlphaComposite c) {
    g.setPaint(
        new GradientPaint(new Point(x, y), Color.yellow, new Point(x + 50, y + 50), Color.green));
    g.fillRect(x, y, 50, 50);

    Image oval = TestJsonGenerator.getImage(g instanceof WebGraphics);
    Graphics2D ovalGraphics = (Graphics2D) oval.getGraphics();
    ovalGraphics.setColor(new Color(255, 0, 0));
    ovalGraphics.fillOval(10, 10, 25, 25);

    Image img = TestJsonGenerator.getImage(g instanceof WebGraphics, 50, 50);
    Graphics2D imgGraphics = (Graphics2D) img.getGraphics();
    imgGraphics.setColor(new Color(0, 0, 255));
    imgGraphics.fillRect(0, 0, 25, 25);
    imgGraphics.setComposite(c);
    imgGraphics.drawImage(oval, 0, 0, null);

    g.drawImage(img, x, y, null);
    g.setColor(Color.black);
    g.drawString("" + c.getRule(), x + 30, y + 40);
  }

  @SuppressWarnings("unused")
  public static boolean t22MetalTooltipTest(Graphics2D g, int repeat) throws IOException {
    if (repeat != 0) {
      return false;
    }
    SliderDemo sd = new SliderDemo();
    printJComponentHelper(g, sd);
    return true;
  }

  @SuppressWarnings("unused")
  public static boolean t23OverlayedImagesWithTransparencyTest(Graphics2D g, int repeat)
      throws IOException {
    if (repeat != 0) {
      return false;
    }
    BufferedImage image = ImageIO.read(Tests.class.getClassLoader().getResource("java.png"));
    g.drawImage(image, 0, 0, 100, 100, null);
    g.setColor(Color.yellow);
    g.fill(new Rectangle(50, 0, 100, 100));
    g.drawImage(image, 50, 0, 100, 100, null);

    g.drawImage(image, 200, 0, 100, 100, null);
    g.setColor(new Color(255, 255, 0, 200));
    g.fill(new Rectangle(250, 0, 100, 100));
    g.drawImage(image, 250, 0, 100, 100, null);
    return true;
  }

  @SuppressWarnings("unused")
  public static boolean t24TransparentCompositeOverImageTest(Graphics2D g, int repeat)
      throws IOException {
    if (repeat != 0) {
      return false;
    }
    BufferedImage image = ImageIO.read(Tests.class.getClassLoader().getResource("ws.png"));
    g.drawImage(image, 0, 0, null);

    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
    g.setColor(Color.yellow);
    g.fillRect(0, 0, image.getWidth(), image.getHeight());

    return true;
  }

  @SuppressWarnings("unused")
  public static boolean t25TransparentFillOverImageTest(Graphics2D g, int repeat)
      throws IOException {
    if (repeat != 0) {
      return false;
    }
    BufferedImage image = ImageIO.read(Tests.class.getClassLoader().getResource("ws.png"));

    g.setBackground(Color.white);
    g.clearRect(0, 0, image.getWidth() + 100, image.getHeight());
    g.drawImage(image, 50, 0, null);
    g.setColor(new Color(0, 255, 255, 128));
    g.fillRect(0, 0, image.getWidth() + 100, image.getHeight());

    return true;
  }

  @SuppressWarnings("unused")
  public static boolean t26DrawImageWithBackgroundTest(Graphics2D g, int repeat) {
    if (repeat != 0) {
      return false;
    }
    Image img = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
    Graphics2D imgGraphics = (Graphics2D) img.getGraphics();
    imgGraphics.setColor(Color.blue);
    imgGraphics.fillRect(10, 10, 30, 30);
    imgGraphics.dispose();

    drawImagesWithBackground(g, img);

    return true;
  }

  @SuppressWarnings("unused")
  public static boolean t27DrawWebImageWithBackgroundTest(Graphics2D g, int repeat) {
    if (repeat != 0) {
      return false;
    }
    Image img = TestJsonGenerator.getImage(g instanceof WebGraphics, 50, 50);
    Graphics2D imgGraphics = (Graphics2D) img.getGraphics();
    imgGraphics.setColor(Color.red);
    imgGraphics.fillRect(10, 10, 30, 30);
    imgGraphics.dispose();

    drawImagesWithBackground(g, img);

    return true;
  }

  private static void drawImagesWithBackground(Graphics2D g, Image img) {
    g.setColor(Color.pink);
    g.fillRect(0, 0, 500, 100);

    Color opaque = Color.cyan;
    Color transparent = new Color(0, 255, 255, 128);

    g.drawImage(img, 0, 0, null);
    g.drawImage(img, 50, 0, opaque, null);
    g.drawImage(img, 100, 0, transparent, null);
    g.drawImage(img, 150, 0, 175, 25, 0, 0, 25, 25, null);
    g.drawImage(img, 200, 0, 225, 25, 0, 0, 25, 25, opaque, null);
    g.drawImage(img, 250, 0, 275, 25, 0, 0, 25, 25, transparent, null);
    g.drawImage(img, new AffineTransform(1, 0, 0, 1, 400, 0), null);
    g.drawImage(img, new AffineTransform(1, 0.5, 0, 1, 450, 0), null);
    g.drawImage(img, 5, 55, 40, 40, null);
    g.drawImage(img, 55, 55, 40, 40, opaque, null);
    // The next on isn't working in java and seems to be a bug on their side. I've sent them a
    // report.
    g.drawImage(img, 105, 55, 40, 40, transparent, null);
    g.drawImage(img, 150, 50, 200, 100, 0, 0, 25, 25, null);
    g.drawImage(img, 200, 50, 250, 100, 0, 0, 25, 25, opaque, null);
    // The next on isn't working in java and seems to be a bug on their side. I've sent them a
    // report.
    g.drawImage(img, 250, 50, 300, 100, 0, 0, 25, 25, transparent, null);
    g.drawImage(img, new AffineTransform(0.5, 0, 0, 0.5, 400, 50), null);
    g.drawImage(img, new AffineTransform(0.5, 0.5, 0, 0.5, 450, 50), null);
  }

  @SuppressWarnings("unused")
  public static boolean t28VariousImageTypesTest(Graphics2D g, int repeat) throws IOException {
    if (repeat != 0) {
      return false;
    }
    BufferedImage image = ImageIO.read(Tests.class.getClassLoader().getResource("rgb.png"));
    g.setBackground(Color.yellow);
    g.clearRect(0, 0, 350, 100);
    g.drawImage(copyImage(image, BufferedImage.TYPE_INT_RGB), 0, 0, 50, 50, null);
    g.drawImage(copyImage(image, BufferedImage.TYPE_INT_ARGB), 50, 0, 50, 50, null);
    g.drawImage(copyImage(image, BufferedImage.TYPE_INT_ARGB_PRE), 100, 0, 50, 50, null);
    g.drawImage(copyImage(image, BufferedImage.TYPE_INT_BGR), 150, 0, 50, 50, null);
    g.drawImage(copyImage(image, BufferedImage.TYPE_3BYTE_BGR), 200, 0, 50, 50, null);
    g.drawImage(copyImage(image, BufferedImage.TYPE_4BYTE_ABGR), 250, 0, 50, 50, null);
    g.drawImage(copyImage(image, BufferedImage.TYPE_4BYTE_ABGR_PRE), 300, 0, 50, 50, null);
    g.drawImage(copyImage(image, BufferedImage.TYPE_USHORT_565_RGB), 0, 50, 50, 50, null);
    g.drawImage(copyImage(image, BufferedImage.TYPE_USHORT_555_RGB), 50, 50, 50, 50, null);
    g.drawImage(copyImage(image, BufferedImage.TYPE_BYTE_GRAY), 100, 50, 50, 50, null);
    g.drawImage(copyImage(image, BufferedImage.TYPE_USHORT_GRAY), 150, 50, 50, 50, null);
    g.drawImage(copyImage(image, BufferedImage.TYPE_BYTE_BINARY), 200, 50, 50, 50, null);
    g.drawImage(copyImage(image, BufferedImage.TYPE_BYTE_INDEXED), 250, 50, 50, 50, null);
    return true;
  }

  @SuppressWarnings("unused")
  public static boolean t29BiasedLinesTest(Graphics2D g, int repeat) {
    if (repeat != 0) {
      return false;
    }
    g.setTransform(new AffineTransform(5, 0, 0, 5, 5, 5));
    filledThenStroked(g);
    g.setTransform(new AffineTransform(4, 0, 0, 4, 10, 10));
    filledThenStroked(g);
    g.setTransform(new AffineTransform(3, 0, 0, 3, 15, 15));
    filledThenStroked(g);
    g.setTransform(new AffineTransform(2, 0, 0, 2, 20, 20));
    filledThenStroked(g);
    g.setTransform(new AffineTransform(1.5, 0, 0, 1.5, 25, 25));
    filledThenStroked(g);
    g.setTransform(new AffineTransform(1, 0, 0, 1, 30, 30));
    filledThenStroked(g);
    g.setTransform(new AffineTransform(0.5, 0, 0, 0.5, 35, 35));
    filledThenStroked(g);

    g.setTransform(new AffineTransform(5, 0, 0, 5, 90, 5));
    strokedThenFilled(g);
    g.setTransform(new AffineTransform(4, 0, 0, 4, 95, 10));
    strokedThenFilled(g);
    g.setTransform(new AffineTransform(3, 0, 0, 3, 100, 15));
    strokedThenFilled(g);
    g.setTransform(new AffineTransform(2, 0, 0, 2, 105, 20));
    strokedThenFilled(g);
    g.setTransform(new AffineTransform(1.5, 0, 0, 1.5, 110, 25));
    strokedThenFilled(g);
    g.setTransform(new AffineTransform(1, 0, 0, 1, 115, 30));
    strokedThenFilled(g);
    g.setTransform(new AffineTransform(0.5, 0, 0, 0.5, 120, 35));
    strokedThenFilled(g);

    /**
     * The next tests compare stroke width rounding when using fraction width and different scales.
     * Due to XOR if stroke widths are equal nothing will be drawn.
     */
    g.setTransform(new AffineTransform());

    g.setColor(Color.gray);
    g.fillRect(175, 30, 170, 30);
    g.setColor(Color.BLACK);

    g.setComposite(AlphaComposite.Xor);

    g.setStroke(new BasicStroke(0.1f));
    g.drawRect(175, 5, 40, 40);
    g.setStroke(new BasicStroke(0.1f));
    g.drawRect(175, 5, 40, 40);

    g.setStroke(new BasicStroke(1.5f));
    g.drawRect(225, 5, 40, 40);
    g.setStroke(new BasicStroke(1.5f));
    g.drawRect(225, 5, 40, 40);

    g.setStroke(new BasicStroke(1f));
    g.drawRect(175, 55, 40, 40);
    g.setStroke(new BasicStroke(1f));
    g.drawRect(175, 55, 40, 40);

    g.setStroke(new BasicStroke(2f));
    g.drawRect(225, 55, 40, 40);
    g.setStroke(new BasicStroke(2f));
    g.drawRect(225, 55, 40, 40);

    g.setStroke(new BasicStroke(3f));
    g.drawRect(275, 5, 40, 40);
    g.setTransform(new AffineTransform(2, 0, 0, 2, 275, 5));
    g.setStroke(new BasicStroke(1.5f));
    g.drawRect(0, 0, 20, 20);

    g.setStroke(new BasicStroke(12f));
    g.setTransform(new AffineTransform(1d, 0, 0, 1d / 4, 275, 55));
    g.drawRect(0, 0, 120, 120);
    g.setTransform(new AffineTransform(12, 0, 0, 3, 275, 55));
    g.setStroke(new BasicStroke(1f));
    g.drawRect(0, 0, 10, 10);

    g.setTransform(new AffineTransform());
    Font sansserif = new Font("SansSerif", Font.PLAIN, 18);
    g.setFont(sansserif);
    g.drawString("test@123&%=-*{};'/.,", 175, 50);

    // g.setStroke(new BasicStroke(12f));
    // g.setTransform(new AffineTransform(1d / 4, 0, 0, 1d / 3, 345, 5));
    // g.drawRect(0, 0, 160, 180);
    // g.setTransform(new AffineTransform(2, 0, 0, 3, 345, 5));
    // g.setStroke(new BasicStroke(1.45f));
    // g.drawRect(0, 0, 20, 20);
    //
    return true;
  }

  @SuppressWarnings("unused")
  public static boolean t30FontConfigTest(Graphics2D g, int repeat) {
    if (repeat != 0) {
      return false;
    }
    JPanel p = new JPanel(new GridLayout(5, 1));
    String text =
        "<html><span style=\"font-size:2em;text-decoration:underline;\">The</span> <span style=\"font-size:1.5em;\"><b>Quick</b></span> <span style=\"font-size:1em;color:white;background-color:red;\">Brown</span> <span style=\"font-size:1em\">Fox <i>Jumps</i> <span style=\"text-decoration:line-through;\"> Over </span> </span><span style=\"font-size:1em\"> <sup>The</sup> <b><i>Lazy</i></b> <sub>Dog</sub> </span>123���������������<span style=\"font-size:0.5em\">test </span></html>";
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    final String[] families = ge.getAvailableFontFamilyNames();
    for (int i = 0; i < Math.min(families.length, 5); i++) {
      Font f = new Font(families[i], Font.PLAIN, 12);
      JLabel theLabel = new JLabel(text);
      p.add(theLabel);
    }
    printJComponentHelper(g, p);

    return true;
  }

  public static boolean t31CustomPaintClassTest(Graphics2D g, int repeat) {
    if (repeat != 0) {
      return false;
    }
    g.setPaint(new CustomPaint());
    g.fill(new RoundRectangle2D.Double(0, 0, 200, 100, 40, 40));
    g.setStroke(new BasicStroke(17, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 2.5f,
        new float[] {3, 20, 40, 20}, 10));
    g.drawPolyline(new int[] {220, 270, 320, 370, 420, 470}, new int[] {5, 95, 5, 95, 5, 95}, 6);

    return true;
  }

  public static boolean t32XorModeTest(Graphics2D g, int repeat) {
    if (repeat > 0) {
      return false;
    }
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    Font sansserif = new Font("SansSerif", Font.PLAIN, 18);
    g.setFont(sansserif);
    g.setStroke(new BasicStroke(20, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
    // fill with gray
    int y = 30;
    g.setColor(Color.GRAY);
    g.fillRect(0, 0, 500, 100);

    g.setColor(Color.GREEN);
    g.drawLine(10, 10, 480, 0);
    g.fillRect(0, 0, 10, 10);
    g.setColor(Color.BLACK);
    g.drawLine(10, 30, 480, 20);
    g.setColor(new Color(0, 0, 0, 0));
    g.fillRect(0, 0, 5, 100);
    g.setColor(Color.BLACK);

    g.setStroke(new BasicStroke(y * 2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
    g.setXORMode(Color.WHITE);
    g.fillRect(0, 0, 5, 5);
    g.drawLine(50, y, 100, y);
    g.setXORMode(Color.YELLOW);
    g.drawLine(100, y, 150, y);
    g.setXORMode(Color.GRAY);
    g.drawLine(150, y, 200, y);
    g.setColor(Color.RED);
    g.drawLine(200, y, 250, y);
    g.setColor(new Color(0, 0, 0, 100));
    g.drawLine(250, y, 300, y);
    g.setColor(Color.BLACK);
    g.setPaint(new GradientPaint(300, y, Color.WHITE, 350, y, Color.black));
    g.drawLine(300, y, 350, y);

    g.setPaintMode();
    g.setColor(Color.BLACK);
    g.drawString("testString", 10, y + 30);
    g.setXORMode(Color.GRAY);
    g.drawString("testString123", 10, y + 30);
    return true;
  }

  public static boolean t33RenderCancelledFallbackTest(Graphics2D g, int repeat) {
    if (repeat > 0) {
      return false;
    }
    System.setProperty(WebImage.FALLBACK_PROPERTY, 100 + "");
    g.setColor(Color.BLACK);
    g.setTransform(new AffineTransform(1.1, 0.001, -0.001, 1.1, 2, 2));
    for (int i = 0; i < 100; i++) {
      g.drawLine(0, i * 3, i * 3, 0);
    }
    g.setTransform(new AffineTransform(1, 0, 0, 1, 0, 0));
    g.copyArea(0, 0, 1, 1, 1, 1); // cancels the fallback
    g.setTransform(new AffineTransform(1.1, 0.001, -0.001, 1.1, 2, 2));
    for (int i = 100; i < 130; i++) {
      g.drawLine(0, i * 3, i * 3, 0);
    }
    return true;
  }

  public static boolean t34RenderFallbackWithCopyAreaTest(Graphics2D g, int repeat) {
    if (repeat > 0) {
      return false;
    }
    System.setProperty(WebImage.FALLBACK_PROPERTY, 100 + "");
    drawlines(g);

    return true;
  }

  public static boolean t35RenderNoFallbackWithCopyAreaTest(Graphics2D g, int repeat) {
    if (repeat > 0) {
      return false;
    }
    System.setProperty(WebImage.FALLBACK_PROPERTY, -1 + "");
    drawlines(g);

    return true;
  }

  public static boolean t36DPRbasedBiasCalculationTest(Graphics2D g, int repeat) {
    if (repeat > 0) {
      return false;
    }
    g.setColor(Color.green);
    Path2D p = new Path2D.Double();
    p.moveTo(0, 25);
    p.lineTo(499, 25);
    g.draw(p);
    g.setColor(Color.red);
    g.fillRect(0, 0, 500, 25);
    g.setColor(Color.blue);
    g.fillRect(0, 26, 500, 27);
    return true;
  }

  public static boolean t37ScaledTextRenderingTest(Graphics2D g, int repeat) {
    if (repeat > 0) {
      return false;
    }
    g.setFont(new Font("dialog", Font.PLAIN, 12));
    String ys = "iiiiwiwiiiii";
    int yswidth = g.getFontMetrics().stringWidth(ys);
    String Ms = "wwwwwwwwwwwwwwwww";
    int Mswidth = g.getFontMetrics().stringWidth(Ms);

    g.setColor(Color.BLACK);
    g.drawLine(0, 10, yswidth, 10);
    g.setColor(Color.RED);
    g.drawLine(yswidth, 10, Mswidth + yswidth, 10);
    g.drawString(ys + Ms, 0, 10);
    g.setColor(Color.BLACK);
    g.drawString(ys, 0, 12);
    g.drawString(Ms, yswidth, 12);

    g.translate(0, 20);
    g.scale(2, 2);
    yswidth = g.getFontMetrics().stringWidth(ys);
    Mswidth = g.getFontMetrics().stringWidth(Ms);

    g.setColor(Color.BLACK);
    g.drawLine(0, 10, yswidth, 10);
    g.setColor(Color.RED);
    g.drawLine(yswidth, 10, Mswidth + yswidth, 10);
    g.drawString(ys + Ms, 0, 10);
    g.setColor(Color.BLACK);
    g.drawString(ys, 0, 12);
    g.drawString(Ms, yswidth, 12);

    g.translate(0, 20);
    g.scale(0.5, 0.5);
    yswidth = g.getFontMetrics().stringWidth(ys);
    Mswidth = g.getFontMetrics().stringWidth(Ms);

    g.setColor(Color.BLACK);
    g.drawLine(0, 10, yswidth, 10);
    g.setColor(Color.RED);
    g.drawLine(yswidth, 10, Mswidth + yswidth, 10);
    g.drawString(ys + Ms, 0, 10);

    return true;
  }

  private static void drawlines(Graphics2D g) {
    for (int j = 0; j < 20; j++) {
      if (j != 0) {
        g.setClip(0, 0, 500, 100);
        g.copyArea(0, 0, 500, 100, 20, 1);
      }
      g.setColor(Color.BLACK);
      g.setBackground(Color.white);
      g.setClip(0, 0, 20, 100);
      g.clearRect(0, 0, 20, 100);
      for (int i = 0; i < 100; i++) {
        g.drawLine(0, i * 4, i * i, 0);
      }
    }
  }

  private static void filledThenStroked(Graphics2D g) {
    g.setColor(Color.blue);
    g.fillRect(0, 0, 15, 15);
    g.setColor(Color.red);
    g.drawRect(0, 0, 15, 15);
  }

  private static void strokedThenFilled(Graphics2D g) {
    g.setColor(Color.red);
    g.drawRect(0, 0, 15, 15);
    g.setColor(Color.blue);
    g.fillRect(0, 0, 15, 15);
  }

  private static Image copyImage(BufferedImage image, int type) {
    BufferedImage copy = new BufferedImage(image.getWidth(), image.getHeight(), type);
    Graphics2D g = copy.createGraphics();
    g.drawImage(image, 0, 0, null);
    g.dispose();
    return copy;
  }

  private static void printJComponentHelper(Graphics2D g, JComponent c) {
    JFrame jf = new JFrame("");
    jf.setSize(500, 100);
    JPanel sd = new JPanel();
    sd.setPreferredSize(new Dimension(500, 100));
    sd.add(c);
    jf.add(sd);
    jf.pack();
    sd.print(g);
  }

  public static void main(String[] args) {
    JFrame frame = new JFrame("tests");

    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    JPanel content = new JPanel();
    content.setLayout(new GridBagLayout());
    int y = 0;
    for (String m : TestJsonGenerator.getTestMethods()) {
      Image image = getImage(m, true);
      content.add(new JLabel(new ImageIcon(image)), new GridBagConstraints(0, y, 1, 1, 0, 0,
          GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
      image = getImage(m, false);
      content.add(new JLabel(new ImageIcon(image)), new GridBagConstraints(1, y++, 1, 1, 0, 0,
          GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    }
    frame.getContentPane().add(new JScrollPane(content));
    frame.pack();
    frame.setSize(1280, 700);
    frame.setVisible(true);
  }

  public static Image getImage(String name, boolean web) {
    Image i = TestJsonGenerator.getImage(web);
    Graphics2D g = (Graphics2D) i.getGraphics();
    try {
      Tests.class.getDeclaredMethod(name, Graphics2D.class, int.class).invoke(null, g, 0);
    } catch (Exception e) {
      e.printStackTrace();
    }
    g.dispose();
    return i;
  }
}
