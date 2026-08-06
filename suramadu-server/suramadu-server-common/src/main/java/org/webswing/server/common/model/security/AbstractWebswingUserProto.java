package org.webswing.server.common.model.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.server.common.service.security.AbstractWebswingUser;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

public class AbstractWebswingUserProto implements Serializable {

  private static final Logger log = LoggerFactory.getLogger(AbstractWebswingUserProto.class);

  private static final long serialVersionUID = -5468963503312366844L;

  private String securedPath;

  private String userId;
  private List<String> roles;
  private List<String> permissions;
  private List<WebswingAction> webswingActionPermissions;
  private List<MapProto> userAttributes;

  public AbstractWebswingUserProto() {}

  public AbstractWebswingUserProto(String securedPath, AbstractWebswingUser user) {
    super();
    this.securedPath = securedPath;
    this.userId = user.getUserId();
    this.roles = user.getRoles();

    if (user.getUserAttributes() != null) {
      JsonMapper mapper = new JsonMapper();

      this.userAttributes = new ArrayList<>();
      for (Entry<String, Serializable> entry : user.getUserAttributes().entrySet()) {
        try {
          userAttributes
              .add(new MapProto(entry.getKey(), mapper.writeValueAsBytes(entry.getValue())));
        } catch (JacksonException e) {
          log.error("Could not serialize user attribute [" + entry.getKey() + "]!", e);
        }
      }
    }

    if (user.getPermissions() != null) {
      this.webswingActionPermissions = new ArrayList<>();
      this.permissions = new ArrayList<>();

      for (String permission : user.getPermissions()) {
        try {
          WebswingAction wa = WebswingAction.valueOf(permission);
          webswingActionPermissions.add(wa);
        } catch (Exception e) {
          // ignore and store as external permission
          permissions.add(permission);
        }
      }
    }
  }

  public String getSecuredPath() {
    return securedPath;
  }

  public void setSecuredPath(String securedPath) {
    this.securedPath = securedPath;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public List<String> getRoles() {
    return roles;
  }

  public void setRoles(List<String> roles) {
    this.roles = roles;
  }

  public List<String> getPermissions() {
    return permissions;
  }

  public void setPermissions(List<String> permissions) {
    this.permissions = permissions;
  }

  public List<MapProto> getUserAttributes() {
    return userAttributes;
  }

  public void setUserAttributes(List<MapProto> userAttributes) {
    this.userAttributes = userAttributes;
  }

  public List<WebswingAction> getWebswingActionPermissions() {
    return webswingActionPermissions;
  }

  public void setWebswingActionPermissions(List<WebswingAction> webswingActionPermissions) {
    this.webswingActionPermissions = webswingActionPermissions;
  }

}
