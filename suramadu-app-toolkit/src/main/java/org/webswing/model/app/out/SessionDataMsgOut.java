package org.webswing.model.app.out;

import org.webswing.model.MsgOut;
import org.webswing.model.common.in.MirroringStatusEnum;
import org.webswing.model.common.in.RecordingStatusEnum;

import java.io.Serial;

public class SessionDataMsgOut implements MsgOut {

  @Serial
  private static final long serialVersionUID = 4505813670370834105L;

  private boolean applet;
  private boolean sessionLoggingEnabled;
  private RecordingStatusEnum recordingStatus;
  private String recordingFile;
  private MirroringStatusEnum mirroringStatus;
  private boolean statisticsLoggingEnabled;

  public SessionDataMsgOut() {}

  public SessionDataMsgOut(boolean applet, boolean sessionLoggingEnabled,
      RecordingStatusEnum recordingStatus, String recordingFile,
      MirroringStatusEnum mirroringStatus, boolean statisticsLoggingEnabled) {
    super();
    this.applet = applet;
    this.sessionLoggingEnabled = sessionLoggingEnabled;
    this.recordingStatus = recordingStatus;
    this.mirroringStatus = mirroringStatus;
    this.recordingFile = recordingFile;
    this.statisticsLoggingEnabled = statisticsLoggingEnabled;
  }

  public boolean isApplet() {
    return applet;
  }

  public void setApplet(boolean applet) {
    this.applet = applet;
  }

  public boolean isSessionLoggingEnabled() {
    return sessionLoggingEnabled;
  }

  public void setSessionLoggingEnabled(boolean sessionLoggingEnabled) {
    this.sessionLoggingEnabled = sessionLoggingEnabled;
  }

  public RecordingStatusEnum getRecordingStatus() {
    return recordingStatus;
  }

  public void setRecordingStatus(RecordingStatusEnum recordingStatus) {
    this.recordingStatus = recordingStatus;
  }

  public MirroringStatusEnum getMirroringStatus() {
    return mirroringStatus;
  }

  public void setMirroringStatus(MirroringStatusEnum mirroringStatus) {
    this.mirroringStatus = mirroringStatus;
  }

  public String getRecordingFile() {
    return recordingFile;
  }

  public void setRecordingFile(String recordingFile) {
    this.recordingFile = recordingFile;
  }

  public boolean isStatisticsLoggingEnabled() {
    return statisticsLoggingEnabled;
  }

  public void setStatisticsLoggingEnabled(boolean statisticsLoggingEnabled) {
    this.statisticsLoggingEnabled = statisticsLoggingEnabled;
  }

}
