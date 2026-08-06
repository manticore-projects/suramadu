package org.webswing.server.common.model;

import org.webswing.server.common.model.meta.*;
import org.webswing.server.common.model.meta.ConfigFieldEditorType.EditorType;

import java.lang.reflect.Method;
import java.util.*;

@ConfigType(metadataGenerator = SwingConfig.SwingConfigurationMetadataGenerator.class)
// NOTE: if you change these names, please see also MigrationConfigurationProvider
@ConfigFieldOrder({"homeDir", "theme", "fontConfig", "directdraw", "javaFx",
    "javaFxClassPathEntries", "compositingWinManager", "debug", "userDir", "jreExecutable",
    "javaVersion", "classPathEntries", "vmArgs", "launcherType", "launcherConfig",
    "swingSessionTimeout", "timeoutIfInactive", "sessionLogging", "sessionLogFileSize",
    "sessionLogMaxFileSize", "isolatedFs", "allowUpload", "allowDelete", "allowDownload",
    "allowAutoDownload", "transparentFileOpen", "transparentFileSave", "transferDir",
    "clearTransferDir", "allowJsLink", "jsLinkWhitelist", "allowLocalClipboard",
    "allowServerPrinting", "dockMode", "allowStatisticsLogging"})
public interface SwingConfig extends Config {

  public enum LauncherType {
    Desktop;
  }

  public enum DockMode {
    ALL, MARKED, NONE
  }

  // FIXME better description for this and homeDir in SecuredPathConfig
  @ConfigField(label = "Home Folder",
      description = "Application's home directory. This will be the base directory of any relative classpath entries specified.")
  @ConfigFieldVariables(VariableSetName.SwingInstance)
  @ConfigFieldDiscriminator // FIXME why discriminator ?
  @ConfigFieldDefaultValueString("${user.dir}")
  public String getHomeDir();

  @ConfigField(tab = ConfigGroup.General, label = "Fonts",
      description = "Customize logical font mappings and define physical fonts available to application. These fonts (TTF only) will be used for DirectDraw as native fonts. Key: name of font (ie. dialog|dialoginput|sansserif|serif|monospaced), Value: path to font file.")
  @ConfigFieldVariables(VariableSetName.SwingInstance)
  @ConfigFieldPresets({"dialog", "dialoginput", "sansserif", "serif", "monospaced"})
  public Map<String, String> getFontConfig();

  @ConfigField(tab = ConfigGroup.General, label = "DirectDraw Rendering",
      description = "DirectDraw rendering mode uses canvas instructions to render the application instead of server-rendered png images. DirectDraw improves performance but is not recomended for applications with lot of graphics content.")
  @ConfigFieldDefaultValueBoolean(true)
  public boolean isDirectdraw();

  @ConfigField(tab = ConfigGroup.General, label = "JavaFx Support",
      description = "Enables native or embeded JavaFx framework support.")
  @ConfigFieldDefaultValueBoolean(false)
  @ConfigFieldDiscriminator
  public boolean isJavaFx();

  @ConfigField(tab = ConfigGroup.General, label = "JavaFX Classpath",
      description = "(Only valid for Java 11+) JavaFX jar libraries to be included in classpath. Supports ? and * wildcards.")
  @ConfigFieldVariables(VariableSetName.SwingInstance)
  public List<String> getJavaFxClassPathEntries();

  @ConfigField(tab = ConfigGroup.General, label = "Compositing Window Manager",
      description = "Use window manager that provides an off-screen buffer for each window. Allows advanced window positioning when embedding and better communication integration. Recommended with DirectDraw rendering mode.")
  @ConfigFieldDefaultValueBoolean(false)
  @ConfigFieldDiscriminator
  public boolean isCompositingWinManager();

  @ConfigField(tab = ConfigGroup.General, label = "Enable Debug Mode",
      description = "Enables remote debug for this application. To start the application in debug mode use '?debugPort=8000' url param.")
  @ConfigFieldDefaultValueBoolean(false)
  public boolean isDebug();

  @ConfigField(tab = ConfigGroup.General, label = "Enable Test Mode",
      description = "Enables test mode for this application to be able to test it in Webswing Test Tool.")
  @ConfigFieldDefaultValueBoolean(false)
  public boolean isTestMode();

  @ConfigField(tab = ConfigGroup.Java, label = "Working Directory",
      description = "The User working directory. Path from which the application process will be started. (See the Java System Property: 'user.dir')")
  @ConfigFieldVariables(VariableSetName.SwingInstance)
  @ConfigFieldDefaultValueString("")
  public String getUserDir();

  @ConfigField(tab = ConfigGroup.Java, label = "JRE Executable",
      description = "Path to java executable that will be used to spawn application process. Java 8 and 11 are supported.")
  @ConfigFieldVariables(VariableSetName.SwingInstance)
  @ConfigFieldDefaultValueString("${java.home}/bin/java")
  public String getJreExecutable();

