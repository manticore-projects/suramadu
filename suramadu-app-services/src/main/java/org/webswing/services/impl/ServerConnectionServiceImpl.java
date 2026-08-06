package org.webswing.services.impl;

import org.webswing.Constants;
import org.webswing.ext.services.ServerConnectionService;
import org.webswing.model.SyncObjectResponse;
import org.webswing.model.app.in.ServerToAppFrameMsgIn;
import org.webswing.model.app.out.AppToServerFrameMsgOut;
import org.webswing.model.appframe.in.AppFrameMsgIn;
import org.webswing.model.appframe.out.AccessibilityMsgOut;
import org.webswing.model.appframe.out.AppFrameMsgOut;
import org.webswing.services.impl.connection.ServerConnection;
import org.webswing.services.impl.connection.impl.AppWebsocketConnectionImpl;
import org.webswing.toolkit.util.Util;
import org.webswing.util.AccessibilityUtil;
import org.webswing.util.AppLogger;
import org.webswing.util.ClassLoaderUtil;
import org.webswing.util.ProtoMapper;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.awt.Component;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Viktor_Meszaros This class is needed to achieve classpath isolation for swing
 *         application, all functionality dependent on external libs is implemented here.
 */
public class ServerConnectionServiceImpl
    implements ServerConnectionService, ServerConnection.MessageListener {

  private static ServerConnectionServiceImpl impl;

  private final ProtoMapper protoMapper = new ProtoMapper(ProtoMapper.PROTO_PACKAGE_APPFRAME_OUT,
      ProtoMapper.PROTO_PACKAGE_APPFRAME_IN, ClassLoaderUtil.getServiceClassLoader());

  private final ServerConnection connection;
  private final AtomicBoolean closed = new AtomicBoolean(false);
  private final List<QueuedMsg> startupMsgQueue = Collections.synchronizedList(new ArrayList<>());
  private String serverUrl = System.getProperty(Constants.SWING_START_SYS_PROP_WEBSOCKET_URL, "");

  public static ServerConnectionServiceImpl getInstance() {
    if (impl == null) {
      impl = new ServerConnectionServiceImpl();
    }
    return impl;
  }

  public ServerConnectionServiceImpl() {
    connection = new AppWebsocketConnectionImpl();
    AccessibilityUtil.registerAccessibilityListeners();
  }

  public void initialize() {
    initConnection(false);

    Util.getWebToolkit().addStartupListener(() -> {
      if (!startupMsgQueue.isEmpty()) {
        AppLogger.info("Dispatching " + startupMsgQueue.size() + " queued messages.");
        startupMsgQueue.stream().forEach(qm -> {
          onMessage(qm.msg);
        });
        startupMsgQueue.clear();
      }
    });

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      try {
        // stop recording
        Util.getWebToolkit().stopRecording();
      } catch (Exception sre) {
        sre.printStackTrace();
      }
      ServerConnectionServiceImpl.this.disconnect(Constants.APP_WEBSOCKET_CLOSE_REASON_SHUTDOWN);
    }));
  }

  private void initConnection(boolean reconnect) {
    try {
      serverUrl += "?instanceId=" + System.getProperty(Constants.SWING_START_SYS_PROP_INSTANCE_ID)
          + "&sessionPoolId=" + System.getProperty(Constants.SWING_START_SYS_PROP_SESSION_POOL_ID)
          + "&reconnect=" + reconnect;
      connection.initialize(serverUrl, this);
      closed.set(false);
    } catch (Exception e) {
      AppLogger.error("Exiting application because it could not connect to server", e);
      System.exit(1);
    }
  }

  @Override
  public void reconnect(String reconnectUrl) {
    disconnect(Constants.APP_WEBSOCKET_CLOSE_REASON_RECONNECT);
    serverUrl = reconnectUrl;
    initConnection(true);
  }

  private void disconnect(String reason) {
    try {
      connection.close(reason);
    } catch (Exception e) {
      AppLogger.info("Disconnecting from Server failed.", e.getMessage());
    } finally {
      closed.set(true);
    }
  }

  @Override
  public void sendObject(AppToServerFrameMsgOut msgOut, AppFrameMsgOut frame) {
    if (!closed.get()) {
      try {
        if (frame != null) {
          try {
            msgOut.setAppFrameMsgOut(protoMapper.encodeProto(frame));
          } catch (IOException e) {
            AppLogger.error("Could not encode proto!", e);
          }
        }
        connection.sendMessage(msgOut);
      } catch (Exception e) {
        AppLogger.error("ServerConnectionService.sendJsonObject", e);
      }
    }
  }

  @Override
  public SyncObjectResponse sendObjectSync(AppToServerFrameMsgOut msgOut, AppFrameMsgOut frame,
      String correlationId) throws Exception {
    if (!closed.get()) {
      try {
        if (frame != null) {
          try {
            msgOut.setAppFrameMsgOut(protoMapper.encodeProto(frame));
          } catch (IOException e) {
            AppLogger.error("Could not encode proto!", e);
            throw e;
          }
        }
        return connection.sendMessageSync(msgOut, correlationId);
      } catch (Exception e) {
        AppLogger.error("ServerConnectionService.sendJsonObject", e);
        throw e;
      }
    } else {
      throw new Exception("Failed to send request. Servie was disconnected.");
    }
  }

  @Override
  public AccessibilityMsgOut getAccessibilityInfo() {
    return AccessibilityUtil.getAccessibilityInfo();
  }

  @Override
  public AccessibilityMsgOut getAccessibilityInfo(Component c, int x, int y) {
    return AccessibilityUtil.getAccessibilityInfo(c, x, y);
  }

  @Override
  public void onMessage(ServerToAppFrameMsgIn msgIn) {
    if (Util.getWebToolkit().getEventDispatcher() == null) {
      startupMsgQueue.add(new QueuedMsg(msgIn));
      return;
    }

    AppFrameMsgIn frame = null;
    if (msgIn.getAppFrameMsgIn() != null) {
      try {
        frame = protoMapper.decodeProto(msgIn.getAppFrameMsgIn(), AppFrameMsgIn.class);
      } catch (IOException e) {
        AppLogger.error("Could not encode proto!", e);
      }
    }

    // first handle sync messages
    if ((msgIn.getApiCallResult() != null && msgIn.getApiCallResult().getCorrelationId() != null)
        || (frame != null && frame.getJsResponse() != null
            && frame.getJsResponse().getCorrelationId() != null)
        || (frame != null && frame.getPixelsResponse() != null
            && frame.getPixelsResponse().getCorrelationId() != null)) {
      connection.handleSyncMessageResult(msgIn, frame);
      return;
    }

    // handle other messages in event dispatcher
    Util.getWebToolkit().getEventDispatcher().onMessage(msgIn, frame);
  }

  @Override
  public Map<String, Serializable> deserializeUserAttributes(byte[] data) throws IOException {
    if (data == null) {
      return null;
    }
    try {
      return new JsonMapper().readValue(data, new TypeReference<Map<String, Serializable>>() {});
    } catch (JacksonException e) {
      throw new IOException("Failed to deserialize user attributes", e);
    }
  }

  private static class QueuedMsg {
    private ServerToAppFrameMsgIn msg;

    public QueuedMsg(ServerToAppFrameMsgIn msg) {
      super();
      this.msg = msg;
    }

  }

}
