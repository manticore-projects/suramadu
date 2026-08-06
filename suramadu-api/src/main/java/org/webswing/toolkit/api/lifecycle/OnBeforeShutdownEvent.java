package org.webswing.toolkit.api.lifecycle;

import java.io.Serializable;

public class OnBeforeShutdownEvent implements Serializable {
  ShutdownReason reason;

  public OnBeforeShutdownEvent(ShutdownReason reason) {
    this.reason = reason;
  }

  public ShutdownReason getReason() {
    return reason;
  }
}
