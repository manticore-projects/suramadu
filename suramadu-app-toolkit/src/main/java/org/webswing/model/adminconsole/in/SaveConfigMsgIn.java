package org.webswing.model.adminconsole.in;

import org.webswing.model.MsgIn;
import org.webswing.model.SyncMsg;

import java.io.Serial;
import java.util.List;
import java.util.UUID;

public class SaveConfigMsgIn implements SyncMsg, MsgIn {

  @Serial
  private static final long serialVersionUID = 518679934438603445L;

  private String path;
  private byte[] serverConfig;
  private boolean saveAppConfigs;
  private List<AppConfigMsgIn> appConfigs;
  private String correlationId = UUID.randomUUID().toString();

  public SaveConfigMsgIn() {}

  public SaveConfigMsgIn(String path, byte[] serverConfig, boolean saveAppConfigs,
      List<AppConfigMsgIn> appConfigs) {
    this.path = path;
    this.serverConfig = serverConfig;
    this.saveAppConfigs = saveAppConfigs;
    this.appConfigs = appConfigs;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public byte[] getServerConfig() {
    return serverConfig;
  }

  public void setServerConfig(byte[] serverConfig) {
    this.serverConfig = serverConfig;
  }

  public boolean isSaveAppConfigs() {
    return saveAppConfigs;
  }

  public void setSaveAppConfigs(boolean saveAppConfigs) {
    this.saveAppConfigs = saveAppConfigs;
  }

  public List<AppConfigMsgIn> getAppConfigs() {
    return appConfigs;
  }

  public void setAppConfigs(List<AppConfigMsgIn> appConfigs) {
    this.appConfigs = appConfigs;
  }

  public String getCorrelationId() {
    return correlationId;
  }

  public void setCorrelationId(String correlationId) {
    this.correlationId = correlationId;
  }

}
