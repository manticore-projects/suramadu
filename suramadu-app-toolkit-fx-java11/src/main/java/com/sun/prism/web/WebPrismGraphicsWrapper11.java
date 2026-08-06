package com.sun.prism.web;

import com.sun.prism.Graphics;

public class WebPrismGraphicsWrapper11 extends AbstractWebPrismGraphicsWrapper {

  public WebPrismGraphicsWrapper11(Graphics original, WebTextureWrapper texture) {
    super(original, texture);
  }

  @Override
  public void setPixelScaleFactors(float v, float v1) {
    original.setPixelScaleFactors(v, v1);
  }

  @Override
  public float getPixelScaleFactorX() {
    return original.getPixelScaleFactorX();
  }

  @Override
  public float getPixelScaleFactorY() {
    return original.getPixelScaleFactorY();
  }
}
