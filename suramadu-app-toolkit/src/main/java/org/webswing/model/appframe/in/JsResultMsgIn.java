package org.webswing.model.appframe.in;

import org.webswing.model.MsgIn;
import org.webswing.model.SyncMsg;

import java.io.Serial;

public class JsResultMsgIn implements MsgIn, SyncMsg {
  @Serial
  private static final long serialVersionUID = 8331062247770059042L;

  private String correlationId;
  private String error;
  private JsParamMsgIn value;

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

  public JsParamMsgIn getValue() {
    return value;
  }

  public void setValue(JsParamMsgIn value) {
    this.value = value;
  }

}
