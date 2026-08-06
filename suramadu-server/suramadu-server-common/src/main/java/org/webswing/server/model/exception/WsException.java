package org.webswing.server.model.exception;

import java.io.Serial;

public class WsException extends Exception {
  @Serial
  private static final long serialVersionUID = -6339066097142624126L;
  private int reponseCode = 500;

  public WsException(String e) {
    super(e);
  }

  public WsException(Exception e) {
    super(e);
  }

  public WsException(String msg, int responseCode) {
    super(msg);
    reponseCode = responseCode;
  }

  public WsException(String msg, Throwable cause) {
    super(msg, cause);
  }

  public int getReponseCode() {
    return reponseCode;
  }
}
