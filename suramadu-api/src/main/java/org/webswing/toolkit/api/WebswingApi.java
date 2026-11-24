package org.webswing.toolkit.api;

import org.webswing.toolkit.api.action.WebActionListener;
import org.webswing.toolkit.api.action.WebWindow;
import org.webswing.toolkit.api.clipboard.BrowserTransferable;
import org.webswing.toolkit.api.clipboard.PasteRequestContext;
import org.webswing.toolkit.api.clipboard.WebswingClipboardData;
import org.webswing.toolkit.api.component.HtmlPanel;
import org.webswing.toolkit.api.lifecycle.WebswingShutdownListener;
import org.webswing.toolkit.api.security.WebswingUser;
import org.webswing.toolkit.api.security.WebswingUserListener;
import org.webswing.toolkit.api.url.WebswingUrlState;
import org.webswing.toolkit.api.url.WebswingUrlStateChangeListener;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Window;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Webswing API used by Swing application for easy integration.
 */
public interface WebswingApi {

  /**
   * Return the user of connected web session. <b>Note:</b>if user disconnects/closes browser, this
   * method will return null.
   *
   * @return user or null if session is disconnected.
   */
  public WebswingUser getPrimaryUser();

  /**
   * Return the user of connected mirror view web session. (Admin console - view session) Note: if
   * admin disconnects/closes browser, this method will return null.
   *
   * @return user or null if mirror view session is disconnected.
   */
  public WebswingUser getMirrorViewUser();

  /**
   * Check if connected web session user has role defined. If no user is connected null is returned.
   * 
   * @param role name of role
   * @return True if user has the role, false if not. Null if no user is connected
   * @throws WebswingApiException if communication with server fails.
   */
  public Boolean primaryUserHasRole(String role) throws WebswingApiException;

  /**
   * Check if connected web session user has permission defined. If no user is connected null is
   * returned.
   * 
   * @param permission name of permission
   * @return True if user has the permission, false if not. Null if no user is connected
   * @throws WebswingApiException if communication with server fails.
   */
  public Boolean primaryUserIsPermitted(String permission) throws WebswingApiException;

  /**
   * Add listener to receive notifications when user (primary/mirror view) connects or disconnects
   * from session
   * 
   * @param listener listener to add
   */
  public void addUserConnectionListener(WebswingUserListener listener);

  /**
   * Remove user listener
   * 
   * @param listener listener to remove
   */
  public void removeUserConnectionListener(WebswingUserListener listener);

  /**
   * When swing application exit and shutdown process takes longer time to process, invoking this
   * method will notify user (web session) the application has finished, and disconnect the user
   * from the session. Swing Application process is removed from running sessions list even though
   * the process might still be running.
   *
   * @param forceKillTimeout how long (in Ms) to wait for process to finish. After this time the
   *        process is forcefully terminated.
   */
  public void notifyShutdown(int forceKillTimeout);

  /**
   * Add listener that is triggered when Webswing requests swing application to exit. If there is no
   * explicit shutdown listener Webswing use default shutdown procedure (send window closing event
   * to all windows). Otherwise listeners are fired. It is expected the result of listener execution
   * will exit the swing process. Otherwise the swing process will be forcefully terminated after
   * defined timeout (system property "webswing.waitForExit", default: 30000)
   * 
   * @param listener listener to add
   */
  public void addShutdownListener(WebswingShutdownListener listener);

  /**
   * Remove shutdown listener
   * 
   * @param listener listener to remove
   */
  public void removeShutdownListener(WebswingShutdownListener listener);

  /**
   * @return the Webswing version in 'git describe' format
   */
  public String getWebswingVersion();

  /**
   * See {@link WebswingApi#setUrlState(WebswingUrlState, boolean)}. This method will not trigger
   * url change event in {@link WebswingUrlStateChangeListener}
   *
   * @param state state object url is generated from
   */
  public void setUrlState(WebswingUrlState state);

  /**
   * Sets the hash Fragment of users browser URL to represent the current state of the swing
   * application. Intended for use in combination with {@link WebswingUrlStateChangeListener} and/or
   * {@link WebswingApi#getUrlState()}
   *
   * @param state state object url is generated from
   * @param fireChangeEvent if true, invoking this method will trigger url change event
   */
  public void setUrlState(WebswingUrlState state, boolean fireChangeEvent);

  /**
   * @return current user's URL state (parsed hash fragment of URL) or null if no user is connected.
   */
  public WebswingUrlState getUrlState();

