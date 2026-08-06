package org.webswing.model.adminconsole.out;

import org.webswing.model.MsgOut;

import java.io.Serial;

public class RegisterInstanceMsgOut implements MsgOut {

  @Serial
  private static final long serialVersionUID = -4389200203211810885L;

  private String path;
  private String instanceId;
  private boolean register;

  public RegisterInstanceMsgOut() {}

  public RegisterInstanceMsgOut(String path, String instanceId, boolean register) {
    super();
    this.path = path;
    this.instanceId = instanceId;
    this.register = register;
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

  public boolean isRegister() {
    return register;
  }

  public void setRegister(boolean register) {
    this.register = register;
  }

}
