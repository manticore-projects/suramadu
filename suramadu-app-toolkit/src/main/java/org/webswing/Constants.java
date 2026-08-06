package org.webswing;

import java.util.concurrent.TimeUnit;

public interface Constants {

  // Web related constants
  String CLIENT_ID_COOKIE = "webswingID";

  // swing startup properties
  String SWING_START_SYS_PROP_INSTANCE_ID = "webswing.clientId";
  String SWING_START_SYS_PROP_SESSION_POOL_ID = "webswing.sessionpool.id";
  String SWING_START_SYS_PROP_USER_ID = "webswing.userId";
  String SWING_START_SYS_PROP_APP_ID = "webswing.appId";
  String SWING_START_SYS_PROP_JMS_ID = "webswing.jmsQueueId";
  String SWING_START_SYS_PROP_MAIN_CLASS = "webswing.mainClass";
  String SWING_START_SYS_PROP_APP_HOME = "webswing.applicationHome";
  String SWING_START_SYS_PROP_CLASS_PATH = "webswing.classPath";
  String SWING_START_SYS_PROP_ISOLATED_FS = "webswing.isolatedFs";
  String SWING_START_SYS_PROP_USE_SHARED_USER_HOME = "webswing.useSharedUserHome";
  String SWING_START_SYS_PROP_TRANSFER_DIR = "webswing.transfer.dir";
  String SWING_START_SYS_PROP_DIRECTDRAW = "webswing.directdraw";
  String SWING_START_SYS_PROP_DIRECTDRAW_SUPPORTED = "webswing.directdraw.supported";
  String SWING_START_SYS_PROP_COMPOSITING_WM = "webswing.compositingWindowManager";
  String SWING_START_SYS_PROP_ALLOW_DOWNLOAD = "webswing.allowDownload";
  String SWING_START_SYS_PROP_ALLOW_AUTO_DOWNLOAD = "webswing.allowAutoDownload";
  String SWING_START_SYS_PROP_ALLOW_UPLOAD = "webswing.allowUpload";
  String SWING_START_SYS_PROP_TRANSPARENT_FILE_OPEN = "webswing.transparentFileOpen";
  String SWING_START_SYS_PROP_TRANSPARENT_FILE_SAVE = "webswing.transparentFileSave";
  String SWING_START_SYS_PROP_ALLOW_DELETE = "webswing.allowDelete";
  String SWING_START_SYS_PROP_ALLOW_JSLINK = "webswing.allowJsLink";
  String SWING_START_SYS_PROP_JSLINK_WHITELIST = "webswing.jsLinkWhitelist";
  String SWING_START_SYS_PROP_ALLOW_LOCAL_CLIPBOARD = "webswing.allowLocalClipboard";
  String SWING_SESSION_TIMEOUT_SEC = "webswing.sessionTimeoutSec";
  int SWING_SESSION_TIMEOUT_SEC_DEFAULT = 300;
  String SWING_SESSION_TIMEOUT_SEC_IF_FILECHOOSER_ACTIVE =
      "webswing.sessionTimeoutSecIfFileChooserActive";
  int SWING_SESSION_TIMEOUT_SEC_IF_FILECHOOSER_ACTIVE_DEFAULT = 1800;
  String EDT_TIMEOUT_SECONDS = "webswing.edtTimeout";
  int EDT_TIMEOUT_SECONDS_DEFAULT = 10;
  String SWING_SESSION_TIMEOUT_IF_INACTIVE = "webswing.sessionTimeoutIfInactive";
  String SWING_START_SYS_PROP_SYNC_TIMEOUT = "webswing.syncCallTimeout";
  long SWING_START_SYS_PROP_SYNC_TIMEOUT_DEFAULT_VALUE = 3000;
  String SWING_START_SYS_PROP_THEME = "webswing.theme";
  String SWING_START_SYS_PROP_WAIT_FOR_EXIT = "webswing.waitForExit";
  int SWING_START_SYS_PROP_WAIT_FOR_EXIT_DEFAULT = 30000;
  String LAST_HEARTBEAT_BEFORE_SHUTDOWN = "webswing.heartbeatTimeout";
  long LAST_HEARTBEAT_BEFORE_SHUTDOWN_DEFAULT = TimeUnit.SECONDS.toMillis(10);
  String SWING_START_SYS_PROP_DOUBLE_CLICK_DELAY = "webswing.doubleClickMaxDelay";
  String SWING_START_SYS_PROP_INITIAL_URL = "webswing.initialUrl";
  String SWING_START_SYS_PROP_CLASS_MODIFICATION_BLACKLIST = "webswing.classModificationBlacklist";
  String SWING_START_SYS_PROP_LOG_LEVEL = "webswing.logLevel";
  String SWING_START_SYS_PROP_TEST_MODE = "webswing.testMode";
  String SWING_START_SYS_PROP_EVENT_DISPATCHER_CLASS = "webswing.eventDispatcherClass";
  String SWING_START_SYS_PROP_PAINT_DISPATCHER_CLASS = "webswing.paintDispatcherClass";
  String SWING_START_SYS_PROP_SESSION_WATCHDOG_CLASS = "webswing.sessionWatchdogClass";
  String SWING_START_SYS_PROP_DOCK_MODE = "webswing.dockMode";
  String SWING_START_SYS_PROP_TOUCH_MODE = "webswing.touchMode";
  String SWING_START_SYS_PROP_ACCESSIBILITY_ENABLED = "webswing.accessibilityEnabled";
  String SWING_START_SYS_PROP_FONT_CONFIG = "webswing.fontConfig";
  String SWING_START_SYS_ALLOW_REDIRECT_STD_OUT = "webswing.allowRedirectStdOut";
  String SWING_START_SYS_PROP_WEBSOCKET_URL = "webswing.websocketUrl";
  String SWING_START_SYS_PROP_STATISTICS_LOGGING_ENABLED = "webswing.statisticsLoggingEnabled";
  String SWING_START_SYS_PROP_RECORDING_FLAGGED = "webswing.recording.flagged";
  String SWING_START_SYS_PROP_RECORDING_ASK_NEEDED = "webswing.recording.ask.needed";
  String SWING_START_SYS_PROP_MIRRORING_ASK_NEEDED = "webswing.mirroring.ask.needed";
  String SWING_START_SYS_PROP_RECORDING_MESSAGE = "webswing.recording.message";
  String SWING_START_SYS_PROP_MIRRORING_MESSAGE = "webswing.mirroring.message";
  String SWING_START_SYS_PROP_RECORDING_MESSAGE_DEFAULT =
      "Administrator wants to record your session.\nDo you want to allow the recording?";
  String SWING_START_SYS_PROP_MIRRORING_MESSAGE_DEFAULT =
      "Administrator wants to mirror and take control of your session.\nDo you want to allow the mirroring?";
  String SWING_START_SYS_PROP_RECORDING_DIALOG_TITLE = "webswing.recording.title";
  String SWING_START_SYS_PROP_MIRRORING_DIALOG_TITLE = "webswing.mirroring.title";
  String SWING_START_SYS_PROP_RECORDING_DIALOG_TITLE_DEFAULT = "Session recording";
  String SWING_START_SYS_PROP_MIRRORING_DIALOG_TITLE_DEFAULT = "Session mirroring";
  String SWING_START_SYS_PROP_RECORDING_ALLOW_BUTTON = "webswing.recording.allow";
  String SWING_START_SYS_PROP_MIRRORING_ALLOW_BUTTON = "webswing.mirroring.allow";
  String SWING_START_SYS_PROP_RECORDING_ALLOW_BUTTON_DEFAULT = "Allow";
  String SWING_START_SYS_PROP_MIRRORING_ALLOW_BUTTON_DEFAULT = "Allow";
  String SWING_START_SYS_PROP_RECORDING_DENY_BUTTON = "webswing.recording.deny";
  String SWING_START_SYS_PROP_MIRRORING_DENY_BUTTON = "webswing.mirroring.deny";
  String SWING_START_SYS_PROP_RECORDING_DENY_BUTTON_DEFAULT = "Deny";
  String SWING_START_SYS_PROP_MIRRORING_DENY_BUTTON_DEFAULT = "Deny";
  String SWING_START_SYS_PROP_IS_APPLET = "webswing.launcherType.applet";
  String SWING_START_SYS_PROP_SESSION_LOGGING_ENABLED = "webswing.sessionLoggingEnabled";
  String SWING_START_SYS_PROP_DATA_STORE_CONFIG = "webswing.dataStoreConfig";

