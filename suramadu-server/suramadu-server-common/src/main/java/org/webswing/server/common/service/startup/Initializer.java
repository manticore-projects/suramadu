package org.webswing.server.common.service.startup;

import org.webswing.server.model.exception.WsInitException;

public interface Initializer {

  void start() throws WsInitException;

}
