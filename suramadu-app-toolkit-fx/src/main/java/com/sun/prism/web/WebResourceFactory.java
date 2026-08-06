package com.sun.prism.web;

import com.sun.glass.ui.Screen;
import com.sun.prism.*;
import com.sun.prism.impl.BaseResourceFactory;
import com.sun.prism.impl.TextureResourcePool;
import com.sun.prism.impl.shape.BasicShapeRep;
import com.sun.prism.shape.ShapeRep;

import java.util.Map;
import java.util.WeakHashMap;

public class WebResourceFactory extends BaseResourceFactory {
  private static final ShapeRep theRep = new BasicShapeRep();

  private static final Map<Image, Texture> clampTexCache = new WeakHashMap<>();
  private static final Map<Image, Texture> repeatTexCache = new WeakHashMap<>();
  private static final Map<Image, Texture> mipmapTexCache = new WeakHashMap<>();

  private final Screen screen;
  private final ResourceFactory original;

  WebResourceFactory(Screen screen, ResourceFactory original) {
    super(clampTexCache, repeatTexCache, mipmapTexCache);
    this.screen = screen;
    this.original = original;
  }

  @Override
  public Presentable createPresentable(PresentableState pState) {
    throw new UnsupportedOperationException("PresentingPainter is not supported in Webswing");
  }

  @Override
  public TextureResourcePool getTextureResourcePool() {
    return original.getTextureResourcePool();
  }

  @Override
  public Texture createTexture(PixelFormat formatHint, Texture.Usage usageHint,
      Texture.WrapMode wrapMode, int w, int h) {
    Texture originalTexture = original.createTexture(formatHint, usageHint, wrapMode, w, h);
    return new WebTextureWrapper(originalTexture);
  }

  @Override
  public Texture createTexture(PixelFormat formatHint, Texture.Usage usageHint,
      Texture.WrapMode wrapMode, int w, int h, boolean useMipmap) {
    Texture originalTexture =
        original.createTexture(formatHint, usageHint, wrapMode, w, h, useMipmap);
    return new WebTextureWrapper(originalTexture);
  }

  @Override
  public Texture createTexture(MediaFrame frame) {
    Texture originalTexture = original.createTexture(frame);
    return new WebTextureWrapper(originalTexture);
  }

  @Override
  public RTTexture createRTTexture(int width, int height, Texture.WrapMode wrapMode) {
    RTTexture originalTexture = original.createRTTexture(width, height, wrapMode);
    return new WebRTTextureWrapper(originalTexture);
  }

  @Override
  public RTTexture createRTTexture(int width, int height, Texture.WrapMode wrapMode, boolean msaa) {
    return createRTTexture(width, height, wrapMode);
  }

  @Override
  public boolean isFormatSupported(PixelFormat format) {
    switch (format) {
      case BYTE_RGB:
      case BYTE_GRAY:
      case INT_ARGB_PRE:
      case BYTE_BGRA_PRE:
        return true;
      case BYTE_ALPHA:
      case BYTE_APPLE_422:
      case MULTI_YCbCr_420:
      case FLOAT_XYZW:
      default:
        return false;
    }
  }

  @Override
  public int getMaximumTextureSize() {
    return Integer.MAX_VALUE;
  }

  @Override
  public int getRTTWidth(int w, Texture.WrapMode wrapMode) {
    return w;
  }

  @Override
  public int getRTTHeight(int h, Texture.WrapMode wrapMode) {
    return h;
  }

  @Override
  public boolean isCompatibleTexture(Texture tex) {
    return tex instanceof WebTextureWrapper;
  }

  @Override
  public ShapeRep createPathRep() {
    return theRep;
  }

  @Override
  public ShapeRep createRoundRectRep() {
    return theRep;
  }

  @Override
  public ShapeRep createEllipseRep() {
    return theRep;
  }

  @Override
  public ShapeRep createArcRep() {
    return theRep;
  }

  @Override
  public void dispose() {
    original.dispose();
  }

  @Override
  protected boolean canClampToZero() {
    return false;
  }

  @Override
  public PhongMaterial createPhongMaterial() {
    throw new UnsupportedOperationException("Not supported.");
  }

  @Override
  public MeshView createMeshView(Mesh mesh) {
    throw new UnsupportedOperationException("Not supported.");
  }

  @Override
  public Mesh createMesh() {
    throw new UnsupportedOperationException("Not supported.");
  }
}
