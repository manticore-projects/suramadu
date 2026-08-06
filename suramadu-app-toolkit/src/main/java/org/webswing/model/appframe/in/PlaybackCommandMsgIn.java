package org.webswing.model.appframe.in;

import org.webswing.model.MsgIn;

import java.io.Serial;

public class PlaybackCommandMsgIn implements MsgIn {
  @Serial
  private static final long serialVersionUID = -5390735227809092937L;

  public static enum PlaybackCommand {
    reset, play, stop, step, step10, step100;
  }

  private PlaybackCommand command;

  public PlaybackCommand getCommand() {
    return command;
  }

  public void setCommand(PlaybackCommand command) {
    this.command = command;
  }

}
