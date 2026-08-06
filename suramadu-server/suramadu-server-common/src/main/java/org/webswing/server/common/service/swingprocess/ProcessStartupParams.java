package org.webswing.server.common.service.swingprocess;

import org.webswing.server.common.model.SwingConfig;
import org.webswing.server.common.util.VariableSubstitutor;

import java.io.File;
import java.util.Map;
import java.util.function.Function;

public class ProcessStartupParams {

  private String websocketUrl;
  private String instanceId;
  private String pathMapping;
  private String appName;
  private int debugPort;
  private boolean recordingFlagged;
  private boolean askForRecordingNeeded;
  private boolean askForMirroringNeeded;
  private SwingConfig appConfig;
  private VariableSubstitutor subs;
  private Function<String, File> fileResolver;
  private Integer screenWidth;
  private Integer screenHeight;
  private Map<String, String> params;
  private String documentBase;
  private String handshakeUrl;
  private boolean directDrawSupported;
  private boolean accessiblityEnabled;
  private boolean touchModeEnabled;
  private boolean dockingSupported;
  private String dataStoreConfig;
  private String userId;

  private String appConnectionSecret;

  public ProcessStartupParams() {}

  public String getWebsocketUrl() {
    return websocketUrl;
  }

  public void setWebsocketUrl(String websocketUrl) {
    this.websocketUrl = websocketUrl;
  }

  public String getInstanceId() {
    return instanceId;
  }

  public void setInstanceId(String instanceId) {
    this.instanceId = instanceId;
  }

  public String getPathMapping() {
    return pathMapping;
  }

  public void setPathMapping(String pathMapping) {
    this.pathMapping = pathMapping;
  }

  public String getAppName() {
    return appName;
  }

  public void setAppName(String appName) {
    this.appName = appName;
  }

  public int getDebugPort() {
    return debugPort;
  }

  public void setDebugPort(int debugPort) {
    this.debugPort = debugPort;
  }

  public SwingConfig getAppConfig() {
    return appConfig;
  }

  public void setAppConfig(SwingConfig appConfig) {
    this.appConfig = appConfig;
  }

  public VariableSubstitutor getSubs() {
    return subs;
  }

  public void setSubs(VariableSubstitutor subs) {
    this.subs = subs;
  }

  public Function<String, File> getFileResolver() {
    return fileResolver;
  }

  public void setFileResolver(Function<String, File> fileResolver) {
    this.fileResolver = fileResolver;
  }

  public Integer getScreenWidth() {
    return screenWidth;
  }

  public void setScreenWidth(Integer screenWidth) {
    this.screenWidth = screenWidth;
  }

  public Integer getScreenHeight() {
    return screenHeight;
  }

  public void setScreenHeight(Integer screenHeight) {
    this.screenHeight = screenHeight;
  }

  public Map<String, String> getParams() {
    return params;
  }

  public void setParams(Map<String, String> params) {
    this.params = params;
  }

  public String getDocumentBase() {
    return documentBase;
  }

  public void setDocumentBase(String documentBase) {
    this.documentBase = documentBase;
  }

  public String getHandshakeUrl() {
    return handshakeUrl;
  }

  public void setHandshakeUrl(String handshakeUrl) {
    this.handshakeUrl = handshakeUrl;
  }

  public boolean isDirectDrawSupported() {
    return directDrawSupported;
  }

  public void setDirectDrawSupported(boolean directDrawSupported) {
    this.directDrawSupported = directDrawSupported;
  }

  public boolean isAccessiblityEnabled() {
    return accessiblityEnabled;
  }

  public void setAccessiblityEnabled(boolean accessiblityEnabled) {
    this.accessiblityEnabled = accessiblityEnabled;
  }

  public boolean isTouchModeEnabled() {
    return touchModeEnabled;
  }

  public void setTouchModeEnabled(boolean touchModeEnabled) {
    this.touchModeEnabled = touchModeEnabled;
  }

  public boolean isDockingSupported() {
    return dockingSupported;
  }

  public void setDockingSupported(boolean dockingSupported) {
    this.dockingSupported = dockingSupported;
  }

  public String getAppConnectionSecret() {
    return appConnectionSecret;
  }

  public void setAppConnectionSecret(String appConnectionSecret) {
    this.appConnectionSecret = appConnectionSecret;
  }

  public boolean isRecordingFlagged() {
    return recordingFlagged;
  }

  public void setRecordingFlagged(boolean recordingFlagged) {
    this.recordingFlagged = recordingFlagged;
  }

  public boolean isAskForRecordingNeeded() {
    return askForRecordingNeeded;
  }

  public void setAskForRecordingNeeded(boolean askForRecordingNeeded) {
    this.askForRecordingNeeded = askForRecordingNeeded;
  }

  public boolean isAskForMirroringNeeded() {
    return askForMirroringNeeded;
  }

  public void setAskForMirroringNeeded(boolean askForMirroringNeeded) {
    this.askForMirroringNeeded = askForMirroringNeeded;
  }

  public String getDataStoreConfig() {
    return dataStoreConfig;
  }

  public void setDataStoreConfig(String dataStoreConfig) {
    this.dataStoreConfig = dataStoreConfig;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

}
