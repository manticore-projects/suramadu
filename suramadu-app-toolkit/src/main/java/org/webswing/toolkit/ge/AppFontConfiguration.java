package org.webswing.toolkit.ge;

import org.webswing.Constants;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

/**
 * Builds the JVM font configuration from fonts the <b>application</b> ships, so a deployment needs
 * neither a {@code fontConfig} section nor font files on the host.
 *
 * <p>
 * The application provides a classpath resource {@value #RESOURCE}:
 *
 * <pre>
 * # Java logical font [.style] = classpath resource (style: plain, bold, italic, bolditalic)
 * dialog         = com/example/fonts/Roboto-Regular.ttf
 * dialog.bold    = com/example/fonts/Roboto-Bold.ttf
 * dialoginput    = com/example/fonts/RobotoMono-Regular.ttf
 * monospaced     = com/example/fonts/RobotoMono-Regular.ttf
 * # optional further physical faces, rendered natively by DirectDraw
 * font.medium    = com/example/fonts/Roboto-Medium.ttf
 * </pre>
 *
 * Missing logical/style combinations resolve exactly like the server-side {@code fontConfig}: the
 * logical font's plain face, else the default monospaced (for
 * {@code dialoginput}/{@code monospaced}) or the default proportional face.
 *
 * <p>
 * <b>Precedence:</b> a server-side {@code fontConfig} always wins; this file is only consulted when
 * the deployment configures no fonts. It must run before the JVM's font manager is created, i.e. in
 * the graphics environment's constructor — Java fixes its logical-font mapping at that point, which
 * is why this is a resource and not a runtime API call.
 */
public final class AppFontConfiguration {

  /** Classpath resource the application provides. */
  public static final String RESOURCE = "META-INF/webfonts.properties";

  private static final List<String> LOGICAL =
      List.of("monospaced", "serif", "sansserif", "dialoginput", "dialog");
  private static final List<String> STYLES = List.of("bolditalic", "italic", "bold", "plain");
  private static final String CHARSET =
      System.getProperty("os.name", "").startsWith("Windows") ? "alphabet" : "latin-1";
  private static final String EXTRA_PREFIX = "font.";

  /**
   * Generates the font configuration from {@value #RESOURCE} on the application classpath and
   * publishes it through {@link Constants#SWING_START_SYS_PROP_FONT_CONFIG}, exactly as if the
   * server had generated it. No-op when the server configured fonts or the application ships none.
   *
   * @return the generated configuration file, or {@code null}
   */
  public static String install() {
    if (System.getProperty(Constants.SWING_START_SYS_PROP_FONT_CONFIG) != null) {
      return null; // deployment fontConfig wins (backward compatibility)
    }
    try {
      URL[] urls = appClassPath();
      if (urls.length == 0) {
        return null;
      }
      try (URLClassLoader cl = new URLClassLoader(urls, null)) {
        URL resource = cl.findResource(RESOURCE);
        if (resource == null) {
          return null;
        }
        Properties props = new Properties();
        try (Reader r =
            new java.io.InputStreamReader(resource.openStream(), StandardCharsets.UTF_8)) {
          props.load(r);
        }
        String file = generate(props, cl);
        if (file != null) {
          System.setProperty(Constants.SWING_START_SYS_PROP_FONT_CONFIG, file);
          System.out.println(
              "AppFontConfiguration: using application fonts from " + resource + " -> " + file);
        }
        return file;
      }
    } catch (Exception ex) {
      System.err.println("AppFontConfiguration: application fonts not installed: " + ex);
      return null;
    }
  }

