package org.webswing.server.api;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.Constants;
import org.webswing.server.api.base.PrimaryUrlHandler;
import org.webswing.server.api.base.UrlHandler;
import org.webswing.server.api.services.application.AppPathHandler;
import org.webswing.server.api.services.application.AppPathHandlerFactory;
import org.webswing.server.api.services.resources.ResourceHandlerFactory;
import org.webswing.server.api.services.resources.WebResourceProvider;
import org.webswing.server.api.services.rest.RestHandlerFactory;
import org.webswing.server.api.services.security.login.LoginHandlerFactory;
import org.webswing.server.api.services.security.login.SecuredPathHandler;
import org.webswing.server.api.services.security.modules.SecurityModuleFactory;
import org.webswing.server.api.services.sessionpool.SessionPoolHolderService;
import org.webswing.server.api.util.ServerApiUtil;
import org.webswing.server.common.datastore.BuiltInDataStoreModules;
import org.webswing.server.common.datastore.WebswingDataStoreConfig;
import org.webswing.server.common.model.SecuredPathConfig;
import org.webswing.server.common.model.meta.ConfigContext;
import org.webswing.server.common.model.meta.MetaObject;
import org.webswing.server.common.service.config.ConfigurationChangeEvent;
import org.webswing.server.common.service.config.ConfigurationChangeListener;
import org.webswing.server.common.service.config.ConfigurationService;
import org.webswing.server.common.util.ServerUtil;
import org.webswing.server.model.exception.WsException;
import org.webswing.server.services.security.api.BuiltInModules;
import org.webswing.server.services.security.api.WebswingSecurityConfig;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.node.ObjectNode;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

