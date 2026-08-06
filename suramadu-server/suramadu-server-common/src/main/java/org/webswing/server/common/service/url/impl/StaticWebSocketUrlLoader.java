package org.webswing.server.common.service.url.impl;

import com.google.common.base.Splitter;
import org.webswing.Constants;
import org.webswing.server.common.service.url.WebSocketUrlLoader;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class StaticWebSocketUrlLoader implements WebSocketUrlLoader {

  private final Set<String> webSocketUrls = Collections.synchronizedSet(new HashSet<>());

  @Override
  public Set<String> reload() {
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

}
