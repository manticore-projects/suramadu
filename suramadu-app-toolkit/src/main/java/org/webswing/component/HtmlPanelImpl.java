package org.webswing.component;

import org.webswing.toolkit.api.action.WebActionEvent;
import org.webswing.toolkit.api.action.WebWindowActionListener;
import org.webswing.toolkit.api.component.HtmlPanel;

import javax.swing.JComponent;
import java.awt.Container;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class HtmlPanelImpl extends HtmlPanel {

  @Serial
  private static final long serialVersionUID = 3974466420979353544L;

  private List<WebWindowActionListener> webActionListeners = new ArrayList<>();

  /**
   * Container parent in case the HtmlPane is included in a Component that should render in own
   * canvas.
   */
  private Container container;
  /**
   * Component parent in case the HtmlPane is included in it and the component should render in own
   * canvas.
   */
  private JComponent component;

  public HtmlPanelImpl() {}

  public HtmlPanelImpl(Container container, JComponent component) {
    this.container = container;
    this.component = component;
  }

  @Override
  public final void handleWebActionEvent(WebActionEvent webActionEvent) {
    for (WebWindowActionListener listener : webActionListeners) {
      listener.actionPerformed(webActionEvent);
    }
  }

  @Override
  public void handleWindowInitialized() {
    for (WebWindowActionListener listener : webActionListeners) {
      listener.windowInitialized();
    }
  }

  public void addWebWindowActionListener(WebWindowActionListener listener) {
    webActionListeners.add(listener);
  }

  public void removeWebWindowActionListener(WebWindowActionListener listener) {
    webActionListeners.remove(listener);
  }

  public Container getWebContainer() {
    return container;
  }

  public JComponent getWebComponent() {
    return component;
  }

}
