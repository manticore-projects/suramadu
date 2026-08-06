package org.webswing.server.common.model;

import org.webswing.server.common.model.meta.*;
import org.webswing.server.common.model.meta.ConfigFieldEditorType.EditorType;

import java.util.*;

@ConfigType(metadataGenerator = SecuredPathConfig.SecuredPathConfigurationMetadataGenerator.class)
// NOTE: if you change these names, please see also MigrationConfigurationProvider
@ConfigFieldOrder({"enabled", "path", "name", "webHomeDir", "webFolder", "restrictedResources",
    "langFolder", "icon", "security", "allowedCorsOrigins", "adminConsoleUrl", "uploadMaxSize",
    "maxClients", "sessionMode", "monitorEdtEnabled", "recordingConsentRequired",
    "mirroringConsentRequired", "loadingAnimationDelay", "allowStealSession", "autoLogout",
    "goodbyeUrl", "dataStore"})
public interface SecuredPathConfig extends Config {

  public enum SessionMode {
    ALWAYS_NEW_SESSION, CONTINUE_FOR_TAB, CONTINUE_FOR_BROWSER, CONTINUE_FOR_USER;
  }

  @ConfigField(label = "Enabled",
      description = "If true, application will be started automatically, when server starts.")
  @ConfigFieldDefaultValueBoolean(true)
  public boolean isEnabled();

  @ConfigField(label = "Context Path",
      description = "Url context path where the application will be deployed.")
  public String getPath();

  @ConfigField(label = "Name", description = "Application name.")
  @ConfigFieldVariables(VariableSetName.SwingApp)
  @ConfigFieldDefaultValueString("My Application")
  public String getName();

  @ConfigField(label = "Web Home Folder",
      description = "Application's home directory for web-related content. This will be the base directory of any relative classpath entries specified.")
  @ConfigFieldVariables(VariableSetName.SwingApp)
  @ConfigFieldDiscriminator // FIXME why discriminator ?
  @ConfigFieldDefaultValueString("${user.dir}")
  public String getWebHomeDir();

  @ConfigField(label = "Web Folder",
      description = "Folder to be used to store customized static web files like HTML, CSS or Javascript.")
  @ConfigFieldVariables(VariableSetName.SwingApp)
  @ConfigFieldDefaultValueString("")
  public String getWebFolder();

  @ConfigField(label = "Restricted Resources",
      description = "Defined Path-prefix restricts access to resources only to authenticated users. Applies to static resources inside 'Web Folder' or packaged with Webswing.")
  @ConfigFieldVariables(VariableSetName.SwingApp)
  @ConfigFieldDefaultValueObject(ArrayList.class)
  public List<String> getRestrictedResources();

  @ConfigField(label = "Localization Folder",
      description = "Folder to be used to store customized messages and translations in supported languages. English is available by default.")
  @ConfigFieldVariables(VariableSetName.SwingApp)
  @ConfigFieldDefaultValueString("")
  public String getLangFolder();

  @ConfigField(label = "Icon",
      description = "Path to icon displayed in application selection dialog. Recommended size 256x256.")
  @ConfigFieldVariables(VariableSetName.SwingApp)
  public String getIcon();

  @ConfigField(label = "Security")
  @ConfigFieldEditorType(editor = EditorType.Object,
      className = "org.webswing.server.services.security.api.WebswingSecurityConfig")
  @ConfigFieldDefaultValueObject(HashMap.class)
  public Map<String, Object> getSecurity();

  @ConfigField(label = "CORS Origins",
      description = "If you are embedding webswing to page on different domain, you have to enable Cross-origin resource sharing (CORS) by adding the domain in this list. Use * to allow all domains.")
  public List<String> getAllowedCorsOrigins();

  @ConfigField(label = "Admin Console Url",
      description = "Admin console used to manage this server.")
  public String getAdminConsoleUrl();

  @ConfigField(label = "Upload Size Limit",
      description = "Maximum size of upload for single file (in MB). Set 0 for unlimited size.")
  @ConfigFieldDefaultValueNumber(5)
  public double getUploadMaxSize();

