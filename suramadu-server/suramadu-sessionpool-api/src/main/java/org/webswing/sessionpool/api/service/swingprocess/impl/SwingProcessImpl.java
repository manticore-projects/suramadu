package org.webswing.sessionpool.api.service.swingprocess.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LifeCycle;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.FileSize;
import org.apache.logging.log4j.core.appender.rolling.SizeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.action.Action;
import org.apache.logging.log4j.core.appender.rolling.action.DeleteAction;
import org.apache.logging.log4j.core.appender.rolling.action.IfAccumulatedFileSize;
import org.apache.logging.log4j.core.appender.rolling.action.IfFileName;
import org.apache.logging.log4j.core.appender.rolling.action.PathCondition;
import org.apache.logging.log4j.core.appender.rolling.action.PathSortByModificationTime;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.webswing.Constants;
import org.webswing.server.common.model.SwingConfig;
import org.webswing.server.common.util.ServerUtil;
import org.webswing.sessionpool.api.service.swingprocess.*;
import org.webswing.sessionpool.api.service.swingprocess.impl.SwingProcessServiceImpl.SessionLogAppenderParams;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import org.webswing.util.NamedThreadFactory;

public class SwingProcessImpl implements SwingProcess {

  /**
   * Teardown of a process runs the full server-side close sequence (ProcessExitListener →
   * instanceClosed → SwingInstance.close → browser websocket send). That chain can block on a
   * half-open browser socket. It must therefore NEVER run on {@link #processHandlerThread}, which
   * carries the log pollers and stdin heartbeats of every other instance.
   */
  private static final ExecutorService TERMINATION_EXECUTOR =
      Executors.newCachedThreadPool(NamedThreadFactory.getInstance("Webswing Process Terminator"));

  private final ScheduledExecutorService processHandlerThread;
  private static final long LOG_POLLING_PERIOD = 100L;
  private static final long HEARTBEAT_PERIOD = 1000L;
  private static final long STARTUP_GRACE_PERIOD_MS = 500L;
  private static final long DEFAULT_LOG_SIZE = 10 * 1024 * 1024; // 10 MB

  // Detects java.util.logging header lines like:
  // "Mar 21, 2026 4:35:49 PM com.manticore.common.Application install"
  // These are redundant — the next line has the level + message.
  private static final Pattern JUL_HEADER_PATTERN = Pattern.compile(
      "^(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec) \\d{1,2}, \\d{4} \\d{1,2}:\\d{2}:\\d{2} [AP]M .+");

  private final String instanceId;
  private final SwingProcessConfig config;
  private final SwingConfig swingConfig;
  private final Logger defaultLog;
  private Logger log;
  private final Logger childLog; // dedicated logger for child process output (minimal pattern)
  private final String shortName; // short display name e.g. "ifrsbox/officer"
  private Process process;
  private ScheduledFuture<?> logsProcessor;
  private ScheduledFuture<?> heartbeat;
  private InputStream out;
  private InputStream err;
  private OutputStream in;
  private final StringBuilder bufferOut = new StringBuilder();
  private final StringBuilder bufferErr = new StringBuilder();
  private final byte[] buffer = new byte[4096];
  private boolean hasSessionLog;
  private String sessionLogDestination;

  /** One-shot latch: set when teardown starts and never cleared. */
  private final AtomicBoolean destroying = new AtomicBoolean(false);
  private ScheduledFuture<?> delayedTermination;
  private boolean forceKilled;
  private ProcessExitListener closeListener;
  private ProcessStatusListener statusListener;
  private ApplicationExitListener appExitListener;

