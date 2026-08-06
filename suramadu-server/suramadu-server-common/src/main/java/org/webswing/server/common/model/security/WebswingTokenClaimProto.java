package org.webswing.server.common.model.security;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.server.common.service.security.AbstractWebswingUser;
import org.webswing.server.common.service.security.WebswingTokenClaim;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

public class WebswingTokenClaimProto implements Serializable {

  private static final Logger log = LoggerFactory.getLogger(WebswingTokenClaimProto.class);

  @Serial
  private static final long serialVersionUID = 5715578968019749866L;

  private List<AbstractWebswingUserProto> userMap;
  private List<MapProto> attributes;
  private String host;

  public WebswingTokenClaimProto() {}

  public WebswingTokenClaimProto(WebswingTokenClaim tokenClaim) {
    super();
    this.host = tokenClaim.getHost();

    if (tokenClaim.getUserMap() != null) {
      this.userMap = new ArrayList<>();

      for (Entry<String, AbstractWebswingUser> entry : tokenClaim.getUserMap().entrySet()) {
        userMap.add(new AbstractWebswingUserProto(entry.getKey(), entry.getValue()));
      }
    }

    if (tokenClaim.getAttributes() != null) {
      JsonMapper mapper = new JsonMapper();

      this.attributes = new ArrayList<>();
      for (Entry<String, Object> entry : tokenClaim.getAttributes().entrySet()) {
        try {
          attributes.add(new MapProto(entry.getKey(), mapper.writeValueAsBytes(entry.getValue())));
        } catch (JacksonException e) {
          log.error("Could not serialize user attribute [" + entry.getKey() + "]!", e);
        }
      }
    }
  }

  public List<AbstractWebswingUserProto> getUserMap() {
    return userMap;
  }

  public void setUserMap(List<AbstractWebswingUserProto> userMap) {
    this.userMap = userMap;
  }

  public List<MapProto> getAttributes() {
    return attributes;
  }

  public void setAttributes(List<MapProto> attributes) {
    this.attributes = attributes;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

}
