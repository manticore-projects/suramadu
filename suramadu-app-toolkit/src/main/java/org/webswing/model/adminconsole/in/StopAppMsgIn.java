package org.webswing.model.adminconsole.in;

import org.webswing.model.MsgIn;

import java.io.Serial;

public class StopAppMsgIn implements MsgIn {

  @Serial
  private static final long serialVersionUID = 4359795607415450588L;

  private String path;

  public StopAppMsgIn() {}

  public StopAppMsgIn(String path) {
    this.path = path;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

}
