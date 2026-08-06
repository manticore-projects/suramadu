package org.webswing.server.api.services.rest;

import org.webswing.server.api.GlobalUrlHandler;
import org.webswing.server.api.base.PrimaryUrlHandler;

public interface RestHandlerFactory {

  AbstractGlobalRestHandler createGlobalRestHandler(GlobalUrlHandler parent);

  AbstractAppRestHandler createAppRestHandler(PrimaryUrlHandler parent, GlobalUrlHandler global);

}
