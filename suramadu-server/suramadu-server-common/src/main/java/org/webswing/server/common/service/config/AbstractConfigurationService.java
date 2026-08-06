package org.webswing.server.common.service.config;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.server.common.extension.ExtensionClassLoader;
import org.webswing.server.common.model.meta.ConfigContext;
import org.webswing.server.common.model.meta.MetaObject;
import org.webswing.server.common.util.CommonUtil;
import org.webswing.server.model.exception.WsException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class AbstractConfigurationService<T>
    implements ConfigurationService<T>, ConfigurationUpdateHandler<T> {
  private static final Logger log = LoggerFactory.getLogger(AbstractConfigurationService.class);

  private final List<ConfigurationChangeListener<T>> changeListeners = new CopyOnWriteArrayList<>();

  protected abstract ConfigurationProvider<T> getProvider();

  protected abstract ExtensionClassLoader getExtensionClassLoader();

  protected void clearChangeListeners() {
    synchronized (changeListeners) {
      changeListeners.clear();
    }
  }

  @Override
  public void registerChangeListener(ConfigurationChangeListener<T> listener) {
    synchronized (changeListeners) {
      changeListeners.add(listener);
    }
  }

  @Override
  public void removeChangeListener(ConfigurationChangeListener<T> listener) {
    synchronized (changeListeners) {
      changeListeners.remove(listener);
    }
  }

  @Override
  public void notifyConfigChanged(String path, T newCfg) {
    synchronized (changeListeners) {
      for (ConfigurationChangeListener<T> listener : changeListeners) {
        if (listener != null) {
          listener.onConfigChanged(new ConfigurationChangeEvent<T>(asPath(path), newCfg));
        }
      }
    }
  }

  @Override
  public void notifyConfigDeleted(String path) {
    synchronized (changeListeners) {
      for (ConfigurationChangeListener<T> listener : changeListeners) {
        if (listener != null) {
          listener.onConfigDeleted(new ConfigurationChangeEvent<T>(asPath(path), null));
        }
      }
    }
  }

  @Override
  public T getConfiguration(String path) {
    path = asPath(path);
    Map<String, Object> configuration = getProvider().getConfiguration(path);
    try {
      return getProvider().toConfig(path, configuration);
    } catch (Exception e) {
      log.error("Error while resolving configuration!", e);
    }
    return null;
  }

  public List<String> getPaths() {
    return getProvider().getPaths();
  }

  @Override
  public void setConfiguration(String path, Map<String, Object> configuration) throws Exception {
    path = asPath(path);
    if (configuration == null) {
      configuration = getProvider().createDefaultConfiguration(path);
    }
    getProvider().validateConfiguration(path, configuration);
    getProvider().saveConfiguration(path, configuration, true);
  }

  @Override
  public void removeConfiguration(String path) throws Exception {
    getProvider().removeConfiguration(asPath(path));
  }

  @Override
  public MetaObject describeConfiguration(String path, Map<String, Object> json, ConfigContext ctx)
      throws WsException {
    return getProvider().describeConfiguration(asPath(path), json, ctx, getExtensionClassLoader());
  }

  public static String asPath(String path) {
    String p = CommonUtil.toPath(path);
    if (StringUtils.isBlank(p)) {
      p = "/";
    }
    return p;
  }

}
