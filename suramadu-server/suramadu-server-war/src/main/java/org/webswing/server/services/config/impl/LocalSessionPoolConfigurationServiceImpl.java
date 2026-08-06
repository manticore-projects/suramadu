package org.webswing.server.services.config.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.Constants;
import org.webswing.server.common.extension.ExtensionClassLoader;
import org.webswing.server.common.model.SwingConfig;
import org.webswing.server.common.service.config.AbstractConfigurationService;
import org.webswing.server.common.service.config.ConfigurationProvider;
import org.webswing.server.common.service.config.ConfigurationUpdateHandler;
import org.webswing.server.model.exception.WsInitException;
import org.webswing.sessionpool.api.base.SessionPoolService;

import java.lang.reflect.Constructor;

@Singleton
public class LocalSessionPoolConfigurationServiceImpl
    extends AbstractConfigurationService<SwingConfig> implements SessionPoolService {
  private static final Logger log =
      LoggerFactory.getLogger(LocalSessionPoolConfigurationServiceImpl.class);

  private final ExtensionClassLoader extensionLoader;
  private ConfigurationProvider<SwingConfig> provider;

  @Inject
  public LocalSessionPoolConfigurationServiceImpl(ExtensionClassLoader extensionLoader) {
    this.extensionLoader = extensionLoader;
  }

  @Override
  public void start() throws WsInitException {
    String providerClassName = System.getProperty(Constants.CONFIG_PROVIDER,
        LocalSessionPoolConfigurationProvider.class.getName());
    try {
      Class<?> providerClass = extensionLoader.loadClass(providerClassName);
      try {
        Constructor<?> constructor =
            providerClass.getDeclaredConstructor(ConfigurationUpdateHandler.class);
        provider = (ConfigurationProvider<SwingConfig>) constructor.newInstance(this);
      } catch (NoSuchMethodException e) {
        provider = (ConfigurationProvider<SwingConfig>) providerClass.newInstance();
      }
    } catch (Exception e) {
      throw new WsInitException("Could not instantiate configuration provider " + providerClassName,
          e);
    }

  }

  @Override
  public void stop() {
    clearChangeListeners();
    if (provider != null) {
      try {
        provider.dispose();
        provider = null;
      } catch (Exception e) {
        log.error("Failed to dispose config provider", e);
      }
    }
  }

  @Override
  protected ConfigurationProvider<SwingConfig> getProvider() {
    return provider;
  }

  @Override
  protected ExtensionClassLoader getExtensionClassLoader() {
    return extensionLoader;
  }

}
