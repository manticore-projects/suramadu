package com.sun.prism.web;

import com.sun.glass.ui.Screen;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.GeneralTransform3D;
import com.sun.javafx.scene.text.GlyphList;
import com.sun.javafx.sg.prism.NGCamera;
import com.sun.javafx.sg.prism.NGLightBase;
import com.sun.javafx.sg.prism.NodePath;
import com.sun.prism.*;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Paint;

public abstract class AbstractWebPrismGraphicsWrapper implements ReadbackGraphics {

  final Graphics original;
  private final WebTextureWrapper texture;

  public AbstractWebPrismGraphicsWrapper(Graphics original, WebTextureWrapper texture) {
    this.original = original;
    this.texture = texture;
  }

  private void addDirtyClipArea() {
    addDirtyRectangleArea(getFinalClipNoClone());
  }

  private void addDirtyRectangleArea(RectBounds r) {
    if (r != null) {
      RectBounds clip = new RectBounds(getFinalClipNoClone());
      if (clip != null) {
        clip.intersectWith(r);
      }
      if (clip.getWidth() > 0 && r.getHeight() > 0) {
        texture.dirty(clip);
      }
    }
  }

  @Override
  public BaseTransform getTransformNoClone() {
    return original.getTransformNoClone();
  }

  @Override
  public void setTransform(BaseTransform xform) {
    original.setTransform(xform);
  }

  @Override
  public void setTransform(double m00, double m10, double m01, double m11, double m02, double m12) {
    original.setTransform(m00, m10, m01, m11, m02, m12);
  }

  @Override
  public void setTransform3D(double mxx, double mxy, double mxz, double mxt, double myx, double myy,
      double myz, double myt, double mzx, double mzy, double mzz, double mzt) {
    original.setTransform3D(mxx, mxy, mxz, mxt, myx, myy, myz, myt, mzx, mzy, mzz, mzt);
  }

  @Override
  public void transform(BaseTransform xform) {
    original.transform(xform);
  }

  @Override
  public void translate(float tx, float ty) {
    original.translate(tx, ty);
  }

  @Override
  public void translate(float tx, float ty, float tz) {
    original.translate(tx, ty, tz);
  }

  @Override
  public void scale(float sx, float sy) {
    original.scale(sx, sy);
  }

  @Override
  public void scale(float sx, float sy, float sz) {
    original.scale(sx, sy, sz);
  }

  @Override
  public void setCamera(NGCamera camera) {
    original.setCamera(camera);
  }

  @Override
  public NGCamera getCameraNoClone() {
    return original.getCameraNoClone();
  }

  @Override
  public void setDepthTest(boolean depthTest) {
    original.setDepthTest(depthTest);
  }

  @Override
  public boolean isDepthTest() {
    return original.isDepthTest();
  }

  @Override
  public void setDepthBuffer(boolean depthBuffer) {
    original.setDepthBuffer(depthBuffer);
  }

  @Override
  public boolean isDepthBuffer() {
    return original.isDepthBuffer();
  }

  @Override
  public boolean isAlphaTestShader() {
    return original.isAlphaTestShader();
  }

  @Override
  public void setAntialiasedShape(boolean aa) {
    original.setAntialiasedShape(aa);
  }

  @Override
  public boolean isAntialiasedShape() {
    return original.isAntialiasedShape();
  }

  @Override
  public RectBounds getFinalClipNoClone() {
    return original.getFinalClipNoClone();
  }

  @Override
  public Rectangle getClipRect() {
    return original.getClipRect();
  }

  @Override
  public Rectangle getClipRectNoClone() {
    return original.getClipRectNoClone();
  }

  @Override
  public void setHasPreCullingBits(boolean hasBits) {
    original.setHasPreCullingBits(hasBits);
  }

  @Override
  public boolean hasPreCullingBits() {
    return original.hasPreCullingBits();
  }

  @Override
  public void setClipRect(Rectangle clipRect) {
    original.setClipRect(clipRect);
  }

  @Override
  public void setClipRectIndex(int index) {
    original.setClipRectIndex(index);
  }

  @Override
  public int getClipRectIndex() {
    return original.getClipRectIndex();
  }

  @Override
  public float getExtraAlpha() {
    return original.getExtraAlpha();
  }

