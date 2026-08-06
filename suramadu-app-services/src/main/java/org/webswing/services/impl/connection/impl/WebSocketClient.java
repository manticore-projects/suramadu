package org.webswing.services.impl.connection.impl;

import jakarta.websocket.DeploymentException;
import org.glassfish.tyrus.client.ClientManager;
import org.glassfish.tyrus.client.ClientProperties;
import org.glassfish.tyrus.client.SslContextConfigurator;
import org.glassfish.tyrus.client.SslEngineConfigurator;
import org.glassfish.tyrus.container.jdk.client.JdkClientContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.Constants;
import org.webswing.server.common.util.CommonUtil;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class WebSocketClient {
  private static final Logger log = LoggerFactory.getLogger(WebSocketClient.class);
  public static final String __OBFUSCATE = "OBF:";

  private static ClientManager client;

  static ClientManager getClient() {
    if (client == null) {
      client = ClientManager.createClient(JdkClientContainer.class.getName());
      client.getProperties().put(ClientProperties.SHARED_CONTAINER_IDLE_TIMEOUT, 0);

      SslContextConfigurator sslContextConfigurator = new SslContextConfigurator();
      String truststore = System.getProperty(Constants.WEBSOCKET_CLIENT_TRUSTSTORE);
      String truststoretype = System.getProperty(Constants.WEBSOCKET_CLIENT_TRUSTSTORE_TYPE);
      String truststorePwd = System.getProperty(Constants.WEBSOCKET_CLIENT_TRUSTSTORE_PWD);
      if (truststore != null) {
        try {
          truststore = CommonUtil.getValidFile(truststore).getCanonicalPath();
          sslContextConfigurator.setTrustStoreFile(truststore);
          if (truststoretype != null) {
            sslContextConfigurator.setTrustStoreType(truststoretype);
          }
          if (truststorePwd != null) {
            // expand password
            if (truststorePwd.startsWith(__OBFUSCATE)) {
              truststorePwd = deobfuscate(truststorePwd);
            }
            sslContextConfigurator.setTrustStorePassword(truststorePwd);
          }
        } catch (IOException e) {
          log.error("Failed to load truststore for Webswing server connection.", e);
        }
      }
      SslEngineConfigurator sslEngineConfigurator =
          new SslEngineConfigurator(sslContextConfigurator, true, false, false);
      sslEngineConfigurator.setHostVerificationEnabled(
          !Boolean.getBoolean(Constants.WEBSOCKET_CLIENT_HOSTNAME_VERIFIER_DISABLED));
      client.getProperties().put(ClientProperties.SSL_ENGINE_CONFIGURATOR, sslEngineConfigurator);

      String proxyUri = System.getProperty(Constants.WEBSOCKET_CLIENT_PROXY_URI);
      if (proxyUri != null) {
        client.getProperties().put(ClientProperties.PROXY_URI, proxyUri);
      }
    }
    return client;
  }

  public static String deobfuscate(String s) {
    if (s.startsWith(__OBFUSCATE)) {
      s = s.substring(4);
    }

    byte[] b = new byte[s.length() / 2];
    int l = 0;
    for (int i = 0; i < s.length(); i += 4) {
      if (s.charAt(i) == 'U') {
        i++;
        String x = s.substring(i, i + 4);
        int i0 = Integer.parseInt(x, 36);
        byte bx = (byte) (i0 >> 8);
        b[l++] = bx;
      } else {
        String x = s.substring(i, i + 4);
        int i0 = Integer.parseInt(x, 36);
        int i1 = i0 / 256;
        int i2 = i0 % 256;
        byte bx = (byte) ((i1 + i2 - 254) / 2);
        b[l++] = bx;
      }
    }

    return new String(b, 0, l, StandardCharsets.UTF_8);
  }

  public void connectToServer(Object annotatedConnectionHndler, String path) throws IOException {
    try {
      getClient().connectToServer(annotatedConnectionHndler, URI.create(path));
    } catch (DeploymentException e) {
      throw new IOException("Failed to connect to websocket", e);
    }
  }

}
