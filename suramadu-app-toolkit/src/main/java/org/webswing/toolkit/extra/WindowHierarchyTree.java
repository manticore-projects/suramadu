package org.webswing.toolkit.extra;

import org.webswing.toolkit.WebToolkit;
import org.webswing.toolkit.WebWindowPeer;
import org.webswing.toolkit.util.Util;
import org.webswing.util.AppLogger;

import javax.swing.SwingUtilities;
import java.awt.*;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Dialog.ModalityType;
import java.awt.Window.Type;
import java.util.*;
import java.util.List;

public class WindowHierarchyTree {

  private final Map<Window, WindowHierarchyNode> lookup =
      new HashMap<Window, WindowHierarchyNode>();
  private final LinkedList<WindowHierarchyNode> rootWindowNodes =
      new LinkedList<WindowHierarchyNode>();

  private final LinkedList<WindowHierarchyNode> alwaysOnTopZOrder =
      new LinkedList<WindowHierarchyNode>();
  private final LinkedList<WindowHierarchyNode> regularZOrder =
      new LinkedList<WindowHierarchyNode>();
  private final LinkedList<WindowHierarchyNode> zOrder = new LinkedList<WindowHierarchyNode>();

  private final ArrayDeque<Window> modalsStack = new ArrayDeque<>();

  protected void bringToFront(Window w) {
    if (w != null && !w.isEnabled()) {
      return;
    }
    if (lookup.containsKey(w)) {
      WindowHierarchyNode node = lookup.get(w);
      if (node.getParent() == null) {
        if (rootWindowNodes.indexOf(node) != 0) {
          rootWindowNodes.remove(node);
          rootWindowNodes.addFirst(node);
        }
        rebuildZOrder(true);
      } else {
        LinkedList<WindowHierarchyNode> parentChildren = node.getParent().getChildren();
        if (parentChildren.indexOf(node) != 0) {
          parentChildren.remove(node);
          parentChildren.addFirst(node);
        }
        if (w.getType() != Type.POPUP) {
          // bring parent NORMAL window to front
          bringToFront(node.getParent().getW());
        }
      }
    } else {
      AppLogger.error("Window not registered. Not able to bring to front.", w);
    }
  }

  protected void addWindow(Window window) {
    if (!lookup.containsKey(window)) {
      WindowHierarchyNode parentNode = lookup.get(window.getParent());
      if ((window instanceof Dialog dialog) && dialog.isModal()) {
        modalsStack.push(window);
      }
      if (window.getParent() == null || parentNode == null) {
        // window without parent
        WindowHierarchyNode thisNode = new WindowHierarchyNode(window);
        lookup.put(window, thisNode);
        rootWindowNodes.add(thisNode);
        rebuildZOrder(false);
      } else {
        // window with parent
        WindowHierarchyNode thisNode = new WindowHierarchyNode(window, parentNode);
        lookup.put(window, thisNode);
        parentNode.addChild(thisNode);
        rebuildZOrder(false);
      }
      window.repaint();
    } else {
      AppLogger.error("Window already registered in hierarchy tree", window);
    }
  }

  protected void removeWindow(Window window) {
    if (lookup.containsKey(window)) {
      WindowHierarchyNode node = lookup.get(window);
      int index = zOrder.indexOf(node);
      modalsStack.remove(window);
      Window successor = findSuccessor(window);
      lookup.remove(window);
      WindowHierarchyNode parent = node.getParent();
      LinkedList<WindowHierarchyNode> children = node.getChildren();
      if (parent != null) {
        parent.getChildren().remove(node);
        for (WindowHierarchyNode child : children) {
          child.setParent(parent);
        }
        parent.getChildren().addAll(children);
      } else {
        rootWindowNodes.remove(node);
        rootWindowNodes.addAll(children);
        for (WindowHierarchyNode child : children) {
          child.setParent(null);
        }
      }
      rebuildZOrder(false);
      requestRepaintUnderlying(index, window.getBounds());
      if (successor != null && Util.getWebToolkit().getWindowManager().isWindowActive(window)) {
        WindowManager.getInstance().activateWindow(successor);
      }
    } else {
      AppLogger.debug("Window not registered. Could not remove.", window);
    }
  }

  private Window findSuccessor(Window window) {
    WindowHierarchyNode node = lookup.get(window);
    if (modalsStack.size() > 0) {
      Window modalWindow = modalsStack.peek();
      if (modalWindow instanceof Dialog dialog
          && !dialog.getModalityType().equals(ModalityType.DOCUMENT_MODAL)) {
        return modalWindow;
      }
    }
    if (node.getParent() != null) {
      return node.getParent().getW();
    } else {
      int index = rootWindowNodes.indexOf(node);
      if (rootWindowNodes.size() > index + 1) {
        return rootWindowNodes.get(index + 1).getW();
      }
    }
    return null;
  }

