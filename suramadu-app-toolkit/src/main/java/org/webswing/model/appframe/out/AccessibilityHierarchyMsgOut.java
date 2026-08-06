package org.webswing.model.appframe.out;

import org.webswing.model.MsgOut;

import java.io.Serial;

public class AccessibilityHierarchyMsgOut implements MsgOut {

  @Serial
  private static final long serialVersionUID = 511384426503762955L;

  // ! make sure to regenerate hashcode and equals after change to this class

  private String id;
  private String role;
  private String text;

  private Integer position;
  private Integer size;

  public AccessibilityHierarchyMsgOut() {}

  public AccessibilityHierarchyMsgOut(String id, String role, String text) {
    this.id = id;
    this.role = role;
    this.text = text;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (id == null ? 0 : id.hashCode());
    result = prime * result + (position == null ? 0 : position.hashCode());
    result = prime * result + (role == null ? 0 : role.hashCode());
    result = prime * result + (size == null ? 0 : size.hashCode());
    result = prime * result + (text == null ? 0 : text.hashCode());
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
    AccessibilityHierarchyMsgOut other = (AccessibilityHierarchyMsgOut) obj;
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }
    if (position == null) {
      if (other.position != null) {
        return false;
      }
    } else if (!position.equals(other.position)) {
      return false;
    }
    if (role == null) {
      if (other.role != null) {
        return false;
      }
    } else if (!role.equals(other.role)) {
      return false;
    }
    if (size == null) {
      if (other.size != null) {
        return false;
      }
    } else if (!size.equals(other.size)) {
      return false;
    }
    if (text == null) {
      if (other.text != null) {
        return false;
      }
    } else if (!text.equals(other.text)) {
      return false;
    }
    return true;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
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

  public Integer getPosition() {
    return position;
  }

  public void setPosition(Integer position) {
    this.position = position;
  }

  public Integer getSize() {
    return size;
  }

  public void setSize(Integer size) {
    this.size = size;
  }

  @Override
  public String toString() {
    return "AccessibilityHierarchyMsg [id=" + id + ", role=" + role + ", text=" + text + "]";
  }

}
