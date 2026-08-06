package org.webswing.model.adminconsole.out;

import org.webswing.model.MsgOut;

import java.io.Serial;

public class MapMsgOut implements MsgOut {

  @Serial
  private static final long serialVersionUID = 4557209800189414238L;

  private String key;
  private String value;

  public MapMsgOut() {}

  public MapMsgOut(String key, String value) {
    this.key = key;
    this.value = value;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

}
