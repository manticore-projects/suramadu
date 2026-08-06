package org.webswing.javafx.toolkit;

import com.sun.glass.ui.SystemClipboard;
import org.webswing.toolkit.util.Util;

import java.util.HashMap;

/**
 * Created by vikto on 07-Mar-17.
 */
public class WebFxDnD extends SystemClipboard {
  private HashMap<String, Object> data;
  private int actions;

  public WebFxDnD(String clipboardName) {
    super(clipboardName);
  }

  @Override
  protected boolean isOwner() {
    return false;
  }

  @Override
  protected void pushToSystem(HashMap<String, Object> cacheData, int supportedActions) {
    this.data = cacheData;
    this.actions = supportedActions;
    Util.getWebToolkit().getEventDispatcher().setJavaFXdragStarted(true);
  }

  @Override
  protected void pushTargetActionToSystem(int actionDone) {

  }

  @Override
  protected Object popFromSystem(String mimeType) {
    if (data != null) {
      return data.get(mimeType);
    }
    return null;
  }

  @Override
  protected int supportedSourceActionsFromSystem() {
    return actions;
  }

  @Override
  protected String[] mimesFromSystem() {
    return data == null ? new String[0] : data.keySet().toArray(new String[data.keySet().size()]);
  }
}
