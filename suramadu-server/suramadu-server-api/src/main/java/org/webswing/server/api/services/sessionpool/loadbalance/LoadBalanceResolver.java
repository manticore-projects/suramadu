package org.webswing.server.api.services.sessionpool.loadbalance;

import org.webswing.server.api.services.sessionpool.ServerSessionPoolConnector;
import org.webswing.server.common.model.SecuredPathConfig;

public interface LoadBalanceResolver {

  /**
   * Decides in which session pool the new instance should be created.
   * 
   * @param path application path
   * @param securedPathConfig app config
   * @return id of the resolved session pool or null if not possible to create a new instance
   */
  public ServerSessionPoolConnector resolveLoadBalance(String path,
      SecuredPathConfig securedPathConfig);

  public void registerSessionPool(ServerSessionPoolConnector sessionPool);

  public void unregisterSessionPool(ServerSessionPoolConnector sessionPool);

}
