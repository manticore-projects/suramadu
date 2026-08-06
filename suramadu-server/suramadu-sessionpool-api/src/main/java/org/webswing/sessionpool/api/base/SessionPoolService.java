package org.webswing.sessionpool.api.base;

import org.webswing.server.model.exception.WsInitException;

public interface SessionPoolService {

  void start() throws WsInitException;

  void stop();

}
