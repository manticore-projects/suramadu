package org.webswing.model.appframe.in;

import org.webswing.model.MsgIn;

import java.io.Serial;

public class UploadEventMsgIn implements MsgIn {

  @Serial
  private static final long serialVersionUID = -7188733550212761231L;

  private String fileId;

  public String getFileId() {
    return fileId;
  }

  public void setFileId(String fileId) {
    this.fileId = fileId;
  }

}
