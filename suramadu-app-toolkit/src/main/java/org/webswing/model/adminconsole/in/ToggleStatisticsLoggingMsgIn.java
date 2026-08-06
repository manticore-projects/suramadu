package org.webswing.model.adminconsole.in;

import org.webswing.model.MsgIn;

import java.io.Serial;

public class ToggleStatisticsLoggingMsgIn implements MsgIn {

  @Serial
  private static final long serialVersionUID = 1865769324317571740L;

  private String path;
  private String instanceId;
  private Boolean enabled;

  public ToggleStatisticsLoggingMsgIn() {}

  public ToggleStatisticsLoggingMsgIn(String path, String instanceId, Boolean enabled) {
    this.path = path;
    this.instanceId = instanceId;
    this.enabled = enabled;
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

  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

}
