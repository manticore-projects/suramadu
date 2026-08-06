package org.webswing.model.app.out;

import org.webswing.model.MsgOut;
import org.webswing.model.SyncMsg;

import java.io.Serial;
import java.util.List;
import java.util.UUID;

public class ApiCallMsgOut implements MsgOut, SyncMsg {

  public enum ApiMethod {
    HasRole, IsPermitted
  }

  @Serial
  private static final long serialVersionUID = 4470097170102688942L;

  private String correlationId = UUID.randomUUID().toString();
  private ApiMethod method;
  private List<String> args;

  public ApiMethod getMethod() {
    return method;
  }

  public void setMethod(ApiMethod method) {
    this.method = method;
  }

  public String getCorrelationId() {
    return correlationId;
  }

  public void setCorrelationId(String correlationId) {
    this.correlationId = correlationId;
  }

  public List<String> getArgs() {
    return args;
  }

  public void setArgs(List<String> args) {
    this.args = args;
  }

}
