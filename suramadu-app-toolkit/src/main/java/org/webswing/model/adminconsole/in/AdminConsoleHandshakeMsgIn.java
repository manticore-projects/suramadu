package org.webswing.model.adminconsole.in;

import org.webswing.model.MsgIn;

import java.io.Serial;

public class AdminConsoleHandshakeMsgIn implements MsgIn {

  @Serial
  private static final long serialVersionUID = 7531089764777425520L;

  private String secretMessage;

  public String getSecretMessage() {
    return secretMessage;
  }

  public void setSecretMessage(String secretMessage) {
    this.secretMessage = secretMessage;
  }

}
