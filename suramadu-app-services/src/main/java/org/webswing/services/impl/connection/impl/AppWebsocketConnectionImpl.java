package org.webswing.services.impl.connection.impl;

import jakarta.websocket.*;
import org.webswing.Constants;
import org.webswing.model.SyncObjectResponse;
import org.webswing.model.app.in.ServerToAppFrameMsgIn;
import org.webswing.model.app.out.AppHandshakeMsgOut;
import org.webswing.model.app.out.AppToServerFrameMsgOut;
import org.webswing.model.appframe.in.AppFrameMsgIn;
import org.webswing.model.common.in.RecordingStatusEnum;
import org.webswing.server.common.util.JwtUtil;
import org.webswing.services.impl.connection.ServerConnection;
import org.webswing.toolkit.util.Util;
import org.webswing.util.AppLogger;
import org.webswing.util.ClassLoaderUtil;
import org.webswing.util.ProtoMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

@ClientEndpoint
public class AppWebsocketConnectionImpl implements ServerConnection {

  private static int maxReconnectRetries = 5;
  private static int maxMessageSize = Integer.getInteger(Constants.WEBSOCKET_MESSAGE_SIZE,
      Constants.WEBSOCKET_MESSAGE_SIZE_DEFAULT_VALUE);
  private static long messageTimeout = Long.getLong(Constants.WEBSOCKET_MESSAGE_TIMEOUT,
      Constants.WEBSOCKET_MESSAGE_TIMEOUT_DEFAULT);
  private static long syncTimeout = Long.getLong(Constants.SWING_START_SYS_PROP_SYNC_TIMEOUT,
      Constants.SWING_START_SYS_PROP_SYNC_TIMEOUT_DEFAULT_VALUE);

  private ProtoMapper protoMapper = new ProtoMapper(ProtoMapper.PROTO_PACKAGE_SERVER_APP_FRAME,
      ProtoMapper.PROTO_PACKAGE_SERVER_APP_FRAME, ClassLoaderUtil.getServiceClassLoader());

  private String serverUrl;
  private MessageListener messageListener;
  private Session session;

  private ByteArrayOutputStream partialMsg = new ByteArrayOutputStream();

  private Map<String, SyncObjectResponse> syncCallResposeMap =
      Collections.synchronizedMap(new ConcurrentHashMap<>());

  private Timer reconnectTimer = new Timer(true);
  private AtomicBoolean reconnectScheduled = new AtomicBoolean(false);
  private AtomicBoolean interruptReconnect = new AtomicBoolean(false);

  private Object sendLock = new Object();

  public AppWebsocketConnectionImpl() {}

  public void initialize(String serverUrl, MessageListener messageListener) throws Exception {
    this.serverUrl = serverUrl;
    this.messageListener = messageListener;
    resetTimer();

    connect();
  }

  private void connect() throws Exception {
    if (reconnectScheduled.get()) {
      resetTimer();
    }

    AppLogger.info("Starting websocket connection to server [" + serverUrl + "].");
    try {
      WebSocketClient.getClient().connectToServer(this, URI.create(serverUrl));
    } catch (Exception e) {
      AppLogger.error("Failed to connect websocket to server [" + serverUrl + "]!", e.getMessage());
      AppLogger.debug(e.getMessage(), e);
      throw e;
    }
  }

  @OnOpen
  public void onOpen(Session session) throws Exception {
    AppLogger.info("Websocket connection opened to server [" + serverUrl + "].");

    this.session = session;

    session.setMaxBinaryMessageBufferSize(maxMessageSize);
    session.getAsyncRemote().setSendTimeout(messageTimeout);

    AppHandshakeMsgOut handshake = new AppHandshakeMsgOut();

    try {
      String secretMessage = JwtUtil.createHandshakeToken();
      handshake.setSecretMessage(secretMessage);
    } catch (Exception e) {
      AppLogger.error("Could not create secret message! Disconnecting...", e);
      disconnect(CloseReason.CloseCodes.CANNOT_ACCEPT, "Connection not secured!");
      throw e;
    }

    AppToServerFrameMsgOut msgOut = new AppToServerFrameMsgOut();
    msgOut.setHandshake(handshake);

    sendMessage(msgOut);
  }

  @OnMessage
  public void onMessage(Session session, byte[] bytes, boolean last) {
    if (bytes == null) {
      return;
    }

    try {
      partialMsg.write(bytes);
      if (last) {
        ServerToAppFrameMsgIn msgIn =
            protoMapper.decodeProto(partialMsg.toByteArray(), ServerToAppFrameMsgIn.class);
        if (msgIn != null) {
          messageListener.onMessage(msgIn);
        }
      }
    } catch (IOException e) {
      AppLogger.error("Could not decode proto message from server [" + serverUrl + "]!", e);
    } finally {
      if (last) {
        try {
          partialMsg.close();
        } catch (IOException e) {
          // ignore
        }
        partialMsg = new ByteArrayOutputStream();
      }
    }
  }

