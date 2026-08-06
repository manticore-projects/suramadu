package org.webswing.toolkit.api;

import java.io.Serial;
import java.io.Serializable;

/**
 * Exception in API call processing
 */
public class WebswingApiException extends Exception implements Serializable {

  @Serial
  private static final long serialVersionUID = 4690618533706576723L;

  public WebswingApiException() {
    super();
  }

  public WebswingApiException(String msg) {
    super(msg);
  }

  public WebswingApiException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
