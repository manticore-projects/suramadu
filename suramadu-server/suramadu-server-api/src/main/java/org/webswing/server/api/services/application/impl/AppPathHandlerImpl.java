package org.webswing.server.api.services.application.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.model.appframe.out.SimpleEventMsgOut;
import org.webswing.model.common.in.ConnectionHandshakeMsgIn;
import org.webswing.server.api.GlobalUrlHandler;
import org.webswing.server.api.base.PrimaryUrlHandler;
import org.webswing.server.api.model.ApplicationInfoMsg;
import org.webswing.server.api.services.application.AppPathHandler;
import org.webswing.server.api.services.files.FileTransferHandler;
import org.webswing.server.api.services.files.FileTransferHandlerFactory;
import org.webswing.server.api.services.resources.ResourceHandlerFactory;
import org.webswing.server.api.services.resources.WebResourceProvider;
import org.webswing.server.api.services.rest.RestHandlerFactory;
import org.webswing.server.api.services.security.login.LoginHandlerFactory;
import org.webswing.server.api.services.security.modules.SecurityModuleFactory;
import org.webswing.server.api.services.sessionpool.SessionPoolHolderService;
import org.webswing.server.api.services.swinginstance.SwingInstanceInfo;
import org.webswing.server.api.services.websocket.PrimaryWebSocketConnection;
import org.webswing.server.api.services.websocket.WebSocketService;
import org.webswing.server.common.datastore.BuiltInDataStoreModules;
import org.webswing.server.common.datastore.WebswingDataStoreConfig;
import org.webswing.server.common.model.SecuredPathConfig;
import org.webswing.server.common.service.config.ConfigurationService;
import org.webswing.server.common.service.security.AbstractWebswingUser;
import org.webswing.server.common.util.CommonUtil;
import org.webswing.server.common.util.ServerUtil;
import org.webswing.server.common.util.VariableSubstitutor;
import org.webswing.server.model.exception.WsException;
import org.webswing.server.services.security.api.AuthorizationConfig;

import javax.servlet.http.HttpServletResponse;
import java.io.File;

