package org.webswing.util;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

public class SessionRecordingHeader implements Serializable {

  @Serial
  private static final long serialVersionUID = 3683092380648813794L;
  public static final int version = 1;

  private Date startDate;
  private String clientId;

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

}
