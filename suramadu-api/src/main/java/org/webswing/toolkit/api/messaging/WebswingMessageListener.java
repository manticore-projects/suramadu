package org.webswing.toolkit.api.messaging;

/**
 * Interface implemented by Swing application to listen to messages of defined type
 * 
 * @param <T> Type of messages
 */
public interface WebswingMessageListener<T> {
  /**
   * This method is invoked when a message of compatible type is published to the topic
   * 
   * @param message wrapped message object
   */
  void onMessage(WebswingMessage<T> message);
}
