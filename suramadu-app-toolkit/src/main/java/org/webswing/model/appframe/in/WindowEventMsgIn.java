package org.webswing.model.appframe.in;

import org.webswing.model.MsgIn;

import java.io.Serial;

public class WindowEventMsgIn implements MsgIn {

  @Serial
  private static final long serialVersionUID = -4269267759304268713L;

  private String id;
  private int x;
  private int y;
  private int width;
  private int height;
  private WindowEventType eventType;

  public enum WindowEventType {
    close, focus, maximize, undecorate, decorate, undock, dock
  }


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
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

  public WindowEventType getEventType() {
    return eventType;
  }

  public void setEventType(WindowEventType eventType) {
    this.eventType = eventType;
  }
}
