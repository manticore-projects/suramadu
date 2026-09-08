package org.webswing.sessionpool.api.service.swingprocess.impl;

import com.google.common.base.Joiner;
import com.google.inject.Singleton;
import main.Main;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.Constants;
import org.webswing.server.common.model.DesktopLauncherConfig;
import org.webswing.server.common.model.SwingConfig.DockMode;
import org.webswing.server.common.service.swingprocess.ProcessStartupParams;
import org.webswing.server.common.util.CommonUtil;
import org.webswing.server.common.util.FontUtils;
import org.webswing.server.model.exception.WsInitException;
import org.webswing.sessionpool.api.service.swingprocess.SwingProcess;
import org.webswing.sessionpool.api.service.swingprocess.SwingProcessConfig;
import org.webswing.sessionpool.api.service.swingprocess.SwingProcessService;
import org.webswing.util.ClasspathUtil;
import org.webswing.util.NamedThreadFactory;

import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Function;
import java.util.stream.Collectors;

@Singleton
public class SwingProcessServiceImpl implements SwingProcessService {

  private static final Logger log = LoggerFactory.getLogger(SwingProcessServiceImpl.class);

  private static final String LAUNCHER_CONFIG = "launcherConfig";
  private static final String WEB_API_CLASS_NAME = "org.webswing.toolkit.api.WebswingApi";
  private static final String WEB_TOOLKIT_CLASS_NAME = "org.webswing.toolkit.WebToolkit";
  private static final String WEB_GRAPHICS_ENV_CLASS_NAME =
      "org.webswing.toolkit.ge.WebGraphicsEnvironment";
  private static final String WEB_PRINTER_JOB_CLASS_NAME =
      "org.webswing.toolkit.WebPrinterJobWrapper";
  private static final String SHELL_FOLDER_MANAGER = "sun.awt.shell.PublicShellFolderManager";
  private static final String JAVA9_PATCHED_JSOBJECT_MODULE_MARKER =
      "netscape.javascript.WebswingPatchedJSObjectJarMarker";
  private static final String JAVA_FX_TOOLKIT_CLASS_NAME =
      "org.webswing.javafx.toolkit.WebsinwgFxToolkitFactory";

  private Map<String, SwingProcess> processMap = Collections.synchronizedMap(new HashMap<>());
  private Map<String, List<String>> pathInstanceMap = Collections.synchronizedMap(new HashMap<>());

  /**
   * Number of threads servicing the log pollers and stdin heartbeats of ALL running child
   * processes. This used to be a single thread, which meant that one blocked task stalled the
   * heartbeat of every other instance — the children then self-terminated with
   * {@code ShutdownReason.ProcessKilled}. Override with
   * {@code -Dwebswing.processHandler.threads=N}.
   */
  private static final int PROCESS_HANDLER_THREADS = Integer.getInteger(
      "webswing.processHandler.threads", Math.max(4, Runtime.getRuntime().availableProcessors()));

  private ScheduledExecutorService processHandlerThread;

  @Override
  public void start() throws WsInitException {
    log.info("Starting Webswing Process Handler pool with {} threads.", PROCESS_HANDLER_THREADS);
    processHandlerThread = Executors.newScheduledThreadPool(PROCESS_HANDLER_THREADS,
        NamedThreadFactory.getInstance("Webswing Process Handler"));
  }

  @Override
  public void stop() {
    processHandlerThread.shutdown();
    SwingProcessImpl.shutdownTerminationExecutor();
  }

  @Override
  public void kill(String instanceId, int delayMs) {
    synchronized (processMap) {
      if (!processMap.containsKey(instanceId)) {
        return;
      }

      processMap.get(instanceId).destroy(delayMs);
    }
  }

  @Override
  public void killAll(String path) {
    List<String> instanceIds = null;
    synchronized (pathInstanceMap) {
      if (pathInstanceMap.containsKey(path)) {
        instanceIds = pathInstanceMap.remove(path);
      }
    }
    if (instanceIds != null) {
      instanceIds.forEach(instanceId -> kill(instanceId, 0));
    }
  }

  @Override
  public void killAll() {
    getAll().forEach(process -> kill(process.getInstanceId(), 0));
  }

