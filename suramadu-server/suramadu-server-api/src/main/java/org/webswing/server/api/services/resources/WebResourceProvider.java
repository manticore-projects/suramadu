package org.webswing.server.api.services.resources;

import java.net.URL;

public interface WebResourceProvider {

  public abstract URL getWebResource(String resource);

}
