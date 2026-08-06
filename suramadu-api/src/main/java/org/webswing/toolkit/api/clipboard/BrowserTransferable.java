package org.webswing.toolkit.api.clipboard;

import java.awt.Image;
import java.awt.datatransfer.Transferable;

public interface BrowserTransferable extends Transferable {

  Image getImage();

  String getText();

  String getHtml();
}
