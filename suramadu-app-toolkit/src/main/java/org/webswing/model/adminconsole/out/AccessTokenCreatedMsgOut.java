package org.webswing.model.adminconsole.out;

import org.webswing.model.MsgOut;

import java.io.Serial;

public class AccessTokenCreatedMsgOut implements MsgOut {

  @Serial
  private static final long serialVersionUID = -7458405228520457102L;

  private String refreshToken;
  private String accessId;
  private Long expiration;

  public AccessTokenCreatedMsgOut() {}

  public AccessTokenCreatedMsgOut(String refreshToken, String accessToken, String accessId,
      Long expiration) {
    this.refreshToken = refreshToken;
    this.accessId = accessId;
    this.expiration = expiration;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public String getAccessId() {
    return accessId;
  }

  public void setAccessId(String accessId) {
    this.accessId = accessId;
  }

  public Long getExpiration() {
    return expiration;
  }

  public void setExpiration(Long expiration) {
    this.expiration = expiration;
  }

}
