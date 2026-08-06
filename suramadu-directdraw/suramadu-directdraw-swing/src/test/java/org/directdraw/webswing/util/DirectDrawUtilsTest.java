package org.directdraw.webswing.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.webswing.directdraw.DirectDraw;
import org.webswing.directdraw.model.DrawInstruction;
import org.webswing.directdraw.toolkit.DrawInstructionFactory;
import org.webswing.directdraw.toolkit.WebGraphics;
import org.webswing.directdraw.toolkit.WebImage;
import org.webswing.directdraw.util.DirectDrawUtils;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DirectDrawUtilsTest {

  DirectDraw dd;
  private DrawInstructionFactory f;

  @BeforeEach
  void setUp() throws Exception {
    dd = new DirectDraw();
    f = dd.getInstructionFactory();
  }

  @Test
  void testGroupedGraphicsCreate() {
    WebImage webImage = new WebImage(dd, 10, 10);
    List<DrawInstruction> inst = new ArrayList<DrawInstruction>();

    inst.add(f.createGraphics((WebGraphics) webImage.getGraphics())); // merged in one graphics
                                                                      // create
    inst.add(f.transform(new AffineTransform(1, 0, 0, 1, 10, 10)));
    inst.add(f.transform(new AffineTransform(1, 0, 0, 1, 10, 10)));
    inst.add(f.setPaint(Color.black));
    inst.add(f.setPaint(Color.blue));
    inst.add(f.setComposite(AlphaComposite.Dst));
    inst.add(f.setComposite(AlphaComposite.Src));
    inst.add(f.setStroke(new BasicStroke(1)));
    inst.add(f.setStroke(new BasicStroke(2)));

    inst.add(f.draw(new Rectangle(), null)); // draw

    inst.add(f.transform(new AffineTransform(1, 0, 0, 1, 10, 10))); // ignored
    inst.add(f.setPaint(Color.black));
    inst.add(f.setComposite(AlphaComposite.Dst));
    inst.add(f.setStroke(new BasicStroke(1)));

    DirectDrawUtils.optimizeInstructions(dd, inst);
    assertEquals(2, inst.size(), "Count not valid");
    assertEquals(new AffineTransform(1, 0, 0, 1, 20, 20), inst.get(0).getArg(1).getValue(),
        "Transform not expected");
    assertEquals(new BasicStroke(2), inst.get(0).getArg(2).getValue(), "Stroke not expected");
    assertEquals(AlphaComposite.Src, inst.get(0).getArg(3).getValue(), "Composite not expected");
    assertEquals(Color.blue, inst.get(0).getArg(4).getValue(), "Color not expected");
  }

  @Test
  void testUnusedGraphicsCreate() {
    WebImage webImage = new WebImage(dd, 10, 10);
    List<DrawInstruction> inst = new ArrayList<DrawInstruction>();

    inst.add(f.createGraphics((WebGraphics) webImage.getGraphics())); // ignored because not used
    inst.add(f.transform(new AffineTransform(1, 0, 0, 1, 10, 10)));
    inst.add(f.transform(new AffineTransform(1, 0, 0, 1, 10, 10)));
    inst.add(f.setPaint(Color.black));
    inst.add(f.setPaint(Color.blue));
    inst.add(f.setComposite(AlphaComposite.Dst));
    inst.add(f.setComposite(AlphaComposite.Src));
    inst.add(f.setStroke(new BasicStroke(1)));
    inst.add(f.setStroke(new BasicStroke(2)));

    inst.add(f.createGraphics((WebGraphics) webImage.getGraphics())); // merged in one graphics
                                                                      // create
    inst.add(f.transform(new AffineTransform(1, 0, 0, 1, 10, 10)));
    inst.add(f.setPaint(Color.blue));
    inst.add(f.setComposite(AlphaComposite.Dst));
    inst.add(f.setStroke(new BasicStroke(2)));

    inst.add(f.draw(new Rectangle(), null)); // draw

    inst.add(f.createGraphics((WebGraphics) webImage.getGraphics())); // ignored
    inst.add(f.transform(new AffineTransform(1, 0, 0, 1, 10, 10)));
    inst.add(f.setPaint(Color.black));
    inst.add(f.setComposite(AlphaComposite.Dst));
    inst.add(f.setStroke(new BasicStroke(1)));

    DirectDrawUtils.optimizeInstructions(dd, inst);
    assertEquals(2, inst.size(), "Count not valid");
    assertEquals(new AffineTransform(1, 0, 0, 1, 10, 10), inst.get(0).getArg(1).getValue(),
        "Transform not expected");
    assertEquals(new BasicStroke(2), inst.get(0).getArg(2).getValue(), "Stroke not expected");
    assertEquals(AlphaComposite.Dst, inst.get(0).getArg(3).getValue(), "Composite not expected");
    assertEquals(Color.blue, inst.get(0).getArg(4).getValue(), "Color not expected");
  }

  @Test
  void testMergedPaints() {
    WebImage webImage = new WebImage(dd, 10, 10);
    List<DrawInstruction> inst = new ArrayList<DrawInstruction>();

    inst.add(f.createGraphics((WebGraphics) webImage.getGraphics())); // graphics

    inst.add(f.draw(new Rectangle(), null)); // draw

    inst.add(f.setPaint(new TexturePaint(new BufferedImage(10, 10, BufferedImage.TYPE_3BYTE_BGR),
        new Rectangle()))); // merged
    inst.add(f.setPaint(Color.green));

    inst.add(f.draw(new Rectangle(), null)); // draw

    DirectDrawUtils.optimizeInstructions(dd, inst);
    assertEquals(4, inst.size(), "Count not valid");
    assertEquals(Color.green, inst.get(2).getArg(0).getValue(), "Color not expected");
  }

  @Test
  void testMergedTransforms() {
    WebImage webImage = new WebImage(dd, 10, 10);
    List<DrawInstruction> inst = new ArrayList<DrawInstruction>();

    inst.add(f.createGraphics((WebGraphics) webImage.getGraphics())); // graphics

    inst.add(f.draw(new Rectangle(), null)); // draw

    inst.add(f.transform(new AffineTransform(1, 0, 0, 1, 10, 10)));
    inst.add(f.transform(new AffineTransform(1, 0, 0, 1, 10, 60)));

    inst.add(f.draw(new Rectangle(), null)); // draw

    DirectDrawUtils.optimizeInstructions(dd, inst);
    assertEquals(4, inst.size(), "Count not valid");
    assertEquals(new AffineTransform(1, 0, 0, 1, 20, 70), inst.get(2).getArg(0).getValue(),
        "Transform not expected");
  }

  @Test
  void testMergedComposites() {
    WebImage webImage = new WebImage(dd, 10, 10);
    List<DrawInstruction> inst = new ArrayList<DrawInstruction>();

    inst.add(f.createGraphics((WebGraphics) webImage.getGraphics())); // graphics

    inst.add(f.draw(new Rectangle(), null)); // draw

    inst.add(f.setComposite(AlphaComposite.Dst));
    inst.add(f.setComposite(AlphaComposite.SrcAtop));

    inst.add(f.draw(new Rectangle(), null)); // draw

    DirectDrawUtils.optimizeInstructions(dd, inst);
    assertEquals(4, inst.size(), "Count not valid");
    assertEquals(AlphaComposite.SrcAtop, inst.get(2).getArg(0).getValue(),
        "Composite not expected");
  }

  @Test
  void testCreateAfterTransform() {
    WebImage webImage = new WebImage(dd, 10, 10);
    List<DrawInstruction> inst = new ArrayList<DrawInstruction>();

    WebGraphics g1 = (WebGraphics) webImage.getGraphics();
    inst.add(f.createGraphics(g1));
    inst.add(f.draw(new Rectangle(0, 0, 1, 1), null));
    inst.add(f.transform(new AffineTransform(1, 0, 0, 1, 5, 3)));

    WebGraphics g2 = (WebGraphics) webImage.getGraphics();
    inst.add(f.createGraphics(g2));
    inst.add(f.draw(new Rectangle(0, 0, 1, 1), null));
    inst.add(f.disposeGraphics(g2));

    inst.add(f.switchGraphics(g1));
    inst.add(f.transform(new AffineTransform(1, 0, 0, 1, -5, -3)));
    inst.add(f.draw(new Rectangle(0, 0, 1, 1), null));

    DirectDrawUtils.optimizeInstructions(dd, inst);

    List<DrawInstruction> originalInstructions = new ArrayList<DrawInstruction>(inst);
    assertEquals(9, inst.size());
    assertEquals(originalInstructions.get(0), inst.get(0), "Create graphics 1");
    assertEquals(originalInstructions.get(1), inst.get(1), "Draw rectangle 1");
    assertEquals(new AffineTransform(1, 0, 0, 1, 5, 3), inst.get(2).getArg(0).getValue(),
        "Transform 1");

    assertEquals(originalInstructions.get(3), inst.get(3), "Create graphics 2");
    assertEquals(originalInstructions.get(4), inst.get(4), "Draw rectangle 2");
    assertEquals(originalInstructions.get(5), inst.get(5), "Dispose graphics 2");

    assertEquals(originalInstructions.get(6), inst.get(6), "Switch graphics");
    assertEquals(new AffineTransform(1, 0, 0, 1, -5, -3), inst.get(7).getArg(0).getValue(),
        "Transform 2");
    assertEquals(originalInstructions.get(8), inst.get(8), "Draw rectangle 2");
  }

  @Test
  void testCreateGraphicsWithSingleTransform() {
    WebImage webImage = new WebImage(dd, 10, 10);
    List<DrawInstruction> inst = new ArrayList<DrawInstruction>();

    WebGraphics g1 = (WebGraphics) webImage.getGraphics();
    inst.add(f.createGraphics(g1));
    inst.add(f.draw(new Rectangle(0, 0, 1, 1), null));

    WebGraphics g2 = (WebGraphics) webImage.getGraphics();
    inst.add(f.createGraphics(g2));
    inst.add(f.transform(new AffineTransform(1, 0, 0, 1, -11, -11)));

    inst.add(f.switchGraphics(g1));
    inst.add(f.draw(new Rectangle(0, 0, 1, 1), null));

    inst.add(f.switchGraphics(g2));
    inst.add(f.disposeGraphics(g2));

    DirectDrawUtils.optimizeInstructions(dd, inst);

    List<DrawInstruction> originalInstructions = new ArrayList<DrawInstruction>(inst);
    assertEquals(4, inst.size());
    assertEquals(originalInstructions.get(0), inst.get(0), "Create graphics 1");
    assertEquals(originalInstructions.get(1), inst.get(1), "Draw rectangle 1");

    assertEquals(originalInstructions.get(2), inst.get(2), "Switch graphics");
    assertEquals(originalInstructions.get(3), inst.get(3), "Draw rectangle 2");
  }
}
