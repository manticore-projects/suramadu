package org.webswing.model.appframe.out;

import org.webswing.model.MsgOut;

import java.io.Serial;

public class WindowSwitchMsgOut implements MsgOut {

  @Serial
  private static final long serialVersionUID = -523823816533325842L;

  private String id;
  private String title;
  private boolean modalBlocked;

  public WindowSwitchMsgOut() {}

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public boolean isModalBlocked() {
    return modalBlocked;
  }

  public void setModalBlocked(boolean modalBlocked) {
    this.modalBlocked = modalBlocked;
  }

}
