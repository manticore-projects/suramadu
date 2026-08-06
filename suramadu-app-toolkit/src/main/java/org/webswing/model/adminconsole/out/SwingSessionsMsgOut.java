package org.webswing.model.adminconsole.out;

import org.webswing.model.MsgOut;
import org.webswing.model.SyncMsg;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class SwingSessionsMsgOut implements SyncMsg, MsgOut {

  @Serial
  private static final long serialVersionUID = -562205063402702649L;

  private List<SwingSessionMsgOut> runningSessions = new ArrayList<>();
  private List<SwingSessionMsgOut> closedSessions = new ArrayList<>();

  private String correlationId;

  public List<SwingSessionMsgOut> getRunningSessions() {
    return runningSessions;
  }

  public void setRunningSessions(List<SwingSessionMsgOut> runningSessions) {
    this.runningSessions = runningSessions;
  }

  public List<SwingSessionMsgOut> getClosedSessions() {
    return closedSessions;
  }

  public void setClosedSessions(List<SwingSessionMsgOut> closedSessions) {
    this.closedSessions = closedSessions;
  }

  public String getCorrelationId() {
    return correlationId;
  }

  public void setCorrelationId(String correlationId) {
    this.correlationId = correlationId;
  }

}
