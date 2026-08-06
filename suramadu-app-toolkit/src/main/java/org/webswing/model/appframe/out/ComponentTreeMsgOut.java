package org.webswing.model.appframe.out;

import org.webswing.model.MsgOut;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.Component;
import java.awt.Frame;
import java.awt.IllegalComponentStateException;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class ComponentTreeMsgOut implements MsgOut {

  @Serial
  private static final long serialVersionUID = -8433885502194335248L;

  private String componentType;
  private String name;
  private String value;
  private int screenX;
  private int screenY;
  private int width;
  private int height;
  private boolean enabled;
  private boolean visible;
  private Boolean selected;

  private boolean hidden; // custom property to flag components that should be hidden (e.g. JavaFX
                          // Region, Group, ...)

  private List<ComponentTreeMsgOut> components;

  public static ComponentTreeMsgOut fromComponent(Component c, String componentType) {
    ComponentTreeMsgOut msg = new ComponentTreeMsgOut();

    msg.setComponentType(componentType);
    msg.setName(c.getName());
    if (c instanceof Frame frame) {
      msg.setValue(frame.getTitle());
    }
    if (c instanceof AbstractButton button) {
      msg.setValue(button.getText());
    }
    if (c instanceof JLabel label) {
      msg.setValue(label.getText());
    }
    if (c instanceof JTextComponent component) {
      msg.setValue(component.getText());
    }
    if (c instanceof JToggleButton button) {
      msg.setSelected(button.isSelected());
    }
    if (c instanceof JComboBox<?> combo) {
      if (combo.getSelectedItem() != null) {
        msg.setValue(combo.getSelectedItem().toString());
      }
    }
    if (c instanceof JSlider slider) {
      msg.setValue(slider.getValue() + "");
    }

    try {
      msg.setScreenX(c.getLocationOnScreen().x);
      msg.setScreenY(c.getLocationOnScreen().y);
    } catch (IllegalComponentStateException e) {
      // component not showing on the screen
    }
    msg.setWidth(c.getWidth());
    msg.setHeight(c.getHeight());
    msg.setEnabled(c.isEnabled());
    msg.setVisible(c.isVisible());

    return msg;
  }

  public void addChildComponent(ComponentTreeMsgOut child) {
    if (components == null) {
      components = new ArrayList<ComponentTreeMsgOut>();
    }

    components.add(child);
  }

  public String getComponentType() {
    return componentType;
  }

  public void setComponentType(String componentType) {
    this.componentType = componentType;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public int getScreenX() {
    return screenX;
  }

  public void setScreenX(int screenX) {
    this.screenX = screenX;
  }

  public int getScreenY() {
    return screenY;
  }

  public void setScreenY(int screenY) {
    this.screenY = screenY;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public boolean isVisible() {
    return visible;
  }

  public void setVisible(boolean visible) {
    this.visible = visible;
  }

  public List<ComponentTreeMsgOut> getComponents() {
    return components;
  }

  public void setComponents(List<ComponentTreeMsgOut> components) {
    this.components = components;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public boolean isHidden() {
    return hidden;
  }

  public void setHidden(boolean hidden) {
    this.hidden = hidden;
  }

  public Boolean getSelected() {
    return selected;
  }

  public void setSelected(Boolean selected) {
    this.selected = selected;
  }

}
