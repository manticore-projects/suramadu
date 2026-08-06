package org.webswing.server.common.model;

import org.webswing.server.common.model.meta.ConfigType;

import java.util.Map;

@ConfigType
public interface Config {
  /**
   * Instantiates a dynamic object of type <code>clazz</code> as a view of JSON object stored under
   * Json property <code>name</code>.
   * 
   * @param name name of Json property
   * @param clazz interface type to be created
   * @return instance of <code>clazz</code>
   */
  <T> T getValueAs(String name, Class<T> clazz);

  /**
   * @return the source json object map
   */
  Map<String, Object> asMap();
}
