package org.webswing.server.common.model.meta;

import java.io.File;

public interface ConfigContext {

  /**
   * Search for resource using in application's home folder if path is relative and check for
   * existence.
   * 
   * @param name name of resource.
   * @return existing file or null
   */
  File resolveFile(String name);

  /**
   * Replace variables in in form <code>${variableName}</code> with their respective value from
   * environment variables or system properties
   * 
   * @param string string with variables
   * @return string with variables replaced
   */
  String replaceVariables(String string);

}