  @Override
  public SwingProcess getByInstanceId(String instanceId) {
    synchronized (processMap) {
      return processMap.get(instanceId);
    }
  }

  @Override
  public List<SwingProcess> getAll() {
    synchronized (processMap) {
      return new ArrayList<>(processMap.values());
    }
  }

  @Override
  public SwingProcess startProcess(ProcessStartupParams startupParams) throws Exception {
    SwingProcess swing = null;
    try {
      SwingProcessConfig processConfig = new SwingProcessConfig();
      processConfig.setPath(startupParams.getPathMapping());
      processConfig.setName(startupParams.getInstanceId());
      processConfig.setApplicationName(startupParams.getAppName());
      String java = getAbsolutePath(
          startupParams.getSubs().replace(startupParams.getAppConfig().getJreExecutable()), false,
          startupParams.getFileResolver());
      processConfig.setJreExecutable(java);
      String homeDir = getAbsolutePath(
          startupParams.getSubs().replace(startupParams.getAppConfig().getUserDir()), true,
          startupParams.getFileResolver());
      processConfig.setBaseDir(homeDir);
      processConfig.setMainClass(Main.class.getName());
      processConfig
          .setClassPath(new File(URI.create(CommonUtil.getWarFileLocation())).getAbsolutePath());
      String javaVersion =
          startupParams.getSubs().replace(startupParams.getAppConfig().getJavaVersion());
      boolean useJFX = startupParams.getAppConfig().isJavaFx();

      // All JDK 11+ use the "11" suffix classes
      String webToolkitClass = WEB_TOOLKIT_CLASS_NAME + "11";
      String webFxToolkitFactory = JAVA_FX_TOOLKIT_CLASS_NAME + "11";
      String webGraphicsEnvClass = WEB_GRAPHICS_ENV_CLASS_NAME + "11";

      // Verify JDK 11+ (parse major version from strings like "11", "17", "21.0.10", etc.)
      int majorVersion;
      try {
        String major =
            javaVersion.contains(".") ? javaVersion.substring(0, javaVersion.indexOf('.'))
                : javaVersion;
        majorVersion = Integer.parseInt(major);
      } catch (NumberFormatException e) {
        majorVersion = 0;
      }
      if (majorVersion < 11) {
        log.error("Java version {} not supported. JDK 11 or later is required.", javaVersion);
        throw new RuntimeException(
            "Java version not supported. JDK 11 or later is required (detected: " + javaVersion
                + ").");
      }

      // JavaFX classpath setup
      if (useJFX) {
        String javaFxToolkitCP =
            CommonUtil.getBootClassPathForClass(JAVA_FX_TOOLKIT_CLASS_NAME, false) + ";"
                + CommonUtil.getBootClassPathForClass(webFxToolkitFactory, false) + ";";
        String jfxCp = startupParams.getSubs().replace(CommonUtil
            .generateClassPathString(startupParams.getAppConfig().getJavaFxClassPathEntries()));
        URL[] urls = ClasspathUtil.populateClassPath(
            processConfig.getClassPath() + ";" + javaFxToolkitCP + ";" + jfxCp, homeDir);
        processConfig.setClassPath(Arrays.stream(urls).map(url -> {
          try {
            return new File(url.toURI()).getAbsolutePath();
          } catch (URISyntaxException e) {
            return url.getFile();
          }
        }).collect(Collectors.joining(File.pathSeparator)));
      }

      // ── Module system flags (JDK 11+) ──────────────────────────────────
      StringBuilder modules = new StringBuilder();

      // Patch modules: WebSwing toolkit overrides
      modules.append(" --patch-module jdk.jsobject=")
          .append(CommonUtil.getBootClassPathForClass(JAVA9_PATCHED_JSOBJECT_MODULE_MARKER));
      // NOTE: --patch-module java.desktop is built later (after resolveJdkPatchJar()) so that
      // the shell-folder-manager path and the headless patch JAR path can be merged into a
      // single --patch-module argument. Passing --patch-module java.desktop= twice is a JVM
      // error ("specified more than once") on strict JDK distributions (e.g. Liberica).
      modules.append(" --add-reads jdk.jsobject=ALL-UNNAMED");

      // --add-opens: reflective access needed by WebSwing internals
      modules.append(" --add-opens=java.base/java.net=ALL-UNNAMED"); // URLStreamHandler in
      // SwingClassloader
      modules.append(" --add-opens=java.base/java.util=ALL-UNNAMED"); // Collections in
      // SwingClassloader
      modules.append(" --add-opens=java.base/java.lang.reflect=ALL-UNNAMED"); // Field.setAccessible
      // in Main/WebToolkit
      modules.append(" --add-opens=java.base/sun.misc=ALL-UNNAMED"); // Unsafe in WebToolkit.init()
      // GE replacement
      modules.append(" --add-opens=java.desktop/java.awt=ALL-UNNAMED"); // EventQueue in SwingMain
      modules.append(" --add-opens=java.desktop/java.awt.event=ALL-UNNAMED"); // KeyEvent.extendedKeyCode
      // in Util
      modules.append(" --add-opens=java.desktop/javax.swing=ALL-UNNAMED"); // PopupFactory.popupType
      // in CwmPaintDispatcher
      modules.append(" --add-opens=java.desktop/sun.java2d=ALL-UNNAMED"); // SunGraphics2D in
      // DirectDraw
      modules.append(" --add-opens=java.desktop/sun.awt=ALL-UNNAMED"); // SunToolkit internals
      modules.append(" --add-opens=java.desktop/sun.awt.image=ALL-UNNAMED"); // SurfaceManager in
      // WebComponentPeer
      modules.append(" --add-opens=java.desktop/sun.font=ALL-UNNAMED"); // GlyphList/FontInfo in
      // DirectDraw

      // --add-exports: compile-time access to internal APIs
      modules.append(" --add-exports=java.desktop/java.awt=ALL-UNNAMED");
      modules.append(" --add-exports=java.desktop/java.awt.peer=ALL-UNNAMED");
      modules.append(" --add-exports=java.desktop/java.awt.dnd=ALL-UNNAMED");
      modules.append(" --add-exports=java.desktop/sun.awt=ALL-UNNAMED");
      modules.append(" --add-exports=java.desktop/sun.awt.event=ALL-UNNAMED");
      modules.append(" --add-exports=java.desktop/sun.awt.dnd=ALL-UNNAMED");
      modules.append(" --add-exports=java.desktop/sun.awt.datatransfer=ALL-UNNAMED");
      modules.append(" --add-exports=java.desktop/sun.awt.image=ALL-UNNAMED");
      modules.append(" --add-exports=java.desktop/sun.java2d=ALL-UNNAMED");
      modules.append(" --add-exports=java.desktop/sun.java2d.loops=ALL-UNNAMED");
      modules.append(" --add-exports=java.desktop/sun.java2d.pipe=ALL-UNNAMED");
      modules.append(" --add-exports=java.desktop/sun.font=ALL-UNNAMED");
      modules.append(" --add-exports=java.desktop/sun.print=ALL-UNNAMED");
      modules.append(" --add-exports=java.base/sun.security.action=ALL-UNNAMED");
      modules.append(" --add-exports=java.base/sun.security.util=ALL-UNNAMED");
      modules.append(" --add-exports=java.base/sun.nio.cs=ALL-UNNAMED");

      // JNA
      modules.append(" --enable-native-access=ALL-UNNAMED");

      // ── Boot classpath ──────────────────────────────────────────────────
      String webSwingToolkitApiJarPath = CommonUtil.getBootClassPathForClass(WEB_API_CLASS_NAME);
      String webSwingCommonJarPath = CommonUtil.getBootClassPathForClass(Constants.class.getName());
      String webSwingToolkitJarPath = CommonUtil.getBootClassPathForClass(WEB_TOOLKIT_CLASS_NAME);
      String webSwingToolkitJarPathSpecific = CommonUtil.getBootClassPathForClass(webToolkitClass);
      String shellFolderMgrJarPath =
          File.pathSeparator + CommonUtil.getBootClassPathForClass(SHELL_FOLDER_MANAGER);

      String bootCp = "-Xbootclasspath/a:" + webSwingToolkitApiJarPath + File.pathSeparatorChar
          + webSwingCommonJarPath + File.pathSeparatorChar + webSwingToolkitJarPathSpecific
          + File.pathSeparatorChar + webSwingToolkitJarPath + shellFolderMgrJarPath;

      // ── Debug flags ─────────────────────────────────────────────────────
      // Use modern JDWP syntax (no -Xnoagent/-Djava.compiler=NONE, those are JDK 1.3 era)
      String debug = "";
      if (startupParams.getAppConfig().isDebug() && startupParams.getDebugPort() != 0) {
        debug = " -agentlib:jdwp=transport=dt_socket,address=*:" + startupParams.getDebugPort()
            + ",server=y,suspend=y";
      }

      String vmArgs = startupParams.getAppConfig().getVmArgs() == null ? ""
          : startupParams.getSubs().replace(startupParams.getAppConfig().getVmArgs());

      // Java2D pipeline suppression: without these flags the JVM attempts to
      // dlopen libGL (GLX pipeline) and libXrender (XRender pipeline) on Linux
      // even when no DISPLAY is present, causing startup failures on headless
      // servers that have no Xvfb installed. noddraw disables DirectDraw on
      // Windows and is a no-op on Linux/Mac.
      //
      // JDK patch: --patch-module replaces sun.awt.PlatformGraphicsInfo so that
      // createGE() returns WebGraphicsEnvironment11 instead of the JDK's
      // hardcoded X11GraphicsEnvironment. This is what makes truly headless
      // (no Xvfb / no DISPLAY) operation possible on JDK 21+.
      //
      // Patch JAR discovery, in order:
      // 1. -Dwebswing.jdkPatchJar=<path> (explicit override)
      // 2. ${webswing.rootDir}/modpatch-java.desktop.jar
      // 3. ${webswing.rootDir}/lib/modpatch-java.desktop.jar
      // 4. ${webswing.rootDir}/suramadu-jdk-patch.jar
      // First existing file wins. If none exist, the child JVM falls back to
      // requiring Xvfb (a warning is logged).
      String patchJar = resolveJdkPatchJar();
      // Build a single --patch-module java.desktop=<paths> argument combining:
      // 1. The shell-folder-manager JAR (always needed for PublicShellFolderManager)
      // 2. The headless patch JAR (replaces PlatformGraphicsInfo, optional)
      // Both paths are joined with File.pathSeparatorChar so the JVM sees only one
      // --patch-module java.desktop directive — passing it twice is a fatal error on
      // strict JDK distributions such as Liberica.
      String shellFolderJarPath = CommonUtil.getBootClassPathForClass(SHELL_FOLDER_MANAGER);
      String patchModule;
      if (patchJar != null) {
        log.info("Using JDK patch JAR for headless operation: {}", patchJar);
        patchModule = " --patch-module java.desktop=" + shellFolderJarPath + File.pathSeparatorChar
            + patchJar;
      } else {
        log.warn("No JDK patch JAR found — child JVM will require Xvfb or a real X server. "
            + "Set -Dwebswing.jdkPatchJar=/path/to/jar to enable truly headless mode.");
        patchModule = " --patch-module java.desktop=" + shellFolderJarPath;
      }

      processConfig.setJvmArgs(modules + " " + bootCp + debug + patchModule // headless: replaces
                                                                            // PlatformGraphicsInfo
          + " -Djavax.sound.sampled.Clip=org.webswing.audio.AudioMixerProvider"
          + " -Dsun.font.fontmanager=org.webswing.toolkit.ge.WebFontManager" // headless: prevents
                                                                             // X11FontManager from
                                                                             // being
                                                                             // cached by
                                                                             // FontManagerFactory.getInstance()
                                                                             // —
                                                                             // X11FontManager's
                                                                             // native
                                                                             // getFontPathNative()
                                                                             // in libawt_xawt.so
                                                                             // segfaults without
                                                                             // X11.
          + " -Dsun.java2d.opengl=false" // no GLX probe on headless Linux
          + " -Dsun.java2d.xrender=false" // no XRender JNI load on headless Linux
          + " -Dsun.java2d.noddraw=true" // no DirectDraw on Windows (no-op elsewhere)
          /*
          + " -noverify" // JDK 25 verifier rejects valid non-contiguous LocalVariableTypeTable
                         // entries (ClassFormatError on slf4j-api LoggerFactory)
                         // Related: https://github.com/qos-ch/slf4j/issues/483
           */
          + " " + vmArgs);

      // ── System properties ───────────────────────────────────────────────
      processConfig.addProperty(Constants.SWING_START_SYS_PROP_INSTANCE_ID,
          startupParams.getInstanceId());
      processConfig.addProperty(Constants.SWING_START_SYS_PROP_SESSION_POOL_ID,
          System.getProperty(Constants.SESSION_POOL_ID));
      processConfig.addProperty(Constants.SWING_START_SYS_PROP_USER_ID, startupParams.getUserId());
      processConfig.addProperty(Constants.SWING_START_SYS_PROP_APP_ID,
          startupParams.getPathMapping());
      processConfig.addProperty(Constants.SWING_START_SYS_PROP_JMS_ID,
          startupParams.getInstanceId());
      processConfig.addProperty(Constants.SWING_START_SYS_PROP_APP_HOME,
          getAbsolutePath(".", false, startupParams.getFileResolver()));
      processConfig.addProperty(Constants.SWING_START_SYS_PROP_CLASS_PATH,
          startupParams.getSubs().replace(CommonUtil
              .generateClassPathString(startupParams.getAppConfig().getClassPathEntries())));
      processConfig.addProperty(Constants.ROOT_DIR_PATH,
          System.getProperty(Constants.ROOT_DIR_PATH));
      processConfig.addProperty(Constants.TEMP_DIR_PATH,
          System.getProperty(Constants.TEMP_DIR_PATH));
      processConfig.addProperty(Constants.SWING_START_SYS_PROP_WEBSOCKET_URL,
          startupParams.getWebsocketUrl());
      processConfig.addProperty(Constants.WEBSWING_CONNECTION_SECRET,
          serializeAppConnectionSecret(startupParams.getAppConnectionSecret()));
      processConfig.addProperty(Constants.SWING_START_SYS_PROP_DATA_STORE_CONFIG,
          startupParams.getDataStoreConfig());

      processConfig.addProperty(Constants.SWING_START_SYS_PROP_ISOLATED_FS,
          startupParams.getAppConfig().isIsolatedFs());
      processConfig.addProperty(Constants.SWING_START_SYS_PROP_TRANSFER_DIR,
          getAbsolutePaths(
              startupParams.getSubs().replace(startupParams.getAppConfig().getTransferDir()), false,
              startupParams.getFileResolver()));
      processConfig.addProperty(Constants.SWING_START_SYS_PROP_ALLOW_DOWNLOAD,
          startupParams.getAppConfig().isAllowDownload());
      processConfig.addProperty(Constants.SWING_START_SYS_PROP_ALLOW_AUTO_DOWNLOAD,
          startupParams.getAppConfig().isAllowAutoDownload());
      processConfig.addProperty(Constants.SWING_START_SYS_PROP_ALLOW_UPLOAD,
          startupParams.getAppConfig().isAllowUpload());
      processConfig.addProperty(Constants.SWING_START_SYS_PROP_TRANSPARENT_FILE_OPEN,
          startupParams.getAppConfig().isTransparentFileOpen());
      processConfig.addProperty(Constants.SWING_START_SYS_PROP_TRANSPARENT_FILE_SAVE,
          startupParams.getAppConfig().isTransparentFileSave());
      processConfig.addProperty(Constants.SWING_START_SYS_PROP_ALLOW_DELETE,
          startupParams.getAppConfig().isAllowDelete());
      processConfig.addProperty(Constants.SWING_START_SYS_PROP_ALLOW_LOCAL_CLIPBOARD,
          startupParams.getAppConfig().isAllowLocalClipboard());
      processConfig.addProperty(Constants.SWING_START_SYS_PROP_ALLOW_JSLINK,
          startupParams.getAppConfig().isAllowJsLink());
      processConfig.addProperty(Constants.SWING_START_SYS_PROP_JSLINK_WHITELIST,
          Joiner.on(',').join(startupParams.getAppConfig().getJsLinkWhitelist()));
      processConfig.addProperty(Constants.SWING_START_SYS_PROP_INITIAL_URL,
          startupParams.getHandshakeUrl());
      processConfig.addProperty(Constants.SWING_START_SYS_PROP_DOCK_MODE,
          startupParams.isDockingSupported() ? startupParams.getAppConfig().getDockMode().name()
              : DockMode.NONE.name());
      processConfig.addProperty(Constants.SWING_START_SYS_PROP_TOUCH_MODE,
          startupParams.isTouchModeEnabled());
      processConfig.addProperty(Constants.SWING_START_SYS_PROP_ACCESSIBILITY_ENABLED,
          startupParams.isAccessiblityEnabled());
      processConfig.addProperty(Constants.SWING_START_SYS_PROP_STATISTICS_LOGGING_ENABLED,
          startupParams.getAppConfig().isAllowStatisticsLogging());
      processConfig.addProperty(Constants.SWING_START_SYS_PROP_RECORDING_FLAGGED,
          startupParams.isRecordingFlagged());
      processConfig.addProperty(Constants.SWING_START_SYS_PROP_RECORDING_ASK_NEEDED,
          startupParams.isAskForRecordingNeeded());
      processConfig.addProperty(Constants.SWING_START_SYS_PROP_MIRRORING_ASK_NEEDED,
          startupParams.isAskForMirroringNeeded());
      processConfig.addProperty(Constants.SWING_START_SYS_PROP_SESSION_LOGGING_ENABLED,
          startupParams.getAppConfig().isSessionLogging());

      processConfig.addProperty(Constants.SWING_START_SYS_PROP_DIRECTDRAW,
          startupParams.getAppConfig().isDirectdraw());
      processConfig.addProperty(Constants.SWING_START_SYS_PROP_DIRECTDRAW_SUPPORTED,
          startupParams.isDirectDrawSupported());
      processConfig.addProperty(Constants.SWING_START_SYS_PROP_COMPOSITING_WM,
          startupParams.getAppConfig().isCompositingWinManager());
      processConfig.addProperty(Constants.SWING_START_SYS_PROP_TEST_MODE,
          startupParams.getAppConfig().isTestMode());
      processConfig.addProperty(Constants.SWING_SESSION_TIMEOUT_SEC,
          startupParams.getAppConfig().getSwingSessionTimeout());
      processConfig.addProperty(Constants.SWING_SESSION_TIMEOUT_IF_INACTIVE,
          startupParams.getAppConfig().isTimeoutIfInactive());
      processConfig.addProperty("awt.toolkit", webToolkitClass);
      processConfig.addProperty("java.awt.headless", false);
      processConfig.addProperty("java.awt.graphicsenv", webGraphicsEnvClass);
      processConfig.addProperty("java.awt.printerjob", WEB_PRINTER_JOB_CLASS_NAME);
      processConfig.addProperty(Constants.PRINTER_JOB_CLASS,
          startupParams.getAppConfig().isAllowServerPrinting()
              ? PrinterJob.getPrinterJob().getClass().getCanonicalName()
              : "org.webswing.toolkit.WebPrinterJob");
      processConfig.addProperty(Constants.SWING_START_SYS_PROP_FONT_CONFIG,
          FontUtils.createFontConfiguration(startupParams.getAppConfig(), startupParams.getSubs()));
      processConfig.addProperty(Constants.SWING_SCREEN_WIDTH,
          startupParams.getScreenWidth() == null ? Constants.SWING_SCREEN_WIDTH_MIN
              : startupParams.getScreenWidth());
      processConfig.addProperty(Constants.SWING_SCREEN_HEIGHT,
          startupParams.getScreenHeight() == null ? Constants.SWING_SCREEN_HEIGHT_MIN
              : startupParams.getScreenHeight());
      processConfig.addProperty(Constants.WEBSOCKET_MESSAGE_SIZE, System.getProperty(
          Constants.WEBSOCKET_MESSAGE_SIZE, "" + Constants.WEBSOCKET_MESSAGE_SIZE_DEFAULT_VALUE));
      processConfig.addProperty(Constants.WEBSOCKET_MESSAGE_TIMEOUT, System.getProperty(
          Constants.WEBSOCKET_MESSAGE_TIMEOUT, "" + Constants.WEBSOCKET_MESSAGE_TIMEOUT_DEFAULT));

      copyProperties(processConfig, Constants.WEBSOCKET_CLIENT_TRUSTSTORE,
          Constants.WEBSOCKET_CLIENT_TRUSTSTORE_TYPE, Constants.WEBSOCKET_CLIENT_TRUSTSTORE_PWD,
          Constants.WEBSOCKET_CLIENT_HOSTNAME_VERIFIER_DISABLED,
          Constants.WEBSOCKET_CLIENT_PROXY_URI);

      if (useJFX) {
        processConfig.addProperty(Constants.SWING_FX_TOOLKIT_FACTORY, webFxToolkitFactory);
        processConfig.addProperty(Constants.SWING_START_SYS_PROP_JFX_TOOLKIT,
            Constants.SWING_START_SYS_PROP_JFX_TOOLKIT_WEB);
        processConfig.addProperty(Constants.SWING_START_SYS_PROP_JFX_PRISM, "web");
        // T2K font engine was removed in JDK 11; OpenJDK uses FreeType.
        // prism.text=t2k is no longer needed.
        processConfig.addProperty("prism.lcdtext", "false");
        processConfig.addProperty("javafx.live.resize", "false");
      }

      if (startupParams.getAppConfig().isSessionLogging()) {
        String singleSize =
            startupParams.getSubs().replace(startupParams.getAppConfig().getSessionLogFileSize());
        String maxSize = startupParams.getSubs()
            .replace(startupParams.getAppConfig().getSessionLogMaxFileSize());
        processConfig.setSessionLogAppenderParams(new SessionLogAppenderParams(singleSize, maxSize,
            startupParams.getInstanceId(), startupParams.getPathMapping(), getSessionLogDir()));
      }

      switch (startupParams.getAppConfig().getLauncherType()) {
        case Desktop:
          DesktopLauncherConfig desktop =
              startupParams.getAppConfig().getValueAs(LAUNCHER_CONFIG, DesktopLauncherConfig.class);
          processConfig.setArgs(startupParams.getSubs().replace(desktop.getArgs()));
          processConfig.addProperty(Constants.SWING_START_SYS_PROP_MAIN_CLASS,
              startupParams.getSubs().replace(desktop.getMainClass()));
          break;
        default:
          throw new IllegalStateException(
              "Launcher type not recognized: " + startupParams.getAppConfig().getLauncherType());
      }

      swing = new SwingProcessImpl(startupParams.getInstanceId(), processConfig,
          startupParams.getAppConfig(), processHandlerThread);
      swing.execute();

      synchronized (processMap) {
        processMap.put(startupParams.getInstanceId(), swing);
      }
      synchronized (pathInstanceMap) {
        String path = startupParams.getPathMapping();
        if (!pathInstanceMap.containsKey(path)) {
          pathInstanceMap.put(path, new ArrayList<>());
        }
        pathInstanceMap.get(path).add(startupParams.getInstanceId());
      }
    } catch (Exception e) {
      throw new Exception(e);
    }
    return swing;
  }

