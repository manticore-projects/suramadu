package org.webswing.toolkit;

import java.awt.Component;
import java.awt.FileDialog;
import java.awt.event.FocusEvent;

public class WebFileDialogPeer11 extends WebFileDialogPeer {

  public WebFileDialogPeer11(FileDialog paramFileDialog) {
    super(paramFileDialog);
  }

  @Override
  public boolean requestFocus(Component lightweightChild, boolean temporary,
      boolean focusedWindowChangeAllowed, long time, FocusEvent.Cause cause) {
    return WebToolkit11.requestFocus(target, lightweightChild, temporary,
        focusedWindowChangeAllowed, time, cause);
  }
}
