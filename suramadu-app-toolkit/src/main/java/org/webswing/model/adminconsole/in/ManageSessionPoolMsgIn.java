package org.webswing.model.adminconsole.in;

import org.webswing.model.MsgIn;

import java.io.Serial;

public class ManageSessionPoolMsgIn implements MsgIn {

  @Serial
  private static final long serialVersionUID = 8611183615441516104L;

  public enum PowerRequestType {
    DRAIN_MODE, RESUME, STOP, FORCE_KILL
  }

  private String sessionPoolId;
  private PowerRequestType powerRequestType;

  public ManageSessionPoolMsgIn() {}

  public String getSessionPoolId() {
    return sessionPoolId;
  }

  public void setSessionPoolId(String sessionPoolId) {
    this.sessionPoolId = sessionPoolId;
  }

  public PowerRequestType getPowerRequestType() {
    return powerRequestType;
  }

  public void setPowerRequestType(PowerRequestType powerRequestType) {
    this.powerRequestType = powerRequestType;
  }
}
