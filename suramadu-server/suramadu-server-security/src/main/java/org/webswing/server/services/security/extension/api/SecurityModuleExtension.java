package org.webswing.server.services.security.extension.api;

import org.apache.commons.lang3.StringUtils;
import org.webswing.server.common.service.security.AbstractWebswingUser;
import org.webswing.server.common.service.security.AuthenticatedWebswingUser;
import org.webswing.server.services.security.api.LoginResponseClosedException;
import org.webswing.server.services.security.api.WebswingAuthenticationException;
import org.webswing.server.services.security.api.WebswingSecurityModule;
import org.webswing.server.services.security.modules.AbstractExtendableSecurityModule;
import org.webswing.server.services.security.modules.AbstractSecurityModule;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Security Extension prototype used in {@link AbstractExtendableSecurityModule}. Supports four
 * extension points:
 * 1.{@link #doSufficientPreValidation(AbstractExtendableSecurityModule, HttpServletRequest, HttpServletResponse)}
 * doSufficientPreValidation}
 * 2.{@link #doRequiredPreValidation(AbstractExtendableSecurityModule, HttpServletRequest, HttpServletResponse)}
 * doRequiredPreValidation}
 * 3.{@link #doRequiredPostValidation(AbstractExtendableSecurityModule, AuthenticatedWebswingUser, HttpServletRequest, HttpServletResponse)}
 * doRequiredPostValidation}
 * 4.{@link #decorateUser(AuthenticatedWebswingUser, HttpServletRequest, HttpServletResponse)
 * decorateUser}
 * 
 * @param <T> Interface for reading configuration settings from JSON
 */
public abstract class SecurityModuleExtension<T extends SecurityModuleExtensionConfig> {

  private final T config;

  public SecurityModuleExtension(T config) {
    this.config = config;
  }

  public T getConfig() {
    return config;
  }

  /**
   * Perform a sufficient credentials validation before the Security modules's
   * {@link WebswingSecurityModule#doLogin(HttpServletRequest, HttpServletResponse, String) doLogin}
   * method is invoked. If valid user is returned, no further validation or processing is carried
   * out. If extension sends a response, it have to throw {@link LoginResponseClosedException} to
   * stop further processing.
   * 
   * @param m security module being extended
   * @param request login request
   * @param response login response
   * @return valid user or null (no credentials provided)
   * @throws LoginResponseClosedException throw this exception if response have been sent by this
   *         extension.
   * @throws WebswingAuthenticationException if the credentials are not valid
   */
  public AuthenticatedWebswingUser doSufficientPreValidation(AbstractExtendableSecurityModule<?> m,
      HttpServletRequest request, HttpServletResponse response)
      throws LoginResponseClosedException, WebswingAuthenticationException {
    return null;
  }

  /**
   * Performs a required pre-validation. If validation fails WebswingAuthenticationException should
   * be thrown. If extension sends a response, it have to throw {@link LoginResponseClosedException}
   * to stop further processing.
   * 
   * @param m security module being extended
   * @param request login request
   * @param response login response
   * @throws LoginResponseClosedException throw this exception if response have been sent by this
   *         extension.
   * @throws WebswingAuthenticationException if login request is not valid
   */
  public void doRequiredPreValidation(AbstractExtendableSecurityModule<?> m,
      HttpServletRequest request, HttpServletResponse response)
      throws LoginResponseClosedException, WebswingAuthenticationException {}

  /**
   * Performs a required pre-validation. If validation fails WebswingAuthenticationException should
   * be thrown. If extension sends a response, it have to throw {@link LoginResponseClosedException}
   * to stop further processing.
   * 
   * @param m security module being extended
   * @param user user authenticated by security module
   * @param request login request
   * @param response login response
   * @throws LoginResponseClosedException throw this exception if response have been sent by this
   *         extension.
   * @throws WebswingAuthenticationException if login request is not valid
   */
  public void doRequiredPostValidation(AbstractExtendableSecurityModule<?> m,
      AuthenticatedWebswingUser user, HttpServletRequest request, HttpServletResponse response)
      throws LoginResponseClosedException, WebswingAuthenticationException {}

  /**
   * Decorate user. Implementations can use {@link WebswingUserDecorator}.
   * 
   * @param user authenticated user
   * @param request login request
   * @param response login response
   * @return decorated user
   */
  public AuthenticatedWebswingUser decorateUser(AuthenticatedWebswingUser user,
      HttpServletRequest request, HttpServletResponse response) {
    return user;
  }

  /**
   * Serve request made after successful authentication. This can be used for additional services,
   * like password reset, profile page or other functionality.
   * 
   * @param user current logged in user
   * @param path path after '/login' (ie. if request path is '/app/login/custom', path will be
   *        '/custom')
   * @param req The HTTP request from servlet container
   * @param res The HTTP response from servlet container
   * @return true if request was served.
   */
  public boolean serveAuthenticated(AbstractWebswingUser user, String path, HttpServletRequest req,
      HttpServletResponse res) {
    return false;
  }

  public void logSuccess(HttpServletRequest r, String user) {
    String path = getConfig().getContext().getSecuredPath();
    path = StringUtils.isEmpty(path) ? "/" : path;
    String module = this.getClass().getName();
    AbstractSecurityModule.auditLog("SUCCESS", r, path, module, user, "");
  }

  public void logFailure(HttpServletRequest r, String user, String reason) {
    String path = getConfig().getContext().getSecuredPath();
    path = StringUtils.isEmpty(path) ? "/" : path;
    String module = this.getClass().getName();
    AbstractSecurityModule.auditLog("FAILED", r, path, module, user, reason);
  }
}
