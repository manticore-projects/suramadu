package org.webswing.server.api.services.swinginstance;

public interface RemoteSwingInstance {

  String getOwnerId();

  String getInstanceId();

  String getConnectionId();

  void setConnectionId(String connectionId);

}
