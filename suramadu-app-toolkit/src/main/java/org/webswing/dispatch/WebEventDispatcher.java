package org.webswing.dispatch;

import org.webswing.Constants;
import org.webswing.audio.AudioClip;
import org.webswing.model.appframe.in.*;
import org.webswing.model.appframe.in.MouseEventMsgIn.MouseEventType;
import org.webswing.model.appframe.out.FileDialogEventMsgOut.FileDialogEventType;
import org.webswing.model.common.in.ConnectionHandshakeMsgIn;
import org.webswing.model.common.in.SimpleEventMsgIn;
import org.webswing.toolkit.FocusEventCause;
import org.webswing.toolkit.WebClipboard;
import org.webswing.toolkit.WebClipboardTransferable;
import org.webswing.toolkit.WebWindowPeer;
import org.webswing.toolkit.api.component.HtmlPanel;
import org.webswing.toolkit.api.lifecycle.ShutdownReason;
import org.webswing.toolkit.extra.WindowManager;
import org.webswing.toolkit.util.Services;
import org.webswing.toolkit.util.Util;
import org.webswing.util.AppLogger;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static java.awt.event.KeyEvent.KEY_LOCATION_NUMPAD;
import static java.awt.event.KeyEvent.KEY_LOCATION_STANDARD;

public class WebEventDispatcher extends AbstractEventDispatcher {

  protected void dispatchMessage(SimpleEventMsgIn message) {
    AppLogger.debug("WebEventDispatcher.dispatchMessage", message);
    switch (message.getType()) {
      case killSwing:
        AppLogger.info("Received kill signal from browser. Application shutting down.");
        Util.getWebToolkit().getSessionWatchdog().scheduleShutdown(ShutdownReason.BrowserKill);
        break;
      case killSwingAdmin:
        AppLogger.info("Received kill signal from Admin console. Application shutting down.");
        Util.getWebToolkit().getSessionWatchdog().scheduleShutdown(ShutdownReason.Admin);
        break;
      case deleteFile:
        Util.getWebToolkit().getPaintDispatcher().notifyDeleteSelectedFile();
        break;
      case downloadFile:
        Util.getWebToolkit().getPaintDispatcher().notifyDownloadSelectedFile();
        break;
      case cancelFileSelection:
        JFileChooser dialog = Util.getWebToolkit().getPaintDispatcher().getFileChooserDialog();
        if (dialog != null) {
          dialog.cancelSelection();
        }
        break;
      case paintAck:
        Util.getWebToolkit().getPaintDispatcher().clientReadyToReceive();
        break;
      case repaint:
        Util.getWebToolkit().getPaintDispatcher().notifyWindowRepaintAll();
        break;
      case unload:
        boolean instantExit =
            Integer.parseInt(System.getProperty(Constants.SWING_SESSION_TIMEOUT_SEC, "300")) == 0;
        if (instantExit) {
          AppLogger.warn(
              "Exiting Application. Client has disconnected from web session. (swingSessionTimeout setting is 0 or less)");
          Util.getWebToolkit().getSessionWatchdog().scheduleShutdown(ShutdownReason.Inactivity);
        }
        break;
      case requestComponentTree:
        if (Util.isTestMode()) {
          Util.getWebToolkit().getPaintDispatcher().notifyComponentTreeRequested();
        }
        break;
      case requestWindowSwitchList:
        Util.getWebToolkit().getPaintDispatcher().notifyWindowSwitchList();
        break;
      case enableStatisticsLogging:
        Util.getWebToolkit().setStatisticsLoggingEnabled(true);
        break;
      case disableStatisticsLogging:
        Util.getWebToolkit().setStatisticsLoggingEnabled(false);
        break;
      case startRecording:
        Util.getWebToolkit().startRecording();
        break;
      case stopRecording:
        Util.getWebToolkit().stopRecording();
        break;
      case startMirroring:
        Util.getWebToolkit().startMirroring();
        break;
      case stopMirroring:
        Util.getWebToolkit().stopMirroring();
        break;
    }
  }

