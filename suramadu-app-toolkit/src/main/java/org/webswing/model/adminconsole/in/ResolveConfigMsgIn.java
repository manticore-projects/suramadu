package org.webswing.model.adminconsole.in;

import org.webswing.model.MsgIn;
import org.webswing.model.SyncMsg;

import java.io.Serial;
import java.util.UUID;

public class ResolveConfigMsgIn implements SyncMsg, MsgIn {

  @Serial
  private static final long serialVersionUID = -51242809130068878L;

  private String path;
  private String type;
  private String resolve;
  private String user;
  private String sessionPoolId;
  private String correlationId = UUID.randomUUID().toString();

  public ResolveConfigMsgIn() {}

  public ResolveConfigMsgIn(String path, String type, String resolve, String user,
      String sessionPoolId) {
    super();
    this.path = path;
    this.type = type;
    this.resolve = resolve;
    this.user = user;
    this.sessionPoolId = sessionPoolId;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getResolve() {
    return resolve;
  }

  public void setResolve(String resolve) {
    this.resolve = resolve;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public String getCorrelationId() {
    return correlationId;
  }

  public void setCorrelationId(String correlationId) {
    this.correlationId = correlationId;
  }

  public String getSessionPoolId() {
    return sessionPoolId;
  }

  public void setSessionPoolId(String sessionPoolId) {
    this.sessionPoolId = sessionPoolId;
  }

}
