package org.webswing.util;

import com.sun.java.accessibility.util.AccessibilityEventMonitor;
import com.sun.java.accessibility.util.Translator;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.webswing.model.appframe.out.AccessibilityHierarchyMsgOut;
import org.webswing.model.appframe.out.AccessibilityMsgOut;
import org.webswing.model.appframe.out.FileDialogEventMsgOut.FileDialogEventType;
import org.webswing.toolkit.util.Util;

import javax.accessibility.*;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.View;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.List;

@SuppressWarnings("restriction")
public class AccessibilityUtil {

  private static Map<AccessibleRole, String> accessibleRoleMap = new HashMap<>();
  private static Map<AccessibleState, String> accessibleStateMap = new HashMap<>();
  private static List<String> allowedHierarchyRoles = new ArrayList<>();
  private static List<String> allowedNoTextHierarchyRoles = new ArrayList<>(); // roles which are
                                                                               // allowed to be in
                                                                               // hierarchy even
                                                                               // though they have
                                                                               // no text to be read

  static {
    accessibleRoleMap.put(AccessibleRole.PUSH_BUTTON, "button");
    accessibleRoleMap.put(AccessibleRole.TOGGLE_BUTTON, "button");
    accessibleRoleMap.put(AccessibleRole.CHECK_BOX, "checkbox");
    accessibleRoleMap.put(AccessibleRole.COMBO_BOX, "combobox");
    accessibleRoleMap.put(AccessibleRole.RADIO_BUTTON, "radio");
    accessibleRoleMap.put(AccessibleRole.ICON, "img");
    accessibleRoleMap.put(AccessibleRole.TOOL_TIP, "tooltip");
    accessibleRoleMap.put(AccessibleRole.PROGRESS_BAR, "progressbar");
    accessibleRoleMap.put(AccessibleRole.TREE, "tree");
    accessibleRoleMap.put(AccessibleRole.SCROLL_BAR, "scrollbar");
    accessibleRoleMap.put(AccessibleRole.LIST, "list");
    accessibleRoleMap.put(AccessibleRole.SLIDER, "slider");
    accessibleRoleMap.put(AccessibleRole.TABLE, "table");
    accessibleRoleMap.put(AccessibleRole.MENU_BAR, "menubar");
    accessibleRoleMap.put(AccessibleRole.MENU_ITEM, "menuitem");
    accessibleRoleMap.put(AccessibleRole.PAGE_TAB_LIST, "tablist");
    accessibleRoleMap.put(AccessibleRole.PAGE_TAB, "tab");
    accessibleRoleMap.put(AccessibleRole.DIALOG, "dialog");
    // accessibleRoleMap.put(AccessibleRole.OPTION_PANE, "dialog"); option pane is included in a
    // dialog
    accessibleRoleMap.put(AccessibleRole.FRAME, "dialog");
    accessibleRoleMap.put(AccessibleRole.WINDOW, "dialog");
    accessibleRoleMap.put(AccessibleRole.ALERT, "alertdialog");
    accessibleRoleMap.put(AccessibleRole.MENU, "menu");
    accessibleRoleMap.put(AccessibleRole.POPUP_MENU, "menu");
    accessibleRoleMap.put(AccessibleRole.TEXT, "textbox");
    accessibleRoleMap.put(AccessibleRole.PASSWORD_TEXT, "textbox");
    accessibleRoleMap.put(AccessibleRole.SPIN_BOX, "spinbutton");
    accessibleRoleMap.put(AccessibleRole.PANEL, "group");
    accessibleRoleMap.put(AccessibleRole.LABEL, "label"); // this is not an actual ARIA role

    accessibleStateMap.put(AccessibleState.ACTIVE, "ACTIVE");
    accessibleStateMap.put(AccessibleState.ARMED, "ARMED");
    accessibleStateMap.put(AccessibleState.BUSY, "BUSY");
    accessibleStateMap.put(AccessibleState.CHECKED, "CHECKED");
    accessibleStateMap.put(AccessibleState.COLLAPSED, "COLLAPSED");
    accessibleStateMap.put(AccessibleState.EDITABLE, "EDITABLE");
    accessibleStateMap.put(AccessibleState.ENABLED, "ENABLED");
    accessibleStateMap.put(AccessibleState.EXPANDABLE, "EXPANDABLE");
    accessibleStateMap.put(AccessibleState.EXPANDED, "EXPANDED");
    accessibleStateMap.put(AccessibleState.FOCUSABLE, "FOCUSABLE");
    accessibleStateMap.put(AccessibleState.FOCUSED, "FOCUSED");
    accessibleStateMap.put(AccessibleState.HORIZONTAL, "HORIZONTAL");
    accessibleStateMap.put(AccessibleState.ICONIFIED, "ICONIFIED");
    accessibleStateMap.put(AccessibleState.INDETERMINATE, "INDETERMINATE");
    accessibleStateMap.put(AccessibleState.MANAGES_DESCENDANTS, "MANAGES_DESCENDANTS");
    accessibleStateMap.put(AccessibleState.MODAL, "MODAL");
    accessibleStateMap.put(AccessibleState.MULTI_LINE, "MULTI_LINE");
    accessibleStateMap.put(AccessibleState.MULTISELECTABLE, "MULTISELECTABLE");
    accessibleStateMap.put(AccessibleState.OPAQUE, "OPAQUE");
    accessibleStateMap.put(AccessibleState.PRESSED, "PRESSED");
    accessibleStateMap.put(AccessibleState.RESIZABLE, "RESIZABLE");
    accessibleStateMap.put(AccessibleState.SELECTABLE, "SELECTABLE");
    accessibleStateMap.put(AccessibleState.SELECTED, "SELECTED");
    accessibleStateMap.put(AccessibleState.SHOWING, "SHOWING");
    accessibleStateMap.put(AccessibleState.SINGLE_LINE, "SINGLE_LINE");
    accessibleStateMap.put(AccessibleState.TRANSIENT, "TRANSIENT");
    accessibleStateMap.put(AccessibleState.TRUNCATED, "TRUNCATED");
    accessibleStateMap.put(AccessibleState.VERTICAL, "VERTICAL");
    accessibleStateMap.put(AccessibleState.VISIBLE, "VISIBLE");

    allowedHierarchyRoles.addAll(accessibleRoleMap.values());
    allowedHierarchyRoles.remove("tablist");

    allowedNoTextHierarchyRoles.add("dialog");
    allowedNoTextHierarchyRoles.add("alertdialog");
  }

