package org.webswing.directdraw.model;

import org.webswing.directdraw.DirectDraw;
import org.webswing.directdraw.proto.Directdraw.CyclicMethodProto;
import org.webswing.directdraw.proto.Directdraw.RadialGradientProto;
import org.webswing.directdraw.util.DirectDrawUtils;

import java.awt.Color;
import java.awt.RadialGradientPaint;
import java.util.Arrays;

public class RadialGradientConst extends ImmutableDrawConstantHolder<RadialGradientPaint> {

  public RadialGradientConst(DirectDraw context, RadialGradientPaint value) {
    super(context, value);
  }

  @Override
  public String getFieldName() {
    return "radialGrad";
  }

  @Override
  public RadialGradientProto toMessage() {
    RadialGradientProto.Builder model = RadialGradientProto.newBuilder();
    model.setXCenter((int) value.getCenterPoint().getX());
    model.setYCenter((int) value.getCenterPoint().getY());
    model.setXFocus((int) value.getFocusPoint().getX());
    model.setYFocus((int) value.getFocusPoint().getY());
    model.setRadius((int) value.getRadius());
    for (Color color : value.getColors()) {
      model.addColors(ColorConst.toRGBA(color));
    }
    for (float fraction : value.getFractions()) {
      model.addFractions(fraction);
    }
    model.setRepeat(CyclicMethodProto.valueOf(value.getCycleMethod().name()));
    return model.build();
  }

  @Override
  public int hashCode() {
    int result = 1;
    result = 31 * result + value.getCenterPoint().hashCode();
    result = 31 * result + value.getFocusPoint().hashCode();
    result = 31 * result + DirectDrawUtils.hashCode(value.getRadius());
    result = 31 * result + Arrays.hashCode(value.getColors());
    result = 31 * result + Arrays.hashCode(value.getFractions());
    result = 31 * result + value.getCycleMethod().hashCode();
    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof RadialGradientConst)) {
      return false;
    }
    RadialGradientConst other = (RadialGradientConst) o;
    return value.getCenterPoint().equals(other.value.getCenterPoint())
        && value.getFocusPoint().equals(other.value.getFocusPoint())
        && Float.floatToIntBits(value.getRadius()) == Float.floatToIntBits(other.value.getRadius())
        && Arrays.equals(value.getColors(), other.value.getColors())
        && Arrays.equals(value.getFractions(), other.value.getFractions())
        && value.getCycleMethod() == other.value.getCycleMethod();
  }
}
