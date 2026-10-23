package org.webswing.directdraw.model;

import com.google.protobuf.UnsafeByteOperations;
import org.webswing.directdraw.DirectDraw;
import org.webswing.directdraw.proto.Directdraw.ImageProto;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Image draw constant with LAZY PNG encoding.
 *
 * <p>
 * Previously the constructor PNG-encoded the image eagerly — on the EDT, at
 * {@code Graphics.drawImage} time, BEFORE the image LRU cache was consulted. Repeated images
 * (icons, tiles) were therefore re-encoded on every paint and the bytes discarded on cache hit.
 *
 * <p>
 * Now the constructor only computes the identity hash (one unavoidable pixel pass, see
 * {@code DirectDrawServicesAdapter.computeHash}); the ZPNG encode happens in {@link #toMessage()},
 * which {@code DrawConstantPool.addToCache} reaches exclusively on cache MISS — and on the sender
 * thread, not the EDT.
 *
 * <p>
 * Trade-off: the encode now observes the image up to one frame interval (~33 ms) after the
 * {@code drawImage} call. An application mutating a {@code BufferedImage} it just painted inside
 * that window would serialize the later state. Correct Swing code does not do this (the painted
 * image must be stable until the paint completes anyway), and the identity hash is still taken at
 * draw time, so a mutated image would simply be a different cache entry on its next use.
 */
public class ImageConst extends DrawConstant<byte[]> {

  protected final long hash;
  /** Original image; {@code null} for hash-only cache-key entries. */
  private final BufferedImage image;
  /** Encoded lazily on first {@link #toMessage()} / {@link #getValue()}. */
  private byte[] png;

  public ImageConst(DirectDraw context, BufferedImage value) {
    super(context);
    this.image = value;
    this.hash = context.getServices().computeHash(value);
  }

  private ImageConst(DirectDraw context, long hash) {
    super(context);
    this.image = null;
    this.hash = hash;
  }

  /**
   * The original image, or {@code null} on a cache-key entry. Lets the server-side fallback
   * renderer ({@code RenderUtil.getImage}) draw directly instead of PNG-encoding and re-decoding.
   */
  public BufferedImage getImage() {
    return image;
  }

  @Override
  public byte[] getValue() {
    return pngBytes();
  }

  private byte[] pngBytes() {
    if (png == null && image != null) {
      png = getContext().getServices().getPngImage(image);
    }
    return png;
  }

  @Override
  public String getFieldName() {
    return "image";
  }

  @Override
  public ImageProto toMessage() {
    ImageProto.Builder model = ImageProto.newBuilder();
    // unsafeWrap: the PNG byte[] is produced here and never mutated afterwards,
    // so the defensive copy of ByteString.copyFrom is pure waste.
    model.setData(UnsafeByteOperations.unsafeWrap(pngBytes()));
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
