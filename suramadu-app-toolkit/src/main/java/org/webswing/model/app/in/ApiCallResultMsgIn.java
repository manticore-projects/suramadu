package org.webswing.model.app.in;

import org.webswing.model.MsgIn;
import org.webswing.model.SyncMsg;

import java.io.Serial;

public class ApiCallResultMsgIn implements MsgIn, SyncMsg {

  @Serial
  private static final long serialVersionUID = 4470097170102688942L;

  private String correlationId;
  private String result;

  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }

  public String getCorrelationId() {
    return correlationId;
  }

  public void setCorrelationId(String correlationId) {
    this.correlationId = correlationId;
  }

}
