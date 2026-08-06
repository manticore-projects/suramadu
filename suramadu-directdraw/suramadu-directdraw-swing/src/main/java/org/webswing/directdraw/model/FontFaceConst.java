package org.webswing.directdraw.model;

import com.google.protobuf.ByteString;
import org.webswing.directdraw.DirectDraw;
import org.webswing.directdraw.proto.Directdraw.FontFaceProto;
import org.webswing.directdraw.util.DirectDrawUtils;

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;

public class FontFaceConst extends ImmutableDrawConstantHolder<Font> {

  public FontFaceConst(DirectDraw context, Font value) {
    super(context, get(value));
  }

  @Override
  public String getFieldName() {
    return "fontFace";
  }

  @Override
  public FontFaceProto toMessage() {
    FontFaceProto.Builder ffb = FontFaceProto.newBuilder();
    String fileName = getContext().getServices().getFileForFont(getValue());
    ffb.setName(DirectDrawUtils.fontNameFromFile(fileName, getValue()));
    try {
      ffb.setFont(ByteString.readFrom(new FileInputStream(new File(fileName))));
    } catch (Exception e) {
      e.printStackTrace();
    }
    FontFaceProto fontface = ffb.build();
    return fontface;
  }

}
