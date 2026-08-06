package org.webswing.server.services.security.modules.shiro;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class ShiroConfigMonitor {

  private static final Logger log = LoggerFactory.getLogger(ShiroConfigMonitor.class);

  private final Path configPath;
  private final Consumer<String> reloadCallback;
  private final ExecutorService reloadExecutor;
  private long lastReloadTime;
  private static final long DEBOUNCE_DELAY_MS = 1000;

  public ShiroConfigMonitor(String filePath, Consumer<String> reloadCallback) {
    this.configPath = Path.of(filePath).toAbsolutePath();
    this.reloadCallback = reloadCallback;
    // Utilizing a fixed pool size of 4 for background monitoring and reload tasks
    this.reloadExecutor = Executors.newFixedThreadPool(4);
  }

  /**
   * Starts the native file watcher in a background thread.
   */
  public void startWatching() {
    reloadExecutor.submit(() -> {
      try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
        Path dir = configPath.getParent();
        if (dir == null) {
          log.error("Could not determine directory for Shiro config: {}", configPath);
          return;
        }

        // Registering the directory for modification events
        dir.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
        log.info("Monitoring Shiro configuration for hot reloads at: {}", configPath);

        while (!Thread.currentThread().isInterrupted()) {
          WatchKey key = watchService.take(); // Blocks until an event occurs

          for (WatchEvent<?> event : key.pollEvents()) {
            Path changed = (Path) event.context();

            // Check if the modified file is actually our shiro.ini
            if (changed != null && changed.getFileName().equals(configPath.getFileName())) {
              debounceAndReload();
            }
          }

          if (!key.reset()) {
            log.warn("WatchKey no longer valid; stopping Shiro config monitor.");
            break;
          }
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        log.info("Shiro config monitor thread interrupted.");
      } catch (Exception e) {
        log.error("Error in Shiro configuration file watcher.", e);
      }
    });
  }

  private void debounceAndReload() {
    long currentTime = System.currentTimeMillis();
    if (currentTime - lastReloadTime > DEBOUNCE_DELAY_MS) {
      lastReloadTime = currentTime;
      log.info("Change detected in {}; triggering reload callback.", configPath.getFileName());
      try {
        // Execute the reload logic defined in the Security Module
        reloadCallback.accept(configPath.toString());
      } catch (Exception e) {
        log.error("Reload callback failed.", e);
      }
    }
  }

  /**
   * Shuts down the executor service and stops the background thread.
   */
  public void shutdown() {
    log.info("Shutting down Shiro configuration monitor...");
    reloadExecutor.shutdownNow();
    try {
      if (!reloadExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
        log.warn("Shiro monitor executor did not terminate gracefully.");
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }
}
