package org.webswing.server.api.services.websocket.impl;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.Constants;
import org.webswing.model.Msg;
import org.webswing.server.api.services.websocket.WebSocketConnection;
import org.webswing.server.common.util.JwtUtil;

import javax.websocket.EndpointConfig;
import javax.websocket.PongMessage;
import javax.websocket.Session;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public abstract class AbstractWebSocketConnection implements WebSocketConnection {
  private static final Logger log = LoggerFactory.getLogger(AbstractWebSocketConnection.class);

  private static final int maxMessageSize = Integer.getInteger(Constants.WEBSOCKET_MESSAGE_SIZE,
      Constants.WEBSOCKET_MESSAGE_SIZE_DEFAULT_VALUE);
  private static final long messageTimeout = Long.getLong(Constants.WEBSOCKET_MESSAGE_TIMEOUT,
      Constants.WEBSOCKET_MESSAGE_TIMEOUT_DEFAULT);

  private static final Timer pingTimer = new Timer("Websocket Ping Timer", true);

  private ByteArrayOutputStream partialMsg = new ByteArrayOutputStream();

  protected Session session;

  private final Object sendLock = new Object();

  protected void onOpen(Session session, EndpointConfig config) {
    this.session = session;
    session.setMaxBinaryMessageBufferSize(maxMessageSize);
    session.getAsyncRemote().setSendTimeout(messageTimeout);

    pingTimer.schedule(new TimerTask() {
      @Override
      public void run() {
        if (session != null && session.isOpen()) {
          try {
            synchronized (sendLock) {
              session.getAsyncRemote().sendPing(ByteBuffer
                  .wrap(Constants.WEBSOCKET_PING_PONG_CONTENT.getBytes(StandardCharsets.UTF_8)));
            }
          } catch (IllegalArgumentException | IOException e) {
            this.cancel();
          }
        } else {
          this.cancel();
        }
      }
    }, Constants.WEBSOCKET_PING_PONG_INTERVAL, Constants.WEBSOCKET_PING_PONG_INTERVAL);
  }

  protected Pair<Msg, Integer> getCompleteMessage(byte[] bytes, boolean last) throws IOException {
    if (bytes == null) {
      return null;
    }

    try {
      partialMsg.write(bytes);
      if (last) {
        byte[] array = partialMsg.toByteArray();

        return Pair.of(decodeIncomingMessage(array), array.length);
      }
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

    return null;
  }

  protected void sendMessage(byte[] encoded) throws IOException {
    try {
      synchronized (sendLock) {
        session.getAsyncRemote().sendBinary(ByteBuffer.wrap(encoded)).get();
      }
    } catch (IllegalStateException | InterruptedException | ExecutionException ex) {
      throw new IOException(ex.getMessage(), ex);
    }
  }

  protected void onPong(Session session, PongMessage pongMessage, Logger log) {
    ByteBuffer buffer = pongMessage.getApplicationData();
    if (buffer == null) {
      log.warn("Empty pong message received for session [{}]!", session.getId());
      return;
    }
    byte[] bytes = new byte[buffer.remaining()];
    buffer.get(bytes);

    String pong = new String(bytes, StandardCharsets.UTF_8);
    if (!Constants.WEBSOCKET_PING_PONG_CONTENT.equals(pong)) {
      log.warn("Error receiving pong message for session [{}], content received [{}]!",
          session.getId(), pong);
    }
  }

  protected boolean validateHandshakeToken(String secret) {
    try {
      if (!JwtUtil.validateHandshakeToken(secret)) {
        throw new IllegalArgumentException(
            "Invalid token [" + secret + "] received during handshake!");
      }
      return true;
    } catch (Exception e1) {
      log.error("Could not validate handshake secret message! Disconnecting...", e1);
      return false;
    }
  }

  protected abstract Msg decodeIncomingMessage(byte[] bytes) throws IOException;

  @Override
  public boolean isConnected() {
    return session != null && session.isOpen();
  }

}
