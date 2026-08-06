package org.webswing.toolkit;

import org.webswing.toolkit.util.Util;
import org.webswing.util.AppLogger;

import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.awt.peer.DesktopPeer;
import java.io.File;
import java.io.IOException;
import java.net.URI;

public class WebDesktopPeer implements DesktopPeer {


  public WebDesktopPeer(Desktop d) {}

  @Override
  public boolean isSupported(Action action) {
    switch (action) {
      case BROWSE:
      case MAIL:
      case EDIT:
      case OPEN:
      case PRINT:
        return true;
      default:
        return false;
    }

  }

  @Override
  public void open(File file) throws IOException {
    AppLogger.info("WebDesktopPeer:open", file);
    sendFile(file, false);
  }

  @Override
  public void edit(File file) throws IOException {
    AppLogger.info("WebDesktopPeer:edit", file);
    sendFile(file, false);
  }

  @Override
  public void print(File file) throws IOException {
    AppLogger.info("WebDesktopPeer:print", file);
    sendFile(file, true);
  }

  private void sendFile(File file, boolean preview) {
    Util.getWebToolkit().getPaintDispatcher().notifyFileRequested(file, preview);
  }

  @Override
  public void mail(URI mailtoURL) throws IOException {
    Util.getWebToolkit().getPaintDispatcher().notifyOpenLinkAction(mailtoURL);
  }

  @Override
  public void browse(URI url) throws IOException {
    Util.getWebToolkit().getPaintDispatcher().notifyOpenLinkAction(url);
  }
}
