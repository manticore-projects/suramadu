package org.webswing.toolkit;

import java.awt.Dimension;
import java.awt.peer.SystemTrayPeer;

public class WebSystemTrayPeer implements SystemTrayPeer {

  @Override
  public Dimension getTrayIconSize() {
    return new Dimension(1, 1);
  }

}