  /**
   * Remove URL state change listener
   *
   * @param listener the listener
   */
  public void removeUrlStateChangeListener(WebswingUrlStateChangeListener listener);

  /**
   * Register a URL state change listener
   *
   * @param listener the listener
   */
  public void addUrlStateChangeListener(WebswingUrlStateChangeListener listener);

  /**
   * Reset session timeout to prevent automatic termination. Useful if a long running operation has
   * to finish even if user disconnects or is inactive for longer timeframe. Note: Reset needs to be
   * called in periods shorter than configured session timeout. ("webswing.sessionTimeoutSec" system
   * property) Note2: This method has no effect if session timeout is set to 0
   */
  public void resetInactivityTimeout();

  /**
   * Clipboard data received from browser after CTRL+V key events (browser security allows access to
   * clipboard only in these events). <br>
   * Typically used for customized clipboard integration, while the built-in integration is disabled
   * in configuration ("allowLocalClipboard" is false). <br>
   * Example use case: <br>
   * 1.Create new context menu item "Paste from browser", that will open a modal dialog asking user
   * to press CTRL+V. <br>
   * 2.Listen for CTRL+V keystroke. When received, call this method to get the clipboard content.
   *
   * @return Latest clipboard content received from browser
   */
  public BrowserTransferable getBrowserClipboard();

  /**
   * Request user to paste from browser clipboard by showing built-in html modal dialog. This method
   * will block EDT thread (by showing a invisible modal JDialog) until response received from user.
   * <br>
   * Typically used for customized clipboard integration, while the built-in integration is disabled
   * in configuration ("allowLocalClipboard" is false).
   *
   * @param ctx request context
   * @return User submited clipboard content received from browser (null if cancelled)
   */
  public BrowserTransferable getBrowserClipboard(PasteRequestContext ctx);

  /**
   * Sends the specified data to browser. <br>
   * A toolbar will appear in browser displaying the data. User can click or press CTRL+C to store
   * the content to local clipboard. <br>
   * Typically used for customized clipboard integration, while the built-in integration is disabled
   * in configuration ("allowLocalClipboard" is false).
   * 
   * @param content clipboard data to be sent to browser
   */
  public void sendClipboard(WebswingClipboardData content);

  /**
   * Sends the current Swing clipboard content to browser. <br>
   * A toolbar will appear in browser displaying the data. User can click or press CTRL+C to store
   * the content to local clipboard. <br>
   * Typically used for customized clipboard integration, while the built-in integration is disabled
   * in configuration ("allowLocalClipboard" is false).
   */
  public void sendClipboard();

  /**
   * Adds a WebswingBrowserActionListener to listen to javascript browser initiated events.
   * 
   * @param listener The listener to add
   */
  public void addBrowserActionListener(WebActionListener listener);

  /**
   * Removed a WebswingBrowserActionListener.
   * 
   * @param listener The listener to remove
   */
  public void removeBrowserActionListener(WebActionListener listener);

  /**
   * Sends an action event with optional data to the browser.
   * 
   * @param actionName name of action to be sent
   * @param data string encoded data
   * @param binaryData binary data
   */
  public void sendActionEvent(String actionName, String data, byte[] binaryData);

  /**
   * Sends an action event to a WebWindow, with optional data to the browser.
   * 
   * @param webWindow window to recieve the action
   * @param actionName name of action to be sent
   * @param data string encoded data
   * @param binaryData binary data
   */
  public void sendActionEvent(WebWindow webWindow, String actionName, String data,
      byte[] binaryData);

  /**
   * Creates an HtmlPanel component. Compositing window manager must be enabled in config.
   * 
   * @throws IllegalArgumentException if isCompositingWindowManager() returns false
   */
  public HtmlPanel createHtmlPanel();

  /**
   * Creates an HtmlPanel component which is a part of given component and has a container parent.
   * Use this method to create an HtmlPanel inside a WebContainer parent container. This is required
   * for web container to correctly compute z-order of its child component canvases. Compositing
   * window manager must be enabled in config.
   * 
   * @throws IllegalArgumentException if isCompositingWindowManager() returns false
   */
  public HtmlPanel createHtmlPanelForWebContainerComponent(Container webContainer,
      JComponent componentParentOfHtmlPanel);

  /**
   * Registers given container to be a parent web container and all of its child components will be
   * rendered into separate canvases. Compositing window manager must be enabled in config.
   * 
   * @throws IllegalArgumentException if isCompositingWindowManager() returns false
   */
  public void registerWebContainer(Container container);

