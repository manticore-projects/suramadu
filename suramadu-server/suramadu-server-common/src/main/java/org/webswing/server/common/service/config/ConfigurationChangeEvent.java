package org.webswing.server.common.service.config;

public class ConfigurationChangeEvent<T> {
  private String path;
  private T newConfig;

  public ConfigurationChangeEvent(String path, T newConfig) {
    super();
    this.path = path;
    this.newConfig = newConfig;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public T getNewConfig() {
    return newConfig;
  }

  public void setNewConfig(T newConfig) {
    this.newConfig = newConfig;
  }

}
