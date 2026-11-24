package org.webswing.directdraw.model;

import com.google.protobuf.ByteString;
import org.webswing.directdraw.DirectDraw;
import org.webswing.directdraw.proto.Directdraw.FontFaceProto;
import org.webswing.directdraw.util.DirectDrawUtils;
import org.webswing.directdraw.util.WebFontRegistry;

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
    if (WebFontRegistry.isKey(fileName)) {
      byte[] data = WebFontRegistry.data(fileName);
      ffb.setFont(data != null ? ByteString.copyFrom(data) : ByteString.EMPTY);
    } else {
      try (FileInputStream in = new FileInputStream(new File(fileName))) {
        ffb.setFont(ByteString.readFrom(in));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    FontFaceProto fontface = ffb.build();
    return fontface;
  }

}
