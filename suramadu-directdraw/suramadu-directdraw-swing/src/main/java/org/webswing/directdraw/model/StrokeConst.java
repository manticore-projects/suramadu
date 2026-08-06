package org.webswing.directdraw.model;

import org.webswing.directdraw.DirectDraw;
import org.webswing.directdraw.proto.Directdraw.StrokeProto;
import org.webswing.directdraw.proto.Directdraw.StrokeProto.StrokeCapProto;
import org.webswing.directdraw.proto.Directdraw.StrokeProto.StrokeJoinProto;

import java.awt.BasicStroke;

public class StrokeConst extends ImmutableDrawConstantHolder<BasicStroke> {

  public StrokeConst(DirectDraw context, BasicStroke value) {
    super(context, value);
  }

  @Override
  public String getFieldName() {
    return "stroke";
  }

  @Override
  public StrokeProto toMessage() {
    StrokeProto.Builder model = StrokeProto.newBuilder();
    model.setWidth(value.getLineWidth());
    model.setMiterLimit(value.getMiterLimit());
    model.setJoin(StrokeJoinProto.valueOf(value.getLineJoin()));
    model.setCap(StrokeCapProto.valueOf(value.getEndCap()));
    model.setDashOffset(value.getDashPhase());
    if (value.getDashArray() != null && value.getDashArray().length > 0) {
      for (float d : value.getDashArray()) {
        model.addDash(d);
      }
    }
    return model.build();
  }
}