  String SESSION_POOL_DRAIN_MODE = "webswing.sessionPool.drainMode";

  String PRINTER_JOB_CLASS = "webswing.printerJobDelegate";


  // logging
  String APP_LOGGER_SYSTEM_MSG_PREFIX = "SYSTEM:";
  String APP_LOGGER_SYSTEM_MSG_EXIT = "EXIT";

  // javafx startup
  String SWING_FX_TOOLKIT_FACTORY = "webswing.fxToolkitFactory";
  String SWING_START_SYS_PROP_JFX_TOOLKIT = "glass.platform";
  String SWING_START_SYS_PROP_JFX_TOOLKIT_WEB = "Web";
  String SWING_START_SYS_PROP_JFX_PRISM = "prism.order";

  // applet startup properties
  String SWING_START_SYS_PROP_APPLET_CLASS = "webswing.appletClass";
  String SWING_START_STS_PROP_APPLET_PARAM_PREFIX = "webswing.appletParam_";
  String SWING_START_SYS_PROP_APPLET_DOCUMENT_BASE = "webswing.appletDocumentBase";

  // server startup constants
  String WAR_FILE_LOCATION = "webswing.warLocation";
  String ROOT_DIR_URI = "webswing.rootDirUri";
  String ROOT_DIR_PATH = "webswing.rootDir";
  String CONFIG_PATH = "webswing.configDir";
  String TEMP_DIR_PATH_BASE = "webswing.tempDirBase";
  String TEMP_DIR_PATH = "webswing.tempDirPath";
  String CREATE_NEW_TEMP = "webswing.createNewTemp";
  String CLEAN_TEMP = "webswing.cleanTempDir";
  String PROPERTIES_FILE_PATH = "webswing.propertiesFile";
  String CONFIG_FILE_PATH = "webswing.configFile";
  String CONFIG_RELOAD_INTERVAL_MS = "webswing.configReloadIntervalMs";
  String DEFAULT_CONFIG_FILE_NAME = "suramadu.config";
  String DEFAULT_CONFIG_FILE_NAME_CLUSTER = "suramadu-server.config";
  String DEFAULT_CONFIG_FILE_NAME_CLUSTER_SESSION_POOL = "suramadu-app.config";
  String DEFAULT_PROPERTIES_FILE_NAME = "suramadu.properties";
  String DEFAULT_PROPERTIES_FILE_NAME_CLUSTER_SESSION_POOL = "suramadu-sessionpool.properties";
  String DEFAULT_PROPERTIES_FILE_NAME_ADMIN = "suramadu-admin.properties";
  String SERVER_HOST = "webswing.server.host";
  String SERVER_PORT = "webswing.server.port";
  String SERVER_CONTEXT_PATH = "webswing.server.contextPath";
  String SERVER_EMBEDED_FLAG = "webswing.server.embeded";
  String JMS_OVERAL_MEM_LIMIT = "webswing.jmsOveralMemoryLimit";
  String JMS_DEST_MEM_LIMIT = "webswing.jmsDestinationMemoryLimit";
  String DEFAULT_WELCOME_PAGE = "webswing.defaultWelcomePage";
  String FILE_SERVLET_WAIT_TIMEOUT = "webswing.fileServletWaitTimeout";
  String REVERSE_PROXY_CONTEXT_PATH = "webswing.proxyContextPath";
  String HTTPS_ONLY = "webswing.httpsOnly";
  String DISABLE_HTTP_SECURITY_HEADERS = "webswing.disableHttpSecurityHeaders";
  String LINK_COOKIE_TO_IP = "webswing.linkCookieToIpAddress";
  String COOKIE_SAMESITE = "webswing.cookieSameSite";
  String SERVER_WEBSOCKET_URL = "webswing.server.websocketUrl";
  String WEBSWING_SERVER_ID = "webswing.server.id";