  private void rebuildZOrder(boolean repaintChildren) {
    WindowHierarchyNode lastWindowAlwaysOnTop =
        alwaysOnTopZOrder.size() > 0 ? alwaysOnTopZOrder.get(0) : null;
    WindowHierarchyNode lastWindowRegular = regularZOrder.size() > 0 ? regularZOrder.get(0) : null;
    alwaysOnTopZOrder.clear();
    regularZOrder.clear();
    zOrder.clear();
    for (WindowHierarchyNode window : rootWindowNodes) {
      buildAlwaysOnTopZOrderList(alwaysOnTopZOrder, false, window);
    }
    for (WindowHierarchyNode window : rootWindowNodes) {
      buildRegularZOrderList(regularZOrder, window);
    }
    zOrder.addAll(alwaysOnTopZOrder);
    zOrder.addAll(regularZOrder);

    if (repaintChildren) {
      repaintChildren(lastWindowAlwaysOnTop, alwaysOnTopZOrder);
      repaintChildren(lastWindowRegular, regularZOrder);
    }

  }

  private void repaintChildren(WindowHierarchyNode lastWindow,
      LinkedList<WindowHierarchyNode> zOrder) {
    if (lastWindow != null) {
      for (WindowHierarchyNode node : zOrder) {
        if (node == lastWindow) {
          break;
        } else {
          Util.getWebToolkit().getPaintDispatcher().notifyWindowZOrderChanged(node.getW());
        }
      }
    }
  }

  private void buildAlwaysOnTopZOrderList(List<WindowHierarchyNode> resultList,
      boolean inheritedAlwaysOnTop, WindowHierarchyNode currentNode) {
    for (WindowHierarchyNode window : currentNode.getChildren()) {
      buildAlwaysOnTopZOrderList(resultList, inheritedAlwaysOnTop, window);
    }
    if (currentNode.getW().isAlwaysOnTop() || inheritedAlwaysOnTop) {
      resultList.add(currentNode);
      inheritedAlwaysOnTop = true;
    }
  }

  private void buildRegularZOrderList(List<WindowHierarchyNode> resultList,
      WindowHierarchyNode currentNode) {
    if (!currentNode.getW().isAlwaysOnTop()) {
      for (WindowHierarchyNode window : currentNode.getChildren()) {
        buildRegularZOrderList(resultList, window);
      }
      resultList.add(currentNode);
    }
  }

  protected boolean contains(Window w) {
    return lookup.containsKey(w);
  }

  private class WindowHierarchyNode {

    private Window w;
    private WindowHierarchyNode parent;
    private LinkedList<WindowHierarchyNode> children = new LinkedList<WindowHierarchyNode>();

    public WindowHierarchyNode(Window w) {
      this.w = w;
    }

    public WindowHierarchyNode(Window w, WindowHierarchyNode parent) {
      this.w = w;
      this.parent = parent;
    }

    public Window getW() {
      return w;
    }

    public void addChild(WindowHierarchyNode child) {
      children.add(child);
    }

    public WindowHierarchyNode getParent() {
      return parent;
    }

    public void setParent(WindowHierarchyNode parent) {
      this.parent = parent;
    }

    public LinkedList<WindowHierarchyNode> getChildren() {
      return children;
    }

    public String toString() {
      return w.toString();
    }
  }

  @SuppressWarnings("unchecked")
  protected Window getVisibleWindowOnPosition(int x, int y) {
    List<WindowHierarchyNode> clonedZOrder = (List<WindowHierarchyNode>) zOrder.clone(); // to avoid
                                                                                         // concurrent
                                                                                         // modification
                                                                                         // exception
    for (WindowHierarchyNode w : clonedZOrder) {
      if (w.getW().getBounds().contains(x, y)) {
        return w.getW();
      }
    }
    return null;
  }

