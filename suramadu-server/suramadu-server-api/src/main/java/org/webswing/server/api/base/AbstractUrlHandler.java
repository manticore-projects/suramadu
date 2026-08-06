package org.webswing.server.api.base;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.Constants;
import org.webswing.server.api.services.security.login.SecuredPathHandler;
import org.webswing.server.api.util.SecurityUtil;
import org.webswing.server.api.util.ServerApiUtil;
import org.webswing.server.common.model.security.WebswingAction;
import org.webswing.server.common.service.security.AbstractWebswingUser;
import org.webswing.server.common.service.security.SecurableService;
import org.webswing.server.common.util.CommonUtil;
import org.webswing.server.model.exception.WsException;

import org.webswing.server.common.model.SecuredPathConfig;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public abstract class AbstractUrlHandler implements UrlHandler, SecurableService {
  private static final Logger log = LoggerFactory.getLogger(AbstractUrlHandler.class);

  /**
   * Matches CR (\r) and LF (\n) characters, including URL-encoded variants (%0d / %0a,
   * case-insensitive). Used to:
   * <ol>
   * <li>Reject Origin header values that would otherwise allow CRLF injection into response headers
   * (early-return guard).</li>
   * <li>Strip any residual CR/LF at the point of use (defense-in-depth sanitization that satisfies
   * taint-tracking static analyzers).</li>
   * </ol>
   */
  private static final Pattern CRLF_PATTERN =
      Pattern.compile("[\r\n]|%0[da]", Pattern.CASE_INSENSITIVE);

  private final UrlHandler parent;
  private final List<UrlHandler> childHandlers =
      Collections.synchronizedList(new LinkedList<UrlHandler>());

  public AbstractUrlHandler(UrlHandler parent) {
    this.parent = parent;
  }

  /**
   * Returns the {@link SecuredPathConfig} that governs this handler, or {@code null} if none is
   * available at this level of the hierarchy.
   *
   * <p>
   * The default implementation returns {@code null}. Subclasses that own or have access to a
   * {@code SecuredPathConfig} (e.g. the root secured-path handler) should override this method to
   * return it. {@link #isOriginAllowed(String)} delegates here so that CORS decisions are always
   * driven by the single authoritative config rather than a parallel in-memory set.
   *
   * @return the secured-path config, or {@code null}
   */
  protected SecuredPathConfig getSecuredPathConfig() {
    return null;
  }

  /**
   * Strips CR, LF, and their URL-encoded forms (%0d / %0a) from a header value.
   *
   * <p>
   * This is a defense-in-depth measure applied at every point where user-supplied data is written
   * into a response header. It complements the early-return CRLF guard and ensures that
   * taint-tracking static analyzers (Semgrep, SpotBugs, SonarQube, etc.) can verify the value is
   * clean at the sink, regardless of control-flow assumptions.
   *
   * @param value the raw header value; may be {@code null}
   * @return the sanitized value, or {@code null} if the input was {@code null}
   */
  private static String sanitizeHeaderValue(String value) {
    return value == null ? null : CRLF_PATTERN.matcher(value).replaceAll("");
  }

  @Override
  public void init() {
    synchronized (childHandlers) {
      for (UrlHandler handler : childHandlers) {
        try {
          handler.init();
        } catch (Exception e) {
          log.error("Failed to initialize child handler: {}", handler.getClass().getName(), e);
        }
      }
    }
  }

  @Override
  public void destroy() {
    synchronized (childHandlers) {
      for (UrlHandler handler : childHandlers) {
        try {
          handler.destroy();
        } catch (Exception e) {
          log.error("Failed to destroy child handler: {}", handler.getClass().getName(), e);
        }
      }
      childHandlers.clear();
    }
  }

  @Override
  public boolean serve(HttpServletRequest req, HttpServletResponse res) throws WsException {
    String pathinfo = getPathInfo(req);
    List<UrlHandler> localHandlerList = new LinkedList<UrlHandler>(childHandlers);
    for (UrlHandler child : localHandlerList) {
      if (isSubPath(toPath(child.getPathMapping()), pathinfo)) {
        boolean served = child.serve(req, res);
        if (served) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public Object secureServe(HttpServletRequest req, HttpServletResponse res) throws WsException {
    return serve(req, res);
  }

  protected void handleCorsHeaders(HttpServletRequest req, HttpServletResponse res)
      throws WsException {
    // Read Origin once into a local variable to avoid TOCTOU and
    // to make it clear to static analyzers that the value is validated
    // before being reflected in the response.
    String origin = req.getHeader("Origin");

    // Guard (CRLF injection): reject any Origin value containing CR, LF, or
    // their URL-encoded equivalents (%0d / %0a). Reflecting such a value
    // into a response header would let an attacker inject arbitrary headers
    // or split the HTTP response. We drop the request rather than sanitise,
    // so that the illegal value never reaches setHeader().
    if (origin != null && CRLF_PATTERN.matcher(origin).find()) {
      log.warn("Rejected Origin header containing CRLF sequence from {}", req.getRemoteAddr());
      return;
    }

    if (origin != null && isOriginAllowed(origin)) {
      // Sanitize at the point of use (defense-in-depth): strip any residual
      // CR/LF characters so that taint-tracking static analyzers can verify
      // the value written to the header is clean, independently of the
      // early-return guard above.
      String safeOrigin = sanitizeHeaderValue(origin);
      res.setHeader("Access-Control-Allow-Origin", safeOrigin);
      res.setHeader("Access-Control-Allow-Credentials", "true");
      res.setHeader("Access-Control-Expose-Headers",
          Constants.HTTP_ATTR_ARGS + ", " + Constants.HTTP_ATTR_RECORDING_FLAG
              + ", X-Cache-Date, X-Atmosphere-tracking-id, X-Requested-With");
      // Indicate that the response varies by Origin so caches don't
      // serve one origin's CORS headers to another origin.
      res.setHeader("Vary", "Origin");

      if ("OPTIONS".equals(req.getMethod())) {
        res.setHeader("Access-Control-Allow-Methods", "OPTIONS, GET, POST, DELETE");
        res.setHeader("Access-Control-Allow-Headers", "Authorization, " + Constants.HTTP_ATTR_ARGS
            + ", " + Constants.HTTP_ATTR_RECORDING_FLAG
            + ", X-Requested-With, Origin, Content-Type, Content-Range, Content-Disposition, Content-Description, X-Atmosphere-Framework, X-Cache-Date, X-Atmosphere-tracking-id, X-Atmosphere-Transport");
        res.setHeader("Access-Control-Max-Age", "-1");
      }
    }
  }

  /**
   * Returns {@code true} if {@code origin} is permitted to make cross-origin requests to this
   * handler, based on the {@code allowedCorsOrigins} list in {@link SecuredPathConfig}.
   *
   * <p>
   * Behaviour:
   * <ul>
   * <li>If no {@link SecuredPathConfig} is available, or the list is null/empty: deny.</li>
   * <li>If the list contains {@code "*"}: allow any origin. The wildcard is intentionally supported
   * for backwards-compatibility with existing {@code webswing.config} files, but production
   * deployments should replace it with explicit origin values. A warning is logged on every request
   * when {@code "*"} is active.</li>
   * <li>Otherwise: exact, case-sensitive membership test. Only a matching entry is allowed.</li>
   * </ul>
   *
   * <p>
   * Note: even when the wildcard is configured, the request's specific {@code Origin} value is
   * reflected in the response (never the literal {@code "*"}), which is required for credentialled
   * CORS requests.
   *
   * @param origin the sanitized Origin header value from the request
   * @return {@code true} if the origin is permitted
   */
  protected boolean isOriginAllowed(String origin) {
    SecuredPathConfig config = getSecuredPathConfig();
    if (config == null) {
      return false;
    }
    List<String> allowed = config.getAllowedCorsOrigins();
    if (allowed == null || allowed.isEmpty()) {
      return false;
    }
    if (allowed.contains("*")) {
      log.warn("allowedCorsOrigins is set to \"*\" — all origins are permitted. "
          + "Replace with explicit origins in webswing.config for production deployments.");
      return true;
    }
    return allowed.contains(origin);
  }

  protected boolean isSameOrigin(HttpServletRequest req) {
    String origin = req.getHeader("Origin");
    if (origin == null) {
      return true; // IE11 on Win7 does not send the Origin header.
    }

    // FIX (CRLF): reject malformed Origin values here too, before any
    // string operations that could be confused by embedded newlines.
    if (CRLF_PATTERN.matcher(origin).find()) {
      log.warn("Rejected Origin header containing CRLF sequence from {}", req.getRemoteAddr());
      return false;
    }

    // FIX (origin validation): reconstruct the expected origin from the
    // servlet's own parsed request attributes rather than trusting the
    // client-supplied Host or X-Forwarded-Host headers.
    //
    // Previous code stripped the scheme from Origin before comparing it
    // against Host/X-Forwarded-Host, so "http://evil.com" and
    // "https://evil.com" were both treated as identical to host "evil.com".
    // X-Forwarded-Host is also trivially spoofable by any client.
    //
    // req.getScheme() / getServerName() / getServerPort() are populated by
    // the container from the actual TLS state and the Host header *after*
    // any trusted reverse-proxy remapping, making them the authoritative
    // values for same-origin checks.
    String scheme = req.getScheme();
    String serverName = req.getServerName();
    int port = req.getServerPort();

    // Omit the port for the two well-known defaults so the comparison
    // matches the Origin header format browsers produce.
    String expectedOrigin;
    if (("http".equals(scheme) && port == 80) || ("https".equals(scheme) && port == 443)) {
      expectedOrigin = scheme + "://" + serverName;
    } else {
      expectedOrigin = scheme + "://" + serverName + ":" + port;
    }

    return expectedOrigin.equals(origin);
  }

  public String getFullPathMapping() {
    String handlerPath = toPath(getPathMapping());
    if (this.parent != null) {
      String parentMapping = parent.getFullPathMapping();
      handlerPath = parentMapping + handlerPath;
    } else {
      handlerPath = ServerApiUtil.getContextPath(getServletContext()) + handlerPath;
    }
    return handlerPath;
  }

  public String getPathInfo(HttpServletRequest req) {
    String fullHandlerPath = getFullPathMapping();
    String requestPath =
        toPath(ServerApiUtil.getContextPath(getServletContext()) + req.getPathInfo());
    if (isSubPath(fullHandlerPath, requestPath)) {
      return toPath(requestPath.substring(fullHandlerPath.length()));
    } else {
      return "/";
    }
  }

  protected abstract String getPath();

  public String getPathMapping() {
    String path = toPath(getPath());
    return path;
  }

  public boolean isSubPath(String subpath, String path) {
    return CommonUtil.isSubPath(subpath, path);
  }

  public static String toPath(String path) {
    return CommonUtil.toPath(path);
  }

  public void registerFirstChildUrlHandler(UrlHandler handler) {
    synchronized (childHandlers) {
      childHandlers.add(0, handler);
    }
  }

  @Override
  public void registerChildUrlHandler(UrlHandler handler) {
    synchronized (childHandlers) {
      childHandlers.add(handler);
    }
  }

  @Override
  public void removeChildUrlHandler(UrlHandler handler) {
    synchronized (childHandlers) {
      if (childHandlers.contains(handler)) {
        childHandlers.remove(handler);
        handler.destroy();
      }
    }
  }

  public ServletContext getServletContext() {
    return parent.getServletContext();
  }

  public String getSecuredPath() {
    if (SecuredPathHandler.class.isAssignableFrom(this.getClass())) {
      SecuredPathHandler provider = (SecuredPathHandler) this;
      if (provider.get() != null) {
        return getFullPathMapping();
      }
    }
    if (parent == null) {
      return getFullPathMapping();
    } else {
      return parent.getSecuredPath();
    }
  }

  public SecuredPathHandler getSecurityProvider() {
    if (SecuredPathHandler.class.isAssignableFrom(this.getClass())) {
      SecuredPathHandler provider = (SecuredPathHandler) this;
      if (provider.get() != null) {
        return provider;
      }
    }
    if (parent == null) {
      return (SecuredPathHandler) this;
    } else {
      return parent.getSecurityProvider();
    }
  }

  public long getLastModified(HttpServletRequest req) {
    return -1;
  }

  public AbstractWebswingUser getUser() {
    return SecurityUtil.getUser(this);
  }

  public AbstractWebswingUser getMasterUser() {
    return SecurityUtil.getUser(getRootHandler());
  }

  @Override
  public UrlHandler getRootHandler() {
    if (parent != null) {
      return parent.getRootHandler();
    } else {
      return this;
    }
  }

  @Override
  public void checkPermission(WebswingAction action) throws WsException {
    AbstractWebswingUser user = getUser();
    checkPermission(user, action);
  }

  @Override
  public void checkMasterPermission(WebswingAction action) throws WsException {
    AbstractWebswingUser user = getMasterUser();
    checkPermission(user, action);
  }

  public void checkPermissionLocalOrMaster(WebswingAction a) throws WsException {
    try {
      checkPermission(a);
    } catch (WsException e) {
      checkMasterPermission(a);
    }
  }

  private void checkPermission(AbstractWebswingUser user, WebswingAction action)
      throws WsException {
    if (user != null) {
      if (user.isPermitted(action.name())) {
        return;
      }
    }
    throw new WsException("User '" + user + "' is not allowed to execute action '" + action + "'",
        HttpServletResponse.SC_UNAUTHORIZED);
  }

  protected void sendContent(HttpServletResponse res, String content) throws WsException {
    try (PrintWriter writer = res.getWriter()) {
      writer.write(content);
      writer.flush();
    } catch (IOException e) {
      throw new WsException(e);
    }
  }

  protected void sendFile(HttpServletResponse res, File file) throws WsException {
    try (FileInputStream fis = new FileInputStream(file)) {
      IOUtils.copy(fis, res.getOutputStream());
    } catch (IOException e) {
      throw new WsException(e);
    }
  }

}
