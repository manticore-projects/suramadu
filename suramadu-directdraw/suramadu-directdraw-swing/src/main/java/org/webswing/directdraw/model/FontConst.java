package org.webswing.directdraw.model;

import org.webswing.directdraw.DirectDraw;
import org.webswing.directdraw.proto.Directdraw.FontProto;
import org.webswing.directdraw.proto.Directdraw.FontProto.StyleProto;
import org.webswing.directdraw.util.DirectDrawUtils;

import java.awt.Font;

public class FontConst extends ImmutableDrawConstantHolder<Font> {

  public FontConst(DirectDraw context, Font value) {
    super(context, get(value));
  }

  @Override
  public String getFieldName() {
    return "font";
  }

  @Override
  public FontProto toMessage() {
    FontProto.Builder model = FontProto.newBuilder();
    String fileName = getContext().getServices().getFileForFont(getValue());
    model.setFamily(DirectDrawUtils.fontNameFromFile(fileName, getValue()));
    model.setFontProvided(Boolean.getBoolean(DirectDraw.FONTS_PROVIDED));
    model.setSize(value.getSize());
    model.setStyle(StyleProto.valueOf(value.getStyle()));
    if (value.isTransformed()) {
      model.setTransform(new TransformConst(getContext(), value.getTransform()).toMessage());
    }
    return model.build();
  }

}
