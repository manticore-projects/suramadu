package org.webswing.toolkit;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import java.awt.FileDialog;
import java.awt.Window;
import java.awt.peer.FileDialogPeer;
import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

abstract public class WebFileDialogPeer extends WebWindowPeer implements FileDialogPeer {

  private final FileDialog dialog;

  @SuppressWarnings("deprecation")
  private JFileChooser fc = new JFileChooser() {
    private static final long serialVersionUID = 1L;

    public void approveSelection() {
      super.approveSelection();
      dialog.setFile(fc.getSelectedFile().getName());
      dialog.setDirectory(fc.getCurrentDirectory().getPath() + File.separator);
      dialog.hide();
    }

    public void cancelSelection() {
      super.cancelSelection();
      dialog.setFile(null);
      dialog.hide();
    }
  };

  public WebFileDialogPeer(FileDialog paramFileDialog) {
    super(new JDialog());
    dialog = paramFileDialog;
    setFile(dialog.getFile());
    fc.setMultiSelectionEnabled(false);
  }

  @Override
  public void blockWindows(List<Window> windows) {}

  @Override
  public void setDirectory(String dir) {}

  @Override
  public void setFile(String file) {
    if (file != null) {
      fc.setSelectedFile(new File(file));
    }
  }

  @Override
  public void setFilenameFilter(final FilenameFilter filter) {
    FileFilter ffilter = new FileFilter() {

      @Override
      public String getDescription() {
        return "filter";
      }

      @Override
      public boolean accept(File f) {
        return filter.accept(f.getParentFile(), f.getName());
      }
    };
    fc.setFileFilter(ffilter);
  }

  @Override
  public void show() {
    new Thread(new Runnable() {
      @Override
      public void run() {
        if (dialog.getMode() == FileDialog.LOAD) {
          fc.showOpenDialog(dialog.getOwner());
        } else {
          fc.showSaveDialog(dialog.getOwner());
        }
      }
    }).start();

  }

  public void hide() {}
}
