package org.webswing.server.api.services.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.server.api.base.AbstractUrlHandler;
import org.webswing.server.api.util.ServerApiUtil;
import org.webswing.server.common.util.CommonUtil;
import org.webswing.server.model.exception.WsException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

public abstract class AbstractResourceHandler extends AbstractUrlHandler
    implements ResourceHandler {

  private static final Logger log = LoggerFactory.getLogger(AbstractResourceHandler.class);

  private final WebResourceProvider webResourceProvider;

  public AbstractResourceHandler(AbstractUrlHandler parent,
      WebResourceProvider webResourceProvider) {
    super(parent);
    this.webResourceProvider = webResourceProvider;
  }

  @Override
  protected String getPath() {
    return "";
  }

  @Override
  public boolean serve(HttpServletRequest req, HttpServletResponse res) throws WsException {
    try {
      if ("GET".equals(req.getMethod())) {
        return lookup(req).respondGet(req, res);
      } else if ("HEAD".equals(req.getMethod())) {
        return lookup(req).respondHead(req, res);
      }
      return false;
    } catch (IOException e) {
      throw new WsException("Failed to process resource.", e);
    }
  }

  protected interface LookupResult {
    boolean respondGet(HttpServletRequest req, HttpServletResponse resp) throws IOException;

    boolean respondHead(HttpServletRequest req, HttpServletResponse resp) throws IOException;

    long getLastModified();
  }

  protected static class ErrorResult implements LookupResult {
    protected final int statusCode;
    protected final String message;

    public ErrorResult(int statusCode, String message) {
      this.statusCode = statusCode;
      this.message = message;
    }

    public long getLastModified() {
      return -1;
    }

    public boolean respondGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
      if (statusCode == HttpServletResponse.SC_NOT_FOUND) {
        return false;
      } else {
        resp.sendError(statusCode, message);
        return true;
      }
    }

    public boolean respondHead(HttpServletRequest req, HttpServletResponse resp) {
      return false;
    }
  }

  private static class RedirectResult implements LookupResult {

    private final String path;

    public RedirectResult(String path) {
      if (path.startsWith("/")) {
        path = path.substring(1);
      }
      this.path = path;
    }

    public long getLastModified() {
      return -1;
    }

    public boolean respondGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
      // Validate that the redirect path is safe (relative, no authority component)
      if (isUnsafeRedirectPath(path)) {
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid path");
        return true;
      }
      ServerApiUtil.sendHttpRedirect(req, resp, path);
      return true;
    }

    public boolean respondHead(HttpServletRequest req, HttpServletResponse resp)
        throws IOException {
      if (isUnsafeRedirectPath(path)) {
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid path");
        return true;
      }
      ServerApiUtil.sendHttpRedirect(req, resp, path);
      return true;
    }
  }

  private static class ResourceUrl implements LookupResult {
    protected final URLConnection url;
    private final String mime;

    public ResourceUrl(String mime, URLConnection url) {
      this.mime = mime;
      this.url = url;
    }

    public long getLastModified() {
      return this.url.getLastModified();
    }

    protected void setHeaders(HttpServletResponse resp) {
      resp.setStatus(HttpServletResponse.SC_OK);
      resp.setHeader("Cache-Control", "public, max-age=120");
      resp.setDateHeader("Last-Modified", getLastModified());
      resp.setContentType(mime);
      // Prevent browsers from MIME-sniffing the content type
      resp.setHeader("X-Content-Type-Options", "nosniff");
      if (url.getContentLength() >= 0) {
        resp.setContentLength(url.getContentLength());
      }
    }

    public boolean respondGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
      long ims = req.getDateHeader("If-Modified-Since");
      if (ims != -1 && Math.abs(ims - getLastModified()) < 1000) { // modification timestamp is same
                                                                   // rounded to seconds
        resp.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
      } else {
        setHeaders(resp);
        final OutputStream os = resp.getOutputStream();
        CommonUtil.transferStreams(url.getInputStream(), os);
      }
      return true;
    }

    public boolean respondHead(HttpServletRequest req, HttpServletResponse resp) {
      setHeaders(resp);
      return true;
    }
  }

  private static class PreCompressedResourceUrl extends ResourceUrl {
    private final String contentEncoding;

    public PreCompressedResourceUrl(String mime, URLConnection url, String contentEncoding) {
      super(mime, url);
      this.contentEncoding = contentEncoding;
    }

    @Override
    protected void setHeaders(HttpServletResponse resp) {
      super.setHeaders(resp);
      resp.setHeader("Content-Encoding", contentEncoding);
      resp.setHeader("Vary", "Accept-Encoding");
    }
  }

  @Override
  public long getLastModified(HttpServletRequest req) {
    return lookup(req).getLastModified();
  }

  protected LookupResult lookup(HttpServletRequest req) {
    LookupResult r = (LookupResult) req.getAttribute("lookupResult-" + getFullPathMapping());
    if (r == null) {
      r = lookupNoCache(req);
      req.setAttribute("lookupResult-" + getFullPathMapping(), r);
    }
    return r;
  }

  protected LookupResult lookupNoCache(HttpServletRequest req) {
    String path = getPathInfo(req);
    return lookupNoCache(req, path);
  }

  protected LookupResult lookupNoCache(HttpServletRequest req, String path) {
    if (path.isEmpty()) {
      path = "/index.html";
    }

    // Normalize and validate the path to prevent path traversal
    path = normalizePath(path);
    if (path == null) {
      return new ErrorResult(HttpServletResponse.SC_BAD_REQUEST, "Invalid path");
    }

    if (isForbidden(path)) {
      return new ErrorResult(HttpServletResponse.SC_NOT_FOUND, "Forbidden");
    }

    URL url = webResourceProvider.getWebResource(path + "/index.html"); // check if this is folder
                                                                        // with default index
    if (url != null && !req.getPathInfo().endsWith("/")) {
      return new RedirectResult(path + "/");
    }
    if (url == null) {
      url = webResourceProvider.getWebResource(path);
    }
    if (url == null) {
      return new ErrorResult(HttpServletResponse.SC_NOT_FOUND, "Not found");
    }

    String mimeType = getMimeType(url.getPath());

    // Check for pre-compressed variants (.br, .gz)
    String acceptEncoding = req.getHeader("Accept-Encoding");
    if (acceptEncoding != null) {
      // Prefer brotli over gzip
      if (acceptEncoding.contains("br")) {
        URL brUrl = webResourceProvider.getWebResource(path + ".br");
        if (brUrl != null) {
          try {
            return new PreCompressedResourceUrl(mimeType, safeOpenConnection(brUrl), "br");
          } catch (IOException e) {
            log.error("Failed to serve pre-compressed brotli for {}", sanitizeForLog(path), e);
          }
        }
      }
      if (acceptEncoding.contains("gzip")) {
        URL gzUrl = webResourceProvider.getWebResource(path + ".gz");
        if (gzUrl != null) {
          try {
            return new PreCompressedResourceUrl(mimeType, safeOpenConnection(gzUrl), "gzip");
          } catch (IOException e) {
            log.error("Failed to serve pre-compressed gzip for {}", sanitizeForLog(path), e);
          }
        }
      }
    }

    try {
      return new ResourceUrl(mimeType, safeOpenConnection(url));
    } catch (IOException e) {
      log.error("Failed to serve path {} with resource {}", sanitizeForLog(path),
          sanitizeForLog(url.toString()), e);
      // Return a generic error message instead of the raw exception message
      // to prevent leaking internal paths or implementation details
      return new ErrorResult(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal error");
    }
  }

  protected boolean isForbidden(String path) {
    String lpath = path.toLowerCase();
    return lpath.startsWith("/web-inf/") || lpath.startsWith("/meta-inf/")
        || lpath.contains("/web-inf/") || lpath.contains("/meta-inf/");
  }

  protected String getMimeType(String path) {
    String mime = getServletContext().getMimeType(path);
    return mime != null ? mime : "application/octet-stream";
  }

  /**
   * Normalize a request path by resolving . and .. segments, collapsing double slashes, and
   * decoding percent-encoded traversal sequences. Returns null if the resulting path escapes the
   * root.
   */
  private static String normalizePath(String path) {
    if (path == null) {
      return null;
    }

    // Reject null bytes (could bypass checks in some runtimes)
    if (path.indexOf('\0') >= 0) {
      return null;
    }

    // Collapse double slashes
    while (path.contains("//")) {
      path = path.replace("//", "/");
    }

    // Resolve . and .. segments
    String[] segments = path.split("/");
    ArrayDeque<String> stack = new ArrayDeque<>();
    for (String seg : segments) {
      if (seg.isEmpty() || ".".equals(seg)) {
        continue;
      } else if ("..".equals(seg)) {
        if (stack.isEmpty()) {
          // Traversal above root
          return null;
        }
        stack.removeLast();
      } else {
        stack.addLast(seg);
      }
    }

    StringBuilder normalized = new StringBuilder("/");
    for (Iterator<String> it = stack.iterator(); it.hasNext();) {
      normalized.append(it.next());
      if (it.hasNext()) {
        normalized.append("/");
      }
    }

    return normalized.toString();
  }

  /**
   * Check if a redirect path could be interpreted as an absolute URL or protocol-relative URL,
   * which would allow an open redirect. After leading slash stripping, a path like "//evil.com"
   * becomes "/evil.com" which browsers may interpret as protocol-relative.
   */
  private static boolean isUnsafeRedirectPath(String path) {
    if (path == null || path.isEmpty()) {
      return false;
    }
    // After leading slash stripping, reject paths that still start with /
    // (which become protocol-relative //host) or contain ://
    return path.startsWith("/") || path.contains("://") || path.startsWith("\\");
  }

  /**
   * Allowlist of URL schemes that are safe to open connections to. Only local resource schemes are
   * permitted; network schemes like http, https, ftp, gopher etc. are rejected to prevent SSRF.
   */
  private static final Set<String> SAFE_URL_SCHEMES = Set.of("file", "jar");

  /**
   * Open a connection to the given URL after validating that its scheme is on the allowlist. This
   * prevents Server-Side Request Forgery (SSRF) when the URL originates from user-influenced
   * resource lookups.
   *
   * @throws IOException if the scheme is not allowed or the connection fails
   */
  private static URLConnection safeOpenConnection(URL url) throws IOException {
    String scheme = url.getProtocol();
    if (scheme == null || !SAFE_URL_SCHEMES.contains(scheme.toLowerCase(Locale.ROOT))) {
      throw new IOException("Blocked connection to disallowed URL scheme: " + scheme);
    }
    // For jar: URLs, verify the nested URL also uses a safe scheme
    if ("jar".equalsIgnoreCase(scheme)) {
      String spec = url.toString(); // jar:file:/path!/entry
      int bangIdx = spec.indexOf("!/");
      if (bangIdx > 0) {
        String inner = spec.substring(4, bangIdx); // strip "jar:"
        try {
          URL innerUrl = new URL(inner);
          String innerScheme = innerUrl.getProtocol();
          if (innerScheme == null
              || !SAFE_URL_SCHEMES.contains(innerScheme.toLowerCase(Locale.ROOT))) {
            throw new IOException(
                "Blocked jar entry with disallowed inner URL scheme: " + innerScheme);
          }
        } catch (MalformedURLException e) {
          throw new IOException("Malformed inner URL in jar reference", e);
        }
      }
    }
    return url.openConnection();
  }

  /**
   * Sanitize a string for safe inclusion in log messages.
   */
  private static String sanitizeForLog(String input) {
    if (input == null) {
      return "null";
    }
    return input.replaceAll("[\\r\\n\\t]", "_");
  }

}
