package org.webswing.toolkit;

import org.webswing.toolkit.util.Services;

import java.awt.Container;
import java.awt.Insets;
import java.awt.peer.ContainerPeer;

abstract public class WebContainerPeer extends WebComponentPeer implements ContainerPeer {

  public WebContainerPeer(Container t) {
    super(t);
  }

  public Insets getInsets() {
    if (isUndecorated()) {
      return new Insets(0, 0, 0, 0);
    } else {
      return Services.getImageService().getWindowDecorationTheme().getInsets();
    }
  }

  public void beginValidate() {}

  public void endValidate() {

  }

  public void beginLayout() {}

  public void endLayout() {}

  public boolean isPaintPending() {
    return false;
  }

  public void restack() {
    throw new UnsupportedOperationException();
  }

  public boolean isRestackSupported() {
    return false;
  }

  public Insets insets() {
    return getInsets();
  }

}
