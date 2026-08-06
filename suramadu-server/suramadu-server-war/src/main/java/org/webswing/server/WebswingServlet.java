package org.webswing.server;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.Constants;
import org.webswing.server.api.GlobalUrlHandler;
import org.webswing.server.api.services.sessionpool.SessionPoolHolderService;
import org.webswing.server.api.services.startup.StartupService;
import org.webswing.server.common.service.security.SecurityManagerService;
import org.webswing.server.services.sessionpool.impl.LocalSessionPoolConnector;
import org.webswing.util.GitRepositoryState;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URI;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

@WebServlet(asyncSupported = true, displayName = "WebswingServlet", urlPatterns = {"/*"})
@MultipartConfig(fileSizeThreshold = 5242880)
public class WebswingServlet extends HttpServlet {
  private static final Logger log = LoggerFactory.getLogger(StartupService.class);

  @Serial
  private static final long serialVersionUID = 1962501775857788874L;

  private StartupService startup;
  private GlobalUrlHandler handler;
  private SecurityManagerService securityManager;
  private LocalSessionPoolConnector sessionPool;

  @Override
  public void init() throws ServletException {
    log.info("Initializing Webswing {}", GitRepositoryState.getInstance().getDescribe());
    Module servletModule = new Module() {
      public void configure(Binder binder) {
        binder.bind(ServletContext.class).toInstance(getServletContext());
      }
    };

    try {
      Injector injector = Guice.createInjector(servletModule, new WebswingServerModule());

      initializeDefaultSystemProperties();

      this.startup = injector.getInstance(StartupService.class);
      this.startup.start();
      this.securityManager = injector.getInstance(SecurityManagerService.class);
      this.handler = injector.getInstance(GlobalUrlHandler.class);
      this.handler.init();

      // initialize local session pool
      SessionPoolHolderService sessionPoolHolder =
          injector.getInstance(SessionPoolHolderService.class);
      this.sessionPool = injector.getInstance(LocalSessionPoolConnector.class);
      sessionPoolHolder.registerSessionPool(this.sessionPool);
    } catch (Exception e) {
      log.error("Initialization of Webswing failed. ", e);
      destroy();
      throw new ServletException("Webswing failed to start!", e);
    }
  }

  private void initializeDefaultSystemProperties() {
    String propFileUri = System.getProperty(Constants.PROPERTIES_FILE_PATH);
    if (propFileUri != null) {
      try {
        File propFile = new File(URI.create(propFileUri));
        Properties p = new Properties(System.getProperties());
        try (InputStream propFileStream = new FileInputStream(propFile)) {
          p.load(propFileStream);
        }

        // set the system properties
        for (Map.Entry<Object, Object> prop : p.entrySet()) {
          if (!System.getProperties().containsKey(prop.getKey())) {
            System.getProperties().put(prop.getKey(), prop.getValue());
          }
        }
      } catch (Exception e) {
        log.error("Exception occurred during initialization of System Properties", e);
        System.exit(-1);
      }
    }

    System.setProperty(Constants.WEBSWING_SERVER_ID, UUID.randomUUID().toString());

    log.info("Starting webswing server with id [{}]...",
        System.getProperty(Constants.WEBSWING_SERVER_ID));

    if (StringUtils.isBlank(System.getProperty(Constants.WEBSWING_CONNECTION_SECRET))) {
      log.error("Missing {} system property!", Constants.WEBSWING_CONNECTION_SECRET);
      System.exit(-1);
    }

    if (StringUtils.equalsIgnoreCase(System.getProperty(Constants.WEBSWING_CONNECTION_SECRET),
        Constants.WEBSWING_CONNECTION_SECRET_DEFAULT)) {
      String msg = "Please change " + Constants.WEBSWING_CONNECTION_SECRET
          + " system property to a non-default value in production!";
      log.error(msg, new IllegalStateException(msg));
    }

    if (System.getProperty(Constants.SERVER_WEBSOCKET_URL) == null) {
      throw new RuntimeException(
          "Failed to initialized server! Missing " + Constants.SERVER_WEBSOCKET_URL + " property!");
    }
  }

  public void handleRequest(HttpServletRequest req, HttpServletResponse res) {
    securityManager.secure(handler, req, res);
  }

  @Override
  public void destroy() {
    if (this.sessionPool != null) {
      this.sessionPool.destroy();
    }
    if (this.handler != null) {
      this.handler.destroy();
    }
    if (this.startup != null) {
      this.startup.stop();
    }
    log.info("Webswing Server Stopped.");
  }

  @Override
  public void doHead(HttpServletRequest req, HttpServletResponse res)
      throws IOException, ServletException {
    handleRequest(req, res);
  }

  @Override
  public void doOptions(HttpServletRequest req, HttpServletResponse res)
      throws IOException, ServletException {
    handleRequest(req, res);
  }

  @Override
  public void doTrace(HttpServletRequest req, HttpServletResponse res)
      throws IOException, ServletException {
    handleRequest(req, res);
  }

  @Override
  public void doDelete(HttpServletRequest req, HttpServletResponse res)
      throws IOException, ServletException {
    handleRequest(req, res);
  }

  @Override
  public void doPut(HttpServletRequest req, HttpServletResponse res)
      throws IOException, ServletException {
    handleRequest(req, res);
  }

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse res)
      throws IOException, ServletException {
    handleRequest(req, res);
  }

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse res)
      throws IOException, ServletException {
    handleRequest(req, res);
  }

  @Override
  protected long getLastModified(HttpServletRequest req) {
    return handler.getLastModified(req);
  }

}
