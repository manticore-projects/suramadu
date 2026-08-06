package org.webswing.server.api.services.rest.impl;

import com.google.inject.Singleton;
import org.webswing.server.api.GlobalUrlHandler;
import org.webswing.server.api.base.PrimaryUrlHandler;
import org.webswing.server.api.services.rest.AbstractAppRestHandler;
import org.webswing.server.api.services.rest.AbstractGlobalRestHandler;
import org.webswing.server.api.services.rest.RestHandlerFactory;

@Singleton
public class RestHandlerFactoryImpl implements RestHandlerFactory {

  @Override
  public AbstractGlobalRestHandler createGlobalRestHandler(GlobalUrlHandler parent) {
    return new GlobalRestHandlerImpl(parent);
  }

  @Override
  public AbstractAppRestHandler createAppRestHandler(PrimaryUrlHandler parent,
      GlobalUrlHandler global) {
    return new AppRestHandlerImpl(parent, global);
  }

}
