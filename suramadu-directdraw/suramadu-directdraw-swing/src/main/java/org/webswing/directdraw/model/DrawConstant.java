package org.webswing.directdraw.model;

import org.webswing.directdraw.DirectDraw;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

public abstract class DrawConstant<T> {

  private static final WeakHashMap<Object, WeakReference<Object>> FLYWEIGHT_REGISTER =
      new WeakHashMap<>();

  @SuppressWarnings("unchecked")
  protected static <T> T get(T object) {
    if (!FLYWEIGHT_REGISTER.containsKey(object)) {
      FLYWEIGHT_REGISTER.put(object, new WeakReference<Object>(object));
      return object;
    }
    return (T) FLYWEIGHT_REGISTER.get(object).get();
  }

  public static final NullConst NULL_CONST = new NullConst();

  private DirectDraw context;

  private int id = -1;

  public DrawConstant(DirectDraw context) {
    this.context = context;
  }

  public abstract T getValue();

  public abstract String getFieldName();

  public abstract Object toMessage();

  @Override
  public abstract int hashCode();

  @Override
  public abstract boolean equals(Object o);

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public DirectDraw getContext() {
    return context;
  }

  public void setContext(DirectDraw context) {
    this.context = context;
  }

  public DrawConstant<T> toCacheEntry() {
    return this;
  }

  private static final class NullConst extends DrawConstant<Object> {

    private NullConst() {
      super(null);
    }

    @Override
    public int getId() {
      // always have zero id
      return 0;
    }

    @Override
    public Object getValue() {
      return null;
    }

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
      return 0;
    }

    @Override
    public boolean equals(Object o) {
      return o == this;
    }
  }

}
