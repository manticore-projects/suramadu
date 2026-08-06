package org.webswing.model.adminconsole.out;

import org.webswing.model.MsgOut;

import java.io.Serial;

public class ApplicationInfoMsgOut implements MsgOut {

  @Serial
  private static final long serialVersionUID = 7963526114521293161L;

  private String path;
  private String name;
  private byte[] byteIcon;
  private boolean enabled;
  private int maxClients;
  private InstanceManagerStatusMsgOut instanceManagerStatus;

  public ApplicationInfoMsgOut() {}

  public ApplicationInfoMsgOut(String path, String name, byte[] byteIcon, boolean enabled,
      int maxClients, InstanceManagerStatusMsgOut instanceManagerStatus) {
    super();
    this.path = path;
    this.name = name;
    this.byteIcon = byteIcon;
    this.enabled = enabled;
    this.maxClients = maxClients;
    this.instanceManagerStatus = instanceManagerStatus;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public byte[] getByteIcon() {
    return byteIcon;
  }

  public void setByteIcon(byte[] byteIcon) {
    this.byteIcon = byteIcon;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public InstanceManagerStatusMsgOut getInstanceManagerStatus() {
    return instanceManagerStatus;
  }

  public void setInstanceManagerStatus(InstanceManagerStatusMsgOut instanceManagerStatus) {
    this.instanceManagerStatus = instanceManagerStatus;
  }

  public int getMaxClients() {
    return maxClients;
  }

  public void setMaxClients(int maxClients) {
    this.maxClients = maxClients;
  }

}
