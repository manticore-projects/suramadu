package org.webswing.model.appframe.in;

import org.webswing.model.MsgIn;

import java.io.Serial;

public class KeyboardEventMsgIn implements MsgIn {

  @Serial
  private static final long serialVersionUID = -896095456169586882L;

  public enum KeyEventType {
    keypress, keydown, keyup;
  }

  private KeyEventType type;
  private int character;
  private int keycode;
  private boolean alt;
  private boolean ctrl;
  private boolean shift;
  private boolean meta;

  public KeyEventType getType() {
    return type;
  }

  public void setType(KeyEventType type) {
    this.type = type;
  }

  public int getCharacter() {
    return character;
  }

  public void setCharacter(int character) {
    this.character = character;
  }

  public int getKeycode() {
    return keycode;
  }

  public void setKeycode(int keycode) {
    this.keycode = keycode;
  }

  public boolean isAlt() {
    return alt;
  }

  public void setAlt(boolean alt) {
    this.alt = alt;
  }

  public boolean isCtrl() {
    return ctrl;
  }

  public void setCtrl(boolean ctrl) {
    this.ctrl = ctrl;
  }

  public boolean isShift() {
    return shift;
  }

  public void setShift(boolean shift) {
    this.shift = shift;
  }

  public boolean isMeta() {
    return meta;
  }

  public void setMeta(boolean meta) {
    this.meta = meta;
  }

  public boolean isAltgr() {
    return false; // not supported by browsers
  }

}
