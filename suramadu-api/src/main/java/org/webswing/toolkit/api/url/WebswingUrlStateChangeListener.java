package org.webswing.toolkit.api.url;

/**
 * URL Change Listener
 */
public interface WebswingUrlStateChangeListener {

  /**
   * Triggered when browser URL is changed
   * 
   * @param event new URL event
   */
  void onUrlStateChange(WebswingUrlStateChangeEvent event);
}
