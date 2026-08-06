package org.webswing.directdraw.model;

import org.webswing.directdraw.DirectDraw;
import org.webswing.directdraw.model.GlyphListConst.StringConstValue;
import org.webswing.directdraw.proto.Directdraw.DrawConstantProto;
import org.webswing.directdraw.util.DirectDrawUtils;
import org.webswing.directdraw.util.DrawConstantPool;
import sun.font.GlyphList;
import sun.java2d.loops.FontInfo;

import java.awt.Font;
import java.awt.geom.AffineTransform;
import java.util.List;

@SuppressWarnings("restriction")
public class GlyphListConst extends CompositeDrawConstantHolder<StringConstValue> {

  public GlyphListConst(DirectDraw ctx, String string, Font f, double x, double y,
      AffineTransform tx) {
    super(ctx);
    value = new StringConstValue(string, get(f), x, y, get(new AffineTransform(tx)));
  }

  @Override
  public void expandAndCacheConstants(List<DrawConstantProto> protos, DrawConstantPool cache) {
    char[] chars = new char[value.string.length()];
    value.string.getChars(0, value.string.length(), chars, 0);
    int[] pts = new int[2 * chars.length]; // (x,y) location per char
    int minX = 0;
    int minY = 0;
    int maxX = 0;
    int maxY = 0; // size of drawing area
    int[] ids = new int[2 + chars.length]; // address of the drawing area size + address of
                                           // locations constant + address of glyph constant per
                                           // char
    FontInfo fontInfo = DirectDrawUtils.getFontInfo(value.font);
    String fontDescriptor = get(DirectDrawUtils.fontInfoDescriptor(value.font, fontInfo)); // font
                                                                                           // description
                                                                                           // to use
                                                                                           // as
                                                                                           // part
                                                                                           // of key
                                                                                           // in
                                                                                           // cache
    GlyphList gl = GlyphList.getInstance();
    try {
      gl.setFromString(fontInfo, value.string, (float) value.x, (float) value.y);
      gl.startGlyphIteration();
      int numglyphs = gl.getNumGlyphs();
      // create glyph reference for each char in the string
      for (int i = 0; i < numglyphs; i++) {
        gl.setGlyphIndex(i);
        // add location of char i
        int metrics[] = gl.getMetrics();
        pts[i * 2] = metrics[0]; // x
        pts[i * 2 + 1] = metrics[1]; // y
        minX = Math.min(minX, metrics[0]);
        minY = Math.min(minY, metrics[1]);
        maxX = Math.max(maxX, metrics[0] + metrics[2]);
        maxY = Math.max(maxY, metrics[1] + metrics[3]);
        GlyphKeyConst glyphConst = new GlyphKeyConst(getContext(), fontDescriptor, chars[i], gl);
        int id = cache.addToCache(protos, glyphConst);
        glyphConst.clear();
        ids[i + 2] = id;
      }
      // create a points proto for size of drawing area and store to cache:
      PointsConst size =
          new PointsConst(getContext(), new int[] {minX, minY, maxX - minX, maxY - minY});
      int sizeId = cache.addToCache(protos, size);
      ids[0] = sizeId;
      // create a points proto for x,y locations and store to cache:
      for (int i = 0; i < chars.length; i++) {
        pts[i * 2] = pts[i * 2] - minX; // x
        pts[i * 2 + 1] = pts[i * 2 + 1] - minY; // y
      }
      PointsConst points = new PointsConst(getContext(), pts);
      int id = cache.addToCache(protos, points);
      ids[1] = id;
      // create combined constant and set address
      CombinedConst constIds = new CombinedConst(getContext(), ids);
      int thisId = cache.addToCache(protos, constIds);
      this.setId(thisId);
    } finally {
      gl.dispose();
    }
  }

  @Override
  public int getExpandedConstantCount() {
    return value.string.length() + 3;
  }

  public static class StringConstValue {
    String string;
    Font font;
    double x;
    double y;
    AffineTransform transform;

    public StringConstValue(String string, Font f, double x, double y, AffineTransform tx) {
      super();
      this.string = string;
      this.font = f;
      this.x = x;
      this.y = y;
      this.transform = tx;
    }

    public String getString() {
      return string;
    }

    public double getX() {
      return x;
    }

    public double getY() {
      return y;
    }

    public Font getFont() {
      return font;
    }

    public AffineTransform getTransform() {
      return transform;
    }

  }
}
