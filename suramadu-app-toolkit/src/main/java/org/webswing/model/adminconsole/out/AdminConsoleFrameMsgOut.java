package org.webswing.model.adminconsole.out;

import org.webswing.model.MsgOut;

import java.io.Serial;
import java.util.List;

public class AdminConsoleFrameMsgOut implements MsgOut {

  @Serial
  private static final long serialVersionUID = 3253892297375570414L;

  private String path;
  private ThreadDumpMsgOut threadDump;
  private InstanceCountsStatsWarningsMsgOut instanceCountsStatsWarnings;
  private SwingSessionsMsgOut swingSessions;
  private List<RegisterInstanceMsgOut> registerInstances;
  private ServerInfoMsgOut serverInfo;
  private ConfigMsgOut config;
  private MetaMsgOut meta;
  private ResolveConfigMsgOut resolveConfig;
  private SearchVariablesMsgOut searchVariables;
  private SaveConfigResultMsgOut saveConfigResult;
  private AccessTokenCreatedMsgOut accessTokenCreated;
  private MirrorFrameMsgOut mirrorFrame;

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public ThreadDumpMsgOut getThreadDump() {
    return threadDump;
  }

  public void setThreadDump(ThreadDumpMsgOut threadDump) {
    this.threadDump = threadDump;
  }

  public InstanceCountsStatsWarningsMsgOut getInstanceCountsStatsWarnings() {
    return instanceCountsStatsWarnings;
  }

  public void setInstanceCountsStatsWarnings(
      InstanceCountsStatsWarningsMsgOut instanceCountsStatsWarnings) {
    this.instanceCountsStatsWarnings = instanceCountsStatsWarnings;
  }

  public SwingSessionsMsgOut getSwingSessions() {
    return swingSessions;
  }

  public void setSwingSessions(SwingSessionsMsgOut swingSessions) {
    this.swingSessions = swingSessions;
  }

  public List<RegisterInstanceMsgOut> getRegisterInstances() {
    return registerInstances;
  }

  public void setRegisterInstances(List<RegisterInstanceMsgOut> registerInstances) {
    this.registerInstances = registerInstances;
  }

  public ServerInfoMsgOut getServerInfo() {
    return serverInfo;
  }

  public void setServerInfo(ServerInfoMsgOut serverInfo) {
    this.serverInfo = serverInfo;
  }

  public ConfigMsgOut getConfig() {
    return config;
  }

  public void setConfig(ConfigMsgOut config) {
    this.config = config;
  }

  public MetaMsgOut getMeta() {
    return meta;
  }

  public void setMeta(MetaMsgOut meta) {
    this.meta = meta;
  }

  public ResolveConfigMsgOut getResolveConfig() {
    return resolveConfig;
  }

  public void setResolveConfig(ResolveConfigMsgOut resolveConfig) {
    this.resolveConfig = resolveConfig;
  }

  public SearchVariablesMsgOut getSearchVariables() {
    return searchVariables;
  }

  public void setSearchVariables(SearchVariablesMsgOut searchVariables) {
    this.searchVariables = searchVariables;
  }

  public SaveConfigResultMsgOut getSaveConfigResult() {
    return saveConfigResult;
  }

  public void setSaveConfigResult(SaveConfigResultMsgOut saveConfigResult) {
    this.saveConfigResult = saveConfigResult;
  }

  public AccessTokenCreatedMsgOut getAccessTokenCreated() {
    return accessTokenCreated;
  }

  public void setAccessTokenCreated(AccessTokenCreatedMsgOut accessTokenCreated) {
    this.accessTokenCreated = accessTokenCreated;
  }

  public MirrorFrameMsgOut getMirrorFrame() {
    return mirrorFrame;
  }

  public void setMirrorFrame(MirrorFrameMsgOut mirrorFrame) {
    this.mirrorFrame = mirrorFrame;
  }

}
