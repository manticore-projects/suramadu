package org.webswing.server.common.service.config;

import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.server.model.exception.WsInitException;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public abstract class MigrationConfigurationProvider<T> extends AbstractConfigurationProvider<T> {

  private static final Logger log = LoggerFactory.getLogger(MigrationConfigurationProvider.class);

  /**
   * Config keys migrated from "SwingConfig" to "SecuredPathConfig"
   */
  private static final Set<String> MIGRATED_KEYS = Collections.unmodifiableSet(
      Sets.newHashSet("name", "uploadMaxSize", "maxClients", "sessionMode", "monitorEdtEnabled",
          "loadingAnimationDelay", "allowStealSession", "autoLogout", "goodbyeUrl"));
  private static final Set<String> SWING_CONFIG_REMOVED =
      Collections.unmodifiableSet(Sets.newHashSet("recordingsFolder", "loggingDirectory"));

  public MigrationConfigurationProvider() throws WsInitException {
    super();
  }

  public MigrationConfigurationProvider(ConfigurationUpdateHandler<T> updateHandler)
      throws WsInitException {// used via reflection
    super(updateHandler);
  }

  @Override
  protected Map<String, Object> initConfiguration(Map<String, Object> config) {
    Map<String, Object> newConfig = super.initConfiguration(config);

    if (migrate(newConfig)) {
      try {
        saveConfiguration(newConfig);
      } catch (Exception e) {
        log.error("Could not save migrated configuration!", e);
      }
    }

    return newConfig;
  }

  @SuppressWarnings("unchecked")
  private boolean migrate(Map<String, Object> config) {
    // FIXME test migration
    // only migrate if this is a CompoundSecuredPathConfig (SecuredPathConfig with SwingConfig)
    boolean migrated = false;
    for (Entry<String, Object> entry : config.entrySet()) {
      Map<String, Object> pathConfig = (Map<String, Object>) entry.getValue();

      Object swingConfigO = pathConfig.get("swingConfig");
      if (swingConfigO == null || !(swingConfigO instanceof Map)) {
        continue;
      }

      Map<String, Object> swingConfig = (Map<String, Object>) swingConfigO;

      for (String removeKey : SWING_CONFIG_REMOVED) {
        if (swingConfig.containsKey(removeKey)) {
          swingConfig.remove(removeKey);
          migrated = true;
        }
      }

      boolean needsSwingConfigMigration =
          CollectionUtils.containsAny(swingConfig.keySet(), MIGRATED_KEYS);
      boolean needsHomeDirMigration = pathConfig.containsKey("homeDir");

      if (needsSwingConfigMigration || needsHomeDirMigration) {
        migrated = true;
        migratePath(pathConfig);
      }
    }
    return migrated;
  }

  @SuppressWarnings("unchecked")
  private Map<String, Object> migratePath(Map<String, Object> pathConfig) {
    Map<String, Object> swingConfig = (Map<String, Object>) pathConfig.get("swingConfig");

    for (String migrateKey : MIGRATED_KEYS) {
      if (swingConfig.containsKey(migrateKey)) {
        pathConfig.put(migrateKey, swingConfig.remove(migrateKey));
      }
    }

    if (pathConfig.containsKey("homeDir")) {
      Object homeDir = pathConfig.get("homeDir");
      pathConfig.remove("homeDir");
      pathConfig.put("webHomeDir", homeDir);
      if (!swingConfig.containsKey("homeDir")) {
        swingConfig.put("homeDir", homeDir);
      }
    }

    return pathConfig;
  }

}
