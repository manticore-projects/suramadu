package org.webswing.model.adminconsole.out;

import org.webswing.model.MsgOut;

import java.io.Serial;
import java.util.List;

public class ServerInfoMsgOut implements MsgOut {

  @Serial
  private static final long serialVersionUID = -562205063402702649L;

  private String id;
  private List<ApplicationInfoMsgOut> appInfos;
  private List<SessionPoolInfoMsgOut> spInfos;
  private Integer instances;
  private Integer users;
  private boolean cluster;

  public ServerInfoMsgOut() {}

  public ServerInfoMsgOut(String id, List<ApplicationInfoMsgOut> appInfos,
      List<SessionPoolInfoMsgOut> spInfos, Integer instances, Integer users, boolean cluster) {
    super();
    this.id = id;
    this.appInfos = appInfos;
    this.spInfos = spInfos;
    this.instances = instances;
    this.users = users;
    this.cluster = cluster;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public List<ApplicationInfoMsgOut> getAppInfos() {
    return appInfos;
  }

  public void setAppInfos(List<ApplicationInfoMsgOut> appInfos) {
    this.appInfos = appInfos;
  }

  public List<SessionPoolInfoMsgOut> getSpInfos() {
    return spInfos;
  }

  public void setSpInfos(List<SessionPoolInfoMsgOut> spInfos) {
    this.spInfos = spInfos;
  }

  public Integer getInstances() {
    return instances;
  }

  public void setInstances(Integer instances) {
    this.instances = instances;
  }

  public Integer getUsers() {
    return users;
  }

  public void setUsers(Integer users) {
    this.users = users;
  }

  public boolean isCluster() {
    return cluster;
  }

  public void setCluster(boolean cluster) {
    this.cluster = cluster;
  }

}
