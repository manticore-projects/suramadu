package org.webswing.model.adminconsole.out;

import org.webswing.model.MsgOut;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class SummaryWarningMsgOut implements MsgOut {

  @Serial
  private static final long serialVersionUID = 7263123944623475517L;

  private String instanceId;
  private List<String> warnings = new ArrayList<>();

  public String getInstanceId() {
    return instanceId;
  }

  public void setInstanceId(String instanceId) {
    this.instanceId = instanceId;
  }

  public List<String> getWarnings() {
    return warnings;
  }

  public void setWarnings(List<String> warnings) {
    this.warnings = warnings;
  }

}
