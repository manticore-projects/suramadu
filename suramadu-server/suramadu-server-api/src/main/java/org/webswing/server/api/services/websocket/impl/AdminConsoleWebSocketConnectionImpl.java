package org.webswing.server.api.services.websocket.impl;

import com.google.inject.Inject;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.model.Msg;
import org.webswing.model.adminconsole.in.AdminConsoleFrameMsgIn;
import org.webswing.model.adminconsole.in.AdminConsoleHandshakeMsgIn;
import org.webswing.model.adminconsole.out.AdminConsoleFrameMsgOut;
import org.webswing.server.api.services.sessionpool.SessionPoolHolderService;
import org.webswing.server.api.services.websocket.AdminConsoleWebSocketConnection;
import org.webswing.server.api.services.websocket.util.AdminConsoleWebSocketConfigurator;
import org.webswing.server.common.util.ProtoMapper;

import javax.websocket.*;
import javax.websocket.CloseReason.CloseCode;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint(value = "/async/adminconsole",
    configurator = AdminConsoleWebSocketConfigurator.class)
public class AdminConsoleWebSocketConnectionImpl extends AbstractWebSocketConnection
    implements AdminConsoleWebSocketConnection {

  private static final Logger log =
      LoggerFactory.getLogger(AdminConsoleWebSocketConnectionImpl.class);

  private ProtoMapper protoMapper = new ProtoMapper(ProtoMapper.PROTO_PACKAGE_ADMIN_CONSOLE_FRAME);

  private final SessionPoolHolderService sessionPoolHolderService;

  private boolean secured;

  @Inject
  public AdminConsoleWebSocketConnectionImpl(SessionPoolHolderService sessionPoolHolderService) {
    this.sessionPoolHolderService = sessionPoolHolderService;
  }

  @OnOpen
  public void onOpen(Session session, EndpointConfig config) {
    super.onOpen(session, config);

    if (!sessionPoolHolderService.registerAdminConsole(this)) {
      disconnect(CloseCodes.CANNOT_ACCEPT, "An admin console is already connected!");
      return;
    }
  }

  @OnMessage
  public void onMessage(Session session, byte[] bytes, boolean last) {
    try {
      Pair<Msg, Integer> frameWithLength = super.getCompleteMessage(bytes, last);
      if (frameWithLength == null) {
        // incomplete
        return;
      }

      AdminConsoleFrameMsgIn msgIn = (AdminConsoleFrameMsgIn) frameWithLength.getKey();

      if (msgIn.getHandshake() != null) {
        AdminConsoleHandshakeMsgIn handshake = msgIn.getHandshake();
        secured = validateHandshakeToken(handshake.getSecretMessage());
      }

      if (!secured) {
        // we must get handshake first, otherwise we disconnect
        disconnect(CloseCodes.CANNOT_ACCEPT, "Connection not secured!");
        return;
      } else {
        sessionPoolHolderService.handleAdminConsoleMessage(msgIn, this);
      }
    } catch (IOException e) {
      log.error("Could not decode proto message from admin console, session id [{}]!",
          session.getId(), e);
    }
  }

  @OnMessage
  public void onPong(Session session, PongMessage pongMessage) {
    super.onPong(session, pongMessage, log);
  }

  @OnClose
  public void onClose(Session session, CloseReason closeReason) {
    if (session != null) {
      log.info("Websocket closed to admin console, session [{}]{}", session.getId(),
          closeReason != null
              ? ", close code [" + closeReason.getCloseCode().getCode() + "], reason ["
                  + closeReason.getReasonPhrase() + "]!"
              : "");
    }

    sessionPoolHolderService.unregisterAdminConsole(this);

  }

  @OnError
  public void onError(Session session, Throwable t) {
    log.error("Websocket error from admin console connection, session [{}]{}",
        session == null ? null : session.getId(), t.getMessage());
    log.debug(t.getMessage(), t);
  }

  @Override
  public void sendMessage(AdminConsoleFrameMsgOut msgOut) {
    try {
      super.sendMessage(protoMapper.encodeProto(msgOut));
    } catch (IOException e) {
      log.error("Failed to send msg to admin console, session [{}]!{}",
          session == null ? null : session.getId(), e.getMessage());
      log.debug(e.getMessage(), e);
    }
  }

  @Override
  public boolean isConnected() {
    return session != null && session.isOpen();
  }

  @Override
  protected Msg decodeIncomingMessage(byte[] bytes) throws IOException {
    return protoMapper.decodeProto(bytes, AdminConsoleFrameMsgIn.class);
  }

  private void disconnect(CloseCode closeCode, String reason) {
    if (session != null && session.isOpen()) {
      try {
        session.close(new CloseReason(closeCode, reason));
      } catch (IOException e) {
        log.error("Failed to disconnect admin console connection session [{}]!{}", session.getId(),
            e.getMessage());
        log.debug(e.getMessage(), e);
      }
    }
  }

}
