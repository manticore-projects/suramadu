package org.webswing;

import org.webswing.ext.services.ToolkitFXService;
import org.webswing.toolkit.util.Services;
import org.webswing.toolkit.util.Util;
import org.webswing.util.AppLogger;
import org.webswing.util.ClasspathUtil;

import javax.swing.SwingUtilities;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

public class SwingMain {

  public static ClassLoader swingLibClassLoader;

  public static void main(String[] args) {
    try {

      String relativeBase = System.getProperty(Constants.SWING_START_SYS_PROP_APP_HOME);
      String cp = System.getProperty(Constants.SWING_START_SYS_PROP_CLASS_PATH);
      URL[] urls = ClasspathUtil.populateClassPath(cp, relativeBase);
      /*
       * wrap into additional URLClassLoader with class path urls because
       * some resources may contain classes from packages that should be loaded
       * with parent class loader that otherwise would not have a classpath
       */
      ClassLoader wrapper = new URLClassLoader(urls, SwingMain.class.getClassLoader());
      swingLibClassLoader = Services.getClassLoaderService().createSwingClassLoader(urls, wrapper);
      initTempFolder();

      startSwingApp(args);

    } catch (Exception e) {
      AppLogger.fatal("SwingMain:main", e);
      System.exit(1);
    }
  }

  private static void startSwingApp(String[] args) throws Exception {
    setupContextClassLoader(swingLibClassLoader);
    Class<?> clazz = swingLibClassLoader
        .loadClass(System.getProperty(Constants.SWING_START_SYS_PROP_MAIN_CLASS));
    Class<?>[] mainArgType = {String[].class};
    Method main = clazz.getMethod("main", mainArgType);
    Util.getWebToolkit().startDispatchers();
    initializeJavaFX();
    Object[] argsArray = {args};
    main.invoke(null, argsArray);
  }

  public static void initializeJavaFX() throws InvocationTargetException, InterruptedException {
    if (Constants.SWING_START_SYS_PROP_JFX_TOOLKIT_WEB
        .equals(System.getProperty(Constants.SWING_START_SYS_PROP_JFX_TOOLKIT))) {

      // Fix jvm crash starting javafx app on windows - See:
      // https://bugs.openjdk.java.net/browse/JDK-8201539
      if (System.getProperty("os.name", "").startsWith("Windows")) {
        try {
          System.load("C:\\Windows\\System32\\WindowsCodecs.dll");
        } catch (UnsatisfiedLinkError e) {
          System.err.println("Native code library failed to load.\n" + e);
        }
      }

      // start swing dispatch thread
      SwingUtilities.invokeAndWait(new Runnable() {
        @Override
        public void run() {
          // nothing to do
        }
      });
      // start JavaFx platform
      try {
        Class<?> clazz = swingLibClassLoader.loadClass("com.sun.javafx.application.PlatformImpl");
        Class<?> startupAttrType[] = {Runnable.class};
        Method startup = clazz.getMethod("startup", startupAttrType);
        startup.invoke(null, new Runnable() {
          @Override
          public void run() {
            try {
              Class<?> toolkitFXServiceImplClass =
                  swingLibClassLoader.loadClass("org.webswing.javafx.toolkit.ToolkitFXServiceImpl");
              Method m = toolkitFXServiceImplClass.getMethod("getInstance");
              Object instance = m.invoke(null);
              ToolkitFXService toolkitFXServiceImpl = (ToolkitFXService) instance;
              Services.initializeToolkitFXService(toolkitFXServiceImpl);
            } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException | SecurityException e) {
              AppLogger.error("Failed to initialize ToolkitFXServiceImpl", e);
            }
          }
        });
      } catch (IllegalAccessException | ClassNotFoundException | NoSuchMethodException e) {
        AppLogger.error("Failed to initialize Javafx Platform", e);
      }
    }
  }

  private static void setupContextClassLoader(ClassLoader swingClassLoader) {
    Util.getWebToolkit().setSwingClassLoader(swingLibClassLoader);
    Thread.currentThread().setContextClassLoader(swingClassLoader);
    try {
      EventQueue q = Toolkit.getDefaultToolkit().getSystemEventQueue();
      Class<?> systemQueue = q.getClass();
      Field cl = systemQueue.getDeclaredField("classLoader");
      cl.setAccessible(true);
      cl.set(q, Thread.currentThread().getContextClassLoader());
    } catch (Exception e) {
      AppLogger
          .error("Error in SwingMain: EventQueue thread - setting context class loader failed.", e);
    }
  }

  private static Map<String, String> resolveProps() {
    HashMap<String, String> result = new HashMap<String, String>();
    for (Object keyObj : System.getProperties().keySet()) {
      String key = (String) keyObj;
      if (key.startsWith(Constants.SWING_START_STS_PROP_APPLET_PARAM_PREFIX)) {
        String paramKey =
            key.substring(Constants.SWING_START_STS_PROP_APPLET_PARAM_PREFIX.length());
        result.put(paramKey, System.getProperty(key));
      }
    }
    return result;
  }

  private static void initTempFolder() {
    // try to create java.io.tmpdir if does not exist
    try {
      File f = new File(System.getProperty("java.io.tmpdir", ".")).getAbsoluteFile();
      if (!f.exists()) {
        f.mkdirs();
      }
    } catch (Exception e) {
      // ignore if not possible to create
    }
  }
}
