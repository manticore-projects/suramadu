package org.webswing.server.services.security.modules.embeded;

import org.webswing.server.common.service.security.AuthenticatedWebswingUser;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class EmbededWebswingUser extends AuthenticatedWebswingUser {

  @Serial
  private static final long serialVersionUID = 4579855803170690448L;

  String user;
  String password;
  List<String> roles;

  public EmbededWebswingUser(String user, String password, List<String> roles) {
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
