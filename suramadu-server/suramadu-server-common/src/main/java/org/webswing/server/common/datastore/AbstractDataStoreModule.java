package org.webswing.server.common.datastore;

public abstract class AbstractDataStoreModule<T extends WebswingDataStoreModuleConfig>
    implements WebswingDataStoreModule {

  private final T config;

  public AbstractDataStoreModule(T config) {
    this.config = config;
  }

  public T getConfig() {
    return config;
  }

}
