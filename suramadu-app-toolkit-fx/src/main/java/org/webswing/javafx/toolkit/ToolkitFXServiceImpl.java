package org.webswing.javafx.toolkit;

import javafx.embed.swing.JFXPanel;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.webswing.ext.services.ToolkitFXService;
import org.webswing.javafx.toolkit.adaper.WindowAdapter;
import org.webswing.model.appframe.out.ComponentTreeMsgOut;
import org.webswing.toolkit.util.ToolkitUtil;
import org.webswing.util.AppLogger;

import javax.swing.JComponent;
import java.awt.Window;
import java.util.*;

public class ToolkitFXServiceImpl implements ToolkitFXService {

  private static ToolkitFXServiceImpl impl;

  private final Set<Stage> stages = Collections.synchronizedSet(new HashSet<>());
  private final List<String> ignoreChildren =
      Arrays.asList("Button", "CheckBox", "ChoiceBox", "ColorPicker", "ComboBox", "DatePicker",
          "Label", "PasswordField", "ProgressBar", "RadioButton", "Slider", "Spinner",
          "SplitMenuButton", "TextArea", "TextField", "ToggleButton");

  public static ToolkitFXServiceImpl getInstance() {
    if (impl == null) {
      impl = new ToolkitFXServiceImpl();
    }
    return impl;
  }

  private ToolkitFXServiceImpl() {}

  @Override
  public void registerStage(Object stage) {
    if (stage instanceof Stage stage1) {
      stages.add(stage1);
    }
  }

  @Override
  public List<ComponentTreeMsgOut> requestNodeTree(Object node) {
    List<ComponentTreeMsgOut> componentTree = new ArrayList<ComponentTreeMsgOut>();

    if (node != null && node instanceof Node node1) {
      componentTree.add(createComponentTreeMsg(node1));
    } else if (node != null && node instanceof JFXPanel panel) {
      componentTree.add(createComponentTreeMsg(panel.getScene().getRoot()));
    } else {
      stages
          .forEach(stage -> componentTree.add(createComponentTreeMsg(stage.getScene().getRoot())));
    }

    return componentTree;
  }

  @Override
  public boolean isFXWindow(Window window) {
    return window instanceof WindowAdapter;
  }

  private ComponentTreeMsgOut createComponentTreeMsg(Node n) {
    ComponentTreeMsgOut msg = createComponentTreeMsgFromNode(n);

    if (ignoreChildren.contains(ToolkitUtil.getComponentType(n))) {
      return msg;
    }

    if (n instanceof Parent parent) {
      parent.getChildrenUnmodifiable()
          .forEach(child -> msg.addChildComponent(createComponentTreeMsg(child)));
    }

    if (n instanceof SwingNode node) {
      JComponent swingComponent = node.getContent();
      if (swingComponent != null) {
        try {
          msg.setComponents(ToolkitUtil.getComponentTree(swingComponent));
        } catch (Exception e) {
          AppLogger.warn("Error while creating Swing component tree for SwingNode!", e);
        }
      }
    }

    return msg;
  }

  private ComponentTreeMsgOut createComponentTreeMsgFromNode(Node n) {
    ComponentTreeMsgOut msg = new ComponentTreeMsgOut();

    msg.setComponentType(ToolkitUtil.getComponentType(n));
    msg.setName(n.getId());
    msg.setEnabled(!n.isDisabled());
    msg.setVisible(n.isVisible());

    boolean transparent = n.getOpacity() == 0d;
    msg.setHidden(transparent);

    if (n instanceof TextInputControl control) {
      msg.setValue(control.getText());
    }

    if (n instanceof Labeled labeled) {
      msg.setValue(labeled.getText());
    }

    if (n instanceof Slider slider) {
      msg.setValue(slider.getValue() + "");
    }

    if (n instanceof Spinner<?> spinner) {
      if (spinner.getValue() != null) {
        msg.setValue(spinner.getValue().toString());
      }
    }

    if (n instanceof ComboBoxBase<?> combo) {
      if (combo.getValue() != null) {
        msg.setValue(combo.getValue().toString());
      }
    }

    if (n instanceof ChoiceBox<?> choicebox) {
      if (choicebox.getValue() != null) {
        msg.setValue(choicebox.getValue().toString());
      }
    }

    if (n instanceof ToggleButton button) {
      msg.setSelected(button.isSelected());
    }

    if (n instanceof CheckBox box) {
      msg.setSelected(box.isSelected());
    }

    Bounds bounds = n.localToScreen(n.getBoundsInLocal());

    msg.setScreenX((int) bounds.getMinX());
    msg.setScreenY((int) bounds.getMinY());
    msg.setWidth((int) bounds.getWidth());
    msg.setHeight((int) bounds.getHeight());

    return msg;
  }

}
