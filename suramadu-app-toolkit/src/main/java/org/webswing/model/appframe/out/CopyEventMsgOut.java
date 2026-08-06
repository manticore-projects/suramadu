package org.webswing.model.appframe.out;

import org.webswing.model.MsgOut;

import java.io.Serial;
import java.util.List;

public class CopyEventMsgOut implements MsgOut {

  @Serial
  private static final long serialVersionUID = -5791089710920190332L;
  private String text;
  private String html;
  private byte[] img;
  private List<String> files;
  private boolean other;

  public CopyEventMsgOut() {}

  public CopyEventMsgOut(String text, String html, byte[] img, List<String> files, boolean other) {
    super();
    this.text = text;
    this.html = html;
    this.img = img;
    this.files = files;
    this.other = other;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getHtml() {
    return html;
  }

  public void setHtml(String html) {
    this.html = html;
  }

  public byte[] getImg() {
    return img;
  }

  public void setImg(byte[] img) {
    this.img = img;
  }

  public List<String> getFiles() {
    return files;
  }

  public void setFiles(List<String> files) {
    this.files = files;
  }

  public boolean isOther() {
    return other;
  }

  public void setOther(boolean other) {
    this.other = other;
  }
}
