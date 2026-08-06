package org.webswing.server.common.service.url;

import java.util.Set;

public class WebSocketUrlLoaderEvent {

  private Set<String> urls;

  public WebSocketUrlLoaderEvent(Set<String> urls) {
    super();
    this.urls = urls;
  }

  public Set<String> getUrls() {
    return urls;
  }

  public void setUrls(Set<String> urls) {
    this.urls = urls;
  }

}
