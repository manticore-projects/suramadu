package org.webswing.directdraw.model;

import org.webswing.directdraw.DirectDraw;
import org.webswing.directdraw.proto.Directdraw.ColorProto;

import java.awt.Color;

public class ColorConst extends ImmutableDrawConstantHolder<Color> {

  public ColorConst(DirectDraw context, Color value) {
    super(context, value);
  }

  @Override
  public String getFieldName() {
    return "color";
  }

  @Override
  public ColorProto toMessage() {
    ColorProto.Builder model = ColorProto.newBuilder();
    model.setRgba(toRGBA(value));
    return model.build();
  }

  public static int toRGBA(Color color) {
    return (color.getRGB() << 8) | color.getAlpha();
  }
}
