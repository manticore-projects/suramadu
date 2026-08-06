package org.webswing.server.common.service.security;

import java.io.Serializable;

/**
 * Provides a logic to map Roles to Permissions for
 * {@link AuthenticatedWebswingUser#isPermitted(String)}'s default implementation.
 */
public interface RolePermissionResolver extends Serializable {
  /**
   * @param permission permission name
   * @return All roles the permission is allowed for
   */
  String[] getRolesForPermission(String permission);
}
