package org.webswing.server.services.security.extension.accessmapping;

import org.apache.commons.lang3.StringUtils;
import org.webswing.server.common.model.security.WebswingAction;
import org.webswing.server.common.service.security.AuthenticatedWebswingUser;
import org.webswing.server.services.security.extension.api.SecurityModuleExtension;
import org.webswing.server.services.security.extension.api.WebswingUserDecorator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AccessMappingSecurityExtension
    extends SecurityModuleExtension<AccessMappingExtensionConfig> {

  private final Map<WebswingAction.AccessType, AccessTypeMapping> accessMapping = new HashMap<>();

  public AccessMappingSecurityExtension(AccessMappingExtensionConfig config) {
    super(config);
    for (AccessTypeMapping mapping : getConfig().getAccessMapping()) {
      accessMapping.put(mapping.getAccessType(), mapping);
    }
  }

  @Override
  public AuthenticatedWebswingUser decorateUser(AuthenticatedWebswingUser user,
      HttpServletRequest request, HttpServletResponse response) {
    return new WebswingUserDecorator(user) {
      private static final long serialVersionUID = -3360930503393350015L;

      @Override
      public boolean isPermitted(String permission) {
        WebswingAction.AccessType[] validAccessTypes;
        try {
          validAccessTypes = WebswingAction.valueOf(permission).getAccessTypes();
        } catch (IllegalArgumentException e) {
          validAccessTypes = new WebswingAction.AccessType[] {WebswingAction.AccessType.admin};
        }
        for (WebswingAction.AccessType at : validAccessTypes) {
          AccessTypeMapping m = accessMapping.get(at);
          if (m != null) {
            // check if valid for everyone
            if (m.isEveryone()) {
              return true;
            }

            // check username valid
            for (String username : m.getUsers()) {
              if (StringUtils.equals(user.getUserId(),
                  getConfig().getContext().replaceVariables(username))) {
                return true;
              }
            }
            // check role valid
            for (String role : m.getRoles()) {
              if (user.hasRole(getConfig().getContext().replaceVariables(role))) {
                return true;
              }
            }
          } else {
            Set<String> defaultRoles =
                WebswingAction.DefaultRolePermissionResolver.getRolesForAccessType(at);
            // check role valid
            for (String role : defaultRoles) {
              if (user.hasRole(role)) {
                return true;
              }
            }
          }
        }
        return false;
      }
    };
  }
}
