package org.webswing.server.api.services.swinginstance;

import org.webswing.model.adminconsole.out.SwingSessionMsgOut;
import org.webswing.model.adminconsole.out.ThreadDumpMsgOut;
import org.webswing.model.app.in.ServerToAppFrameMsgIn;
import org.webswing.model.app.out.AppToServerFrameMsgOut;
import org.webswing.model.appframe.in.AppFrameMsgIn;
import org.webswing.model.browser.in.BrowserToServerFrameMsgIn;
import org.webswing.model.browser.out.ServerToBrowserFrameMsgOut;
import org.webswing.model.common.in.ConnectionHandshakeMsgIn;
import org.webswing.server.api.model.ProcessStatusEnum;
import org.webswing.server.api.services.websocket.ApplicationWebSocketConnection;
import org.webswing.server.api.services.websocket.MirrorWebSocketConnection;
import org.webswing.server.api.services.websocket.PrimaryWebSocketConnection;
import org.webswing.server.common.model.SecuredPathConfig;

public interface ConnectedSwingInstance extends RemoteSwingInstance {

  SecuredPathConfig getConfig();

  String getUserId();

  String getConnectionId();

  void connectBrowser(PrimaryWebSocketConnection r, ConnectionHandshakeMsgIn h);

  void connectApplication(ApplicationWebSocketConnection r, boolean reconnect);

  void connectMirroredWebSession(MirrorWebSocketConnection resource);

  void browserDisconnected(String connectionId);

  void disconnectMirroredWebSession(boolean disconnect);

  void disconnectMirroredWebSession(String sessionId, boolean disconnect);

  void applicationDisconnected(String reason);

  void handleAppMessage(AppToServerFrameMsgOut msgOut);

  void handleBrowserMessage(BrowserToServerFrameMsgIn msgIn);

  void handleBrowserMirrorMessage(byte[] frame);

  boolean sendMessageToBrowser(ServerToBrowserFrameMsgOut msgOut);

  boolean sendMessageToApp(ServerToAppFrameMsgIn msgIn);

  boolean sendMessageToApp(AppFrameMsgIn msgIn);

  void notifyUserConnected();

  void close();

  SwingSessionMsgOut toSwingSession();

  boolean isStatisticsLoggingEnabled();

  void logWarningHistory();

  void startRecording();

  void stopRecording();

  ThreadDumpMsgOut getThreadDump(String timestamp);

  void toggleStatisticsLogging(boolean enabled);

  boolean isRunning();

  String getPathMapping();

  String getAppName();

  void updateProcessStatus(ProcessStatusEnum processStatus);

}
