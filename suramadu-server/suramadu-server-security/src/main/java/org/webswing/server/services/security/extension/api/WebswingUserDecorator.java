package org.webswing.server.services.security.extension.api;

import org.webswing.server.common.service.security.AuthenticatedWebswingUser;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class WebswingUserDecorator extends AuthenticatedWebswingUser {

  @Serial
  private static final long serialVersionUID = 5175691012719235120L;

  private final AuthenticatedWebswingUser user;
  private List<String> permissions;

  public WebswingUserDecorator(AuthenticatedWebswingUser user) {
    this.user = user;
  }

  @Override
  public String getUserId() {
    return user.getUserId();
  }

  @Override
  public List<String> getUserRoles() {
    return user.getUserRoles();
  }

  @Override
  public List<String> getUserPermissions() {
    if (permissions == null) {
      permissions = resolvePermissions();
    }
    return permissions;
  }

  @Override
  public Map<String, Serializable> getUserAttributes() {
    return user.getUserAttributes();
  }

  @Override
  public Map<String, Serializable> getUserSessionAttributes() {
    return user.getUserSessionAttributes();
  }

  @Override
  public boolean hasRole(String role) {
    return user.hasRole(role);
  }

  @Override
  public boolean isPermitted(String permission) {
    return user.isPermitted(permission);
  }

}
