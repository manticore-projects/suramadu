package org.webswing.server.services.security.api;

import java.util.List;

/**
 * Used in admin console to display list of available SecurityModules on classpath. (as Java SPI)
 */
public interface WebswingSecurityModuleProvider {

  List<String> getSecurityModuleClassNames();
}
