package org.webswing.model.browser.out;

import org.webswing.model.MsgOut;

import java.io.Serial;

public class ConnectionInfoMsgOut implements MsgOut {

  @Serial
  private static final long serialVersionUID = -23686931923075716L;

  private String serverId;
  private String sessionPoolId;
  private boolean autoLogout;

  public ConnectionInfoMsgOut() {
    super();
  }

  public ConnectionInfoMsgOut(String serverId, String sessionPoolId, boolean autoLogout) {
    super();
    this.serverId = serverId;
    this.sessionPoolId = sessionPoolId;
    this.autoLogout = autoLogout;
  }

  public String getServerId() {
    return serverId;
  }

  public void setServerId(String serverId) {
    this.serverId = serverId;
  }

  public String getSessionPoolId() {
    return sessionPoolId;
  }

  public void setSessionPoolId(String sessionPoolId) {
    this.sessionPoolId = sessionPoolId;
  }

  public boolean isAutoLogout() {
    return autoLogout;
  }

  public void setAutoLogout(boolean autoLogout) {
    this.autoLogout = autoLogout;
  }

}
