package org.webswing.server.common.service.config;

/**
 * Created by vikto on 26-May-17.
 */
public interface ConfigurationUpdateHandler<T> {

  void notifyConfigChanged(String path, T newCfg);

  void notifyConfigDeleted(String path);

}
