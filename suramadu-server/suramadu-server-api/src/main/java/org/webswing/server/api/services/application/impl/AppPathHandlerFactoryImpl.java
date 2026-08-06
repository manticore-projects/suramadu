package org.webswing.server.api.services.application.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.webswing.server.api.GlobalUrlHandler;
import org.webswing.server.api.services.application.AppPathHandler;
import org.webswing.server.api.services.application.AppPathHandlerFactory;
import org.webswing.server.api.services.files.FileTransferHandlerFactory;
import org.webswing.server.api.services.resources.ResourceHandlerFactory;
import org.webswing.server.api.services.rest.RestHandlerFactory;
import org.webswing.server.api.services.security.login.LoginHandlerFactory;
import org.webswing.server.api.services.security.modules.SecurityModuleFactory;
import org.webswing.server.api.services.sessionpool.SessionPoolHolderService;
import org.webswing.server.api.services.websocket.WebSocketService;
import org.webswing.server.common.model.SecuredPathConfig;
import org.webswing.server.common.service.config.ConfigurationService;

@Singleton
public class AppPathHandlerFactoryImpl implements AppPathHandlerFactory {

  private final WebSocketService websocket;
  private final FileTransferHandlerFactory fileFactory;
  private final LoginHandlerFactory loginFactory;
  private final ResourceHandlerFactory resourceFactory;
  private final RestHandlerFactory restHandlerFactory;
  private final SecurityModuleFactory securityModuleFactory;
  private final ConfigurationService<SecuredPathConfig> configService;
  private final SessionPoolHolderService sessionPoolHolderService;

  @Inject
  public AppPathHandlerFactoryImpl(WebSocketService websocket,
      FileTransferHandlerFactory fileFactory, LoginHandlerFactory loginFactory,
      ResourceHandlerFactory resourceFactory, SecurityModuleFactory securityModuleFactory,
      ConfigurationService<SecuredPathConfig> configService,
      SessionPoolHolderService sessionPoolHolderService, RestHandlerFactory restHandlerFactory) {
    this.websocket = websocket;
    this.fileFactory = fileFactory;
    this.loginFactory = loginFactory;
    this.resourceFactory = resourceFactory;
    this.securityModuleFactory = securityModuleFactory;
    this.configService = configService;
    this.sessionPoolHolderService = sessionPoolHolderService;
    this.restHandlerFactory = restHandlerFactory;
  }

  public AppPathHandler createAppPathHandler(GlobalUrlHandler parent, String path) {
    return new AppPathHandlerImpl(parent, path, websocket, fileFactory, loginFactory,
        resourceFactory, securityModuleFactory, configService, sessionPoolHolderService,
        restHandlerFactory);
  }

}
