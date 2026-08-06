package org.webswing.directdraw.model;

import org.webswing.directdraw.DirectDraw;
import org.webswing.directdraw.proto.Directdraw.ArcProto;
import org.webswing.directdraw.proto.Directdraw.ArcProto.ArcTypeProto;

import java.awt.geom.Arc2D;

public class ArcConst extends MutableDrawConstantHolder<Arc2D, ArcProto> {

  public ArcConst(DirectDraw context, Arc2D value) {
    super(context, value);
  }

  @Override
  public String getFieldName() {
    return "arc";
  }

  @Override
  public ArcProto buildMessage(Arc2D value) {
    ArcProto.Builder model = ArcProto.newBuilder();
    model.setX((float) value.getX());
    model.setY((float) value.getY());
    model.setW((float) value.getWidth());
    model.setH((float) value.getHeight());
    model.setStart((float) value.getAngleStart());
    model.setExtent((float) value.getAngleExtent());
    model.setType(ArcTypeProto.valueOf(value.getArcType()));
    return model.build();
  }

  @Override
  public Arc2D getValue() {
    return new Arc2D.Float(message.getX(), message.getY(), message.getW(), message.getH(),
        message.getStart(), message.getExtent(), message.getType().getNumber());
  }
}
