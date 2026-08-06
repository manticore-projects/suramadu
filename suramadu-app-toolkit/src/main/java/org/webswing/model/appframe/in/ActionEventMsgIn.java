package org.webswing.model.appframe.in;

import org.webswing.model.MsgIn;

import java.io.Serial;

public class ActionEventMsgIn implements MsgIn {

  @Serial
  private static final long serialVersionUID = -6048866464362966579L;

  public enum ActionEventType {
    init, user
  }

  public ActionEventMsgIn() {}

  private String windowId;
  private String actionName;
  private String data;
  private byte[] binaryData;
  private ActionEventType eventType;

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

  public String getWindowId() {
    return windowId;
  }

  public void setWindowId(String windowId) {
    this.windowId = windowId;
  }

  public ActionEventType getEventType() {
    return eventType;
  }

  public void setEventType(ActionEventType eventType) {
    this.eventType = eventType;
  }

}
