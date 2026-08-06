package org.webswing.model.adminconsole.out;

import org.webswing.model.MsgOut;
import org.webswing.model.common.in.MirroringStatusEnum;
import org.webswing.model.common.in.RecordingStatusEnum;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class SwingSessionMsgOut implements MsgOut {

  @Serial
  private static final long serialVersionUID = -562205063402702649L;

  public enum StatusEnum {
    NOT_STARTED, EXITING, RUNNING, FORCE_KILLED, FINISHED
  }

  private StatusEnum status;
  private String instanceId;
  private String user;
  private String userIp;
  private String userOs;
  private String userBrowser;
  private String application;
  private String applicationPath;
  private long startedAt;
  private long endedAt;
  private boolean connected;
  private boolean applet;
  private long disconnectedSince;
  private RecordingStatusEnum recordingStatus;
  private MirroringStatusEnum mirroringStatus;
  private String recordingFile;
  private boolean loggingEnabled;
  private boolean statisticsLoggingEnabled;

  private List<String> warnings = new ArrayList<>();
  private List<String> warningHistory = new ArrayList<>();
  private List<ThreadDumpMsgOut> threadDumps = new ArrayList<>();

  private List<MetricMsgOut> metrics = new ArrayList<>();
  private List<StatEntryMsgOut> stats = new ArrayList<>();

  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public String getInstanceId() {
    return instanceId;
  }

  public void setInstanceId(String instanceId) {
    this.instanceId = instanceId;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public String getUserIp() {
    return userIp;
  }

  public void setUserIp(String userIp) {
    this.userIp = userIp;
  }

  public String getUserOs() {
    return userOs;
  }

  public void setUserOs(String userOs) {
    this.userOs = userOs;
  }

  public String getUserBrowser() {
    return userBrowser;
  }

  public void setUserBrowser(String userBrowser) {
    this.userBrowser = userBrowser;
  }

  public String getApplication() {
    return application;
  }

  public void setApplication(String application) {
    this.application = application;
  }

  public String getApplicationPath() {
    return applicationPath;
  }

  public void setApplicationPath(String applicationPath) {
    this.applicationPath = applicationPath;
  }

  public long getStartedAt() {
    return startedAt;
  }

  public void setStartedAt(long startedAt) {
    this.startedAt = startedAt;
  }

  public long getEndedAt() {
    return endedAt;
  }

  public void setEndedAt(long endedAt) {
    this.endedAt = endedAt;
  }

  public boolean isConnected() {
    return connected;
  }

  public void setConnected(boolean connected) {
    this.connected = connected;
  }

  public boolean isApplet() {
    return applet;
  }

  public void setApplet(boolean applet) {
    this.applet = applet;
  }

  public long getDisconnectedSince() {
    return disconnectedSince;
  }

  public void setDisconnectedSince(long disconnectedSince) {
    this.disconnectedSince = disconnectedSince;
  }

  public RecordingStatusEnum getRecordingStatus() {
    return recordingStatus;
  }

  public void setRecordingStatus(RecordingStatusEnum recordingStatus) {
    this.recordingStatus = recordingStatus;
  }

  public String getRecordingFile() {
    return recordingFile;
  }

  public void setRecordingFile(String recordingFile) {
    this.recordingFile = recordingFile;
  }

  public MirroringStatusEnum getMirroringStatus() {
    return mirroringStatus;
  }

  public void setMirroringStatus(MirroringStatusEnum mirroringStatus) {
    this.mirroringStatus = mirroringStatus;
  }

  public boolean isLoggingEnabled() {
    return loggingEnabled;
  }

  public void setLoggingEnabled(boolean loggingEnabled) {
    this.loggingEnabled = loggingEnabled;
  }

  public List<String> getWarnings() {
    return warnings;
  }

  public void setWarnings(List<String> warnings) {
    this.warnings = warnings;
  }

  public List<String> getWarningHistory() {
    return warningHistory;
  }

  public void setWarningHistory(List<String> warningHistory) {
    this.warningHistory = warningHistory;
  }

  public List<ThreadDumpMsgOut> getThreadDumps() {
    return threadDumps;
  }

  public void setThreadDumps(List<ThreadDumpMsgOut> threadDumps) {
    this.threadDumps = threadDumps;
  }

  public List<MetricMsgOut> getMetrics() {
    return metrics;
  }

  public void setMetrics(List<MetricMsgOut> metrics) {
    this.metrics = metrics;
  }

  public boolean isStatisticsLoggingEnabled() {
    return statisticsLoggingEnabled;
  }

  public void setStatisticsLoggingEnabled(boolean statisticsLoggingEnabled) {
    this.statisticsLoggingEnabled = statisticsLoggingEnabled;
  }

  public List<StatEntryMsgOut> getStats() {
    return stats;
  }

  public void setStats(List<StatEntryMsgOut> stats) {
    this.stats = stats;
  }

}