  private static String generate(Properties props, ClassLoader cl) throws IOException {
    Path dir = Files.createTempDirectory("webfonts");
    dir.toFile().deleteOnExit();

    Map<String, File> extracted = new LinkedHashMap<>(); // resource -> file
    Map<File, String> faceNames = new LinkedHashMap<>(); // file -> full face name
    Map<String, File> logical = new LinkedHashMap<>(); // "dialog bold" -> file

    for (String key : props.stringPropertyNames()) {
      String k = key.trim().toLowerCase(Locale.ROOT);
      String res = props.getProperty(key).trim();
      if (res.startsWith("/")) {
        res = res.substring(1);
      }
      File f = extract(res, cl, dir, extracted, faceNames);
      if (f == null || k.startsWith(EXTRA_PREFIX)) {
        continue;
      }
      String[] parts = k.split("[.\\s]+");
      String style = parts.length > 1 ? parts[1] : "plain";
      if (!LOGICAL.contains(parts[0]) || !STYLES.contains(style)) {
        System.err.println("AppFontConfiguration: ignoring unknown key '" + key + "'");
        continue;
      }
      logical.put(parts[0] + " " + style, f);
    }
    if (logical.isEmpty()) {
      System.err.println("AppFontConfiguration: " + RESOURCE
          + " maps no logical font (dialog, dialoginput, serif, sansserif, monospaced); ignored");
      return null;
    }

    File defaultFont = firstOf(logical, "dialog plain", "sansserif plain", "serif plain");
    File defaultMono = firstOf(logical, "monospaced plain", "dialoginput plain");
    if (defaultFont == null) {
      defaultFont = logical.values().iterator().next();
    }
    if (defaultMono == null) {
      defaultMono = defaultFont;
    }

    // Same format as the server's FontUtils, so WebToolkit.installFonts and DirectDraw read it.
    StringBuilder cfg = new StringBuilder("version=1\n");
    StringBuilder meta = new StringBuilder();
    cfg.append("sequence.allfonts=").append(CHARSET).append('\n');
    for (String lf : LOGICAL) {
      for (String style : STYLES) {
        File f = logical.get(lf + " " + style);
        if (f == null) {
          f = logical.get(lf + " plain");
        }
        if (f == null) {
          f = lf.equals("monospaced") || lf.equals("dialoginput") ? defaultMono : defaultFont;
        }
        cfg.append(lf).append('.').append(style).append('.').append(CHARSET).append('=')
            .append(faceNames.get(f)).append('\n');
        meta.append("#@@").append(lf).append('.').append(style).append('=')
            .append(f.getAbsolutePath()).append('\n');
      }
    }
    for (Map.Entry<File, String> e : faceNames.entrySet()) {
      String path = e.getKey().getCanonicalPath();
      cfg.append("filename.").append(e.getValue().replace(' ', '_')).append('=')
          .append(path.replace("\\", "\\\\")).append('\n');
      meta.append("#@@").append(e.getValue()).append('=').append(e.getKey().getAbsolutePath())
          .append('\n');
    }
    cfg.append('\n').append(meta);

    // the config lives next to the fonts: WebToolkit.installFonts trusts the config's directory
    File config = dir.resolve("fontconfig.properties").toFile();
    Files.writeString(config.toPath(), cfg.toString(), StandardCharsets.UTF_8);
    config.deleteOnExit();
    return config.getAbsolutePath();
  }

  private static File extract(String res, ClassLoader cl, Path dir, Map<String, File> extracted,
      Map<File, String> faceNames) throws IOException {
    File done = extracted.get(res);
    if (done != null) {
      return done;
    }
    byte[] data;
    try (InputStream in = cl.getResourceAsStream(res)) {
      if (in == null) {
        System.err.println("AppFontConfiguration: font resource not found: " + res);
        return null;
      }
      data = in.readAllBytes();
    }
    // NOT Font.createFont: that would create the font manager now, before sun.awt.fontconfig points
    // at the configuration being generated, and Java would fix its logical fonts without it.
    String face = fullName(data);
    if (face == null) {
      System.err.println("AppFontConfiguration: not a TrueType font (no full name): " + res);
      return null;
    }
    String name = res.substring(res.lastIndexOf('/') + 1).replaceAll("[^A-Za-z0-9._-]", "_");
    File f = dir.resolve(extracted.size() + "-" + name).toFile();
    Files.write(f.toPath(), data);
    f.deleteOnExit();
    extracted.put(res, f);
    faceNames.put(f, face);
    return f;
  }

