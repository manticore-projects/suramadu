package org.webswing.toolkit;

import org.webswing.Constants;
import org.webswing.toolkit.api.clipboard.BrowserTransferable;
import org.webswing.toolkit.api.clipboard.PasteRequestContext;
import org.webswing.toolkit.api.clipboard.WebswingClipboardData;
import org.webswing.toolkit.util.Services;
import org.webswing.toolkit.util.Util;
import org.webswing.util.AppLogger;
import sun.awt.datatransfer.TransferableProxy;

import javax.swing.SwingUtilities;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WebClipboard extends Clipboard {
  private static DataFlavor htmlDf;
  private static DataFlavor plaintxtDf;

  static {

    try {
      htmlDf = new DataFlavor("text/html;class=java.lang.String");
      plaintxtDf = new DataFlavor("text/plain;class=java.lang.String");
    } catch (ClassNotFoundException e) {
      AppLogger.error("initialization error:", e);
    }
  }

  public final static DataFlavor HTML_FLAVOR = htmlDf;

  private final boolean isSystemClipboard;
  public final ClipboardOwner owner = new ClipboardOwner() {

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {}
  };
  private WebClipboardTransferable browserClipboard;

  public WebClipboard(String name, boolean isSystemClipboard) {
    super(name);
    this.isSystemClipboard = isSystemClipboard;
  }

  public void setContents(Transferable contents) {
    // skip setting the content if browser sends the same text content as already stored in
    // clipboard - to preserve non-string mime types
    if (!(contents instanceof WebClipboardTransferable
        && !contents.isDataFlavorSupported(DataFlavor.imageFlavor)
        && stringFlavorsEquals(this.contents, contents))) {
      super.setContents(contents, owner);
    }
  }

  @Override
  public synchronized void setContents(Transferable contents, ClipboardOwner owner) {
    contents = new TransferableProxy(contents, true);

    super.setContents(contents, owner);
    if (isSystemClipboard
        && Boolean.getBoolean(Constants.SWING_START_SYS_PROP_ALLOW_LOCAL_CLIPBOARD)) {
      WebswingClipboardData data = toWebswingClipboardData(contents);
      Util.getWebToolkit().getPaintDispatcher().notifyCopyEvent(data);
    }
  }

  public static WebswingClipboardData toWebswingClipboardData(Transferable contents) {
    WebswingClipboardData data = new WebswingClipboardData();
    if (contents != null) {
      if (contents.isDataFlavorSupported(HTML_FLAVOR)) {
        try {
          Object transferData = contents.getTransferData(HTML_FLAVOR);
          data.setHtml(transferData.toString());
        } catch (Exception e) {
          AppLogger.error("WebClipboard:setContent:HTML", e);
        }
      }

      if (contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
        try {
          data.setText((String) contents.getTransferData(DataFlavor.stringFlavor));
        } catch (Exception e) {
          AppLogger.error("WebClipboard:setContent:Plain", e);
        }
      } else if (contents.isDataFlavorSupported(plaintxtDf)) {
        try {
          data.setText((String) contents.getTransferData(plaintxtDf));
        } catch (Exception e) {
          AppLogger.error("WebClipboard:setContent:text/plain", e);
        }
      }
      if (contents.isDataFlavorSupported(DataFlavor.imageFlavor)) {
        try {
          Image image = (Image) contents.getTransferData(DataFlavor.imageFlavor);
          if (image != null) {
            BufferedImage result = new BufferedImage(image.getWidth(null), image.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);
            Graphics g = result.getGraphics();
            g.drawImage(image, 0, 0, null);
            g.dispose();
            data.setImg(Services.getImageService().getPngImage(result));
          }
        } catch (Exception e) {
          AppLogger.error("WebClipboard:setContent:Image", e);
        }
      }
      if (contents.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
        try {
          List<?> fileList = (List<?>) contents.getTransferData(DataFlavor.javaFileListFlavor);
          if (fileList != null) {
            ArrayList<String> files = new ArrayList<String>();
            for (Object o : fileList) {
              if (o instanceof File f) {
                if (Boolean.getBoolean(Constants.SWING_START_SYS_PROP_ALLOW_DOWNLOAD)) {
                  if (f.exists() && f.canRead() && !f.isDirectory()) {
                    files.add(f.getAbsolutePath());
                  } else {
                    files.add("#" + f.getAbsolutePath());
                  }
                } else {
                  files.add("#Downloading not allowed.");
                  break;
                }
              }
            }
            data.setFiles(files);
          }
        } catch (Exception e) {
          AppLogger.error("WebClipboard:setContent:Files", e);
        }
      }
    }
    return data;
  }

  private boolean stringFlavorsEquals(Transferable a, Transferable b) {
    try {
      if (!a.isDataFlavorSupported(DataFlavor.stringFlavor)
          && !b.isDataFlavorSupported(DataFlavor.stringFlavor)) {
        return true;
      }
      if (a.isDataFlavorSupported(DataFlavor.stringFlavor)
          && b.isDataFlavorSupported(DataFlavor.stringFlavor)) {
        String valA = (String) a.getTransferData(DataFlavor.stringFlavor);
        String valB = (String) b.getTransferData(DataFlavor.stringFlavor);
        return valA != null && valA.equals(valB);
      }
      return false;
    } catch (Exception e) {
      return false;
    }
  }

  public void setBrowserClipboard(WebClipboardTransferable browserClipboard) {
    this.browserClipboard = browserClipboard;
  }

  public WebClipboardTransferable getBrowserClipboard() {
    return browserClipboard;
  }

  public BrowserTransferable requestClipboard(final PasteRequestContext ctx) {
    if (SwingUtilities.isEventDispatchThread()) {
      setBrowserClipboard(new WebClipboardTransferable(null));
      Util.getWebToolkit().getPaintDispatcher().requestBrowserClipboard(ctx);
      if (browserClipboard != null && !browserClipboard.isEmpty()) {
        return browserClipboard;
      }
      return null;
    } else {
      try {
        SwingUtilities.invokeAndWait(new Runnable() {
          @Override
          public void run() {
            requestClipboard(ctx);
          }
        });
        if (browserClipboard != null && !browserClipboard.isEmpty()) {
          return browserClipboard;
        }
      } catch (Exception e) {
        AppLogger.error("Failed to process paste request.", e);
      }
      return null;
    }
  }
}
