package org.webswing.server.api.services.resources;

import org.webswing.server.api.base.PrimaryUrlHandler;

public interface ResourceHandlerFactory {
  ResourceHandler create(PrimaryUrlHandler manager, WebResourceProvider webResourceProvider);
}
