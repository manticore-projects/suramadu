package org.webswing.model.app.out;

import org.webswing.model.MsgOut;

import java.io.Serial;

public class ExitMsgOut implements MsgOut {

  @Serial
  private static final long serialVersionUID = -8007742149401885272L;

  private int waitForExit;

  /**
   * Name of the {@code ShutdownReason} constant that triggered the exit — BrowserKill, Admin,
   * Inactivity or ProcessKilled. Kept as a String so this model class does not drag the
   * suramadu-api enum into every module that touches the app frame, and so that a peer running a
   * different version still reports something readable instead of dropping the value.
   */
  private String reason;

  /** Free text detail from the application explaining the reason. May be null. */
  private String reasonDetail;

  public int getWaitForExit() {
    return waitForExit;
  }

  public void setWaitForExit(int waitForExit) {
    this.waitForExit = waitForExit;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public String getReasonDetail() {
    return reasonDetail;
  }

  public void setReasonDetail(String reasonDetail) {
    this.reasonDetail = reasonDetail;
  }

}
