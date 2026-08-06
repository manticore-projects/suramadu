package org.webswing.directdraw.model;

import org.webswing.directdraw.DirectDraw;
import org.webswing.directdraw.proto.Directdraw.CombinedProto;

import java.util.Arrays;

public class CombinedConst extends ImmutableDrawConstantHolder<int[]> {

  public CombinedConst(DirectDraw context, int... value) {
    super(context, value);
  }

  @Override
  public String getFieldName() {
    return "combined";
  }

  @Override
  public CombinedProto toMessage() {
    CombinedProto.Builder model = CombinedProto.newBuilder();
    if (value != null) {
      for (int i = 0; i < value.length; i++) {
        model.addIds(value[i]);
      }
    }
    return model.build();
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(value);
  }

  @Override
  public boolean equals(Object o) {
    return o == this || o instanceof CombinedConst cc && Arrays.equals(value, cc.value);
  }
}
