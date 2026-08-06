package org.webswing.server.services.config.impl;

import org.webswing.server.common.model.SwingConfig;
import org.webswing.server.common.service.config.ConfigurationUpdateHandler;
import org.webswing.server.common.service.config.MigrationConfigurationProvider;
import org.webswing.server.common.util.ConfigUtil;
import org.webswing.server.model.exception.WsInitException;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class LocalSessionPoolConfigurationProvider
    extends MigrationConfigurationProvider<SwingConfig> {

  private Map<String, Object> originalConfiguration;

  public LocalSessionPoolConfigurationProvider() throws WsInitException {
    super();
  }

  public LocalSessionPoolConfigurationProvider(
      ConfigurationUpdateHandler<SwingConfig> updateHandler) throws WsInitException {// used via
                                                                                     // reflection
    super(updateHandler);
  }

  @Override
  protected Map<String, Object> initConfiguration(Map<String, Object> config) {
    originalConfiguration = cloneJsonObject(super.initConfiguration(config));

    Map<String, Object> configMap = new HashMap<>();
    Map<String, Object> cloned = cloneJsonObject(originalConfiguration);
    for (Entry<String, Object> entry : cloned.entrySet()) {
      if (entry.getValue() != null && entry.getValue() instanceof Map) {
        Object swingConfig = ((Map<String, Object>) entry.getValue()).get("swingConfig");
        if (swingConfig != null && swingConfig instanceof Map) {
          configMap.put(entry.getKey(), swingConfig);
        } else {
          configMap.put(entry.getKey(), new HashMap<>());
        }
      }
    }

    return configMap;
  }

  @Override
  public SwingConfig toConfig(String path, Map<String, Object> configuration) throws Exception {
    return ConfigUtil.instantiateConfig(configuration, SwingConfig.class);
  }

  @Override
  public Map<String, Object> createDefaultConfiguration(String path) {
    HashMap<String, Object> result = new HashMap<String, Object>();
    return result;
  }

  @Override
  public void saveConfiguration(String path, Map<String, Object> config, boolean reload)
      throws Exception {
    // reload configuration from file -> changed from ServerConfigurationProvider updated with
    // server config
    loadConfiguration();
    Map<String, Object> cloned = cloneJsonObject(originalConfiguration);
    Map<String, Object> saveConfig = (Map<String, Object>) cloned.get(path);
    saveConfig.put("swingConfig", config);
    super.saveConfiguration(path, saveConfig, reload);
  }

}
