package org.webswing.toolkit.api.clipboard;

import java.util.List;

public class WebswingClipboardData {
  private String text;
  private String html;
  private byte[] img;
  private List<String> files;

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
}
