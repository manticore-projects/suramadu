package org.webswing.toolkit.api.security;

public class UserEvent {
  private WebswingUser user;

  public UserEvent(WebswingUser user) {
    super();
    this.user = user;
  }

  public WebswingUser getUser() {
    return user;
  }

  public void setUser(WebswingUser user) {
    this.user = user;
  }

}