  @Override
  public void setExtraAlpha(float extraAlpha) {
    original.setExtraAlpha(extraAlpha);
  }

  @Override
  public void setLights(NGLightBase[] lights) {
    original.setLights(lights);
  }

  @Override
  public NGLightBase[] getLights() {
    return original.getLights();
  }

  @Override
  public Paint getPaint() {
    return original.getPaint();
  }

  @Override
  public void setPaint(Paint paint) {
    original.setPaint(paint);
  }

  @Override
  public BasicStroke getStroke() {
    return original.getStroke();
  }

  @Override
  public void setStroke(BasicStroke stroke) {
    original.setStroke(stroke);
  }

  @Override
  public void setCompositeMode(CompositeMode mode) {
    original.setCompositeMode(mode);
  }

  @Override
  public CompositeMode getCompositeMode() {
    return original.getCompositeMode();
  }

  @Override
  public void clear() {
    addDirtyClipArea();
    original.clear();
  }

  @Override
  public void clear(Color color) {
    addDirtyClipArea();
    original.clear(color);
  }

  @Override
  public void clearQuad(float x1, float y1, float x2, float y2) {
    addDirtyClipArea();
    original.clearQuad(x1, y1, x2, y2);
  }

  @Override
  public void fill(Shape shape) {
    addDirtyClipArea();
    original.fill(shape);
  }

  @Override
  public void fillQuad(float x1, float y1, float x2, float y2) {
    addDirtyClipArea();
    original.fillQuad(x1, y1, x2, y2);
  }

  @Override
  public void fillRect(float x, float y, float width, float height) {
    addDirtyClipArea();
    original.fillRect(x, y, width, height);
  }

  @Override
  public void fillRoundRect(float x, float y, float width, float height, float arcw, float arch) {
    addDirtyClipArea();
    original.fillRoundRect(x, y, width, height, arcw, arch);
  }

  @Override
  public void fillEllipse(float x, float y, float width, float height) {
    addDirtyClipArea();
    original.fillEllipse(x, y, width, height);
  }

  @Override
  public void draw(Shape shape) {
    addDirtyClipArea();
    original.draw(shape);
  }

  @Override
  public void drawLine(float x1, float y1, float x2, float y2) {
    addDirtyClipArea();
    original.drawLine(x1, y1, x2, y2);
  }

  @Override
  public void drawRect(float x, float y, float width, float height) {
    addDirtyClipArea();
    original.drawRect(x, y, width, height);
  }

  @Override
  public void drawRoundRect(float x, float y, float width, float height, float arcw, float arch) {
    addDirtyClipArea();
    original.drawRoundRect(x, y, width, height, arcw, arch);
  }

  @Override
  public void drawEllipse(float x, float y, float width, float height) {
    addDirtyClipArea();
    original.drawEllipse(x, y, width, height);
  }

  @Override
  public void setNodeBounds(RectBounds bounds) {
    original.setNodeBounds(bounds);
  }

  @Override
  public void drawString(GlyphList gl, FontStrike strike, float x, float y, Color selectColor,
      int selectStart, int selectEnd) {
    addDirtyClipArea();
    original.drawString(gl, strike, x, y, selectColor, selectStart, selectEnd);
  }

  @Override
  public void blit(RTTexture srcTex, RTTexture dstTex, int srcX0, int srcY0, int srcX1, int srcY1,
      int dstX0, int dstY0, int dstX1, int dstY1) {
    original.blit(srcTex, dstTex, srcX0, srcY0, srcX1, srcY1, dstX0, dstY0, dstX1, dstY1);
  }

  @Override
  public void drawTexture(Texture tex, float x, float y, float w, float h) {
    addDirtyClipArea();
    if (tex instanceof WebTextureWrapper wrapper) {
      tex = wrapper.getOriginal();
    }
    original.drawTexture(tex, x, y, w, h);
  }

  @Override
  public void drawTexture(Texture tex, float dx1, float dy1, float dx2, float dy2, float sx1,
      float sy1, float sx2, float sy2) {
    addDirtyClipArea();
    if (tex instanceof WebTextureWrapper wrapper) {
      tex = wrapper.getOriginal();
    }
    original.drawTexture(tex, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2);
  }

