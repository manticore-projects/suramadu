package org.webswing.server.common.service.security;

import org.webswing.server.common.model.security.WebswingAction;
import org.webswing.toolkit.api.security.WebswingUser;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Authenticated users in Webswing are represented by this class
 */
public abstract class AuthenticatedWebswingUser implements WebswingUser, Serializable {

  @Serial
  private static final long serialVersionUID = -9025181162519446299L;

  /**
   * Special role representing all authenticated users.
   */
  public static final String ROLE_AUTHENTICATED = "authenticated";

  private RolePermissionResolver resolver;

  private List<String> permissions;

  public AuthenticatedWebswingUser() {
    this(new WebswingAction.DefaultRolePermissionResolver());
  }

  /**
   * @param resolver provide logic for mapping from permissions to roles
   */
  public AuthenticatedWebswingUser(RolePermissionResolver resolver) {
    this.resolver = resolver;
  }

  public abstract String getUserId();

  public abstract List<String> getUserRoles();

  public abstract Map<String, Serializable> getUserAttributes();

  /**
   * Attributes that will be stored in user session. User session is stored in JWT token cookie.
   * Note that cookie has a limitation of 4096 characters.
   */
  public abstract Map<String, Serializable> getUserSessionAttributes();

  /**
   * @param role name or the role
   * @return true if user has the specified role. False otherwise.
   */
  public abstract boolean hasRole(String role);

  public List<String> getUserPermissions() {
    if (permissions == null) {
      permissions = resolvePermissions();
    }
    return permissions;
  }

  protected List<String> resolvePermissions() {
    List<String> permissions = new ArrayList<>();
    // initialize a default set of permissions based on WebswingActions
    for (WebswingAction action : WebswingAction.values()) {
      if (isPermitted(action.name())) {
        permissions.add(action.name());
      }
    }
    return permissions;
  }

  /**
   * Checks if current user has defined permission. Default implementation uses associated
   * {@link RolePermissionResolver} to check if user has any of the the roles assigned to defined
   * permission.
   * 
   * @param permission permission name
   * @return true if user has the permission.
   */
  public boolean isPermitted(String permission) {
    if (resolver != null) {
      String[] roles = resolver.getRolesForPermission(permission);
      if (roles != null) {
        for (String role : roles) {
          if (ROLE_AUTHENTICATED.equals(role) || hasRole(role)) {
            return true;
          }
        }
      }
    }
    return false;
  }

  @Override
  public String toString() {
    return getUserId();
  }
}
