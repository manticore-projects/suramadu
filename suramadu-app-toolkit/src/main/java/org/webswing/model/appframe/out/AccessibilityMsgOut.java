package org.webswing.model.appframe.out;

import org.webswing.model.MsgOut;

import java.io.Serial;
import java.util.List;

public class AccessibilityMsgOut implements MsgOut {

  @Serial
  private static final long serialVersionUID = 8060354449415906755L;

  // ! make sure to regenerate hashcode and equals after change to this class

  private String id;
  private String role;
  private String text;
  private String tooltip;
  private String value;
  private String description;
  private String columnheader;
  private boolean password;
  private boolean toggle;
  private int selstart;
  private int selend;
  private int rowheight;
  private int rows;
  private int size;
  private int position;
  private int level;
  private int colindex;
  private int rowindex;
  private int rowcount;
  private int colcount;
  private List<String> states;
  private int min;
  private int max;
  private int val;
  private int screenX;
  private int screenY;
  private int width;
  private int height;
  private List<AccessibilityHierarchyMsgOut> hierarchy;

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + colcount;
    result = prime * result + colindex;
    result = prime * result + (columnheader == null ? 0 : columnheader.hashCode());
    result = prime * result + (description == null ? 0 : description.hashCode());
    result = prime * result + height;
    result = prime * result + (hierarchy == null ? 0 : hierarchy.hashCode());
    result = prime * result + (id == null ? 0 : id.hashCode());
    result = prime * result + level;
    result = prime * result + max;
    result = prime * result + min;
    result = prime * result + (password ? 1231 : 1237);
    result = prime * result + position;
    result = prime * result + (role == null ? 0 : role.hashCode());
    result = prime * result + rowcount;
    result = prime * result + rowheight;
    result = prime * result + rowindex;
    result = prime * result + rows;
    result = prime * result + screenX;
    result = prime * result + screenY;
    result = prime * result + selend;
    result = prime * result + selstart;
    result = prime * result + size;
    result = prime * result + (states == null ? 0 : states.hashCode());
    result = prime * result + (text == null ? 0 : text.hashCode());
    result = prime * result + (toggle ? 1231 : 1237);
    result = prime * result + (tooltip == null ? 0 : tooltip.hashCode());
    result = prime * result + val;
    result = prime * result + (value == null ? 0 : value.hashCode());
    result = prime * result + width;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    AccessibilityMsgOut other = (AccessibilityMsgOut) obj;
    if (colcount != other.colcount) {
      return false;
    }
    if (colindex != other.colindex) {
      return false;
    }
    if (columnheader == null) {
      if (other.columnheader != null) {
        return false;
      }
    } else if (!columnheader.equals(other.columnheader)) {
      return false;
    }
    if (description == null) {
      if (other.description != null) {
        return false;
      }
    } else if (!description.equals(other.description)) {
      return false;
    }
    if (height != other.height) {
      return false;
    }
    if (hierarchy == null) {
      if (other.hierarchy != null) {
        return false;
      }
    } else if (!hierarchy.equals(other.hierarchy)) {
      return false;
    }
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }
    if (level != other.level) {
      return false;
    }
    if (max != other.max) {
      return false;
    }
    if (min != other.min) {
      return false;
    }
    if (password != other.password) {
      return false;
    }
    if (position != other.position) {
      return false;
    }
    if (role == null) {
      if (other.role != null) {
        return false;
      }
    } else if (!role.equals(other.role)) {
      return false;
    }
    if (rowcount != other.rowcount) {
      return false;
    }
    if (rowheight != other.rowheight) {
      return false;
    }
    if (rowindex != other.rowindex) {
      return false;
    }
    if (rows != other.rows) {
      return false;
    }
    if (screenX != other.screenX) {
      return false;
    }
    if (screenY != other.screenY) {
      return false;
    }
    if (selend != other.selend) {
      return false;
    }
    if (selstart != other.selstart) {
      return false;
    }
    if (size != other.size) {
      return false;
    }
    if (states == null) {
      if (other.states != null) {
        return false;
      }
    } else if (!states.equals(other.states)) {
      return false;
    }
    if (text == null) {
      if (other.text != null) {
        return false;
      }
    } else if (!text.equals(other.text)) {
      return false;
    }
    if (toggle != other.toggle) {
      return false;
    }
    if (tooltip == null) {
      if (other.tooltip != null) {
        return false;
      }
    } else if (!tooltip.equals(other.tooltip)) {
      return false;
    }
    if (val != other.val) {
      return false;
    }
    if (value == null) {
      if (other.value != null) {
        return false;
      }
    } else if (!value.equals(other.value)) {
      return false;
    }
    if (width != other.width) {
      return false;
    }
    return true;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public List<String> getStates() {
    return states;
  }

  public void setStates(List<String> states) {
    this.states = states;
  }

  public int getMin() {
    return min;
  }

  public void setMin(int min) {
    this.min = min;
  }

  public int getMax() {
    return max;
  }

  public void setMax(int max) {
    this.max = max;
  }

  public int getVal() {
    return val;
  }

  public void setVal(int val) {
    this.val = val;
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

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTooltip() {
    return tooltip;
  }

  public void setTooltip(String tooltip) {
    this.tooltip = tooltip;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getColumnheader() {
    return columnheader;
  }

  public void setColumnheader(String columnheader) {
    this.columnheader = columnheader;
  }

  public boolean isPassword() {
    return password;
  }

  public void setPassword(boolean password) {
    this.password = password;
  }

  public boolean isToggle() {
    return toggle;
  }

  public void setToggle(boolean toggle) {
    this.toggle = toggle;
  }

  public int getSelstart() {
    return selstart;
  }

  public void setSelstart(int selstart) {
    this.selstart = selstart;
  }

  public int getSelend() {
    return selend;
  }

  public void setSelend(int selend) {
    this.selend = selend;
  }

  public int getRowheight() {
    return rowheight;
  }

  public void setRowheight(int rowheight) {
    this.rowheight = rowheight;
  }

  public int getRows() {
    return rows;
  }

  public void setRows(int rows) {
    this.rows = rows;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public int getPosition() {
    return position;
  }

  public void setPosition(int position) {
    this.position = position;
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public int getColindex() {
    return colindex;
  }

  public void setColindex(int colindex) {
    this.colindex = colindex;
  }

  public int getRowindex() {
    return rowindex;
  }

  public void setRowindex(int rowindex) {
    this.rowindex = rowindex;
  }

  public int getRowcount() {
    return rowcount;
  }

  public void setRowcount(int rowcount) {
    this.rowcount = rowcount;
  }

  public int getColcount() {
    return colcount;
  }

  public void setColcount(int colcount) {
    this.colcount = colcount;
  }

  public List<AccessibilityHierarchyMsgOut> getHierarchy() {
    return hierarchy;
  }

  public void setHierarchy(List<AccessibilityHierarchyMsgOut> hierarchy) {
    this.hierarchy = hierarchy;
  }

}
