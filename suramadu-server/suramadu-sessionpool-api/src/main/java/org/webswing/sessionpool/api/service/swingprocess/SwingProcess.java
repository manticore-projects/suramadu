package org.webswing.sessionpool.api.service.swingprocess;

import org.webswing.server.common.model.SwingConfig;

public interface SwingProcess {

  boolean isRunning();

  void destroy(int delayMs);

  void execute() throws Exception;

  boolean isForceKilled();

  void setProcessStatusListener(ProcessStatusListener listener);

  void setProcessExitListener(ProcessExitListener listener);

  void setApplicationExitListener(ApplicationExitListener listener);

  SwingProcessConfig getConfig();

  SwingConfig getSwingConfig();

  String getInstanceId();

  void reconnect(String serverUrl);

}
