package org.webswing.model.appframe.in;

import org.webswing.model.MsgIn;

import java.io.Serial;

public class CopyEventMsgIn implements MsgIn {

  @Serial
  private static final long serialVersionUID = 4159382604946656046L;

  public static enum CopyEventMsgType {
    copy, cut, getFileFromClipboard
  }

  private CopyEventMsgType type;
  private String file;

  public CopyEventMsgType getType() {
    return type;
  }

  public void setType(CopyEventMsgType type) {
    this.type = type;
  }

  public String getFile() {
    return file;
  }

  public void setFile(String file) {
    this.file = file;
  }

}
