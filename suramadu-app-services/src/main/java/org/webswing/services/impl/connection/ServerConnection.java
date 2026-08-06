package org.webswing.services.impl.connection;

import org.webswing.model.SyncObjectResponse;
import org.webswing.model.app.in.ServerToAppFrameMsgIn;
import org.webswing.model.app.out.AppToServerFrameMsgOut;
import org.webswing.model.appframe.in.AppFrameMsgIn;

import java.util.concurrent.TimeoutException;

public interface ServerConnection {

  void initialize(String serverUrl, MessageListener messageListener) throws Exception;

  void close();

  void close(String reason);

  void sendMessage(AppToServerFrameMsgOut msgOut);

  SyncObjectResponse sendMessageSync(AppToServerFrameMsgOut msgOut, String correlationId)
      throws TimeoutException;

  void handleSyncMessageResult(ServerToAppFrameMsgIn msgIn, AppFrameMsgIn frame);

  interface MessageListener {
    void onMessage(ServerToAppFrameMsgIn msgIn);
  }

}
