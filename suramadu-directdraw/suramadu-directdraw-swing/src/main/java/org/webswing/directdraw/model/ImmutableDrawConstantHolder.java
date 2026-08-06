package org.webswing.directdraw.model;

import org.webswing.directdraw.DirectDraw;

/**
 * <u>Holder for immutable values</u> that would be serialized on demand. Some constants are hard or
 * even impossible to deserialize properly (see {@link java.awt.Font}). And it makes no sense to
 * serialize other immutable values as they'll be needed later for rendering.
 */
public abstract class ImmutableDrawConstantHolder<T> extends DrawConstant<T> {

  protected final T value;

  public ImmutableDrawConstantHolder(DirectDraw context, T value) {
    super(context);
    this.value = value;
  }

  public final T getValue() {
    return value;
  }

  @Override
  public int hashCode() {
    return value.hashCode();
  }

  @Override
  public boolean equals(Object o) {
    return o == this
        || o instanceof ImmutableDrawConstantHolder<?> idch && value.equals(idch.getValue());
  }
}
