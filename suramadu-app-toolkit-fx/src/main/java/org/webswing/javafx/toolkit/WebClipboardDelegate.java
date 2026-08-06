package org.webswing.javafx.toolkit;

import com.sun.glass.ui.Clipboard;
import com.sun.glass.ui.delegate.ClipboardDelegate;

/**
 * Created by vikto on 28-Feb-17.
 */
public class WebClipboardDelegate implements ClipboardDelegate {
  @Override
  public Clipboard createClipboard(String clipboardName) {
    if (Clipboard.SYSTEM.equals(clipboardName)) {
      return new WebFxClipboard(clipboardName);
    }
    if (Clipboard.DND.equals(clipboardName)) {
      return new WebFxDnD(clipboardName);
    }
    return null;
  }
}
