package org.webswing.server.api.services.startup.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.server.api.base.WebswingService;
import org.webswing.server.api.services.startup.StartupService;
import org.webswing.server.model.exception.WsInitException;

import java.util.Set;

@Singleton
public class DefaultStartupServiceImpl implements StartupService {

  private static final Logger log = LoggerFactory.getLogger(DefaultStartupServiceImpl.class);

  Set<WebswingService> services;

  @Inject
  public DefaultStartupServiceImpl(Set<WebswingService> services) {
    this.services = services;
  }

  public void start() throws WsInitException {
    try {
      for (WebswingService service : services) {
        log.info("Starting service {}", service.getClass().getSimpleName());
        service.start();
      }
    } catch (WsInitException e) {
      throw e;
    } catch (Exception e) {
      throw new WsInitException("Failed to start Webswing. " + e.getMessage(), e);
    }
  }

  public void stop() {
    for (WebswingService service : services) {
      service.stop();
    }
  }
}
