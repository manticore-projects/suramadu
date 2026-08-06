package org.webswing.server.common.service.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebswingTokenClaim {

  private Map<String, AbstractWebswingUser> userMap = new ConcurrentHashMap<>();
  private Map<String, Object> attributes = new ConcurrentHashMap<>();
  private String host;

  public Map<String, AbstractWebswingUser> getUserMap() {
    return userMap;
  }

  public void setUserMap(Map<String, AbstractWebswingUser> userMap) {
    this.userMap = userMap;
  }

  public Map<String, Object> getAttributes() {
    return attributes;
  }

  public void setAttributes(Map<String, Object> attributes) {
    this.attributes = attributes;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

}
