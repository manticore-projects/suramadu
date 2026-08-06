package org.webswing.model.adminconsole.out;

import org.webswing.model.MsgOut;
import org.webswing.model.SyncMsg;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class InstanceCountsStatsWarningsMsgOut implements SyncMsg, MsgOut {

  @Serial
  private static final long serialVersionUID = 8702716725981252496L;

  private int runningCount;
  private int connectedCount;
  private int closedCount;
  private List<StatEntryMsgOut> summaryStats = new ArrayList<>();
  private List<SummaryWarningMsgOut> summaryWarnings = new ArrayList<>();
  private String correlationId;

  public int getRunningCount() {
    return runningCount;
  }

  public void setRunningCount(int runningCount) {
    this.runningCount = runningCount;
  }

  public int getConnectedCount() {
    return connectedCount;
  }

  public void setConnectedCount(int connectedCount) {
    this.connectedCount = connectedCount;
  }

  public int getClosedCount() {
    return closedCount;
  }

  public void setClosedCount(int closedCount) {
    this.closedCount = closedCount;
  }

  public List<StatEntryMsgOut> getSummaryStats() {
    return summaryStats;
  }

  public void setSummaryStats(List<StatEntryMsgOut> summaryStats) {
    this.summaryStats = summaryStats;
  }

  public List<SummaryWarningMsgOut> getSummaryWarnings() {
    return summaryWarnings;
  }

  public void setSummaryWarnings(List<SummaryWarningMsgOut> summaryWarnings) {
    this.summaryWarnings = summaryWarnings;
  }

  public String getCorrelationId() {
    return correlationId;
  }

  public void setCorrelationId(String correlationId) {
    this.correlationId = correlationId;
  }

}
