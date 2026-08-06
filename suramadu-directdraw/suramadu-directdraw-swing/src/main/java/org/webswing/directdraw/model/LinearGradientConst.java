package org.webswing.directdraw.model;

import org.webswing.directdraw.DirectDraw;
import org.webswing.directdraw.proto.Directdraw.CyclicMethodProto;
import org.webswing.directdraw.proto.Directdraw.LinearGradientProto;

import java.awt.Color;
import java.awt.LinearGradientPaint;
import java.util.Arrays;

public class LinearGradientConst extends ImmutableDrawConstantHolder<LinearGradientPaint> {

  public LinearGradientConst(DirectDraw context, LinearGradientPaint value) {
    super(context, value);
  }

  @Override
  public String getFieldName() {
    return "linearGrad";
  }

  @Override
  public LinearGradientProto toMessage() {
    LinearGradientProto.Builder model = LinearGradientProto.newBuilder();
    model.setXStart((int) value.getStartPoint().getX());
    model.setYStart((int) value.getStartPoint().getY());
    model.setXEnd((int) value.getEndPoint().getX());
    model.setYEnd((int) value.getEndPoint().getY());
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
    result = 31 * result + value.getStartPoint().hashCode();
    result = 31 * result + value.getEndPoint().hashCode();
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
    if (!(o instanceof LinearGradientConst)) {
      return false;
    }
    LinearGradientConst other = (LinearGradientConst) o;
    return value.getStartPoint().equals(other.value.getStartPoint())
        && value.getEndPoint().equals(other.value.getEndPoint())
        && Arrays.equals(value.getColors(), other.value.getColors())
        && Arrays.equals(value.getFractions(), other.value.getFractions())
        && value.getCycleMethod() == other.value.getCycleMethod();
  }
}
