package org.webswing.dispatch;

import org.webswing.audio.AudioClip;
import org.webswing.model.appframe.out.AccessibilityMsgOut;
import org.webswing.model.appframe.out.FocusEventMsgOut;
import org.webswing.toolkit.api.clipboard.PasteRequestContext;
import org.webswing.toolkit.api.clipboard.WebswingClipboardData;
import org.webswing.toolkit.api.component.HtmlPanel;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.RepaintManager;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;
import java.util.List;
import java.util.Map;

public interface PaintDispatcher {

  Object webPaintLock = new Object();

  void clientReadyToReceive(); // paint ack received from JS

  RepaintManager getDefaultRepaintManager();

  void notifyWindowAreaRepainted(String guid, Rectangle repaintedArea); // area has been repainted

  void notifyWindowAreaVisible(String guid, Rectangle visibleArea); // area became visible in window
                                                                    // manager

  void notifyWindowBoundsChanged(String guid, Rectangle newBounds); // window resized

  void notifyWindowClosed(String guid); // window closed

  void notifyWindowActivated(Window activeWindow);

  void notifyWindowDeactivated(Window oldActiveWindow);

  void notifyWindowZOrderChanged(Window w);

  void notifyWindowMaximized(JFrame target);

  void notifyWindowRepaintAll();

  void notifyWindowDecorationUpdated(String guid, Rectangle bounds, Insets insets);

  void notifyBackgroundAreaVisible(Rectangle toRepaint);

  void notifyScreenSizeChanged(int oldWidht, int oldHeight, int screenWidth, int screenHeight);

  void notifyWindowMoved(Window w, int zIndex, Rectangle from, Rectangle to);

  void notifyWindowDockStateChanged();

  void notifyWindowSwitchList();

  void notifyFocusEvent(FocusEventMsgOut msg);

  void notifyAccessibilityInfoUpdate(Component a, int x, int y);

  void notifyAccessibilityInfoUpdate(AccessibilityMsgOut msg);

  void notifyAccessibilityInfoUpdate();

  void clearAccessibilityInfoState();

  void notifyCursorUpdate(Cursor cursor, Cursor overridenCursorName, String winId);

  void notifyOpenLinkAction(URI uri);

  void notifyUrlRedirect(String url);

  void notifyCopyEvent(WebswingClipboardData data);

  void requestBrowserClipboard(PasteRequestContext ctx);

  boolean closePasteRequestDialog();

  void notifyFileDialogActive(Window window);

  void notifyFileDialogActive();

  void notifyFileDialogHidden();

  void notifyDownloadSelectedFile();

  void notifyDeleteSelectedFile();

  void notifyFileRequested(File file, boolean preview);

  void notifyPrintPdfFile(ByteArrayOutputStream out);

  JFileChooser getFileChooserDialog();

  void notifyApplicationExiting();

  void notifyApplicationExiting(int waitBeforeKill);

  void notifyComponentTreeRequested(); // test tool

  void notifyActionEvent(String windowId, String actionName, String data, byte[] binaryData);

  void registerWebContainer(Container container);

  Map<Window, List<Container>> getRegisteredWebContainersAsMap();

  void registerHtmlPanel(HtmlPanel hp);

  Map<Window, List<HtmlPanel>> getRegisteredHtmlPanelsAsMap();

  HtmlPanel findHtmlPanelById(String id);

  void notifyNewDirtyRegionQueued();

  void notifyAudioEventDispose(AudioClip clip);

  void notifyAudioEventUpdate(AudioClip clip, Float time, Integer loop);

  void notifyAudioEventStop(AudioClip clip);

  void notifyAudioEventPlay(AudioClip clip, byte[] data, Float time, Integer loop);

  AudioClip findAudioClip(String id);

  void notifyThreadDumpCreated(String reason);

  void notifySessionTimeoutWarning();

  void notifySessionTimedOut();

  void notifyNewSessionStats(int edtUnresponsivenessSeconds);

  void notifySessionDataChanged();

  void registerFileChooserWindows(JFileChooser fileChooser, Window parent);

  JFileChooser findRegisteredFileChooser(Window w);
}
