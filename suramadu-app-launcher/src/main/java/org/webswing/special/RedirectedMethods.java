package org.webswing.special;

import org.webswing.SwingMain;
import org.webswing.toolkit.WebPrinterJobWrapper;
import org.webswing.util.AppLogger;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.attribute.AttributeSet;
import javax.swing.JDesktopPane;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class RedirectedMethods {

  public static void exit(int status) {
    System.exit(1);
  }

  public static void setErr(PrintStream s) {
    // do nothing
  }

  public static void setOut(PrintStream s) {
    // do nothing
  }

  public static void writeObject(Object o) {
    // bug in java serialization- serializing class from custom classloader fails
    System.out.println("DUMMY writeObject(Object o)");
  }

  public static ClassLoader getSystemClassLoader() {
    return SwingMain.swingLibClassLoader;
  }

  public static ClassLoader getPlatformClassLoader() {
    return SwingMain.swingLibClassLoader;
  }

  // This is only for IntelliJ IDEA
  public static ClassLoader getPlatformLoaderParentIfOnJdk9() {
    return SwingMain.swingLibClassLoader;
  }

  public static URL getSystemResource(String name) {
    return SwingMain.swingLibClassLoader.getResource(name);
  }

  public static InputStream getSystemResourceAsStream(String name) {
    return SwingMain.swingLibClassLoader.getResourceAsStream(name);
  }

  public static Enumeration<URL> getSystemResources(String name) throws IOException {
    return SwingMain.swingLibClassLoader.getResources(name);
  }

  public static File[] getRootDirectories() {
    List<File> roots = new ArrayList<>();

    for (Path rootPath : FileSystems.getDefault().getRootDirectories()) {
      roots.add(rootPath.toFile()); // Convert Path to File
    }

    return roots.toArray(new File[0]); // Convert List<File> to File[]
  }

  @SuppressWarnings("restriction")
  public static File[] listRoots() {

    return (File[]) getRootDirectories();
  }

  public static void putClientProperty(JDesktopPane target, Object key, Object value) {
    if ("JDesktopPane.dragMode".equals(key) && "outline".equals(value)) {
      AppLogger.warn("Outline drag mode for JDesktop pane is not supported in Webswing.");
    } else {
      target.putClientProperty(key, value);
    }
  }

  public static PrintService[] lookupPrintServices(DocFlavor flavor, AttributeSet attributes) {
    return WebPrinterJobWrapper.wrappedLookupPrintServices(flavor, attributes);
  }

  private RedirectedMethods() {}
}