  @OnClose
  public void onClose(CloseReason closeReason) {
    AppLogger.info("Websocket closed to server [" + serverUrl + "]"
        + (closeReason != null
            ? ", close code [" + closeReason.getCloseCode().getCode() + "], reason ["
                + closeReason.getReasonPhrase() + "]!"
            : ""));

    if (!Util.getWebToolkit().getSessionWatchdog().isTerminated()) {
      scheduleReconnect(0);
    }
  }

  private void scheduleReconnect(int retry) {
    if (retry >= maxReconnectRetries) {
      return;
    }

    if (reconnectScheduled.get()) {
      resetTimer();
    } else {
      interruptReconnect.set(false);
    }

    reconnectTimer.schedule(new TimerTask() {
      @Override
      public void run() {
        reconnectScheduled.set(false);
        if (interruptReconnect.get()) {
          interruptReconnect.set(false);
          return;
        }
        try {
          connect();
        } catch (Exception e) {
          scheduleReconnect(retry + 1);
        }
      }
    }, 1000);
  }

  private void resetTimer() {
    reconnectTimer.cancel();
    reconnectTimer = new Timer(true);
    interruptReconnect.set(true);
  }

  @OnError
  public void onError(Session session, Throwable t) {
    AppLogger.error("Websocket error on server [" + serverUrl + "]!", t.getMessage());
    AppLogger.debug(t.getMessage(), t);
  }

  @Override
  public void handleSyncMessageResult(ServerToAppFrameMsgIn msgIn, AppFrameMsgIn frame) {
    String correlationId = null;
    if (msgIn.getApiCallResult() != null && msgIn.getApiCallResult().getCorrelationId() != null) {
      correlationId = msgIn.getApiCallResult().getCorrelationId();
    } else if (frame.getJsResponse() != null && frame.getJsResponse().getCorrelationId() != null) {
      correlationId = frame.getJsResponse().getCorrelationId();
    } else if (frame.getJavaRequest() != null
        && frame.getJavaRequest().getCorrelationId() != null) {
      correlationId = frame.getJavaRequest().getCorrelationId();
    } else if (frame.getPixelsResponse() != null
        && frame.getPixelsResponse().getCorrelationId() != null) {
      correlationId = frame.getPixelsResponse().getCorrelationId();
    }

    if (syncCallResposeMap.containsKey(correlationId)) {
      Object syncObject = syncCallResposeMap.get(correlationId);
      syncCallResposeMap.put(correlationId, new SyncObjectResponse(msgIn, frame));
      synchronized (syncObject) {
        syncObject.notifyAll();
      }
    } else {
      AppLogger.warn("No thread waiting for sync-ed message with id " + correlationId);
    }
  }

  @Override
  public void sendMessage(final AppToServerFrameMsgOut msgOut) {
    if (session == null || !session.isOpen()) {
      AppLogger.debug("Cannot send message, session closed!");
      return;
    }

    if (Util.getWebToolkit().getRecordingStatus() == RecordingStatusEnum.RECORDING
        && msgOut.getAppFrameMsgOut() != null) {
      Util.getWebToolkit().recordFrame(msgOut.getAppFrameMsgOut());
    }

    try {
      byte[] encoded = protoMapper.encodeProto(msgOut);
      synchronized (sendLock) {
        session.getAsyncRemote().sendBinary(ByteBuffer.wrap(encoded)).get();
      }
    } catch (IOException | InterruptedException | ExecutionException e) {
      AppLogger.error(
          "Error sending msg to server [" + serverUrl + "] , session [" + session.getId() + "]",
          e.getMessage());
      AppLogger.debug(e.getMessage(), e);
    }
  }

  @Override
  public SyncObjectResponse sendMessageSync(AppToServerFrameMsgOut msgOut, String correlationId)
      throws TimeoutException {
    SyncObjectResponse syncObject = new SyncObjectResponse();
    syncCallResposeMap.put(correlationId, syncObject);
    sendMessage(msgOut);
    SyncObjectResponse response = null;
    try {
      synchronized (syncObject) {
        if (syncCallResposeMap.get(correlationId) == syncObject) {
          syncObject.wait(syncTimeout);
        }
      }
    } catch (InterruptedException e) {
    }

    response = syncCallResposeMap.get(correlationId);
    syncCallResposeMap.remove(correlationId);
    if (response == syncObject) {
      throw new TimeoutException(
          "Call timed out after " + syncTimeout + " ms. Call id " + correlationId);
    }
    return response;
  }

  @Override
  public void close() {
    close("Closing connection. Application shutdown.");
  }

  @Override
  public void close(String reason) {
    disconnect(CloseReason.CloseCodes.NORMAL_CLOSURE, reason);
  }

  private void disconnect(CloseReason.CloseCodes closeCode, String reason) {
    AppLogger.info("Disconnecting websocket to server [" + serverUrl + "].");
    if (session != null && session.isOpen()) {
      try {
        session.close(new CloseReason(closeCode, reason));
      } catch (IOException e) {
        AppLogger.error(
            "Failed to destroy websocket connection, session [" + session.getId() + "]!",
            e.getMessage());
        AppLogger.debug(e.getMessage(), e);
      }
    }
  }

}
