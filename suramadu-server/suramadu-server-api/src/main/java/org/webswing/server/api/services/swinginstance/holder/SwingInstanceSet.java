package org.webswing.server.api.services.swinginstance.holder;

import org.webswing.server.api.services.swinginstance.RemoteSwingInstance;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SwingInstanceSet<T extends RemoteSwingInstance> {
  Set<T> instances = new HashSet<>();

  public synchronized void add(T swingInstance) {
    instances.add(swingInstance);
  }

  public boolean contains(T swingInstance) {
    return instances.contains(swingInstance);
  }

  public synchronized void remove(String instanceId) {
    instances.remove(findByInstanceId(instanceId));
  }

  public int size() {
    return instances.size();
  }

  public synchronized T findByInstanceId(String instanceId) {
    for (T i : instances) {
      if (instanceId != null && instanceId.equals(i.getInstanceId())) {
        return i;
      }
    }
    return null;
  }

  public T findByOwnerId(String ownerId) {
    for (T i : instances) {
      if (ownerId != null && ownerId.equals(i.getOwnerId())) {
        return i;
      }
    }
    return null;
  }

  public synchronized List<T> getAllInstances() {
    return new ArrayList<>(instances);
  }

}
