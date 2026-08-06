package org.webswing.directdraw.model;

public class IntegerConst extends DrawConstant<Integer> {

  public IntegerConst(int integer) {
    super(null);
    setId(integer);
  }

  @Override
  public Integer getValue() {
    return getId();
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
    return getId();
  }

  @Override
  public boolean equals(Object o) {
    return o == this || o instanceof IntegerConst ic && getId() == ic.getId();
  }
}
