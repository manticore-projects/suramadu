package org.webswing.model.appframe.in;

import org.webswing.model.MsgIn;

import java.io.Serial;

public class WindowFocusMsgIn implements MsgIn {

  @Serial
  private static final long serialVersionUID = 5306185208935013049L;

  private String windowId;
  private String htmlPanelId;

  public String getWindowId() {
    return windowId;
  }

  public void setWindowId(String windowId) {
    this.windowId = windowId;
  }

  public String getHtmlPanelId() {
    return htmlPanelId;
  }

  public void setHtmlPanelId(String htmlPanelId) {
    this.htmlPanelId = htmlPanelId;
  }

}
