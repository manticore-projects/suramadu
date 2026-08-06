package org.webswing.model.appframe.in;

import org.webswing.model.MsgIn;
import org.webswing.model.SyncMsg;

import java.io.Serial;
import java.util.List;

public class JavaEvalRequestMsgIn implements MsgIn, SyncMsg {
  @Serial
  private static final long serialVersionUID = 7491354280941799313L;

  private String correlationId;
  private String objectId;
  private String method;
  private List<JsParamMsgIn> params;

  public String getCorrelationId() {
    return correlationId;
  }

  public void setCorrelationId(String correlationId) {
    this.correlationId = correlationId;
  }

  public String getObjectId() {
    return objectId;
  }

  public void setObjectId(String objectId) {
    this.objectId = objectId;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public List<JsParamMsgIn> getParams() {
    return params;
  }

  public void setParams(List<JsParamMsgIn> params) {
    this.params = params;
  }

}
