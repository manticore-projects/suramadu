package org.webswing.dev.server;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.*;

public class Watcher implements Runnable {

  private final List<Path> dir;
  private final String name;
  private final ExecutorService thread;
  private final WatchService watcher;
  private final Map<WatchKey, Path> keys;
  private final Path workingdir;
  private final String path;
  private final String[] triggers;
  private final String[] buildCommand;
  private final AtomicBoolean buildInProgress = new AtomicBoolean();

  Watcher(List<Path> dir, String name, Path workingdir, String path, String[] triggers,
      String... buildCommand) throws IOException {
    this.dir = dir;
    this.name = name;
    this.workingdir = workingdir;
    this.path = path;
    this.triggers = triggers;
    this.buildCommand = buildCommand;
    this.watcher = FileSystems.getDefault().newWatchService();
    this.keys = new HashMap<WatchKey, Path>();
    registerDir(dir);
    thread = Executors.newSingleThreadExecutor(r -> new Thread(r, name));
    thread.submit(this);
  }

  @SuppressWarnings("unchecked")
  static <T> WatchEvent<T> cast(WatchEvent<?> event) {
    return (WatchEvent<T>) event;
  }

  private void registerDir(List<Path> dirs) throws IOException {
    for (Path dir : dirs) {
      System.out.println("[" + name + "] Watching for changes in:");
      Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
            throws IOException {
          WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
          keys.put(key, dir);
          System.out.println("[" + name + "]" + dir);
          return FileVisitResult.CONTINUE;
        }
      });
    }
  }

  public void run() {
    while (true) {

      try {
        // wait for key to be signalled
        WatchKey key;
        try {
          key = watcher.take();
        } catch (InterruptedException x) {
          return;
        }
        Path dir = keys.get(key);
        if (dir != null) {
          for (WatchEvent<?> event : key.pollEvents()) {
            // Context for directory entry event is the file name of entry
            WatchEvent<Path> ev = cast(event);
            Path name = ev.context();
            Path child = dir.resolve(name);

            // print out event
            System.out.format("[" + Watcher.this.name + "] CHANGE in %s: re-builing %s\n", child,
                Watcher.this.name);

            // if directory is created, and watching recursively, then
            // register it and its sub-directories
            if (event.kind() == ENTRY_CREATE) {
              try {
                if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
                  registerDir(Arrays.asList(child));
                }
              } catch (IOException x) {
                // ignore to keep sample readbale
              }
            }
          }
          if (!buildInProgress.get()) {
            rebuild();
          } else {
            System.out.println(
                "[" + Watcher.this.name + "] Changes ignored, build already in progress...");
          }

          // reset keys and remove from set if directory no longer accessible
          for (WatchKey k : new ArrayList<>(keys.keySet())) {
            boolean valid = k.reset();
            if (!valid) {
              keys.remove(k);

              // all directories are inaccessible
              if (keys.isEmpty()) {
                break;
              }
            } else {
              k.pollEvents();
            }
          }
        } else {
          System.err.println("[" + Watcher.this.name + "] Unknown Watcher Key");
        }
      } catch (Exception e) {
        System.err.println("[" + Watcher.this.name + "] Unexpected exception");
        e.printStackTrace();
      }
    }
  }

  protected synchronized int rebuild() {
    try {
      buildInProgress.getAndSet(true);
      System.out
          .println("[" + Watcher.this.name + "] Starting rebuild :" + Arrays.asList(buildCommand));
      int result = 1;
      // Start build process
      Process p = null;
      try {
        ProcessBuilder pb =
            new ProcessBuilder(buildCommand).inheritIO().directory(workingdir.toFile());
        Map<String, String> env = pb.environment();
        env.put("PATH", this.path);
        p = pb.start();
        p.waitFor(100, TimeUnit.SECONDS);
        result = p.exitValue();
        if (p.exitValue() == 0) {
          for (String trigger : triggers) {
            Watcher watcher = InteractiveServerLauncher.getWatcher(trigger.trim());
            if (watcher != null) {
              int exitValue = watcher.rebuild();
              if (exitValue != 0) {
                result = exitValue;
                System.out.println(
                    "[" + Watcher.this.name + "] Failed to build dependent module: " + trigger);
                break;
              }
            } else {
              System.out.println(trigger.trim().isEmpty() ? ""
                  : ("[" + Watcher.this.name + "] Trigger failed. Module '" + trigger
                      + "' not found"));
            }
          }
        }
      } catch (IOException | InterruptedException | IllegalThreadStateException e) {
        System.err.println("[" + Watcher.this.name + "] Failed to rebuild:");
        e.printStackTrace();
      } finally {
        if (p != null && p.isAlive()) {
          p.destroyForcibly();
          System.err.println("[" + Watcher.this.name + "] Build process killed.");
        }
      }
      System.out
          .println("[" + Watcher.this.name + "] Done re-building " + name + " result:" + result);
      return result;
    } catch (Exception e) {
      System.err.println("[" + Watcher.this.name + "] Unexpected exception:");
      e.printStackTrace();
      return 1;
    } finally {
      buildInProgress.getAndSet(false);
    }
  }
}
