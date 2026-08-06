package org.webswing.server.common.datastore;

import org.webswing.server.common.model.Config;

/**
 * Configuration for {@link WebswingDataStoreModule} implementations. This interface is dynamically
 * instantiated by Webswing to provide easy access to JSON configuration values stored in
 * webswing.config file. {@link WebswingDataStoreModule} implementations should extend this
 * interface with their own specific java beans getters, to read configuration options from JSON
 * format.
 */
public interface WebswingDataStoreModuleConfig extends Config {

}
