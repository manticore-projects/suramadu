package org.webswing.model.common.in;

import org.webswing.model.CommonMsg;
import org.webswing.model.MsgIn;

import java.io.Serial;

public class SimpleEventMsgIn implements MsgIn, CommonMsg {

  @Serial
  private static final long serialVersionUID = 5832849328825358575L;

  public enum SimpleEventType implements CommonMsg {
    unload, killSwing, killSwingAdmin, paintAck, repaint, downloadFile, deleteFile, cancelFileSelection, requestComponentTree, requestWindowSwitchList, enableStatisticsLogging, disableStatisticsLogging, startRecording, stopRecording, startMirroring, stopMirroring
  }

  public SimpleEventMsgIn() {}

  public SimpleEventMsgIn(SimpleEventType type) {
    this.type = type;
  }

  private SimpleEventType type;

  public SimpleEventType getType() {
    return type;
  }

  public void setType(SimpleEventType type) {
    this.type = type;
  }

}
