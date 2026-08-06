package org.webswing.directdraw.model;

import org.webswing.directdraw.DirectDraw;

public class StringConst extends ImmutableDrawConstantHolder<String> {

  public StringConst(DirectDraw context, String value) {
    super(context, value);
  }

  @Override
  public String getFieldName() {
    return "string";
  }

  @Override
  public String toMessage() {
    return getValue();
  }
}