  private void copyProperties(SwingProcessConfig processConfig, String... propNames) {
    for (String prop : propNames) {
      if (System.getProperty(prop) != null) {
        processConfig.addProperty(prop, System.getProperty(prop));
      }
    }
  }

  @Override
  public void closeProcess(String instanceId) {
    SwingProcess process = getByInstanceId(instanceId);

    if (process == null) {
      return;
    }

    if (process.isRunning()) {
      process.setProcessExitListener(null);
    }

    if (process.getSwingConfig().isIsolatedFs() && process.getSwingConfig().isClearTransferDir()) {
      String transferDir =
          process.getConfig().getProperties().get(Constants.SWING_START_SYS_PROP_TRANSFER_DIR);
      try {
        if (transferDir.indexOf(File.pathSeparator) != -1) {
          throw new IOException(
              "Can not clear upload folder if multiple roots are defined. Turn off the option in Webswing config. ["
                  + transferDir + "]");
        } else if (transferDir != null) {
          FileUtils.deleteDirectory(new File(transferDir));
          log.info("Transfer dir for session [{}] cleared. [{}]", process.getConfig().getName(),
              transferDir);
        }
      } catch (IOException e) {
        log.error("Failed to delete transfer dir {}", transferDir, e);
      }
    }

    synchronized (processMap) {
      processMap.remove(instanceId);
    }
    synchronized (pathInstanceMap) {
      if (pathInstanceMap.containsKey(process.getConfig().getPath())) {
        pathInstanceMap.get(process.getConfig().getPath()).remove(instanceId);
      }
    }
  }

