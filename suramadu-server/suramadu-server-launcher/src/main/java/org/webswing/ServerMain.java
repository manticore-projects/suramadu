package org.webswing;

import org.conscrypt.Conscrypt;
import org.eclipse.jetty.alpn.server.ALPNServerConnectionFactory;
import org.eclipse.jetty.compression.server.CompressionHandler;
// Jetty 12 EE8
import org.eclipse.jetty.ee8.webapp.WebAppContext;
import org.eclipse.jetty.http2.server.HTTP2ServerConnectionFactory;
// Jetty 12 core
import org.eclipse.jetty.server.ConnectionFactory;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.LowResourceMonitor;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.ErrorHandler;
import org.eclipse.jetty.util.StringUtil;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.webswing.util.AppLogger;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSessionContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.URI;
import java.nio.channels.SocketChannel;
import java.security.KeyStore;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;

public class ServerMain {

  // -------- Stability tuning for flaky VPN (Netskope TH <-> NG) --------
  private static final long HTTP_IDLE_TIMEOUT_MS = 300_000L; // 5 min
  private static final long H2_STREAM_IDLE_TIMEOUT_MS = 300_000L;
  private static final long STOP_TIMEOUT_MS = 30_000L; // graceful drain
  private static final int ACCEPT_QUEUE_SIZE = 512;

  // Per-socket TCP keepalive — must be shorter than Netskope/firewall idle reaper
  private static final int TCP_KEEPIDLE_SEC = 45;
  private static final int TCP_KEEPINTVL_SEC = 15;
  private static final int TCP_KEEPCOUNT = 9;

  // TLS session resumption — when a connection IS killed, resumption avoids
  // a full handshake (1-RTT instead of 2-RTT, no key exchange).
  private static final int TLS_SESSION_CACHE_SIZE = 10_000;
  private static final int TLS_SESSION_TIMEOUT_SEC = 86_400; // 24h

  // Thread pool sized for many parallel Webswing sessions
  private static final int MAX_THREADS = 800;
  private static final int MIN_THREADS = 50;
  private static final int THREAD_IDLE_TIMEOUT_MS = 60_000;

  static Server server;

