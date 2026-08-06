package org.webswing.directdraw.model;

import org.webswing.directdraw.DirectDraw;
import org.webswing.directdraw.proto.Directdraw.RoundRectangleProto;

import java.awt.geom.RoundRectangle2D;

public class RoundRectangleConst
    extends MutableDrawConstantHolder<RoundRectangle2D, RoundRectangleProto> {

  public RoundRectangleConst(DirectDraw context, RoundRectangle2D value) {
    super(context, value);
  }

  @Override
  public String getFieldName() {
    return "roundRectangle";
  }

  @Override
  public RoundRectangleProto buildMessage(RoundRectangle2D value) {
    RoundRectangleProto.Builder model = RoundRectangleProto.newBuilder();
    model.setX((float) value.getX());
    model.setY((float) value.getY());
    model.setW((float) value.getWidth());
    model.setH((float) value.getHeight());
    model.setArcH((float) value.getArcHeight());
    model.setArcW((float) value.getArcWidth());
    return model.build();
  }

  @Override
  public RoundRectangle2D getValue() {
    return new RoundRectangle2D.Float(message.getX(), message.getY(), message.getW(),
        message.getH(), message.getArcW(), message.getArcH());
  }
}
