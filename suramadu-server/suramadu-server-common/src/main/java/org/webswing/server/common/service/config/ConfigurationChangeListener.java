package org.webswing.server.common.service.config;

public interface ConfigurationChangeListener<T> {

  void onConfigChanged(ConfigurationChangeEvent<T> e);

  void onConfigDeleted(ConfigurationChangeEvent<T> e);
}
