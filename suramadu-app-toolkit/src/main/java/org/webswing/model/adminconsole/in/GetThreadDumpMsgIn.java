package org.webswing.model.adminconsole.in;

import org.webswing.model.MsgIn;
import org.webswing.model.SyncMsg;

import java.io.Serial;
import java.util.UUID;

public class GetThreadDumpMsgIn implements SyncMsg, MsgIn {

  @Serial
  private static final long serialVersionUID = -4725610602464860114L;

  private String path;
  private String instanceId;
  private String timestamp;
  private String correlationId = UUID.randomUUID().toString();

  public GetThreadDumpMsgIn() {}

  public GetThreadDumpMsgIn(String path, String instanceId, String timestamp) {
    this.path = path;
    this.instanceId = instanceId;
    this.timestamp = timestamp;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getInstanceId() {
    return instanceId;
  }

  public void setInstanceId(String instanceId) {
    this.instanceId = instanceId;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public String getCorrelationId() {
    return correlationId;
  }

  public void setCorrelationId(String correlationId) {
    this.correlationId = correlationId;
  }

}
