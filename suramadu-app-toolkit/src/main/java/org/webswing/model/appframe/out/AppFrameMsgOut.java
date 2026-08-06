package org.webswing.model.appframe.out;

import org.webswing.model.MsgOut;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class AppFrameMsgOut implements MsgOut {

  @Serial
  private static final long serialVersionUID = 6019708608380425820L;
  private StartApplicationMsgOut startApplication;
  private LinkActionMsgOut linkAction;
  private WindowMoveActionMsgOut moveAction;
  private CopyEventMsgOut copyEvent;
  private PasteRequestMsgOut pasteRequest;
  private FileDialogEventMsgOut fileDialogEvent;
  private List<WindowMsgOut> windows = new ArrayList<>();
  private WindowMsgOut closedWindow;
  private SimpleEventMsgOut event;
  private JsEvalRequestMsgOut jsRequest;
  private JsResultMsgOut javaResponse;
  private PixelsAreaRequestMsgOut pixelsRequest;
  private PlaybackInfoMsgOut playback;
  private String instanceId;
  private String startTimestamp = "" + System.currentTimeMillis();
  private String sendTimestamp;
  private FocusEventMsgOut focusEvent;
  private List<ComponentTreeMsgOut> componentTree;
  private boolean directDraw;
  private ActionEventMsgOut actionEvent;
  private boolean compositingWM;
  private AudioEventMsgOut audioEvent;
  private WindowDockMsgOut dockAction;
  private AccessibilityMsgOut accessible;
  private List<WindowSwitchMsgOut> windowSwitchList;
  private CursorChangeEventMsgOut cursorChangeEvent;

  public AppFrameMsgOut() {}

  public WindowMsgOut getOrCreateWindowById(String guid) {
    for (WindowMsgOut w : windows) {
      if (w.getId().equals(guid)) {
        return w;
      }
    }
    WindowMsgOut window = new WindowMsgOut();
    window.setId(guid);
    windows.add(window);
    return window;
  }

  public PasteRequestMsgOut getPasteRequest() {
    return pasteRequest;
  }

  public void setPasteRequest(PasteRequestMsgOut pasteRequest) {
    this.pasteRequest = pasteRequest;
  }

  public StartApplicationMsgOut getStartApplication() {
    return startApplication;
  }

  public void setStartApplication(StartApplicationMsgOut startApplication) {
    this.startApplication = startApplication;
  }

  public LinkActionMsgOut getLinkAction() {
    return linkAction;
  }

  public void setLinkAction(LinkActionMsgOut linkAction) {
    this.linkAction = linkAction;
  }

  public WindowMoveActionMsgOut getMoveAction() {
    return moveAction;
  }

  public void setMoveAction(WindowMoveActionMsgOut moveAction) {
    this.moveAction = moveAction;
  }

  public CopyEventMsgOut getCopyEvent() {
    return copyEvent;
  }

  public void setCopyEvent(CopyEventMsgOut copyEvent) {
    this.copyEvent = copyEvent;
  }

  public FileDialogEventMsgOut getFileDialogEvent() {
    return fileDialogEvent;
  }

  public void setFileDialogEvent(FileDialogEventMsgOut fileDialogEvent) {
    this.fileDialogEvent = fileDialogEvent;
  }

  public List<WindowMsgOut> getWindows() {
    return windows;
  }

  public void setWindows(List<WindowMsgOut> windows) {
    this.windows = windows;
  }

  public WindowMsgOut getClosedWindow() {
    return closedWindow;
  }

  public void setClosedWindow(WindowMsgOut closedWindow) {
    this.closedWindow = closedWindow;
  }

  public SimpleEventMsgOut getEvent() {
    return event;
  }

  public void setEvent(SimpleEventMsgOut event) {
    this.event = event;
  }

  public JsEvalRequestMsgOut getJsRequest() {
    return jsRequest;
  }

  public void setJsRequest(JsEvalRequestMsgOut jsRequest) {
    this.jsRequest = jsRequest;
  }

  public JsResultMsgOut getJavaResponse() {
    return javaResponse;
  }

  public void setJavaResponse(JsResultMsgOut javaResponse) {
    this.javaResponse = javaResponse;
  }

  public String getInstanceId() {
    return instanceId;
  }

  public void setInstanceId(String instanceId) {
    this.instanceId = instanceId;
  }

  public PlaybackInfoMsgOut getPlayback() {
    return playback;
  }

  public void setPlayback(PlaybackInfoMsgOut playback) {
    this.playback = playback;
  }

  public String getStartTimestamp() {
    return startTimestamp;
  }

  public void setStartTimestamp(String startTimestamp) {
    this.startTimestamp = startTimestamp;
  }

  public String getSendTimestamp() {
    return sendTimestamp;
  }

  public void setSendTimestamp(String sendTimestamp) {
    this.sendTimestamp = sendTimestamp;
  }

  public PixelsAreaRequestMsgOut getPixelsRequest() {
    return pixelsRequest;
  }

  public void setPixelsRequest(PixelsAreaRequestMsgOut pixelsRequest) {
    this.pixelsRequest = pixelsRequest;
  }

  public FocusEventMsgOut getFocusEvent() {
    return focusEvent;
  }

  public void setFocusEvent(FocusEventMsgOut focusEvent) {
    this.focusEvent = focusEvent;
  }

  public List<ComponentTreeMsgOut> getComponentTree() {
    return componentTree;
  }

  public void setComponentTree(List<ComponentTreeMsgOut> componentTree) {
    this.componentTree = componentTree;
  }

  public ActionEventMsgOut getActionEvent() {
    return actionEvent;
  }

  public void setActionEvent(ActionEventMsgOut actionEvent) {
    this.actionEvent = actionEvent;
  }

  public boolean isDirectDraw() {
    return directDraw;
  }

  public void setDirectDraw(boolean directDraw) {
    this.directDraw = directDraw;
  }

  public boolean isCompositingWM() {
    return compositingWM;
  }

  public void setCompositingWM(boolean compositingWM) {
    this.compositingWM = compositingWM;
  }

  public AudioEventMsgOut getAudioEvent() {
    return audioEvent;
  }

  public void setAudioEvent(AudioEventMsgOut audioEvent) {
    this.audioEvent = audioEvent;
  }

  public WindowDockMsgOut getDockAction() {
    return dockAction;
  }

  public void setDockAction(WindowDockMsgOut dockAction) {
    this.dockAction = dockAction;
  }

  public AccessibilityMsgOut getAccessible() {
    return accessible;
  }

  public void setAccessible(AccessibilityMsgOut accessible) {
    this.accessible = accessible;
  }

  public List<WindowSwitchMsgOut> getWindowSwitchList() {
    return windowSwitchList;
  }

  public void setWindowSwitchList(List<WindowSwitchMsgOut> windowSwitchList) {
    this.windowSwitchList = windowSwitchList;
  }

  public CursorChangeEventMsgOut getCursorChangeEvent() {
    return cursorChangeEvent;
  }

  public void setCursorChangeEvent(CursorChangeEventMsgOut cursorChangeEvent) {
    this.cursorChangeEvent = cursorChangeEvent;
  }

}
