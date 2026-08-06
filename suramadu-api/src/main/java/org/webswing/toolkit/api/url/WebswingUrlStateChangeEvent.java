package org.webswing.toolkit.api.url;

/**
 * Event representing change of Url in browser.
 */
public interface WebswingUrlStateChangeEvent {

  /**
   * @return the new url in full form
   */
  String getFullUrl();

  /**
   * @return URL state parsed from the hash fragmet part of URL
   */
  WebswingUrlState getState();


  /**
   * @return Previous Url state
   */
  WebswingUrlState getOldState();
}
