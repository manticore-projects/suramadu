package org.webswing.server.api.services.websocket.impl;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.Constants;
import org.webswing.model.Msg;
import org.webswing.server.api.services.websocket.WebSocketConnection;
import org.webswing.server.common.util.JwtUtil;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.EndpointConfig;
import javax.websocket.PongMessage;
import javax.websocket.SendResult;
import javax.websocket.Session;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Base class for all server side websocket endpoints.
 * <p>
 * <b>Outbound frames are queued and delivered asynchronously.</b> Callers hand a frame over and
 * return immediately; exactly one send is in flight per connection, which preserves frame order. No
 * server thread ever waits for the network.
 * <p>
 * This replaces a blocking design in which every caller waited for the container to confirm the
 * send. That design caused a server wide outage: a browser that vanished without closing its TCP
 * connection left the send pending forever, and the waiting thread held both the send lock and the
 * caller's own monitors. Bounding the wait fixed the hang, but a slow client could still occupy a
 * Jetty thread for the whole timeout, and enough slow clients at once would still exhaust the pool.
 * Not waiting at all removes the class of failure rather than capping its duration.
 * <p>
 * Backpressure is expressed as queue limits instead of as waiting. A client that stops consuming
 * fills its queue, and a full queue means the connection is dead — it is then aborted immediately
 * rather than after a timeout.
 * <p>
 * The two websocket operations JSR-356 defines as blocking — {@code sendPing}, which is declared on
 * {@code RemoteEndpoint} rather than on {@code Async}, and {@code Session.close} — are dispatched
 * to a small IO pool, so a client that has gone away cannot stall the shared ping timer or a
 * container IO thread for every other session.
 */
public abstract class AbstractWebSocketConnection implements WebSocketConnection {
  private static final Logger log = LoggerFactory.getLogger(AbstractWebSocketConnection.class);

  private static final int maxMessageSize = Integer.getInteger(Constants.WEBSOCKET_MESSAGE_SIZE,
      Constants.WEBSOCKET_MESSAGE_SIZE_DEFAULT_VALUE);
  private static final long messageTimeout = Long.getLong(Constants.WEBSOCKET_MESSAGE_TIMEOUT,
      Constants.WEBSOCKET_MESSAGE_TIMEOUT_DEFAULT);

  /**
   * Maximum number of frames waiting to be written to one connection. Reaching it means the client
   * is not consuming what we send. Override with {@code -Dwebswing.websocket.maxQueuedFrames=N}.
   */
  private static final int MAX_QUEUED_FRAMES =
      Integer.getInteger("webswing.websocket.maxQueuedFrames", 256);

  /**
   * Maximum number of bytes waiting to be written to one connection. A frame count alone is not a
   * sufficient bound, because a single frame can be megabytes. Override with
   * {@code -Dwebswing.websocket.maxQueuedBytes=N}.
   */
  private static final long MAX_QUEUED_BYTES =
      Long.getLong("webswing.websocket.maxQueuedBytes", 32L * 1024 * 1024);

  /**
   * How long one frame may remain in flight before the connection is considered dead. Checked by
   * the shared ping timer, so detection is accurate to one ping interval. Override with
   * {@code -Dwebswing.websocket.sendTimeoutMs=N}.
   */
  private static final long SEND_TIMEOUT_MS = Long.getLong("webswing.websocket.sendTimeoutMs",
      messageTimeout > 0 ? messageTimeout : 30000L);

  /**
   * Carries out the two websocket operations that JSR-356 defines as blocking: {@code sendPing} is
   * declared on {@code RemoteEndpoint} rather than on {@code Async} and writes synchronously, and
   * {@code Session.close} writes a close frame the same way. Neither may run on the shared ping
   * timer thread or on a container IO thread, because a client that has gone away without closing
   * its connection would then stall that thread for every other session too. Override the size with
   * {@code -Dwebswing.websocket.ioThreads=N}.
   */
  private static final ExecutorService ioExecutor =
      Executors.newFixedThreadPool(Integer.getInteger("webswing.websocket.ioThreads",
          Math.max(4, Runtime.getRuntime().availableProcessors())), r -> {
            Thread t = new Thread(r, "Websocket IO");
            t.setDaemon(true);
            return t;
          });

  private static final Timer pingTimer = new Timer("Websocket Ping Timer", true);

  private ByteArrayOutputStream partialMsg = new ByteArrayOutputStream();

  protected Session session;

