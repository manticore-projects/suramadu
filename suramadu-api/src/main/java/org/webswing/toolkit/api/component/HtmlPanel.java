package org.webswing.toolkit.api.component;

import org.webswing.toolkit.api.action.WebWindow;
import org.webswing.toolkit.api.action.WebWindowActionListener;

import javax.swing.JPanel;
import java.io.Serial;

public abstract class HtmlPanel extends JPanel implements WebWindow {

  @Serial
  private static final long serialVersionUID = 1601744731001086393L;

  @Override
  public boolean isFocusTraversable() {
    return true; // when html panel' content is interacted with switch swing focus to this component
  }

  public abstract void addWebWindowActionListener(WebWindowActionListener listener);

  public abstract void removeWebWindowActionListener(WebWindowActionListener listener);

}
