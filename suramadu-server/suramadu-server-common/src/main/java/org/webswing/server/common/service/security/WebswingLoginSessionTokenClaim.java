package org.webswing.server.common.service.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebswingLoginSessionTokenClaim {

  private Map<String, Object> attributes = new ConcurrentHashMap<>();

  public Map<String, Object> getAttributes() {
    return attributes;
  }

  public void setAttributes(Map<String, Object> attributes) {
    this.attributes = attributes;
  }

}
