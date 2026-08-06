package org.webswing.server.services.security.modules.shiro;

import org.webswing.server.common.service.security.AuthenticatedWebswingUser;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ShiroWebswingUser extends AuthenticatedWebswingUser {
  @Serial
  private static final long serialVersionUID = -2997239323873658547L;

  String user;
  String password;
  List<String> roles;

  public ShiroWebswingUser(String user, String password, List<String> roles) {
    super();
    this.user = user;
    this.password = password;
    this.roles = roles;
  }

  @Override
  public String getUserId() {
    return user;
  }

  @Override
  public List<String> getUserRoles() {
    return roles;
  }

  @Override
  public Map<String, Serializable> getUserAttributes() {
    return Collections.emptyMap();
  }

  @Override
  public Map<String, Serializable> getUserSessionAttributes() {
    return Collections.emptyMap();
  }

  @Override
  public boolean hasRole(String role) {
    return roles.contains(role);
  }

  public String getPassword() {
    return password;
  }
}
