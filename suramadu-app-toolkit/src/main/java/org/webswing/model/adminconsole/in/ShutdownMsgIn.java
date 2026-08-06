package org.webswing.model.adminconsole.in;

import org.webswing.model.MsgIn;

import java.io.Serial;

public class ShutdownMsgIn implements MsgIn {

  @Serial
  private static final long serialVersionUID = 1438814753331419308L;

  private String path;
  private String instanceId;
  private boolean force;

  public ShutdownMsgIn() {}

  public ShutdownMsgIn(String path, String instanceId, boolean force) {
    super();
    this.path = path;
    this.instanceId = instanceId;
    this.force = force;
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

  public boolean isForce() {
    return force;
  }

  public void setForce(boolean force) {
    this.force = force;
  }

}
