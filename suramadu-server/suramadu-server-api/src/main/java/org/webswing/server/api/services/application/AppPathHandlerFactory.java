package org.webswing.server.api.services.application;

import org.webswing.server.api.GlobalUrlHandler;

public interface AppPathHandlerFactory {

  AppPathHandler createAppPathHandler(GlobalUrlHandler parent, String path);

}
