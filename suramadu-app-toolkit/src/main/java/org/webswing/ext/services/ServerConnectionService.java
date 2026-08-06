package org.webswing.ext.services;

import org.webswing.model.SyncObjectResponse;
import org.webswing.model.app.out.AppToServerFrameMsgOut;
import org.webswing.model.appframe.out.AccessibilityMsgOut;
import org.webswing.model.appframe.out.AppFrameMsgOut;

import java.awt.Component;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public interface ServerConnectionService {

  void reconnect(String reconnectUrl);

  void sendObject(AppToServerFrameMsgOut msgOut, AppFrameMsgOut frame);

  SyncObjectResponse sendObjectSync(AppToServerFrameMsgOut msgOut, AppFrameMsgOut frame,
      String correlationId) throws TimeoutException, Exception;

  AccessibilityMsgOut getAccessibilityInfo();

  AccessibilityMsgOut getAccessibilityInfo(Component c, int x, int y);

  Map<String, Serializable> deserializeUserAttributes(byte[] data) throws IOException;

}
