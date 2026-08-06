package org.webswing.model.app.in;

import org.webswing.model.MsgIn;

import java.io.Serial;

public class KeyValueMsgIn implements MsgIn {

  @Serial
  private static final long serialVersionUID = -6649653718258580250L;

  private String key;
  private String value;

  public KeyValueMsgIn(String key, String value) {
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

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

}