  @Override
  public void drawTexture3SliceH(Texture tex, float dx1, float dy1, float dx2, float dy2, float sx1,
      float sy1, float sx2, float sy2, float dh1, float dh2, float sh1, float sh2) {
    addDirtyClipArea();
    if (tex instanceof WebTextureWrapper wrapper) {
      tex = wrapper.getOriginal();
    }
    original.drawTexture3SliceH(tex, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, dh1, dh2, sh1, sh2);
  }

  @Override
  public void drawTexture3SliceV(Texture tex, float dx1, float dy1, float dx2, float dy2, float sx1,
      float sy1, float sx2, float sy2, float dv1, float dv2, float sv1, float sv2) {
    addDirtyClipArea();
    if (tex instanceof WebTextureWrapper wrapper) {
      tex = wrapper.getOriginal();
    }
    original.drawTexture3SliceV(tex, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, dv1, dv2, sv1, sv2);
  }

  @Override
  public void drawTexture9Slice(Texture tex, float dx1, float dy1, float dx2, float dy2, float sx1,
      float sy1, float sx2, float sy2, float dh1, float dv1, float dh2, float dv2, float sh1,
      float sv1, float sh2, float sv2) {
    addDirtyClipArea();
    if (tex instanceof WebTextureWrapper wrapper) {
      tex = wrapper.getOriginal();
    }
    original.drawTexture9Slice(tex, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, dh1, dv1, dh2, dv2, sh1,
        sv1, sh2, sv2);
  }

  @Override
  public void drawTextureVO(Texture tex, float topopacity, float botopacity, float dx1, float dy1,
      float dx2, float dy2, float sx1, float sy1, float sx2, float sy2) {
    addDirtyClipArea();
    if (tex instanceof WebTextureWrapper wrapper) {
      tex = wrapper.getOriginal();
    }
    original.drawTextureVO(tex, topopacity, botopacity, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2);
  }

  @Override
  public void drawTextureRaw(Texture tex, float dx1, float dy1, float dx2, float dy2, float tx1,
      float ty1, float tx2, float ty2) {
    addDirtyClipArea();
    if (tex instanceof WebTextureWrapper wrapper) {
      tex = wrapper.getOriginal();
    }
    original.drawTextureRaw(tex, dx1, dy1, dx2, dy2, tx1, ty1, tx2, ty2);
  }

  @Override
  public void drawMappedTextureRaw(Texture tex, float dx1, float dy1, float dx2, float dy2,
      float tx11, float ty11, float tx21, float ty21, float tx12, float ty12, float tx22,
      float ty22) {
    addDirtyClipArea();
    if (tex instanceof WebTextureWrapper wrapper) {
      tex = wrapper.getOriginal();
    }
    original.drawMappedTextureRaw(tex, dx1, dy1, dx2, dy2, tx11, ty11, tx21, ty21, tx12, ty12, tx22,
        ty22);
  }

  @Override
  public void sync() {
    original.sync();
  }

  @Override
  public Screen getAssociatedScreen() {
    return original.getAssociatedScreen();
  }

  @Override
  public ResourceFactory getResourceFactory() {
    return original.getResourceFactory();
  }

  @Override
  public RenderTarget getRenderTarget() {
    return original.getRenderTarget();
  }

  @Override
  public void setRenderRoot(NodePath root) {
    original.setRenderRoot(root);
  }

  @Override
  public NodePath getRenderRoot() {
    return original.getRenderRoot();
  }

  @Override
  public void setState3D(boolean flag) {
    original.setState3D(flag);
  }

  @Override
  public boolean isState3D() {
    return original.isState3D();
  }

  @Override
  public void setup3DRendering() {
    original.setup3DRendering();
  }

  @Override
  public boolean canReadBack() {
    return original instanceof ReadbackGraphics rg && rg.canReadBack();
  }

  @Override
  public RTTexture readBack(Rectangle view) {
    return original instanceof ReadbackGraphics rg ? rg.readBack(view) : null;
  }

  @Override
  public void releaseReadBackBuffer(RTTexture view) {
    if (original instanceof ReadbackGraphics graphics) {
      graphics.releaseReadBackBuffer(view);
    }
  }

  @Override
  public void setPerspectiveTransform(GeneralTransform3D generalTransform3D) {
    original.setPerspectiveTransform(generalTransform3D);
  }

}
