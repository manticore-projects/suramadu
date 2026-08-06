package org.webswing.server.common.util;

import com.google.common.collect.Iterators;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.Constants;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.node.ObjectNode;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class ServerUtil {

  private static final Logger log = LoggerFactory.getLogger(ServerUtil.class);

  private static final DateFormat EXPIRES_FORMAT =
      new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);

  private static final String HEADER_AUTHORIZATION = "Authorization";
  private static final String BEARER_TYPE = "Bearer";

  private static final JsonMapper mapper = new JsonMapper();

  static {
    EXPIRES_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
  }

  public static String getClientIp(HttpServletRequest r) {
    String result = null;
    result = r.getHeader("X-Forwarded-For");
    if (result == null) {
      result = r.getRemoteAddr();
    }
    return result;
  }

  public static String getClientOs(String userAgent) {
    if (userAgent == null) {
      return "Unknown";
    }
    if (userAgent.toLowerCase().indexOf("windows") >= 0) {
      return "Windows";
    } else if (userAgent.toLowerCase().indexOf("mac") >= 0) {
      return "Mac";
    } else if (userAgent.toLowerCase().indexOf("x11") >= 0) {
      return "Linux";
    } else if (userAgent.toLowerCase().indexOf("android") >= 0) {
      return "Android";
    } else if (userAgent.toLowerCase().indexOf("iphone") >= 0) {
      return "IPhone";
    } else {
      return "Unknown";
    }
  }

  public static String domainFromUrl(String fullUrl) {
    try {
      URL url = new URL(fullUrl);
      return url.getProtocol() + "://" + url.getHost()
          + (url.getPort() != -1 ? ":" + url.getPort() : "");
    } catch (MalformedURLException e) {
      return null;
    }
  }

  public static boolean isAdminUrlSameOrigin(String adminUrl, String url) {
    if (StringUtils.isBlank(adminUrl)) {
      return false;
    }

    if (adminUrl.startsWith("http")) {
      return Objects.equals(ServerUtil.domainFromUrl(adminUrl), ServerUtil.domainFromUrl(url));
    }

    // adminUrl is relative, consider it same origin
    return true;
  }

  public static String getClientBrowser(String userAgent) {
    if (userAgent == null) {
      return "Unknown";
    }
    String ua = userAgent.toLowerCase();

    // Order matters: check more specific UA tokens before generic ones,
    // since Chromium-based browsers all include "chrome" in the UA string.

    if (ua.contains("edg/")) {
      // Chromium-based Edge (not legacy "Edge/")
      return extractBrowserVersion(userAgent, "Edg");
    } else if (ua.contains("opr/") || ua.contains("opera")) {
      // Opera (modern OPR/ or legacy Opera/)
      String token = ua.contains("opr/") ? "OPR" : "Opera";
      return extractBrowserVersion(userAgent, token).replace("OPR", "Opera");
    } else if (ua.contains("samsungbrowser/")) {
      return extractBrowserVersion(userAgent, "SamsungBrowser");
    } else if (ua.contains("firefox/")) {
      return extractBrowserVersion(userAgent, "Firefox");
    } else if (ua.contains("chrome/")) {
      // Must come after Edge, Opera, Samsung — they all contain "chrome"
      return extractBrowserVersion(userAgent, "Chrome");
    } else if (ua.contains("safari/") && ua.contains("version/")) {
      // Safari (real Safari, not Chrome pretending)
      return extractBrowserVersion(userAgent, "Version").replace("Version", "Safari");
    }

    return "Unknown";
  }

  private static String extractBrowserVersion(String userAgent, String token) {
    int idx = userAgent.indexOf(token);
    if (idx < 0) {
      return token;
    }
    // Grab "Token/1.2.3" and replace "/" with "-"
    String fragment = userAgent.substring(idx).split(" ")[0];
    return fragment.replace("/", "-");
  }

  public static URL getFileResource(String resource, File folder) {
    if (resource == null || folder == null || !folder.isDirectory()) {
      return null;
    }
    try {
      // Resolve and canonicalize both paths, then verify the resolved file is strictly
      // contained within the folder. Path.startsWith is a component-wise check (not a
      // substring check), so it correctly blocks traversal sequences such as
      // "../../etc/passwd" in the user-provided resource. (CWE-22)
      Path base = folder.getCanonicalFile().toPath();
      Path target = new File(folder, resource).getCanonicalFile().toPath();
      if (!target.startsWith(base)) {
        log.warn("Blocked path traversal attempt for resource: {}", sanitizeForLog(resource));
        return null;
      }
      File file = target.toFile();
      if (file.isFile()) {
        return file.toURI().toURL();
      }
    } catch (IOException e) {
      log.error("Failed to get file from Folder.", e);
    }
    return null;
  }

  public static URL getWebResource(String resource, ServletContext servletContext, File webFolder) {
    URL result = getFileResource(resource, webFolder);
    if (result == null && isSafeContextResourcePath(resource)) {
      try {
        result = servletContext.getResource(resource);
      } catch (MalformedURLException e) {
        log.error("Failed to get file from Web context path.", e);
      }
    }
    return result;
  }

  /**
   * Validate a path before it is resolved against the servlet context. The path must be non-empty,
   * must not reference a network/URL location, and must not contain any traversal segment. This
   * keeps {@link ServletContext#getResource(String)} confined to the web application root. (CWE-22)
   */
  private static boolean isSafeContextResourcePath(String resource) {
    if (resource == null || resource.isEmpty()) {
      return false;
    }
    // Reject protocol-relative / absolute URLs and backslash separators.
    if (resource.contains("://") || resource.startsWith("//") || resource.indexOf('\\') >= 0) {
      return false;
    }
    // Reject any traversal segment, then confirm the normalized form stays in-context.
    if (resource.contains("..")) {
      return false;
    }
    Path normalized = Paths.get(resource).normalize();
    return !normalized.startsWith("..");
  }

  public static boolean isFileLocked(File file) {
    if (file.exists()) {
      try {
        Path source = file.toPath();
        Path dest = file.toPath().resolveSibling(file.getName() + ".wstest");
        Files.move(source, dest);
        Files.move(dest, source);
        return false;
      } catch (IOException e) {
        return true;
      }
    }
    return false;
  }

  public static String getContextPath(ServletContext ctx) {
    String contextPath = ctx.getContextPath();
    String contextPathExplicit = System.getProperty(Constants.REVERSE_PROXY_CONTEXT_PATH);
    if (contextPathExplicit != null) {
      return CommonUtil.toPath(contextPathExplicit);
    } else if (contextPath != null && !"/".equals(contextPath) && !"".equals(contextPath)) {
      return CommonUtil.toPath(contextPath);
    } else {
      return "";
    }
  }

  public static void sendHttpRedirect(HttpServletRequest req, HttpServletResponse resp,
      String relativeUrl) throws IOException {
    String safeUrl = sanitizeRedirectUrl(req, relativeUrl);
    resp.sendRedirect(safeUrl);
  }

  /**
   * Sanitize a string for safe inclusion in log messages.
   * <ul>
   * <li>Strips all Unicode control characters (C0, C1, DEL)</li>
   * <li>Strips Unicode line/paragraph separators</li>
   * <li>Strips bidirectional overrides (Trojan Source defense)</li>
   * <li>Strips zero-width and invisible formatting characters</li>
   * <li>Truncates to a maximum length to prevent log flooding</li>
   * </ul>
   */
  private static final int MAX_LOG_LENGTH = 1000;

  private static final Pattern UNSAFE_LOG_CHARS = Pattern.compile("[\\p{Cc}" // C0 control chars
                                                                             // (U+0000–U+001F), DEL
                                                                             // (U+007F), C1
                                                                             // (U+0080–U+009F)
      + "\\u200B-\\u200F" // zero-width space, ZWNJ, ZWJ, LRM, RLM
      + "\\u2028\\u2029" // Unicode line separator, paragraph separator
      + "\\u202A-\\u202E" // bidi overrides: LRE, RLE, PDF, LRO, RLO
      + "\\u2060-\\u2064" // word joiner, invisible operators
      + "\\u2066-\\u2069" // bidi isolates: LRI, RLI, FSI, PDI
      + "\\uFEFF" // BOM / zero-width no-break space
      + "]");

  private static String sanitizeForLog(String input) {
    if (input == null) {
      return "null";
    }
    String sanitized = UNSAFE_LOG_CHARS.matcher(input).replaceAll("_");
    if (sanitized.length() > MAX_LOG_LENGTH) {
      sanitized = sanitized.substring(0, MAX_LOG_LENGTH) + "...(truncated)";
    }
    return sanitized;
  }

  private static String sanitizeRedirectUrl(HttpServletRequest req, String relativeUrl) {
    if (relativeUrl == null || relativeUrl.isEmpty()) {
      return "/";
    }

    // 1. Reject absolute URLs entirely — redirects should always be path-based
    if (relativeUrl.contains("://") || relativeUrl.startsWith("//")
        || relativeUrl.startsWith("\\\\")) {
      log.warn("Blocked absolute redirect attempt: {}", sanitizeForLog(relativeUrl));
      return "/";
    }

    // 2. Reject protocol-relative tricks and dangerous schemes
    // Covers "javascript:", "data:", CRLF header injection, etc.
    if (relativeUrl.matches("(?i)^\\s*(javascript|data|vbscript):.*") || relativeUrl.contains("\r")
        || relativeUrl.contains("\n") || relativeUrl.contains("%0d")
        || relativeUrl.contains("%0a")) {
      log.warn("Blocked malicious redirect attempt: {}", sanitizeForLog(relativeUrl));
      return "/";
    }

    // 3. Normalize to a context-absolute path so the browser can't reinterpret it
    if (!relativeUrl.startsWith("/")) {
      String requestPath =
          ServerUtil.getContextPath(req.getServletContext()) + CommonUtil.toPath(req.getPathInfo());
      if (!requestPath.startsWith("/")) {
        requestPath = "/" + requestPath;
      }
      String requestPathBase = requestPath.substring(0, requestPath.lastIndexOf("/") + 1);
      relativeUrl = requestPathBase + relativeUrl;
    }

    // 4. Final guard: only ever return a same-origin path beginning with a single "/".
    // We deliberately do NOT build an absolute URL from forwarding headers
    // (X-Forwarded-Host / X-Forwarded-Proto): those values are client-influenced and only
    // format-validated, so concatenating them would re-introduce an open-redirect /
    // host-header-injection vector (CWE-601). A relative Location header is resolved
    // correctly by the servlet container and by any trusted reverse proxy.
    if (relativeUrl.startsWith("/") && !relativeUrl.startsWith("//")) {
      return relativeUrl;
    }
    return "/";
  }

  public static String normalizeForFileName(String text) {
    return text.replaceAll("\\W+", "_");
  }

  /**
   * Extract the bearer token from a header.
   *
   * @param request The request.
   * @return The token, or null if no Authorization header was supplied.
   */
  public static String extractBearerToken(HttpServletRequest request) {
    Enumeration<String> headers = request.getHeaders(HEADER_AUTHORIZATION);
    if (headers == null) {
      return null;
    }
    return extractBearerToken(Iterators.forEnumeration(headers));
  }

  public static String extractBearerToken(Map<String, List<String>> map) {
    if (map == null) {
      return null;
    }
    if (!map.containsKey(Constants.HTTP_ATTR_TOKEN)) {
      return null;
    }

    List<String> paramValues = map.get(Constants.HTTP_ATTR_TOKEN);

    if (paramValues == null || paramValues.isEmpty()) {
      return null;
    }

    return paramValues.get(0);
  }

  private static String extractBearerToken(Iterator<String> headers) {
    while (headers.hasNext()) { // typically there is only one (most servers enforce that)
      String value = headers.next();
      if (value.toLowerCase().startsWith(BEARER_TYPE.toLowerCase())) {
        String authHeaderValue = value.substring(BEARER_TYPE.length()).trim();
        int commaIndex = authHeaderValue.indexOf(',');
        if (commaIndex > 0) {
          authHeaderValue = authHeaderValue.substring(0, commaIndex);
        }
        return authHeaderValue;
      }
    }
    return null;
  }

  public static String parseTokenFromCookie(HttpServletRequest req, String cookieName) {
    if (req.getCookies() == null) {
      return null;
    }

    String proxypath = System.getProperty(Constants.REVERSE_PROXY_CONTEXT_PATH, "")
        .replaceAll("[^A-Za-z0-9]", "_");
    cookieName += proxypath;

    String token_cookie = null;

    for (Cookie cookie : req.getCookies()) {
      if (cookieName.equals(cookie.getName())) {
        token_cookie = cookie.getValue();
        break;
      }
    }

    return token_cookie;
  }

  public static void setTokenCookie(HttpServletResponse resp, String cookieName, String token) {
    setTokenCookie(resp, cookieName, token, false);
  }

  public static void setTokenCookie(HttpServletResponse resp, String cookieName, String token,
      boolean clear) {
    setCookie(resp, cookieName, token, null, clear);
  }

  public static void setCookie(HttpServletResponse resp, String cookieName, String value,
      String path, boolean clear) {
    String proxypath = System.getProperty(Constants.REVERSE_PROXY_CONTEXT_PATH, "")
        .replaceAll("[^A-Za-z0-9]", "_");
    boolean serverIsHttpsOnly = Boolean.getBoolean(Constants.HTTPS_ONLY);

    StringBuilder cookieHeader = new StringBuilder();
    cookieHeader.append(cookieName).append(proxypath);
    cookieHeader.append("=");
    cookieHeader.append(value);

    if (clear) {
      cookieHeader.append("; Expires=").append(EXPIRES_FORMAT.format(new Date(0)));
    }
    if (path == null) {
      cookieHeader.append("; Path=/");
    } else {
      cookieHeader.append("; Path=").append(path);
    }
    cookieHeader.append("; HttpOnly");

    if (serverIsHttpsOnly) {
      cookieHeader.append("; Secure");
      cookieHeader.append("; SameSite=")
          .append(System.getProperty(Constants.COOKIE_SAMESITE, "NONE").toUpperCase());
    }

    resp.addHeader("Set-Cookie", cookieHeader.toString());
  }

  public static void writeLoginSessionToken(HttpServletResponse resp,
      String serializedLoginSessionClaim) {
    String loginSessionToken = JwtUtil.createLoginSessionToken(serializedLoginSessionClaim);
    ServerUtil.setTokenCookie(resp, Constants.WEBSWING_SESSION_LOGIN_SESSION_TOKEN,
        loginSessionToken);
  }

  public static void writeTokens(HttpServletResponse resp, String serializedWebswingClaim,
      boolean cookieOnly) {
    String refreshToken = JwtUtil.createRefreshToken(serializedWebswingClaim);
    String transferToken = JwtUtil.createTransferToken(serializedWebswingClaim);
    String adminConsoleLoginToken = JwtUtil.createAdminConsoleLoginToken(serializedWebswingClaim);

    ServerUtil.setTokenCookie(resp, Constants.WEBSWING_SESSION_REFRESH_TOKEN, refreshToken);
    ServerUtil.setTokenCookie(resp, Constants.WEBSWING_SESSION_TRANSFER_TOKEN, transferToken);
    ServerUtil.setTokenCookie(resp, Constants.WEBSWING_SESSION_ADMIN_CONSOLE_LOGIN_TOKEN,
        adminConsoleLoginToken);

    if (!cookieOnly) {
      String accessToken = JwtUtil.createAccessToken(serializedWebswingClaim);

      ObjectNode result = mapper.createObjectNode();
      result.put("accessToken", accessToken);

      resp.setContentType("application/json");
      resp.setCharacterEncoding("UTF-8");

      try {
        resp.getWriter().write(result.toString());
      } catch (IOException e) {
        log.error("Could not write token to response!", e);
      }
    }
  }

  public static void clearLoginTokenFromCookies(HttpServletResponse resp) {
    ServerUtil.setTokenCookie(resp, Constants.WEBSWING_SESSION_LOGIN_SESSION_TOKEN, "expired",
        true);
  }

  public static void clearTokensFromCookies(HttpServletResponse resp) {
    ServerUtil.setTokenCookie(resp, Constants.WEBSWING_SESSION_REFRESH_TOKEN, "expired", true);
    ServerUtil.setTokenCookie(resp, Constants.WEBSWING_SESSION_TRANSFER_TOKEN, "expired", true);
    ServerUtil.setTokenCookie(resp, Constants.WEBSWING_SESSION_ADMIN_CONSOLE_LOGIN_TOKEN, "expired",
        true);
  }

  public static void clearAdminConsoleCookie(HttpServletResponse resp) {
    ServerUtil.setTokenCookie(resp, Constants.WEBSWING_SESSION_ADMIN_CONSOLE_REFRESH_TOKEN,
        "expired", true);
    ServerUtil.setTokenCookie(resp, Constants.WEBSWING_SESSION_ADMIN_CONSOLE_DOWNLOAD_TOKEN,
        "expired", true);
  }

  private ServerUtil() {}

}