  /**
   * Is compositing window manager enabled?
   */
  public boolean isCompositingWindowManager();

  /**
   * Is docking enabled for window? Compositing window manager and docking mode in config must be
   * enabled.
   */
  public boolean isDockingEnabled(Window window);

  /**
   * Toggle the dock state of a window. Compositing window manager must be enabled in config. Window
   * must be allowed to change the dock state according to config.
   * 
   * @throws IllegalArgumentException if isCompositingWindowManager() or isDockingEnabled(window)
   *         returns false
   */
  public void toggleWindowDock(Window window);

  /**
   * Toggle the dock state of a window. Compositing window manager must be enabled in config. Window
   * must be allowed to change the dock state according to config.
   * 
   * @throws IllegalArgumentException if isCompositingWindowManager() or isDockingEnabled(window)
   *         returns false
   */
  public void toggleWindowDock(Window window, boolean undocked);

  /**
   * Check whether window is in undocked state. Compositing window manager must be enabled in
   * config.
   * 
   * @throws IllegalArgumentException if isCompositingWindowManager() returns false
   */
  public boolean isUndocked(Window window);

  /**
   * Is touch mode active?
   */
  public boolean isTouchMode();


  /**
   * Register JFileChooser that should show Webswing integration toolbar if embedded in custom
   * window. Transparent mode is not available in case of custom window.
   * 
   * @param fileChooser instance of file chooser
   * @param parent custom window that contains this filechooser
   */
  public void registerCustomFileChooser(JFileChooser fileChooser, Window parent);

  // ===========================================================================
  // Host properties — generic runtime configuration of the application JVM side
  // ===========================================================================
  //
  // Signatures use JDK types only, so applications can call these reflectively without a
  // compile-time dependency on this API. Methods added after the WebSwing 20.2.5 API are default
  // methods, so existing implementations (e.g. test doubles) keep compiling.

  /**
   * Returns a host property: the JVM system property of the key if set (the deployment's
   * {@code jvmArgs} — explicit configuration always wins), otherwise the value set at runtime
   * through {@link #setProperties(Map)}.
   *
   * @param key property key, e.g. {@code org.webswing.window.theme.primaryColor}
   * @return the value, or {@code null} when neither a system property nor a runtime value exists
   */
  public default String getProperty(String key) {
    return key == null ? null : System.getProperty(key);
  }

  /**
   * Sets or clears host properties at runtime. A {@code null} value removes the runtime value, so
   * the key falls back to its system property (or the consumer's built-in default).
   *
   * <p>
   * All values are applied first; listeners registered through
   * {@link #addPropertyChangeListener(String, PropertyChangeListener)} are then notified
   * synchronously on the calling thread, once per key whose effective value changed.
   *
   * <p>
   * Runtime values live in an overlay and are <b>not</b> written to {@link System#getProperties()}.
   * A key set as system property ({@code jvmArgs}) stays fixed: the runtime value is kept but not
   * effective, so existing deployments are unaffected. A key only takes effect where its consumer
   * reads it through {@link #getProperty(String)}: immediately for consumers listening on it,
   * otherwise the next time the consumer reads it. A key no listener covers is logged as a warning.
   *
   * @param properties key/value pairs; {@code null} values allowed
   * @throws UnsupportedOperationException if the implementation does not support runtime properties
   */
  public default void setProperties(Map<String, String> properties) {
    throw new UnsupportedOperationException("setProperties");
  }

  /**
   * Convenience for setting a single host property. See {@link #setProperties(Map)}.
   *
   * @param key property key
   * @param value property value, or {@code null} to remove the runtime value
   */
  public default void setProperty(String key, String value) {
    Map<String, String> m = new HashMap<>(1);
    m.put(key, value);
    setProperties(m);
  }

  /**
   * Registers a listener for runtime changes of every host property whose key starts with
   * {@code keyPrefix}. An empty prefix matches every key.
   *
   * @param keyPrefix key prefix, e.g. {@code org.webswing.window.theme.}
   * @param listener listener receiving one event per changed key
   */
  public default void addPropertyChangeListener(String keyPrefix, PropertyChangeListener listener) {
    // no runtime properties in this implementation: nothing will ever change
  }

  /**
   * Removes a listener registered with
   * {@link #addPropertyChangeListener(String, PropertyChangeListener)} for the same prefix.
   *
   * @param keyPrefix key prefix used at registration
   * @param listener listener to remove
   */
  public default void removePropertyChangeListener(String keyPrefix,
      PropertyChangeListener listener) {
    // see addPropertyChangeListener
  }

