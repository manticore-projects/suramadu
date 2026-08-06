package org.webswing.javafx.toolkit;

import com.sun.glass.ui.SystemClipboard;
import org.webswing.toolkit.WebClipboard;
import org.webswing.toolkit.util.Util;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by vikto on 07-Mar-17.
 */
public class WebFxClipboard extends SystemClipboard {

  private final WebClipboard c;

  public final ClipboardOwner owner = new ClipboardOwner() {

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {}
  };

  protected WebFxClipboard(String name) {
    super(name);
    c = (WebClipboard) Util.getWebToolkit().getSystemClipboard();
  }

  @Override
  protected boolean isOwner() {
    return false;
  }

  @Override
  protected void pushToSystem(HashMap<String, Object> cacheData, int supportedActions) {
    c.setContents(new WebFxTransferable(cacheData), c.owner);
  }

  @Override
  protected void pushTargetActionToSystem(int actionDone) {

  }

  @Override
  protected Object popFromSystem(String mimeType) {
    try {
      return c.getData(toSwingFlavor(mimeType));
    } catch (Exception e) {
      return null;
    }

  }

  @Override
  protected int supportedSourceActionsFromSystem() {
    return ACTION_ANY;
  }

  @Override
  protected String[] mimesFromSystem() {
    List<String> result = new LinkedList<>();
    for (DataFlavor f : c.getAvailableDataFlavors()) {
      result.add(toJfxFlavor(f));
    }
    return result.toArray(new String[result.size()]);
  }

  private class WebFxTransferable implements Transferable {
    HashMap<String, Object> data;
    private DataFlavor[] flavors;

    public WebFxTransferable(HashMap<String, Object> cacheData) {
      this.data = cacheData;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
      if (flavors == null) {
        List<DataFlavor> result = new LinkedList<>();
        for (String jfxType : data.keySet()) {
          DataFlavor flavor = toSwingFlavor(jfxType);
          if (flavor != null) {
            result.add(flavor);
          }
        }
        flavors = result.toArray(new DataFlavor[result.size()]);
      }
      return flavors;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor dataFlavor) {
      return data.get(toJfxFlavor(dataFlavor)) != null;
    }

    @Override
    public Object getTransferData(DataFlavor dataFlavor)
        throws UnsupportedFlavorException, IOException {
      return data.get(toJfxFlavor(dataFlavor));
    }
  }

  private static DataFlavor toSwingFlavor(String jfxType) {
    if (com.sun.glass.ui.Clipboard.TEXT_TYPE.equals(jfxType)) {
      return DataFlavor.stringFlavor;
    }
    if (com.sun.glass.ui.Clipboard.HTML_TYPE.equals(jfxType)) {
      return WebClipboard.HTML_FLAVOR;
    }
    try {
      return new DataFlavor(jfxType);
    } catch (ClassNotFoundException e) {
      return null;
    }
  }

  private static String toJfxFlavor(DataFlavor flavor) {
    if (DataFlavor.stringFlavor.equals(flavor)) {
      return com.sun.glass.ui.Clipboard.TEXT_TYPE;
    }
    if (WebClipboard.HTML_FLAVOR.equals(flavor)) {
      return com.sun.glass.ui.Clipboard.HTML_TYPE;
    }
    return flavor.getMimeType();
  }
}
