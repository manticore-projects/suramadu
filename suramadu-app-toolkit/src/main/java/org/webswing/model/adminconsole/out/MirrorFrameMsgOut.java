package org.webswing.model.adminconsole.out;

import org.webswing.model.MsgOut;

import java.io.Serial;

public class MirrorFrameMsgOut implements MsgOut {

  @Serial
  private static final long serialVersionUID = -9216019450138355731L;

  private byte[] frame;
  private String instanceId;
  private String sessionId;
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

  public boolean isDisconnect() {
    return disconnect;
  }

  public void setDisconnect(boolean disconnect) {
    this.disconnect = disconnect;
  }

}
