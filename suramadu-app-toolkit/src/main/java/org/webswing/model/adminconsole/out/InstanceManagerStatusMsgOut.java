package org.webswing.model.adminconsole.out;

import org.webswing.model.MsgOut;

import java.io.Serial;

public class InstanceManagerStatusMsgOut implements MsgOut {

  @Serial
  private static final long serialVersionUID = -8087696319719546037L;

  private String status;
  private String error;
  private String errorDetails;

  public InstanceManagerStatusMsgOut() {}

  public InstanceManagerStatusMsgOut(String status, String error, String errorDetails) {
    this.status = status;
    this.error = error;
    this.errorDetails = errorDetails;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public String getErrorDetails() {
    return errorDetails;
  }

  public void setErrorDetails(String errorDetails) {
    this.errorDetails = errorDetails;
  }

}
