package org.webswing.toolkit.util;

import org.webswing.model.appframe.out.ComponentTreeMsgOut;

import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;

public class ToolkitUtil {

  /**
   * Return the whole component tree (Swing) and node tree (JavaFX), if available.
   */
  public static List<ComponentTreeMsgOut> getComponentTree() {
    List<ComponentTreeMsgOut> tree = new ArrayList<ComponentTreeMsgOut>();

    tree.addAll(getComponentTree(null));

    if (Services.getToolkitFXService() != null) {
      tree.addAll(getNodeTree());
    }

    return tree;
  }

  /**
   * Return the given Component's Swing component tree from ToolkitService.
   */
  public static List<ComponentTreeMsgOut> getComponentTree(Component root) {
    List<ComponentTreeMsgOut> componentTree = new ArrayList<ComponentTreeMsgOut>();

    if (root == null) {
      Window[] windows = Util.getAllWindows();
      for (Window w : windows) {
        componentTree.add(createComponentTreeMsg(w));
      }
    } else {
      componentTree.add(createComponentTreeMsg(root));
    }

    return componentTree;
  }

  /**
   * Return the whole JavaFX node tree from ToolkitFXService.
   */
  public static List<ComponentTreeMsgOut> getNodeTree() {
    return getNodeTree(null);
  }

  /**
   * Return the given Node's JavaFX node tree from ToolkitFXService.
   */
  public static List<ComponentTreeMsgOut> getNodeTree(Object node) {
    return Services.getToolkitFXService().requestNodeTree(node);
  }

  public static String getComponentType(Object o) {
    Class<?> cls = o.getClass();

    if (cls.isAnonymousClass()) {
      cls = cls.getInterfaces().length == 0 ? cls.getSuperclass() : cls.getInterfaces()[0];
    }

    return cls.getSimpleName();
  }

  private static ComponentTreeMsgOut createComponentTreeMsg(Component c) {
    String cType = getComponentType(c);
    ComponentTreeMsgOut msg = ComponentTreeMsgOut.fromComponent(c, cType);

    if ("JFXPanel".equalsIgnoreCase(cType)) {
      msg.setComponents(getNodeTree(c));
    } else if (c instanceof Container container) {
      synchronized (c.getTreeLock()) {
        Component[] children = container.getComponents();
        if (children != null && children.length > 0) {
          for (Component child : children) {
            msg.addChildComponent(createComponentTreeMsg(child));
          }
        }
      }
    }

    return msg;
  }

  private ToolkitUtil() {}

}
