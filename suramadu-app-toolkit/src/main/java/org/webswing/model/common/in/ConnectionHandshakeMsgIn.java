package org.webswing.model.common.in;

import org.webswing.model.CommonMsg;
import org.webswing.model.MsgIn;

import java.io.Serial;
import java.util.List;

public class ConnectionHandshakeMsgIn implements MsgIn, CommonMsg {

  @Serial
  private static final long serialVersionUID = -7143564320373144470L;

  private String instanceId;
  private String browserId;
  private String viewId;
  private String tabId;
  private Integer desktopWidth;
  private Integer desktopHeight;
  private String applicationName;
  private String documentBase;
  private String locale;
  private String timeZone;
  private String url;
  private boolean mirrored;
  private boolean directDrawSupported;
  private boolean dockingSupported;
  private boolean touchMode;
  private boolean accessiblityEnabled;
  private List<ParamMsgIn> params;

  public String getInstanceId() {
    return instanceId;
  }

  public void setInstanceId(String instanceId) {
    this.instanceId = instanceId;
  }

  public String getBrowserId() {
    return browserId;
  }

  public void setBrowserId(String browserId) {
    this.browserId = browserId;
  }

  public Integer getDesktopWidth() {
    return desktopWidth;
  }

  public void setDesktopWidth(Integer desktopWidth) {
    this.desktopWidth = desktopWidth;
  }

  public Integer getDesktopHeight() {
    return desktopHeight;
  }

  public void setDesktopHeight(Integer desktopHeight) {
    this.desktopHeight = desktopHeight;
  }

  public String getApplicationName() {
    return applicationName;
  }

  public void setApplicationName(String applicationName) {
    this.applicationName = applicationName;
  }

  public boolean isMirrored() {
    return mirrored;
  }

  public void setMirrored(boolean mirrored) {
    this.mirrored = mirrored;
  }

  public boolean isDirectDrawSupported() {
    return directDrawSupported;
  }

  public void setDirectDrawSupported(boolean directDrawSupported) {
    this.directDrawSupported = directDrawSupported;
  }

  public String getDocumentBase() {
    return documentBase;
  }

  public void setDocumentBase(String documentBase) {
    this.documentBase = documentBase;
  }

  public List<ParamMsgIn> getParams() {
    return params;
  }

  public void setParams(List<ParamMsgIn> params) {
    this.params = params;
  }

  public String getLocale() {
    return locale;
  }

  public void setLocale(String locale) {
    this.locale = locale;
  }

  public String getTimeZone() {
    return timeZone;
  }

  public void setTimeZone(String timeZone) {
    this.timeZone = timeZone;
  }

  public String getViewId() {
    return viewId;
  }

  public void setViewId(String viewId) {
    this.viewId = viewId;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public boolean isDockingSupported() {
    return dockingSupported;
  }

  public void setDockingSupported(boolean dockingSupported) {
    this.dockingSupported = dockingSupported;
  }

  public boolean isTouchMode() {
    return touchMode;
  }

  public void setTouchMode(boolean touchMode) {
    this.touchMode = touchMode;
  }

  public boolean isAccessiblityEnabled() {
    return accessiblityEnabled;
  }

  public void setAccessiblityEnabled(boolean accessiblityEnabled) {
    this.accessiblityEnabled = accessiblityEnabled;
  }

  public String getTabId() {
    return tabId;
  }

  public void setTabId(String tabId) {
    this.tabId = tabId;
  }

}