  public static void registerAccessibilityListeners() {
    FocusManager.getCurrentManager().addPropertyChangeListener("focusOwner",
        new PropertyChangeListener() {
          @Override
          public void propertyChange(PropertyChangeEvent evt) {
            Util.getWebToolkit().getPaintDispatcher().notifyAccessibilityInfoUpdate();
          }
        });
    AccessibilityEventMonitor.addPropertyChangeListener(evt -> {
      switch (evt.getPropertyName()) {
        case AccessibleContext.ACCESSIBLE_STATE_PROPERTY:
        case AccessibleContext.ACCESSIBLE_SELECTION_PROPERTY:
        case AccessibleContext.ACCESSIBLE_VALUE_PROPERTY:
        case AccessibleContext.ACCESSIBLE_ACTIVE_DESCENDANT_PROPERTY:
          // does not work on Linux
          Util.getWebToolkit().getPaintDispatcher().notifyAccessibilityInfoUpdate();
          break;
      }
    });
    Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
      @Override
      public void eventDispatched(AWTEvent event) {
        Util.getWebToolkit().getPaintDispatcher().notifyAccessibilityInfoUpdate();
      }
    }, AWTEvent.FOCUS_EVENT_MASK);
  }

  public static AccessibilityMsgOut getAccessibilityInfo() {
    Component c = FocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
    Accessible a = null;

    if (c != null) {
      if (c instanceof Accessible accessible) {
        a = accessible;
      } else {
        a = Translator.getAccessible(c);
      }
    }

    return getAccessibilityInfo(a);
  }

  public static AccessibilityMsgOut getAccessibilityInfo(Component c, int x, int y) {
    Point p = new Point(x, y);
    SwingUtilities.convertPointFromScreen(p, c);
    Component deepC = SwingUtilities.getDeepestComponentAt(c, p.x, p.y);
    Accessible a = null;

    if (deepC != null) {
      if (deepC instanceof Accessible accessible) {
        a = accessible;
      } else {
        a = Translator.getAccessible(deepC);
      }
    }

    return getAccessibilityInfo(a);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private static AccessibilityMsgOut getAccessibilityInfo(Accessible accessible) {
    Accessible a = accessible;
    AccessibilityMsgOut result = new AccessibilityMsgOut();

    if (a == null) {
      return null;
    }

    if (isJavaFXWindow(a)) {
      // no support for JavaFX accessibility at this time
      return null;
    }

    if (a instanceof Component component) {
      Window w = SwingUtilities.getWindowAncestor(component);
      if (w != null) {
        JFileChooser fc = Util.discoverFileChooser(w);
        if (fc != null) {
          FileDialogEventType fdet = Util.getFileChooserEventType(fc);
          if (fdet == FileDialogEventType.AutoSave || fdet == FileDialogEventType.AutoUpload) {
            // ignore file chooser dialog if transparent file open/save is enabled
            // in this case the file chooser is hidden from UI, so it should not be focused
            // instead an html dialog is shown in the UI
            return null;
          }
        }
      }
    }

    // handle focus keyStrokes

    if (a instanceof JTable || a instanceof JTextArea) {
      Component c = (Component) a;

      Set<AWTKeyStroke> forwardKeys =
          new HashSet<>(c.getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
      KeyStroke escapeStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
      if (!forwardKeys.contains(escapeStroke)) {
        forwardKeys.add(escapeStroke);
        c.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forwardKeys);
      }

      Set<AWTKeyStroke> backwardKeys =
          new HashSet<>(c.getFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS));
      KeyStroke shiftEscapeStroke =
          KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, KeyEvent.SHIFT_DOWN_MASK);
      if (!backwardKeys.contains(shiftEscapeStroke)) {
        backwardKeys.add(shiftEscapeStroke);
        c.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, backwardKeys);
      }
    }

    // overrides

    if (a instanceof JRootPane || a instanceof JPopupMenu) {
      // find actually selected menu item, because we only get focus on JPopupMenu or JRootPane
      MenuElement[] me = MenuSelectionManager.defaultManager().getSelectedPath();

      if (me != null && me.length > 0) {
        if (me[me.length - 1] instanceof Accessible accessible1) {
          a = accessible1;
        }
      }

      // we want either JMenu or JMenuItem, JPopupMenu has no text, this happens when a popup menu
      // of a JMenu is open but no JMenuItem is selected
      if (a instanceof JPopupMenu menu) {
        if (menu.getInvoker() instanceof JMenu) {
          a = (JMenu) menu.getInvoker();
        }
      }
    }

    JDialog optionPaneDialog = null;
    if (a instanceof JDialog dialog) {

      JOptionPane optionPane = findOptionPane(dialog.getContentPane());
      if (optionPane != null) {
        a = optionPane;
        optionPaneDialog = dialog;
      }
    }

    // ------

    AccessibleContext ctx = a.getAccessibleContext();

    if (ctx == null) {
      return null;
    }

    String description = getAccessibleDescription(ctx);
    String id = System.identityHashCode(a) + "";

    result.setText(ctx.getAccessibleName());

    String ariaRole = toAriaRole(ctx.getAccessibleRole());
    if (ariaRole == null) {
      // ignore roles that we do not map in javascript
      return null;
    }
    result.setRole(ariaRole);

    AccessibleStateSet stateSet = ctx.getAccessibleStateSet();
    if (stateSet != null) {
      ArrayList<String> states = new ArrayList<>();
      for (AccessibleState state : stateSet.toArray()) {
        String ariaState = toAriaState(state);
        if (ariaState != null) {
          states.add(ariaState);
        }
      }
      result.setStates(states);
    }

    if (a instanceof Component component) {
      handleComponent(result, component);
    }

    if (a instanceof JComponent component) {
      String tooltip = component.getToolTipText();
      if (tooltip != null && tooltip.trim().length() > 0) {
        result.setTooltip(tooltip);
      }
    }

    if (ctx.getAccessibleRole() == AccessibleRole.PANEL) {
      if (StringUtils.isBlank(result.getText())) {
        // ignore panel without text
        return null;
      }
    }

    if (a instanceof JOptionPane optionPane) {

      if (optionPane.getMessage() instanceof String) {
        description = (String) optionPane.getMessage();
      } else if (optionPane.getMessage() instanceof Accessible) {
        description =
            ((Accessible) optionPane.getMessage()).getAccessibleContext().getAccessibleName();
      }

      if (optionPaneDialog != null) {
        result.setText(optionPaneDialog.getTitle());
      } else {
        Window w = SwingUtilities.getWindowAncestor(optionPane);
        if (w != null && w instanceof Dialog dialog) {
          result.setText(dialog.getTitle());
        }
      }
    }

    if (a instanceof JLabel label) {
      View view = (View) label.getClientProperty("html");
      if (view != null) {
        Document doc = view.getDocument();
        try {
          result.setText(doc.getText(0, doc.getLength()));
        } catch (BadLocationException e) {
          // ignore
          e.printStackTrace();
        }
      }
    }

    if (a instanceof JTextComponent textComponent) {

      Document doc = textComponent.getDocument();
      try {
        result.setText(doc.getText(0, doc.getLength()));
      } catch (BadLocationException e) {
        // ignore
      }

      if (ctx.getAccessibleRole() == AccessibleRole.PASSWORD_TEXT) {
        result.setPassword(true);
      }

      AccessibleText at = ctx.getAccessibleText();
      if (at != null && at.getSelectionStart() != -1 && at.getSelectionEnd() != -1) {
        result.setSelstart(at.getSelectionStart());
        result.setSelend(at.getSelectionEnd());
      }
    }

    if (a instanceof JTextArea area) {
      int height = result.getHeight();
      int rowHeight = area.getFontMetrics(area.getFont()).getHeight();
      result.setRowheight(rowHeight);
      result.setRows((int) (height / rowHeight));
    }

    if (a instanceof JEditorPane editor) {

      if (ctx.getAccessibleDescription() != null
          && ctx.getAccessibleDescription().equals(editor.getContentType())) {
        // see AccessibleJEditorPane.getAccessibleDescription()
        // "Gets the accessibleDescription property of this object. If this property isn't set,
        // returns the content type of this <code>JEditorPane</code> instead (e.g. "plain/text",
        // "html/text")."
        description = null;
      }
    }

    if (ctx.getAccessibleRole() == AccessibleRole.TOGGLE_BUTTON) {
      result.setToggle(true);
    }

    if (ctx.getAccessibleRole() == AccessibleRole.RADIO_BUTTON) {
      if (ctx.getAccessibleRelationSet().contains(AccessibleRelation.MEMBER_OF)) {
        Object[] members =
            ctx.getAccessibleRelationSet().get(AccessibleRelation.MEMBER_OF).getTarget();

        if (members != null) {
          result.setSize(members.length);
          result.setPosition(ArrayUtils.indexOf(members, a) + 1);
        }
      }
    }

    if (ctx.getAccessibleRole() == AccessibleRole.COMBO_BOX && a instanceof JComboBox combo) {
      result.setSize(combo.getItemCount());
      if (combo.getSelectedIndex() > -1) {
        result.setPosition(combo.getSelectedIndex() + 1);
      }
      if (combo.getSelectedItem() != null) {
        String valueText = null;

        Accessible comboA = ctx.getAccessibleSelection().getAccessibleSelection(0); // 0 - first
                                                                                    // item in the
                                                                                    // array of
                                                                                    // selected
                                                                                    // items,
                                                                                    // multiselect
                                                                                    // is not
                                                                                    // supported by
                                                                                    // JComboBox
        if (comboA != null) {
          if (comboA.getAccessibleContext().getAccessibleParent() instanceof JList) {
            JList list = (JList) comboA.getAccessibleContext().getAccessibleParent();

            if (list.getModel() != null) {
              int index = list.getLeadSelectionIndex();
              if (index >= 0) {
                ListCellRenderer renderer = list.getCellRenderer();
                Object value = list.getModel().getElementAt(index);
                if (renderer != null && value != null) {
                  Component listC = list.getCellRenderer().getListCellRendererComponent(list, value,
                      index, list.isSelectedIndex(index), true);
                  if (listC != null) {
                    if (listC instanceof JLabel label) {
                      valueText = getTextFromLabel(label);
                    }
                    if (listC instanceof Accessible && listC.getAccessibleContext() != null) {
                      if (valueText == null) {
                        valueText = listC.getAccessibleContext().getAccessibleName();
                      }
                      description = getAccessibleDescription(listC.getAccessibleContext());
                    }
                  }
                }
              }
            }
          }

          if (valueText == null) {
            valueText = comboA.getAccessibleContext().getAccessibleName();
          }
        }

        if (valueText == null) {
          valueText = combo.getSelectedItem().toString();
        }

        result.setValue(valueText);
      }
    }

    if (ctx.getAccessibleRole() == AccessibleRole.PAGE_TAB_LIST
        && a instanceof JTabbedPane tabPane) {

      result.setSize(tabPane.getTabCount());
      result.setPosition(tabPane.getSelectedIndex() + 1);
    }

    if (ctx.getAccessibleRole() == AccessibleRole.PAGE_TAB
        && ctx.getAccessibleParent() instanceof JTabbedPane) {
      JTabbedPane tabPane = (JTabbedPane) ctx.getAccessibleParent();

      result.setSize(tabPane.getTabCount());
      result.setPosition(ctx.getAccessibleIndexInParent() + 1);
    }

    if (ctx.getAccessibleRole() == AccessibleRole.LIST && a instanceof JList list) {

      if (list.getModel() != null) {
        int index = list.getLeadSelectionIndex();
        if (index >= 0) {
          // if there is a focused list item
          result.setRole("listitem");
          result.setSize(list.getModel().getSize());
          result.setPosition(index + 1);

          ListCellRenderer renderer = list.getCellRenderer();
          Object value = list.getModel().getElementAt(index);

          String valueText = null;
          if (renderer != null && value != null) {
            Component listC = list.getCellRenderer().getListCellRendererComponent(list, value,
                index, list.isSelectedIndex(index), true);
            if (listC != null) {
              if (listC instanceof JLabel label) {
                valueText = getTextFromLabel(label);
              }
              if (listC instanceof Accessible && listC.getAccessibleContext() != null) {
                if (valueText == null) {
                  valueText = listC.getAccessibleContext().getAccessibleName();
                }
                description = getAccessibleDescription(listC.getAccessibleContext());
              }
            }
          }

          if (valueText == null) {
            valueText = list.getModel().getElementAt(index).toString();
          }

          if (valueText != null) {
            result.setText(valueText);
          }

          if (list.isSelectedIndex(index)) {
            if (!result.getStates().contains("SELECTED")) {
              result.getStates().add("SELECTED");
            }
          }
        }
      }
    }

    if (ctx.getAccessibleRole() == AccessibleRole.TREE && a instanceof JTree tree) {

      if (tree.getSelectionModel() != null && tree.getModel() != null) {
        TreePath path = tree.getSelectionModel().getLeadSelectionPath();
        if (path != null) {
          // if there is a focused tree item
          result.setRole("treeitem");

          if (path.getLastPathComponent() instanceof TreeNode) {
            TreeNode node = (TreeNode) path.getLastPathComponent();

            if (node.getParent() != null) {
              result.setLevel(path.getPath().length);
              result.setSize(node.getParent().getChildCount());
              result.setPosition(node.getParent().getIndex(node) + 1);
            } else {
              // root
              result.setLevel(1);
              result.setSize(1);
              result.setPosition(1);
            }

            List<String> states = new ArrayList<>();
            if (tree.isPathEditable(path)) {
              states.add("EDITABLE");
            }
            if (tree.isPathSelected(path)) {
              states.add("SELECTED");
            }
            if (tree.isExpanded(path)) {
              states.add("EXPANDED");
            }
            if (tree.getSelectionModel()
                .getSelectionMode() != TreeSelectionModel.SINGLE_TREE_SELECTION) {
              states.add("MULTISELECTABLE");
            }
            states.add("ENABLED");
            result.setStates(states);

            result.setText(node.toString());
          }
        }
      }
    }

    if (ctx.getAccessibleRole() == AccessibleRole.TABLE && a instanceof JTable table) {

      if (table.getSelectionModel() != null && table.getColumnModel() != null
          && table.getColumnModel().getSelectionModel() != null) {
        int rowIndex = table.getSelectionModel().getLeadSelectionIndex();
        int colIndex = table.getColumnModel().getSelectionModel().getLeadSelectionIndex();

        if (rowIndex >= 0 && colIndex >= 0) {
          result.setRole("gridcell");
          result.setColindex(colIndex + 1);
          result.setRowindex(rowIndex + 1);
          result.setRowcount(table.getRowCount());
          result.setColcount(table.getColumnCount());

          Object headerValue = table.getColumnModel().getColumn(colIndex).getHeaderValue();
          if (headerValue != null) {
            result.setColumnheader(headerValue.toString());
          }

          TableCellRenderer renderer = table.getCellRenderer(rowIndex, colIndex);
          Object value = table.getValueAt(rowIndex, colIndex);
          if (renderer != null && value != null) {
            Component cellC = renderer.getTableCellRendererComponent(table, value,
                table.isCellSelected(rowIndex, colIndex), true, rowIndex, colIndex);
            if (cellC != null) {
              String text = null;
              if (cellC instanceof JLabel label) {
                text = getTextFromLabel(label);
              }
              if (cellC instanceof Accessible && cellC.getAccessibleContext() != null) {
                if (text == null) {
                  text = cellC.getAccessibleContext().getAccessibleName();
                }
                description = getAccessibleDescription(cellC.getAccessibleContext());
              }
              if (text != null) {
                result.setText(text);
              }
            }
          }

          if (table.isCellSelected(rowIndex, colIndex)) {
            if (!result.getStates().contains("SELECTED")) {
              result.getStates().add("SELECTED");
            }
          } else {
            result.getStates().remove("SELECTED");
          }
          if (table.getSelectionModel().getSelectionMode() != ListSelectionModel.SINGLE_SELECTION) {
            result.getStates().add("MULTISELECTABLE");
          }
          if (table.isCellEditable(rowIndex, colIndex)) {
            result.getStates().add("EDITABLE");
          }
        }
      }
    }

    if (ctx.getAccessibleRole() == AccessibleRole.MENU_ITEM && a instanceof JMenuItem menuItem) {

      Component parent = null;
      if (menuItem.getParent() instanceof JPopupMenu) {
        Component invoker = ((JPopupMenu) menuItem.getParent()).getInvoker();
        if (invoker instanceof JMenu menu) {
          parent = menu;
          result.setSize(menu.getMenuComponentCount());
        } else {
          parent = menuItem.getParent();
          result.setSize(((JPopupMenu) menuItem.getParent()).getComponentCount());
        }
      }

      if (parent != null) {
        id = System.identityHashCode(parent) + "";
      }

      if (a instanceof JCheckBoxMenuItem) {
        result.setRole("menuitemcheckbox");
      }
      if (a instanceof JRadioButtonMenuItem) {
        result.setRole("menuitemradio");
      }

      result.setPosition(ctx.getAccessibleIndexInParent() + 1);
    }

    AccessibleValue value = ctx.getAccessibleValue();
    if (value != null) {
      if (value.getCurrentAccessibleValue() != null) {
        result.setVal(value.getCurrentAccessibleValue().intValue());
      }
      if (value.getMaximumAccessibleValue() != null) {
        result.setMax(value.getMaximumAccessibleValue().intValue());
      }
      if (value.getMinimumAccessibleValue() != null) {
        result.setMin(value.getMinimumAccessibleValue().intValue());
      }
    }

    if (StringUtils.isNotBlank(description)) {
      result.setDescription(description);
    }
    if (StringUtils.isNotBlank(id)) {
      result.setId(id);
    }

    List<AccessibilityHierarchyMsgOut> hierarchy = new ArrayList<>();
    resolveHierarchy(ctx.getAccessibleParent(), hierarchy);
    result.setHierarchy(hierarchy);

    return result;
  }

  private static boolean isJavaFXWindow(Accessible a) {
    Window w = null;

    if (a instanceof Window window && Util.isFXWindow(window)) {
      w = window;
    } else if (a instanceof Component component) {
      w = SwingUtilities.getWindowAncestor(component);
    }

    return w != null && Util.isFXWindow(w);
  }

  private static String getTextFromLabel(JLabel label) {
    View view = (View) label.getClientProperty("html");

    if (view != null) {
      Document doc = view.getDocument();
      try {
        return doc.getText(0, doc.getLength());
      } catch (BadLocationException e) {
        // ignore
      }
    }

    return null;
  }

  private static void resolveHierarchy(Accessible a, List<AccessibilityHierarchyMsgOut> hierarchy) {
    if (a == null || a.getAccessibleContext() == null) {
      return;
    }

    AccessibleContext ctx = a.getAccessibleContext();

    resolveHierarchy(ctx.getAccessibleParent(), hierarchy);

    String id = System.identityHashCode(a) + "";
    String role = toAriaRole(ctx.getAccessibleRole());
    String text = ctx.getAccessibleName();
    Integer position = null;
    Integer size = null;

    if (ctx.getAccessibleRole() == AccessibleRole.PAGE_TAB_LIST
        && a instanceof JTabbedPane tabPane) {

      position = tabPane.getSelectedIndex() + 1;
      size = tabPane.getTabCount();
    }

    if (ctx.getAccessibleRole() == AccessibleRole.PAGE_TAB
        && ctx.getAccessibleParent() instanceof JTabbedPane) {
      JTabbedPane tabPane = (JTabbedPane) ctx.getAccessibleParent();

      position = ctx.getAccessibleIndexInParent() + 1;
      size = tabPane.getTabCount();
    }

    if (role != null && allowedHierarchyRoles.contains(role)
        && (StringUtils.isNotBlank(text) || allowedNoTextHierarchyRoles.contains(role))) {
      AccessibilityHierarchyMsgOut msg = new AccessibilityHierarchyMsgOut(id, role, text);
      if (position != null && size != null) {
        msg.setPosition(position);
        msg.setSize(size);
      }
      hierarchy.add(msg);
    }
  }

  private static void handleComponent(AccessibilityMsgOut result, Component a) {
    Component c = (Component) a;
    try {
      result.setScreenX(c.getLocationOnScreen().x);
      result.setScreenY(c.getLocationOnScreen().y);
    } catch (IllegalComponentStateException e) {
      // component not showing on the screen
    }
    Dimension dim = getComponentDimensions(c);
    result.setHeight(dim.height);
    result.setWidth(dim.width);
  }

  private static JOptionPane findOptionPane(Container contentPane) {
    for (Component c : contentPane.getComponents()) {
      if (c instanceof JOptionPane pane) {
        return pane;
      }
    }
    return null;
  }

  private static Dimension getComponentDimensions(Component c) {
    int minHeight = c.getHeight();
    int minWidth = c.getWidth();

    Component parent = c.getParent();
    while (parent != null) {
      if (parent.getHeight() < minHeight) {
        minHeight = parent.getHeight();
      }
      if (parent.getWidth() < minWidth) {
        minWidth = parent.getWidth();
      }
      parent = parent.getParent();
    }
    return new Dimension(minWidth, minHeight);
  }

  private static String getAccessibleDescription(AccessibleContext ctx) {
    if (ctx.getAccessibleDescription() != null) {
      return ctx.getAccessibleDescription();
    }

    if (ctx.getAccessibleRelationSet().contains(AccessibleRelation.LABELED_BY)) {
      Object[] target =
          ctx.getAccessibleRelationSet().get(AccessibleRelation.LABELED_BY).getTarget();
      if (target != null && target.length > 0 && target[0] instanceof Accessible accessible) {
        String description = accessible.getAccessibleContext().getAccessibleName();
        if (StringUtils.isNotBlank(description)) {
          return description;
        }
      }
    }

    return null;
  }

  private static String toAriaRole(AccessibleRole accessibleRole) {
    if (accessibleRole == null) {
      return null;
    }

    return accessibleRoleMap.get(accessibleRole);
  }

  // private static String getDefaultAccessibleRole(AccessibleRole accessibleRole) {
  // String key = accessibleRole.toDisplayString();
  // try {
  // Field keyField = AccessibleBundle.class.getDeclaredField("key");
  // keyField.setAccessible(true);
  // key = (String) keyField.get(accessibleRole);
  // } catch (NoSuchFieldException | SecurityException | IllegalArgumentException |
  // IllegalAccessException e) {
  // // ignore
  // }
  // return key;
  // }

  private static String toAriaState(AccessibleState state) {
    if (state == null) {
      return null;
    }

    return accessibleStateMap.get(state);
  }

  private AccessibilityUtil() {}

}
