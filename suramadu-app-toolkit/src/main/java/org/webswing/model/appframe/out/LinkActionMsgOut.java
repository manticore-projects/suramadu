package org.webswing.model.appframe.out;

import org.webswing.model.MsgOut;

import java.io.Serial;

public class LinkActionMsgOut implements MsgOut {

  @Serial
  private static final long serialVersionUID = -1341111272614100725L;

  public enum LinkActionType {
    file, url, print, redirect;
  }

  private LinkActionType action;
  private String src;

  public LinkActionMsgOut() {}

  public LinkActionMsgOut(LinkActionType action, String url) {
    super();
    this.action = action;
    this.src = url;
  }

  public LinkActionType getAction() {
    return action;
  }

  public void setAction(LinkActionType action) {
    this.action = action;
  }

  public String getSrc() {
    return src;
  }

  public void setSrc(String src) {
    this.src = src;
  }

}