  protected void dispatchKeyboardEvent(KeyboardEventMsgIn event) {
    Window w = Util.getWebToolkit().getWindowManager().getActiveWindow();
    if (w != null) {
      long when = System.currentTimeMillis();
      int modifiers = Util.getKeyModifiersAWTFlag(event);
      int type = Util.getKeyType(event.getType());
      char character = Util.getKeyCharacter(event);
      Component src = w.getFocusOwner() == null ? w : w.getFocusOwner();
      if (event.getKeycode() == 13) {// enter keycode
        event.setKeycode(10);
        event.setCharacter(10);
        character = 10;
      } else if (CONVERTED_KEY_CODES.containsKey(event.getKeycode())
          && type != KeyEvent.KEY_TYPED) {
        int converted = CONVERTED_KEY_CODES.get(event.getKeycode());
        event.setKeycode(converted);
        character = (char) converted;
      } else if (NON_STANDARD_KEY_CODES.contains(event.getKeycode())) {
        event.setKeycode(0);
      }
      if (event.getType() == KeyboardEventMsgIn.KeyEventType.keydown) {
        getReleaseCharMap().put(event.getKeycode(), event.getCharacter());
      }
      if (event.getType() == KeyboardEventMsgIn.KeyEventType.keyup) {
        Integer c = getReleaseCharMap().get(event.getKeycode());
        character = (char) (c == null ? event.getCharacter() : c);
      }
      if (type == KeyEvent.KEY_TYPED) {
        AWTEvent e =
            new KeyEvent(src, KeyEvent.KEY_TYPED, when, modifiers, 0, (char) event.getCharacter());
        dispatchKeyEventInSwing(w, e);
      } else {
        int keyLocation =
            event.getKeycode() >= 96 && event.getKeycode() <= 111 ? KEY_LOCATION_NUMPAD
                : KEY_LOCATION_STANDARD;
        AWTEvent e = Util.createKeyEvent(src, type, when, modifiers, event.getKeycode(), character,
            keyLocation);
        dispatchKeyEventInSwing(w, e);
        if ((event.getKeycode() == 32 || event.getKeycode() == 9 || event.getKeycode() == 8)
            && event.getType() == KeyboardEventMsgIn.KeyEventType.keydown && !event.isCtrl()) {// space
                                                                                               // keycode
                                                                                               // handle
                                                                                               // press
          event.setType(KeyboardEventMsgIn.KeyEventType.keypress);
          dispatchKeyboardEvent(event);
        }
      }
    }
  }

  @Override
  protected void windowFocusEvent(WindowFocusMsgIn event) {
    if (event.getHtmlPanelId() != null) {
      HtmlPanel panel =
          Util.getWebToolkit().getPaintDispatcher().findHtmlPanelById(event.getHtmlPanelId());
      if (panel != null) {
        panel.requestFocusInWindow();
      }
    } else if (event.getWindowId() != null) {
      WindowManager.getInstance().activateWindow(Util.findWindowById(event.getWindowId()));
    }
  }

