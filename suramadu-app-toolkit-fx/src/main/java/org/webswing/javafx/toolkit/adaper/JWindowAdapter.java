package org.webswing.javafx.toolkit.adaper;

import javax.swing.JDialog;
import javax.swing.JRootPane;
import java.awt.Window;

public class JWindowAdapter extends JDialog implements WindowAdapter {
  public JWindowAdapter(Window parent) {
    super(parent);
    setUndecorated(true);
    getRootPane().setWindowDecorationStyle(JRootPane.NONE);
    setFocusableWindowState(false);
    setType(Window.Type.POPUP);
  }

  @Override
  public Window getThis() {
    return this;
  }

}