  // websocket constants
  int WEBSOCKET_MESSAGE_SIZE_DEFAULT_VALUE = 5 * 1024 * 1024;
  String WEBSOCKET_MESSAGE_SIZE = "webswing.websocketMessageSizeLimit";
  long WEBSOCKET_MESSAGE_TIMEOUT_DEFAULT = TimeUnit.SECONDS.toMillis(30);
  String WEBSOCKET_MESSAGE_TIMEOUT = "webswing.websocketMessageTimeout";
  long WEBSOCKET_PING_PONG_INTERVAL = TimeUnit.SECONDS.toMillis(30);
  String WEBSOCKET_PING_PONG_CONTENT = "ping-pong";
  String WEBSOCKET_URL_LOADER_INTERVAL = "webswing.websocketUrlLoader.interval";
  String WEBSOCKET_URL_LOADER_TYPE = "webswing.websocketUrlLoader.type";
  String WEBSOCKET_URL_LOADER_SCRIPT = "webswing.websocketUrlLoader.script";
  long WEBSOCKET_URL_LOADER_INTERVAL_DEFAULT = 5; // seconds
  String WEBSOCKET_ADMIN_CONSOLE_SUFFIX = "/async/adminconsole";
  String WEBSOCKET_SESSION_POOL_SUFFIX = "/async/sessionpool";
  String JETTY_REQUEST_HEADER_SIZE = "jetty.request.header.size";
  int JETTY_REQUEST_HEADER_SIZE_DEFAULT = 512 * 1024;

