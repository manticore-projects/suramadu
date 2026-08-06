package org.webswing.directdraw.model;

import org.webswing.directdraw.DirectDraw;
import org.webswing.directdraw.proto.Directdraw.EllipseProto;

import java.awt.geom.Ellipse2D;

public class EllipseConst extends MutableDrawConstantHolder<Ellipse2D, EllipseProto> {

  public EllipseConst(DirectDraw context, Ellipse2D value) {
    super(context, value);
  }

  @Override
  protected EllipseProto buildMessage(Ellipse2D value) {
    EllipseProto.Builder model = EllipseProto.newBuilder();
    model.setX((float) value.getX());
    model.setY((float) value.getY());
    model.setW((float) value.getWidth());
    model.setH((float) value.getHeight());
    return model.build();
  }

  @Override
  public String getFieldName() {
    return "ellipse";
  }

  @Override
  public Ellipse2D getValue() {
    return new Ellipse2D.Float(message.getX(), message.getY(), message.getW(), message.getH());
  }
}
