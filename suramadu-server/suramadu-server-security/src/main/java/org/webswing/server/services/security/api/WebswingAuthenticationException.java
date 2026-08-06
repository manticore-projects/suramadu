package org.webswing.server.services.security.api;

import java.io.Serial;

/**
 * Exception thrown when user credentials are not valid or can not be verified.
 */
public class WebswingAuthenticationException extends Exception {
  @Serial
  private static final long serialVersionUID = 5376492025474474131L;

  public static final String INVALID_USER_OR_PASSWORD = "login.invalidUserPassword";
  public static final String FAILED_TO_AUTHENTICATE = "login.failedToAuthenticate";
  public static final String SERVER_NOT_AVAILABLE = "login.serverNotAvailable";
  public static final String UNEXPECTED_ERROR = "login.unexpectedError";
  public static final String NO_ACCESS = "login.noAccessMessage";
  public static final String CONFIG_ERROR = "login.configurationError";

  String localizationKey;

  public WebswingAuthenticationException(String message) {
    super(message);
  }

  public WebswingAuthenticationException(String message, Throwable t) {
    super(message, t);
  }

  public WebswingAuthenticationException(String message, String localizedKey) {
    super(message);
    this.localizationKey = localizedKey;
  }

  public WebswingAuthenticationException(String message, String localizedKey, Throwable t) {
    super(message, t);
    this.localizationKey = localizedKey;
  }

  @Override
  public String getLocalizedMessage() {
    return localizationKey == null ? super.getLocalizedMessage() : ("${" + localizationKey + "}");
  }
}