  // websocket client config
  String WEBSOCKET_CLIENT_TRUSTSTORE = "webswing.server.websocket.truststore";
  String WEBSOCKET_CLIENT_TRUSTSTORE_TYPE = "webswing.server.websocket.truststore.type";
  String WEBSOCKET_CLIENT_TRUSTSTORE_PWD = "webswing.server.websocket.truststore.password";
  String WEBSOCKET_CLIENT_HOSTNAME_VERIFIER_DISABLED =
      "webswing.server.websocket.hostnameVerifier.disabled";
  String WEBSOCKET_CLIENT_PROXY_URI = "webswing.server.websocket.proxyUri";

  // logging properties
  String LOGS_DIR_PATH = "webswing.logsDir";
  String LOGS_SESSION_MAX_SIZE = "webswing.sessionLog.maxSize";
  String LOGS_SESSION_SIZE = "webswing.sessionLog.size";
  String SERVER_LOGS_DIR_PATH = "webswing.server.logsDir";

  // swing start related properties
  String SWING_SCREEN_WIDTH = "webswing.screenWidth";
  String SWING_SCREEN_HEIGHT = "webswing.screenHeight";
  int SWING_SCREEN_WIDTH_MIN = 300;
  int SWING_SCREEN_HEIGHT_MIN = 300;
  String SWING_SCREEN_VALIDATION_DISABLED = "webswing.disableWindowPositionValidation";
  String PAINT_ACK_TIMEOUT = "webswing.paintAckTimeout";



  // webswing configuration variables
  String USER_NAME_SUBSTITUTE = "user";
  String INSTANCE_ID_SUBSTITUTE = "clientId";
  String SESSION_IP_SUBSTITUTE = "clientIp";
  String SESSION_LOCALE_SUBSTITUTE = "clientLocale";
  String SESSION_TIMEZONE_SUBSTITUTE = "clientTimeZone";
  String SESSION_CUSTOMARGS_SUBSTITUTE = "customArgs";
  String APP_HOME_FOLDER_SUBSTITUTE = "webswing.homeFolder";
  String APP_CONTEXT_PATH_SUBSTITUTE = "webswing.appPath";


  // http request header names
  String HTTP_ATTR_RECORDING_FLAG = "X-suramadu-recording";
  String HTTP_ATTR_ARGS = "X-suramadu-args";
  String HTTP_ATTR_DEBUG_PORT = "X-suramadu-debugPort";
  String HTTP_ATTR_TOKEN = "X-suramadu-token";
  String HTTP_ATTR_INSTANCE_ID = "X-suramadu-instanceId";
  String HTTP_PARAM_SECURITY_TOKEN_HEADER = "securityToken";

