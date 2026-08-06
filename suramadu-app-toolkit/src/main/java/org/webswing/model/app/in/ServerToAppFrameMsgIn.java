package org.webswing.model.app.in;

import org.webswing.model.MsgIn;
import org.webswing.model.common.in.ConnectionHandshakeMsgIn;
import org.webswing.model.common.in.SimpleEventMsgIn;
import org.webswing.model.common.in.TimestampsMsgIn;

import java.io.Serial;
import java.util.List;

public class ServerToAppFrameMsgIn implements MsgIn {

  @Serial
  private static final long serialVersionUID = -7029550828562961792L;

  private ThreadDumpRequestMsgIn threadDumpRequest;
  private ApiEventMsgIn apiEvent;
  private ApiCallResultMsgIn apiCallResult;
  private ConnectionHandshakeMsgIn handshake;
  private List<SimpleEventMsgIn> events;
  private List<TimestampsMsgIn> timestamps;

  private byte[] appFrameMsgIn;

  public byte[] getAppFrameMsgIn() {
    return appFrameMsgIn;
  }

  public void setAppFrameMsgIn(byte[] appFrameMsgIn) {
    this.appFrameMsgIn = appFrameMsgIn;
  }

  public ThreadDumpRequestMsgIn getThreadDumpRequest() {
    return threadDumpRequest;
  }

  public void setThreadDumpRequest(ThreadDumpRequestMsgIn threadDumpRequest) {
    this.threadDumpRequest = threadDumpRequest;
  }

  public ApiEventMsgIn getApiEvent() {
    return apiEvent;
  }

  public void setApiEvent(ApiEventMsgIn apiEvent) {
    this.apiEvent = apiEvent;
  }

  public ConnectionHandshakeMsgIn getHandshake() {
    return handshake;
  }

  public void setHandshake(ConnectionHandshakeMsgIn handshake) {
    this.handshake = handshake;
  }

  public ApiCallResultMsgIn getApiCallResult() {
    return apiCallResult;
  }

  public void setApiCallResult(ApiCallResultMsgIn apiCallResult) {
    this.apiCallResult = apiCallResult;
  }

  public List<SimpleEventMsgIn> getEvents() {
    return events;
  }

  public void setEvents(List<SimpleEventMsgIn> events) {
    this.events = events;
  }

  public List<TimestampsMsgIn> getTimestamps() {
    return timestamps;
  }

  public void setTimestamps(List<TimestampsMsgIn> timestamps) {
    this.timestamps = timestamps;
  }

}
