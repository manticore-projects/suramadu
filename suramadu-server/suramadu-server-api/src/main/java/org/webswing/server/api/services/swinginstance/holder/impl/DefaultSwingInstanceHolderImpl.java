package org.webswing.server.api.services.swinginstance.holder.impl;

import org.webswing.server.api.services.swinginstance.ConnectedSwingInstance;
import org.webswing.server.api.services.swinginstance.holder.SwingInstanceHolder;
import org.webswing.server.api.services.swinginstance.holder.SwingInstanceSet;

import java.util.List;

public class DefaultSwingInstanceHolderImpl implements SwingInstanceHolder {

  private final SwingInstanceSet<ConnectedSwingInstance> runningInstances =
      new SwingInstanceSet<>();
  private final SwingInstanceSet<ConnectedSwingInstance> closedInstances = new SwingInstanceSet<>();

  @Override
  public ConnectedSwingInstance findInstanceByOwnerId(String ownerId) {
    return runningInstances.findByOwnerId(ownerId);
  }

  @Override
  public ConnectedSwingInstance findInstanceByInstanceId(String instanceId) {
    return runningInstances.findByInstanceId(instanceId);
  }

  @Override
  public ConnectedSwingInstance findClosedInstanceByInstanceId(String instanceId) {
    return closedInstances.findByInstanceId(instanceId);
  }

  @Override
  public List<ConnectedSwingInstance> getAllInstances() {
    return runningInstances.getAllInstances();
  }

  @Override
  public List<ConnectedSwingInstance> getAllClosedInstances() {
    return closedInstances.getAllInstances();
  }

  @Override
  public void add(ConnectedSwingInstance swingInstance) {
    runningInstances.add(swingInstance);
  }

  @Override
  public void remove(ConnectedSwingInstance swingInstance, boolean force) {
    if (!runningInstances.contains(swingInstance)) {
      return;
    }

    if (!force) {
      // force removed instance is not stored as a closed instance
      if (!closedInstances.contains(swingInstance)) {
        closedInstances.add(swingInstance);
      }
    }
    runningInstances.remove(swingInstance.getInstanceId());
  }

  @Override
  public int getRunningInstacesCount() {
    return runningInstances.size();
  }

  @Override
  public int getConnectedInstancesCount() {
    return (int) runningInstances.getAllInstances().stream()
        .filter(i -> i.getConnectionId() != null).count();
  }

  @Override
  public int getClosedInstacesCount() {
    return closedInstances.size();
  }

}
