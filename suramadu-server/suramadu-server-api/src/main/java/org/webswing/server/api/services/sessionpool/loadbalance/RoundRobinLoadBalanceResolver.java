package org.webswing.server.api.services.sessionpool.loadbalance;

import org.webswing.server.api.services.sessionpool.ServerSessionPoolConnector;
import org.webswing.server.common.model.SecuredPathConfig;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

public class RoundRobinLoadBalanceResolver implements LoadBalanceResolver {

  private final Deque<ServerSessionPoolConnector> sessionPools = new ConcurrentLinkedDeque<>();

  @Override
  public ServerSessionPoolConnector resolveLoadBalance(String path, SecuredPathConfig config) {
    ServerSessionPoolConnector firstPool = null;

    synchronized (sessionPools) {
      ServerSessionPoolConnector sp = sessionPools.pollFirst();

      if (sp == null) {
        return null;
      }

      sessionPools.addLast(sp);

      while (sp != firstPool) {
        // break condition so that we do only one loop through all session pools

        if (firstPool == null) {
          firstPool = sp;
        }

        if (!sp.isDrainMode() && sp.accepts(path)) {
          return sp;
        }

        sp = sessionPools.pollFirst();
        sessionPools.addLast(sp);
      }
    }

    return null;
  }

  @Override
  public void registerSessionPool(ServerSessionPoolConnector sessionPool) {
    synchronized (sessionPools) {
      this.sessionPools.addLast(sessionPool);
    }
  }

  @Override
  public void unregisterSessionPool(ServerSessionPoolConnector sessionPool) {
    synchronized (sessionPools) {
      this.sessionPools.remove(sessionPool);
    }
  }

}
