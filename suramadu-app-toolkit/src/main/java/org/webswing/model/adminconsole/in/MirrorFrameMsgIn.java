package org.webswing.model.adminconsole.in;

import org.webswing.model.MsgIn;

import java.io.Serial;

public class MirrorFrameMsgIn implements MsgIn {

  @Serial
  private static final long serialVersionUID = -9216019450138355731L;

  private byte[] frame;
  private String instanceId;
  private String sessionId;
  private String token;
  private boolean connect;
  private boolean disconnect;

  public byte[] getFrame() {
    return frame;
  }

  public void setFrame(byte[] frame) {
    this.frame = frame;
  }

  public String getInstanceId() {
    return instanceId;
  }

  public void setInstanceId(String instanceId) {
    this.instanceId = instanceId;
  }

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public boolean isConnect() {
    return connect;
  }

  public void setConnect(boolean connect) {
    this.connect = connect;
  }

  public boolean isDisconnect() {
    return disconnect;
  }

  public void setDisconnect(boolean disconnect) {
    this.disconnect = disconnect;
  }

}
