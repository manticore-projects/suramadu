package org.webswing.model.appframe.in;

import org.webswing.model.MsgIn;

import java.io.Serial;
import java.util.List;

public class FilesSelectedEventMsgIn implements MsgIn {

  @Serial
  private static final long serialVersionUID = 75198619L;
  private List<String> files;

  public List<String> getFiles() {
    return files;
  }

  public void setFiles(List<String> files) {
    this.files = files;
  }

}
