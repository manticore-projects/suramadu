package org.webswing.directdraw.model;

import com.google.protobuf.ByteString;
import org.webswing.directdraw.DirectDraw;
import org.webswing.directdraw.proto.Directdraw.GlyphProto;
import org.webswing.directdraw.util.DirectDrawUtils;
import sun.font.GlyphList;

@SuppressWarnings("restriction")
public class GlyphKeyConst extends DrawConstant<String> {
  private String key;
  private char c;
  private GlyphList gl;

  public GlyphKeyConst(DirectDraw ctx, String key, char c, GlyphList gl) {
    super(ctx);
    this.key = key;
    this.c = c;
    this.gl = gl;
  }

  @Override
  public Object toMessage() {
    GlyphProto.Builder glyphProto = GlyphProto.newBuilder();
    int[] metrics = gl.getMetrics();
    if (metrics[2] != 0 && metrics[3] != 0) {
      byte[] png = DirectDrawUtils.toPNG(getContext(), gl.getGrayBits(), metrics[2], metrics[3]);
      glyphProto.setData(ByteString.copyFrom(png));
    }
    return glyphProto.build();
  }

  public void clear() {
    this.gl = null;
  }

  @Override
  public String getValue() {
    return this.key + this.c;
  }

  @Override
  public int hashCode() {
    return (this.key + this.c).hashCode();
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof GlyphKeyConst gkc && (this.key + this.c).equals(gkc.getValue());
  }

  @Override
  public String getFieldName() {
    return "glyph";
  }
}
