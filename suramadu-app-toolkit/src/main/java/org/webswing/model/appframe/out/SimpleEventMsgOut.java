package org.webswing.model.appframe.out;

import org.webswing.model.MsgOut;

public enum SimpleEventMsgOut implements MsgOut {
  applicationAlreadyRunning, shutDownNotification, tooManyClientsNotification, continueOldSession, configurationError, sessionStolenNotification, unauthorizedAccess, shutDownAutoLogoutNotification, sessionTimeoutWarning, sessionTimedOutNotification, applicationBusy, reconnectInstanceNotFound;

  public AppFrameMsgOut buildMsgOut() {
    AppFrameMsgOut result = new AppFrameMsgOut();
    result.setEvent(this);
    return result;
  }
}
