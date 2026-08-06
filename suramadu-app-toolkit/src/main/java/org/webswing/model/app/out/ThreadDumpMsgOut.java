package org.webswing.model.app.out;

import org.webswing.model.MsgOut;

import java.io.Serial;

public class ThreadDumpMsgOut implements MsgOut {

  @Serial
  private static final long serialVersionUID = -2047166209199097463L;

  private long timestamp;
  private String dumpId;
  private String reason;

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  public String getDumpId() {
    return dumpId;
  }

  public void setDumpId(String dumpId) {
    this.dumpId = dumpId;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }
}
