package org.webswing.model.appframe.in;

import org.webswing.model.MsgIn;

import java.io.Serial;

public class AudioEventMsgIn implements MsgIn {

  @Serial
  private static final long serialVersionUID = -4269267759304268713L;

  private String id;
  private boolean stop;
  private boolean ping;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public boolean isStop() {
    return stop;
  }

  public void setStop(boolean stop) {
    this.stop = stop;
  }

  public boolean isPing() {
    return ping;
  }

  public void setPing(boolean ping) {
    this.ping = ping;
  }

}
