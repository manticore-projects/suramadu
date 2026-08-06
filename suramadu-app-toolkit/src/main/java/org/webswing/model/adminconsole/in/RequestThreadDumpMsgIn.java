package org.webswing.model.adminconsole.in;

import org.webswing.model.MsgIn;

import java.io.Serial;

public class RequestThreadDumpMsgIn implements MsgIn {

  @Serial
  private static final long serialVersionUID = 3181698522228498301L;

  private String path;
  private String instanceId;

  public RequestThreadDumpMsgIn() {}

  public RequestThreadDumpMsgIn(String path, String instanceId) {
    super();
    this.path = path;
    this.instanceId = instanceId;
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
