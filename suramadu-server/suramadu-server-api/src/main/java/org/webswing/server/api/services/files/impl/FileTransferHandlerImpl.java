package org.webswing.server.api.services.files.impl;

import com.google.common.primitives.Longs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.Constants;
import org.webswing.server.api.base.AbstractUrlHandler;
import org.webswing.server.api.services.application.AppPathHandler;
import org.webswing.server.api.services.files.FileTransferHandler;
import org.webswing.server.common.datastore.WebswingDataStoreType;
import org.webswing.server.common.model.security.WebswingAction;
import org.webswing.server.common.service.security.impl.WebswingSecuritySubject;
import org.webswing.server.model.exception.WsException;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.regex.Pattern;

public class FileTransferHandlerImpl extends AbstractUrlHandler implements FileTransferHandler {

  private static final Logger log = LoggerFactory.getLogger(FileTransferHandlerImpl.class);
  private static final int DEFAULT_BUFFER_SIZE = 10240; // 10KB.
  private static final String HMAC_ALGORITHM = "HmacSHA256";
  private final AppPathHandler manager;

  /**
   * Instance-scoped HMAC key for file ID signing. Generated once per server lifecycle — all file
   * IDs issued by this instance can be verified. File IDs do not survive a server restart, which is
   * acceptable since the transfer data store is also ephemeral.
   */
  private final byte[] hmacKey;

  public FileTransferHandlerImpl(AppPathHandler parent) {
    super(parent);
    this.manager = parent;
    try {
      KeyGenerator keyGen = KeyGenerator.getInstance(HMAC_ALGORITHM);
      keyGen.init(256);
      SecretKey key = keyGen.generateKey();
      this.hmacKey = key.getEncoded();
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException("HMAC algorithm not available: " + HMAC_ALGORITHM, e);
    }
  }

  @Override
  protected String getPath() {
    return "file";
  }

  @Override
  public boolean serve(HttpServletRequest req, HttpServletResponse res) throws WsException {
    // override security subject resolution using transfer token
    // FIXME find a better solution
    WebswingSecuritySubject.buildAndSetTransferSubjectFrom(req);

    try {
      if ("GET".equals(req.getMethod())) {
        handleDownload(req, res);
        return true;
      } else if ("POST".equals(req.getMethod())) {
        handleUpload(req, res);
        return true;
      } else if ("OPTIONS".equals(req.getMethod())) {
        return true;
      }
    } catch (Exception e) {
      log.error("FileTransfer failed.", e);
      throw new WsException("Failed to process file transfer " + req.getMethod(), e);
    }
    return false;
  }

