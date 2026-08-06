package org.webswing.model.appframe.out;

import org.webswing.model.MsgOut;

import java.awt.Point;
import java.io.Serial;
import java.util.List;

public class WindowMsgOut implements MsgOut {

  @Serial
  private static final long serialVersionUID = -523823816533325842L;

  public enum WindowType {
    basic, html, internal, internalHtml, internalWrapper;
  }

  public enum WindowClassType {
    other, Window, JWindow, Dialog, JDialog, Frame, JFrame;
  }

  public enum DockMode {
    none, dockable, autoUndock
  }

  public enum DockState {
    docked, undocked
  }

  private String id;
  private List<WindowPartialContentMsgOut> content;
  private byte[] directDraw;
  private String title;
  private String name;
  private Integer posX;
  private Integer posY;
  private Integer width;
  private Integer height;
  private WindowType type = WindowType.basic;
  private boolean modalBlocked;
  private String ownerId;
  private Integer state;
  private List<WindowMsgOut> internalWindows;
  private DockMode dockMode = DockMode.none;
  private DockState dockState = DockState.docked;
  private WindowClassType classType = WindowClassType.other;

  public WindowMsgOut() {}

  public WindowMsgOut(String id, String name, Point location, Integer width, Integer height,
      WindowType type, boolean modalBlocked, String ownerId) {
    super();
    this.id = id;
    this.name = name;
    this.posX = location.x;
    this.posY = location.y;
    this.width = width;
    this.height = height;
    this.type = type;
    this.modalBlocked = modalBlocked;
    this.ownerId = ownerId;
  }

  public void setBounds(Integer posX, Integer posY, Integer width, Integer height) {
    this.posX = posX;
    this.posY = posY;
    this.width = width;
    this.height = height;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public List<WindowPartialContentMsgOut> getContent() {
    return content;
  }

  public void setContent(List<WindowPartialContentMsgOut> content) {
    this.content = content;
  }

  public byte[] getDirectDraw() {
    return directDraw;
  }

  public void setDirectDraw(byte[] directDraw) {
    this.directDraw = directDraw;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getPosX() {
    return posX;
  }

  public void setPosX(Integer posX) {
    this.posX = posX;
  }

  public Integer getPosY() {
    return posY;
  }

  public void setPosY(Integer posY) {
    this.posY = posY;
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

  public boolean isModalBlocked() {
    return modalBlocked;
  }

  public void setModalBlocked(boolean modalBlocked) {
    this.modalBlocked = modalBlocked;
  }

  public String getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public WindowType getType() {
    return type;
  }

  public void setType(WindowType type) {
    this.type = type;
  }

  public List<WindowMsgOut> getInternalWindows() {
    return internalWindows;
  }

  public void setInternalWindows(List<WindowMsgOut> internalWindows) {
    this.internalWindows = internalWindows;
  }

  public DockMode getDockMode() {
    return dockMode;
  }

  public void setDockMode(DockMode dockMode) {
    this.dockMode = dockMode;
  }

  public WindowClassType getClassType() {
    return classType;
  }

  public void setClassType(WindowClassType classType) {
    this.classType = classType;
  }

  public DockState getDockState() {
    return dockState;
  }

  public void setDockState(DockState dockState) {
    this.dockState = dockState;
  }
}
