package org.webswing.model.appframe.out;

import org.webswing.model.MsgOut;
import org.webswing.model.SyncMsg;

import java.io.Serial;

public class JsResultMsgOut implements MsgOut, SyncMsg {
  @Serial
  private static final long serialVersionUID = 8331062247770059042L;

  private String correlationId;
  private String error;
  private JsParamMsgOut value;

  public String getCorrelationId() {
    return correlationId;
  }

  public void setCorrelationId(String correlationId) {
    this.correlationId = correlationId;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public JsParamMsgOut getValue() {
    return value;
  }

  public void setValue(JsParamMsgOut value) {
    this.value = value;
  }

}