  @ConfigField(tab = ConfigGroup.Java, label = "Java Version",
      description = "Java version of the JRE executable defined above. Expected values are starting with '1.8' or '11'.")
  @ConfigFieldVariables(VariableSetName.SwingInstance)
  @ConfigFieldDefaultValueString("${java.version}")
  public String getJavaVersion();

  @ConfigField(tab = ConfigGroup.Java, label = "Class Path",
      description = "Application's classpath. Absolute or relative path to jar file or classes directory. At least one classPath entry should be specified containing the main class. Supports ? and * wildcards.")
  @ConfigFieldVariables(VariableSetName.SwingInstance)
  public List<String> getClassPathEntries();

  @ConfigField(tab = ConfigGroup.Java, label = "JVM Arguments",
      description = "Commandline arguments processed by Oracle's Java Virtual Machine. (ie. '-Xmx128m')")
  @ConfigFieldVariables(VariableSetName.SwingInstance)
  public String getVmArgs();

  @ConfigField(tab = ConfigGroup.Java, label = "Launcher Type",
      description = "Select the application type. Only Desktop Application is supported.")
  @ConfigFieldDefaultValueString("Desktop")
  @ConfigFieldDiscriminator
  public LauncherType getLauncherType();

  @ConfigField(tab = ConfigGroup.Java, label = "Launcher Configuration",
      description = "Launcher type specific configuration options")
  @ConfigFieldDefaultValueObject(HashMap.class)
  @ConfigFieldEditorType(editor = EditorType.Object)
  public Map<String, Object> getLauncherConfig();

  @ConfigField(tab = ConfigGroup.Session, label = "Session Timeout",
      description = "Specifies how long (seconds) will be the application left running after the user closes the browser. User can reconnect in this interval and continue in last session.")
  @ConfigFieldDefaultValueNumber(300)
  public int getSwingSessionTimeout();

  @ConfigField(tab = ConfigGroup.Session, label = "Timeout if Inactive",
      description = "If True, the Session Timeout will apply for user inactivity (Session Timeout has to be > 0). Otherwise only disconnected sessions will time out.")
  @ConfigFieldDefaultValueBoolean(false)
  public boolean isTimeoutIfInactive();

  @ConfigField(tab = ConfigGroup.Logging, label = "Session Logging",
      description = "If enabled, sessions are logged into a separate log file.")
  @ConfigFieldDefaultValueBoolean(false)
  @ConfigFieldDiscriminator
  public boolean isSessionLogging();

  @ConfigField(tab = ConfigGroup.Logging, label = "Maximum Session Logs Size",
      description = "Maximum size of all session log files. After file size is exceeded, old log files are deleted.")
  @ConfigFieldVariables(VariableSetName.SwingInstance)
  @ConfigFieldDefaultValueString("${webswing.sessionLog.maxSize:-1000MB}")
  public String getSessionLogMaxFileSize();

  @ConfigField(tab = ConfigGroup.Logging, label = "Session Log Size",
      description = "Maximum size of a single session log file.")
  @ConfigFieldVariables(VariableSetName.SwingInstance)
  @ConfigFieldDefaultValueString("${webswing.sessionLog.size:-10MB}")
  public String getSessionLogFileSize();

  @ConfigField(tab = ConfigGroup.Logging, label = "Statistics Logging",
      description = "If enabled, statistics will be logged for sessions.")
  @ConfigFieldDefaultValueBoolean(true)
  public boolean isAllowStatisticsLogging();

  @ConfigField(tab = ConfigGroup.Features, label = "Isolated Filesystem",
      description = "If true, every file chooser dialog will be restricted to access only the home directory of current application.")
  @ConfigFieldDefaultValueBoolean(false)
  @ConfigFieldDiscriminator
  public boolean isIsolatedFs();

  @ConfigField(tab = ConfigGroup.Features, label = "Uploading Files",
      description = "If selected, the JFileChooser integration will allow users to upload files to folder opened in the file chooser dialog")
  @ConfigFieldDefaultValueBoolean(true)
  @ConfigFieldDiscriminator
  public boolean isAllowUpload();

  @ConfigField(tab = ConfigGroup.Features, label = "Deleting Files",
      description = "If selected, the JFileChooser integration will allow users to delete files displayed in the file chooser dialog")
  @ConfigFieldDefaultValueBoolean(true)
  public boolean isAllowDelete();

  @ConfigField(tab = ConfigGroup.Features, label = "Downloading Files",
      description = "If selected, the JFileChooser integration will allow users to download files displayed in the file chooser dialog")
  @ConfigFieldDefaultValueBoolean(true)
  @ConfigFieldDiscriminator
  public boolean isAllowDownload();

  @ConfigField(tab = ConfigGroup.Features, label = "Auto-Download from Save Dialog",
      description = "If selected, the JFileChooser dialog's save mode will trigger file download as soon as the selected file is available on filesystem.")
  @ConfigFieldDefaultValueBoolean(true)
  public boolean isAllowAutoDownload();

