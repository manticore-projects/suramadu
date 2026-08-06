package main;

import org.webswing.Constants;

import java.awt.AWTError;
import java.awt.Toolkit;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Main {

  private static ClassLoader defaultCL;

  private static final String WHITELISTS_RESOURCE = "webswing-classloader.properties";

  /**
   * Glob whitelists per logical classloader role, loaded from
   * suramadu-classloader-whitelists.properties on the classpath.
   *
   * ALL JARs live in WEB-INF/lib/ (single copy). Each module classloader reads from lib/ but only
   * picks JARs matching its whitelist. Non-matching JARs are available via parent delegation to the
   * shared classloader (which loads everything from lib/ unfiltered).
   */
  private static final Map<String, List<PathMatcher>> WHITELISTS = loadWhitelists();

  private static Map<String, List<PathMatcher>> loadWhitelists() {
    Properties props = new Properties();
    try (InputStream in = Main.class.getClassLoader().getResourceAsStream(WHITELISTS_RESOURCE)) {
      if (in == null) {
        System.out.println("[Main] WARNING: " + WHITELISTS_RESOURCE
            + " not found — no JAR filtering will be applied");
        return Collections.emptyMap();
      }
      props.load(in);
    } catch (IOException e) {
      System.err.println("[Main] WARNING: Failed to load " + WHITELISTS_RESOURCE + ": " + e);
      return Collections.emptyMap();
    }

    FileSystem fs = FileSystems.getDefault();
    Map<String, List<PathMatcher>> result = new HashMap<>();

    for (String name : props.stringPropertyNames()) {
      List<PathMatcher> matchers = new ArrayList<>();
      for (String glob : props.getProperty(name).split(",")) {
        glob = glob.trim();
        if (!glob.isEmpty()) {
          matchers.add(fs.getPathMatcher("glob:" + glob));
        }
      }
      if (!matchers.isEmpty()) {
        result.put(name.trim(), matchers);
      }
    }

    System.out.println("[Main] Loaded whitelists: " + result.keySet());
    return result;
  }

  public static void main(String[] mainArgs) {
    boolean client = System.getProperty(Constants.SWING_START_SYS_PROP_INSTANCE_ID) != null;
    boolean sessionpool = false;
    boolean admin = false;

    String[] args = mainArgs;
    if (args != null) {
      List<String> argsList = new ArrayList<>();
      for (String arg : args) {
        if ("-sessionpool".equals(arg)) {
          sessionpool = true;
        } else if ("-admin".equals(arg)) {
          admin = true;
        } else {
          argsList.add(arg);
        }
      }
      args = argsList.toArray(new String[0]);
    }

    try {
      ProtectionDomain domain = Main.class.getProtectionDomain();
      URL location = domain.getCodeSource().getLocation();
      String warLocation = location.toExternalForm();

      System.setProperty(Constants.CREATE_NEW_TEMP, getCreateNewTemp(args));
      System.setProperty(Constants.CLEAN_TEMP, getBoolParam(args, "-tc", true));
      System.setProperty(Constants.WAR_FILE_LOCATION, warLocation);

      // ── Initialize temp dir BEFORE any JAR extraction ─────────────
      if (sessionpool) {
        initTempDirPath(args, "tmp/sp");
      } else if (!client) {
        initTempDirPath(args, "tmp/server");
      }

      // ── Shared classloader: ALL JARs from WEB-INF/lib/ ───────────
      List<URL> sharedUrls = new ArrayList<>();
      URL libResource = Main.class.getClassLoader().getResource("WEB-INF/lib");
      if (libResource != null) {
        populateClasspathFromDir("WEB-INF/lib", sharedUrls, null);
      }

      ClassLoader baseParent = ClassLoader.getSystemClassLoader();
      ClassLoader sharedLoader;
      if (!sharedUrls.isEmpty()) {
        sharedLoader = new URLClassLoader(sharedUrls.toArray(URL[]::new), baseParent);
        System.out
            .println("[Main] Shared classloader: " + sharedUrls.size() + " JARs from WEB-INF/lib");
      } else {
        sharedLoader = baseParent;
        System.out
            .println("[Main] No WEB-INF/lib found, " + "falling back to flat classloader layout");
      }

      // ── Module classloaders: filtered view of WEB-INF/lib/ ───────
      List<URL> urls = new ArrayList<>();
      if (client) {
        // swing-lib role: pick only swing-lib whitelisted JARs from lib/
        populateClasspathFromDir("WEB-INF/lib", urls, WHITELISTS.get("swing-lib"));

        AccessController.doPrivileged(new PrivilegedAction<Void>() {
          public Void run() {
            Class<?> cls = null;
            String nm = System.getProperty("awt.toolkit");
            try {
              cls = Class.forName(nm);
            } catch (ClassNotFoundException e) {
              ClassLoader cl = ClassLoader.getSystemClassLoader();
              if (cl != null) {
                try {
                  cls = cl.loadClass(nm);
                } catch (final ClassNotFoundException ignored) {
                  throw new AWTError("Toolkit not found: " + nm);
                }
              }
            }
            try {
              if (cls != null) {
                Toolkit toolkit = (Toolkit) cls.getConstructor().newInstance();
                System.out.println("Loaded Webtoolkit " + toolkit.getClass().getName());

                Field field = Toolkit.class.getDeclaredField("toolkit");
                field.setAccessible(true);
                field.set(null, toolkit);
                System.out.println("Forced field accessible.");

                MethodHandles.Lookup lookup =
                    MethodHandles.privateLookupIn(Toolkit.class, MethodHandles.lookup());
                VarHandle varHandle =
                    lookup.findStaticVarHandle(Toolkit.class, "toolkit", Toolkit.class);
                varHandle.set(toolkit);
                System.out.println("Toolkit.toolkit field has been set to: " + toolkit);
              }
            } catch (final ReflectiveOperationException ignored) {
              throw new AWTError("Could not create Toolkit: " + nm);
            }
            return null;
          }
        });
        initializeExtLibServices(urls, sharedLoader);

        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        defaultToolkit.getClass().getMethod("init").invoke(defaultToolkit);
        System.out.println(
            "Toolkit.toolkit field has been set to: " + defaultToolkit.getClass().getName());

        retainOnlyLauncherUrl(urls);
      } else if (sessionpool) {
        // sessionpool: no filtering — load everything
        populateClasspathFromDir("WEB-INF/lib", urls, null);
      } else {
        // server / admin: pick only server-lib whitelisted JARs from lib/
        populateClasspathFromDir("WEB-INF/lib", urls, WHITELISTS.get("server-lib"));
      }

      defaultCL = new URLClassLoader(urls.toArray(URL[]::new), sharedLoader);
      Thread.currentThread().setContextClassLoader(defaultCL);

      Class<?> mainClass;
      if (client) {
        mainClass = defaultCL.loadClass("org.webswing.SwingMain");
      } else if (sessionpool) {
        mainClass = defaultCL.loadClass("org.webswing.cluster.sessionpool.SessionPoolMain");
      } else {
        try {
          mainClass = defaultCL.loadClass("org.webswing.ServerMain");
        } catch (ClassNotFoundException e) {
          // Use try-with-resources to prevent resource leaks
          try (InputStream readme =
              Main.class.getClassLoader().getResourceAsStream("WEB-INF/server-lib/README.txt")) {
            if (readme != null) {
              try (Scanner s = new Scanner(readme, StandardCharsets.UTF_8).useDelimiter("\\A")) {
                String result = s.hasNext() ? s.next() : "";
                throw new Exception(result, e);
              }
            } else {
              throw new Exception("Unexpected error.", e);
            }
          }
        }
      }

      Method method = mainClass.getMethod("main", args.getClass());
      method.invoke(null, new Object[] {args});
    } catch (Exception e) {
      System.err.println("Uncaught exception.");
      e.printStackTrace();
      System.exit(1);
    }
  }

  public static String getCreateNewTemp(String[] args) {
    return getBoolParam(args, "-d", false);
  }

  public static String getBoolParam(String[] args, String param, Boolean def) {
    for (int i = 0; i < args.length; i++) {
      if (args[i].equals(param) && i + 1 < args.length) {
        return args[i + 1];
      }
    }
    return def.toString();
  }

  private static void retainOnlyLauncherUrl(List<URL> urls) {
    urls.removeIf(url -> !url.getFile().contains("suramadu-app-launcher"));
  }

  private static void initializeExtLibServices(List<URL> urls, ClassLoader sharedLoader)
      throws Exception {
    ClassLoader platformCL = null;
    try {
      platformCL =
          (ClassLoader) ClassLoader.class.getDeclaredMethod("getPlatformClassLoader").invoke(null);
    } catch (Exception e) {
      // ignore — pre-Java 9
    }

    ClassLoader extLibClassLoader = new URLClassLoader(urls.toArray(URL[]::new), sharedLoader);
    Class<?> classLoaderUtilClass =
        extLibClassLoader.loadClass("org.webswing.util.ClassLoaderUtil");
    Method initializeServicesMethod = classLoaderUtilClass.getMethod("initializeServices");
    Thread.currentThread().setContextClassLoader(extLibClassLoader);
    initializeServicesMethod.invoke(null);
  }

  // ── Classpath population with optional glob whitelist ─────────────

  private static void populateClasspathFromDir(String dir, List<URL> urls) throws IOException {
    populateClasspathFromDir(dir, urls, null);
  }

  private static void populateClasspathFromDir(String dir, List<URL> urls,
      List<PathMatcher> whitelist) throws IOException {
    for (URL f : getFilesFromPath(Main.class.getClassLoader().getResource(dir), whitelist)) {
      urls.add(f);
    }
  }

  public static List<URL> getFilesFromPath(URL r) throws IOException {
    return getFilesFromPath(r, null);
  }

  public static List<URL> getFilesFromPath(URL r, List<PathMatcher> whitelist) throws IOException {
    List<URL> urls = new ArrayList<>();
    String tempDirPath = getTempDir().getAbsolutePath();

    if (r.getPath().contains("!")) {
      String[] splitPath = r.getPath().split("!/");
      String jar = splitPath[0];
      String path = splitPath[1];
      JarFile jarFile = new JarFile(new File(URI.create(jar)));
      Enumeration<JarEntry> jarEntries = jarFile.entries();
      while (jarEntries.hasMoreElements()) {
        JarEntry jarEntry = jarEntries.nextElement();

        // Reject jar entries with path traversal sequences (Zip Slip protection)
        if (jarEntry.getName().contains("..")) {
          System.err.println(
              "[Main] WARNING: Skipping jar entry with path traversal: " + jarEntry.getName());
          continue;
        }

        if (!jarEntry.isDirectory() && jarEntry.getName().endsWith(".jar")
            && jarEntry.getName().startsWith(path)) {

          if (whitelist != null && !matchesWhitelist(jarEntry.getName(), whitelist)) {
            continue;
          }

          urls.add(jarEntryAsFile(jarFile, jarEntry, tempDirPath).toURI().toURL());
        }
      }
    } else {
      File dir;
      try {
        dir = new File(r.toURI());
      } catch (URISyntaxException e) {
        dir = new File(r.getPath());
      }
      if (dir.isDirectory()) {
        for (File f : Objects.requireNonNull(dir.listFiles())) {
          if (f.isFile() && f.getName().endsWith(".jar")) {

            if (whitelist != null && !matchesWhitelist(f.getName(), whitelist)) {
              continue;
            }

            urls.add(f.toURI().toURL());
          }
        }
      }
    }
    return urls;
  }

  private static boolean matchesWhitelist(String nameOrPath, List<PathMatcher> whitelist) {
    int slash = nameOrPath.lastIndexOf('/');
    String basename = slash >= 0 ? nameOrPath.substring(slash + 1) : nameOrPath;
    Path p = Path.of(basename);
    return whitelist.stream().anyMatch(m -> m.matches(p));
  }

  private static File jarEntryAsFile(JarFile jarFile, JarEntry jarEntry, String tempDirPath)
      throws IOException {
    InputStream input = null;
    OutputStream output = null;
    try {
      String name = jarEntry.getName();
      if (name.contains("/")) {
        name = name.replace('/', '_');
        int i = name.lastIndexOf(".");
        String extension = i > -1 ? name.substring(i) : "";
        name = name.substring(0, name.length() - extension.length()) + extension;
      }
      File file = new File(tempDirPath + File.separator + name).getAbsoluteFile();

      // Zip Slip protection: verify the resolved canonical path stays
      // within the intended temp directory
      String canonicalTempDir = new File(tempDirPath).getCanonicalPath();
      String canonicalFile = file.getCanonicalPath();
      if (!canonicalFile.startsWith(canonicalTempDir + File.separator)) {
        throw new IOException("Jar entry would extract outside temp directory: " + name);
      }

      if (!file.exists()) {
        file.createNewFile();
        input = jarFile.getInputStream(jarEntry);
        output = new FileOutputStream(file);
        int readCount;
        byte[] buffer = new byte[4096];
        while ((readCount = input.read(buffer)) != -1) {
          output.write(buffer, 0, readCount);
        }
      }
      return file;
    } finally {
      close(input);
      close(output);
    }
  }

  private static void close(Closeable closeable) {
    if (closeable != null) {
      try {
        closeable.close();
      } catch (IOException e) {
        System.err.println("[Main] Failed to close resource: " + e.getMessage());
      }
    }
  }

  public static File getTempDir() {
    if (System.getProperty(Constants.TEMP_DIR_PATH) == null) {
      File baseDir = new File(
          System.getProperty(Constants.TEMP_DIR_PATH_BASE, System.getProperty("java.io.tmpdir")))
          .getAbsoluteFile();
      if (!baseDir.exists()) {
        baseDir.mkdirs();
      }
      SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd-HH-mm-ss");
      String baseName;
      if (Boolean.parseBoolean(System.getProperty(Constants.CREATE_NEW_TEMP, ""))) {
        baseName = sdf.format(new Date()) + "-";
        for (int counter = 0; counter < 10; counter++) {
          File tempDir = new File(baseDir, baseName + counter);
          if (tempDir.mkdir()) {
            // Restrict permissions: owner-only read/write/execute
            restrictPermissions(tempDir);
            System.setProperty(Constants.TEMP_DIR_PATH, tempDir.toURI().toString());
            return tempDir;
          }
        }
      } else {
        baseName = "release";
        File tempDir = new File(baseDir, baseName).getAbsoluteFile();
        if (!tempDir.exists()) {
          tempDir.mkdir();
          restrictPermissions(tempDir);
        } else if (Boolean.parseBoolean(System.getProperty(Constants.CLEAN_TEMP, "true"))) {
          File[] children = tempDir.listFiles();
          if (children != null) {
            for (File f : children) {
              if (!delete(f)) {
                throw new IllegalStateException(
                    "Not possible to clean the temp folder. Make sure no other "
                        + "instance of webswing is running or use '-d true' option "
                        + "to create a new temp folder.");
              }
            }
          }
        }
        System.setProperty(Constants.TEMP_DIR_PATH, tempDir.toURI().toString());
        return tempDir;
      }
      throw new IllegalStateException("Failed to create directory within " + 10
          + " attempts (tried " + baseName + " to " + baseName + (100 - 1) + ')');
    } else {
      return new File(URI.create(System.getProperty(Constants.TEMP_DIR_PATH)));
    }
  }

  /**
   * Restrict directory permissions to owner-only access where supported. Prevents other users on
   * shared systems from reading extracted JARs or injecting malicious files into the temp
   * directory.
   */
  private static void restrictPermissions(File dir) {
    dir.setReadable(false, false);
    dir.setWritable(false, false);
    dir.setExecutable(false, false);
    dir.setReadable(true, true);
    dir.setWritable(true, true);
    dir.setExecutable(true, true);
  }

  public static File getRootDir() {
    if (System.getProperty(Constants.ROOT_DIR_PATH) == null) {
      File defaultRoot = new File(System.getProperty("user.dir"));
      System.setProperty(Constants.ROOT_DIR_URI, defaultRoot.toURI().toString());
      System.setProperty(Constants.ROOT_DIR_PATH, defaultRoot.getAbsolutePath());
      return defaultRoot;
    } else {
      String pathOrUri = System.getProperty(Constants.ROOT_DIR_PATH);
      try {
        File file = new File(URI.create(pathOrUri));
        if (file.exists()) {
          System.setProperty(Constants.ROOT_DIR_URI, file.toURI().toString());
          System.setProperty(Constants.ROOT_DIR_PATH, file.getAbsolutePath());
          return file;
        } else {
          throw new IllegalArgumentException("File " + file.getAbsolutePath() + " not found.");
        }
      } catch (IllegalArgumentException e) {
        File absoluteConfigFile = new File(pathOrUri).getAbsoluteFile();
        if (absoluteConfigFile.exists()) {
          System.setProperty(Constants.ROOT_DIR_URI, absoluteConfigFile.toURI().toString());
          System.setProperty(Constants.ROOT_DIR_PATH, absoluteConfigFile.getAbsolutePath());
          return absoluteConfigFile;
        } else {
          throw new IllegalArgumentException(
              "File " + absoluteConfigFile.getAbsolutePath() + " not found.");
        }
      }
    }
  }

  public static File getConfigProfileDir() {
    if (System.getProperty(Constants.CONFIG_PATH) == null) {
      System.setProperty(Constants.CONFIG_PATH, getRootDir().getAbsolutePath());
      return getRootDir();
    } else {
      try {
        String configProfile = System.getProperty(Constants.CONFIG_PATH);

        // Reject path traversal in config profile
        if (configProfile.contains("..")) {
          throw new IOException("Config profile path contains traversal sequence");
        }

        File relative = new File(getRootDir(), configProfile);
        // Validate canonical path stays within root directory
        String canonicalRoot = getRootDir().getCanonicalPath();
        String canonicalRelative = relative.getCanonicalPath();
        if (relative.exists() && canonicalRelative.startsWith(canonicalRoot + File.separator)) {
          return relative;
        } else if (relative.exists()) {
          // Relative path resolved outside root — treat as untrusted
          throw new IOException("Config profile resolved outside root directory");
        } else {
          File absolute = new File(configProfile);
          if (absolute.exists()) {
            return absolute;
          } else {
            throw new IOException(
                "Failed to resolve configuration profile path for '" + configProfile + "'");
          }
        }
      } catch (IOException e) {
        System.out.println("Ignoring Config profile setting due to following exception:");
        e.printStackTrace();
        System.setProperty(Constants.CONFIG_PATH, getRootDir().getAbsolutePath());
        return getRootDir();
      }
    }
  }

  private static boolean delete(File f) {
    if (f.isDirectory()) {
      File[] children = f.listFiles();
      if (children != null) {
        for (File fx : children) {
          if (!delete(fx)) {
            return false;
          }
        }
      }
    }
    return f.delete();
  }

  private static void initTempDirPath(String[] args, String defaulttmp) {
    if (args != null) {
      for (int i = 0; i < args.length - 1; i++) {
        if ("-t".equals(args[i]) || "-temp".equals(args[i])) {
          String tempPath = args[i + 1];
          // Reject traversal sequences in user-supplied temp path
          if (tempPath.contains("..")) {
            System.err
                .println("[Main] WARNING: Temp path contains traversal sequence, using default");
            break;
          }
          System.setProperty(Constants.TEMP_DIR_PATH_BASE, tempPath);
          return;
        }
      }
    }
    System.setProperty(Constants.TEMP_DIR_PATH_BASE, defaulttmp);
  }

  public static ClassLoader getDefaultCL() {
    return defaultCL;
  }
}
