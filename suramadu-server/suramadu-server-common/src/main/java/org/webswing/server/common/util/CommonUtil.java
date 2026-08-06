package org.webswing.server.common.util;

import com.github.weisj.jsvg.SVGDocument;
import com.github.weisj.jsvg.parser.LoaderContext;
import com.github.weisj.jsvg.parser.SVGLoader;
import com.github.weisj.jsvg.view.FloatSize;
import com.github.weisj.jsvg.view.ViewBox;
import main.Main;
import org.apache.commons.lang3.ClassUtils.Interfaces;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.ConfigFileResolver;
import org.webswing.Constants;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.PathMatcher;
import java.security.ProtectionDomain;
import java.util.*;

import com.manticore.tools.ZPNG;

public class CommonUtil {
  public static final int bufferSize = 4 * 1024;
  private static final String DEFAULT = "default";
  private static final Logger log = LoggerFactory.getLogger(CommonUtil.class);
  private static final Map<String, byte[]> iconMap = new HashMap<>();
  private static URLClassLoader swingBootClassLoader;

  /** Maximum icon dimension (pixels). Both SVG and PNG are scaled to fit. */
  private static final int MAX_ICON_SIZE = 112;

  /**
   * Scale image down to fit within maxWidth × maxHeight, preserving aspect ratio. Returns the
   * original image if it already fits.
   */
  private static BufferedImage scaleToFit(BufferedImage src, int maxWidth, int maxHeight) {
    int w = src.getWidth();
    int h = src.getHeight();
    if (w <= maxWidth && h <= maxHeight) {
      return src;
    }

    double scale = Math.min((double) maxWidth / w, (double) maxHeight / h);
    int targetW = Math.max(1, (int) Math.round(w * scale));
    int targetH = Math.max(1, (int) Math.round(h * scale));

    // Progressive downscale: halve repeatedly until within 2× of target,
    // then do one final bicubic pass. This avoids the blurriness of a
    // single large-ratio bicubic step.
    BufferedImage current = src;
    int curW = w;
    int curH = h;

    while (curW > targetW * 2 || curH > targetH * 2) {
      curW = Math.max(targetW, curW / 2);
      curH = Math.max(targetH, curH / 2);
      current = resampleBicubic(current, curW, curH);
    }

    if (curW != targetW || curH != targetH) {
      current = resampleBicubic(current, targetW, targetH);
    }

    return current;
  }