  public static void main(String[] args) throws Exception {
    // Register Conscrypt (BoringSSL) as the primary TLS provider
    // — JSSE rejects underscores in SNI hostnames, Conscrypt does not.
    Security.insertProviderAt(Conscrypt.newProvider(), 1);

    Configuration config = ConfigurationImpl.parse(args);
    System.out.println(config.toString());
    System.setProperty(Constants.SERVER_EMBEDED_FLAG, "true");
    System.setProperty(Constants.SERVER_PORT, config.getHttpPort());
    System.setProperty(Constants.SERVER_HOST, config.getHost());
    System.setProperty(Constants.SERVER_CONTEXT_PATH, config.getContextPath());
    boolean isHttpsOnly = config.isHttps() && !config.isHttp();
    System.setProperty(Constants.HTTPS_ONLY,
        System.getProperty(Constants.HTTPS_ONLY, "" + isHttpsOnly));
    System.setProperty(Constants.SERVER_WEBSOCKET_URL, buildWebsocketUrl(config));
    if (config.getServerId() != null) {
      System.setProperty(Constants.WEBSWING_SERVER_ID, config.getServerId());
    }

    if (config.getConfigFile() != null) {
      File configFile = ConfigFileResolver.resolveExplicit(config.getConfigFile());
      if (configFile.exists()) {
        System.setProperty(Constants.CONFIG_FILE_PATH, configFile.toURI().toString());
      } else {
        AppLogger.error("Webswing configuration file " + config.getConfigFile()
            + " not found. Using default location.");
      }
    }
    if (config.getPropertiesFile() != null) {
      File propFile = new File(config.getPropertiesFile());
      if (propFile.exists()) {
        System.setProperty(Constants.PROPERTIES_FILE_PATH, propFile.toURI().toString());
      } else {
        AppLogger.error("Webswing properties file " + config.getPropertiesFile()
            + " not found. Using default location.");
      }
    }

    // -------- Thread pool --------
    QueuedThreadPool threadPool = new QueuedThreadPool(MAX_THREADS, MIN_THREADS);
    threadPool.setName("jetty");
    threadPool.setIdleTimeout(THREAD_IDLE_TIMEOUT_MS);
    threadPool.setDetailedDump(false);

    server = new Server(threadPool);
    server.setStopAtShutdown(true);
    server.setStopTimeout(STOP_TIMEOUT_MS);

    int cpus = Runtime.getRuntime().availableProcessors();
    int acceptors = Math.max(2, cpus / 4);
    int selectors = Math.max(2, cpus / 2);

    List<Connector> connectors = new ArrayList<Connector>();

    // -------- Plain HTTP connector --------
    if (config.isHttp()) {
      HttpConfiguration http_config = baseHttpConfiguration();
      if (config.isHttps()) {
        http_config.setSecurePort(Integer.parseInt(config.getHttpsPort()));
      }
      ServerConnector http = newHardenedConnector(server, acceptors, selectors,
          new HttpConnectionFactory(http_config));
      http.setPort(Integer.parseInt(config.getHttpPort()));
      http.setHost(config.getHost());
      http.setIdleTimeout(HTTP_IDLE_TIMEOUT_MS);
      http.setAcceptQueueSize(ACCEPT_QUEUE_SIZE);
      http.setShutdownIdleTimeout(5_000);
      http.setReuseAddress(true);
      connectors.add(http);
    }

    // -------- HTTPS connector: HTTP/1.1 + HTTP/2 over TCP --------
    if (config.isHttps()) {
      if (config.getTruststore() != null && !config.getTruststore().isEmpty()
          && config.getKeystore() != null && config.getKeystore().isEmpty()) {
        AppLogger.error(
            "SSL configuration is invalid. Please specify the location of truststore and keystore files.");
      } else {
        File keyStoreFile = config.resolveConfigFile(config.getKeystore());
        File trustStoreFile = config.resolveConfigFile(config.getTruststore());
        if (!trustStoreFile.exists()) {
          AppLogger.error("SSL configuration is invalid. Truststore file "
              + trustStoreFile.getAbsolutePath() + " does not exist.");
        } else if (!keyStoreFile.exists()) {
          AppLogger.error("SSL configuration is invalid. Keystore file "
              + keyStoreFile.getAbsolutePath() + " does not exist.");
        } else {
          // Build SSLContext via Conscrypt — used by H1+H2 over TCP
          KeyStore ks = KeyStore.getInstance("JKS");
          try (FileInputStream fis = new FileInputStream(keyStoreFile)) {
            ks.load(fis, config.getKeystorePassword().toCharArray());
          }

          KeyStore ts = KeyStore.getInstance("JKS");
          try (FileInputStream fis = new FileInputStream(trustStoreFile)) {
            ts.load(fis, config.getTruststorePassword().toCharArray());
          }

          KeyManagerFactory kmf =
              KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
          kmf.init(ks, config.getKeystorePassword().toCharArray());

          TrustManagerFactory tmf =
              TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
          tmf.init(ts);

          SSLContext sslContext = SSLContext.getInstance("TLS", "Conscrypt");
          sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

          // TLS session resumption on the Conscrypt context — when a connection
          // IS killed, reconnect is a 1-RTT abbreviated handshake instead of full.
          SSLSessionContext serverSessionContext = sslContext.getServerSessionContext();
          if (serverSessionContext != null) {
            serverSessionContext.setSessionCacheSize(TLS_SESSION_CACHE_SIZE);
            serverSessionContext.setSessionTimeout(TLS_SESSION_TIMEOUT_SEC);
          }

          SslContextFactory.Server sslContextFactory = new SslContextFactory.Server();
          sslContextFactory.setSslContext(sslContext);
          sslContextFactory.setSniRequired(false);
          sslContextFactory.setNeedClientAuth(config.isClientAuthEnabled());
          sslContextFactory.setRenegotiationAllowed(true);

          HttpConfiguration https_config = baseHttpConfiguration();

          SecureRequestCustomizer src = new SecureRequestCustomizer();
          /* SNI (Server Name Indication) is a TLS extension where the browser sends
           * the hostname during the handshake. Jetty 12 has strict SNI checking
           * enabled by default — disable it for hostnames with underscores. */
          src.setSniHostCheck(false);
          https_config.addCustomizer(src);

          // -------- TCP connector: HTTP/1.1 + HTTP/2 via ALPN --------
          HttpConnectionFactory http11 = new HttpConnectionFactory(https_config);

          HTTP2ServerConnectionFactory h2 = new HTTP2ServerConnectionFactory(https_config);
          h2.setStreamIdleTimeout(H2_STREAM_IDLE_TIMEOUT_MS);
          h2.setMaxConcurrentStreams(256);
          h2.setInitialSessionRecvWindow(8 * 1024 * 1024);
          h2.setInitialStreamRecvWindow(2 * 1024 * 1024);

          ALPNServerConnectionFactory alpn = new ALPNServerConnectionFactory();
          alpn.setDefaultProtocol(http11.getProtocol());

          SslConnectionFactory sslConnectionFactory =
              new SslConnectionFactory(sslContextFactory, alpn.getProtocol());

          ServerConnector https = newHardenedConnector(server, acceptors, selectors,
              sslConnectionFactory, alpn, h2, http11);
          https.setPort(Integer.parseInt(config.getHttpsPort()));
          https.setHost(config.getHost());
          https.setIdleTimeout(HTTP_IDLE_TIMEOUT_MS);
          https.setAcceptQueueSize(ACCEPT_QUEUE_SIZE);
          https.setShutdownIdleTimeout(5_000);
          https.setReuseAddress(true);
          connectors.add(https);
        }
      }
    }

    server.setConnectors(connectors.toArray(new Connector[0]));

    // -------- Low-resource monitor --------
    // Under FD/thread/connection pressure, shorten idle timeouts so misbehaving
    // clients are reaped quickly to free capacity for healthy ones.
    LowResourceMonitor lowResources = new LowResourceMonitor(server);
    lowResources.setPeriod(2_000);
    lowResources.setLowResourcesIdleTimeout(30_000);
    lowResources.setMonitorThreads(true);
    lowResources.setMaxMemory(0);
    // 12.x: setMaxConnections was removed and replaced with a pluggable
    // LowResourceCheck strategy. Add the connection-count check additively
    // so we don't clobber any default checks Jetty installs.
    java.util.Set<LowResourceMonitor.LowResourceCheck> checks =
        new java.util.HashSet<>(lowResources.getLowResourceChecks());
    checks.add(lowResources.new MaxConnectionsLowResourceCheck(20_000));
    lowResources.setLowResourceChecks(checks);
    server.addBean(lowResources);

    WebAppContext webapp = new WebAppContext();
    webapp.setContextPath(System.getProperty(Constants.SERVER_CONTEXT_PATH, "/"));
    webapp.setWar(System.getProperty(Constants.WAR_FILE_LOCATION));
    webapp.setTempDirectory(new File(URI.create(System.getProperty(Constants.TEMP_DIR_PATH))));
    webapp.setPersistTempDirectory(true);
    webapp.setAttribute("org.eclipse.jetty.server.webapp.WebInfIncludeJarPattern",
        ".*/suramadu-server-api-[^/]*\\.jar$");

    // -------- WebSocket idle hint --------
    // For jetty-ee8-websocket-javax-server. May or may not be honored depending
    // on Webswing version — if WS sessions still drop after 30–60s, the timeout
    // is being set inside the WAR's ServerEndpointConfig and you'll need to
    // patch it there (javax.websocket Session.setMaxIdleTimeout, 0 = no limit).
    webapp.setAttribute("org.eclipse.jetty.ee8.websocket.javax.maxIdleTime", HTTP_IDLE_TIMEOUT_MS);

    CompressionHandler compressionHandler = new CompressionHandler();
    compressionHandler.setHandler(webapp);

    server.setHandler(compressionHandler);

    // Hide server version in error pages (pen test requirement)
    ErrorHandler errorHandler = new ErrorHandler();
    errorHandler.setShowStacks(false);
    errorHandler.setShowMessageInTitle(false);
    server.setErrorHandler(errorHandler);

    try {
      server.start();
      server.join();
    } catch (Exception e) {
      AppLogger.error("Webswing Server initialization failed. Stopping the server.", e);
      server.stop();
    }
    server = null;
  }

