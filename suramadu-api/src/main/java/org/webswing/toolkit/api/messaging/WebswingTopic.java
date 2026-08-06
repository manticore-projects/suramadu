package org.webswing.toolkit.api.messaging;

import java.io.IOException;

/**
 * Reference to a topic filtered by message type
 * 
 * @param <T> message type
 */
public interface WebswingTopic<T> {

  /**
   * Publish a message to all subscribed webswing sessions.
   * 
   * @param message the message
   * @throws IOException if failed to send the message
   */
  void publish(T message) throws IOException;

  /**
   * Subscribe for receiving messages of defined type
   * 
   * @param listener the listener
   */
  void subscribe(WebswingMessageListener<T> listener);


  /**
   * Unsubscribe from receiving messages of defined type
   * 
   * @param listener the listener
   */
  void unsubscribe(WebswingMessageListener<T> listener);
}