  @ConfigField(label = "Session Mode",
      description = "Select session behavior when user reconnects to application. 1.ALWAYS_NEW_SESSION: New application is started for every Webswing session. (Session timeout will be set to 0) 2.CONTINUE_FOR_TAB: Webswing session can be resumed within the same browser tab after connection is terminated or user refresh the page. (Session timeout applies)  3.CONTINUE_FOR_BROWSER: Webswing session can be resumed in the same browser after connection is terminated (Session timeout applies). 4.CONTINUE_FOR_USER: Application session can be resumed by the same user from any computer after the connection is terminated(Session timeout applies).")
  @ConfigFieldDefaultValueString("CONTINUE_FOR_BROWSER")
  public SessionMode getSessionMode();

  @ConfigField(label = "Max. Connections",
      description = "Maximum number of allowed simultaneous connections for this application.")
  @ConfigFieldDefaultValueNumber(1)
  public int getMaxClients();

  @ConfigField(label = "Monitor App Responsiveness",
      description = "If True, Webswing will display a progress animation if Swing's Event Dispatch thread is not responding.")
  @ConfigFieldDefaultValueBoolean(true)
  @ConfigFieldDiscriminator
  public boolean isMonitorEdtEnabled();

  @ConfigField(label = "Require Recording Consent",
      description = "If enabled, triggering Session recording from admin console will show a dialog for users to provide their consent before Session recording is started")
  @ConfigFieldDefaultValueBoolean(false)
  @ConfigFieldDiscriminator
  public boolean isRecordingConsentRequired();

  @ConfigField(label = "Require Mirroring Consent",
      description = "If enabled, triggering Mirror View from admin console will show a dialog for users to provide their consent before Mirror View is started")
  @ConfigFieldDefaultValueBoolean(false)
  @ConfigFieldDiscriminator
  public boolean isMirroringConsentRequired();

  @ConfigField(label = "Loading Animation delay",
      description = "If EDT thread is blocked for more then defined delay in seconds, dialog with loading animation is displayed appears. Delay must be  >= 2 seconds.")
  @ConfigFieldDefaultValueNumber(2)
  int getLoadingAnimationDelay();

  @ConfigField(label = "Session Stealing",
      description = "If enabled, and session mode 'CONTINUE_FOR_USER' is selected, user can resume Webswing session even if the connection is open in other browser. Former browser window will be disconnected.")
  @ConfigFieldDefaultValueBoolean(true)
  public boolean isAllowStealSession();

  @ConfigField(label = "Auto Logout",
      description = "If enabled, user is automatically logged out after the application finished.")
  @ConfigFieldDefaultValueBoolean(true)
  @ConfigFieldDiscriminator
  public boolean isAutoLogout();

  @ConfigField(label = "Goodbye URL",
      description = "Absolute or relative URL to redirect to, when application exits. Use '/' to navigate back to Application selector.")
  @ConfigFieldVariables(VariableSetName.SwingApp)
  @ConfigFieldDefaultValueString("")
  String getGoodbyeUrl();

  @ConfigField(label = "Data Store")
  @ConfigFieldEditorType(editor = EditorType.Object,
      className = "org.webswing.server.common.datastore.WebswingDataStoreConfig")
  @ConfigFieldDefaultValueObject(HashMap.class)
  public Map<String, Object> getDataStore();

  public static class SecuredPathConfigurationMetadataGenerator
      extends MetadataGenerator<SecuredPathConfig> {

    @Override
    protected LinkedHashSet<String> getPropertyNames(SecuredPathConfig config, ClassLoader cl)
        throws Exception {
      LinkedHashSet<String> names = super.getPropertyNames(config, cl);

      if (config.isAutoLogout()) {
        names.remove("goodbyeUrl");
      }
      if (!config.isMonitorEdtEnabled()) {
        names.remove("loadingAnimationDelay");
      }

      return names;
    }

  }

}