  /** Common HttpConfiguration for both http and https connectors. */
  private static HttpConfiguration baseHttpConfiguration() {
    HttpConfiguration cfg = new HttpConfiguration();
    cfg.setSendServerVersion(false);
    cfg.setRequestHeaderSize(Integer.getInteger(Constants.JETTY_REQUEST_HEADER_SIZE,
        Constants.JETTY_REQUEST_HEADER_SIZE_DEFAULT));
    cfg.setResponseHeaderSize(Integer.getInteger(Constants.JETTY_REQUEST_HEADER_SIZE,
        Constants.JETTY_REQUEST_HEADER_SIZE_DEFAULT));
    cfg.setIdleTimeout(HTTP_IDLE_TIMEOUT_MS);
    // Larger output buffers help slow consumers — server can flush more in one
    // shot rather than blocking per-write.
    cfg.setOutputBufferSize(64 * 1024);
    cfg.setOutputAggregationSize(32 * 1024);
    return cfg;
  }

  /**
   * ServerConnector that enables SO_KEEPALIVE plus Linux/macOS-specific TCP keepalive timing on
   * every accepted socket. Most important change for surviving Netskope/middlebox idle reaping.
   *
   * <p>
   * Complements (does not replace) host-level tuning. On the server:
   * 
   * <pre>
   *   # /etc/sysctl.d/99-suramadu-vpn.conf
   *   net.ipv4.tcp_keepalive_time   = 45
   *   net.ipv4.tcp_keepalive_intvl  = 15
   *   net.ipv4.tcp_keepalive_probes = 9
   *   net.ipv4.tcp_user_timeout     = 300000   # 5 min
   *   net.core.somaxconn            = 4096
   *   net.ipv4.tcp_fin_timeout      = 30
   *   net.ipv4.tcp_tw_reuse         = 1
   * </pre>
   */
  private static ServerConnector newHardenedConnector(Server server, int acceptors, int selectors,
      ConnectionFactory... factories) {
    return new ServerConnector(server, null, null, null, acceptors, selectors, factories) {
      @Override
      protected void configure(Socket socket) {
        super.configure(socket);
        // SO_KEEPALIVE — portable, enables kernel keepalive on this socket
        try {
          socket.setKeepAlive(true);
        } catch (SocketException ignored) {
        }
        // Disable Nagle — for an interactive Webswing session, latency on
        // small frames matters more than throughput. (Jetty also sets this
        // by default via setAcceptedTcpNoDelay; harmless to set again.)
        try {
          socket.setTcpNoDelay(true);
        } catch (SocketException ignored) {
        }
        // Per-socket keepalive timing — Linux/macOS only, via the underlying
        // SocketChannel and jdk.net.ExtendedSocketOptions. On Windows or
        // older JDKs these throw UnsupportedOperationException; system-wide
        // sysctl values then apply.
        SocketChannel sc = socket.getChannel();
        if (sc != null) {
          try {
            sc.setOption(jdk.net.ExtendedSocketOptions.TCP_KEEPIDLE, TCP_KEEPIDLE_SEC);
          } catch (Exception ignored) {
          }
          try {
            sc.setOption(jdk.net.ExtendedSocketOptions.TCP_KEEPINTERVAL, TCP_KEEPINTVL_SEC);
          } catch (Exception ignored) {
          }
          try {
            sc.setOption(jdk.net.ExtendedSocketOptions.TCP_KEEPCOUNT, TCP_KEEPCOUNT);
          } catch (Exception ignored) {
          }
        }
      }
    };
  }

  private static String buildWebsocketUrl(Configuration config) {
    boolean isHttpsOnly = config.isHttps() && !config.isHttp();
    String host = config.getHost();
    if ("0.0.0.0".equals(host)) {
      host = "127.0.0.1";
    }
    return (isHttpsOnly ? "wss://" : "ws://") + host + getPortString(config)
        + config.getContextPath();
  }

  private static String getPortString(Configuration config) {
    boolean isHttpsOnly = config.isHttps() && !config.isHttp();
    String port = isHttpsOnly ? config.getHttpsPort() : config.getHttpPort();
    return StringUtil.isBlank(port) ? "" : ":" + port;
  }

  public static void stopServer() {
    if (server != null) {
      try {
        server.stop();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
