package org.webswing.directdraw.model;

import org.webswing.directdraw.DirectDraw;
import org.webswing.directdraw.proto.Directdraw.DrawConstantProto;
import org.webswing.directdraw.util.DrawConstantPool;

import java.util.List;

public abstract class CompositeDrawConstantHolder<T> extends DrawConstant<T> {
  T value;

  public CompositeDrawConstantHolder(DirectDraw ctx) {
    super(ctx);
  }

  @Override
  public T getValue() {
    return value;
  }

  public abstract void expandAndCacheConstants(List<DrawConstantProto> protos,
      DrawConstantPool cache);

  public abstract int getExpandedConstantCount();

  @Override
  public String getFieldName() {
    return null;
  }

  @Override
  public Object toMessage() {
    throw new UnsupportedOperationException();
  }

  @Override
  public int hashCode() {
    return getId();
  }

  @Override
  public boolean equals(Object o) {
    return o == this;
  }

}
