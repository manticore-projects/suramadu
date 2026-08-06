package org.webswing.server.common.service.url.impl;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.Constants;
import org.webswing.server.common.service.url.WebSocketUrlLoader;
import org.webswing.server.common.util.CommonUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class ScriptWebSocketUrlLoader implements WebSocketUrlLoader {

  private static final Logger log = LoggerFactory.getLogger(ScriptWebSocketUrlLoader.class);

  private static final long PROCESS_TIMEOUT_SECONDS = 5;

  private final String scriptFilePath;
  private final Set<String> webSocketUrls = Collections.synchronizedSet(new HashSet<>());

  private Process process;

  public ScriptWebSocketUrlLoader(String scriptFilePath) {
    this.scriptFilePath = getValidFilePath(scriptFilePath);
  }

  @Override
  public Set<String> reload() {
    loadFromScript();

    synchronized (webSocketUrls) {
      webSocketUrls.clear();
      String urls = System.getProperty(Constants.SERVER_WEBSOCKET_URL, "");
      Splitter.on(',').trimResults().omitEmptyStrings().split(urls).forEach(url -> {
        if (url.endsWith("/")) {
          url = url.substring(0, url.length() - 1);
        }
        webSocketUrls.add(url);
      });
      return webSocketUrls;
    }
  }

  private void loadFromScript() {
    if (scriptFilePath == null) {
      log.warn("Could not reload web socket URLs, no script path defined!");
      return;
    }

    if (isRunning()) {
      log.warn("Could not reload web socket URLs, process still running!");
      return;
    }

    // Validate the script path resolves to a real, executable file
    // to prevent execution of arbitrary commands from misconfiguration
    File scriptFile = new File(scriptFilePath);
    if (!scriptFile.isFile()) {
      log.error("Websocket URL loader script does not exist or is not a regular file: {}",
          sanitizeForLog(scriptFilePath));
      return;
    }
    if (!scriptFile.canExecute()) {
      log.error("Websocket URL loader script is not executable: {}",
          sanitizeForLog(scriptFilePath));
      return;
    }

    String urls = "";

    try {
      ProcessBuilder processBuilder = new ProcessBuilder(scriptFile.getAbsolutePath());
      process = processBuilder.start();

      StringBuilder sb = new StringBuilder();

      new Thread(() -> {
        try (BufferedReader in = new BufferedReader(
            new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
          String line;
          while ((line = in.readLine()) != null) {
            sb.append(line);
          }
        } catch (IOException e) {
          log.error("Error while getting websocket URL loader script output!", e);
        }
      }).start();

      process.waitFor(PROCESS_TIMEOUT_SECONDS, TimeUnit.SECONDS);

      String output = sb.toString();
      log.debug("Websocket URL loader script output [{}]", sanitizeForLog(output));

      if (StringUtils.isNotBlank(output)) {
        List<String> parsed = Splitter.on(',').omitEmptyStrings().trimResults().splitToList(output);
        if (!parsed.isEmpty()) {
          urls = Joiner.on(',').join(parsed);
        }
      }
    } catch (Exception e) {
      log.error("Error while executing websocket url loader script!", e);
    }

    System.setProperty(Constants.SERVER_WEBSOCKET_URL, urls);
  }

  private boolean isRunning() {
    if (process == null) {
      return false;
    }
    try {
      process.exitValue();
      return false;
    } catch (Exception e) {
      return true;
    }
  }

  private String getValidFilePath(String pathOrUri) {
    try {
      return CommonUtil.getValidFile(pathOrUri).getAbsolutePath();
    } catch (FileNotFoundException e) {
      log.warn("Script file not found: {}", sanitizeForLog(pathOrUri));
      return null;
    }
  }

  private static String sanitizeForLog(String input) {
    if (input == null) {
      return "null";
    }
    return input.replaceAll("[\\p{Cc}\\p{Cf}]", "_").substring(0, Math.min(input.length(), 500));
  }
}
