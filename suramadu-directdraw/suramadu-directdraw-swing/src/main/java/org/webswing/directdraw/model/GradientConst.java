package org.webswing.directdraw.model;

import org.webswing.directdraw.DirectDraw;
import org.webswing.directdraw.proto.Directdraw.CyclicMethodProto;
import org.webswing.directdraw.proto.Directdraw.LinearGradientProto;
import org.webswing.directdraw.util.DirectDrawUtils;

import java.awt.GradientPaint;
import java.awt.geom.Point2D;

public class GradientConst extends ImmutableDrawConstantHolder<GradientPaint> {

  public GradientConst(DirectDraw context, GradientPaint value) {
    super(context, value);
  }

  @Override
  public String getFieldName() {
    return "linearGrad";
  }

  @Override
  public LinearGradientProto toMessage() {
    LinearGradientProto.Builder model = LinearGradientProto.newBuilder();
    Point2D start = value.getPoint1();
    Point2D end = value.getPoint2();
    model.setXStart((int) start.getX());
    model.setYStart((int) start.getY());
    model.setXEnd((int) end.getX());
    model.setYEnd((int) end.getY());
    model.addColors(ColorConst.toRGBA(value.getColor1()));
    model.addColors(ColorConst.toRGBA(value.getColor2()));
    model.addFractions(0f);
    model.addFractions(1f);
    model.setRepeat(value.isCyclic() ? CyclicMethodProto.REPEAT : CyclicMethodProto.NO_CYCLE);
    return model.build();
  }

  @Override
  public int hashCode() {
    int result = 1;
    result = 31 * result + value.getPoint1().hashCode();
    result = 31 * result + value.getPoint2().hashCode();
    result = 31 * result + value.getColor1().hashCode();
    result = 31 * result + value.getColor2().hashCode();
    result = 31 * result + DirectDrawUtils.hashCode(value.isCyclic());
    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof GradientConst)) {
      return false;
    }
    GradientConst other = (GradientConst) o;
    return value.getPoint1().equals(other.value.getPoint1())
        && value.getPoint2().equals(other.value.getPoint2())
        && value.getColor1().equals(other.value.getColor1())
        && value.getColor2().equals(other.value.getColor2())
        && value.isCyclic() == other.value.isCyclic();
  }
}