  protected void dispatchMouseEvent(MouseEventMsgIn event) {
    Component c = null;
    boolean relatedToLastEvent = false;
    if (Util.getWebToolkit().getWindowManager().isLockedToWindowDecorationHandler()) {
      c = Util.getWebToolkit().getWindowManager().getLockedToWindow();
    } else {
      c = Util.getWebToolkit().getWindowManager().getVisibleComponentOnPosition(event.getX(),
          event.getY(), event.getWinId());
      if (relatedToLastEvent(event) && !isJavaFXdragStarted() && !isDndInProgress()) {
        c = (Component) getLastMouseEvent().getSource();
        relatedToLastEvent = true;
      }
    }
    if (c == null) {
      if (Util.getWebToolkit().getPaintDispatcher() != null) {
        Util.getWebToolkit().getPaintDispatcher()
            .notifyCursorUpdate(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR), null, null);
      }
      dispatchExitEvent(System.currentTimeMillis(), 0, -1, -1, event.getX(), event.getY());
      return;
    }
    if (c.isShowing()) {
      MouseEvent e = null;
      int x = 0;
      int y = 0;
      try {
        x = (int) (event.getX() - c.getLocationOnScreen().getX());
        y = (int) (event.getY() - c.getLocationOnScreen().getY());
      } catch (IllegalComponentStateException e1) {
        // in case the window closed in meantime, just ignore this event
        return;
      }
      setLastMousePosition(event.getX(), event.getY());
      long when = System.currentTimeMillis();
      int modifiers = Util.getMouseModifiersAWTFlag(event);
      int id = 0;
      int clickcount = 0;
      int buttons = Util.getMouseButtonsAWTFlag(event.getButton());
      if (buttons != 0 && event.getType() == MouseEventType.mousedown) {
        Window w = (Window) (c instanceof Window ? c : SwingUtilities.windowForComponent(c));
        Util.getWebToolkit().getWindowManager().activateWindow(w, null, x, y, false, true,
            FocusEventCause.MOUSE_EVENT);
      }
      switch (event.getType()) {
        case mousemove:
          id = event.getButtons() != 0 ? MouseEvent.MOUSE_DRAGGED : MouseEvent.MOUSE_MOVED;
          buttons = 0; // in swing mouse move/drag has always MouseEvent.button==0
          e = new MouseEvent(c, id, when, modifiers, x, y, event.getX(), event.getY(), clickcount,
              false, buttons);
          setLastMouseEvent(e);
          dispatchMouseEventInSwing(c, e, relatedToLastEvent);
          break;
        case mouseup:
          if (buttons == 0) {
            // a button must be specified for this event
            break;
          }
          id = MouseEvent.MOUSE_RELEASED;
          boolean popupTrigger = buttons == 3;
          clickcount = computeClickCount(x, y, buttons, false, event.getTimeMilis());
          modifiers = modifiers & (((1 << 6) - 1) | (-(1 << 14)) | MouseEvent.CTRL_DOWN_MASK
              | MouseEvent.ALT_DOWN_MASK | MouseEvent.SHIFT_DOWN_MASK | MouseEvent.META_DOWN_MASK);
          e = new MouseEvent(c, id, when, modifiers, x, y, event.getX(), event.getY(), clickcount,
              popupTrigger, buttons);
          dispatchMouseEventInSwing(c, e, relatedToLastEvent);
          if (isNearLastMousePressEvent(x, y)) {
            e = new MouseEvent(c, MouseEvent.MOUSE_CLICKED, when, modifiers, x, y, event.getX(),
                event.getY(), clickcount, popupTrigger, buttons);
            dispatchMouseEventInSwing(c, e, relatedToLastEvent);
            setLastMouseEvent(e);
            setLastMousePressEvent(e, event.getTimeMilis());
          } else {
            setLastMouseEvent(e);
            setLastMousePressEvent(e, event.getTimeMilis());
          }
          break;
        case mousedown:
          if (buttons == 0) {
            // a button must be specified for this event
            break;
          }
          id = MouseEvent.MOUSE_PRESSED;
          clickcount = computeClickCount(x, y, buttons, true, event.getTimeMilis());
          e = new MouseEvent(c, id, when, modifiers, x, y, event.getX(), event.getY(), clickcount,
              false, buttons);
          dispatchMouseEventInSwing(c, e, relatedToLastEvent);
          setLastMousePressEvent(e, event.getTimeMilis());
          setLastMouseEvent(e);
          break;
        case mousewheel:
          id = MouseEvent.MOUSE_WHEEL;
          buttons = 0;
          modifiers = 0;
          e = new MouseWheelEvent(c, id, when, modifiers, x, y, clickcount, false,
              MouseWheelEvent.WHEEL_UNIT_SCROLL, 3, event.getWheelDelta());
          dispatchMouseEventInSwing(c, e, relatedToLastEvent);
          break;
        case dblclick:
          // e = new MouseEvent(w, MouseEvent.MOUSE_CLICKED, when,
          // modifiers, x, y, event.x, event.y, 2, false, buttons);
          // dispatchEventInSwing(w, e);
          // break;
        default:
          break;
      }
    }
  }

  protected void handleActionEvent(ActionEventMsgIn event) {
    Util.getWebToolkit().processApiEvent(event);
  }

  @Override
  protected void dispatchHandshakeEvent(ConnectionHandshakeMsgIn handshake) {
    PaintDispatcher paintDispatcher = Util.getWebToolkit().getPaintDispatcher();

    Util.getWebToolkit().initSize(handshake.getDesktopWidth(), handshake.getDesktopHeight());
    paintDispatcher.notifyFileDialogActive();
    paintDispatcher.closePasteRequestDialog();
    Util.getWebToolkit().processApiEvent(handshake);
    System.setProperty(Constants.SWING_START_SYS_PROP_TOUCH_MODE, handshake.isTouchMode() + "");

    boolean oldAccessibility = Boolean
        .valueOf(System.getProperty(Constants.SWING_START_SYS_PROP_ACCESSIBILITY_ENABLED, "false"));
    System.setProperty(Constants.SWING_START_SYS_PROP_ACCESSIBILITY_ENABLED,
        handshake.isAccessiblityEnabled() + "");
    if (handshake.isAccessiblityEnabled() && !oldAccessibility) {
      paintDispatcher.notifyAccessibilityInfoUpdate();
      paintDispatcher.clearAccessibilityInfoState();
    }
  }

  protected void handleCopyEvent(final CopyEventMsgIn copy) {
    SwingUtilities.invokeLater(() -> {
      if (copy.getType() != null) {
        switch (copy.getType()) {
          case copy:
            dispatchCopyEvent();
            break;
          case cut:
            dispatchCutEvent();
            break;
          case getFileFromClipboard:
            handleClipboardFileDownload(copy);
        }
      }
    });
  }

  protected void handleClipboardFileDownload(CopyEventMsgIn copy) {
    if (Boolean.getBoolean(Constants.SWING_START_SYS_PROP_ALLOW_DOWNLOAD)) {
      Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
      if (clipboard.isDataFlavorAvailable(DataFlavor.javaFileListFlavor)) {
        try {
          List<?> files = (List<?>) clipboard.getData(DataFlavor.javaFileListFlavor);
          for (Object o : files) {
            File file = (File) o;
            if (file.getAbsolutePath().equals(copy.getFile())) {
              if (file.exists() && !file.isDirectory() && file.canRead()) {
                Util.getWebToolkit().getPaintDispatcher().notifyFileRequested(file, false);
              } else {
                AppLogger.error("Failed to download file " + copy.getFile()
                    + " from clipboard. File is not accessible or is a directory");
              }
            }
          }
        } catch (Exception e) {
          AppLogger.error("Failed to download file " + copy.getFile() + " from clipboard.", e);
        }
      }
    }
  }

  protected void handlePasteEvent(final PasteEventMsgIn paste) {
    AppLogger.debug("WebEventDispatcher.handlePasteEvent", paste);
    SwingUtilities.invokeLater(() -> {
      boolean clipboardRequested =
          Util.getWebToolkit().getPaintDispatcher().closePasteRequestDialog();
      WebClipboardTransferable transferable = new WebClipboardTransferable(paste);
      WebClipboard wc = (WebClipboard) Util.getWebToolkit().getSystemClipboard();
      wc.setBrowserClipboard(transferable);
      if (!transferable.isEmpty()
          && Boolean.getBoolean(Constants.SWING_START_SYS_PROP_ALLOW_LOCAL_CLIPBOARD)) {
        wc.setContents(transferable);
      }
      if (!clipboardRequested) {
        WebEventDispatcher.this.dispatchPasteEvent(paste.isSpecial());
      }
    });
  }

  protected void handleFileSelectionEvent(final FilesSelectedEventMsgIn e) {
    SwingUtilities.invokeLater(() -> {
      JFileChooser fc = Util.getWebToolkit().getPaintDispatcher().getFileChooserDialog();
      if (fc != null) {
        FileDialogEventType fileChooserEventType = Util.getFileChooserEventType(fc);
        boolean saveMode = FileDialogEventType.AutoSave == fileChooserEventType;
        fc.rescanCurrentDirectory();
        if (e.getFiles() != null && e.getFiles().size() > 0) {
          if (fc.isMultiSelectionEnabled()) {
            List<File> arr = new ArrayList<File>();
            for (int i = 0; i < e.getFiles().size(); i++) {
              if (getFileUploadMap().get(e.getFiles().get(i)) != null) {
                arr.add(new File(fc.getCurrentDirectory(),
                    getFileUploadMap().get(e.getFiles().get(i))));
              } else if (saveMode) {
                arr.add(new File(Util.getTimestampedTransferFolder("autosavemulti"),
                    e.getFiles().get(i)));
              }
            }
            fc.setSelectedFiles(arr.toArray(new File[0]));
            AppLogger.info("Files selected :" + arr);
          } else {
            if (getFileUploadMap().get(e.getFiles().get(0)) != null) {
              File f =
                  new File(fc.getCurrentDirectory(), getFileUploadMap().get(e.getFiles().get(0)));
              fc.setSelectedFile(f);
            } else if (saveMode) {
              fc.setSelectedFile(
                  new File(Util.getTimestampedTransferFolder("autosave"), e.getFiles().get(0)));
            }

            if (fc.getFileFilter() != null && fc.getSelectedFile() != null
                && !fc.getFileFilter().accept(fc.getSelectedFile())) {
              for (FileFilter ff : fc.getChoosableFileFilters()) {
                if (ff.accept(fc.getSelectedFile())) {
                  fc.setFileFilter(ff);
                  break;
                }
              }
            }

            AppLogger.info("File selected :"
                + (fc.getSelectedFile() != null ? fc.getSelectedFile().getAbsoluteFile() : null));
          }
          if (FileDialogEventType.AutoUpload == fileChooserEventType
              || FileDialogEventType.AutoSave == fileChooserEventType) {
            fc.approveSelection();
          }
        } else {
          if (FileDialogEventType.AutoUpload == fileChooserEventType
              || FileDialogEventType.AutoSave == fileChooserEventType) {
            fc.cancelSelection();
          }
        }
      }
      getFileUploadMap().clear();
    });
  }

  protected void handleUploadEvent(final UploadEventMsgIn upload) {
    SwingUtilities.invokeLater(() -> {
      JFileChooser dialog = Util.getWebToolkit().getPaintDispatcher().getFileChooserDialog();
      if (dialog != null) {
        File currentDir = dialog.getCurrentDirectory();

        String fileId = upload.getFileId();

        if (currentDir.canWrite()
            && Services.getDataStoreService().dataExists("transfer", fileId)) {
          // FIXME -> move this to some Util class (same is in FileTransferHandler,
          // AbstractPaintDispatcher)
          String fileName = "";
          try {
            String[] fileData = fileId.split("_");
            if (fileData != null) {
              if (fileData.length > 0) {
                fileName = decodeHashedFileData(fileData[0]);
              }
            }
          } catch (Exception e) {
            AppLogger.error("Failed to decode file data [" + fileId + "]!", e);
            return;
          }

          String validFilename = Util.resolveUploadFilename(currentDir, fileName);

          try (InputStream is = Services.getDataStoreService().readData("transfer", fileId)) {
            if (is == null) {
              AppLogger.error("Failed to read file data [" + fileId + "]!");
              return;
            }

            Services.getDataStoreService().writeStreamToFile(is,
                new File(currentDir, validFilename));

            getFileUploadMap().put(fileName, validFilename);

            AppLogger.info("File upload notification received: " + validFilename);
          } catch (Exception e) {
            AppLogger.error(
                "Error while moving uploaded file '" + validFilename + "' to target folder: ", e);
          }
        } else {
          AppLogger.error("Error while uploading file '" + fileId + "'. "
              + (currentDir.canWrite() ? " Upload file " + fileId + " not found"
                  : "Can not write to target folder " + currentDir.getAbsoluteFile()));
        }
      } else {
        AppLogger.error("Error while uploading file. FileChooser dialog instance not found");
      }
    });
  }

  private String decodeHashedFileData(String data) {
    // try {
    // return new String(Base64.getUrlDecoder().decode(data.getBytes("UTF-8")), "UTF-8");
    // }
    return new String(Base64.getUrlDecoder().decode(data.getBytes(StandardCharsets.UTF_8)),
        StandardCharsets.UTF_8);
  }

  protected void handleWindowEvent(WindowEventMsgIn windowUpdate) {
    WebWindowPeer winPeer = Util.findWindowPeerById(windowUpdate.getId());

    if (winPeer == null) {
      return;
    }

    synchronized (Util.getWebToolkit().getTreeLock()) {
      synchronized (WebPaintDispatcher.webPaintLock) {
        final Window win = (Window) winPeer.getTarget();
        if (windowUpdate.getEventType() != null) {
          switch (windowUpdate.getEventType()) {
            case close:
              Toolkit.getDefaultToolkit().getSystemEventQueue()
                  .postEvent(new WindowEvent(win, WindowEvent.WINDOW_CLOSING));
              break;
            case decorate:
              winPeer.setUndecoratedOverride(false);
              break;
            case undecorate:
              winPeer.setUndecoratedOverride(true);
              break;
            case dock:
              winPeer.setUndocked(false);
              break;
            case undock:
              winPeer.setUndocked(true);
              break;
            case focus:
              if (!Util.isFXWindow(win)) {
                win.requestFocus();
              }
              break;
            case maximize:
              if (win instanceof Frame frame) {
                frame.setExtendedState(Frame.MAXIMIZED_BOTH);
              }
              break;
            default:
              break;
          }
        } else {
          SwingUtilities.invokeLater(() -> {
            win.setBounds(windowUpdate.getX(), windowUpdate.getY(), windowUpdate.getWidth(),
                windowUpdate.getHeight());
          });
        }
      }
    }
  }

  @Override
  protected void handleAudioEvent(AudioEventMsgIn event) {
    AudioClip clip = Util.getWebToolkit().getPaintDispatcher().findAudioClip(event.getId());

    if (clip == null) {
      AppLogger.warn("Audio clip [" + event.getId() + "] not found. Ignoring audio event.");
      return;
    }

    if (event.isStop()) {
      clip.notifyPlaybackStopped();
    }
    if (event.isPing()) {
      clip.playbackPing();
    }
  }

}
