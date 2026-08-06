package org.webswing.server.common.datastore;

import java.util.List;

/**
 * Used in admin console to display list of available DataStoreModules on classpath. (as Java SPI)
 */
public interface WebswingDataStoreModuleProvider {

  List<String> getDataStoreModuleClassNames();
}
