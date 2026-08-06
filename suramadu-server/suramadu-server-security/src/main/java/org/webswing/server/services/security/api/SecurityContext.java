package org.webswing.server.services.security.api;

import org.webswing.server.common.model.meta.ConfigContext;

import java.net.URL;

/**
 * Context for {@link WebswingSecurityModule} stored in {@link WebswingSecurityModuleConfig} that
 * provides access to resources and Webswing specific services.
 */
public interface SecurityContext extends ConfigContext {

  /**
   * Returns URL of static web resource if exists.
   * 
   * @param resource resource path relative to web context
   * @return URL or null
   */
  URL getWebResource(String resource);

  /**
   * Retrieves object from session if exists.
   * 
   * @param attributeName name of session attribute
   * @return object if present in session
   */
  Object getFromSecuritySession(String attributeName);

  /**
   * Stores object in session.
   * 
   * @param attributeName name of attribute
   * @param value object to store
   */
  void setToSecuritySession(String attributeName, Object value);

  /**
   * @return the root URK path where the {@link WebswingSecurityModule} related to this context is
   *         used.
   */
  String getSecuredPath();
}
