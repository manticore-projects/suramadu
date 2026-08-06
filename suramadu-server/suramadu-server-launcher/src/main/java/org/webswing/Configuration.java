package org.webswing;

import java.io.File;

public abstract class Configuration {

  private static final Configuration singleton = new ConfigurationImpl();

  public abstract String getHost();

  public abstract boolean isHttp();

  public abstract String getHttpPort();

  public abstract boolean isHttps();

  public abstract String getHttpsPort();

  public abstract String getTruststore();

  public abstract String getTruststorePassword();

  public abstract String getKeystore();

  public abstract String getKeystorePassword();

  public abstract boolean isClientAuthEnabled();

  public abstract String getConfigFile();

  public abstract String getPropertiesFile();

  public abstract File resolveConfigFile(String filename);

  public abstract String getContextPath();

  public abstract String getServerId();

  public static Configuration getInstance() {
    return singleton;
  }
}
