package org.webswing.toolkit.api.action;

import org.webswing.toolkit.api.WebswingUtil;

public interface WebWindow {

  public default void performWebAction(String actionName, String data, byte[] binaryData) {
    WebswingUtil.getWebswingApi().sendActionEvent(this, actionName, data, binaryData);
  }

  public void handleWebActionEvent(WebActionEvent webActionEvent);

  public void handleWindowInitialized();
}