  private void handleDownload(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException, WsException {
    String fileId = request.getParameter("id");

    // Validate fileId format: only Base64URL characters and underscores
    if (fileId == null || !fileId.matches("[A-Za-z0-9_\\-=]+")) {
      log.warn("Invalid file id parameter received");
      response.sendError(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }

    String userId = getUser() != null ? getUser().getUserId() : "null";

    String fileUserId = null;
    String fileName = "";
    String fileSize = "";
    try {
      String[] fileData = fileId.split("_");
      // Accept 3 segments (legacy: name_userId_size)
      // or 4 segments (HMAC-signed: name_userId_size_hmac)
      if (fileData.length < 3) {
        log.warn("File id has insufficient segments: {}", sanitizeForLog(fileId));
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
        return;
      }
      fileName = decodeHashedFileData(fileData[0]);
      fileUserId = decodeHashedFileData(fileData[1]);
      fileSize = decodeHashedFileData(fileData[2]);

      // Verify HMAC if present (4-segment format from new uploads)
      if (fileData.length >= 4) {
        String payload = fileData[0] + "_" + fileData[1] + "_" + fileData[2];
        String expectedMac = fileData[3];
        if (!verifyHmac(payload, expectedMac)) {
          log.warn("HMAC verification failed for file id [{}]", sanitizeForLog(fileId));
          response.sendError(HttpServletResponse.SC_FORBIDDEN);
          return;
        }
      } else {
        log.debug("Legacy file id without HMAC: {}", sanitizeForLog(fileId));
      }
    } catch (Exception e) {
      log.error("Failed to decode file data [{}]!", sanitizeForLog(fileId), e);
      response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
      return;
    }

    checkPermission(WebswingAction.file_download);

    if (!userId.equals(fileUserId)) {
      log.error("Requested file for user [{}] by user [{}] not allowed!",
          sanitizeForLog(fileUserId), sanitizeForLog(userId));
      response.sendError(HttpServletResponse.SC_FORBIDDEN); // 403.
      return;
    }

    try {
      InputStream is = manager.getDataStore().readData(WebswingDataStoreType.transfer.name(),
          fileId, Long.getLong(Constants.FILE_SERVLET_WAIT_TIMEOUT, 300000));

      if (is != null) {
        response.reset();
        response.setBufferSize(DEFAULT_BUFFER_SIZE);
        response.setContentType("application/octet-stream");
        Long longSize = Longs.tryParse(fileSize);
        if (longSize != null && longSize > 0) {
          // Emit the parsed numeric length, never the raw user-controlled string. (CWE-113)
          response.setContentLengthLong(longSize);
        }
        // Strip CR/LF and other control characters before the value reaches a response header,
        // then percent-encode. This prevents HTTP response splitting via the file name. (CWE-113)
        String safeName = fileName == null ? "file" : fileName.replaceAll("[\\p{Cc}\\r\\n]", "");
        String encodedName =
            URLEncoder.encode(safeName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-Disposition",
            "attachment; filename=\"" + encodedName + "\"; filename*=UTF-8''" + encodedName);
        BufferedInputStream input = null;
        BufferedOutputStream output = null;

        try {
          input = new BufferedInputStream(is, DEFAULT_BUFFER_SIZE);
          output = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);

          byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
          int length;
          while ((length = input.read(buffer)) > 0) {
            output.write(buffer, 0, length);
          }
        } finally {
          close(output);
          close(input);
        }

        return;
      }
    } catch (Exception e) {
      log.error("Error while downloading file id [{}], name [{}]!", sanitizeForLog(fileId),
          sanitizeForLog(fileName), e);
    }

    response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
    return;
  }

  private void handleUpload(HttpServletRequest request, HttpServletResponse resp)
      throws ServletException, IOException, WsException {
    checkPermission(WebswingAction.file_upload);
    try {
      double maxMB = manager.getConfig().getUploadMaxSize();
      long maxsize = (long) (maxMB * 1024 * 1024);
      Part filePart = request.getPart("files[]"); // Retrieves <input type="file" name="file">

      if (filePart == null) {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        resp.getWriter().write("No file part found in request.");
        return;
      }

      String filename = getFilename(filePart);

      // Validate filename
      if (filename == null || filename.isEmpty()) {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        resp.getWriter().write("Invalid or missing filename.");
        return;
      }

      if (maxsize > 0 && filePart.getSize() > maxsize) {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        // Response is JSON with escapeJson()-sanitized user data; Content-Type prevents HTML
        // interpretation
        writeJsonResponse(resp, "{\"error\":\"File '%s' is too large. (Max. file size is %.1fMB)\"}"
            .formatted(escapeJson(filename), maxMB));
      } else {
        String fileId = createHashedUploadFileId(filename, filePart.getSize() + "");
        try (InputStream filecontent = filePart.getInputStream()) {
          manager.getDataStore().storeData(WebswingDataStoreType.transfer.name(), fileId,
              filecontent, true);
        }

        log.info("File {} uploaded (size:{})", sanitizeForLog(filename), filePart.getSize());

        // Response is JSON with escapeJson()-sanitized user data; Content-Type prevents HTML
        // interpretation
        writeJsonResponse(resp, "{\"files\":[{\"name\":\"" + escapeJson(filename) + "\", \"id\":\""
            + escapeJson(fileId) + "\"}]}");
      }
    } catch (Exception e) {
      if (e.getCause() instanceof EOFException) {
        log.warn("File upload canceled by user: {}", sanitizeForLog(e.getMessage()));
      } else {
        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        resp.getWriter().write("Upload finished with error...");
        log.error("Error while uploading file: {}", sanitizeForLog(e.getMessage()), e);
      }
    }
  }

  private String createHashedUploadFileId(String fileName, String fileSize) {
    String hashedName =
        new String(Base64.getUrlEncoder().encode(fileName.getBytes(StandardCharsets.UTF_8)),
            StandardCharsets.UTF_8);
    String hashedUserId = getUser() != null ? getUser().getUserId() : "null";
    String hashedSize =
        new String(Base64.getUrlEncoder().encode(fileSize.getBytes(StandardCharsets.UTF_8)),
            StandardCharsets.UTF_8);
    hashedUserId =
        new String(Base64.getUrlEncoder().encode(hashedUserId.getBytes(StandardCharsets.UTF_8)),
            StandardCharsets.UTF_8);

    String payload = hashedName + "_" + hashedUserId + "_" + hashedSize;
    String mac = computeHmac(payload);
    return payload + "_" + mac;
  }

  private String decodeHashedFileData(String data) {
    return new String(Base64.getUrlDecoder().decode(data.getBytes(StandardCharsets.UTF_8)),
        StandardCharsets.UTF_8);
  }

  /**
   * Compute HMAC-SHA256 over the given payload, returned as URL-safe Base64.
   */
  private String computeHmac(String payload) {
    try {
      Mac mac = Mac.getInstance(HMAC_ALGORITHM);
      mac.init(new SecretKeySpec(hmacKey, HMAC_ALGORITHM));
      byte[] raw = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
      return Base64.getUrlEncoder().withoutPadding().encodeToString(raw);
    } catch (Exception e) {
      throw new RuntimeException("HMAC computation failed", e);
    }
  }

  /**
   * Verify that the given HMAC matches the payload. Uses constant-time comparison to prevent timing
   * attacks.
   */
  private boolean verifyHmac(String payload, String expectedMac) {
    String computedMac = computeHmac(payload);
    return MessageDigest.isEqual(computedMac.getBytes(StandardCharsets.UTF_8),
        expectedMac.getBytes(StandardCharsets.UTF_8));
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

  /**
   * Write a JSON response with proper Content-Type and security headers. Setting Content-Type to
   * application/json and X-Content-Type-Options to nosniff prevents browsers from interpreting the
   * response as HTML, mitigating XSS.
   */
  private static void writeJsonResponse(HttpServletResponse resp, String json) throws IOException { // nosemgrep:
                                                                                                    // no-direct-response-writer
    resp.setContentType("application/json; charset=UTF-8");
    resp.setCharacterEncoding("UTF-8");
    resp.setHeader("X-Content-Type-Options", "nosniff");
    resp.getWriter().write(json);
  }

  /**
   * Escape a string for safe inclusion in a JSON string value. Handles backslash, double-quote, and
   * control characters.
   */
  private static String escapeJson(String input) {
    if (input == null) {
      return "";
    }
    return input.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n")
        .replace("\r", "\\r").replace("\t", "\\t");
  }

  private void close(Closeable resource) {
    if (resource != null) {
      try {
        resource.close();
      } catch (IOException e) {
        log.warn("Failed to close resource", e);
      }
    }
  }

  private String getFilename(Part part) {
    if (part == null) {
      return null;
    }
    String header = part.getHeader("Content-Disposition");
    if (header == null) {
      return null;
    }
    for (String cd : header.split(";")) {
      if (cd.trim().startsWith("filename")) {
        String filename = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
        // Extract base name — handle both forward and backslashes
        int lastSlash = filename.lastIndexOf('/');
        int lastBackslash = filename.lastIndexOf('\\');
        int lastSep = Math.max(lastSlash, lastBackslash);
        String baseName = filename.substring(lastSep + 1);
        // Reject path traversal patterns and empty names
        if (baseName.isEmpty() || baseName.contains("..")) {
          return null;
        }
        return baseName;
      }
    }
    return null;
  }

}
