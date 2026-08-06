package org.webswing.model.adminconsole.out;

import org.webswing.model.MsgOut;
import org.webswing.model.SyncMsg;

import java.io.Serial;
import java.util.List;

public class ResolveConfigMsgOut implements SyncMsg, MsgOut {

  @Serial
  private static final long serialVersionUID = -7226371902803856086L;

  private List<MapMsgOut> resolved;
  private String correlationId;

  public ResolveConfigMsgOut() {}

  public ResolveConfigMsgOut(List<MapMsgOut> resolved, String correlationId) {
    super();
    this.correlationId = correlationId;
    this.resolved = resolved;
  }

  public List<MapMsgOut> getResolved() {
    return resolved;
  }

  public void setResolved(List<MapMsgOut> resolved) {
    this.resolved = resolved;
  }

  public String getCorrelationId() {
    return correlationId;
  }

  public void setCorrelationId(String correlationId) {
    this.correlationId = correlationId;
  }

}
