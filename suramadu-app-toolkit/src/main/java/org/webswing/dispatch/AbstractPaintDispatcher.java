package org.webswing.dispatch;

import org.webswing.Constants;
import org.webswing.audio.AudioClip;
import org.webswing.model.app.out.*;
import org.webswing.model.appframe.out.*;
import org.webswing.model.appframe.out.LinkActionMsgOut.LinkActionType;
import org.webswing.toolkit.WebCursor;
import org.webswing.toolkit.WebToolkit;
import org.webswing.toolkit.WebWindowPeer;
import org.webswing.toolkit.api.clipboard.PasteRequestContext;
import org.webswing.toolkit.api.clipboard.WebswingClipboardData;
import org.webswing.toolkit.api.file.WebswingFileChooserUtil;
import org.webswing.toolkit.extra.IsolatedFsShellFolderManager;
import org.webswing.toolkit.util.Services;
import org.webswing.toolkit.util.ToolkitUtil;
import org.webswing.toolkit.util.Util;
import org.webswing.util.AppLogger;
import org.webswing.util.CpuMonitor;
import org.webswing.util.NamedThreadFactory;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractPaintDispatcher implements PaintDispatcher {
  private static final long AUDIO_PLAYBACK_TIMEOUT =
      Long.getLong(Constants.AUDIO_PLAYBACK_TIMEOUT, Constants.AUDIO_PLAYBACK_TIMEOUT_DEFAULT);

  private volatile Map<String, Set<Rectangle>> areasToUpdate = new HashMap<>();
  private volatile FocusEventMsgOut focusEvent;
  private volatile AccessibilityMsgOut accessible;
  private final AtomicBoolean clientReadyToReceive = new AtomicBoolean(true);
  private final Long ackTimeout = Long.getLong(Constants.PAINT_ACK_TIMEOUT, 5000);
  private long lastReadyStateTime;
  private JFileChooser fileChooserDialog;
  private JDialog clipboardDialog;
  private ScheduledExecutorService executorService;
  private ScheduledExecutorService audioChecker;

  private final Object accessibilityLock = new Object();
  private boolean accessibilityUpdateScheduled;
  private Component accessibilityComponent;
  private Integer accessibilityX;
  private Integer accessibilityY;

  private final WeakHashMap<Window, WeakReference<JFileChooser>> registeredFileChooserWindows =
      new WeakHashMap<>();
  private final FileChooserShowingListener fileChooserVisibilityListener =
      new FileChooserShowingListener();

  private final WeakHashMap<String, WeakReference<AudioClip>> registeredAudioClips =
      new WeakHashMap<>();
  private SecondaryLoop clipboardDialogLoop;

  public AbstractPaintDispatcher() {
    Runnable audioCheckerTask = () -> {
      try {
        synchronized (registeredAudioClips) {
          for (WeakReference<AudioClip> wr : registeredAudioClips.values()) {
            if (wr != null && wr.get() != null) {
              AudioClip clip = wr.get();
              long lastPlaybackTimestamp = clip.getLastPlaybackTimestamp();
              if (lastPlaybackTimestamp > 0) {
                // if marked as playing in browser, check the last ping is within a timeout
                if (lastPlaybackTimestamp + AUDIO_PLAYBACK_TIMEOUT < System.currentTimeMillis()) {
                  clip.notifyPlaybackStopped();
                }
              }
            }
          }
        }
      } catch (Exception e) {
        AppLogger.error("Failed to check running audio clips!", e);
      }
    };
    long audioCheckerInterval = Long.getLong(Constants.AUDIO_CHECKER_INTERVAL_SECONDS,
        Constants.AUDIO_CHECKER_INTERVAL_SECONDS_DEFAULT);
    getAudioChecker().scheduleWithFixedDelay(audioCheckerTask, audioCheckerInterval,
        audioCheckerInterval, TimeUnit.SECONDS);
  }

  public void clientReadyToReceive() {
    synchronized (webPaintLock) {
      clientReadyToReceive.set(true);
    }
  }

  @Override
  public void notifyNewDirtyRegionQueued() {}

  @Override
  public RepaintManager getDefaultRepaintManager() {
    return RepaintManager.currentManager(null);
  }

  public void notifyScreenSizeChanged(int oldWidht, int oldHeight, int screenWidth,
      int screenHeight) {
    clientReadyToReceive();
    Util.resetWindowsPosition(oldWidht, oldHeight); // in case windows moved out of screen by
                                                    // resizing screen.
    notifyWindowRepaintAll();
  }

  public void notifyWindowDecorationUpdated(String guid, Rectangle bounds, Insets insets) {
    if (insets != null && bounds != null) {
      addDirtyArea(guid, new Rectangle(0, 0, bounds.width, insets.top)); // top
      addDirtyArea(guid, new Rectangle(0, 0, insets.left, bounds.height)); // left
      addDirtyArea(guid,
          new Rectangle(bounds.width - insets.right, 0, insets.right, bounds.height)); // right
      addDirtyArea(guid,
          new Rectangle(0, bounds.height - insets.bottom, bounds.width, insets.bottom)); // bottom
    }
  }

  public void notifyWindowClosed(String guid) {
    removeDirtyArea(guid);

    AppToServerFrameMsgOut msgOut = new AppToServerFrameMsgOut();

    AppFrameMsgOut f = new AppFrameMsgOut();
    WindowMsgOut fdEvent = new WindowMsgOut();
    fdEvent.setId(guid);
    f.setClosedWindow(fdEvent);

    AppLogger.debug("WebPaintDispatcher:notifyWindowClosed", guid);

    sendObject(msgOut, f);
  }

  @SuppressWarnings("restriction")
  public void notifyWindowRepaintAll() {
    notifyBackgroundAreaVisible(new Rectangle(Util.getWebToolkit().getScreenSize()));
    if (Util.isDD()) {
      Services.getDirectDrawService().resetCache();
      Util.repaintAllWindow();
    } else {
      for (Window w : Util.getAllWindows()) {
        if (w.isShowing()) {
          addDirtyArea(w);
        }
      }
    }
  }

  public void notifyActionEvent(String windowId, String actionName, String data,
      byte[] binaryData) {
    AppToServerFrameMsgOut msgOut = new AppToServerFrameMsgOut();

    AppFrameMsgOut f = new AppFrameMsgOut();

    ActionEventMsgOut actionEvent = new ActionEventMsgOut();
    actionEvent.setWindowId(windowId);
    actionEvent.setActionName(actionName);
    actionEvent.setData(data);
    actionEvent.setBinaryData(binaryData);

    f.setActionEvent(actionEvent);

    AppLogger.debug("WebPaintDispatcher:notifyActionEvent", f);

    sendObject(msgOut, f);
  }


  public void notifyOpenLinkAction(URI uri) {
    AppToServerFrameMsgOut msgOut = new AppToServerFrameMsgOut();

    AppFrameMsgOut f = new AppFrameMsgOut();
    LinkActionMsgOut linkAction =
        new LinkActionMsgOut(LinkActionMsgOut.LinkActionType.url, uri.toString());
    f.setLinkAction(linkAction);

    AppLogger.info("WebPaintDispatcher:notifyOpenLinkAction", uri);

    sendObject(msgOut, f);
  }

  public void notifyCopyEvent(WebswingClipboardData data) {
    AppToServerFrameMsgOut msgOut = new AppToServerFrameMsgOut();

    AppFrameMsgOut f = new AppFrameMsgOut();
    CopyEventMsgOut copyEvent;
    copyEvent =
        new CopyEventMsgOut(data.getText(), data.getHtml(), data.getImg(), data.getFiles(), false);
    f.setCopyEvent(copyEvent);

    AppLogger.debug("WebPaintDispatcher:notifyCopyEvent", f);

    sendObject(msgOut, f);
  }

  public void notifyFileDialogActive(Window window) {
    setFileChooserDialog(Util.discoverFileChooser(window));
    notifyFileDialogActive();
  }

  public void notifyFileDialogActive() {
    if (!SwingUtilities.isEventDispatchThread()) {
      SwingUtilities.invokeLater(this::notifyFileDialogActive);
    } else {
      if (getFileChooserDialog() != null) {
        AppToServerFrameMsgOut msgOut = new AppToServerFrameMsgOut();

        AppFrameMsgOut f = new AppFrameMsgOut();

        boolean allowDownload =
            Boolean.valueOf(System.getProperty(Constants.SWING_START_SYS_PROP_ALLOW_DOWNLOAD));
        boolean allowUpload =
            Boolean.valueOf(System.getProperty(Constants.SWING_START_SYS_PROP_ALLOW_UPLOAD));
        boolean allowDelete =
            Boolean.valueOf(System.getProperty(Constants.SWING_START_SYS_PROP_ALLOW_DELETE));

        FileDialogEventMsgOut fdEvent = new FileDialogEventMsgOut(getFileChooserDialog(),
            allowDownload, allowUpload, allowDelete);
        f.setFileDialogEvent(fdEvent);
        FileDialogEventMsgOut.FileDialogEventType fileChooserEventType =
            Util.getFileChooserEventType(getFileChooserDialog());
        if (fileChooserEventType == FileDialogEventMsgOut.FileDialogEventType.AutoUpload
            && getFileChooserDialog().getFileSelectionMode() == JFileChooser.DIRECTORIES_ONLY) {
          // open dialog with auto upload enabled will automatically select the transfer folder
          SwingUtilities.invokeLater(() -> {
            try {
              getFileChooserDialog().setSelectedFile(
                  new File(getFileChooserDialog().getCurrentDirectory().getCanonicalPath()));
              getFileChooserDialog().approveSelection();
            } catch (IOException e) {
              getFileChooserDialog().cancelSelection();
            }
          });
          return;
        }
        fdEvent.setEventType(fileChooserEventType);
        if (FileDialogEventMsgOut.FileDialogEventType.AutoUpload == fileChooserEventType
            || FileDialogEventMsgOut.FileDialogEventType.AutoSave == fileChooserEventType) {
          fdEvent.setAllowDelete(false);
          fdEvent.setAllowDownload(false);
          fdEvent.setAllowUpload(false);
          if (FileDialogEventMsgOut.FileDialogEventType.AutoUpload == fileChooserEventType) {
            getFileChooserDialog()
                .setCurrentDirectory(Util.getTimestampedTransferFolder("autoupload"));
          }
          Window d = SwingUtilities.getWindowAncestor(getFileChooserDialog());
          d.setBounds(0, 0, 1, 1);
          SwingUtilities.invokeLater(() -> { // ensure LaF does not change the bounds in meantime
                                             // (issue n.162)
            Window d1 = SwingUtilities.getWindowAncestor(getFileChooserDialog());
            d1.setBounds(0, 0, 1, 1);
          });

        }
        fdEvent.setSelection(Util.getFileChooserSelection(getFileChooserDialog()));
        fdEvent.addFilter(getFileChooserDialog().getChoosableFileFilters());
        fdEvent.setMultiSelection(getFileChooserDialog().isMultiSelectionEnabled());

        AppLogger
            .info("WebPaintDispatcher:notifyFileTransferBarActive " + fileChooserEventType.name());

        sendObject(msgOut, f);
      }
    }
  }

  public void notifyFileDialogHidden() {
    AppToServerFrameMsgOut msgOut = new AppToServerFrameMsgOut();

    AppFrameMsgOut f = new AppFrameMsgOut();

    boolean allowDownload =
        Boolean.valueOf(System.getProperty(Constants.SWING_START_SYS_PROP_ALLOW_DOWNLOAD));
    boolean allowUpload =
        Boolean.valueOf(System.getProperty(Constants.SWING_START_SYS_PROP_ALLOW_UPLOAD));
    boolean allowDelete =
        Boolean.valueOf(System.getProperty(Constants.SWING_START_SYS_PROP_ALLOW_DELETE));

    FileDialogEventMsgOut fdEvent =
        new FileDialogEventMsgOut(null, allowDownload, allowUpload, allowDelete);
    fdEvent.setEventType(FileDialogEventMsgOut.FileDialogEventType.Close);
    f.setFileDialogEvent(fdEvent);

    AppLogger.info("WebPaintDispatcher:notifyFileTransferBarHidden "
        + FileDialogEventMsgOut.FileDialogEventType.Close.name());

    validateSelection(getFileChooserDialog());

    if (Boolean.getBoolean(Constants.SWING_START_SYS_PROP_ALLOW_AUTO_DOWNLOAD)) {
      if (getFileChooserDialog() != null
          && getFileChooserDialog().getDialogType() == JFileChooser.SAVE_DIALOG) {
        try {
          Field resultValueField = JFileChooser.class.getDeclaredField("returnValue");
          resultValueField.setAccessible(true);
          if (resultValueField.get(getFileChooserDialog()).equals(JFileChooser.APPROVE_OPTION)) {
            File saveFile = getFileChooserDialog().getSelectedFile();
            if (saveFile != null) {
              String fileId = createHashedFileId(saveFile.getName(), saveFile.length());
              String overwriteDetails = null;
              if (saveFile.exists()) {
                overwriteDetails = saveFile.length() + "|" + saveFile.lastModified();
              }

              boolean registered = Services.getDataStoreService().registerFileWhenReady(saveFile,
                  fileId, 30, TimeUnit.MINUTES, getUserId(), getInstanceId(), overwriteDetails);

              if (registered) {
                AppToServerFrameMsgOut saveMsgOut = new AppToServerFrameMsgOut();
                AppFrameMsgOut frame = new AppFrameMsgOut();
                frame.setLinkAction(new LinkActionMsgOut(LinkActionType.file, fileId));

                sendObject(saveMsgOut, frame);
              }
            }
          }
        } catch (Exception e) {
          AppLogger.warn("Save file dialog's file monitoring failed: " + e.getMessage());
        }
      }
    }
    setFileChooserDialog(null);

    sendObject(msgOut, f);
  }

  public void notifyDownloadSelectedFile() {
    if (getFileChooserDialog() != null
        && Boolean.getBoolean(Constants.SWING_START_SYS_PROP_ALLOW_DOWNLOAD)) {
      File file = getFileChooserDialog().getSelectedFile();
      if (file != null && file.exists() && !file.isDirectory() && file.canRead()) {
        String fileId = createHashedFileId(file.getName(), file.length());

        boolean registered = Services.getDataStoreService().registerFile(file, fileId, 30,
            TimeUnit.MINUTES, getUserId(), getInstanceId());

        if (registered) {
          AppToServerFrameMsgOut msgOut = new AppToServerFrameMsgOut();
          AppFrameMsgOut frame = new AppFrameMsgOut();
          frame.setLinkAction(new LinkActionMsgOut(LinkActionType.file, fileId));

          sendObject(msgOut, frame);
        }
      }
    }
  }

  public void notifyDeleteSelectedFile() {
    if (getFileChooserDialog() != null
        && Boolean.getBoolean(Constants.SWING_START_SYS_PROP_ALLOW_DELETE)) {
      File[] selected = getFileChooserDialog().getSelectedFiles();
      if ((selected == null || selected.length == 0)
          && getFileChooserDialog().getSelectedFile() != null) {
        selected = new File[] {getFileChooserDialog().getSelectedFile()};
      }
      if (selected != null) {
        for (File f : selected) {
          if (f.exists() && f.canWrite()) {
            // FIXME delete in dataStore ?
            boolean deleted = f.delete();
            if (!deleted) {
              AppLogger
                  .info("notifyDeleteSelectedFile: Failed to delete file:" + f.getAbsolutePath());
            }
          }
        }
      }
      getFileChooserDialog().rescanCurrentDirectory();
    }
  }

  @Override
  public void notifyFileRequested(File file, boolean preview) {
    file = file.getAbsoluteFile();
    String fileId = createHashedFileId(file.getName(), file.length());

    boolean registered = Services.getDataStoreService().registerFile(file, fileId, 30,
        TimeUnit.MINUTES, getUserId(), getInstanceId());

    if (registered) {
      AppToServerFrameMsgOut msgOut = new AppToServerFrameMsgOut();
      String extension = getFileExtension(file.getName());
      LinkActionType actionType =
          preview && ".pdf".equalsIgnoreCase(extension) ? LinkActionType.print
              : LinkActionType.file;

      AppFrameMsgOut frame = new AppFrameMsgOut();
      frame.setLinkAction(new LinkActionMsgOut(actionType, fileId));

      sendObject(msgOut, frame);
    }
  }

  public void notifyPrintPdfFile(ByteArrayOutputStream out) {
    String id = createHashedFileId(UUID.randomUUID().toString() + ".pdf", out.size());

    boolean registered = Services.getDataStoreService().registerData(out.toByteArray(), id, 30,
        TimeUnit.MINUTES, getUserId(), getInstanceId(), true);

    if (registered) {
      AppToServerFrameMsgOut msgOut = new AppToServerFrameMsgOut();
      AppFrameMsgOut frame = new AppFrameMsgOut();
      frame.setLinkAction(new LinkActionMsgOut(LinkActionType.print, id));

      sendObject(msgOut, frame);
    }
  }

  private String getInstanceId() {
    return System.getProperty(Constants.SWING_START_SYS_PROP_INSTANCE_ID);
  }

  private String getUserId() {
    return System.getProperty(Constants.SWING_START_SYS_PROP_USER_ID);
  }

  private String createHashedFileId(String name, long length) {
    String hashedName = name;
    String hashedUserId = getUserId();
    String hashedSize = length + "";
    hashedName =
        new String(Base64.getUrlEncoder().encode(hashedName.getBytes(StandardCharsets.UTF_8)),
            StandardCharsets.UTF_8);
    hashedUserId =
        new String(Base64.getUrlEncoder().encode(hashedUserId.getBytes(StandardCharsets.UTF_8)),
            StandardCharsets.UTF_8);
    hashedSize =
        new String(Base64.getUrlEncoder().encode(hashedSize.getBytes(StandardCharsets.UTF_8)),
            StandardCharsets.UTF_8);
    return hashedName + "_" + hashedUserId + "_" + hashedSize;
  }

  private String getFileExtension(String name) {
    int lastIndexOf = name.lastIndexOf(".");
    if (lastIndexOf == -1) {
      return "";
    }
    return name.substring(lastIndexOf);
  }

  public void notifyApplicationExiting() {
    notifyApplicationExiting(
        Integer.getInteger(Constants.SWING_START_SYS_PROP_WAIT_FOR_EXIT, 30000));
  }

  public void notifyApplicationExiting(int waitBeforeKill) {
    AppToServerFrameMsgOut msgOut = new AppToServerFrameMsgOut();

    ExitMsgOut f = new ExitMsgOut();
    f.setWaitForExit(waitBeforeKill);

    msgOut.setExit(f);

    sendObject(msgOut, null);

    getExecutorService().shutdownNow();
    getAudioChecker().shutdownNow();
  }

  public void notifyUrlRedirect(String url) {
    AppToServerFrameMsgOut msgOut = new AppToServerFrameMsgOut();

    AppFrameMsgOut f = new AppFrameMsgOut();
    f.setLinkAction(new LinkActionMsgOut(LinkActionMsgOut.LinkActionType.redirect, url));

    sendObject(msgOut, f);
  }

  public void requestBrowserClipboard(PasteRequestContext ctx) {
    AppToServerFrameMsgOut msgOut = new AppToServerFrameMsgOut();

    AppFrameMsgOut f = new AppFrameMsgOut();
    PasteRequestMsgOut paste = new PasteRequestMsgOut();
    paste.setTitle(ctx.getTitle());
    paste.setMessage(ctx.getMessage());
    f.setPasteRequest(paste);

    sendObject(msgOut, f);


    if (clipboardDialogLoop != null) {
      clipboardDialogLoop.exit();
    }

    clipboardDialogLoop = Toolkit.getDefaultToolkit().getSystemEventQueue().createSecondaryLoop();
    clipboardDialogLoop.enter(); // this call is blocking until paste request dialog is closed
  }

  public boolean closePasteRequestDialog() {
    if (clipboardDialogLoop != null) {
      return clipboardDialogLoop.exit();
    }
    return false;
  }

  public void notifyComponentTreeRequested() {
    if (!Util.isTestMode()) {
      return;
    }

    AppToServerFrameMsgOut msgOut = new AppToServerFrameMsgOut();

    AppFrameMsgOut f = new AppFrameMsgOut();
    f.setComponentTree(ToolkitUtil.getComponentTree());

    AppLogger.debug("WebPaintDispatcher:sendComponentTree");

    sendObject(msgOut, f);
  }

  @Override
  public void notifyWindowSwitchList() {
    AppToServerFrameMsgOut msgOut = new AppToServerFrameMsgOut();

    AppFrameMsgOut f = new AppFrameMsgOut();
    f.setWindowSwitchList(Util.getWindowSwitchList());

    AppLogger.debug("WebPaintDispatcher:notifyWindowSwitchList");

    sendObject(msgOut, f);
  }

  protected ScheduledExecutorService getExecutorService() {
    if (this.executorService == null) {
      this.executorService = Executors.newScheduledThreadPool(1,
          NamedThreadFactory.getInstance("Webswing Paint Dispatcher"));
    }
    return this.executorService;
  }

  protected ScheduledExecutorService getAudioChecker() {
    if (this.audioChecker == null) {
      this.audioChecker = Executors.newScheduledThreadPool(1,
          NamedThreadFactory.getInstance("Webswing Audio Checker"));
    }
    return this.audioChecker;
  }

  protected void sendObject(AppToServerFrameMsgOut msgOut, AppFrameMsgOut frame) {
    AppLogger.debug("WebPaintDispatcher:sendJsonObject", msgOut);
    Services.getConnectionService().sendObject(msgOut, frame);
  }

  protected boolean isClientReadyToReceiveOrResetAfterTimedOut() {
    synchronized (webPaintLock) {
      if (!clientReadyToReceive.get()) {
        if (System.currentTimeMillis() - lastReadyStateTime > ackTimeout) {
          AppLogger.debug("paintDispatcher.clientReadyToReceive re-enabled after timeout");
          if (Util.isDD()) {
            Services.getDirectDrawService().resetCache();
          }
          clientReadyToReceive();
          lastReadyStateTime = System.currentTimeMillis();
          return true;
        }
        return false;
      } else {
        lastReadyStateTime = System.currentTimeMillis();
        return true;
      }
    }
  }

  protected boolean isClientReadyToReceive() {
    synchronized (webPaintLock) {
      return clientReadyToReceive.get();
    }
  }

  protected void setClientNotReady() {
    synchronized (webPaintLock) {
      clientReadyToReceive.set(false);
    }
  }

  protected Map<String, Set<Rectangle>> popProcessableDirtyAreas() {
    Map<String, Set<Rectangle>> currentAreasToUpdate;
    currentAreasToUpdate = areasToUpdate;
    areasToUpdate = Util.postponeNonShowingAreas(currentAreasToUpdate);
    return currentAreasToUpdate;
  }

  protected void addDirtyArea(String guid, Rectangle repaintedArea, boolean reset) {
    synchronized (webPaintLock) {
      if (repaintedArea.width > 0 && repaintedArea.height > 0) {
        if (areasToUpdate.containsKey(guid)) {
          Set<Rectangle> rset = areasToUpdate.get(guid);
          if (reset) {
            rset.clear();
          }
          rset.add(repaintedArea);
        } else {
          Set<Rectangle> rset = new HashSet<>();
          rset.add(repaintedArea);
          areasToUpdate.put(guid, rset);
        }
        AppLogger.trace("WebPaintDispatcher:addDirtyArea", guid, repaintedArea);
      }
    }
  }

  protected void addDirtyArea(String guid, Rectangle repaintedArea) {
    addDirtyArea(guid, repaintedArea, false);
  }

  protected void addDirtyArea(Window w) {
    Rectangle bounds = w.getBounds();
    WebWindowPeer peer = (WebWindowPeer) WebToolkit.targetToPeer(w);
    if (peer != null) {
      addDirtyArea(peer.getGuid(), new Rectangle(0, 0, bounds.width, bounds.height));
    }
  }

  private void removeDirtyArea(String guid) {
    synchronized (webPaintLock) {
      areasToUpdate.remove(guid);
    }
  }

  public void notifyFocusEvent(FocusEventMsgOut msg) {
    if (msg == null) {
      return;
    }

    AppToServerFrameMsgOut msgOut = new AppToServerFrameMsgOut();
    AppFrameMsgOut f = new AppFrameMsgOut();
    f.setFocusEvent(msg);

    AppLogger.debug("WebPaintDispatcher:sendNotifyFocusEvent", f);

    sendObject(msgOut, f);

    notifyAccessibilityInfoUpdate();
  }

  public void notifyAccessibilityInfoUpdate(Component a, int x, int y) {
    if (!Util.isAccessibilityEnabled()) {
      return;
    }

    synchronized (accessibilityLock) {
      accessibilityComponent = a;
      accessibilityX = x;
      accessibilityY = y;

      if (accessibilityUpdateScheduled) {
        return;
      }
      accessibilityUpdateScheduled = true;
    }

    SwingUtilities.invokeLater(() -> {
      processAccessibility();
    });
  }

  public void notifyAccessibilityInfoUpdate() {
    if (!Util.isAccessibilityEnabled()) {
      return;
    }

    synchronized (accessibilityLock) {
      accessibilityComponent = null;
      accessibilityX = null;
      accessibilityY = null;

      if (accessibilityUpdateScheduled) {
        return;
      }
      accessibilityUpdateScheduled = true;
    }

    SwingUtilities.invokeLater(() -> {
      processAccessibility();
    });
  }

  private void processAccessibility() {
    synchronized (accessibilityLock) {
      if (accessibilityComponent != null) {
        sendNotifyAccessibilityInfoUpdate(Services.getConnectionService()
            .getAccessibilityInfo(accessibilityComponent, accessibilityX, accessibilityY));
      } else {
        sendNotifyAccessibilityInfoUpdate(Services.getConnectionService().getAccessibilityInfo());
      }
      accessibilityUpdateScheduled = false;
    }
  }

  @Override
  public void clearAccessibilityInfoState() {
    accessible = null;
  }

  @Override
  public void notifyAccessibilityInfoUpdate(AccessibilityMsgOut msg) {
    sendNotifyAccessibilityInfoUpdate(msg);
  }

  private void sendNotifyAccessibilityInfoUpdate(AccessibilityMsgOut newAccessible) {
    if (newAccessible == null || (accessible != null && newAccessible.equals(accessible))) {
      return;
    }
    accessible = newAccessible;

    AppToServerFrameMsgOut msgOut = new AppToServerFrameMsgOut();

    AppFrameMsgOut f = new AppFrameMsgOut();
    f.setAccessible(accessible);

    AppLogger.debug("WebPaintDispatcher:sendNotifyAccessibilityInfo", f);

    sendObject(msgOut, f);
  }

  public JFileChooser getFileChooserDialog() {
    return fileChooserDialog;
  }

  protected void setFileChooserDialog(JFileChooser fileChooserDialog) {
    this.fileChooserDialog = fileChooserDialog;
  }

  protected JDialog getClipboardDialog() {
    return clipboardDialog;
  }

  protected void setClipboardDialog(JDialog clipboardDialog) {
    this.clipboardDialog = clipboardDialog;
  }

  public void notifyCursorUpdate(Cursor cursor, Cursor overridenCursorName, String winId) {
    String webcursorName = null;
    Cursor webcursor = null;
    if (overridenCursorName == null) {
      if (cursor == null) {
        webcursorName = CursorChangeEventMsgOut.DEFAULT_CURSOR;

      } else {
        switch (cursor.getType()) {
          case Cursor.DEFAULT_CURSOR:
            webcursorName = CursorChangeEventMsgOut.DEFAULT_CURSOR;
            break;
          case Cursor.HAND_CURSOR:
            webcursorName = CursorChangeEventMsgOut.HAND_CURSOR;
            break;
          case Cursor.CROSSHAIR_CURSOR:
            webcursorName = CursorChangeEventMsgOut.CROSSHAIR_CURSOR;
            break;
          case Cursor.MOVE_CURSOR:
            webcursorName = CursorChangeEventMsgOut.MOVE_CURSOR;
            break;
          case Cursor.TEXT_CURSOR:
            webcursorName = CursorChangeEventMsgOut.TEXT_CURSOR;
            break;
          case Cursor.WAIT_CURSOR:
            webcursorName = CursorChangeEventMsgOut.WAIT_CURSOR;
            break;
          case Cursor.E_RESIZE_CURSOR:
          case Cursor.W_RESIZE_CURSOR:
            webcursorName = CursorChangeEventMsgOut.EW_RESIZE_CURSOR;
            break;
          case Cursor.N_RESIZE_CURSOR:
          case Cursor.S_RESIZE_CURSOR:
            webcursorName = CursorChangeEventMsgOut.NS_RESIZE_CURSOR;
            break;
          case Cursor.NW_RESIZE_CURSOR:
          case Cursor.SE_RESIZE_CURSOR:
            webcursorName = CursorChangeEventMsgOut.BACKSLASH_RESIZE_CURSOR;
            break;
          case Cursor.NE_RESIZE_CURSOR:
          case Cursor.SW_RESIZE_CURSOR:
            webcursorName = CursorChangeEventMsgOut.SLASH_RESIZE_CURSOR;
            break;
          case Cursor.CUSTOM_CURSOR:
            webcursorName = cursor.getName();
            break;
          default:
            webcursorName = CursorChangeEventMsgOut.DEFAULT_CURSOR;
        }
      }
      webcursor = cursor;
    } else {
      webcursor = overridenCursorName;
      webcursorName = overridenCursorName.getName();
    }
    String currentCursor = getCurrentCursor(winId);
    if (currentCursor != null && !currentCursor.equals(webcursorName)) {
      AppToServerFrameMsgOut msgOut = new AppToServerFrameMsgOut();

      CursorChangeEventMsgOut cursorChange = new CursorChangeEventMsgOut(webcursorName);
      cursorChange.setWinId(winId);
      if (webcursor instanceof WebCursor c) {
        BufferedImage img = c.getImage();
        cursorChange.setB64img(Services.getImageService().getPngImage(img));
        cursorChange.setX(c.getHotSpot() != null ? c.getHotSpot().x : 0);
        cursorChange.setY(c.getHotSpot() != null ? c.getHotSpot().y : 0);

        byte[] converted = Util.convertCursor(img, cursorChange.getX(), cursorChange.getY());
        if (converted != null) {
          int id = Arrays.hashCode(converted);
          String fileId = createHashedFileId("c" + id + ".cur", converted.length);

          if (!Services.getDataStoreService().dataExists("transfer", fileId)) {
            Services.getDataStoreService().registerData(converted, fileId, 1, TimeUnit.DAYS,
                getUserId(), getInstanceId(), false);
          }
          cursorChange.setCurFile(fileId);
        }
      }

      setCurrentCursor(winId, webcursorName);

      AppLogger.debug("WebPaintDispatcher:notifyCursorUpdate", cursorChange);

      AppFrameMsgOut appFrame = new AppFrameMsgOut();
      appFrame.setCursorChangeEvent(cursorChange);

      sendObject(msgOut, appFrame);
    }
  }

  protected abstract String getCurrentCursor(String winId);

  protected abstract void setCurrentCursor(String winId, String cursor);

  protected void validateSelection(JFileChooser fileChooserDialog) {
    if (Boolean.getBoolean(Constants.SWING_START_SYS_PROP_ISOLATED_FS)
        && fileChooserDialog != null) {

      try {
        if (fileChooserDialog.getSelectedFile() != null) {
          if (!IsolatedFsShellFolderManager
              .isSubfolderOfRoots(fileChooserDialog.getSelectedFile())) {
            throw new IOException("Invalid selection " + fileChooserDialog.getSelectedFile());
          }
        }
        if (fileChooserDialog.getSelectedFiles() != null
            && fileChooserDialog.getSelectedFiles().length > 0) {
          for (File selection : fileChooserDialog.getSelectedFiles()) {
            if (!IsolatedFsShellFolderManager.isSubfolderOfRoots(selection)) {
              throw new IOException("Invalid selection " + selection);
            }
          }
        }
      } catch (IOException e) {
        AppLogger.error("Selection is outside isolated path", e);
        fileChooserDialog.cancelSelection();
      }
    }
  }

  public void notifyAudioEventPlay(AudioClip clip, byte[] data, Float time, Integer loop) {
    AppToServerFrameMsgOut msgOut = new AppToServerFrameMsgOut();

    AppFrameMsgOut f = new AppFrameMsgOut();
    f.setAudioEvent(
        new AudioEventMsgOut(clip.getId(), AudioEventMsgOut.AudioEventType.play, data, time, loop));

    AppLogger.debug("WebPaintDispatcher:notifyAudioEvent play");

    sendObject(msgOut, f);

    registerAudioClip(clip);
  }

  public void notifyAudioEventStop(AudioClip clip) {
    AppToServerFrameMsgOut msgOut = new AppToServerFrameMsgOut();

    AppFrameMsgOut f = new AppFrameMsgOut();
    f.setAudioEvent(new AudioEventMsgOut(clip.getId(), AudioEventMsgOut.AudioEventType.stop));

    AppLogger.debug("WebPaintDispatcher:notifyAudioEvent stop");

    sendObject(msgOut, f);
  }

  public void notifyAudioEventUpdate(AudioClip clip, Float time, Integer loop) {
    AppToServerFrameMsgOut msgOut = new AppToServerFrameMsgOut();

    AppFrameMsgOut f = new AppFrameMsgOut();
    f.setAudioEvent(
        new AudioEventMsgOut(clip.getId(), AudioEventMsgOut.AudioEventType.update, time, loop));

    AppLogger.debug("WebPaintDispatcher:notifyAudioEvent update");

    sendObject(msgOut, f);

    registerAudioClip(clip);
  }

  public void notifyAudioEventDispose(AudioClip clip) {
    AppToServerFrameMsgOut msgOut = new AppToServerFrameMsgOut();

    AppFrameMsgOut f = new AppFrameMsgOut();
    f.setAudioEvent(new AudioEventMsgOut(clip.getId(), AudioEventMsgOut.AudioEventType.dispose));

    AppLogger.debug("WebPaintDispatcher:notifyAudioEvent dispose");

    sendObject(msgOut, f);

    synchronized (registeredAudioClips) {
      registeredAudioClips.remove(clip.getId());
    }
  }

  public void notifyThreadDumpCreated(String reason) {
    AppToServerFrameMsgOut msgOut = new AppToServerFrameMsgOut();

    ThreadDumpMsgOut msg = new ThreadDumpMsgOut();
    msg.setTimestamp(System.currentTimeMillis());
    msg.setReason(reason);
    msg.setDumpId(Util.saveThreadDump(reason));

    msgOut.setThreadDump(msg);

    sendObject(msgOut, null);
  }

  @Override
  public void notifyNewSessionStats(int edtUnresponsivenessSeconds) {
    AppToServerFrameMsgOut msgOut = new AppToServerFrameMsgOut();

    JvmStatsMsgOut msg = new JvmStatsMsgOut();
    if (Util.getWebToolkit().isStatisticsLoggingEnabled()) {
      int mb = 1024 * 1024;
      Runtime runtime = Runtime.getRuntime();
      msg.setHeapSize(runtime.maxMemory() / mb);
      msg.setHeapSizeUsed((runtime.totalMemory() - runtime.freeMemory()) / mb);
      msg.setCpuUsage(CpuMonitor.getCpuUtilization());
    }
    msg.setEdtPingSeconds(edtUnresponsivenessSeconds);

    msgOut.setJvmStats(msg);

    sendObject(msgOut, null);
  }

  @Override
  public void notifySessionDataChanged() {
    AppToServerFrameMsgOut msgOut = new AppToServerFrameMsgOut();

    SessionDataMsgOut msg = new SessionDataMsgOut(Util.isApplet(), Util.isSessionLoggingEnabled(),
        Util.getWebToolkit().getRecordingStatus(), Util.getWebToolkit().getRecordingFileName(),
        Util.getWebToolkit().getMirroringStatus(),
        Util.getWebToolkit().isStatisticsLoggingEnabled());
    msgOut.setSessionData(msg);

    sendObject(msgOut, null);
  }

  @Override
  public void notifySessionTimeoutWarning() {
    AppToServerFrameMsgOut msgOut = new AppToServerFrameMsgOut();
    AppFrameMsgOut f = SimpleEventMsgOut.sessionTimeoutWarning.buildMsgOut();

    sendObject(msgOut, f);
  }

  @Override
  public void notifySessionTimedOut() {
    AppToServerFrameMsgOut msgOut = new AppToServerFrameMsgOut();
    AppFrameMsgOut f = SimpleEventMsgOut.sessionTimedOutNotification.buildMsgOut();

    sendObject(msgOut, f);
  }

  private void registerAudioClip(AudioClip clip) {
    synchronized (registeredAudioClips) {
      if (!registeredAudioClips.containsKey(clip.getId())) {
        registeredAudioClips.put(clip.getId(), new WeakReference<AudioClip>(clip));
        return;
      }

      WeakReference<AudioClip> wr = registeredAudioClips.get(clip.getId());
      if (wr == null || wr.get() == null) {
        registeredAudioClips.put(clip.getId(), new WeakReference<AudioClip>(clip));
      }
    }
  }

  public AudioClip findAudioClip(String id) {
    synchronized (registeredAudioClips) {
      WeakReference<AudioClip> wr = registeredAudioClips.get(id);
      if (wr != null && wr.get() != null) {
        return wr.get();
      }
      return null;
    }
  }

  public void registerFileChooserWindows(JFileChooser chooser, Window parent) {
    if (parent != null) {
      this.registeredFileChooserWindows.put(parent, new WeakReference<>(chooser));
      if (chooser != null) {
        chooser.removeComponentListener(fileChooserVisibilityListener); // prevent having double
                                                                        // listener
        chooser.addComponentListener(fileChooserVisibilityListener);
        chooser.removeHierarchyListener(fileChooserVisibilityListener); // prevent having double
                                                                        // listener
        chooser.addHierarchyListener(fileChooserVisibilityListener);
      }
    }
  }

  public JFileChooser findRegisteredFileChooser(Window parent) {
    if (parent != null) {
      WeakReference<JFileChooser> chooserRef = this.registeredFileChooserWindows.get(parent);
      if (chooserRef != null) {
        JFileChooser chooser = chooserRef.get();
        if (chooser != null && SwingUtilities.getWindowAncestor(chooser) == parent) {
          chooser.putClientProperty(WebswingFileChooserUtil.CUSTOM_FILE_CHOOSER, true);
          return chooser;
        }
      }
    }
    return null;
  }

  private class FileChooserShowingListener extends ComponentAdapter implements HierarchyListener {
    @Override
    public void componentShown(ComponentEvent e) {
      notifyShow((JFileChooser) e.getComponent());
    }

    private void notifyShow(JFileChooser chooser) {
      if (Util.discoverFileChooser(
          Util.getWebToolkit().getWindowManager().getActiveWindow()) == chooser) {
        setFileChooserDialog(chooser);
        notifyFileDialogActive();
      }
    }

    @Override
    public void componentHidden(ComponentEvent e) {
      notifyHide((JFileChooser) e.getComponent());

    }

    private void notifyHide(JFileChooser chooser) {
      if (getFileChooserDialog() == chooser) {
        Util.getWebToolkit().getPaintDispatcher().notifyFileDialogHidden();
      }
    }

    @Override
    public void hierarchyChanged(HierarchyEvent e) {
      if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) == HierarchyEvent.SHOWING_CHANGED) {
        if (e.getComponent().isShowing()) {
          notifyShow((JFileChooser) e.getComponent());
        } else {
          notifyHide((JFileChooser) e.getComponent());
        }
      }
    }
  }
}
