package org.webswing.model.appframe.in;

import org.webswing.model.MsgIn;

import java.io.Serial;
import java.util.List;

public class AppFrameMsgIn implements MsgIn {

  @Serial
  private static final long serialVersionUID = -7046154705244466351L;
  private List<InputEventMsgIn> events;
  private PasteEventMsgIn paste;
  private CopyEventMsgIn copy;
  private UploadEventMsgIn upload;
  private FilesSelectedEventMsgIn selected;
  private JsResultMsgIn jsResponse; // java2js call return value
  private JavaEvalRequestMsgIn javaRequest; // js2java call
  private PixelsAreaResponseMsgIn pixelsResponse; // WebRobotPeer
  private WindowEventMsgIn window;
  private AudioEventMsgIn audio;
  private ActionEventMsgIn action;

  public List<InputEventMsgIn> getEvents() {
    return events;
  }

  public void setEvents(List<InputEventMsgIn> events) {
    this.events = events;
  }

  public CopyEventMsgIn getCopy() {
    return copy;
  }

  public void setCopy(CopyEventMsgIn copy) {
    this.copy = copy;
  }

  public PasteEventMsgIn getPaste() {
    return paste;
  }

  public void setPaste(PasteEventMsgIn paste) {
    this.paste = paste;
  }

  public FilesSelectedEventMsgIn getSelected() {
    return selected;
  }

  public void setSelected(FilesSelectedEventMsgIn selected) {
    this.selected = selected;
  }

  public JsResultMsgIn getJsResponse() {
    return jsResponse;
  }

  public void setJsResponse(JsResultMsgIn jsResponse) {
    this.jsResponse = jsResponse;
  }

  public JavaEvalRequestMsgIn getJavaRequest() {
    return javaRequest;
  }

  public void setJavaRequest(JavaEvalRequestMsgIn javaRequest) {
    this.javaRequest = javaRequest;
  }

  public PixelsAreaResponseMsgIn getPixelsResponse() {
    return pixelsResponse;
  }

  public void setPixelsResponse(PixelsAreaResponseMsgIn pixelsResponse) {
    this.pixelsResponse = pixelsResponse;
  }

  public WindowEventMsgIn getWindow() {
    return window;
  }

  public void setWindow(WindowEventMsgIn window) {
    this.window = window;
  }

  public ActionEventMsgIn getAction() {
    return action;
  }

  public void setAction(ActionEventMsgIn action) {
    this.action = action;
  }

  public UploadEventMsgIn getUpload() {
    return upload;
  }

  public void setUpload(UploadEventMsgIn upload) {
    this.upload = upload;
  }

  public AudioEventMsgIn getAudio() {
    return audio;
  }

  public void setAudio(AudioEventMsgIn audio) {
    this.audio = audio;
  }

}
