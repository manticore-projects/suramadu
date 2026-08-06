package org.webswing.server.api.services.websocket.impl;

import com.google.inject.Inject;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.model.Msg;
import org.webswing.model.appframe.out.AppFrameMsgOut;
import org.webswing.model.appframe.out.SimpleEventMsgOut;
import org.webswing.model.appframe.out.StartApplicationMsgOut;
import org.webswing.model.browser.in.BrowserToServerFrameMsgIn;
import org.webswing.model.browser.out.ServerToBrowserFrameMsgOut;
import org.webswing.server.api.services.application.AppPathHandler;
import org.webswing.server.api.services.playback.SessionRecordingPlayback;
import org.webswing.server.api.services.websocket.RecordingWebSocketConnection;
import org.webswing.server.api.services.websocket.WebSocketService;
import org.webswing.server.api.services.websocket.util.BrowserWebSocketConfigurator;
import org.webswing.server.api.util.SecurityUtil;
import org.webswing.server.common.datastore.WebswingDataStoreModule;
import org.webswing.server.common.model.security.WebswingAction;
import org.webswing.server.common.service.security.AbstractWebswingUser;
import org.webswing.server.common.service.security.SecurityManagerService;
import org.webswing.server.common.service.security.impl.WebswingSecuritySubject;
import org.webswing.server.common.util.ProtoMapper;

import javax.websocket.*;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint(value = "/{appPath}/async/swing-play",
    configurator = BrowserWebSocketConfigurator.class)