@Singleton
public class GlobalUrlHandler extends PrimaryUrlHandler
    implements SecuredPathHandler, WebResourceProvider {
  private static final Logger log = LoggerFactory.getLogger(GlobalUrlHandler.class);
  private static final String SERVERNAME =
      System.getProperty(Constants.BRANDING_PREFIX, "webswing.org");

  private final ConfigurationService<SecuredPathConfig> configService;
  private final AppPathHandlerFactory appPathHandlerFactory;
  private final ResourceHandlerFactory resourceFactory;
  private final RestHandlerFactory restHandlerFactory;
  private final LoginHandlerFactory loginFactory;
  private final SessionPoolHolderService sessionPoolHolderService;

  private ServletContext servletContext;
  private JsonMapper mapper = new JsonMapper();

  private Map<String, AppPathHandler> appPathHandlers = new LinkedHashMap<String, AppPathHandler>();

  private final ConfigurationChangeListener<SecuredPathConfig> changeListener =
      new ConfigurationChangeListener<SecuredPathConfig>() {
        @Override
        public void onConfigChanged(ConfigurationChangeEvent<SecuredPathConfig> e) {
          if ("/".equals(e.getPath())) {
            initConfiguration();

            appPathHandlers.values().forEach(AppPathHandler::initDataStore);
          } else {
            AppPathHandler manager = appPathHandlers.get(e.getPath());
            if (manager == null) {
              installApplication(e.getNewConfig()).init();
            } else if (manager.isEnabled()) {
              manager.initConfiguration();
            }
          }

          sessionPoolHolderService.sendServerInfoUpdate();
        }

        @Override
        public void onConfigDeleted(ConfigurationChangeEvent<SecuredPathConfig> e) {
          if ("/".equals(e.getPath())) {
            initConfiguration();
          } else {
            AppPathHandler manager = appPathHandlers.get(e.getPath());
            if (manager != null) {
              uninstallApplication(manager);
            }
          }

          sessionPoolHolderService.sendServerInfoUpdate();
        }
      };

  @Inject
  public GlobalUrlHandler(ConfigurationService<SecuredPathConfig> config,
      AppPathHandlerFactory appPathHandlerFactory, ResourceHandlerFactory resourceFactory,
      SecurityModuleFactory securityFactory, LoginHandlerFactory loginFactory,
      ServletContext servletContext, RestHandlerFactory restHandlerFactory,
      SessionPoolHolderService sessionPoolHolderService) {
    super(null, securityFactory, config);
    this.configService = config;
    this.appPathHandlerFactory = appPathHandlerFactory;
    this.resourceFactory = resourceFactory;
    this.loginFactory = loginFactory;
    this.servletContext = servletContext;
    this.restHandlerFactory = restHandlerFactory;
    this.sessionPoolHolderService = sessionPoolHolderService;
  }

  public void init() {
    registerChildUrlHandler(loginFactory.createLoginHandler(this));
    registerChildUrlHandler(loginFactory.createLogoutHandler(this));

    registerChildUrlHandler(restHandlerFactory.createGlobalRestHandler(this));
    registerChildUrlHandler(resourceFactory.create(this, this));

    loadApplications();
    this.configService.registerChangeListener(this.changeListener);
    super.init();
    if (!isInitialized()) {
      throw new RuntimeException("Failed to start primary handler.");
    }
  }

  public void destroy() {
    this.configService.removeChangeListener(this.changeListener);
    appPathHandlers.clear();
    super.destroy();
  }

  public boolean serve(HttpServletRequest req, HttpServletResponse res) {
    try {
      setSecurityHeaders(req, res);
      boolean served = super.serve(req, res);
      if (!served) {
        throw new WsException("Not Found.", HttpServletResponse.SC_NOT_FOUND);
      }
    } catch (Exception e) {
      handleException(e, req, res);
    }
    return true;
  }

  private void setSecurityHeaders(HttpServletRequest req, HttpServletResponse res) {
    res.addHeader("Server", SERVERNAME);
    if (!Boolean.getBoolean(Constants.DISABLE_HTTP_SECURITY_HEADERS)) {
      res.addHeader("X-Frame-Options", "SAMEORIGIN");
      res.addHeader("X-Content-Type-Options", "nosniff");
      res.addHeader("X-XSS-Protection", "1; mode=block");
      res.addHeader("Referrer-Policy", "strict-origin-when-cross-origin");
      if (StringUtils.equalsIgnoreCase(req.getScheme(), "https")) {
        res.addHeader("Strict-Transport-Security", "max-age=63072000; includeSubDomains; preload");
      }
    }
  }

  @Override
  protected WebswingSecurityConfig getSecurityConfig() {
    log.info("Loading master security module.({}).", getConfig().getSecurity());
    WebswingSecurityConfig secConfig = super.getSecurityConfig();
    if (BuiltInModules.INHERITED.name().equals(secConfig.getModule())) {
      log.error(
          "Master security module INHERITED is not valid. Falling back to default module EMBEDDED.");
      SecuredPathConfig newconfig = getConfig();
      newconfig.getSecurity().put("module", BuiltInModules.EMBEDDED.name());
      secConfig = newconfig.getValueAs("security", WebswingSecurityConfig.class);
    }
    return secConfig;
  }

  @Override
  public WebswingDataStoreConfig getDataStoreConfig() {
    WebswingDataStoreConfig dataStoreConfig = super.getDataStoreConfig();

    if (BuiltInDataStoreModules.INHERITED.name().equals(dataStoreConfig.getModule())) {
      log.info(
          "Master dataStore module INHERITED is not valid. Falling back to default module FILESYSTEM.");
      SecuredPathConfig newconfig = getConfig();
      newconfig.getDataStore().put("module", BuiltInDataStoreModules.FILESYSTEM.name());
      dataStoreConfig = newconfig.getValueAs("dataStore", WebswingDataStoreConfig.class);
    }

    return dataStoreConfig;
  }

  private void loadApplications() {
    log.info("Loading configured Applications.");
    synchronized (appPathHandlers) {
      for (String path : configService.getPaths()) {
        SecuredPathConfig configPath = configService.getConfiguration(path);
        String pathMapping = toPath(path);
        if (!toPath("/").equals(pathMapping)) {
          AppPathHandler childHandler = appPathHandlers.get(pathMapping);
          if (childHandler == null) {
            installApplication(configPath);
          } else {
            log.error("Application with path '{}' already exists! Application skipped.",
                pathMapping, new IllegalStateException("Invalid Application configuration."));
          }
        }
      }
    }
  }

  private AppPathHandler installApplication(SecuredPathConfig swing) {
    log.info("Installing application {}", swing.getPath());
    AppPathHandler appPathHandler =
        appPathHandlerFactory.createAppPathHandler(this, swing.getPath());
    registerFirstChildUrlHandler(appPathHandler);
    return appPathHandler;
  }

  private void uninstallApplication(AppPathHandler appPathHandlerToRemove) {
    log.info("Removing application {}", appPathHandlerToRemove.getPathMapping());
    appPathHandlerToRemove.destroy();
    removeChildUrlHandler(appPathHandlerToRemove);
  }

  private void handleException(Exception e, HttpServletRequest req, HttpServletResponse res) {
    // Sanitize user-controlled path before logging to prevent log injection (CRLF / Trojan Source)
    String safePath = sanitizeForLog(req.getPathInfo());
    log.debug("Failed to process request. {}", safePath, e);
    try {
      if (e instanceof WsException wse) {
        if (!res.isCommitted()) {
          // Send generic status text instead of exception message
          // to prevent leaking internal details to the client
          res.sendError(wse.getReponseCode());
        }
      } else {
        log.error("Failed to process request. {}", safePath, e);
        if (!res.isCommitted()) {
          // Return a generic error body — never send stack traces or
          // exception messages to the client (information disclosure)
          res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
      }
    } catch (IOException e1) {
      log.error("Failed send error response to client.");
    }

  }

  /**
   * Sanitize a string for safe inclusion in log messages. Strips control characters and truncates
   * to prevent log injection / flooding.
   */
  private static String sanitizeForLog(String input) {
    if (input == null) {
      return "null";
    }
    return input.replaceAll("[\\p{Cc}\\p{Cf}]", "_").substring(0, Math.min(input.length(), 500));
  }

  @Override
  public void registerFirstChildUrlHandler(UrlHandler handler) {
    super.registerFirstChildUrlHandler(handler);
    if (handler instanceof AppPathHandler manager) {
      synchronized (appPathHandlers) {
        appPathHandlers.put(manager.getPathMapping(), manager);
      }
    }
  }

  @Override
  public void registerChildUrlHandler(UrlHandler handler) {
    super.registerChildUrlHandler(handler);
    if (handler instanceof AppPathHandler manager) {
      synchronized (appPathHandlers) {
        appPathHandlers.put(manager.getPathMapping(), manager);
      }
    }
  }

  @Override
  public void removeChildUrlHandler(UrlHandler handler) {
    super.removeChildUrlHandler(handler);
    if (handler instanceof AppPathHandler manager) {
      synchronized (appPathHandlers) {
        appPathHandlers.remove(manager.getPathMapping(), manager);
      }
    }
  }

  protected String getPath() {
    return "";
  }

  public ServletContext getServletContext() {
    return servletContext;
  }

  @Override
  public URL getWebResource(String resource) {
    if (!isCustomIndexPage() && StringUtils.equals("/index.html", toPath(resource))) {
      resource = System.getProperty(Constants.DEFAULT_WELCOME_PAGE, "/selector/index.html");
    }
    return super.getWebResource(resource);
  }

  public List<AppPathHandler> getApplications() {
    ArrayList<AppPathHandler> result = new ArrayList<>();
    synchronized (appPathHandlers) {
      result.addAll(appPathHandlers.values());
    }
    return result;
  }

  public AppPathHandler getAppHandler(String path) {
    synchronized (appPathHandlers) {
      return appPathHandlers.get(path);
    }
  }

  public PrimaryUrlHandler getAppPrimaryUrlHandler(String path) {
    synchronized (appPathHandlers) {
      AppPathHandler handler = appPathHandlers.get(path);
      if (handler instanceof PrimaryUrlHandler urlHandler) {
        return urlHandler;
      }
    }
    return null;
  }

  private boolean isCustomIndexPage() {
    String customFolder = getConfig().getWebFolder();
    if (StringUtils.isBlank(customFolder)) {
      return false;
    }
    File customFolderFile = resolveFile(customFolder);
    if (customFolderFile != null && customFolderFile.isDirectory()
        && new File(customFolderFile, "index.html").isFile()) {
      return true;
    }
    return false;
  }

  public byte[] getConfigBytes(String path) throws Exception {
    ConfigContext configContext = "/".equals(path) ? this : getAppPrimaryUrlHandler(path);
    MetaObject config = configService.describeConfiguration(path, null, configContext);
    return mapper.writeValueAsBytes(config);
  }

  @SuppressWarnings("unchecked")
  public byte[] getMetaBytes(String path, byte[] requestBody) throws Exception {
    Map<String, Object> meta =
        mapper.readValue(requestBody, new TypeReference<Map<String, Object>>() {});
    ConfigContext configContext = "/".equals(path) ? this : getAppPrimaryUrlHandler(path);
    if (meta != null && meta.get("data") != null) {
      MetaObject config = configService.describeConfiguration(path,
          (Map<String, Object>) meta.get("data"), configContext);
      return mapper.writeValueAsBytes(config);
    } else {
      throw new IllegalArgumentException("Could not find meta data!");
    }
  }

  @SuppressWarnings("unchecked")
  public void saveConfig(String path, byte[] configBytes) throws Exception {
    Map<String, Object> config =
        mapper.readValue(configBytes, new TypeReference<Map<String, Object>>() {});
    if (config != null && config.get("data") != null) {
      configService.setConfiguration(path, (Map<String, Object>) config.get("data"));
    } else {
      throw new IllegalArgumentException("Could not find config data!");
    }
  }

  public void removeConfig(String path) {
    AppPathHandler swingManager = getAppHandler(path);
    if (swingManager != null) {
      if (!swingManager.isEnabled()) {
        try {
          configService.removeConfiguration(path);
        } catch (Exception e) {
          log.error("Unable to Remove App '{}'!", path, e);
        }
      } else {
        log.error("Unable to Remove App '{}' while running. Stop the app first", path);
      }
    }
  }

  public void createConfig(String path) {
    AppPathHandler swingManager = getAppHandler(path);
    if (swingManager == null) {
      Map<String, Object> config = new HashMap<>();
      config.put("enabled", false);
      try {
        configService.setConfiguration(path, config); // first create with enabled:false to prevent
                                                      // initiation
        configService.setConfiguration(path, null); // once exists,
      } catch (Exception e) {
        log.error("Unable to Create App '{}'!", path, e);
      }
    } else {
      log.error("Unable to Create App '{}'. Application already exits.", path);
    }
  }

  public void issueAdminConsoleAccessToken(HttpServletRequest req, HttpServletResponse res) {
    String loginToken =
        ServerUtil.parseTokenFromCookie(req, Constants.WEBSWING_SESSION_ADMIN_CONSOLE_LOGIN_TOKEN);
    if (StringUtils.isBlank(loginToken)) {
      res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    String accessId = UUID.randomUUID().toString();

    String servletPrefix = ServerApiUtil.getContextPath(getServletContext());

    if (sessionPoolHolderService.issueAdminConsoleAccessToken(accessId, loginToken,
        servletPrefix)) {
      ObjectNode result = mapper.createObjectNode();
      result.put("accessId", accessId);

      res.setContentType("application/json");
      res.setCharacterEncoding("UTF-8");

      try {
        res.getWriter().write(result.toString());
      } catch (IOException e) {
        log.error("Could not write accessId to response!", e);
      }

      res.setStatus(HttpServletResponse.SC_OK);
    } else {
      res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

}