  public static class SessionLogAppenderParams {
    public String singleSize;
    public String maxSize;
    public String instanceId;
    public String pathMapping;
    public String sessionLogDir;

    public SessionLogAppenderParams(String singleSize, String maxSize, String instanceId,
        String pathMapping, String sessionLogDir) {
      super();
      this.singleSize = singleSize;
      this.maxSize = maxSize;
      this.instanceId = instanceId;
      this.pathMapping = pathMapping;
      this.sessionLogDir = sessionLogDir;
    }
  }

  private String serializeAppConnectionSecret(String secret) {
    return new String(Base64.getEncoder().encode(secret.getBytes(StandardCharsets.UTF_8)),
        StandardCharsets.UTF_8);
  }

  /**
   * Locates the JDK patch JAR that supplies the replacement {@code sun.awt.PlatformGraphicsInfo}
   * for truly headless operation.
   *
   * <p>
   * Discovery order:
   * <ol>
   * <li>{@code -Dwebswing.jdkPatchJar=<path>} — explicit override</li>
   * <li>{@code ${webswing.rootDir}/modpatch-java.desktop.jar}</li>
   * <li>{@code ${webswing.rootDir}/lib/modpatch-java.desktop.jar}</li>
   * <li>{@code ${webswing.rootDir}/suramadu-jdk-patch.jar}</li>
   * </ol>
   * Returns the path of the first existing file, or {@code null} if none found.
   */
  private String resolveJdkPatchJar() {
    String explicit = System.getProperty("webswing.jdkPatchJar", "").trim();
    if (!explicit.isEmpty() && new File(explicit).isFile()) {
      return explicit;
    }
    String rootDir = System.getProperty("webswing.rootDir", "");
    if (rootDir.isEmpty()) {
      return null;
    }
    String[] candidates = {rootDir + File.separator + "modpatch-java.desktop.jar",
        rootDir + File.separator + "lib" + File.separator + "modpatch-java.desktop.jar",
        rootDir + File.separator + "suramadu-jdk-patch.jar"};
    for (String candidate : candidates) {
      if (new File(candidate).isFile()) {
        return candidate;
      }
    }
    return null;
  }

