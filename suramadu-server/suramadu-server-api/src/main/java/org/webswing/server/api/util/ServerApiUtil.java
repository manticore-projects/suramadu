package org.webswing.server.api.util;

import org.webswing.model.common.in.ConnectionHandshakeMsgIn;
import org.webswing.server.api.services.websocket.PrimaryWebSocketConnection;
import org.webswing.server.common.model.SecuredPathConfig;
import org.webswing.server.common.model.SecuredPathConfig.SessionMode;
import org.webswing.server.common.util.ServerUtil;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ServerApiUtil {

  public static String resolveOwnerIdForSessionMode(PrimaryWebSocketConnection r,
      ConnectionHandshakeMsgIn h, SecuredPathConfig conf) {
    return resolveOwnerIdForSessionMode(r, h, conf.getSessionMode());
  }

  public static String resolveOwnerIdForSessionMode(PrimaryWebSocketConnection r,
      ConnectionHandshakeMsgIn h, SessionMode sessionMode) {
    String user = r.getUser() != null ? r.getUser().getUserId() : "null";
    switch (sessionMode) {
      case CONTINUE_FOR_USER:
        return user;
      case CONTINUE_FOR_BROWSER:
        return user + h.getBrowserId();
      case CONTINUE_FOR_TAB:
        return user + h.getTabId();
      case ALWAYS_NEW_SESSION:
      default:
        return user + h.getBrowserId() + h.getViewId();
    }
  }

  public static String generateInstanceId(PrimaryWebSocketConnection r, ConnectionHandshakeMsgIn h,
      String appPath) {
    String user = r.getUser() != null ? r.getUser().getUserId() : "null";
    String app = (appPath.startsWith("/") ? appPath.substring(1) : appPath).replace("/", "+");
    return app + "_" + user + "_" + h.getBrowserId() + "_" + System.currentTimeMillis();
  }

  public static String getContextPath(ServletContext ctx) {
    return ServerUtil.getContextPath(ctx);
  }

  public static void sendHttpRedirect(HttpServletRequest req, HttpServletResponse resp,
      String relativeUrl) throws IOException {
    ServerUtil.sendHttpRedirect(req, resp, relativeUrl);
  }

  private ServerApiUtil() {}

}
