package org.webswing.javafx.toolkit;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.CommonDialogs;
import com.sun.glass.ui.Cursor;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.Screen;
import com.sun.glass.ui.Size;
import com.sun.glass.ui.Timer;
import com.sun.glass.ui.View;
import com.sun.glass.ui.Window;
import org.webswing.Constants;
import org.webswing.javafx.toolkit.adaper.WindowAdapter;
import org.webswing.toolkit.util.Util;
import sun.awt.AWTAccessor;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.SecondaryLoop;
import java.awt.Toolkit;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by vikto on 28-Feb-17.
 */
public abstract class AbstractWebApplication extends Application {

  public static final long doubleClickMaxDelay =
      Long.getLong(Constants.SWING_START_SYS_PROP_DOUBLE_CLICK_DELAY, 750);

  LinkedList<SecondaryLoop> loops = new LinkedList<>();

  @Override
  protected void runLoop(Runnable launchable) {
    setEventThread(AWTAccessor.getEventQueueAccessor()
        .getDispatchThread(Toolkit.getDefaultToolkit().getSystemEventQueue()));
    invokeLater(launchable);
  }

  @Override
  protected void _invokeAndWait(Runnable runnable) {
    try {
      SwingUtilities.invokeAndWait(runnable);
    } catch (InterruptedException e) {
    } catch (InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void _invokeLater(Runnable runnable) {
    SwingUtilities.invokeLater(runnable);
  }

  @Override
  protected Object _enterNestedEventLoop() {
    SecondaryLoop loop = Toolkit.getDefaultToolkit().getSystemEventQueue().createSecondaryLoop();
    loops.push(loop);
    loop.enter();
    return loop;
  }

  @Override
  protected void _leaveNestedEventLoop(Object retValue) {
    SecondaryLoop loop = loops.pop();
    if (loop != null) {
      loop.exit();
    }
  }

  @Override
  protected void finishTerminating() {
    super.finishTerminating();
    for (java.awt.Window w : java.awt.Window.getWindows()) {
      if (w.isShowing()) {
        return;
      }
    }
    Util.getWebToolkit().exitSwing(0);
  }

  @Override
  public Window createWindow(Window owner, Screen screen, int styleMask) {
    return new WebWindow(owner, screen, styleMask);
  }

  @Override
  public Window createWindow(long parent) {
    // called from applet, not supported
    throw new UnsupportedOperationException();
  }

  @Override
  public View createView() {
    return new WebFxView();
  }

  @Override
  public Cursor createCursor(int type) {
    return new WebFxCursor(type);
  }

  @Override
  public Cursor createCursor(int x, int y, Pixels pixels) {
    return new WebFxCursor(x, y, pixels);
  }

  @Override
  protected void staticCursor_setVisible(boolean visible) {

  }

  @Override
  protected Size staticCursor_getBestSize(int width, int height) {
    return new Size(width, height);
  }

  @Override
  public Pixels createPixels(int width, int height, ByteBuffer data) {
    return WebsinwgFxToolkitFactory.getFactory().createPixels(width, height, data);
  }

  @Override
  public Pixels createPixels(int width, int height, IntBuffer data) {
    return WebsinwgFxToolkitFactory.getFactory().createPixels(width, height, data);
  }

  @Override
  protected int staticPixels_getNativeFormat() {
    return Pixels.Format.BYTE_BGRA_PRE;
  }

  @Override
  protected double staticScreen_getVideoRefreshPeriod() {
    return 0;
  }

  @Override
  protected CommonDialogs.FileChooserResult staticCommonDialogs_showFileChooser(Window owner,
      String folder, String filename, String title, int type, boolean multipleMode,
      CommonDialogs.ExtensionFilter[] extensionFilters, int defaultFilterIndex) {
    java.awt.Window parent = getWindow(owner);
    JFileChooser fc = new JFileChooser();
    fc.setCurrentDirectory(new File(folder));
    fc.setSelectedFile(new File(filename));
    fc.setDialogTitle(title);
    fc.setMultiSelectionEnabled(multipleMode);
    for (int i = 0; i < extensionFilters.length; i++) {
      List<String> list = extensionFilters[i].getExtensions();
      if (list.size() > 0) {
        fc.addChoosableFileFilter(new FileNameExtensionFilter(extensionFilters[i].getDescription(),
            list.toArray(new String[list.size()])));
      }
    }

    int result;
    if (type == CommonDialogs.Type.OPEN) {
      result = fc.showOpenDialog(parent);
    } else {
      result = fc.showSaveDialog(parent);
    }
    if (result == JFileChooser.APPROVE_OPTION) {
      List<File> selected = new ArrayList<>();
      if (!fc.isMultiSelectionEnabled() && fc.getSelectedFile() != null) {
        selected.add(fc.getSelectedFile());
      } else if (fc.getSelectedFiles() != null) {
        for (File f : fc.getSelectedFiles()) {
          selected.add(f);
        }
      }
      return new CommonDialogs.FileChooserResult(selected, null);
    }
    return new CommonDialogs.FileChooserResult();
  }

  private java.awt.Window getWindow(Window owner) {
    java.awt.Window parent = null;
    if (owner instanceof WebWindow window) {
      WindowAdapter adapter = window.w;
      parent = adapter != null ? adapter.getThis() : null;
    }
    return parent;
  }

  @Override
  protected File staticCommonDialogs_showFolderChooser(Window owner, String folder, String title) {
    java.awt.Window parent = getWindow(owner);
    JFileChooser fc = new JFileChooser();
    fc.setCurrentDirectory(new File(folder));
    fc.setDialogTitle(title);
    fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    int result = fc.showOpenDialog(parent);
    if (result == JFileChooser.APPROVE_OPTION) {
      return fc.getSelectedFile();
    }
    return null;
  }

  @Override
  public Timer createTimer(Runnable runnable) {
    return new WebTimer(runnable);
  }

  @Override
  protected int staticTimer_getMinPeriod() {
    return 0;
  }

  @Override
  protected int staticTimer_getMaxPeriod() {
    return 1000000;
  }

  @Override
  protected long staticView_getMultiClickTime() {
    return doubleClickMaxDelay;
  }

  @Override
  protected int staticView_getMultiClickMaxX() {
    return 0;
  }

  @Override
  protected int staticView_getMultiClickMaxY() {
    return 0;
  }

  @Override
  protected boolean _supportsTransparentWindows() {
    return false;
  }

  @Override
  protected boolean _supportsUnifiedWindows() {
    return false;
  }

  @Override
  protected int _getKeyCodeForChar(char c) {
    return c;
  }
}
