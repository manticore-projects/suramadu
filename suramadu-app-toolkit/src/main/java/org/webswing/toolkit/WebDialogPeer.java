package org.webswing.toolkit;

import java.awt.Dialog;
import java.awt.Window;
import java.awt.peer.DialogPeer;
import java.util.List;


abstract public class WebDialogPeer extends WebWindowPeer implements DialogPeer {

  public WebDialogPeer(Dialog t) {
    super(t);
  }

  public void blockWindows(List<Window> windows) {}

}
