package org.webswing.model.appframe.out;

import org.webswing.model.MsgOut;

import java.io.Serial;

public class WindowPartialContentMsgOut implements MsgOut {

  @Serial
  private static final long serialVersionUID = 5963096092200874758L;
  private Integer positionX;
  private Integer positionY;
  private Integer width;
  private Integer height;
  private byte[] base64Content;

  public WindowPartialContentMsgOut() {}

  public WindowPartialContentMsgOut(Integer positionX, Integer positionY, Integer width,
      Integer height) {
    this.positionX = positionX;
    this.positionY = positionY;
    this.width = width;
    this.height = height;
  }

  public Integer getPositionX() {
    return positionX;
  }

  public void setPositionX(Integer positionX) {
    this.positionX = positionX;
  }

  public Integer getPositionY() {
    return positionY;
  }

  public void setPositionY(Integer positionY) {
    this.positionY = positionY;
  }

  public Integer getWidth() {
    return width;
  }

  public void setWidth(Integer width) {
    this.width = width;
  }

  public Integer getHeight() {
    return height;
  }

  public void setHeight(Integer height) {
    this.height = height;
  }

  public byte[] getBase64Content() {
    return base64Content;
  }

  public void setBase64Content(byte[] base64Content) {
    this.base64Content = base64Content;
  }

}