public class AppPathHandlerImpl extends PrimaryUrlHandler
    implements AppPathHandler, WebResourceProvider {
  private static final Logger log = LoggerFactory.getLogger(AppPathHandlerImpl.class);

  private final GlobalUrlHandler parent;

  private final String path;
  private final WebSocketService websocket;
  private final LoginHandlerFactory loginFactory;
  private final ResourceHandlerFactory resourceFactory;
  private final RestHandlerFactory restHandlerFactory;
  private final FileTransferHandler fileFactory;
  private final SessionPoolHolderService sessionPoolHolderService;

  public AppPathHandlerImpl(GlobalUrlHandler parent, String path, WebSocketService websocket,
      FileTransferHandlerFactory fileFactory, LoginHandlerFactory loginFactory,
      ResourceHandlerFactory resourceFactory, SecurityModuleFactory securityModuleFactory,
      ConfigurationService<SecuredPathConfig> configService,
      SessionPoolHolderService sessionPoolHolderService, RestHandlerFactory restHandlerFactory) {
    super(parent, securityModuleFactory, configService);
    this.parent = parent;
    this.path = path;
    this.websocket = websocket;
    this.loginFactory = loginFactory;
    this.resourceFactory = resourceFactory;
    this.fileFactory = fileFactory.create(this);
    this.sessionPoolHolderService = sessionPoolHolderService;
    this.restHandlerFactory = restHandlerFactory;
  }

  @Override
  public void init() {
    websocket.registerPathHandler(path, this);

    registerChildUrlHandler(loginFactory.createLoginHandler(this));
    registerChildUrlHandler(loginFactory.createLogoutHandler(this));
    registerChildUrlHandler(fileFactory);

    registerChildUrlHandler(restHandlerFactory.createAppRestHandler(this, parent));

    registerChildUrlHandler(resourceFactory.create(this, this));
    super.init();
  }

  @Override
  public void destroy() {
    sessionPoolHolderService.destroy(path);
    websocket.unregisterPathHandler(path);
    super.destroy();
  }

  @Override
  public void initDataStore() {
    super.dataStore = null;
    getDataStore();
  }

  @Override
  public void connectView(ConnectionHandshakeMsgIn handshake, PrimaryWebSocketConnection r) {
    try {
      checkAuthorization(r.getUser());
      if (!isEnabled()) {
        throw new WsException("This application is disabled.");
      }
      if (handshake.isMirrored()) {
        // mirror connects from admin console to a correct cluster server
        // if a mirror handshake ends up here it is an error
        throw new WsException("Direct mirror connection is not allowed!");
      }
    } catch (WsException e1) {
      log.error("User authorization failed. {}", e1.getMessage());
      if (r.isConnected()) {
        r.sendMessage(SimpleEventMsgOut.unauthorizedAccess.buildMsgOut());
      }
      return;
    }

    try {
      sessionPoolHolderService.connectView(path, handshake, r, createSwingInstanceInfo());
    } catch (WsException e) {
      log.error("Failed to connect to instance. ", e);
      if (r.isConnected()) {
        r.sendMessage(SimpleEventMsgOut.configurationError.buildMsgOut());
      }
    }
  }

  @Override
  public SwingInstanceInfo createSwingInstanceInfo() {
    return new SwingInstanceInfo(ServerUtil.getContextPath(getServletContext()), getPathMapping(),
        getConfig(), getDataStoreConfig());
  }

  @Override
  protected String getPath() {
    return path;
  }

  @Override
  protected void killAll() {
    sessionPoolHolderService.killAll(path);
  }

  @Override
  public ApplicationInfoMsg getApplicationInfoMsg() {
    ApplicationInfoMsg app = new ApplicationInfoMsg();
    app.setName(getConfig().getName());
    app.setUrl(getFullPathMapping());
    app.setBase64Icon(getIconAsBytes());
    return app;
  }

  @Override
  public byte[] getIconAsBytes() {
    File icon = resolveFile(getConfig().getIcon());
    return CommonUtil.loadImage(icon);
  }

  @Override
  public boolean isUserAuthorized() {
    return isUserAuthorized(null);
  }

  @Override
  public WebswingDataStoreConfig getDataStoreConfig() {
    WebswingDataStoreConfig dataStoreConfig = super.getDataStoreConfig();

    if (BuiltInDataStoreModules.INHERITED.name().equals(dataStoreConfig.getModule())) {
      dataStoreConfig = parent.getDataStoreConfig();
    }

    return dataStoreConfig;
  }

  private boolean isUserAuthorized(AbstractWebswingUser user) {
    if (user == null) {
      user = getUser();
    }
    if (user == null) {
      return false;
    }

    AuthorizationConfig authorizationConfig = getSecurityConfig().getAuthorizationConfig();
    if (authorizationConfig == null || (authorizationConfig.getRoles().size() == 0
        && authorizationConfig.getUsers().size() == 0)) {
      return true;
    } else {
      VariableSubstitutor subs = VariableSubstitutor.forSwingApp(getConfig());
      for (String role : authorizationConfig.getRoles()) {
        String resolvedRole = subs.replace(role);
        if (user.hasRole(resolvedRole)) {
          return true;
        }
      }
      for (String u : authorizationConfig.getUsers()) {
        String resolvedUser = subs.replace(u);
        if (user.getUserId().equals(resolvedUser)) {
          return true;
        }
      }
    }
    return false;
  }

  private void checkAuthorization(AbstractWebswingUser user) throws WsException {
    if (!isUserAuthorized(user)) {
      throw new WsException(
          "User '" + user + "' is not authorized to access application " + getPathMapping(),
          HttpServletResponse.SC_UNAUTHORIZED);
    }
  }

}
