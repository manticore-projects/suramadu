package org.webswing;

import org.apache.commons.cli.*;
import org.webswing.util.AppLogger;
import org.webswing.util.GitRepositoryState;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationImpl extends Configuration {

  private static final String PREFIX =
      System.getProperty(Constants.BRANDING_PREFIX, "org.webswing");

  private String host = "localhost";

  private boolean http = true;
  private String httpPort = "8080";

  private boolean https;
  private String httpsPort = "8443";
  private String truststore;
  private String truststorePassword;
  private String keystore;
  private String keystorePassword;
  private boolean clientAuthEnabled;

  private String configFile;
  private String propFile;

  private String contextPath = "/";
  private String serverId;

  public static Configuration parse(String[] args) {
    ConfigurationImpl cimpl = (ConfigurationImpl) Configuration.getInstance();
    // create the command line parser
    CommandLineParser parser = new PosixParser();

    // create the Options
    Options options = new Options();
    options.addOption("h", "host", true,
        "Local interface address where the web server will listen. (localhost)");
    options.addOption("p", "port", true,
        "Http port where the web server will listen. If 0 http is disabled. (8080)");
    options.addOption("ctx", "contextpath", true, "Context path where Webswing is deployed.(/)");
    options.addOption("id", "id", true, "Server id.(Random uuid if not specified)");

    options.addOption("s", "sslport", true,
        "Https port where the web server will listen. If 0 https is disabled. (0)");
    options.addOption("ts", "truststore", true, "Truststore file location for ssl configuration ");
    options.addOption("tp", "truststorepwd", true, "Truststore password");
    options.addOption("ks", "keystore", true, "Keystore file location for ssl configuration");
    options.addOption("kp", "keystorepwd", true, "Keystore password");

    options.addOption("t", "temp", true,
        "The folder where temp folder will be created for the server. (./tmp)");
    options.addOption("tc", "tempclean", true, "Delete the content of temp folder. (true)");
    options.addOption("d", true, "Create new temp folder for every instance (false)");

    options.addOption("j", "jetty", true, "Jetty startup configuration file. (./jetty.properties)");
    options.addOption("c", "config", true,
        "Configuration file name. (<suramadu-server.war path>/webswing.config)");
    options.addOption("pf", "propertiesfile", true,
        "Properties file name. (<suramadu-server.war path>/webswing.properties)");

    try {
      // parse the command line arguments
      CommandLine line = parser.parse(options, args);
      // read jetty.properties config file
      if (line.getOptionValue('j') != null) {
        cimpl.readPropertyFile(line.getOptionValue('j'));
      }

      // override configuration
      if (line.getOptionValue('h') != null) {
        cimpl.setHost(line.getOptionValue('h'));
      }

      if (line.getOptionValue('p') != null) {
        String value = line.getOptionValue('p');
        cimpl.setHttp("0".equals(value) ? false : true);
        cimpl.setHttpPort(value);
      }

      if (line.getOptionValue("ctx") != null) {
        String value = line.getOptionValue("ctx");
        cimpl.setContextPath(value);
      }

      if (line.getOptionValue("id") != null) {
        String value = line.getOptionValue("id");
        cimpl.setServerId(value);
      }

      if (line.getOptionValue('s') != null) {
        String value = line.getOptionValue('s');
        cimpl.setHttps("0".equals(value) ? false : true);
        cimpl.setHttpsPort(value);
      }

      if (line.getOptionValue("ts") != null) {
        String value = line.getOptionValue("ts");
        cimpl.setTruststore(value);
      }

      if (line.getOptionValue("tp") != null) {
        String value = line.getOptionValue("tp");
        cimpl.setTruststorePassword(value);
      }

      if (line.getOptionValue("ks") != null) {
        String value = line.getOptionValue("ks");
        cimpl.setKeystore(value);
      }

      if (line.getOptionValue("kp") != null) {
        String value = line.getOptionValue("kp");
        cimpl.setKeystorePassword(value);
      }

      if (line.getOptionValue('c') != null) {
        cimpl.setConfigFile(line.getOptionValue('c'));
      }

      if (line.getOptionValue("pf") != null) {
        cimpl.setPropertiesFile(line.getOptionValue("pf"));
      }

      // NOTE: -d, -t and -tc are parsed in main.Main
    } catch (ParseException exp) {
      AppLogger.debug(exp.getMessage());
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp(PREFIX, options);
    } catch (IOException e) {
      AppLogger.error("Server configuration failed.", e);
    }
    return cimpl;
  }

  private void readPropertyFile(String filename) throws IOException {
    Properties prop = new Properties();

    File file = resolveConfigFile(filename);
    InputStream inputStream = new FileInputStream(file);
    prop.load(inputStream);
    setHost(prop.getProperty(PREFIX + ".server.host"));

    setHttp(Boolean.parseBoolean(prop.getProperty(PREFIX + ".server.http", "true")));
    setHttpPort(prop.getProperty(PREFIX + ".server.http.port"));

    setHttps(Boolean.parseBoolean(prop.getProperty(PREFIX + ".server.https", "true")));
    setHttpsPort(prop.getProperty(PREFIX + ".server.https.port"));
    setTruststore(prop.getProperty(PREFIX + ".server.https.truststore"));
    setTruststorePassword(prop.getProperty(PREFIX + ".server.https.truststore.password"));
    setKeystore(prop.getProperty(PREFIX + ".server.https.keystore"));
    setKeystorePassword(prop.getProperty(PREFIX + ".server.https.keystore.password"));

    setClientAuthEnabled(Boolean
        .parseBoolean(prop.getProperty(PREFIX + ".server.https.clientAuthEnabled", "false")));
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public boolean isHttp() {
    return http;
  }

  public void setHttp(boolean http) {
    this.http = http;
  }

  public String getHttpPort() {
    return httpPort;
  }

  public void setHttpPort(String httpPort) {
    this.httpPort = httpPort;
  }

  public boolean isHttps() {
    return https;
  }

  public void setHttps(boolean https) {
    this.https = https;
  }

  public String getHttpsPort() {
    return httpsPort;
  }

  public void setHttpsPort(String httpsPort) {
    this.httpsPort = httpsPort;
  }

  public String getTruststore() {
    return truststore;
  }

  public void setTruststore(String truststore) {
    this.truststore = truststore;
  }

  public String getTruststorePassword() {
    return truststorePassword;
  }

  public void setTruststorePassword(String truststorePassword) {
    this.truststorePassword = truststorePassword;
  }

  public String getKeystorePassword() {
    return keystorePassword;
  }

  public void setKeystorePassword(String keystorePassword) {
    this.keystorePassword = keystorePassword;
  }

  public String getKeystore() {
    return keystore;
  }

  public void setKeystore(String keystore) {
    this.keystore = keystore;
  }

  public String getConfigFile() {
    return configFile;
  }

  @Override
  public String getPropertiesFile() {
    return propFile;
  }

  @Override
  public String getContextPath() {
    return contextPath;
  }

  public void setContextPath(String contextPath) {
    this.contextPath = contextPath;
  }

  public void setConfigFile(String configFile) {
    this.configFile = configFile;
  }

  public void setPropertiesFile(String propFile) {
    this.propFile = propFile;
  }

  @Override
  public String toString() {
    return "########################Server Configuration ################################\n"
        + " host=" + host + "\n http=" + http + "\n httpPort=" + httpPort + "\n https=" + https
        + "\n httpsPort=" + httpsPort + "\n truststore=" + truststore + "\n truststorePassword=***"
        + "\n keystore=" + keystore + "\n keystorePassword=***" + "\n clientAuthEnabled="
        + clientAuthEnabled + "\n configFile=" + configFile + "\n propertiesFile=" + propFile
        + "\n contextPath=" + contextPath + "\n serverId=" + serverId + "\n version="
        + GitRepositoryState.getInstance().getDescribe()
        + "\n########################Server Configuration End#############################\n";
  }

  public boolean isClientAuthEnabled() {
    return clientAuthEnabled;
  }

  public void setClientAuthEnabled(boolean clientAuthEnabled) {
    this.clientAuthEnabled = clientAuthEnabled;
  }

  public String getServerId() {
    return serverId;
  }

  public void setServerId(String serverId) {
    this.serverId = serverId;
  }

  @Override
  public File resolveConfigFile(String filename) {
    File file = new File(filename);
    if (!(file.isAbsolute() && file.exists())) {
      file = new File(System.getProperty(Constants.CONFIG_PATH), filename);
    }
    return file;
  }

}
