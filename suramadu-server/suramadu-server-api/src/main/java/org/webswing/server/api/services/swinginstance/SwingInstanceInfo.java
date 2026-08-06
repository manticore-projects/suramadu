package org.webswing.server.api.services.swinginstance;

import org.webswing.server.common.datastore.WebswingDataStoreConfig;
import org.webswing.server.common.model.SecuredPathConfig;

public class SwingInstanceInfo {

  private SecuredPathConfig config;
  private String sessionPoolId;
  private String ownerId;
  private String instanceId;
  private String urlContext;
  private String pathMapping;
  private WebswingDataStoreConfig dataStoreConfig;

  public SwingInstanceInfo(String urlContext, String pathMapping, SecuredPathConfig config,
      WebswingDataStoreConfig dataStoreConfig) {
    this.urlContext = urlContext;
    this.pathMapping = pathMapping;
    this.config = config;
    this.dataStoreConfig = dataStoreConfig;
  }

  public SecuredPathConfig getConfig() {
    return config;
  }

  public void setConfig(SecuredPathConfig config) {
    this.config = config;
  }

  public String getSessionPoolId() {
    return sessionPoolId;
  }

  public void setSessionPoolId(String sessionPoolId) {
    this.sessionPoolId = sessionPoolId;
  }

  public String getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
  }

  public String getInstanceId() {
    return instanceId;
  }

  public void setInstanceId(String instanceId) {
    this.instanceId = instanceId;
  }

  public String getUrlContext() {
    return urlContext;
  }

  public void setUrlContext(String urlContext) {
    this.urlContext = urlContext;
  }

  public String getPathMapping() {
    return pathMapping;
  }

  public void setPathMapping(String pathMapping) {
    this.pathMapping = pathMapping;
  }

  public WebswingDataStoreConfig getDataStoreConfig() {
    return dataStoreConfig;
  }

  public void setDataStoreConfig(WebswingDataStoreConfig dataStoreConfig) {
    this.dataStoreConfig = dataStoreConfig;
  }

}
