package org.webswing.toolkit.api.security;

import java.io.Serializable;
import java.util.Map;

/**
 * Representation of user logged-in to Webswing through web interface.
 */
public interface WebswingUser {

  /**
   * @return Unique user id
   */
  String getUserId();

  /**
   * @return map of user attributes specific to configured security module implementation.
   */
  Map<String, Serializable> getUserAttributes();

}
