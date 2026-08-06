package org.webswing.model.adminconsole.out;

import org.webswing.model.MsgOut;

import java.io.Serial;

public class SaveConfigAppResultMsgOut implements MsgOut {

  @Serial
  private static final long serialVersionUID = 3216083039081406233L;

  private boolean result = true;
  private String sessionPoolId;
  private String error;

  public SaveConfigAppResultMsgOut() {}

  public SaveConfigAppResultMsgOut(boolean result, String sessionPoolId) {
    super();
    this.result = result;
    this.sessionPoolId = sessionPoolId;
  }

  public SaveConfigAppResultMsgOut(boolean result, String sessionPoolId, String error) {
    this.result = result;
    this.sessionPoolId = sessionPoolId;
    this.error = error;
  }

  public boolean isResult() {
    return result;
  }

  public void setResult(boolean result) {
    this.result = result;
  }

  public String getSessionPoolId() {
    return sessionPoolId;
  }

  public void setSessionPoolId(String sessionPoolId) {
    this.sessionPoolId = sessionPoolId;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

}
