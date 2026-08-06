package org.webswing.directdraw.model;

import com.google.protobuf.ByteString;
import org.webswing.directdraw.DirectDraw;
import org.webswing.directdraw.proto.Directdraw.ImageProto;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageConst extends ImmutableDrawConstantHolder<byte[]> {

  protected long hash;

  public ImageConst(DirectDraw context, BufferedImage value) {
    super(context, context.getServices().getPngImage(value));
    this.hash = context.getServices().computeHash(value);
  }

  private ImageConst(DirectDraw context, long hash) {
    super(context, null);
    this.hash = hash;
  }

  @Override
  public String getFieldName() {
    return "image";
  }

  @Override
  public ImageProto toMessage() {
    ImageProto.Builder model = ImageProto.newBuilder();
    model.setData(ByteString.copyFrom(value));
    return model.build();
  }

  @Override
  public int hashCode() {
    return (int) hash;
  }

  @Override
  public boolean equals(Object o) {
    return o == this || o instanceof ImageConst ic && this.hash == ic.hash;
  }

  @Override
  public DrawConstant<byte[]> toCacheEntry() {
    return new ImageConst(getContext(), this.hash);
  }

  public static BufferedImage getValue(ImageProto proto) {
    try {
      return ImageIO.read(proto.getData().newInput());
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

}