  @ConfigField(tab = ConfigGroup.Features, label = "Transparent Open File Dialog",
      description = "If selected, the JFileChooser dialog's open mode will open a client side file browser and transparently upload selected files and triggers selection.")
  @ConfigFieldDefaultValueBoolean(true)
  public boolean isTransparentFileOpen();

  @ConfigField(tab = ConfigGroup.Features, label = "Transparent Save File Dialog",
      description = "If selected, the JFileChooser dialog's save mode will open a client side dialog to enter the file name to be saved.")
  @ConfigFieldDefaultValueBoolean(true)
  public boolean isTransparentFileSave();

  @ConfigField(tab = ConfigGroup.Features, label = "Upload Folder",
      description = "If Isolated Filesystem is enabled. This will be the folder on the server where the user can upload and download files from. Multiple folders can be defined using path separator (${path.separator})")
  @ConfigFieldVariables(VariableSetName.SwingInstance)
  @ConfigFieldDefaultValueString("${user}/upload")
  public String getTransferDir();

  @ConfigField(tab = ConfigGroup.Features, label = "Clear Upload Folder",
      description = "If enabled, all files in the transfer folder will be deleted when the application process is terminated.")
  @ConfigFieldDefaultValueBoolean(true)
  public boolean isClearTransferDir();

  @ConfigField(tab = ConfigGroup.Features, label = "Allow JsLink",
      description = "If enabled, the JSLink feature will be enabled, allowing application to invoke javascript and vice versa. (See netscape.javascript.JSObject)")
  @ConfigFieldDefaultValueBoolean(true)
  @ConfigFieldDiscriminator
  public boolean isAllowJsLink();

  @ConfigField(tab = ConfigGroup.Features, label = "JsLink White List",
      description = "List of allowed Java classes. Calls to declared methods of these classes are allowed via JsLink. Supports trailing * wildcard. Use single * entry to allow any class. Leave empty to disallow everything. E.g. org.webswing.*")
  @ConfigFieldDefaultValueGenerator("defaultJsLinkWhitelistValue")
  public List<String> getJsLinkWhitelist();

  @ConfigField(tab = ConfigGroup.Features, label = "Allow Local Clipboard",
      description = "Enables built-in integration of client's local clipboard. Due to browser security limitations clipboard toolbar is displayed.")
  @ConfigFieldDefaultValueBoolean(true)
  boolean isAllowLocalClipboard();

  @ConfigField(tab = ConfigGroup.Features, label = "Allow Server Printing",
      description = "Enables native printing on devices configured on server's OS. If disabled a pdf is generated and sent to client browser.")
  @ConfigFieldDefaultValueBoolean(false)
  boolean isAllowServerPrinting();

  @ConfigField(tab = ConfigGroup.Features, label = "Docking Mode",
      description = "Select the mode for undocking windows to a separate browser window: 1.ALL: all windows can be undocked. 2.MARKED: only windows marked with Dockable interface can be undocked. 3.NONE: disable undocking")
  @ConfigFieldDefaultValueString("ALL")
  public DockMode getDockMode();

  @Deprecated
  /*
  *  Use SecuredPathConfig.getAllowedCorsOrigins instead.
  */
  public List<String> getAllowedCorsOrigins();

  public static List<String> defaultJsLinkWhitelistValue(SwingConfig config) {
    return Arrays.asList("*");
  }

  public static class SwingConfigurationMetadataGenerator extends MetadataGenerator<SwingConfig> {
    @Override
    public Class<?> getExplicitType(SwingConfig config, ClassLoader cl, String propertyName,
        Method readMethod, Object value) throws ClassNotFoundException {
      if ("launcherConfig".equals(propertyName)) {
        if (config.getLauncherType() != null) {
          if (Objects.requireNonNull(config.getLauncherType()) == LauncherType.Desktop) {
            return DesktopLauncherConfig.class;
          }
          return null;
        } else {
          return null;
        }
      } else {
        return super.getExplicitType(config, cl, propertyName, readMethod, value);
      }
    }

    @Override
    protected LinkedHashSet<String> getPropertyNames(SwingConfig config, ClassLoader cl)
        throws Exception {
      LinkedHashSet<String> names = super.getPropertyNames(config, cl);
      if (!config.isAllowUpload()) {
        names.remove("uploadMaxSize");
        names.remove("allowAutoUpload");
      }
      if (!config.isAllowDownload()) {
        names.remove("allowAutoDownload");
      }
      if (!config.isIsolatedFs()) {
        names.remove("transferDir");
        names.remove("transparentFileSave");
        names.remove("transparentFileOpen");
        names.remove("clearTransferDir");
      }
      if (!config.isSessionLogging()) {
        names.remove("sessionLogMaxFileSize");
        names.remove("sessionLogFileSize");
      }
      if (!config.isCompositingWinManager()) {
        names.remove("dockMode");
      }
      if (!config.isAllowJsLink()) {
        names.remove("jsLinkWhitelist");
      }

      if (!config.isJavaFx()) {
        names.remove("javaFxClassPathEntries");
      }

      return names;
    }

  }
}