  /**
   * Reads the full font name (name ID 4) from a TrueType/OpenType {@code name} table, with the
   * preference order Java uses for {@code Font.getFontName(Locale.ROOT)}: Windows platform, US
   * English; then any Windows-platform entry; then the Macintosh Roman entry.
   *
   * @return the full name, or {@code null} for anything that is not a single TrueType font
   */
  static String fullName(byte[] d) {
    try {
      java.nio.ByteBuffer b = java.nio.ByteBuffer.wrap(d); // big-endian by default
      int version = b.getInt(0);
      if (version != 0x00010000 && version != 0x4F54544F /* OTTO */
          && version != 0x74727565 /* true */) {
        return null; // also rejects collections (ttcf)
      }
      int numTables = b.getShort(4) & 0xFFFF;
      int nameOff = -1;
      for (int i = 0; i < numTables; i++) {
        int rec = 12 + 16 * i;
        if (b.getInt(rec) == 0x6E616D65 /* name */) {
          nameOff = b.getInt(rec + 8);
          break;
        }
      }
      if (nameOff < 0) {
        return null;
      }
      int count = b.getShort(nameOff + 2) & 0xFFFF;
      int strBase = nameOff + (b.getShort(nameOff + 4) & 0xFFFF);
      String winUs = null;
      String win = null;
      String mac = null;
      for (int i = 0; i < count; i++) {
        int r = nameOff + 6 + 12 * i;
        int platform = b.getShort(r) & 0xFFFF;
        int encoding = b.getShort(r + 2) & 0xFFFF;
        int language = b.getShort(r + 4) & 0xFFFF;
        int nameId = b.getShort(r + 6) & 0xFFFF;
        int len = b.getShort(r + 8) & 0xFFFF;
        int off = strBase + (b.getShort(r + 10) & 0xFFFF);
        if (nameId != 4 || off + len > d.length) {
          continue;
        }
        if (platform == 3 && (encoding == 1 || encoding == 10)) {
          String s = new String(d, off, len, StandardCharsets.UTF_16BE);
          if (language == 0x409 && winUs == null) {
            winUs = s;
          } else if (win == null) {
            win = s;
          }
        } else if (platform == 1 && encoding == 0 && mac == null) {
          mac = new String(d, off, len, StandardCharsets.ISO_8859_1);
        }
      }
      String name = winUs != null ? winUs : win != null ? win : mac;
      return name == null || name.isBlank() ? null : name.trim();
    } catch (IndexOutOfBoundsException ex) {
      return null; // truncated or not a font
    }
  }

  private static File firstOf(Map<String, File> m, String... keys) {
    for (String k : keys) {
      if (m.containsKey(k)) {
        return m.get(k);
      }
    }
    return null;
  }

  /**
   * The application classpath as passed by the launcher ({@code ;} or the platform separator,
   * {@code dir/*} wildcards, relative to the application home).
   */
  private static URL[] appClassPath() throws IOException {
    String cp = System.getProperty(Constants.SWING_START_SYS_PROP_CLASS_PATH);
    if (cp == null || cp.isBlank()) {
      return new URL[0];
    }
    String home = System.getProperty(Constants.SWING_START_SYS_PROP_APP_HOME, ".");
    List<URL> urls = new ArrayList<>();
    for (String entry : cp.split("[;" + File.pathSeparator + "]")) {
      String e = entry.trim();
      if (e.isEmpty()) {
        continue;
      }
      File f = new File(e);
      if (!f.isAbsolute()) {
        f = new File(home, e);
      }
      String fname = f.getName();
      if (fname.equals("*") || fname.equals("*.jar")) {
        File[] jars = f.getParentFile() != null
            ? f.getParentFile().listFiles((d, n) -> n.toLowerCase(Locale.ROOT).endsWith(".jar"))
            : null;
        if (jars != null) {
          for (File j : jars) {
            urls.add(j.toURI().toURL());
          }
        }
      } else if (f.exists()) {
        urls.add(f.toURI().toURL());
      }
    }
    return urls.toArray(new URL[0]);
  }

  private AppFontConfiguration() {}
}
