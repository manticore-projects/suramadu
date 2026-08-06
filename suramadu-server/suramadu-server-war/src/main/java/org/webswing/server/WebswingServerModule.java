package org.webswing.server;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.server.api.GlobalUrlHandler;
import org.webswing.server.api.base.WebswingService;
import org.webswing.server.api.services.application.AppPathHandlerFactory;
import org.webswing.server.api.services.application.impl.AppPathHandlerFactoryImpl;
import org.webswing.server.api.services.config.impl.DefaultConfigurationServiceImpl;
import org.webswing.server.api.services.files.FileTransferHandlerFactory;
import org.webswing.server.api.services.files.impl.FileTransferHandlerFactoryImpl;
import org.webswing.server.api.services.resources.ResourceHandlerFactory;
import org.webswing.server.api.services.resources.impl.DefaultResourceHandlerFactoryImpl;
import org.webswing.server.api.services.rest.RestHandlerFactory;
import org.webswing.server.api.services.rest.impl.RestHandlerFactoryImpl;
import org.webswing.server.api.services.security.login.LoginHandlerFactory;
import org.webswing.server.api.services.security.login.impl.LoginHandlerFactoryImpl;
import org.webswing.server.api.services.security.modules.SecurityModuleFactory;
import org.webswing.server.api.services.security.modules.impl.DefaultSecurityModuleFactoryImpl;
import org.webswing.server.api.services.sessionpool.SessionPoolHolderService;
import org.webswing.server.api.services.sessionpool.impl.SessionPoolHolderServiceImpl;
import org.webswing.server.api.services.startup.StartupService;
import org.webswing.server.api.services.startup.impl.DefaultStartupServiceImpl;
import org.webswing.server.api.services.stat.StatisticsLoggerService;
import org.webswing.server.api.services.stat.impl.DefaultStatisticsLoggerServiceImpl;
import org.webswing.server.api.services.swinginstance.SwingInstanceFactory;
import org.webswing.server.api.services.swinginstance.holder.SwingInstanceHolderFactory;
import org.webswing.server.api.services.swinginstance.holder.impl.DefaultSwingInstanceHolderFactoryImpl;
import org.webswing.server.api.services.swinginstance.impl.SwingInstanceFactoryImpl;
import org.webswing.server.api.services.websocket.WebSocketService;
import org.webswing.server.api.services.websocket.impl.DefaultWebSocketServiceImpl;
import org.webswing.server.api.services.websocket.util.AdminConsoleWebSocketConfigurator;
import org.webswing.server.api.services.websocket.util.ApplicationWebSocketConfigurator;
import org.webswing.server.api.services.websocket.util.BrowserWebSocketConfigurator;
import org.webswing.server.common.extension.ExtensionClassLoader;
import org.webswing.server.common.model.SecuredPathConfig;
import org.webswing.server.common.service.config.ConfigurationService;
import org.webswing.server.common.service.security.SecurityManagerService;
import org.webswing.server.common.service.security.impl.SecurityManagerServiceImpl;
import org.webswing.server.common.service.startup.Initializer;
import org.webswing.server.common.service.startup.impl.DefaultInitializer;

public class WebswingServerModule extends AbstractModule {
  private static final Logger log = LoggerFactory.getLogger(WebswingServerModule.class);

  private final Class<? extends StartupService> startupServiceClass =
      DefaultStartupServiceImpl.class;

  @Override
  protected void configure() {
    bind(Initializer.class).to(DefaultInitializer.class).asEagerSingleton();
    bind(ExtensionClassLoader.class).asEagerSingleton();
    bind(GlobalUrlHandler.class);

    Multibinder<WebswingService> serviceBinder =
        Multibinder.newSetBinder(binder(), WebswingService.class);

    bind(StartupService.class).to(startupServiceClass);
    serviceBinder.addBinding().to(DefaultWebSocketServiceImpl.class);
    serviceBinder.addBinding().to(DefaultConfigurationServiceImpl.class);
    serviceBinder.addBinding().to(SessionPoolHolderServiceImpl.class);

    bind(WebSocketService.class).to(DefaultWebSocketServiceImpl.class);
    bind(new TypeLiteral<ConfigurationService<SecuredPathConfig>>() {})
        .to(DefaultConfigurationServiceImpl.class);
    bind(FileTransferHandlerFactory.class).to(FileTransferHandlerFactoryImpl.class);
    bind(ResourceHandlerFactory.class).to(DefaultResourceHandlerFactoryImpl.class);
    bind(AppPathHandlerFactory.class).to(AppPathHandlerFactoryImpl.class);
    bind(SessionPoolHolderService.class).to(SessionPoolHolderServiceImpl.class);
    bind(SwingInstanceHolderFactory.class).to(DefaultSwingInstanceHolderFactoryImpl.class);
    bind(SwingInstanceFactory.class).to(SwingInstanceFactoryImpl.class);
    bind(StatisticsLoggerService.class).to(DefaultStatisticsLoggerServiceImpl.class);
    bind(RestHandlerFactory.class).to(RestHandlerFactoryImpl.class);

    bind(SecurityManagerService.class).to(SecurityManagerServiceImpl.class);
    bind(SecurityModuleFactory.class).to(DefaultSecurityModuleFactoryImpl.class);
    bind(LoginHandlerFactory.class).to(LoginHandlerFactoryImpl.class);

    requestStaticInjection(BrowserWebSocketConfigurator.class);
    requestStaticInjection(ApplicationWebSocketConfigurator.class);
    requestStaticInjection(AdminConsoleWebSocketConfigurator.class);
  }

}
