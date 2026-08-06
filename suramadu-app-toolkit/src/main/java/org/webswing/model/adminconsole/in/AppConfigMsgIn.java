package org.webswing.model.adminconsole.in;

import org.webswing.model.MsgOut;

import java.io.Serial;

public class AppConfigMsgIn implements MsgOut {

  @Serial
  private static final long serialVersionUID = -7226371902803856086L;

  private byte[] appConfig;
  private String sessionPoolId;

  public AppConfigMsgIn() {}

  public AppConfigMsgIn(byte[] appConfig, String sessionPoolId) {
    super();
    this.appConfig = appConfig;
    this.sessionPoolId = sessionPoolId;
  }

  public byte[] getAppConfig() {
    return appConfig;
  }

  public void setAppConfig(byte[] appConfig) {
    this.appConfig = appConfig;
  }

  public String getSessionPoolId() {
    return sessionPoolId;
  }

  public void setSessionPoolId(String sessionPoolId) {
    this.sessionPoolId = sessionPoolId;
  }

}
