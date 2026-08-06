package org.webswing.model.adminconsole.in;

import org.webswing.model.MsgIn;
import org.webswing.model.SyncMsg;

import java.io.Serial;
import java.util.UUID;

public class SearchVariablesMsgIn implements SyncMsg, MsgIn {

  @Serial
  private static final long serialVersionUID = -51242809130068878L;

  private String path;
  private String type;
  private String search;
  private String user;
  private String sessionPoolId;
  private String correlationId = UUID.randomUUID().toString();

  public SearchVariablesMsgIn() {}

  public SearchVariablesMsgIn(String path, String type, String search, String user,
      String sessionPoolId) {
    this.path = path;
    this.type = type;
    this.search = search;
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

  public String getSearch() {
    return search;
  }

  public void setSearch(String search) {
    this.search = search;
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
