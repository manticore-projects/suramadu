package org.webswing.server.api.services.websocket;

import java.util.Date;

public class WebSocketUserInfo {

  private final String userId;
  private final String userBrowser;
  private final String customArgs;
  private final int debugPort;
  private final String userIp;
  private final String userOs;
  private Date disconnectedSince;

  public WebSocketUserInfo(String userId, String userBrowser, String customArgs, int debugPort,
      String userIp, String userOs) {
    super();
    this.userId = userId;
    this.userBrowser = userBrowser;
    this.customArgs = customArgs;
    this.debugPort = debugPort;
    this.userIp = userIp;
    this.userOs = userOs;
  }

  public String getUserId() {
    return userId;
  }

  public String getCustomArgs() {
    return customArgs;
  }

  public int getDebugPort() {
    return debugPort;
  }

  public String getUserIp() {
    return userIp;
  }

  public String getUserOs() {
    return userOs;
  }

  public String getUserBrowser() {
    return userBrowser;
  }

  public Date getDisconnectedSince() {
    return disconnectedSince;
  }

  public void setDisconnected() {
    disconnectedSince = new Date();
  }

}
