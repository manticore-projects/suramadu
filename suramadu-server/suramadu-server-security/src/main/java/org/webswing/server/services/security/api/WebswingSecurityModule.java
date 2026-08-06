package org.webswing.server.services.security.api;

import org.webswing.server.common.service.security.AbstractWebswingUser;
import org.webswing.server.common.service.security.AuthenticatedWebswingUser;
import org.webswing.server.services.security.modules.AbstractExtendableSecurityModule;
import org.webswing.server.services.security.modules.AbstractSecurityModule;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * Main interface used by Webswing server responsible for user authentication. Implementations
 * handle the full authentication process from serving the login page, parsing credentials,
 * validation of credentials to creation of {@link AuthenticatedWebswingUser} instance. It is
 * recommended to extend {@link AbstractSecurityModule} or {@link AbstractExtendableSecurityModule}
 * instead of implementing this interface directly.
 * </p>
 * There is always exactly one instance of {@link WebswingSecurityModule} per swing application
 * handling all login requests therefore the implementations must be thread safe.
 */
public interface WebswingSecurityModule {

  /**
   * Initialization method. Always invoked only once right after creation. In case of error during
   * initialization, this method should throw a {@link RuntimeException}.
   */
  void init();

  /**
   * Responsible for full authentication process. This method is triggered by Webswing when a HTTP
   * request is received on URL <blockquote>/{app_path}/login</blockquote> and no valid session is
   * exists. <br>
   * Implementations are expected to handle following scenarios:
   * <ul>
   * <li><b>HTTP Request has no credential.</b> - responds login page with status 401 (UNAUTHORIZED)
   * and returns null.</li>
   * <li><b>HTTP Request has invalid credentials.</b> - responds with login page displaying error
   * message with status 401 (UNAUTHORIZED) and returns null.</li>
   * <li><b>HTTP Request has valid credentials.</b> - responds with status 200 (OK) and returns
   * instance of {@link AuthenticatedWebswingUser}.</li>
   * </ul>
   *
   * @param request The HTTP request from servlet container
   * @param response The HTTP response from servlet container
   * @param securedPath secured path of this login request
   * @return instance of user or null if user is not resolved
   * @throws ServletException thrown when sending the http response fails
   * @throws IOException thrown when sending the http response fails
   */
  AuthenticatedWebswingUser doLogin(HttpServletRequest request, HttpServletResponse response,
      String securedPath) throws ServletException, IOException;

  /**
   * This method is triggered by Webswing when a HTTP request is received on URL
   * <blockquote>/{app_path}/logout</blockquote> after the current user session is invalidated. <br>
   *
   * @param req The HTTP request from servlet container
   * @param res The HTTP response from servlet container
   * @param user The user which just logged out
   * @throws ServletException thrown when sending the http response fails
   * @throws IOException thrown when sending the http response fails
   */
  void doLogout(HttpServletRequest req, HttpServletResponse res, AbstractWebswingUser user)
      throws ServletException, IOException;

  /**
   * This method is triggered by Webswing when a HTTP request is received on URL
   * <blockquote>/{app_path}/login</blockquote> and user is authenticated. It is expected to return
   * status 200 with response header 'webswingUsername' with value stating current user name, if the
   * path is empty. For non-empty path the behavior is defined by the security module
   * implementation.
   * 
   * @param user current logged in user
   * @param path path after '/login' (ie. if request path is '/app/login/custom', path will be
   *        '/custom')
   * @param request The HTTP request from servlet container
   * @param response The HTTP response from servlet container
   */
  void doServeAuthenticated(AbstractWebswingUser user, String path, HttpServletRequest request,
      HttpServletResponse response) throws IOException;

  /**
   * Tear down of this module. Invoked when Webserver is shutting down or application is being
   * reloaded/uninstalled.
   */
  void destroy();

}