  // ===========================================================================
  // Web fonts
  // ===========================================================================

  /**
   * Registers a TrueType font so DirectDraw renders text in it natively in the browser, without a
   * {@code fontConfig} entry and without glyph fallback. Typically called with the same bytes the
   * application passes to {@link Font#createFont(int, java.io.InputStream)} and
   * {@link java.awt.GraphicsEnvironment#registerFont(Font)}; register before the font is first
   * painted. Idempotent for identical bytes.
   *
   * <p>
   * The font is identified by its content and its face name as parsed from these bytes, so a
   * mismatch between configured and actual font names cannot occur. Registered fonts are shipped to
   * the browser, so only register fonts whose licence permits that.
   *
   * @param ttf TrueType font file content
   * @return the registered face name, or {@code null} if the bytes are not a readable TrueType font
   *         or the implementation does not support web fonts
   */
  public default String registerWebFont(byte[] ttf) {
    return null;
  }

  // ===========================================================================
  // Window theme — typed convenience over the host properties
  // ===========================================================================

  /** Key prefix of all window-decoration theme properties. */
  public static final String WINDOW_THEME_PREFIX = "org.webswing.window.theme.";
  /** Active title bar colour ({@code #rrggbb}). */
  public static final String WINDOW_THEME_PRIMARY = WINDOW_THEME_PREFIX + "primaryColor";
  /** Inactive title bar colour. */
  public static final String WINDOW_THEME_SECONDARY = WINDOW_THEME_PREFIX + "secondaryColor";
  /** Brand accent (hover). */
  public static final String WINDOW_THEME_ACCENT = WINDOW_THEME_PREFIX + "accentColor";
  /** Fill of the chrome margin, should match the content pane. */
  public static final String WINDOW_THEME_CONTENT_BG = WINDOW_THEME_PREFIX + "contentBgColor";
  /** Title font: {@code Face-STYLE-size} ({@link Font#decode(String)} form) or a bare face name. */
  public static final String WINDOW_THEME_TITLE_FONT = WINDOW_THEME_PREFIX + "titleFont";
  /** Title bar height in px — startup-only. */
  public static final String WINDOW_THEME_TITLE_HEIGHT = WINDOW_THEME_PREFIX + "titleHeight";
  /** Visible border width in px — startup-only. */
  public static final String WINDOW_THEME_BORDER_WIDTH = WINDOW_THEME_PREFIX + "borderWidth";

  /**
   * Sets the window-decoration theme at runtime in one batch; open windows repaint immediately. A
   * {@code null} argument resets that value to the built-in default. A value configured as system
   * property ({@code jvmArgs}) keeps precedence, so existing deployments look unchanged until their
   * {@code jvmArgs} are removed. Geometry is startup-only and not part of this call (use
   * {@link #setProperties(Map)} with {@link #WINDOW_THEME_TITLE_HEIGHT} /
   * {@link #WINDOW_THEME_BORDER_WIDTH} before the first window, or {@code jvmArgs}).
   *
   * <p>
   * A value present in {@code UIManager} under the same key (e.g. from a FlatLaf theme
   * {@code .properties}) also takes precedence over this call.
   *
   * @param primary active title bar colour
   * @param secondary inactive title bar colour
   * @param accent brand accent
   * @param contentBg chrome margin fill
   * @param titleFont title font (face, style and size are used)
   */
  public default void setWindowTheme(Color primary, Color secondary, Color accent, Color contentBg,
      Font titleFont) {
    Map<String, String> m = new HashMap<>(8);
    m.put(WINDOW_THEME_PRIMARY, toHex(primary));
    m.put(WINDOW_THEME_SECONDARY, toHex(secondary));
    m.put(WINDOW_THEME_ACCENT, toHex(accent));
    m.put(WINDOW_THEME_CONTENT_BG, toHex(contentBg));
    m.put(WINDOW_THEME_TITLE_FONT, toFontSpec(titleFont));
    setProperties(m);
  }

  private static String toHex(Color c) {
    return c == null ? null : String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
  }

  private static String toFontSpec(Font f) {
    if (f == null) {
      return null;
    }
    String style = switch (f.getStyle()) {
      case Font.BOLD -> "BOLD";
      case Font.ITALIC -> "ITALIC";
      case Font.BOLD | Font.ITALIC -> "BOLDITALIC";
      default -> "PLAIN";
    };
    return f.getFontName() + "-" + style + "-" + f.getSize();
  }

}
