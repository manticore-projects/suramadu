package org.webswing.model.app.out;

import org.webswing.model.MsgOut;

import java.io.Serial;

public class AppToServerFrameMsgOut implements MsgOut {

  @Serial
  private static final long serialVersionUID = -7029550828562961792L;

  private AppHandshakeMsgOut handshake;
  private ThreadDumpMsgOut threadDump;
  private ExitMsgOut exit;
  private ApiCallMsgOut apiCall;
  private JvmStatsMsgOut jvmStats;
  private SessionDataMsgOut sessionData;

  private byte[] appFrameMsgOut;

  public byte[] getAppFrameMsgOut() {
    return appFrameMsgOut;
  }

  public void setAppFrameMsgOut(byte[] appFrameMsgOut) {
    this.appFrameMsgOut = appFrameMsgOut;
  }

  public ThreadDumpMsgOut getThreadDump() {
    return threadDump;
  }

  public void setThreadDump(ThreadDumpMsgOut threadDump) {
    this.threadDump = threadDump;
  }

  public ExitMsgOut getExit() {
    return exit;
  }

  public void setExit(ExitMsgOut exit) {
    this.exit = exit;
  }

  public ApiCallMsgOut getApiCall() {
    return apiCall;
  }

  public void setApiCall(ApiCallMsgOut apiCall) {
    this.apiCall = apiCall;
  }

  public JvmStatsMsgOut getJvmStats() {
    return jvmStats;
  }

  public void setJvmStats(JvmStatsMsgOut jvmStats) {
    this.jvmStats = jvmStats;
  }

  public AppHandshakeMsgOut getHandshake() {
    return handshake;
  }

  public void setHandshake(AppHandshakeMsgOut handshake) {
    this.handshake = handshake;
  }

  public SessionDataMsgOut getSessionData() {
    return sessionData;
  }

  public void setSessionData(SessionDataMsgOut sessionData) {
    this.sessionData = sessionData;
  }

}
