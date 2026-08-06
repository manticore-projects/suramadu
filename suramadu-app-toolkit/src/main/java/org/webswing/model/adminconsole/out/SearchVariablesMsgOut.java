package org.webswing.model.adminconsole.out;

import org.webswing.model.MsgOut;
import org.webswing.model.SyncMsg;

import java.io.Serial;
import java.util.List;

public class SearchVariablesMsgOut implements SyncMsg, MsgOut {

  @Serial
  private static final long serialVersionUID = 2651350816255793897L;

  private List<MapMsgOut> variables;
  private String correlationId;

  public SearchVariablesMsgOut() {}

  public SearchVariablesMsgOut(List<MapMsgOut> variables, String correlationId) {
    super();
    this.correlationId = correlationId;
    this.variables = variables;
  }

  public List<MapMsgOut> getVariables() {
    return variables;
  }

  public void setVariables(List<MapMsgOut> variables) {
    this.variables = variables;
  }

  public String getCorrelationId() {
    return correlationId;
  }

  public void setCorrelationId(String correlationId) {
    this.correlationId = correlationId;
  }

}
