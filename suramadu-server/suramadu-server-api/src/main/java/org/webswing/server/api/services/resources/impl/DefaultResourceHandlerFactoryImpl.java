package org.webswing.server.api.services.resources.impl;

import com.google.inject.Singleton;
import org.webswing.server.api.base.PrimaryUrlHandler;
import org.webswing.server.api.services.resources.ResourceHandler;
import org.webswing.server.api.services.resources.ResourceHandlerFactory;
import org.webswing.server.api.services.resources.WebResourceProvider;

@Singleton
public class DefaultResourceHandlerFactoryImpl implements ResourceHandlerFactory {

  @Override
  public ResourceHandler create(PrimaryUrlHandler manager,
      WebResourceProvider webResourceProvider) {
    return new DefaultResourceHandlerImpl(manager, webResourceProvider);
  }

}