  private static BufferedImage resampleBicubic(BufferedImage src, int w, int h) {
    BufferedImage dst = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = dst.createGraphics();
    try {
      g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
          RenderingHints.VALUE_INTERPOLATION_BICUBIC);
      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
      g.drawImage(src, 0, 0, w, h, null);
    } finally {
      g.dispose();
    }
    return dst;
  }

  public static byte[] loadImage(File iconFile) {
    String icon;
    if (iconFile == null || !iconFile.isFile()) {
      icon = null;
    } else {
      icon = iconFile.getAbsolutePath();
    }
    try {
      if (icon == null) {
        if (iconMap.containsKey(DEFAULT)) {
          return iconMap.get(DEFAULT);
        } else {
          BufferedImage defaultIcon = ImageIO.read(Objects.requireNonNull(
              CommonUtil.class.getClassLoader().getResourceAsStream("images/java.png")));
          defaultIcon = scaleToFit(defaultIcon, MAX_ICON_SIZE, MAX_ICON_SIZE);
          byte[] byteIcon = getPngImage(defaultIcon);
          iconMap.put(DEFAULT, byteIcon);
          return byteIcon;
        }
      } else {
        if (iconMap.containsKey(icon)) {
          return iconMap.get(icon);
        } else {
          BufferedImage image;
          if (icon.toLowerCase().endsWith(".svg")) {
            image = renderSvg(new File(icon), MAX_ICON_SIZE, MAX_ICON_SIZE);
          } else {
            image = ImageIO.read(new File(icon));
            image = scaleToFit(image, MAX_ICON_SIZE, MAX_ICON_SIZE);
          }
          byte[] byteIcon = getPngImage(image);
          iconMap.put(icon, byteIcon);
          return byteIcon;
        }
      }
    } catch (IOException e) {
      log.error("Failed to load image {}", icon, e);
      return null;
    }
  }

  /** Supersample factor: render SVG at Nx resolution, then downscale. */
  private static final int SUPERSAMPLE = 3;

  /**
   * Render an SVG file to a BufferedImage at the given dimensions. Uses 3× supersampling for sharp
   * edges at small icon sizes. If width/height are 0, the SVG's intrinsic size is used.
   */
  public static BufferedImage renderSvg(File svgFile, int width, int height) throws IOException {
    SVGLoader loader = new SVGLoader();
    SVGDocument document;
    try (InputStream in = new FileInputStream(svgFile)) {
      document = loader.load(in, svgFile.toURI(), LoaderContext.createDefault());
    }
    if (document == null) {
      throw new IOException("Failed to parse SVG: " + svgFile);
    }
    return renderSvgDocument(document, width, height);
  }

  /**
   * Render an SVG from a classpath resource to a BufferedImage. Uses 3× supersampling for sharp
   * edges at small icon sizes.
   */
  public static BufferedImage renderSvg(InputStream svgStream, int width, int height)
      throws IOException {
    SVGLoader loader = new SVGLoader();
    SVGDocument document = loader.load(svgStream, null, LoaderContext.createDefault());
    if (document == null) {
      throw new IOException("Failed to parse SVG from stream");
    }
    return renderSvgDocument(document, width, height);
  }

  private static BufferedImage renderSvgDocument(SVGDocument document, int width, int height) {
    FloatSize size = document.size();
    if (width <= 0 || height <= 0) {
      width = Math.max(1, Math.round(size.width));
      height = Math.max(1, Math.round(size.height));
    }

    // Render at Nx resolution for supersampling
    int ssWidth = width * SUPERSAMPLE;
    int ssHeight = height * SUPERSAMPLE;

    BufferedImage hires = new BufferedImage(ssWidth, ssHeight, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = hires.createGraphics();
    try {
      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
      g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
      document.render(null, g, new ViewBox(ssWidth, ssHeight));
    } finally {
      g.dispose();
    }

    // Downscale with high-quality bicubic interpolation
    return scaleToFit(hires, width, height);
  }

  private static byte[] getPngImage(BufferedImage image) {
    if (image == null) {
      return null;
    }
    return ZPNG.encode(image, image.getColorModel().hasAlpha() ? 4 : 3, 2);
  }

  public static File resolveFile(String name, String homeDir, VariableSubstitutor subs) {
    if (name == null) {
      return null;
    }
    name = subs.replace(name);
    File relativeToHomeInConfigProfile =
        new File(Main.getConfigProfileDir(), homeDir + File.separator + name).getAbsoluteFile();
    if (relativeToHomeInConfigProfile.exists()) {
      return relativeToHomeInConfigProfile;
    }
    File relativeToHomeInRoot =
        new File(Main.getRootDir(), homeDir + File.separator + name).getAbsoluteFile();
    if (relativeToHomeInRoot.exists()) {
      return relativeToHomeInRoot;
    }
    File relativeToHome = new File(homeDir + File.separator + name).getAbsoluteFile();
    if (relativeToHome.exists()) {
      return relativeToHome;
    }
    File absolute = new File(name).getAbsoluteFile();
    if (absolute.exists()) {
      return absolute;
    }
    return null;
  }

  public static String getWarFileLocation() {
    String warFile = System.getProperty(Constants.WAR_FILE_LOCATION);
    if (warFile == null) {
      ProtectionDomain domain = Main.class.getProtectionDomain();
      URL location = domain.getCodeSource().getLocation();
      String locationString = location.toExternalForm();
      if (locationString.endsWith("/WEB-INF/classes/")) {
        locationString =
            locationString.substring(0, locationString.length() - "/WEB-INF/classes/".length());
      }
      System.setProperty(Constants.WAR_FILE_LOCATION, locationString);
      return locationString;
    }
    return warFile;
  }

  public static void transferStreams(InputStream is, OutputStream os) throws IOException {
    try {
      byte[] buf = new byte[bufferSize];
      int bytesRead;
      while ((bytesRead = is.read(buf)) != -1) {
        os.write(buf, 0, bytesRead);
      }
    } finally {
      is.close();
      os.close();
    }
  }

  public static File getConfigFile() {
    String configFile = System.getProperty(Constants.CONFIG_FILE_PATH);
    if (configFile == null) {
      String war = CommonUtil.getWarFileLocation();
      String warDir = war.substring(0, war.lastIndexOf("/") + 1);

      // Prefer the current configuration file name, falling back to the legacy one so that
      // deployments created before the project was renamed keep working untouched.
      File resolved = ConfigFileResolver.resolve(new File(URI.create(warDir)),
          Constants.DEFAULT_CONFIG_FILE_NAME);

      configFile = resolved.toURI().toString();

      // NOTE: the arguments here were previously transposed, so the resolved location was never
      // actually cached and this block re-ran on every call.
      System.setProperty(Constants.CONFIG_FILE_PATH, configFile);
    }
    return new File(URI.create(configFile));
  }

  public static String generateClassPathString(Collection<String> classPathEntries) {
    StringBuilder result = new StringBuilder();
    if (classPathEntries != null) {
      for (String cpe : classPathEntries) {
        result.append(cpe).append(";");
      }
      result = new StringBuilder(
          !result.isEmpty() ? result.substring(0, result.length() - 1) : result.toString());
    }
    return result.toString();
  }

  public static boolean isSubPath(String subpath, String path) {
    return path.equals(subpath) || path.startsWith(subpath + "/");
  }

  public static boolean isSubPathIgnoreCase(String subpath, String path) {
    return path.equalsIgnoreCase(subpath)
        || path.toLowerCase().startsWith(subpath.toLowerCase() + "/");
  }

  public static String toPath(String path) {
    String mapping = path == null ? "/" : path;
    mapping = mapping.startsWith("/") ? mapping : ("/" + mapping);
    mapping = mapping.endsWith("/") ? mapping.substring(0, mapping.length() - 1) : mapping;
    return mapping;
  }

  public static <T extends Annotation> T findAnnotation(Method readMethod, Class<T> ann) {
    T annotation = readMethod.getAnnotation(ann);
    if (annotation != null) {
      return annotation;
    } else {
      Set<Method> overrideHierarchy =
          MethodUtils.getOverrideHierarchy(readMethod, Interfaces.INCLUDE);
      for (Method m : overrideHierarchy) {
        annotation = m.getAnnotation(ann);
        if (annotation != null) {
          return annotation;
        }
      }
    }
    return null;
  }

  private static final String WHITELISTS_RESOURCE = "webswing-classloader.properties";

  /**
   * Load glob patterns for a given key from the whitelists properties file.
   */
  private static List<PathMatcher> loadWhitelist(String key) {
    Properties props = new Properties();
    try (InputStream in =
        CommonUtil.class.getClassLoader().getResourceAsStream(WHITELISTS_RESOURCE)) {
      if (in == null) {
        log.warn("{} not found — no filtering for '{}'", WHITELISTS_RESOURCE, key);
        return null;
      }
      props.load(in);
    } catch (IOException e) {
      log.warn("Failed to load {}: {}", WHITELISTS_RESOURCE, e.getMessage());
      return null;
    }

    String value = props.getProperty(key);
    if (value == null || value.isBlank()) {
      return null;
    }

    FileSystem fs = FileSystems.getDefault();
    List<PathMatcher> matchers = new ArrayList<>();
    for (String glob : value.split(",")) {
      glob = glob.trim();
      if (!glob.isEmpty()) {
        matchers.add(fs.getPathMatcher("glob:" + glob));
      }
    }
    return matchers.isEmpty() ? null : matchers;
  }

  /**
   * Build a classloader for the Swing child process, filtered by the 'swing-classpath' whitelist
   * from suramadu-classloader-whitelists.properties.
   *
   * Only JARs matching the whitelist patterns are included — server-only JARs (jetty, shiro, asm,
   * tyrus, etc.) are excluded. Falls back to loading all JARs if no whitelist is configured.
   *
   * Filtering is done inside Main.getFilesFromPath() BEFORE jarEntryAsFile() mangles the names — so
   * glob patterns match against original JAR basenames.
   */
  private static URLClassLoader getSwingBootClassLoader() throws IOException {
    if (swingBootClassLoader == null) {
      URL libFolder;
      if (new File(URI.create(getWarFileLocation())).isFile()) {
        libFolder = new URL("jar:" + getWarFileLocation() + "!/WEB-INF/lib");
      } else if (new File(URI.create(getWarFileLocation())).isDirectory()) {
        libFolder = new URL(getWarFileLocation() + "WEB-INF/lib");
      } else {
        throw new IOException("WAR location not found: " + getWarFileLocation());
      }

      List<PathMatcher> whitelist = loadWhitelist("swing-classpath");
      List<URL> jars = Main.getFilesFromPath(libFolder, whitelist);

      if (whitelist != null) {
        log.info("Swing boot classloader: {} JARs from WEB-INF/lib (filtered by swing-classpath)",
            jars.size());
      } else {
        log.info("Swing boot classloader: {} JARs from WEB-INF/lib (unfiltered)", jars.size());
      }

      swingBootClassLoader = new URLClassLoader(jars.toArray(new URL[0]));
    }
    return swingBootClassLoader;
  }

  public static String getBootClassPathForClass(String className) throws Exception {
    return getBootClassPathForClass(className, true);
  }

  public static String getBootClassPathForClass(String className, boolean withQuotes)
      throws Exception {
    String classfile = className.replace(".", "/") + ".class";
    URL url = getSwingBootClassLoader().getResource(classfile);
    if (url != null) {
      String cp = URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8);
      if (cp.endsWith(classfile)) {
        cp = cp.substring(0, cp.length() - (classfile.length() + 2));
        if (cp.startsWith("file:")) {
          cp = cp.substring(5);
        }
        if (cp.startsWith("/") && cp.contains(":")) {// remove leading slash if windows
          cp = cp.substring(1);
        }
      }
      return withQuotes ? ("\"" + cp + "\"") : cp;

    } else {
      throw new IllegalStateException(
          "Class " + className + " not found in bootclasspath folder of suramadu-server.war. ");
    }
  }

  public static String addParam(String url, String param) {
    if (url.contains("?")) {
      url = url + "&" + param;
    } else {
      url = url + "?" + param;
    }
    return url;
  }

  public static String getValidURI(String pathOrUri) throws FileNotFoundException {
    return getValidFile(pathOrUri).toURI().toString();
  }

  public static File getValidFile(String pathOrUri) throws FileNotFoundException {
    if (pathOrUri != null) {
      try {
        URI uri = URI.create(pathOrUri);
        File urifile = new File(uri);
        if (urifile.exists()) {
          return urifile;
        } else {
          throw new FileNotFoundException("File " + uri.toString() + "not found.");
        }
      } catch (IllegalArgumentException e) {
        File relativeConfigFile = new File(Main.getConfigProfileDir(), pathOrUri);
        File absoluteConfigFile = new File(pathOrUri);
        if (relativeConfigFile.exists()) {
          return relativeConfigFile;
        } else if (absoluteConfigFile.exists()) {
          return absoluteConfigFile;
        }
        throw new FileNotFoundException("File " + relativeConfigFile.getAbsolutePath() + " or "
            + absoluteConfigFile.getAbsolutePath() + " not found.");
      }
    }
    throw new FileNotFoundException("Path not specified.");
  }

  private CommonUtil() {}
}
