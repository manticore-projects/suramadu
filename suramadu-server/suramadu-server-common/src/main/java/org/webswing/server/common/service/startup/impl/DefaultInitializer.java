package org.webswing.server.common.service.startup.impl;

import main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.ConfigFileResolver;
import org.webswing.Constants;
import org.webswing.server.common.service.startup.Initializer;
import org.webswing.server.common.util.CommonUtil;
import org.webswing.server.model.exception.WsInitException;

import java.io.File;
import java.io.FileNotFoundException;

public class DefaultInitializer implements Initializer {
  private static final Logger log = LoggerFactory.getLogger(DefaultInitializer.class);

  public DefaultInitializer() {
    try {
      start();
    } catch (WsInitException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public void start() throws WsInitException {
    // initialize temp folder (create/delete old content)
    File tempDir = Main.getTempDir();
    log.info("Using Temp folder: {}", tempDir.getAbsolutePath());

    // initialize root dir
    File root = Main.getRootDir();
    log.info("Using Root folder: {}", root.getAbsolutePath());

    // initialize config profile dir
    File configProfile = Main.getConfigProfileDir();
    if (!root.equals(configProfile)) {
      log.info("Using Config profile folder: {}", configProfile.getAbsolutePath());
    }

    // verify war file and convert to URI
    validatePropertyFilePath(Constants.WAR_FILE_LOCATION, null);
    log.info("Using War file: {}", System.getProperty(Constants.WAR_FILE_LOCATION));

    // verify config file and convert to URI
    validatePropertyFilePath(Constants.CONFIG_FILE_PATH, Constants.DEFAULT_CONFIG_FILE_NAME);
    log.info("Using Config file: {}", System.getProperty(Constants.CONFIG_FILE_PATH));

    // verify properties file and convert to URI
    try {
      validatePropertyFilePathOptional(Constants.PROPERTIES_FILE_PATH,
          Constants.DEFAULT_PROPERTIES_FILE_NAME);
      log.info("Using Properties file: {}", System.getProperty(Constants.PROPERTIES_FILE_PATH));
    } catch (FileNotFoundException e) {
      System.clearProperty(Constants.PROPERTIES_FILE_PATH);
      log.info(e.getMessage());
      log.info("Properties file not found. Using provided system properties instead.");
    }
  }

  protected void validatePropertyFilePath(String propertyName, String defaultValue)
      throws WsInitException {
    try {
      validatePropertyFilePathOptional(propertyName, defaultValue);
    } catch (FileNotFoundException e) {
      throw new WsInitException("Invalid system property " + propertyName + ": " + e.getMessage());
    }
  }

  protected void validatePropertyFilePathOptional(String propertyName, String defaultValue)
      throws FileNotFoundException {
    String supplied = System.getProperty(propertyName, defaultValue);
    String configFilePath;

    try {
      configFilePath = CommonUtil.getValidURI(supplied);
    } catch (FileNotFoundException notFound) {
      // Fall back to the legacy file name, so that an installation created before the project
      // was renamed keeps working without any action from its administrator.
      String counterpart = ConfigFileResolver.counterpartPathOf(supplied);
      if (counterpart == null) {
        throw notFound;
      }
      try {
        configFilePath = CommonUtil.getValidURI(counterpart);
        log.debug("{} not found; using {} instead.", supplied, counterpart);
      } catch (FileNotFoundException alsoMissing) {
        // Neither exists. Report the current name, since that is the one to create.
        throw notFound;
      }
    }

    System.setProperty(propertyName, configFilePath);
  }

}