  private String getSessionLogDir() {
    String logDir = System.getProperty(Constants.LOGS_DIR_PATH, "logs/");
    if (!logDir.endsWith("/") && !logDir.endsWith("\\")) {
      logDir = logDir + "/";
    }

    return logDir + "session/";
  }

  private String getAbsolutePaths(String paths, boolean b, Function<String, File> fileResolver)
      throws IOException {
    String result = "";
    for (String s : paths.split(File.pathSeparator)) {
      result += getAbsolutePath(s, b, fileResolver) + File.pathSeparator;
    }
    return result.substring(0, Math.max(0, result.length() - 1));
  }

  private String getAbsolutePath(String path, boolean create, Function<String, File> fileResolver)
      throws IOException {
    if (StringUtils.isBlank(path)) {
      path = ".";
    }
    File f = fileResolver.apply(path);
    if (f == null || !f.exists()) {
      path = path.replaceAll("\\\\", "/");
      String[] pathSegs = path.split("/");
      boolean absolute = pathSegs[0].length() == 0 || pathSegs[0].contains(":");
      if (!absolute) {
        File home = fileResolver.apply(".");
        f = new File(home, path);
      } else {
        f = new File(path);
      }
      if (create) {
        boolean done = f.mkdirs();
        if (!done) {
          throw new IOException("Unable to create path. " + f.getAbsolutePath());
        }
      }
    }
    return f.getCanonicalPath();
  }

}
