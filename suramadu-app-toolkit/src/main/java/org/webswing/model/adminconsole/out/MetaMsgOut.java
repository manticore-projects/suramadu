package org.webswing.model.adminconsole.out;

import org.webswing.model.MsgOut;
import org.webswing.model.SyncMsg;

import java.io.Serial;
import java.util.List;

public class MetaMsgOut implements SyncMsg, MsgOut {

  @Serial
  private static final long serialVersionUID = -7226371902803856086L;

  private byte[] serverConfig;
  private String serverError;
  private List<AppConfigMsgOut> appConfigs;
  private String correlationId;

  public MetaMsgOut() {}

  public MetaMsgOut(byte[] serverConfig, String serverError, List<AppConfigMsgOut> appConfigs,
      String correlationId) {
    super();
    this.serverConfig = serverConfig;
    this.serverError = serverError;
    this.appConfigs = appConfigs;
    this.correlationId = correlationId;
  }

  public byte[] getServerConfig() {
    return serverConfig;
  }

  public void setServerConfig(byte[] serverConfig) {
    this.serverConfig = serverConfig;
  }

  public String getServerError() {
    return serverError;
  }

  public void setServerError(String serverError) {
    this.serverError = serverError;
  }

  public List<AppConfigMsgOut> getAppConfigs() {
    return appConfigs;
  }

  public void setAppConfigs(List<AppConfigMsgOut> appConfigs) {
    this.appConfigs = appConfigs;
  }

  public String getCorrelationId() {
    return correlationId;
  }

  public void setCorrelationId(String correlationId) {
    this.correlationId = correlationId;
  }

}
