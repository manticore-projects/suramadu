package org.webswing.server.common.service.security;

import org.webswing.toolkit.api.security.WebswingUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class AbstractWebswingUser implements WebswingUser {

  private String userId;
  private List<String> roles;
  private List<String> permissions;
  private Map<String, Serializable> userAttributes;

  public AbstractWebswingUser() {}

  public AbstractWebswingUser(AuthenticatedWebswingUser user) {
    this.userId = user.getUserId();
    this.roles = new ArrayList<>(user.getUserRoles());
    this.permissions = new ArrayList<>(user.getUserPermissions());
    this.userAttributes = new HashMap<>(user.getUserSessionAttributes());
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

  public boolean isPermitted(String permission) {
    return permissions != null && permissions.contains(permission);
  }

  public boolean hasRole(String role) {
    return roles != null && roles.contains(role);
  }

  public Map<String, Serializable> getUserAttributes() {
    return userAttributes;
  }

  public void setUserAttributes(Map<String, Serializable> userAttributes) {
    this.userAttributes = userAttributes;
  }

  @Override
  public String toString() {
    return userId;
  }

}
