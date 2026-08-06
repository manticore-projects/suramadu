package org.webswing.toolkit.api.lifecycle;

import org.webswing.toolkit.api.WebswingApi;

public interface WebswingShutdownListener {

  /**
   * Invoked before Webswing requests application to exit. Do not execute long operations in this
   * listener - listener execution will be interrupted if blocking for &gt; 3 seconds and the
   * application will exit without delay. Connection to server is still open when this callback is
   * triggered. This method can delay the shutdown. Calling
   * {@link WebswingApi#resetInactivityTimeout()} within the delay period will cancel the shutdown
   * sequence.
   *
   * This method is not called on the event dispatch thread.
   *
   * @param event Event contains the reason of this shutdown - either triggered from Admin console's
   *        rest interface or by inactivity
   * @return number of seconds to delay the shutdown, returning 0 will cause shutdown without delay
   *         (even if {@link WebswingApi#resetInactivityTimeout()} has been called)
   */
  int onBeforeShutdown(OnBeforeShutdownEvent event);

  /**
   * Invoked when Webswing requests swing application to exit. This method should cause this process
   * to exit (not necessarily in the same thread). When this method is called, connection to server
   * is already closed
   *
   * This method is not called on the event dispatch thread.
   */
  void onShutdown();
}
