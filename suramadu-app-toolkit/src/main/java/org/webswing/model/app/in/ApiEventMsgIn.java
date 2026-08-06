package org.webswing.model.app.in;

import org.webswing.model.MsgIn;

import java.io.Serial;

public class ApiEventMsgIn implements MsgIn {
  @Serial
  private static final long serialVersionUID = -6912899913199681417L;

  public enum ApiEventType {
    UserConnected, UserDisconnected, MirrorViewConnected, MirrorViewDisconnected
  }

  private String userId;
  private ApiEventType event;
  private byte[] args;

  public ApiEventMsgIn() {}

  public ApiEventMsgIn(ApiEventType event, String userId, byte[] args) {
    super();
    this.userId = userId;
    this.event = event;
    this.args = args;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public ApiEventType getEvent() {
    return event;
  }

  public void setEvent(ApiEventType event) {
    this.event = event;
  }

  public byte[] getArgs() {
    return args;
  }

  public void setArgs(byte[] args) {
    this.args = args;
  }

}
