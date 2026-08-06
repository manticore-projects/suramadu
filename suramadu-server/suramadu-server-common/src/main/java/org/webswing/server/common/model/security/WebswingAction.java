package org.webswing.server.common.model.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.server.common.service.security.AuthenticatedWebswingUser;
import org.webswing.server.common.service.security.RolePermissionResolver;

import java.io.Serial;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Actions defined within Webswing server. Actions are grouped in AccessTypes. AccessTypes can be
 * mapped to Roles. These mappings can be customized by implementing {@link RolePermissionResolver}
 * interface.
 */
public enum WebswingAction {

  master_admin_access(AccessType.admin), master_support_access(AccessType.support,
      AccessType.admin), master_basic_access(AccessType.basic, AccessType.support,
          AccessType.admin),
  // ADMIN CONSOLE
  rest_getPaths(AccessType.admin, AccessType.support), rest_getAppInfo(AccessType.admin,
      AccessType.support), rest_getSession(AccessType.admin, AccessType.support), rest_getStats(
          AccessType.admin, AccessType.support), rest_startRecording(AccessType.admin,
              AccessType.support), rest_sessionShutdown(AccessType.admin,
                  AccessType.support), rest_sessionShutdownForce(AccessType.admin,
                      AccessType.support), rest_getConfig(AccessType.admin,
                          AccessType.support), rest_setConfig(AccessType.admin), rest_startApp(
                              AccessType.admin), rest_stopApp(AccessType.admin), rest_createApp(
                                  AccessType.admin), rest_removeApp(
                                      AccessType.admin), rest_controlSessionPool(
                                          AccessType.admin), rest_getThreadDump(AccessType.admin,
                                              AccessType.support), rest_requestThreadDump(
                                                  AccessType.admin,
                                                  AccessType.support), rest_toggleStatisticsLogging(
                                                      AccessType.admin,
                                                      AccessType.support), rest_getOverview(
                                                          AccessType.admin,
                                                          AccessType.support), rest_viewLogs(
                                                              AccessType.admin,
                                                              AccessType.support), rest_getApps(
                                                                  AccessType.admin,
                                                                  AccessType.support,
                                                                  AccessType.basic),
  // Security
  rest_getOneTimePassword(AccessType.admin, AccessType.support),
  // websocket
  websocket_connect(AccessType.admin, AccessType.support,
      AccessType.basic), websocket_startRecordingPlayback(AccessType.admin,
          AccessType.support), websocket_startSwingApplication(AccessType.admin, AccessType.support,
              AccessType.basic), websocket_startMirrorView(AccessType.admin, AccessType.support),
  // file handler
  file_download(AccessType.admin, AccessType.support,
      AccessType.basic), file_upload(AccessType.admin, AccessType.support, AccessType.basic);

  private final AccessType[] accessTypes;

  WebswingAction(AccessType... accessTypes) {

    this.accessTypes = accessTypes;
  }

  public AccessType[] getAccessTypes() {
    return accessTypes;
  }

  public enum AccessType {
    basic, support, admin
  }

  public static class DefaultRolePermissionResolver implements RolePermissionResolver {

    @Serial
    private static final long serialVersionUID = 6867717587467658245L;

    private static final Logger log = LoggerFactory.getLogger(DefaultRolePermissionResolver.class);

    @Override
    public String[] getRolesForPermission(String action) {
      Set<String> roles = new HashSet<>();
      try {
        for (AccessType at : WebswingAction.valueOf(action).accessTypes) {
          roles.addAll(getRolesForAccessType(at));
        }
      } catch (Exception e) {
        log.error("Error resolving roles for action '{}' falling back to admin", action);
        roles = getRolesForAccessType(AccessType.admin);
      }
      return roles.toArray(new String[roles.size()]);
    }

    public static Set<String> getRolesForAccessType(AccessType accessType) {
      switch (accessType) {
        case admin:
          return Collections.singleton(AccessType.admin.name());
        case support:
          return Collections.singleton(AccessType.support.name());
        case basic:
          return Collections.singleton(AuthenticatedWebswingUser.ROLE_AUTHENTICATED);
        default:
          return Collections.emptySet();
      }
    }
  }
}
