package org.webswing.server.api.services.config.impl;

import org.webswing.server.common.model.SecuredPathConfig;
import org.webswing.server.common.service.config.ConfigurationUpdateHandler;
import org.webswing.server.common.service.config.MigrationConfigurationProvider;
import org.webswing.server.common.util.CommonUtil;
import org.webswing.server.common.util.ConfigUtil;
import org.webswing.server.model.exception.WsInitException;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ServerConfigurationProvider extends MigrationConfigurationProvider<SecuredPathConfig> {

  private Map<String, Object> originalConfiguration;

  public ServerConfigurationProvider() throws WsInitException {
    super();
  }

  public ServerConfigurationProvider(ConfigurationUpdateHandler<SecuredPathConfig> updateHandler)
      throws WsInitException {// used via reflection
    super(updateHandler);
  }

  @Override
  protected Map<String, Object> initConfiguration(Map<String, Object> config) {
    originalConfiguration = cloneJsonObject(super.initConfiguration(config));

    Map<String, Object> cloned = cloneJsonObject(originalConfiguration);
    for (Entry<String, Object> entry : cloned.entrySet()) {
      if (entry.getValue() != null && entry.getValue() instanceof Map) {
        ((Map<String, Object>) entry.getValue()).remove("swingConfig");
      }
    }

    return cloned;
  }

  @Override
  public SecuredPathConfig toConfig(String path, Map<String, Object> configuration)
      throws Exception {
    HashMap<String, Object> copy = configuration == null ? new HashMap<String, Object>()
        : new HashMap<String, Object>(configuration);
    copy.put("path", CommonUtil.toPath(path));
    return ConfigUtil.instantiateConfig(copy, SecuredPathConfig.class);
  }

  @Override
  public Map<String, Object> createDefaultConfiguration(String path) {
    HashMap<String, Object> result = new HashMap<String, Object>();
    result.put("path", path);
    return result;
  }

  @Override
  public void saveConfiguration(String path, Map<String, Object> config, boolean reload)
      throws Exception {
    if (originalConfiguration == null) {
      return;
    }
    config.put("path", path);
    if (originalConfiguration.get(path) != null) { // original configuration is null when this is a
                                                   // new app config that is being created
      config.put("swingConfig",
          ((Map<String, Object>) originalConfiguration.get(path)).get("swingConfig")); // put
                                                                                       // removed
                                                                                       // swingConfig
                                                                                       // back
    }
    super.saveConfiguration(path, config, reload);
  }

}
