package org.webswing.directdraw.model;

import org.webswing.directdraw.DirectDraw;

public class FallbackConstant<T> extends ImmutableDrawConstantHolder<T> {
  public FallbackConstant(DirectDraw context, T value) {
    super(context, value);
  }

  @Override
  public String getFieldName() {
    return null;
  }

  @Override
  public Object toMessage() {
    return null;
  }
}