  @SuppressWarnings("restriction")
  protected Map<String, List<Rectangle>> extractNonVisibleAreas() {
    Map<String, List<Rectangle>> result = new HashMap<String, List<Rectangle>>();
    if (zOrder.size() > 0) {
      for (int i = 1; i < zOrder.size() + 1; i++) {
        String id = i == zOrder.size() ? WebToolkit.BACKGROUND_WINDOW_ID
            : ((WebWindowPeer) WebToolkit.targetToPeer(zOrder.get(i).getW())).getGuid();
        Rectangle current = i == zOrder.size() ? new Rectangle(Util.getWebToolkit().getScreenSize())
            : zOrder.get(i).getW().getBounds();
        List<Rectangle> currentDifferences = new ArrayList<Rectangle>();
        for (int j = i - 1; j >= 0; j--) {
          Rectangle previousBounds = zOrder.get(j).getW().getBounds();
          Rectangle intersect = SwingUtilities.computeIntersection(current.x, current.y,
              current.width, current.height, (Rectangle) previousBounds.clone());
          if (!intersect.isEmpty()) {
            intersect.setLocation(intersect.x - current.x, intersect.y - current.y);
            currentDifferences.add(intersect);
          }
        }
        if (currentDifferences.size() != 0) {
          result.put(id, currentDifferences);
        }
      }
    }
    return result;
  }

  public List<String> getZOrder() {
    List<String> zorder = new ArrayList<>();
    for (int i = 0; i < zOrder.size(); i++) {
      String id = ((WebWindowPeer) WebToolkit.targetToPeer(zOrder.get(i).getW())).getGuid();
      zorder.add(id);
    }
    return zorder;
  }

  public void requestRepaintAfterMove(Window w, Rectangle originalPosition) {
    requestRepaintUnderlying(zOrder.indexOf(lookup.get(w)) + 1, originalPosition);
    Rectangle newPosition = w.getBounds();
    if (originalPosition.x != newPosition.x || originalPosition.y != newPosition.y) {
      Util.getWebToolkit().getPaintDispatcher().notifyWindowMoved(w, zOrder.indexOf(lookup.get(w)),
          originalPosition, newPosition);
    }
  }

  private void requestRepaintUnderlying(int index, Rectangle bounds) {
    for (int i = index; i < zOrder.size(); i++) {
      Window underlying = zOrder.get(i).getW();
      Rectangle uBounds = underlying.getBounds();
      Rectangle boundsCopy = new Rectangle(bounds);
      SwingUtilities.computeIntersection(uBounds.x, uBounds.y, uBounds.width, uBounds.height,
          boundsCopy);
      WebWindowPeer peer = (WebWindowPeer) WebToolkit.targetToPeer(underlying);
      Util.getWebToolkit().getPaintDispatcher().notifyWindowAreaVisible(peer.getGuid(),
          new Rectangle(boundsCopy.x - uBounds.x, boundsCopy.y - uBounds.y, boundsCopy.width,
              boundsCopy.height));
    }
    Rectangle boundsCopy = new Rectangle(bounds);
    Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
    SwingUtilities.computeIntersection(0, 0, screensize.width, screensize.height, boundsCopy);
    Util.getWebToolkit().getPaintDispatcher().notifyBackgroundAreaVisible(boundsCopy);
  }

  protected boolean isInSameModalBranch(Window active, Window current) {
    if (active == null) {
      return true;
    }
    Window modalParentofActive = getModalParent(active);
    Window modalParentofCurrent = getModalParent(current);
    boolean isActiveParentOfCurrent = isParent(modalParentofActive, modalParentofCurrent);
    if (modalParentofActive == null || modalParentofActive == modalParentofCurrent
        || (current == modalParentofCurrent && isActiveParentOfCurrent)) {
      return true;
    } else {
      return false;
    }
  }

  public boolean isParent(Window parent, Window child) {
    if (parent != null && child != null) {
      if (child.getParent() != null) {
        return child.getParent() == parent || isParent(parent, (Window) child.getParent());
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  private Window getModalParent(Window w) {
    WindowHierarchyNode window = lookup.get(w);
    if (window != null) {
      if ((window.getW() instanceof Dialog) && ((Dialog) window.getW()).isModal()) {
        return window.getW();
      } else if (window.getW().getParent() != null) {
        return getModalParent((Window) window.getW().getParent());
      } else {
        return null;
      }
    } else {
      return null;
    }
  }

  public boolean isInModalBranch(Window w) {
    return getModalParent(w) != null;
  }

  public boolean isInFullModalBranch(Window w) {
    WindowHierarchyNode window = lookup.get(w);
    if (window != null) {
      if ((window.getW() instanceof Dialog) && ((Dialog) window.getW()).isModal()
          && ((Dialog) window.getW()).getModalityType() != ModalityType.DOCUMENT_MODAL) {
        return true;
      } else if (window.getW().getParent() != null) {
        return isInFullModalBranch((Window) window.getW().getParent());
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

}
