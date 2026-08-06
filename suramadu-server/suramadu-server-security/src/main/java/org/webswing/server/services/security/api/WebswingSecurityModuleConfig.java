package org.webswing.server.services.security.api;

import org.webswing.server.common.model.Config;

/**
 * Configuration and context holder for {@link WebswingSecurityModule} implementations. This
 * interface is dynamically instantiated by Webswing to provide easy access to JSON configuration
 * values stored in webswing.config file. {@link WebswingSecurityModule} implementations should
 * extend this interface with their own specific java beans getters, to read configuration options
 * from JSON format.
 */
public interface WebswingSecurityModuleConfig extends Config {
  /**
   * Context of for {@link WebswingSecurityModule}. Used to resolve web resources or local files.
   * 
   * @return the Context implementation
   */
  SecurityContext getContext();

}
