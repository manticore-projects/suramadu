package org.webswing.directdraw.util;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

/**
 * JVM-wide registry of fonts the application hands to DirectDraw explicitly (see
 * {@code WebswingApi#registerWebFont(byte[])}), so they are rendered natively in the browser
 * without any {@code fontConfig} entry.
 *
 * <p>
 * <b>Identity is the content hash</b>, never a name typed into a config file: the face name used
 * for the lookup is parsed by {@link Font#createFont} from the very bytes the application renders
 * with, in the same JVM, so the requested {@link Font#getFontName(Locale)} and the registered one
 * match by construction. The browser family is derived from the hash.
 *
 * <p>
 * Registered fonts travel through the existing file-keyed DirectDraw pipeline as a pseudo file
 * {@code webfont:<hash>} (see {@link #isKey(String)}).
 */
public final class WebFontRegistry {

  /** Prefix of the pseudo file name under which registry fonts travel through DirectDraw. */
  public static final String KEY_PREFIX = "webfont:";

  /** Hex digits of the SHA-256 used in keys and browser family names (64 bit). */
  private static final int HASH_HEX = 16;

  private record Entry(String key, String faceName, byte[] data) {}

  /** Lower-cased face name (ROOT locale) -> entry. */
  private static final Map<String, Entry> BY_NAME = new ConcurrentHashMap<>();
  /** Key -> entry. */
  private static final Map<String, Entry> BY_KEY = new ConcurrentHashMap<>();
  /** Face names already reported as unresolved, to log each only once. */
  private static final Set<String> REPORTED = ConcurrentHashMap.newKeySet();

  /**
   * Registers a TrueType font for native browser rendering. Idempotent for identical bytes.
   *
   * @param ttf TrueType font file content
   * @return the font's face name, or {@code null} if the bytes are not a readable TrueType font
   */
  public static String register(byte[] ttf) {
    if (ttf == null || ttf.length == 0) {
      return null;
    }
    final Font font;
    try {
      font = Font.createFont(Font.TRUETYPE_FONT, new ByteArrayInputStream(ttf));
    } catch (FontFormatException | IOException ex) {
      System.err.println("WebFontRegistry: rejected font (" + ttf.length + " bytes): " + ex);
      return null;
    }
    String faceName = font.getFontName(Locale.ROOT);
    String key = KEY_PREFIX + sha256Hex(ttf).substring(0, HASH_HEX);
    Entry entry = new Entry(key, faceName, ttf.clone());

    Entry previous = BY_NAME.put(faceName.toLowerCase(Locale.ROOT), entry);
    BY_KEY.put(key, entry);
    if (previous == null) {
      System.out.println("WebFontRegistry: registered '" + faceName + "' as " + key);
    } else if (!previous.key().equals(key)) {
      System.err
          .println("WebFontRegistry: '" + faceName + "' re-registered with different content ("
              + previous.key() + " -> " + key + "); the newer one is served");
    }
    return faceName;
  }

  /**
   * @return the pseudo file key for the font, or {@code null} when it is not registered
   */
  public static String keyFor(Font font) {
    if (font == null || BY_NAME.isEmpty()) {
      return null;
    }
    Entry e = BY_NAME.get(font.getFontName(Locale.ROOT).toLowerCase(Locale.ROOT));
    return e != null ? e.key() : null;
  }

  /** @return true when {@code file} is a registry key rather than a real file path */
  public static boolean isKey(String file) {
    return file != null && file.startsWith(KEY_PREFIX);
  }

  /** @return the font bytes for a registry key, or {@code null} */
  public static byte[] data(String key) {
    Entry e = BY_KEY.get(key);
    return e != null ? e.data() : null;
  }

  /**
   * Browser font-family for a registry key: {@code f} + hash. Short (old engines ignored family
   * names over 31 characters) and free of characters that need quoting.
   */
  public static String familyFor(String key) {
    return "f" + key.substring(KEY_PREFIX.length());
  }

  /**
   * Logs, once per face name, that a requested physical font is neither registered nor configured
   * and therefore falls back to glyph rendering.
   */
  public static void reportUnresolved(Font font) {
    if (font == null) {
      return;
    }
    String name = font.getFontName(Locale.ROOT);
    if (REPORTED.add(name)) {
      Set<String> registered = new TreeSet<>();
      BY_NAME.values().forEach(e -> registered.add(e.faceName()));
      System.err.println("WebFontRegistry: font '" + name + "' (family '"
          + font.getFamily(Locale.ROOT)
          + "') is not registered or configured; using glyph fallback. Registered: " + registered);
    }
  }

  private static String sha256Hex(byte[] data) {
    try {
      return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(data));
    } catch (NoSuchAlgorithmException ex) {
      throw new IllegalStateException("SHA-256 not available", ex); // mandatory in every JDK
    }
  }

  private WebFontRegistry() {}
}
