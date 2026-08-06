package org.webswing.server.common.service.config;

import org.webswing.server.common.model.meta.ConfigContext;
import org.webswing.server.common.model.meta.MetaObject;
import org.webswing.server.model.exception.WsException;

import java.util.List;
import java.util.Map;

public interface ConfigurationProvider<T> {

  List<String> getPaths();

  Map<String, Object> getConfiguration(String path);

  void saveConfiguration(String path, Map<String, Object> configuration, boolean reload)
      throws Exception;

  void removeConfiguration(String path) throws Exception;

  void validateConfiguration(String path, Map<String, Object> c) throws Exception;

  T toConfig(String path, Map<String, Object> configuration) throws Exception;

  MetaObject describeConfiguration(String path, Map<String, Object> json, ConfigContext ctx,
      ClassLoader cl) throws WsException;

  Map<String, Object> createDefaultConfiguration(String path);

  void dispose();

}