  /** Guards the outbound queue and its bookkeeping. Never held across a send. */
  private final ReentrantLock queueLock = new ReentrantLock();
  private final Deque<ByteBuffer> outboundQueue = new ArrayDeque<>();
  private long queuedBytes;
  private boolean sendInFlight;
  private long sendStartedAt;
  private boolean aborted;
  /** Set by {@link #closeWhenDrained}; the close runs once the queue empties. */
  private CloseReason pendingClose;

  protected void onOpen(Session session, EndpointConfig config) {
    this.session = session;
    session.setMaxBinaryMessageBufferSize(maxMessageSize);
    if (messageTimeout > 0) {
      session.getAsyncRemote().setSendTimeout(messageTimeout);
    }

    pingTimer.schedule(new TimerTask() {
      @Override
      public void run() {
        if (session == null || !session.isOpen()) {
          this.cancel();
          return;
        }

        if (isSendStalled()) {
          // A frame has been in flight far too long. The container is not going to complete it,
          // so stop holding a queue for a client that is no longer there.
          abortConnection("frame in flight for more than " + SEND_TIMEOUT_MS + "ms");
          this.cancel();
          return;
        }

        // Pings bypass the outbound queue but must not overlap a data frame, so they are skipped
        // while one is in flight. A skipped ping is harmless; the next one is one tick away.
        if (!tryMarkPingSend()) {
          return;
        }

        // sendPing is a BLOCKING call, so it must never run on this shared timer thread.
        TimerTask task = this;
        if (!runOnIoThread(() -> {
          try {
            session.getAsyncRemote().sendPing(ByteBuffer
                .wrap(Constants.WEBSOCKET_PING_PONG_CONTENT.getBytes(StandardCharsets.UTF_8)));
          } catch (IllegalArgumentException | IllegalStateException | IOException e) {
            task.cancel();
          } finally {
            clearPingSend();
          }
        })) {
          clearPingSend();
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
   * Queues one binary frame for delivery and returns immediately. Frames are delivered in the order
   * they were queued.
   *
   * @throws IOException if the connection is already aborted, or if its queue is full — which means
   *         the client has stopped consuming and the connection is being dropped
   */
  protected void sendMessage(byte[] encoded) throws IOException {
    if (encoded == null || encoded.length == 0) {
      return;
    }

    ByteBuffer toSend = null;
    boolean overflow = false;
    int queueDepth = 0;
    long queueSize = 0;

    queueLock.lock();
    try {
      if (aborted) {
        throw new IOException("Websocket connection has been aborted; frame discarded.");
      }
      if (pendingClose != null) {
        throw new IOException("Websocket connection is closing; frame discarded.");
      }

      if (outboundQueue.size() >= MAX_QUEUED_FRAMES
          || queuedBytes + encoded.length > MAX_QUEUED_BYTES) {
        overflow = true;
        queueDepth = outboundQueue.size();
        queueSize = queuedBytes;
      } else {
        outboundQueue.add(ByteBuffer.wrap(encoded));
        queuedBytes += encoded.length;

        if (!sendInFlight) {
          toSend = pollNextLocked();
        }
      }
    } finally {
      queueLock.unlock();
    }

    if (overflow) {
      abortConnection("outbound queue full (" + queueDepth + " frames, " + queueSize
          + " bytes) - client is not consuming");
      throw new IOException("Websocket outbound queue full; connection dropped.");
    }

    if (toSend != null) {
      deliver(toSend);
    }
  }

  /**
   * Takes the next frame and marks it in flight. Caller must hold {@link #queueLock}.
   *
   * @return the frame to send, or null when the queue is empty
   */
  private ByteBuffer pollNextLocked() {
    ByteBuffer next = outboundQueue.poll();
    if (next == null) {
      sendInFlight = false;
      sendStartedAt = 0;
      return null;
    }
    queuedBytes -= next.remaining();
    sendInFlight = true;
    sendStartedAt = System.currentTimeMillis();
    return next;
  }

  /**
   * Hands one frame to the container. The completion handler chains the next frame, which is what
   * keeps exactly one send in flight and therefore preserves order.
   */
  private void deliver(ByteBuffer buffer) {
    try {
      session.getAsyncRemote().sendBinary(buffer, this::onSendComplete);
    } catch (Throwable t) {
      // sendBinary itself refused the frame (closed session, illegal state, ...)
      onSendComplete(new SendResult(t));
    }
  }

  private void onSendComplete(SendResult result) {
    if (result != null && !result.isOK()) {
      abortConnection("send failed: " + String.valueOf(result.getException()));
      return;
    }

    ByteBuffer next;
    boolean closeNow = false;
    queueLock.lock();
    try {
      sendInFlight = false;
      next = pollNextLocked();
      closeNow = next == null && pendingClose != null;
    } finally {
      queueLock.unlock();
    }

    if (next != null) {
      // Normally the container invokes this handler asynchronously, so this is not recursion. On
      // the error path deliver() completes inline, but that path aborts instead of chaining.
      deliver(next);
    } else if (closeNow) {
      closeSessionAsync();
    }
  }

  private boolean isSendStalled() {
    queueLock.lock();
    try {
      return sendInFlight && sendStartedAt > 0
          && System.currentTimeMillis() - sendStartedAt > SEND_TIMEOUT_MS;
    } finally {
      queueLock.unlock();
    }
  }

  private boolean tryMarkPingSend() {
    queueLock.lock();
    try {
      if (sendInFlight || aborted || pendingClose != null) {
        return false;
      }
      sendInFlight = true;
      sendStartedAt = System.currentTimeMillis();
      return true;
    } finally {
      queueLock.unlock();
    }
  }

  private void clearPingSend() {
    ByteBuffer next;
    boolean closeNow;
    queueLock.lock();
    try {
      sendInFlight = false;
      next = pollNextLocked();
      closeNow = next == null && pendingClose != null;
    } finally {
      queueLock.unlock();
    }
    if (next != null) {
      deliver(next);
    } else if (closeNow) {
      closeSessionAsync();
    }
  }

  /**
   * Closes the session once everything already queued has reached the client, and returns
   * immediately.
   * <p>
   * Delivery is asynchronous, so a frame handed over a moment ago may still be queued. Closing
   * straight away would let the close frame overtake it and the user would lose the shutdown notice
   * or goodbye redirect — the session would simply vanish from the screen. Rather than wait for the
   * queue to drain, the close is queued behind it: whichever completion empties the queue performs
   * it. Nothing blocks, and no frame is lost.
   * <p>
   * If the client never consumes the backlog, the stall detector in the ping timer aborts the
   * connection instead, so this cannot hold a session open indefinitely.
   */
  protected void closeWhenDrained(CloseReason reason) {
    boolean closeNow;

    queueLock.lock();
    try {
      if (aborted || pendingClose != null) {
        return;
      }
      pendingClose = reason;
      closeNow = !sendInFlight && outboundQueue.isEmpty();
    } finally {
      queueLock.unlock();
    }

    if (closeNow) {
      closeSessionAsync();
    }
  }

  /**
   * Performs the pending close on an IO thread. {@code Session.close} writes a close frame and is a
   * blocking call, so it must not run on a container IO thread or on the shared ping timer.
   */
  private void closeSessionAsync() {
    Session s = this.session;
    CloseReason reason;

    queueLock.lock();
    try {
      reason = pendingClose;
    } finally {
      queueLock.unlock();
    }

    if (s == null || reason == null) {
      return;
    }

    if (!runOnIoThread(() -> {
      try {
        s.close(reason);
      } catch (Throwable t) {
        log.debug("Failed to close websocket session [{}]", s.getId(), t);
      }
    })) {
      log.debug("Could not schedule close of websocket session [{}]", s.getId());
    }
  }

  /**
   * @return false when the task could not be scheduled, which happens only while the server is
   *         shutting down
   */
  private static boolean runOnIoThread(Runnable task) {
    try {
      ioExecutor.execute(task);
      return true;
    } catch (RejectedExecutionException e) {
      return false;
    }
  }

  /**
   * Drops every queued frame and closes the session. Used when the client has demonstrably stopped
   * consuming; there is no point keeping either the connection or its backlog.
   */
  private void abortConnection(String reason) {
    Session s = this.session;

    queueLock.lock();
    try {
      if (aborted) {
        return;
      }
      aborted = true;
      outboundQueue.clear();
      queuedBytes = 0;
      sendInFlight = false;
      sendStartedAt = 0;
    } finally {
      queueLock.unlock();
    }

    log.warn("Aborting websocket session [{}]: {}", s == null ? null : s.getId(), reason);

    if (s == null) {
      return;
    }
    // Session.close blocks; keep it off the caller's thread.
    runOnIoThread(() -> {
      try {
        s.close(new CloseReason(CloseCodes.GOING_AWAY, "Connection aborted"));
      } catch (Throwable t) {
        log.debug("Failed to close aborted websocket session [{}]", s.getId(), t);
      }
    });
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
