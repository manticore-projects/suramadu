package org.webswing.toolkit.api.action;

public class WebActionEvent {

  private String actionName;
  private String data;
  private byte[] binaryData;

  public WebActionEvent(String actionName, String data, byte[] binaryData) {
    this.actionName = actionName;
    this.data = data;
    this.binaryData = binaryData;
  }

  public String getActionName() {
    return actionName;
  }

  public void setActionName(String actionName) {
    this.actionName = actionName;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public byte[] getBinaryData() {
    return binaryData;
  }

  public void setBinaryData(byte[] binaryData) {
    this.binaryData = binaryData;
  }

}
