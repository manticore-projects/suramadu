package org.webswing;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Resolves configuration files that were renamed when the project was renamed.
 *
 * <p>
 * Current names carry the {@code suramadu} prefix — {@code suramadu.config},
 * {@code suramadu-server.config}, {@code suramadu-admin.properties} and so on. Deployments created
 * before the rename carry the same names under the previous prefix. Those continue to work and are
 * picked up without comment, so upgrading requires no action from an administrator.
 *
 * <p>
 * Resolution order for a directory:
 *
 * <ol>
 * <li>the current name — used if present</li>
 * <li>the legacy counterpart — used if present, logged only at {@link Level#FINE}</li>
 * <li>neither present — the <em>current</em> name is returned, so that "not found" messages and any
 * file the caller goes on to create name the file administrators should adopt</li>
 * </ol>
 *
 * <p>
 * The same applies to an explicitly supplied path, in both directions: a launcher script or
 * container image may keep passing either name across an upgrade.
 */
public final class ConfigFileResolver {

  private static final Logger LOGGER = Logger.getLogger(ConfigFileResolver.class.getName());

  /** Prefix of the current file names. */
  public static final String CURRENT_PREFIX = "suramadu";

  /** Prefix of the file names used before the project was renamed. */
  public static final String LEGACY_PREFIX = String.join("", "web", "swing");

  private ConfigFileResolver() {
    // utility class
  }

  /**
   * Resolves a configuration file inside a directory.
   *
   * @param directory the directory to search, must not be {@code null}
   * @param currentName the current file name, for example {@code suramadu.config}
   * @return the file to use; may not exist, in which case it carries the current name
   */
  public static File resolve(File directory, String currentName) {
    if (directory == null || currentName == null) {
      throw new IllegalArgumentException("directory and currentName are required");
    }

    File current = new File(directory, currentName);
    if (current.isFile()) {
      return current;
    }

    String legacyName = legacyNameOf(currentName);
    if (legacyName != null) {
      File legacy = new File(directory, legacyName);
      if (legacy.isFile()) {
        LOGGER.log(Level.FINE, "Using legacy configuration file {0}; the current name is {1}.",
            new Object[] {legacyName, currentName});
        return legacy;
      }
    }

    return current;
  }

  /**
   * Resolves an explicitly supplied path, falling back to its counterpart under the other prefix.
   *
   * <p>
   * If the path names an existing file it is returned unchanged. Otherwise the same directory is
   * checked for the counterpart under the other prefix.
   *
   * @param path the supplied path, may be {@code null}
   * @return the file to use, or {@code null} when {@code path} was {@code null}
   */
  public static File resolveExplicit(String path) {
    if (path == null) {
      return null;
    }

    File supplied = new File(path);
    if (supplied.isFile()) {
      return supplied;
    }

    String counterpartName = counterpartNameOf(supplied.getName());
    if (counterpartName != null) {
      File counterpart = new File(supplied.getParentFile(), counterpartName);
      if (counterpart.isFile()) {
        LOGGER.log(Level.FINE, "{0} not found; using {1} instead.",
            new Object[] {supplied.getName(), counterpartName});
        return counterpart;
      }
    }

    return supplied;
  }

  /**
   * Returns the legacy counterpart of a current file name.
   *
   * @param currentName a current file name, for example {@code suramadu-admin.properties}
   * @return the legacy name, or {@code null} when the name does not carry the current prefix
   */
  public static String legacyNameOf(String currentName) {
    return swapPrefix(currentName, CURRENT_PREFIX, LEGACY_PREFIX);
  }

  /**
   * Returns the counterpart of a file name under whichever prefix it does not currently use.
   *
   * @param name any file name
   * @return the counterpart name, or {@code null} when the name carries neither prefix
   */
  public static String counterpartNameOf(String name) {
    String swapped = swapPrefix(name, CURRENT_PREFIX, LEGACY_PREFIX);
    return swapped != null ? swapped : swapPrefix(name, LEGACY_PREFIX, CURRENT_PREFIX);
  }

  /**
   * Returns the counterpart of a path or URI, swapping the prefix of its final segment only.
   *
   * <p>
   * Accepts a bare file name, a relative or absolute path, or a {@code file:} URI. The directory
   * portion is preserved unchanged.
   *
   * @param pathOrUri any path, URI or bare file name
   * @return the counterpart, or {@code null} when the final segment carries neither prefix
   */
  public static String counterpartPathOf(String pathOrUri) {
    if (pathOrUri == null) {
      return null;
    }
    int cut = Math.max(pathOrUri.lastIndexOf('/'), pathOrUri.lastIndexOf(File.separatorChar));

    String directory = cut >= 0 ? pathOrUri.substring(0, cut + 1) : "";
    String name = cut >= 0 ? pathOrUri.substring(cut + 1) : pathOrUri;

    String counterpart = counterpartNameOf(name);
    return counterpart != null ? directory + counterpart : null;
  }

  /**
   * Replaces a leading prefix when it is followed by a separator, so that {@code suramadu.config}
   * and {@code suramadu-server.config} both match but an unrelated name does not.
   */
  private static String swapPrefix(String name, String from, String to) {
    if (name == null || !name.startsWith(from) || name.length() <= from.length()) {
      return null;
    }
    char separator = name.charAt(from.length());
    if (separator != '.' && separator != '-') {
      return null;
    }
    return to + name.substring(from.length());
  }
}
