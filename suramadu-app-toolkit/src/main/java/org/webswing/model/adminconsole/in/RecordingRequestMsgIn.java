package org.webswing.model.adminconsole.in;

import org.webswing.model.MsgIn;

import java.io.Serial;

public class RecordingRequestMsgIn implements MsgIn {
  @Serial
  private static final long serialVersionUID = -5866985862298757516L;

  public enum RecordingRequestType {
    startRecording, stopRecording
  }

  private RecordingRequestType type;
  private String path;
  private String instanceId;

  public RecordingRequestMsgIn() {}

  public RecordingRequestMsgIn(RecordingRequestType type, String path, String instanceId) {
    super();
    this.type = type;
    this.path = path;
    this.instanceId = instanceId;
  }

  public RecordingRequestType getType() {
    return type;
  }

  public void setType(RecordingRequestType type) {
    this.type = type;
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

}
