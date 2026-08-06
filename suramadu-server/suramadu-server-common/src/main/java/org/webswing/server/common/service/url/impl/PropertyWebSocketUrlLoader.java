package org.webswing.server.common.service.url.impl;

import com.google.common.base.Splitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.Constants;
import org.webswing.server.common.service.url.WebSocketUrlLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class PropertyWebSocketUrlLoader implements WebSocketUrlLoader {

  private static final Logger log = LoggerFactory.getLogger(PropertyWebSocketUrlLoader.class);

  private final File propertiesFile;
  private final Set<String> webSocketUrls = Collections.synchronizedSet(new HashSet<>());

  public PropertyWebSocketUrlLoader(File propertiesFile) {
    this.propertiesFile = propertiesFile;
  }

  @Override
  public Set<String> reload() {
    reloadPropertyFile();

    synchronized (webSocketUrls) {
      webSocketUrls.clear();
      String urls = System.getProperty(Constants.SERVER_WEBSOCKET_URL, "");
      Splitter.on(',').trimResults().omitEmptyStrings().split(urls).forEach(url -> {
        if (url.endsWith("/")) {
          url = url.substring(0, url.length() - 1);
        }
        webSocketUrls.add(url);
      });
      return webSocketUrls;
    }
  }

  private void reloadPropertyFile() {
    if (propertiesFile == null) {
      return;
    }

    Properties p = new Properties(System.getProperties());
    try (InputStream propFileStream = new FileInputStream(propertiesFile)) {
      p.load(propFileStream);
    } catch (Exception e) {
      log.error("Could not realod properties file!", e);
    }

    System.setProperty(Constants.SERVER_WEBSOCKET_URL,
        p.getProperty(Constants.SERVER_WEBSOCKET_URL, ""));
  }

}
