package org.webswing.dispatch;

import org.webswing.Constants;
import org.webswing.toolkit.api.lifecycle.ShutdownReason;
import org.webswing.toolkit.util.Services;
import org.webswing.toolkit.util.Util;
import org.webswing.util.AppLogger;
import org.webswing.util.NamedThreadFactory;

import javax.swing.SwingUtilities;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class WebSessionWatchdog implements SessionWatchdog {

  private static final long LAST_HEARTBEAT_BEFORE_SHUTDOWN = Long.getLong(
      Constants.LAST_HEARTBEAT_BEFORE_SHUTDOWN, Constants.LAST_HEARTBEAT_BEFORE_SHUTDOWN_DEFAULT);
  private static final int SWING_SESSION_TIMEOUT_SEC_IF_FILECHOOSER_ACTIVE =
      Integer.getInteger(Constants.SWING_SESSION_TIMEOUT_SEC_IF_FILECHOOSER_ACTIVE,
          Constants.SWING_SESSION_TIMEOUT_SEC_IF_FILECHOOSER_ACTIVE_DEFAULT);
  private static final int EDT_TIMEOUT_SECONDS =
      Integer.getInteger(Constants.EDT_TIMEOUT_SECONDS, Constants.EDT_TIMEOUT_SECONDS_DEFAULT);

  private final ScheduledExecutorService exitScheduler = Executors.newSingleThreadScheduledExecutor(
      NamedThreadFactory.getInstance("Webswing Shutdown scheduler"));

  private final Object delayedShutdownScheduleLock = new Object();
  private ScheduledFuture<?> delayedShutdownFuture;
  private boolean schedulingShutdown;
  private final AtomicBoolean watchHeartbeat = new AtomicBoolean(true);
  private final AtomicBoolean terminated = new AtomicBoolean(false);
  private final AtomicLong lastHeartbeat = new AtomicLong(System.currentTimeMillis());

  public WebSessionWatchdog() {
    // if shutdown is scheduled, we ignore the timeout setting
    // IE halts js execution when filechooser is open and no heartbeat messages are sent causing
    // timeout(see https://bitbucket.org/webswing/webswing-home/issues/18)
    /*+10000 is to compensate for 10s js heartbeat interval*/
    // countdown message for the last 60 seconds
    // if still alive 10 seconds after server should have killed this process
    // only call once
    // if value is not reset after 10 seconds - EDT is stuck
    Runnable watchdog = new Runnable() {
      boolean exitDumpCreated;
      AtomicInteger pingEventDispatchThread = new AtomicInteger(0);

      @Override
      public void run() {
        try {
          int timeoutSec = Integer.getInteger(Constants.SWING_SESSION_TIMEOUT_SEC,
              Constants.SWING_SESSION_TIMEOUT_SEC_DEFAULT);
          synchronized (delayedShutdownScheduleLock) {
            if (delayedShutdownFuture != null && !delayedShutdownFuture.isDone()) {
              if (delayedShutdownFuture.getDelay(TimeUnit.SECONDS) % 60 == 0) {
                AppLogger.warn("Application shutdown expected in "
                    + delayedShutdownFuture.getDelay(TimeUnit.SECONDS) + " seconds");
              }
              timeoutSec = -1; // if shutdown is scheduled, we ignore the timeout setting
            }
          }

          if (watchHeartbeat.get()) {
            if (System.currentTimeMillis() - lastHeartbeat.get() > LAST_HEARTBEAT_BEFORE_SHUTDOWN) {
              watchHeartbeat.set(false);
              scheduleShutdown(ShutdownReason.ProcessKilled, () -> {
                AppLogger.warn("Exiting application due to session pool shutdown.");
              });
            }
          } else {
            if (terminated.get()) {
              AppLogger.warn(
                  "Application has not been forcefully terminated by session pool. Session pool is probably down. Exiting Manually.");
              System.exit(1);
            }
          }

          // IE halts js execution when filechooser is open and no heartbeat messages are sent
          // causing timeout(see https://bitbucket.org/webswing/webswing-home/issues/18)
          if (timeoutSec >= 0 && Util.getWebToolkit().getPaintDispatcher() != null
              && Util.getWebToolkit().getPaintDispatcher().getFileChooserDialog() != null
              && Util.getWebToolkit().getApi().getPrimaryUser() != null) {
            int minTimeoutSecIfFileChooserActive = SWING_SESSION_TIMEOUT_SEC_IF_FILECHOOSER_ACTIVE;
            timeoutSec = Math.max(timeoutSec, minTimeoutSecIfFileChooserActive);
          }

          boolean timeoutIfInactive =
              Boolean.getBoolean(Constants.SWING_SESSION_TIMEOUT_IF_INACTIVE) && timeoutSec > 0;
          if (timeoutSec >= 0) {
            long lastTstp =
                Util.getWebToolkit().getEventDispatcher().getLastEventTimestamp(timeoutIfInactive)
                    + 10000;/*+10000 is to compensate for 10s js heartbeat interval*/
            long diff = System.currentTimeMillis() - lastTstp;
            int timeoutMs = timeoutSec * 1000;
            timeoutMs = Math.max(1000, timeoutMs);
            if (diff / 1000 > 10) {
              String msg = timeoutIfInactive ? "User" : "Session";
              if ((diff / 1000) % 60 == 0) {
                AppLogger.warn(msg + " inactive for " + diff / 1000 + " seconds."
                    + (terminated.get() ? "[waiting for application to stop]" : ""));
              }
              if (!terminated.get() && (timeoutMs - diff < 60000)) { // countdown message for the
                                                                     // last 60 seconds
                Util.getWebToolkit().getPaintDispatcher().notifySessionTimeoutWarning();
              }
              Integer waitForExit = Integer.getInteger(Constants.SWING_START_SYS_PROP_WAIT_FOR_EXIT,
                  Constants.SWING_START_SYS_PROP_WAIT_FOR_EXIT_DEFAULT);
              if (terminated.get() && !exitDumpCreated && diff > (timeoutMs + waitForExit - 5000)) {
                Util.getWebToolkit().getPaintDispatcher()
                    .notifyThreadDumpCreated("Before-Kill Thread Dump");
                AppLogger.error("Application did not exit gracefully within " + waitForExit / 1000
                    + " seconds. Application will be forcefully terminated. Thread dump has been generated.");
                exitDumpCreated = true;
              }

              if (terminated.get() && diff > (timeoutMs + waitForExit + 10000)) {// if still alive
                                                                                 // 10 seconds after
                                                                                 // server should
                                                                                 // have killed this
                                                                                 // process
                AppLogger.warn(
                    "Application has not been forcefully terminated by server. Exiting Manually.");
                System.exit(1);
              }
            }

            if (diff > timeoutMs) {
              if (!terminated.get()) {// only call once
                scheduleShutdown(ShutdownReason.Inactivity, () -> {
                  AppLogger.warn(
                      "Exiting application due to inactivity for " + diff / 1000 + " seconds.");
                  Util.getWebToolkit().getPaintDispatcher().notifySessionTimedOut();
                });
              }
            }
          }

          if (!terminated.get()) {
            Util.getWebToolkit().getPaintDispatcher()
                .notifyNewSessionStats(pingEventDispatchThread.get());
            if (pingEventDispatchThread.getAndIncrement() == EDT_TIMEOUT_SECONDS) {// if value is
                                                                                   // not reset
                                                                                   // after 10
                                                                                   // seconds - EDT
                                                                                   // is stuck
              AppLogger.warn("Application is not responding for " + EDT_TIMEOUT_SECONDS
                  + " seconds. Thread dump generated . ");
              Util.getWebToolkit().getPaintDispatcher()
                  .notifyThreadDumpCreated("Application not responding");
            } else {
              SwingUtilities.invokeLater(() -> pingEventDispatchThread.set(0));
            }
          }

        } catch (Throwable e) {
          AppLogger.error("Exception in webswing shutdown scheduler", e);
        }
      }
    };
    exitScheduler.scheduleWithFixedDelay(watchdog, 1, 1, TimeUnit.SECONDS);

    Thread heartbeatThread = new Thread(() -> {
      try (BufferedReader reader =
          new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8))) {
        String line = null;
        while ((line = reader.readLine()) != null) {
          if ("ping".equalsIgnoreCase(line)) {
            lastHeartbeat.set(System.currentTimeMillis());
          }
          if (line.startsWith("reconnect")) {
            try {
              String reconnectUrl = line.split(" ")[1];
              if (reconnectUrl == null || reconnectUrl.trim().length() == 0) {
                throw new IllegalArgumentException("Empty reconnect URL received!");
              }
              Thread reconnectThread = new Thread(() -> {
                // this must be started on a different thread, otherwise heartbeat is not received
                // and app is killed
                Services.getConnectionService().reconnect(reconnectUrl);
              });
              reconnectThread.setDaemon(true);
              reconnectThread.start();
            } catch (Exception e) {
              AppLogger.error("Could not parse reconnect command [" + line + "]!", e);
            }
          }
        }
      } catch (IOException e) {
        AppLogger.error("Error reading from standard input!", e);
      }
    });
    heartbeatThread.setDaemon(true);
    heartbeatThread.start();
  }

  @Override
  public void notifyExit() {
    // notify session pool that the instance is exiting
    // session pool should immediately send a broadcast to all servers to remove the instance from
    // instance registry
    System.out.println(
        Constants.APP_LOGGER_SYSTEM_MSG_PREFIX + " " + Constants.APP_LOGGER_SYSTEM_MSG_EXIT);
  }

  @Override
  public void scheduleShutdown(ShutdownReason reason) {
    scheduleShutdown(reason, () -> {
    });
  }

  @Override
  public void requestThreadDump() {
    exitScheduler.execute(() -> Util.getWebToolkit().getPaintDispatcher()
        .notifyThreadDumpCreated("User requested thread dump"));
  }

  @Override
  public boolean isTerminated() {
    return terminated.get();
  }

  private void scheduleShutdown(ShutdownReason reason, Runnable shutdownlogic) {
    synchronized (delayedShutdownScheduleLock) {
      try {
        schedulingShutdown = true;
        if (delayedShutdownFuture == null || delayedShutdownFuture.isDone()) {
          int delaySeconds = Util.getWebToolkit().executeOnBeforeShutdownListeners(reason);
          delayedShutdownFuture = exitScheduler.schedule(() -> {
            shutdownlogic.run();
            terminated.set(true);
            Util.getWebToolkit().exitSwing(0);
          }, delaySeconds, TimeUnit.SECONDS);
          AppLogger.info("(" + reason + ") Application Shutdown scheduled. (delayed by "
              + delayedShutdownFuture.getDelay(TimeUnit.SECONDS) + " seconds).");
        } else {
          AppLogger.info("(" + reason
              + ") Application Shutdown request ignored. Delayed shutdown already scheduled in "
              + delayedShutdownFuture.getDelay(TimeUnit.SECONDS) + " seconds.");
        }
      } finally {
        schedulingShutdown = false;
      }
    }
  }

  @Override
  public void resetInactivityTimers() {
    synchronized (delayedShutdownScheduleLock) {
      if (schedulingShutdown) {
        // if resetInactivityTimers called from onBeforeShutdown listener, we need to defer
        // execution, because the shutdown timer is not yet created.
        exitScheduler.schedule(this::resetInactivityTimers, 1, TimeUnit.MILLISECONDS);
      } else {
        Util.getWebToolkit().getEventDispatcher().resetLastEventTimestamp();
        Util.getWebToolkit().getEventDispatcher().resetLastMessageTimestamp();
        if (delayedShutdownFuture != null && !delayedShutdownFuture.isDone()) {
          AppLogger.warn("Cancelling Delayed Application Shutdown expected in "
              + delayedShutdownFuture.getDelay(TimeUnit.SECONDS) + " seconds.");
          delayedShutdownFuture.cancel(false);
        }
      }
    }
  }


}