  public SwingProcessImpl(String instanceId, SwingProcessConfig config, SwingConfig swingConfig,
      ScheduledExecutorService processHandlerThread) {
    super();
    this.instanceId = instanceId;
    this.config = config;
    this.swingConfig = swingConfig;

    defaultLog =
        (Logger) LogManager.getLogger(SwingProcessImpl.class + "_" + config.getApplicationName());
    log = defaultLog;
    this.processHandlerThread = processHandlerThread;

    // Derive short display name: "ifrsbox_officer_4da8b467f8b_1774..." → "ifrsbox/officer"
    String[] nameParts = config.getName().split("_");
    shortName = nameParts.length >= 2 ? nameParts[0] + "/" + nameParts[1] : config.getName();

    // Create a dedicated child logger with a minimal console pattern
    // Output: "2026-03-21 17:59:56,433 INFO [ifrsbox/officer] The actual message"
    childLog = (Logger) LogManager.getLogger("childapp." + config.getName());
    LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
    Configuration loggerConfig = ctx.getConfiguration();
    loggerConfig.setLoggerAdditive(childLog, false);

    PatternLayout childLayout =
        PatternLayout.newBuilder().withPattern("%d{yyyy-MM-dd HH:mm:ss,SSS} %-5p  %m%n").build();
    ConsoleAppender childAppender = ConsoleAppender.newBuilder()
        .setName("childapp-console-" + config.getName()).setLayout(childLayout).build();
    childAppender.start();
    childLog.addAppender(childAppender);
    childLog.setLevel(Level.DEBUG);
  }

  public void execute() throws Exception {
    if (!isRunning()) {
      ProcessBuilder processBuilder = new ProcessBuilder(buildCommandline());
      if (verifyBaseDir()) {
        processBuilder.directory(new File(config.getBaseDir()));
      }

      List<String> protectedCommand = getProtectedCommand(processBuilder.command());
      log.info("Starting application process [" + config.getName() + "] from ["
          + config.getBaseDir() + "] :" + protectedCommand);

      process = processBuilder.start();
      long pid = process.pid();
      log.info("Application process [{}] started with PID {}", config.getName(), pid);

      // Check if the process survived the startup grace period.
      // If the child JVM crashes immediately (e.g., bad --patch-module JAR,
      // missing class, module error), the process exits before the log poller
      // starts and the error output is silently lost.
      if (!checkProcessStartup()) {
        return; // error already logged by checkProcessStartup
      }

      // Do not rely solely on the log poller to notice that the child died: if the handler pool is
      // busy or blocked, the exit goes unnoticed and the process, its scheduled tasks and its
      // SwingInstance all leak. The reaper thread costs nothing and is never blocked.
      process.onExit().thenRun(this::terminateAsync);

      initSessionLog(process);

      if (hasSessionLog) {
        log.info("Starting application process [" + config.getName() + "] from ["
            + config.getBaseDir() + "] :" + protectedCommand);
        defaultLog.info("Logging into: {}", sessionLogDestination);
        log.info("Logging into: {}", sessionLogDestination);
      }

      if (statusListener != null) {
        statusListener.statusChanged();
      }

      logsProcessor = processHandlerThread.scheduleAtFixedRate(() -> {
        if (process != null) {
          if (out == null || err == null) {
            out = process.getInputStream();
            err = process.getErrorStream();
          }
          try {
            processStream(out, bufferOut, buffer, config.getName(), false);
            processStream(err, bufferErr, buffer, config.getName(), true);
          } catch (Exception e) {
            log.error("Failed to process process logs for application process {}", config.getName(),
                e);
            terminateAsync();
            return;
          }
          if (!SwingProcessImpl.this.isRunning()) {
            terminateAsync();
          }
        }
      }, LOG_POLLING_PERIOD, LOG_POLLING_PERIOD, TimeUnit.MILLISECONDS);

      heartbeat = processHandlerThread.scheduleAtFixedRate(() -> {
        if (process != null) {
          if (in == null) {
            in = process.getOutputStream();
          }
          try {
            sendHeartbeat(in);
          } catch (Exception e) {
            log.error("Failed to send heartbeat ping to application process {}", config.getName(),
                e);
          }
        }
      }, HEARTBEAT_PERIOD, HEARTBEAT_PERIOD, TimeUnit.MILLISECONDS);
    } else {
      throw new IllegalStateException("Process is already running.");
    }
  }

