package org.webswing.dev.server;

import main.Main;
import org.webswing.Constants;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class InteractiveServerLauncher {

  private static final Map<String, Watcher> watcherMap = new HashMap<>();

  public static void main(String[] args) {
    boolean firstRun = true;
    String defaultExampleFolderName = "default-all-apps";

    final String[] newArgs;
    if (args.length > 1 && "-example".equals(args[0])) {
      defaultExampleFolderName = args[1];
      newArgs = Arrays.copyOfRange(args, 2, args.length);
    } else {
      newArgs = Arrays.copyOfRange(args, 0, args.length);
    }

    startRebuildWatch();

    try (Scanner s = new Scanner(System.in)) {
      while (true) {
        // reset settings
        System.clearProperty(Constants.CONFIG_PATH);
        System.clearProperty(Constants.CONFIG_FILE_PATH);
        System.clearProperty(Constants.PROPERTIES_FILE_PATH);
        System.clearProperty(Constants.TEMP_DIR_PATH);
        System.clearProperty(Constants.HTTPS_ONLY);

        String root = System.getProperty(Constants.ROOT_DIR_PATH);
        if (root == null) {
          System.out.println("Please enter path to Webswing examples folder:");
          String line = s.nextLine();
          if (line.trim().length() > 0) {
            root = line.trim();
          } else {
            System.out.println("invalid folder.");
            continue;
          }
        }
        File rootFile = new File(root);
        // FIX (path traversal): resolve to the canonical (symlink-free, ../-free)
        // absolute path before any further use. Without this, a user-supplied
        // value such as "../../etc" would pass the exists/isDirectory checks while
        // pointing outside the intended examples tree.
        try {
          rootFile = rootFile.getCanonicalFile();
          root = rootFile.getAbsolutePath();
        } catch (IOException e) {
          System.out.println("Invalid folder path: " + e.getMessage());
          continue;
        }
        if (rootFile.exists() && rootFile.canRead() && rootFile.isDirectory()) {
          // FIX (path traversal / null-deref): listFiles() returns null on I/O
          // error or permission denial; guard before streaming.
          File[] listed = rootFile.listFiles();
          if (listed == null) {
            System.out.println("Could not list directory: " + root);
            continue;
          }
          File finalRootFile = rootFile;
          List<File> folders = Arrays.stream(listed).filter(file -> {
            try {
              // FIX (path traversal): canonicalize each child so that symlinks
              // pointing outside rootFile are detected and rejected before the
              // config file is accessed.
              File canonicalChild = file.getCanonicalFile();
              if (!canonicalChild.getPath().startsWith(finalRootFile.getPath())) {
                return false;
              }
              File configFile = new File(canonicalChild, Constants.DEFAULT_CONFIG_FILE_NAME);
              return configFile.exists() && configFile.isFile();
            } catch (IOException e) {
              return false;
            }
          }).collect(Collectors.toList());
          if (firstRun && folders.size() > 0) {
            root = folders.get(0).getAbsolutePath();

            for (File folder : folders) {
              // select default example
              if (defaultExampleFolderName.equals(folder.getName())) {
                root = folder.getAbsolutePath();
                break;
              }
            }

            firstRun = false;
          } else {
            System.out.println("Select example: ");
            IntStream.range(0, folders.size()).mapToObj(i -> i + ": " + folders.get(i).getName())
                .forEach(System.out::println);
            System.out.println("Select folder:");
            String line = s.nextLine();
            try {
              root = folders.get(Integer.parseInt(line.trim())).getAbsolutePath();
            } catch (Exception e) {
              System.out.println("invalid selection.\n\n");
              continue;
            }
          }
          System.setProperty(Constants.CONFIG_PATH, root);
        } else {
          System.out.println("Example folder " + root + " does not exist.");
          continue;
        }

        System.out.println("Starting server (press enter to re-start)...");
        Thread serverStarter = new Thread(new Runnable() {
          @Override
          public void run() {
            Main.main(newArgs);
          }
        });
        serverStarter.start();

        s.nextLine();

        try {
          while (Main.getDefaultCL() == null) {
            // wait for server startup
            Thread.sleep(100);
          }
          System.out.println("Stopping server...");
          Class<?> mainClass = Main.getDefaultCL().loadClass("org.webswing.ServerMain");
          Method method = mainClass.getMethod("stopServer");
          method.setAccessible(true);
          method.invoke(null);
          System.out.println("Waiting for server to stop...");
          serverStarter.join(5000);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException
            | InvocationTargetException | InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private static void startRebuildWatch() {
    Path basetmp = Path.of(System.getProperty("user.dir"));
    if (basetmp.resolve("suramadu-api").toFile().isDirectory()) {
      basetmp = basetmp.getParent();
    } else if (basetmp.resolve("../suramadu-api").toFile().isDirectory()) {
      basetmp = basetmp.getParent().getParent();
    } else if (basetmp.resolve("../../suramadu-api").toFile().isDirectory()) {
      basetmp = basetmp.getParent().getParent().getParent();
    }
    final Path base = basetmp;

    Properties watchProps = new Properties();
    try {
      watchProps.load(InteractiveServerLauncher.class.getClassLoader()
          .getResourceAsStream("interactive-watch-changes.properties"));
    } catch (IOException e) {
      System.err.println("Failed to load watch configuration.");
      e.printStackTrace();
      return;
    }

    String resolvedPath = "";
    String[] envPaths = watchProps.getProperty("environment.path", "").split(",");
    for (String path : envPaths) {
      // FIX (path traversal): normalise each resolved path and verify it does
      // not escape the project base directory. A tampered or malicious
      // properties file could otherwise use "../.." sequences to reach
      // arbitrary filesystem locations.
      Path candidate = base.resolve(path).normalize();
      if (!candidate.startsWith(base.normalize())) {
        System.err.println("Skipping environment.path entry outside base directory: " + path);
        continue;
      }
      resolvedPath += candidate.toAbsolutePath().toString() + File.pathSeparator;
    }

    String[] watchers = watchProps.getProperty("enabled.watchers", "").split(",");
    for (String watcher : watchers) {
      try {
        String[] watchPaths = watchProps.getProperty(watcher + ".watchPaths", "").split(",");
        List<Path> watchPathsResolved = new ArrayList<>();
        for (String wp : watchPaths) {
          // FIX (path traversal): same guard for each watch path.
          Path candidate = base.resolve(wp).normalize();
          if (!candidate.startsWith(base.normalize())) {
            System.err.println(
                "Skipping watchPath outside base directory for watcher '" + watcher + "': " + wp);
            continue;
          }
          watchPathsResolved.add(candidate);
        }

        String workDir = watchProps.getProperty(watcher + ".workDir", "");
        // FIX (path traversal): same guard for the working directory.
        Path workDirResolved = base.resolve(workDir).normalize();
        if (!workDirResolved.startsWith(base.normalize())) {
          System.err.println(
              "Skipping watcher '" + watcher + "': workDir escapes base directory: " + workDir);
          continue;
        }

        String command = watchProps.getProperty(watcher + ".command", "");
        String[] commandResolved = translateCommandline(command);

        String[] triggers = watchProps.getProperty(watcher + ".triggers", "").split(",");

        watcherMap.put(watcher, new Watcher(watchPathsResolved, watcher, workDirResolved,
            resolvedPath, triggers, commandResolved));
      } catch (Exception e) {
        System.err.println("Failed to start watcher for " + watcher);
        e.printStackTrace();
      }
    }
  }

  public static Watcher getWatcher(String watcher) {
    return watcherMap.get(watcher);
  }

  /**
   * Copy of method from Apache Ant - Commandline class. Crack a command line.
   *
   * @param toProcess the command line to process.
   * @return the command line broken into strings. An empty or null toProcess parameter results in a
   *         zero sized array.
   * @throws Exception
   */
  public static String[] translateCommandline(String toProcess) throws Exception {
    if (toProcess == null || toProcess.length() == 0) {
      // no command? no string
      return new String[0];
    }
    // parse with a simple finite state machine

    final int normal = 0;
    final int inQuote = 1;
    final int inDoubleQuote = 2;
    int state = normal;
    final StringTokenizer tok = new StringTokenizer(toProcess, "\"\' ", true);
    final ArrayList<String> result = new ArrayList<String>();
    final StringBuilder current = new StringBuilder();
    boolean lastTokenHasBeenQuoted = false;

    while (tok.hasMoreTokens()) {
      String nextTok = tok.nextToken();
      switch (state) {
        case inQuote:
          if ("\'".equals(nextTok)) {
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
          if ("\'".equals(nextTok)) {
            state = inQuote;
          } else if ("\"".equals(nextTok)) {
            state = inDoubleQuote;
          } else if (" ".equals(nextTok)) {
            if (lastTokenHasBeenQuoted || current.length() != 0) {
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
    if (lastTokenHasBeenQuoted || current.length() != 0) {
      result.add(current.toString());
    }
    if (state == inQuote || state == inDoubleQuote) {
      throw new Exception("unbalanced quotes in " + toProcess);
    }
    return result.toArray(new String[result.size()]);
  }

}
