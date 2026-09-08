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
import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantLock;

public abstract class AbstractWebSocketConnection implements WebSocketConnection {
  private static final Logger log = LoggerFactory.getLogger(AbstractWebSocketConnection.class);

  private static final int maxMessageSize = Integer.getInteger(Constants.WEBSOCKET_MESSAGE_SIZE,
      Constants.WEBSOCKET_MESSAGE_SIZE_DEFAULT_VALUE);
  private static final long messageTimeout = Long.getLong(Constants.WEBSOCKET_MESSAGE_TIMEOUT,
      Constants.WEBSOCKET_MESSAGE_TIMEOUT_DEFAULT);

  /**
   * Hard upper bound on how long a single outbound frame may block the calling thread. The
   * container's own send timeout is unreliable here (it is a no-op when the configured value is
   * &lt;= 0, and is not honoured on every Jetty code path), and an unbounded wait inside
   * {@link #sendLock} is what allowed a half-open browser connection to park a thread indefinitely
   * while holding server-wide monitors. Override with {@code -Dwebswing.websocket.sendTimeoutMs=N}.
   */
  private static final long SEND_TIMEOUT_MS = Long.getLong("webswing.websocket.sendTimeoutMs",
      messageTimeout > 0 ? messageTimeout : 30000L);

  /** How long the shared ping timer is prepared to wait for the send lock before skipping. */
  private static final long PING_LOCK_TIMEOUT_MS =
      Long.getLong("webswing.websocket.pingLockTimeoutMs", 1000L);

  private static final Timer pingTimer = new Timer("Websocket Ping Timer", true);

  private ByteArrayOutputStream partialMsg = new ByteArrayOutputStream();

  protected Session session;

  /**
   * Serialises outbound frames on this connection. A {@link ReentrantLock} rather than an intrinsic
   * monitor so the shared ping timer can back off instead of blocking: the timer thread is global,
   * and one stuck connection must not stop pings for every other session.
   */
  private final ReentrantLock sendLock = new ReentrantLock();

  protected void onOpen(Session session, EndpointConfig config) {
    this.session = session;
    session.setMaxBinaryMessageBufferSize(maxMessageSize);
    if (messageTimeout > 0) {
      session.getAsyncRemote().setSendTimeout(messageTimeout);
    }

    pingTimer.schedule(new TimerTask() {
      @Override
      public void run() {
        if (session != null && session.isOpen()) {
          boolean locked = false;
          try {
            locked = sendLock.tryLock(PING_LOCK_TIMEOUT_MS, TimeUnit.MILLISECONDS);
            if (!locked) {
              // a data frame is currently in flight on this connection; skip this ping rather
              // than block the timer thread, which is shared by every websocket session
              log.debug("Skipping websocket ping for session [{}]: send lock busy.",
                  session.getId());
              return;
            }
            session.getAsyncRemote().sendPing(ByteBuffer
                .wrap(Constants.WEBSOCKET_PING_PONG_CONTENT.getBytes(StandardCharsets.UTF_8)));
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            this.cancel();
          } catch (IllegalArgumentException | IllegalStateException | IOException e) {
            this.cancel();
          } finally {
            if (locked) {
              sendLock.unlock();
            }
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

  /**
   * Sends one binary frame, blocking until the container reports completion.
   * <p>
   * The wait is bounded by {@link #SEND_TIMEOUT_MS}. An unbounded wait here is not survivable: a
   * browser that disappears without closing its TCP connection (laptop suspend, VPN or NAT drop,
   * mobile handover) leaves the write pending forever, and the calling thread then holds both
   * {@link #sendLock} and whatever caller-side monitor it entered with. In production this parked
   * the shared process-handler thread, which stopped delivering stdin heartbeats to every child
   * JVM; each child then shut itself down with {@code ShutdownReason.ProcessKilled} a few seconds
   * after startup, and only a server restart cleared it.
   * <p>
   * On timeout the pending send is cancelled and the session is aborted, so the dead connection is
   * torn down instead of accumulating.
   */
  protected void sendMessage(byte[] encoded) throws IOException {
    boolean timedOut = false;

    sendLock.lock();
    try {
      Future<Void> pending = session.getAsyncRemote().sendBinary(ByteBuffer.wrap(encoded));
      try {
        pending.get(SEND_TIMEOUT_MS, TimeUnit.MILLISECONDS);
      } catch (TimeoutException e) {
        pending.cancel(true);
        timedOut = true;
      }
    } catch (InterruptedException ex) {
      Thread.currentThread().interrupt();
      throw new IOException(ex.getMessage(), ex);
    } catch (IllegalStateException | ExecutionException ex) {
      throw new IOException(ex.getMessage(), ex);
    } finally {
      sendLock.unlock();
    }

    if (timedOut) {
      // deliberately outside sendLock: the abort itself must not be able to block other senders
      abortTimedOutSession();
      throw new IOException(
          "Websocket send timed out after " + SEND_TIMEOUT_MS + "ms; session aborted.");
    }
  }

  /**
   * Closes a session whose outbound frame timed out. Best effort — the connection is already
   * considered lost by the time this is called.
   */
  private void abortTimedOutSession() {
    Session s = this.session;
    if (s == null) {
      return;
    }
    log.warn("Websocket send timed out after {}ms, aborting session [{}].", SEND_TIMEOUT_MS,
        s.getId());
    try {
      s.close(new CloseReason(CloseCodes.GOING_AWAY, "Send timeout"));
    } catch (Throwable t) {
      log.debug("Failed to close timed-out websocket session [{}]", s.getId(), t);
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
