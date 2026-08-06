package org.webswing.model;

import org.webswing.model.app.in.ServerToAppFrameMsgIn;
import org.webswing.model.appframe.in.AppFrameMsgIn;

public class SyncObjectResponse {

  private ServerToAppFrameMsgIn msgIn;
  private AppFrameMsgIn frame;

  public SyncObjectResponse() {}

  public SyncObjectResponse(ServerToAppFrameMsgIn msgIn, AppFrameMsgIn frame) {
    super();
    this.msgIn = msgIn;
    this.frame = frame;
  }

  public ServerToAppFrameMsgIn getMsgIn() {
    return msgIn;
  }

  public AppFrameMsgIn getFrame() {
    return frame;
  }

}
