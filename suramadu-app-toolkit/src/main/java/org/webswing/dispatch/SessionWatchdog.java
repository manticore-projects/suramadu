package org.webswing.dispatch;

import org.webswing.toolkit.api.lifecycle.ShutdownReason;

public interface SessionWatchdog {

  void resetInactivityTimers();

  void scheduleShutdown(ShutdownReason admin);

  void requestThreadDump();

  boolean isTerminated();

  void notifyExit();
}
