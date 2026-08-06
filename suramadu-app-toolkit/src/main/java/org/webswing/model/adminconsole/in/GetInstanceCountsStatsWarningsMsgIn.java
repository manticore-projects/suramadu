package org.webswing.model.adminconsole.in;

import org.webswing.model.MsgIn;
import org.webswing.model.SyncMsg;

import java.io.Serial;
import java.util.UUID;

public class GetInstanceCountsStatsWarningsMsgIn implements SyncMsg, MsgIn {

  @Serial
  private static final long serialVersionUID = -3249000552957246009L;

  private String path;
  private String correlationId = UUID.randomUUID().toString();

  public GetInstanceCountsStatsWarningsMsgIn() {}

  public GetInstanceCountsStatsWarningsMsgIn(String path) {
    this.path = path;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getCorrelationId() {
    return correlationId;
  }

  public void setCorrelationId(String correlationId) {
    this.correlationId = correlationId;
  }

}
