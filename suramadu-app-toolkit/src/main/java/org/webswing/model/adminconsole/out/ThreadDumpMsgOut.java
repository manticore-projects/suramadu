package org.webswing.model.adminconsole.out;

import org.webswing.model.MsgOut;
import org.webswing.model.SyncMsg;

import java.io.Serial;

public class ThreadDumpMsgOut implements SyncMsg, MsgOut {

  @Serial
  private static final long serialVersionUID = -4389200203211810885L;

  private String instanceId;
  private long timestamp;
  private String content;
  private String reason;
  private String correlationId;

  public ThreadDumpMsgOut() {}

  public ThreadDumpMsgOut(String instanceId, long timestamp, String content, String reason) {
    this.instanceId = instanceId;
    this.timestamp = timestamp;
    this.content = content;
    this.reason = reason;
  }

  public String getInstanceId() {
    return instanceId;
  }

  public void setInstanceId(String instanceId) {
    this.instanceId = instanceId;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public String getCorrelationId() {
    return correlationId;
  }

  public void setCorrelationId(String correlationId) {
    this.correlationId = correlationId;
  }

}
