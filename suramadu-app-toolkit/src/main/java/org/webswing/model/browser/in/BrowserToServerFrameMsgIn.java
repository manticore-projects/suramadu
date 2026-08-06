package org.webswing.model.browser.in;

import org.webswing.model.MsgIn;
import org.webswing.model.appframe.in.PlaybackCommandMsgIn;
import org.webswing.model.common.in.ConnectionHandshakeMsgIn;
import org.webswing.model.common.in.SimpleEventMsgIn;
import org.webswing.model.common.in.TimestampsMsgIn;

import java.io.Serial;
import java.util.List;

public class BrowserToServerFrameMsgIn implements MsgIn {

  @Serial
  private static final long serialVersionUID = -4224151320404291717L;

  private ConnectionHandshakeMsgIn handshake;
  private List<TimestampsMsgIn> timestamps;
  private List<SimpleEventMsgIn> events;
  private PlaybackCommandMsgIn playback;

  private byte[] appFrameMsgIn;

  public ConnectionHandshakeMsgIn getHandshake() {
    return handshake;
  }

  public void setHandshake(ConnectionHandshakeMsgIn handshake) {
    this.handshake = handshake;
  }

  public byte[] getAppFrameMsgIn() {
    return appFrameMsgIn;
  }

  public void setAppFrameMsgIn(byte[] appFrameMsgIn) {
    this.appFrameMsgIn = appFrameMsgIn;
  }

  public List<TimestampsMsgIn> getTimestamps() {
    return timestamps;
  }

  public void setTimestamps(List<TimestampsMsgIn> timestamps) {
    this.timestamps = timestamps;
  }

  public List<SimpleEventMsgIn> getEvents() {
    return events;
  }

  public void setEvents(List<SimpleEventMsgIn> events) {
    this.events = events;
  }

  public PlaybackCommandMsgIn getPlayback() {
    return playback;
  }

  public void setPlayback(PlaybackCommandMsgIn playback) {
    this.playback = playback;
  }

}
