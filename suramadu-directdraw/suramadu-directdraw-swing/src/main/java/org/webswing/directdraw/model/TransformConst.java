package org.webswing.directdraw.model;

import org.webswing.directdraw.DirectDraw;
import org.webswing.directdraw.proto.Directdraw.TransformProto;

import java.awt.geom.AffineTransform;

public class TransformConst extends MutableDrawConstantHolder<AffineTransform, TransformProto> {

  public TransformConst(DirectDraw context, AffineTransform value) {
    super(context, value);
  }

  @Override
  public String getFieldName() {
    return "transform";
  }

  @Override
  public TransformProto buildMessage(AffineTransform value) {
    TransformProto.Builder model = TransformProto.newBuilder();
    if (value.getScaleX() != 1) {
      model.setM00((float) value.getScaleX());
    }
    if (value.getShearY() != 0) {
      model.setM10((float) value.getShearY());
    }
    if (value.getShearX() != 0) {
      model.setM01((float) value.getShearX());
    }
    if (value.getScaleY() != 1) {
      model.setM11((float) value.getScaleY());
    }
    if (value.getTranslateX() != 0) {
      model.setM02((float) value.getTranslateX());
    }
    if (value.getTranslateY() != 0) {
      model.setM12((float) value.getTranslateY());
    }
    return model.build();
  }

  @Override
  public AffineTransform getValue() {
    return new AffineTransform(message.getM00(), message.getM10(), message.getM01(),
        message.getM11(), message.getM02(), message.getM12());
  }
}
