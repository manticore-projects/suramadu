package org.webswing.server.model.exception;

import java.io.Serial;

public class WsInitException extends Exception {

  @Serial
  private static final long serialVersionUID = -2905292367900301745L;

  public WsInitException(String message) {
    super(message);
  }

  public WsInitException(String msg, Throwable t) {
    super(msg, t);
  }
}
