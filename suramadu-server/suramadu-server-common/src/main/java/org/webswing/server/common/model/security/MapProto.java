package org.webswing.server.common.model.security;

import java.io.Serial;
import java.io.Serializable;

public class MapProto implements Serializable {

  @Serial
  private static final long serialVersionUID = -27076384699449480L;

  private String key;
  private byte[] value; // serialized with ObjectMapper

  public MapProto() {}

  public MapProto(String key, byte[] value) {
    super();
    this.key = key;
    this.value = value;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public byte[] getValue() {
    return value;
  }

  public void setValue(byte[] value) {
    this.value = value;
  }

}
