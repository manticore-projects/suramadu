package org.webswing.model.browser.out;

import org.webswing.model.MsgOut;

import java.io.Serial;

public class ServerToBrowserFrameMsgOut implements MsgOut {

  @Serial
  private static final long serialVersionUID = -8775701114901949469L;

  private byte[] appFrameMsgOut;

  private ConnectionInfoMsgOut connectionInfo;

  public byte[] getAppFrameMsgOut() {
    return appFrameMsgOut;
  }

  public void setAppFrameMsgOut(byte[] appFrameMsgOut) {
    this.appFrameMsgOut = appFrameMsgOut;
  }

  public ConnectionInfoMsgOut getConnectionInfo() {
    return connectionInfo;
  }

  public void setConnectionInfo(ConnectionInfoMsgOut connectionInfo) {
    this.connectionInfo = connectionInfo;
  }

}
