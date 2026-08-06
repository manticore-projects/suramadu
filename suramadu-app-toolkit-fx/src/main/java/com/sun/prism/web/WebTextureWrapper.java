package com.sun.prism.web;

import com.sun.javafx.geom.RectBounds;
import com.sun.prism.Image;
import com.sun.prism.MediaFrame;
import com.sun.prism.PixelFormat;
import com.sun.prism.Texture;
import org.webswing.javafx.toolkit.util.DirtyAreaTracker;

import java.nio.Buffer;

public class WebTextureWrapper implements Texture {

  public static final DirtyAreaTracker dirtyAreaTracker = new DirtyAreaTracker();

  private final Texture original;

  public WebTextureWrapper(Texture original) {
    this.original = original;
  }

  @Override
  public PixelFormat getPixelFormat() {
    return original.getPixelFormat();
  }

  @Override
  public int getPhysicalWidth() {
    return original.getPhysicalWidth();
  }

  @Override
  public int getPhysicalHeight() {
    return original.getPhysicalHeight();
  }

  @Override
  public int getContentX() {
    return original.getContentX();
  }

  @Override
  public int getContentY() {
    return original.getContentY();
  }

  @Override
  public int getContentWidth() {
    return original.getContentWidth();
  }

  @Override
  public int getContentHeight() {
    return original.getContentHeight();
  }

  @Override
  public int getMaxContentWidth() {
    return original.getMaxContentWidth();
  }

  @Override
  public int getMaxContentHeight() {
    return original.getMaxContentHeight();
  }

  @Override
  public void setContentWidth(int contentWidth) {
    original.setContentWidth(contentWidth);
  }

  @Override
  public void setContentHeight(int contentHeight) {
    original.setContentHeight(contentHeight);
  }

  @Override
  public int getLastImageSerial() {
    return original.getLastImageSerial();
  }

  @Override
  public void setLastImageSerial(int serial) {
    original.setLastImageSerial(serial);
  }

  @Override
  public void update(Image img) {
    update(img, 0, 0);
  }

  @Override
  public void update(Image img, int dstx, int dsty) {
    update(img, dstx, dsty, img.getWidth(), img.getHeight());
  }

  @Override
  public void update(Image img, int dstx, int dsty, int srcw, int srch) {
    update(img, dstx, dsty, srcw, srch, false);
  }

  @Override
  public void update(Image img, int dstx, int dsty, int srcw, int srch, boolean skipFlush) {
    update(img.getPixelBuffer(), img.getPixelFormat(), dstx, dsty, img.getMinX(), img.getMinY(),
        srcw, srch, img.getScanlineStride(), skipFlush);
  }

  @Override
  public void update(Buffer buffer, PixelFormat format, int dstx, int dsty, int srcx, int srcy,
      int srcw, int srch, int srcscan, boolean skipFlush) {
    original.update(buffer, format, dstx, dsty, srcx, srcy, srcw, srch, srcscan, skipFlush);
  }

  @Override
  public void update(MediaFrame frame, boolean skipFlush) {
    original.update(frame, skipFlush);
  }

  @Override
  public WrapMode getWrapMode() {
    return original.getWrapMode();
  }

  @Override
  public boolean getUseMipmap() {
    return original.getUseMipmap();
  }

  @Override
  public Texture getSharedTexture(WrapMode altMode) {
    Texture sharedOriginal = original.getSharedTexture(altMode);
    if (sharedOriginal == null) {
      return null;
    }
    if (original == sharedOriginal) {
      return this;
    }
    return new WebTextureWrapper(sharedOriginal);
  }

  @Override
  public boolean getLinearFiltering() {
    return original.getLinearFiltering();
  }

  @Override
  public void setLinearFiltering(boolean linear) {
    original.setLinearFiltering(linear);
  }

  @Override
  public void lock() {
    original.lock();
  }

  @Override
  public void unlock() {
    original.unlock();
  }

  @Override
  public boolean isLocked() {
    return original.isLocked();
  }

  @Override
  public int getLockCount() {
    return original.getLockCount();
  }

  @Override
  public void assertLocked() {
    original.assertLocked();
  }

  @Override
  public void makePermanent() {
    original.makePermanent();
  }

  @Override
  public void contentsUseful() {
    original.contentsUseful();
  }

  @Override
  public void contentsNotUseful() {
    original.contentsNotUseful();
  }

  @Override
  public boolean isSurfaceLost() {
    return original.isSurfaceLost();
  }

  @Override
  public void dispose() {
    original.dispose();
  }

  public void dirty(RectBounds clip) {
    dirtyAreaTracker.addDirtyArea(this, clip);
  }

  public Texture getOriginal() {
    return original;
  }
}