  // integration-branding
  String BRANDING_PREFIX = "webswing.brandingPrefix";
  String CONFIG_PROVIDER = "webswing.configProvider";

  String SESSION_LOG_PATTERN = "%d %-5p [%t] (%F:%L) %m%n";

  // stats properties
  String STATS_INTERVAL = "webswing.stats.interval";
  String STATS_HISTORY = "webswing.stats.historySize";
  String STATS_WARN_MEMUSAGE_TRESHOLD = "webswing.stats.memUsageWarn";
  String STATS_WARN_LATENCY_TRESHOLD = "webswing.stats.latencyWarn";
  String STATS_WARN_PING_TRESHOLD = "webswing.stats.pingWarn";

  // session pool properties
  String SESSION_POOL_ID = "sessionpool.id";
  String SESSION_POOL_PRIORITY = "sessionpool.priority";
  String SESSION_POOL_MAX_INSTANCES = "sessionpool.instances.max";
  String SESSION_POOL_RECONNECT_INTERVAL = "sessionpool.reconnect.interval";
  String SESSION_POOL_RECONNECT_RETRIES = "sessionpool.reconnect.retries";
  String SESSION_POOL_RECONNECT_DELAY = "sessionpool.reconnect.delay";
  long SESSION_POOL_RECONNECT_DELAY_DEFAULT = TimeUnit.SECONDS.toMillis(3);

  // admin console properties
  String ADMIN_CONSOLE_WEBSWING_SERVER_PUBLIC_URL = "webswing.server.publicUrl";
  String ADMIN_CONSOLE_CORS = "admin.server.cors";
  String APP_WEBSOCKET_CLOSE_REASON_RECONNECT = "reconnect";
  String APP_WEBSOCKET_CLOSE_REASON_SHUTDOWN = "shutdown";

  // JWT
  String WEBSWING_SESSION_ID = "WebswingSessionId";
  String WEBSWING_SESSION_REFRESH_TOKEN = "wrt";
  String WEBSWING_SESSION_TRANSFER_TOKEN = "wtt";
  String WEBSWING_SESSION_LOGIN_SESSION_TOKEN = "wlst";
  String WEBSWING_SESSION_ADMIN_CONSOLE_LOGIN_TOKEN = "waclt";
  String WEBSWING_SESSION_ADMIN_CONSOLE_REFRESH_TOKEN = "wacrt";
  String WEBSWING_SESSION_ADMIN_CONSOLE_DOWNLOAD_TOKEN = "wacdt";

  String WEBSWING_CONNECTION_SECRET = "webswing.connection.secret";
  String WEBSWING_CONNECTION_SECRET_DEFAULT =
      "change_this_in_production_000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";

  String JWT_SUBJECT_HANDSHAKE = "handshake";
  String JWT_SUBJECT_ACCESS = "access";
  String JWT_SUBJECT_REFRESH = "refresh";
  String JWT_SUBJECT_LOGIN_SESSION = "loginSession";
  String JWT_SUBJECT_TRANSFER = "transfer";
  String JWT_SUBJECT_ADMIN_CONSOLE_LOGIN = "adminConsoleLogin";
  String JWT_SUBJECT_ADMIN_CONSOLE_ACCESS = "adminConsoleAccess";
  String JWT_SUBJECT_ADMIN_CONSOLE_REFRESH = "adminConsoleRefresh";
  String JWT_SUBJECT_ADMIN_CONSOLE_DOWNLOAD = "adminConsoleDownload";
  String JWT_CLAIM_WEBSWING = "webs";
  String JWT_CLAIM_WEBSWING_LOGIN_SESSION = "wlsc";

  String JWT_SERIALIZATION_USE_GZIP = "webswing.jwt.serialization.gzip";
  String JWT_SERIALIZATION_USE_GZIP_DEFAULT = "true";
  String JWT_SERIALIZATION_USE_PROTO = "webswing.jwt.serialization.proto";
  String JWT_SERIALIZATION_USE_PROTO_DEFAULT = "true";
  String jWT_SERIALIZATION_USE_ENCRYPTION = "webswing.jwt.serialization.encryption";
  String JWT_SERIALIZATION_USE_ENCRYPTION_DEFAULT = "true";

