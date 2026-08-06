package org.webswing.sessionpool.api.service.startup.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.server.model.exception.WsInitException;
import org.webswing.sessionpool.api.base.SessionPoolService;
import org.webswing.sessionpool.api.service.startup.SessionPoolStartupService;

import java.util.Set;

@Singleton
public class SessionPoolStartupServiceImpl implements SessionPoolStartupService {

  private static final Logger log = LoggerFactory.getLogger(SessionPoolStartupServiceImpl.class);

  Set<SessionPoolService> services;

  @Inject
  public SessionPoolStartupServiceImpl(Set<SessionPoolService> services) {
    this.services = services;
  }

  public void start() throws WsInitException {
    try {
      for (SessionPoolService service : services) {
        log.info("Starting session pool service {}", service.getClass().getSimpleName());
        service.start();
      }
    } catch (WsInitException e) {
      throw e;
    } catch (Exception e) {
      throw new WsInitException("Failed to start Webswing Session Pool. " + e.getMessage(), e);
    }
  }

  public void stop() {
    for (SessionPoolService service : services) {
      service.stop();
    }
  }
}