public class RecordingPlaybackWebSocketConnectionImpl extends AbstractWebSocketConnection
    implements RecordingWebSocketConnection {

  private static final Logger log =
      LoggerFactory.getLogger(RecordingPlaybackWebSocketConnectionImpl.class);

  private ProtoMapper protoMapper = new ProtoMapper(ProtoMapper.PROTO_PACKAGE_SERVER_BROWSER_FRAME,
      ProtoMapper.PROTO_PACKAGE_SERVER_BROWSER_FRAME);
  private ProtoMapper appFrameProtoMapper = new ProtoMapper(ProtoMapper.PROTO_PACKAGE_APPFRAME_OUT,
      ProtoMapper.PROTO_PACKAGE_APPFRAME_IN);
  private WebSocketService webSocketService;
  private AppPathHandler appPathHandler;

  private SessionRecordingPlayback playback;
  private String path;
  private AbstractWebswingUser user;

  @Inject
  public RecordingPlaybackWebSocketConnectionImpl(WebSocketService webSocketService) {
    this.webSocketService = webSocketService;
  }

  @OnOpen
  public void onOpen(Session session, EndpointConfig config, @PathParam("appPath") String appPath) {
    super.onOpen(session, config);

    this.path = "/" + appPath;
    this.appPathHandler = webSocketService.getAppPathHandler(this.path);
    this.user = SecurityUtil.resolveUser(getSecuritySubject(), appPathHandler);

    if (!(Boolean) session.getUserProperties().get(BrowserWebSocketConfigurator.AUTHENTICATED)
        || this.user == null) {
      disconnect("Unauthorized access!");
      return;
    }

    try {
      if (hasPermission(WebswingAction.websocket_connect)
          && hasPermission(WebswingAction.websocket_startRecordingPlayback)) {
        String fileParam = (String) session.getUserProperties()
            .get(BrowserWebSocketConfigurator.HANDSHAKE_PARAM_FILE);
        playback = getRecordingPlayback(this, fileParam, appPathHandler.getDataStore());

        if (playback == null) {
          disconnect("Could not open recording file!");
          return;
        }

        try {
          AppFrameMsgOut appFrame = new AppFrameMsgOut();
          appFrame.setStartApplication(new StartApplicationMsgOut());

          ServerToBrowserFrameMsgOut msgOut = new ServerToBrowserFrameMsgOut();
          msgOut.setAppFrameMsgOut(appFrameProtoMapper.encodeProto(appFrame));

          sendMessage(msgOut);
        } catch (IOException e) {
          log.error("Could not encode proto message for recording playback [{}]!", session.getId(),
              e);
        }
      } else {
        ServerToBrowserFrameMsgOut msgOut = new ServerToBrowserFrameMsgOut();
        msgOut.setAppFrameMsgOut(
            appFrameProtoMapper.encodeProto(SimpleEventMsgOut.unauthorizedAccess.buildMsgOut()));

        sendMessage(msgOut);
        disconnect("Unauthorized access!");
        return;
      }
    } catch (IOException e) {
      log.error("Could not encode proto message for recording playback [{}]!", session.getId(), e);
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
      BrowserToServerFrameMsgIn frame = (BrowserToServerFrameMsgIn) frameWithLength.getKey();

      if (frame != null && frame.getPlayback() != null) {
        playback.handlePlaybackControl(frame.getPlayback());
      }
    } catch (IOException e) {
      log.error("Could not decode proto message from recording playback [{}]!", session.getId(), e);
    }
  }

  @OnClose
  public void onClose(Session session, CloseReason closeReason) {
    if (session != null) {
      log.info("Websocket closed for recording playback, session [{}]{}", session.getId(),
          closeReason != null
              ? ", close code [" + closeReason.getCloseCode().getCode() + "], reason ["
                  + closeReason.getReasonPhrase() + "]!"
              : "");
    }
    if (playback != null) {
      playback.close();
    }
  }

  @OnError
  public void onError(Session session, Throwable t) {
    log.error("Websocket error from recording playback connection, session [{}]! {}",
        session == null ? null : session.getId(), t.getMessage());
    log.debug(t.getMessage(), t);
  }

  @Override
  public void sendMessage(AppFrameMsgOut frame) {
    try {
      ServerToBrowserFrameMsgOut msgOut = new ServerToBrowserFrameMsgOut();
      msgOut.setAppFrameMsgOut(appFrameProtoMapper.encodeProto(frame));
      sendMessage(msgOut);
    } catch (IOException e) {
      log.error("Could not encode AppFrameMsgOut for session [{}]!", session.getId(), e);
    }
  }

  private void sendMessage(ServerToBrowserFrameMsgOut msgOut) {
    try {
      byte[] encoded = protoMapper.encodeProto(msgOut);
      super.sendMessage(encoded);
    } catch (IOException e) {
      log.error("Failed to send playback msg to browser, session [{}]! {}",
          session == null ? null : session.getId(), e.getMessage());
      log.debug(e.getMessage(), e);
    }
  }

  @Override
  protected Msg decodeIncomingMessage(byte[] bytes) throws IOException {
    return protoMapper.decodeProto(bytes, BrowserToServerFrameMsgIn.class);
  }

  private void disconnect(String reason) {
    if (session != null && session.isOpen()) {
      try {
        session.close(new CloseReason(CloseCodes.NORMAL_CLOSURE, reason));
      } catch (IOException e) {
        log.error("Failed to disconnect playback connection, session [{}]! {}", session.getId(),
            e.getMessage());
        log.debug(e.getMessage(), e);
      }
    }
  }

  private SessionRecordingPlayback getRecordingPlayback(RecordingWebSocketConnection connection,
      String fileName, WebswingDataStoreModule dataStore) {
    return new SessionRecordingPlayback(connection, fileName, dataStore);
  }

  private AbstractWebswingUser getUser() {
    return user;
  }

  private WebswingSecuritySubject getSecuritySubject() {
    if (session == null) {
      return null;
    }
    return (WebswingSecuritySubject) session.getUserProperties()
        .get(SecurityManagerService.SECURITY_SUBJECT);
  }

  private boolean hasPermission(WebswingAction action) {
    return getUser() == null ? false : getUser().isPermitted(action.name());
  }

}