  String JWT_HANDSHAKE_TOKEN_EXPIRATION_MILLIS = "webswing.jwt.token.handshake.expiration";
  long JWT_HANDSHAKE_TOKEN_EXPIRATION_MILLIS_DEFAULT = TimeUnit.MINUTES.toMillis(1);

  String JWT_ACCESS_TOKEN_EXPIRATION_MILLIS = "webswing.jwt.token.access.expiration";
  long JWT_ACCESS_TOKEN_EXPIRATION_MILLIS_DEFAULT = TimeUnit.MINUTES.toMillis(5);

  String JWT_REFRESH_TOKEN_EXPIRATION_MILLIS = "webswing.jwt.token.refresh.expiration";
  long JWT_REFRESH_TOKEN_EXPIRATION_MILLIS_DEFAULT = TimeUnit.MINUTES.toMillis(30);

  String JWT_LOGIN_SESSION_TOKEN_EXPIRATION_MILLIS = "webswing.jwt.token.loginSession.expiration";
  long JWT_LOGIN_SESSION_TOKEN_EXPIRATION_MILLIS_DEFAULT = TimeUnit.MINUTES.toMillis(5);

  String JWT_TRANSFER_TOKEN_EXPIRATION_MILLIS = "webswing.jwt.token.transfer.expiration";
  long JWT_TRANSFER_TOKEN_EXPIRATION_MILLIS_DEFAULT = TimeUnit.MINUTES.toMillis(30);

  String JWT_ADMIN_CONSOLE_LOGIN_TOKEN_EXPIRATION_MILLIS =
      "webswing.jwt.token.adminConsoleLogin.expiration";
  long JWT_ADMIN_CONSOLE_LOGIN_TOKEN_EXPIRATION_MILLIS_DEFAULT = TimeUnit.MINUTES.toMillis(30);

  String JWT_ADMIN_CONSOLE_ACCESS_TOKEN_EXPIRATION_MILLIS =
      "webswing.jwt.token.adminConsoleAccess.expiration";
  long JWT_ADMIN_CONSOLE_ACCESS_TOKEN_EXPIRATION_MILLIS_DEFAULT = TimeUnit.MINUTES.toMillis(5);

  String JWT_ADMIN_CONSOLE_REFRESH_TOKEN_EXPIRATION_MILLIS =
      "webswing.jwt.token.adminConsoleRefresh.expiration";
  long JWT_ADMIN_CONSOLE_REFRESH_TOKEN_EXPIRATION_MILLIS_DEFAULT = TimeUnit.MINUTES.toMillis(30);

  String JWT_ADMIN_CONSOLE_DOWNLOAD_TOKEN_EXPIRATION_MILLIS =
      "webswing.jwt.token.adminConsoleThreadDump.expiration";
  long JWT_ADMIN_CONSOLE_DOWNLOAD_TOKEN_EXPIRATION_MILLIS_DEFAULT = TimeUnit.MINUTES.toMillis(30);

  String JWT_CLOCK_SKEW_SECONDS = "webswing.jwt.clockskew.seconds";
  long JWT_CLOCK_SKEW_SECONDS_DEFAULT = TimeUnit.MINUTES.toSeconds(3);

  String JWT_ADMIN_CONSOLE_ACCESSID_EXPIRATION = "webswing.jwt.adminConsole.accessId.expiration";
  long JWT_ADMIN_CONSOLE_ACCESSID_EXPIRATION_DEFAULT = TimeUnit.MINUTES.toMillis(5);

  // audio
  String AUDIO_CHECKER_INTERVAL_SECONDS = "webswing.audio.checker.interval";
  long AUDIO_CHECKER_INTERVAL_SECONDS_DEFAULT = 3;
  String AUDIO_PLAYBACK_TIMEOUT = "webswing.audio.playback.timeout";
  long AUDIO_PLAYBACK_TIMEOUT_DEFAULT = TimeUnit.SECONDS.toMillis(10);

}
