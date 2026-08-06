package org.webswing.javafx.toolkit.adaper;

import javax.swing.JDialog;
import javax.swing.JRootPane;
import java.awt.Window;

public class JDialogAdapter extends JDialog implements WindowAdapter {
  public JDialogAdapter(Window parent, boolean titled) {
    super(parent);
    if (!titled) {
      setUndecorated(true);
      getRootPane().setWindowDecorationStyle(JRootPane.NONE);
    }
  }

  @Override
  public Window getThis() {
    return this;
  }
}
