package org.webswing.server.api.services.swinginstance.holder;

import org.webswing.server.api.services.swinginstance.ConnectedSwingInstance;

import java.util.List;

public interface SwingInstanceHolder {

  ConnectedSwingInstance findInstanceByOwnerId(String ownerId);

  ConnectedSwingInstance findInstanceByInstanceId(String instanceId);

  ConnectedSwingInstance findClosedInstanceByInstanceId(String instanceId);

  List<ConnectedSwingInstance> getAllInstances();

  List<ConnectedSwingInstance> getAllClosedInstances();

  void add(ConnectedSwingInstance swingInstance);

  void remove(ConnectedSwingInstance swingInstance, boolean force);

  int getRunningInstacesCount();

  int getConnectedInstancesCount();

  int getClosedInstacesCount();
}
