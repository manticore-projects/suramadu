package org.webswing.model.adminconsole.out;

import org.webswing.model.MsgOut;
import org.webswing.model.SyncMsg;

import java.io.Serial;
import java.util.List;

public class SaveConfigResultMsgOut implements SyncMsg, MsgOut {

  @Serial
  private static final long serialVersionUID = 3216083039081406233L;

  private boolean serverResult;
  private String serverError;
  private List<SaveConfigAppResultMsgOut> appResults;
  private String correlationId;

  public SaveConfigResultMsgOut() {}

  public SaveConfigResultMsgOut(boolean serverResult, String serverError,
      List<SaveConfigAppResultMsgOut> appResults, String correlationId) {
    this.serverResult = serverResult;
    this.serverError = serverError;
    this.appResults = appResults;
    this.correlationId = correlationId;
  }

  public boolean isServerResult() {
    return serverResult;
  }

  public void setServerResult(boolean serverResult) {
    this.serverResult = serverResult;
  }

  public String getServerError() {
    return serverError;
  }

  public void setServerError(String serverError) {
    this.serverError = serverError;
  }

  public List<SaveConfigAppResultMsgOut> getAppResults() {
    return appResults;
  }

  public void setAppResults(List<SaveConfigAppResultMsgOut> appResults) {
    this.appResults = appResults;
  }

  public String getCorrelationId() {
    return correlationId;
  }

  public void setCorrelationId(String correlationId) {
    this.correlationId = correlationId;
  }

}
