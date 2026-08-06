package org.webswing.server.api.base;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.Constants;
import org.webswing.server.api.model.InstanceManagerStatus;
import org.webswing.server.api.model.InstanceManagerStatus.StatusEnum;
import org.webswing.server.api.services.datastore.DataStoreHandler;
import org.webswing.server.api.services.security.login.SecuredPathHandler;
import org.webswing.server.api.services.security.modules.SecurityModuleFactory;
import org.webswing.server.api.util.SecurityUtil;
import org.webswing.server.api.util.ServerApiUtil;
import org.webswing.server.common.datastore.DataStoreModuleWrapper;
import org.webswing.server.common.datastore.WebswingDataStoreConfig;
import org.webswing.server.common.datastore.WebswingDataStoreModule;
import org.webswing.server.common.model.SecuredPathConfig;
import org.webswing.server.common.service.config.ConfigurationService;
import org.webswing.server.common.util.CommonUtil;
import org.webswing.server.common.util.JwtUtil;
import org.webswing.server.common.util.ServerUtil;
import org.webswing.server.common.util.VariableSubstitutor;
import org.webswing.server.model.exception.WsException;
import org.webswing.server.model.exception.WsInitException;
import org.webswing.server.services.security.api.SecurityContext;
import org.webswing.server.services.security.api.WebswingAuthenticationException;
import org.webswing.server.services.security.api.WebswingSecurityConfig;
import org.webswing.server.services.security.api.WebswingSecurityModule;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class PrimaryUrlHandler extends AbstractUrlHandler
    implements SecuredPathHandler, SecurityContext, DataStoreHandler {

  private static final Logger log = LoggerFactory.getLogger(PrimaryUrlHandler.class);
  public static final String JAVASCRIPT_NLS_PREFIX = "/javascript/nls";

  private final SecurityModuleFactory securityModuleFactory;
  private final ConfigurationService<SecuredPathConfig> configService;

  private WebswingSecurityModule securityModule;
  private boolean enabled;
  private VariableSubstitutor varSubs;
  private final InstanceManagerStatus status = new InstanceManagerStatus();
  protected WebswingDataStoreModule dataStore;

  private boolean initialized;

  private SecuredPathConfig config;

  public PrimaryUrlHandler(UrlHandler parent, SecurityModuleFactory securityModuleFactory,
      ConfigurationService<SecuredPathConfig> configService) {
    super(parent);
    this.securityModuleFactory = securityModuleFactory;
    this.securityModule = securityModuleFactory.createNoAccess(null, this, null); // no access until
                                                                                  // real SM is
                                                                                  // initialized
    this.varSubs = VariableSubstitutor.basic();
    this.configService = configService;
  }

  @Override
  public void init() {
    super.init();

    try {
      if (getConfig().isEnabled()) {
        initConfiguration();
      } else {
        disable();
      }
      initialized = true;
    } catch (Throwable e) {
      log.error("Failed to start '{}'.", getPathMapping(), e);
      try {
        destroy();
      } catch (Throwable e1) {
        // do nothing
      }
      setStatusError(e);
    }
  }

  public synchronized void initConfiguration() {
    status.setStatus(StatusEnum.STARTING);
    String path = StringUtils.isEmpty(getPathMapping()) ? "/" : getPathMapping();
    config = configService.getConfiguration(path);
    WebswingSecurityConfig securityConfig = getSecurityConfig();

    try {
      varSubs = VariableSubstitutor.forSwingApp(getConfig());
      if (!new File(getHome()).getAbsoluteFile().isDirectory()) {// check if home dir exists
        throw new WsInitException(
            "Home Folder '" + new File(getHome()).getAbsolutePath() + "'does not exist!");
      }
      try {
        if (securityModule != null) {
          securityModule.destroy();
        }
      } catch (Exception e) {
        log.error("Failed to destroy Security module for {}.", path, e);
      }
      securityModule = securityModuleFactory.create(this, securityConfig);
      if (securityModule != null) {
        securityModule.init();
      }

      status.setStatus(StatusEnum.RUNNING);
      enabled = true;
    } catch (Exception e) {
      securityModule = securityModuleFactory
          .createNoAccess(WebswingAuthenticationException.CONFIG_ERROR, this, securityConfig);
      setStatusError(e);
    }
    try {
      dataStore = null;
      getDataStore();
    } catch (Exception e) {
      enabled = false;
      setStatusError(e);
    }
  }

  @Override
  public SecuredPathConfig getConfig() {
    if (config == null || !enabled) {
      String path = StringUtils.isEmpty(getPathMapping()) ? "/" : getPathMapping();
      config = configService.getConfiguration(path);
    }
    return config;
  }

  @Override
  public WebswingDataStoreModule getDataStore() {
    if (dataStore == null) {
      dataStore = new DataStoreModuleWrapper(getDataStoreConfig());
    }
    return dataStore;
  }

  public synchronized void disable() {
    status.setStatus(StatusEnum.STOPPING);
    enabled = false;
    try {
      killAll();
      if (securityModule != null) {
        securityModule.destroy();
      }
    } finally {
      this.securityModule = securityModuleFactory.createNoAccess(null, this, null); // no access
                                                                                    // until real SM
                                                                                    // is
                                                                                    // initialized
    }
    status.setStatus(StatusEnum.STOPPED);
  }

  protected void killAll() {// to be implemented in subclass
  }

  private void setStatusError(Throwable e) {
    status.setStatus(StatusEnum.ERROR);
    status.setError(e.getMessage());
    StringWriter out = new StringWriter();
    PrintWriter stacktrace = new PrintWriter(out);
    e.printStackTrace(stacktrace);
    status.setErrorDetails(out.toString());
  }

  protected WebswingSecurityConfig getSecurityConfig() {
    return getConfig().getValueAs("security", WebswingSecurityConfig.class);
  }

  public WebswingDataStoreConfig getDataStoreConfig() {
    return getConfig().getValueAs("dataStore", WebswingDataStoreConfig.class);
  }

  @Override
  public void destroy() {
    super.destroy();
    disable();
    initialized = false;
  }

  @Override
  public boolean serve(HttpServletRequest req, HttpServletResponse res) throws WsException {
    handleCorsHeaders(req, res);
    // redirect to url that is correct case and ends with '/' to ensure browser queries correct
    // resources
    if (isWrongUrlCase(req) || isRootPathWithoutSlash(req)) {
      try {
        String redirectUrl = getFullPathMapping()
            + (ServerApiUtil.getContextPath(getServletContext()) + toPath(req.getPathInfo()))
                .substring(getFullPathMapping().length());
        redirectUrl = isRootPathWithoutSlash(req) ? (redirectUrl + "/") : redirectUrl;
        // use apache flag instead: ProxyPreserveHost on
        // if (System.getProperty(Constants.REVERSE_PROXY_CONTEXT_PATH) != null) { //reverse proxy
        // will add the context path to redirect url so we need to remove it to avoid duplicate. see
        // ProxyPassReverse apache directive
        // redirectUrl =
        // redirectUrl.substring(ServerUtil.getContextPath(getServletContext()).length());
        // }
        String queryString = req.getQueryString() == null ? "" : ("?" + req.getQueryString());
        ServerApiUtil.sendHttpRedirect(req, res, redirectUrl + queryString);
      } catch (IOException e) {
        log.error("Failed to redirect.", e);
      }
      return true;
    } else {
      return super.serve(req, res);
    }
  }

  @Override
  public boolean isSubPath(String subpath, String path) {
    return CommonUtil.isSubPathIgnoreCase(subpath, path);
  }

  private boolean isWrongUrlCase(HttpServletRequest req) {
    if (req.getPathInfo() == null) {
      return false;
    }
    String relevantUrlPart = toPath(req.getPathInfo()).substring(0, getPathMapping().length());
    boolean isWrongCase = !relevantUrlPart.equals(getPathMapping())
        && relevantUrlPart.equalsIgnoreCase(getPathMapping()); // if this handler path is not
                                                               // correct case
    return isWrongCase;
  }

  private boolean isRootPathWithoutSlash(HttpServletRequest req) {
    boolean isRootPathWithoutSlash =
        (ServerApiUtil.getContextPath(getServletContext()) + req.getPathInfo())
            .equals(getFullPathMapping()); // path has to end with '/'
    return req.getPathInfo() == null || isRootPathWithoutSlash;
  }

  protected boolean isOriginAllowed(String header) {
    if (super.isOriginAllowed(header)) {
      return true;
    }

    String url = VariableSubstitutor.basic().replace(getConfig().getAdminConsoleUrl());
    if (StringUtils.isNotBlank(url) && url.toLowerCase().startsWith("http")
        && ServerUtil.domainFromUrl(url).equals(header)) {
      return true;
    }

    List<String> allowedCorsOrigins = getConfig().getAllowedCorsOrigins();
    if (allowedCorsOrigins == null) {
      allowedCorsOrigins = new ArrayList<>();
    }

    for (String s : allowedCorsOrigins) {
      if (s.trim().equals(header) || "*".equals(s.trim())) {
        return true;
      }
    }

    return false;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  @Override
  public WebswingSecurityModule get() {
    return securityModule;
  }

  @Override
  public File resolveFile(String name) {
    String home = getHome();
    return CommonUtil.resolveFile(name, home, varSubs);
  }

  private String getHome() {
    return VariableSubstitutor.basic().replace(getConfig().getWebHomeDir());
  }

  @Override
  public URL getWebResource(String resource) {
    if (toPath(resource).startsWith(JAVASCRIPT_NLS_PREFIX)) {
      String langs = getConfig().getLangFolder();
      File langsFolder = StringUtils.isEmpty(langs) ? null : resolveFile(langs);
      URL result = ServerUtil
          .getFileResource(toPath(resource).substring(JAVASCRIPT_NLS_PREFIX.length()), langsFolder);
      if (result != null) {
        return result;
      }
    }

    String webFolderPath = getConfig().getWebFolder();
    File webFolder = StringUtils.isEmpty(webFolderPath) ? null : resolveFile(webFolderPath);
    return ServerUtil.getWebResource(toPath(resource), getServletContext(), webFolder);
  }

  @Override
  public String replaceVariables(String string) {
    return varSubs.replace(string);
  }

  @Override
  public Object getFromSecuritySession(String attributeName) {
    return SecurityUtil.getFromSecuritySession(attributeName);
  }

  @Override
  public void setToSecuritySession(String attributeName, Object value) {
    SecurityUtil.setToSecuritySession(attributeName, value);
  }

  public boolean isInitialized() {
    return initialized;
  }

  public InstanceManagerStatus getStatus() {
    return status;
  }

  public void refreshToken(HttpServletRequest req, HttpServletResponse res) {
    String refreshToken =
        ServerUtil.parseTokenFromCookie(req, Constants.WEBSWING_SESSION_REFRESH_TOKEN);
    if (StringUtils.isBlank(refreshToken)) {
      res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    Map<String, Object> claims = JwtUtil.parseRefreshTokenClaims(refreshToken);
    if (claims == null) {
      res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    String webswingClaim = (String) claims.get(Constants.JWT_CLAIM_WEBSWING);

    if (StringUtils.isBlank(webswingClaim)) {
      res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    // copy claims body to new token
    ServerUtil.writeTokens(res, webswingClaim, false);

    res.setStatus(HttpServletResponse.SC_OK);
  }

}