  /**
   * Checks if the child process survived the startup grace period. If the process died immediately,
   * drains all stdout/stderr and logs the exit code so the failure is visible in the server log.
   *
   * @return true if the process is still running, false if it died at startup
   */
  private boolean checkProcessStartup() {
    try {
      boolean exited = process.waitFor(STARTUP_GRACE_PERIOD_MS, TimeUnit.MILLISECONDS);
      if (exited) {
        int exitCode = process.exitValue();
        log.error("Application process [{}] died immediately with exit code {}. Draining output:",
            config.getName(), exitCode);

        // Drain all remaining stdout/stderr so the error is visible
        drainStream(process.getInputStream(), config.getName(), false);
        drainStream(process.getErrorStream(), config.getName(), true);

        if (statusListener != null) {
          statusListener.statusChanged();
        }
        if (closeListener != null) {
          try {
            closeListener.onClose();
          } catch (Exception e) {
            log.error("Failed to call onClose after startup failure", e);
          }
        }
        return false;
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      log.warn("Interrupted while waiting for process startup check");
    }
    return true;
  }

  /**
   * Drains all available data from a stream and logs each line. Used after a process dies to
   * capture any error output.
   */
  private void drainStream(InputStream stream, String name, boolean isError) {
    try {
      byte[] drainBuffer = new byte[4096];
      StringBuilder sb = new StringBuilder();
      while (stream.available() > 0) {
        int read = stream.read(drainBuffer, 0, Math.min(stream.available(), drainBuffer.length));
        if (read > 0) {
          sb.append(new String(drainBuffer, 0, read, StandardCharsets.UTF_8));
        }
      }
      // Blocking read to catch data still in transit
      int read;
      while ((read = stream.read(drainBuffer)) > 0) {
        sb.append(new String(drainBuffer, 0, read, StandardCharsets.UTF_8));
      }
      if (!sb.isEmpty()) {
        for (String line : sb.toString().split("\n")) {
          line = line.replace("\r", "");
          if (!line.isEmpty()) {
            logChildMessage(name, line, isError);
          }
        }
      }
    } catch (IOException e) {
      log.warn("Failed to drain process output stream", e);
    }
  }

  private List<String> getProtectedCommand(List<String> command) {
    List<String> protectedCommand = new ArrayList<>(command.size());

    command.forEach(c -> {
      if (c.startsWith("-Dwebswing.connection.secret=")) {
        protectedCommand.add("-Dwebswing.connection.secret=<hidden>");
      } else {
        protectedCommand.add(c);
      }
    });

    return protectedCommand;
  }

  private boolean verifyBaseDir() {
    if (config.getBaseDir() == null || config.getBaseDir().isEmpty()) {
      return false;
    } else {
      File file = new File(config.getBaseDir());
      if (file.exists() && file.isDirectory() && file.canRead()) {
        return true;
      } else {
        String error = "";
        if (!file.exists()) {
          error = "Path does not exist.";
        } else if (!file.isDirectory()) {
          error = "Path is not a directory";
        } else if (!file.canRead()) {
          error = "Directory is not accessible";
        }
        throw new IllegalArgumentException("Failed to start application process with base dir:'"
            + config.getBaseDir() + "'. " + error);
      }
    }
  }

  /**
   * Shuts the shared termination executor down. Called by SwingProcessServiceImpl#stop().
   */
  public static void shutdownTerminationExecutor() {
    TERMINATION_EXECUTOR.shutdown();
    try {
      TERMINATION_EXECUTOR.awaitTermination(10, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  public void destroy() {
    destroy(0);
  }

  /**
   * Cancels this process' recurring tasks and performs the teardown on the
   * {@link #TERMINATION_EXECUTOR}. Call this instead of {@link #destroy()} from anything running on
   * {@link #processHandlerThread} or on a process reaper thread: the teardown chain reaches the
   * browser websocket and may block for a long time on a half-open connection.
   */
  private void terminateAsync() {
    cancelScheduledTasks();
    try {
      TERMINATION_EXECUTOR.execute(() -> {
        if (!isRunning()) {
          // the child is gone — capture whatever it printed before dying
          drainRemainingOutput();
        }
        destroy(0);
      });
    } catch (RejectedExecutionException e) {
      // executor already shut down (server stopping) — fall back to inline teardown
      destroy(0);
    }
  }

  private void cancelScheduledTasks() {
    ScheduledFuture<?> lp = logsProcessor;
    if (lp != null) {
      lp.cancel(false);
    }
    ScheduledFuture<?> hb = heartbeat;
    if (hb != null) {
      hb.cancel(false);
    }
  }

  private void drainRemainingOutput() {
    if (process == null) {
      return;
    }
    try {
      drainStream(process.getInputStream(), config.getName(), false);
      drainStream(process.getErrorStream(), config.getName(), true);
    } catch (Exception e) {
      log.warn("Failed to drain remaining output of application process {}", config.getName(), e);
    }
  }

  public void destroy(int delayMs) {
    if (delayMs > 0 && delayedTermination == null) {
      log.info("Waiting " + delayMs + "ms for app process " + config.getName() + " to end.");
      delayedTermination =
          processHandlerThread.schedule(this::terminateAsync, delayMs, TimeUnit.MILLISECONDS);
    } else if (destroying.compareAndSet(false, true)) {
      try {
        if (delayedTermination != null) {
          delayedTermination.cancel(false);
        }
        destroyInternal();
      } finally {
        if (logsProcessor != null) {
          logsProcessor.cancel(false);
        }
        if (heartbeat != null) {
          heartbeat.cancel(false);
        }
        log.info("[{}] app process terminated. ", config.getName());
        if (hasSessionLog) {
          defaultLog.info("[{}] app process terminated. ", config.getName());
        }
        if (closeListener != null) {
          try {
            closeListener.onClose();
          } catch (Exception e) {
            log.error("Failed to call onClose", e);
          }
        }
        if (hasSessionLog) {
          log.getAppenders().values().forEach(LifeCycle::stop);
        }
        // Stop the child logger's appenders
        childLog.getAppenders().values().forEach(LifeCycle::stop);
        // NOTE: the flag is deliberately NOT reset. Teardown is one-shot: the log poller, the
        // Process.onExit() reaper and an explicit kill() can all race to terminate the same
        // process, and running the close sequence twice would notify the browser and unregister
        // the instance twice.
      }
    }
  }

  private void destroyInternal() {
    if (isRunning()) {
      log.error("Killing Application process " + config.getName() + ".");
      process.destroy();
      forceKilled = true;
    }
    if (statusListener != null) {
      statusListener.statusChanged();
    }
  }

  public boolean isRunning() {
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

  private String[] buildCommandline() throws Exception {
    List<String> cmd = new ArrayList<>();

    if (config.getJreExecutable() == null || config.getJreExecutable().isEmpty()) {
      throw new IllegalArgumentException("JRE executable cannot be empty. Please specify JRE.");
    }
    translateAndAdd(cmd, config.getJreExecutable(), "jreExecutable");
    if (config.getJvmArgs() != null) {
      translateAndAdd(cmd, config.getJvmArgs(), "jvmArgs");
    }
    if (!config.getProperties().isEmpty()) {
      for (Entry<String, String> entry : config.getProperties().entrySet()) {
        String property = "-D" + entry.getKey();
        String value = entry.getValue();
        if (value != null && !value.isEmpty()) {
          property += "=" + value;
        }
        cmd.add(property);
      }
    }
    if (config.getClassPath() != null) {
      cmd.add("-cp");
      cmd.add(config.getClassPath());
    }
    if (config.getMainClass() != null) {
      cmd.add(config.getMainClass());
    }
    if (config.getArgs() != null) {
      translateAndAdd(cmd, config.getArgs(), "args");
    }
    return cmd.toArray(new String[0]);
  }

  private void translateAndAdd(List<String> cmd, String args, String fieldName) throws Exception {
    try {
      cmd.addAll(Arrays.asList(translateCommandline(args)));
    } catch (Exception e) {
      throw new Exception("Illegal value for '" + fieldName + "' field.", e);
    }
  }

  /**
   * Copy of method from Apache Ant - Commandline class. Crack a command line.
   *
   * @param toProcess the command line to process.
   * @return the command line broken into strings. An empty or null toProcess parameter results in a
   *         zero sized array.
   * @throws Exception if quotes are unbalanced
   */
  public static String[] translateCommandline(String toProcess) throws Exception {
    if (toProcess == null || toProcess.isEmpty()) {
      return new String[0];
    }

    final int normal = 0;
    final int inQuote = 1;
    final int inDoubleQuote = 2;
    int state = normal;
    final StringTokenizer tok = new StringTokenizer(toProcess, "\"' ", true);
    final ArrayList<String> result = new ArrayList<>();
    final StringBuilder current = new StringBuilder();
    boolean lastTokenHasBeenQuoted = false;

    while (tok.hasMoreTokens()) {
      String nextTok = tok.nextToken();
      switch (state) {
        case inQuote:
          if ("'".equals(nextTok)) {
            lastTokenHasBeenQuoted = true;
            state = normal;
          } else {
            current.append(nextTok);
          }
          break;
        case inDoubleQuote:
          if ("\"".equals(nextTok)) {
            lastTokenHasBeenQuoted = true;
            state = normal;
          } else {
            current.append(nextTok);
          }
          break;
        default:
          if ("'".equals(nextTok)) {
            state = inQuote;
          } else if ("\"".equals(nextTok)) {
            state = inDoubleQuote;
          } else if (" ".equals(nextTok)) {
            if (lastTokenHasBeenQuoted || !current.isEmpty()) {
              result.add(current.toString());
              current.setLength(0);
            }
          } else {
            current.append(nextTok);
          }
          lastTokenHasBeenQuoted = false;
          break;
      }
    }
    if (lastTokenHasBeenQuoted || !current.isEmpty()) {
      result.add(current.toString());
    }
    if (state == inQuote || state == inDoubleQuote) {
      throw new Exception("unbalanced quotes in " + toProcess);
    }
    return result.toArray(new String[0]);
  }

  private void processStream(InputStream out, StringBuilder bufferOut, byte[] buffer, String name,
      boolean isError) throws IOException {
    long start = System.currentTimeMillis();
    boolean timeout = false;
    while (out.available() > 0 && !timeout) {
      int available = out.available();
      int read = out.read(buffer, 0, Math.min(available, buffer.length));
      bufferOut.append(new String(buffer, 0, read, StandardCharsets.UTF_8));
      while (bufferOut.indexOf("\n") >= 0) {
        int indexofNewLine = bufferOut.indexOf("\n");
        boolean isCR = indexofNewLine > 0 && bufferOut.charAt(indexofNewLine - 1) == '\r';
        String appMsg =
            bufferOut.subSequence(0, isCR ? indexofNewLine - 1 : indexofNewLine).toString();

        if (!handleSystemMessage(appMsg)) {
          logChildMessage(name, appMsg, isError);
        }

        bufferOut.delete(0, indexofNewLine + 1);
      }
      timeout = System.currentTimeMillis() - start > LOG_POLLING_PERIOD;
    }
  }

  /**
   * Logs a message from the child process, parsing java.util.logging format to extract the actual
   * log level and message. JUL outputs two lines per entry:
   * 
   * <pre>
   *   Line 1: "Mar 21, 2026 4:35:49 PM com.example.MyClass myMethod"  (header — skipped)
   *   Line 2: "INFO: The actual message"                                (level + message)
   * </pre>
   * 
   * JVM warnings like "WARNING: package ... not in java.desktop" are also handled.
   */
  private void logChildMessage(String name, String appMsg, boolean isError) {
    // Skip JUL header lines (timestamp + class.method) — the next line has the content
    if (JUL_HEADER_PATTERN.matcher(appMsg).matches()) {
      return;
    }

    // Skip empty lines and whitespace-only lines
    if (appMsg.isBlank()) {
      return;
    }

    String prefix = "[" + shortName + "] ";

    // Parse JUL level prefixes and map to correct log level
    if (appMsg.startsWith("SEVERE: ")) {
      childLog.error("{}{}", prefix, appMsg.substring(8));
    } else if (appMsg.startsWith("WARNING: ")) {
      childLog.warn("{}{}", prefix, appMsg.substring(9));
    } else if (appMsg.startsWith("INFO: ")) {
      childLog.info("{}{}", prefix, appMsg.substring(6));
    } else if (appMsg.startsWith("CONFIG: ")) {
      childLog.info("{}{}", prefix, appMsg.substring(8));
    } else if (appMsg.startsWith("FINE: ") || appMsg.startsWith("FINER: ")
        || appMsg.startsWith("FINEST: ")) {
      childLog.debug("{}{}", prefix, appMsg.substring(appMsg.indexOf(": ") + 2));
    } else if (isError) {
      childLog.error("{}{}", prefix, appMsg);
    } else {
      childLog.info("{}{}", prefix, appMsg);
    }
  }

  private boolean handleSystemMessage(String appMsg) {
    if (!appMsg.startsWith(Constants.APP_LOGGER_SYSTEM_MSG_PREFIX)) {
      return false;
    }

    String[] sysMsgSplit = appMsg.split(" ", 2);
    if (sysMsgSplit.length != 2) {
      return false;
    }

    String systemMsg = sysMsgSplit[1];

    if (Constants.APP_LOGGER_SYSTEM_MSG_EXIT.equals(systemMsg)) {
      if (appExitListener != null) {
        appExitListener.onExit();
      }
      return true;
    }

    return false;
  }

  private void sendHeartbeat(OutputStream in) throws IOException {
    in.write("ping\r\n".getBytes(StandardCharsets.UTF_8));
    in.flush();
  }

  @Override
  public void reconnect(String serverUrl) {
    if (process == null || in == null) {
      log.error("Cannot send reconnect to server, process or input stream is null!");
      return;
    }

    try {
      in.write(("reconnect " + serverUrl + "\r\n").getBytes(StandardCharsets.UTF_8));
      in.flush();
    } catch (Exception e) {
      log.error("Failed to send reconnect to application process " + config.getName(), e);
    }
  }

  private void initSessionLog(Process process) {
    if (config.getSessionLogAppenderParams() == null || process == null) {
      return;
    }

    String pid = String.valueOf(process.pid());

    log = (Logger) LogManager.getLogger(
        SwingProcessImpl.class + "_" + config.getApplicationName() + "_" + config.getName());

    Configuration loggerConfig = ((LoggerContext) LogManager.getContext(false)).getConfiguration();
    loggerConfig.setLoggerAdditive(log, false);

    Appender logAppender = createSessionLogAppender(config.getSessionLogAppenderParams(), pid);

    if (logAppender instanceof RollingFileAppender appender) {
      sessionLogDestination = new File(appender.getFileName()).getAbsolutePath();
    }

    log.addAppender(logAppender);
    childLog.addAppender(logAppender);
    hasSessionLog = true;
  }

  private Appender createSessionLogAppender(SessionLogAppenderParams params, String pid) {
    String logDir = params.sessionLogDir;

    if (StringUtils.isNotBlank(logDir)) {
      logDir = new File("").toURI().relativize(new File(logDir).toURI()).getPath();
    }

    String appUrlNormalized = ServerUtil.normalizeForFileName(params.pathMapping);
    String instanceIdNormalized = ServerUtil.normalizeForFileName(params.instanceId);
    String logFileName = logDir + "/suramadu-" + instanceIdNormalized + "-" + appUrlNormalized
        + (StringUtils.isNotBlank(pid) ? ("-" + pid) : "") + ".session.log";
    String globPattern = "suramadu-*-" + appUrlNormalized
        + (StringUtils.isNotBlank(pid) ? "-*-" : "") + ".session.log*";

    BuiltConfiguration logConfig = ConfigurationBuilderFactory.newConfigurationBuilder().build();

    long maxLogRollingSize = FileSize.parse(params.singleSize, DEFAULT_LOG_SIZE) / 2;
    SizeBasedTriggeringPolicy sizeBasedPolicy =
        SizeBasedTriggeringPolicy.createPolicy(maxLogRollingSize + " B");
    String maxSize = params.maxSize;

    RollingFileAppender appender = RollingFileAppender.newBuilder()
        .setName(SwingProcessImpl.class.getName()).withFileName(logFileName)
        .withFilePattern(logFileName + ".%i").withAppend(true)
        .setLayout(PatternLayout.newBuilder().withPattern(Constants.SESSION_LOG_PATTERN).build())
        .withPolicy(sizeBasedPolicy)
        .withStrategy(DefaultRolloverStrategy.newBuilder().withMax("1").withConfig(logConfig)
            .withCustomActions(new Action[] {DeleteAction.createDeleteAction(logDir, false, 1,
                false, PathSortByModificationTime.createSorter(true),
                new PathCondition[] {IfFileName.createNameCondition(globPattern, null,
                    IfAccumulatedFileSize.createFileSizeCondition(maxSize))},
                null, logConfig)})
            .build())
        .build();
    appender.start();

    return appender;
  }

  @Override
  public boolean isForceKilled() {
    return forceKilled;
  }

  @Override
  public void setProcessExitListener(ProcessExitListener closeListener) {
    this.closeListener = closeListener;
  }

  @Override
  public void setProcessStatusListener(ProcessStatusListener listener) {
    this.statusListener = listener;
  }

  @Override
  public void setApplicationExitListener(ApplicationExitListener listener) {
    this.appExitListener = listener;
  }

  @Override
  public SwingProcessConfig getConfig() {
    return config;
  }

  @Override
  public SwingConfig getSwingConfig() {
    return swingConfig;
  }

  @Override
  public String getInstanceId() {
    return instanceId;
  }

}
