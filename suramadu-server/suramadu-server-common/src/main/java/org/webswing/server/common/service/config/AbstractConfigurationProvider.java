package org.webswing.server.common.service.config;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.Constants;
import org.webswing.server.common.model.meta.ConfigContext;
import org.webswing.server.common.model.meta.MetaObject;
import org.webswing.server.common.util.CommonUtil;
import org.webswing.server.common.util.ConfigUtil;
import org.webswing.server.common.util.WebswingObjectMapper;
import org.webswing.server.model.exception.WsException;
import org.webswing.server.model.exception.WsInitException;
import org.webswing.util.NamedThreadFactory;
import tools.jackson.core.JacksonException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class AbstractConfigurationProvider<T> implements ConfigurationProvider<T> {
  private static final Logger log = LoggerFactory.getLogger(AbstractConfigurationProvider.class);

  private ScheduledExecutorService configReloader = Executors
      .newSingleThreadScheduledExecutor(NamedThreadFactory.getInstance("Webswing Config Monitor"));
  private Map<String, Object> configuration = new HashMap<String, Object>();
  private long fileLastModified;
  private ConfigurationUpdateHandler<T> updateHandler;

  public AbstractConfigurationProvider() throws WsInitException {
    loadConfiguration();
    int interval = Integer.getInteger(Constants.CONFIG_RELOAD_INTERVAL_MS, 1000);
    if (interval > 0) {
      configReloader.scheduleWithFixedDelay(new Runnable() {
        @Override
        public void run() {
          try {
            reloadConfiguration();
          } catch (Throwable t) {
            log.error("Failed to reload configuration.", t);
          }
        }
      }, interval, interval, TimeUnit.MILLISECONDS);
    }
  }

  public AbstractConfigurationProvider(ConfigurationUpdateHandler<T> updateHandler)
      throws WsInitException {// used via reflection
    this();
    this.updateHandler = updateHandler;
  }

  @Override
  public abstract T toConfig(String path, Map<String, Object> configuration) throws Exception;

  public abstract Map<String, Object> createDefaultConfiguration(String path);

  protected Map<String, Object> initConfiguration(Map<String, Object> config) {
    return config;
  }

  @Override
  public List<String> getPaths() {
    return new ArrayList<String>(configuration.keySet());
  }

  @SuppressWarnings("unchecked")
  @Override
  public Map<String, Object> getConfiguration(String path) {
    return cloneJsonObject((Map<String, Object>) configuration.get(path));
  }

  @SuppressWarnings("unchecked")
  protected Map<String, Object> cloneJsonObject(Map<String, Object> obj) {
    if (obj != null) {
      try {
        String serialized = WebswingObjectMapper.get().writeValueAsString(obj);
        return WebswingObjectMapper.get().readValue(serialized, Map.class);
      } catch (JacksonException e) {
        log.error("Failed to clone configuration object", e);
      }
    }
    return obj;
  }

  @SuppressWarnings("unchecked")
  public void saveConfiguration(String path, Map<String, Object> configuration, boolean reload)
      throws Exception {
    try {
      File configFile = getConfigFile();
      Map<String, Object> json = WebswingObjectMapper.get().readValue(configFile, Map.class);
      json.put(path, configuration);
      synchronized (this) {
        WebswingObjectMapper.get().writerWithDefaultPrettyPrinter().writeValue(configFile, json);
        if (reload) {
          this.fileLastModified = 0;
          reloadConfiguration();
        }
      }
    } catch (Exception e) {
      log.error("Failed to save Webswing configuration :", e);
      throw new Exception("Failed to save Webswing configuration :", e);
    }
  }

  protected void saveConfiguration(Map<String, Object> configuration) throws Exception {
    try {
      synchronized (this) {
        WebswingObjectMapper.get().writerWithDefaultPrettyPrinter().writeValue(getConfigFile(),
            configuration);
      }
    } catch (Exception e) {
      log.error("Failed to save Webswing configuration :", e);
      throw new Exception("Failed to save Webswing configuration :", e);
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public void removeConfiguration(String path) throws Exception {
    try {
      File configFile = getConfigFile();
      Map<String, Object> json = WebswingObjectMapper.get().readValue(configFile, Map.class);
      json.remove(path);
      synchronized (this) {
        WebswingObjectMapper.get().writerWithDefaultPrettyPrinter().writeValue(configFile, json);
        this.fileLastModified = 0;
        reloadConfiguration();
      }
    } catch (Exception e) {
      log.error("Failed to save Webswing configuration :", e);
      throw new Exception("Failed to save Webswing configuration :", e);
    }
  }

  @Override
  public void validateConfiguration(String path, Map<String, Object> configuration)
      throws Exception {
    T c = toConfig(path, configuration);
    try {
      WebswingObjectMapper.get().writeValueAsString(c);
    } catch (Exception e) {
      throw new WsException("Configuration Json is not valid.", e);
    }
  }

  @Override
  public MetaObject describeConfiguration(String path, Map<String, Object> json, ConfigContext ctx,
      ClassLoader cl) throws WsException {
    if (json == null) {
      json = getConfiguration(path);
    }
    try {
      T config = toConfig(path, json);
      MetaObject mo = ConfigUtil.getConfigMetadata(config, cl, ctx);
      mo.setData(json);
      return mo;
    } catch (Exception e) {
      log.error("Failed to generate configuration descriptor.", e);
      throw new WsException("Failed to generate configuration descriptor.");
    }
  }

  @SuppressWarnings("unchecked")
  protected synchronized void loadConfiguration() throws WsInitException {
    try {
      File config = getConfigFile();
      if (config != null && config.exists()) {
        if (isConfigFileModified(config, this.fileLastModified)) {
          this.fileLastModified = config.lastModified();
          Map<String, Object> json = WebswingObjectMapper.get().readValue(config, Map.class);
          Map<String, Object> newConfig = ConfigUtil.fixPaths(config, json);
          configuration = initConfiguration(newConfig);
        }
      } else {
        if (this.fileLastModified != -1) {
          this.fileLastModified = -1;
          throw new WsInitException("Configuration file "
              + (config != null ? config.getPath() : null) + " does not exist!");
        }
      }
    } catch (Exception e) {
      throw new WsInitException("Webswing application configuration failed to load:", e);
    }
  }

  private boolean isConfigFileModified(File config, long lastModified) {
    long currentLastModified = config.lastModified();
    return currentLastModified != lastModified;
  }

  protected File getConfigFile() throws WsInitException {
    return CommonUtil.getConfigFile();
  }

  @Override
  public void dispose() {
    configReloader.shutdownNow();
  }

  private void reloadConfiguration() throws WsInitException {
    if (updateHandler != null) {
      Map<String, Object> oldConfig = configuration;
      loadConfiguration();
      Map<String, Object> newConfig = configuration;
      if (oldConfig != newConfig) {
        notifyChanges(oldConfig, newConfig);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private void notifyChanges(Map<String, Object> oldConfig, Map<String, Object> newConfig) {
    for (String path : newConfig.keySet()) {
      if (ObjectUtils.notEqual(oldConfig.get(path), newConfig.get(path))) {
        if (newConfig.get(path) instanceof Map) {
          try {
            // update
            updateHandler.notifyConfigChanged(path,
                toConfig(path, (Map<String, Object>) newConfig.get(path)));
            log.info("App configuration for path '{}' changed.", path);
          } catch (Exception e) {
            log.error("Failed to update app configuration for path '{}'.", path, e);
          }
        } else {
          log.warn("Path '{}' has invalid configuration value.", path);
        }
      }
      oldConfig.remove(path);
    }
    for (String path : oldConfig.keySet()) {
      try {
        // delete
        updateHandler.notifyConfigDeleted(path);
        log.error("Deleted app configuration '{}'.", path);
      } catch (Exception e) {
        log.error("Failed to delete app configuration'{}'.", path, e);
      }
    }
  }

}
