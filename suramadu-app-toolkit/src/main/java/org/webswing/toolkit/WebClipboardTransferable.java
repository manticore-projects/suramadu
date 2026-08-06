package org.webswing.toolkit;

import org.webswing.model.appframe.in.PasteEventMsgIn;
import org.webswing.toolkit.api.clipboard.BrowserTransferable;
import org.webswing.toolkit.util.Services;

import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;

public class WebClipboardTransferable implements BrowserTransferable {

  private Image image;
  private String text;
  private String html;

  public WebClipboardTransferable(PasteEventMsgIn paste) {
    if (paste != null) {
      if (paste.getImg() != null) {
        this.image = Services.getImageService().readFromDataUrl(paste.getImg());
      }
      this.text = paste.getText();
      this.html = paste.getHtml();
    }
  }

  public boolean isEmpty() {
    if (image == null && text == null && html == null) {
      return true;
    }
    return false;
  }

  public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
    if (isDataFlavorSupported(flavor)) {
      if (DataFlavor.imageFlavor.equals(flavor)) {
        return image;
      }
      if (DataFlavor.stringFlavor.equals(flavor)) {
        return text;
      }
      if (WebClipboard.HTML_FLAVOR.equals(flavor)) {
        return html;
      }
    }
    throw new UnsupportedFlavorException(flavor);
  }

  public boolean isDataFlavorSupported(DataFlavor flavor) {
    if (DataFlavor.imageFlavor.equals(flavor)) {
      return image != null;
    }
    if (DataFlavor.stringFlavor.equals(flavor)) {
      return text != null;
    }
    if (WebClipboard.HTML_FLAVOR.equals(flavor)) {
      return html != null;
    }
    return false;
  }

  public DataFlavor[] getTransferDataFlavors() {
    return new DataFlavor[] {DataFlavor.imageFlavor, DataFlavor.stringFlavor,
        WebClipboard.HTML_FLAVOR};
  }

  public Image getImage() {
    return image;
  }

  public String getText() {
    return text;
  }

  public String getHtml() {
    return html;
  }
}
