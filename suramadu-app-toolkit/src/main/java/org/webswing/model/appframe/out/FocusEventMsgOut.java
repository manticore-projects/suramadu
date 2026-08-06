package org.webswing.model.appframe.out;

import org.webswing.model.MsgOut;

import java.io.Serial;

public class FocusEventMsgOut implements MsgOut {
  @Serial
  private static final long serialVersionUID = -8513536127927219011L;

  public enum FocusEventType {
    focusLost, focusGained, focusWithCarretGained, focusPasswordGained;
  }

  FocusEventType type;
  int x;
  int y;
  int w;
  int h;
  int caretX;
  int caretY;
  int caretH;
  boolean editable;

  public FocusEventType getType() {
    return type;
  }

  public void setType(FocusEventType type) {
    this.type = type;
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public int getW() {
    return w;
  }

  public void setW(int w) {
    this.w = w;
  }

  public int getH() {
    return h;
  }

  public void setH(int h) {
    this.h = h;
  }

  public int getCaretX() {
    return caretX;
  }

  public void setCaretX(int caretX) {
    this.caretX = caretX;
  }

  public int getCaretY() {
    return caretY;
  }

  public void setCaretY(int caretY) {
    this.caretY = caretY;
  }

  public int getCaretH() {
    return caretH;
  }

  public void setCaretH(int caretH) {
    this.caretH = caretH;
  }

  public boolean isEditable() {
    return editable;
  }

  public void setEditable(boolean editable) {
    this.editable = editable;
  }

}
