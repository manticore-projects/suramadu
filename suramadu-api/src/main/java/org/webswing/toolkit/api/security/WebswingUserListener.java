package org.webswing.toolkit.api.security;

public interface WebswingUserListener {

  void onPrimaryUserConnected(UserEvent evt);

  void onPrimaryUserDisconnected(UserEvent evt);

  void onMirrorViewConnected(UserEvent evt);

  void onMirrorViewDisconnected(UserEvent evt);
}
