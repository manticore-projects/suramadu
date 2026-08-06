package org.webswing.server.common.service.config;

import org.webswing.server.common.model.meta.ConfigContext;
import org.webswing.server.common.model.meta.MetaObject;
import org.webswing.server.model.exception.WsException;

import java.util.List;
import java.util.Map;

public interface ConfigurationService<T> {

  T getConfiguration(String path);

  List<String> getPaths();

  void setConfiguration(String path, Map<String, Object> config) throws Exception;

  void removeConfiguration(String path) throws Exception;

  void registerChangeListener(ConfigurationChangeListener<T> changeListener);

  void removeChangeListener(ConfigurationChangeListener<T> listener);

  MetaObject describeConfiguration(String string, Map<String, Object> json, ConfigContext ctx)
      throws WsException;

}
