package org.webswing.toolkit.extra;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Drop-in replacement for the DefaultShellFolderManager class.
 */
public class DefaultShellFolderManager {
  private final Map<String, Object> properties;
  private final DefaultShellFolder parent;
  private final File file;

  /**
   * Default constructor initializes default properties.
   */
  public DefaultShellFolderManager() {
    this.properties = new HashMap<>();
    this.parent = null;
    this.file = null; // No specific file associated in this case
    initializeDefaults();
  }

  /**
   * Constructor with a parent shell folder and a file.
   *
   * @param parent the parent DefaultShellFolder
   * @param file the file represented by this manager
   */
  public DefaultShellFolderManager(DefaultShellFolder parent, File file) {
    if (file == null) {
      throw new IllegalArgumentException("File cannot be null.");
    }
    this.parent = parent;
    this.file = file;
    this.properties = new HashMap<>();
    initializeDefaults();
  }

  /**
   * Initializes default properties for the manager.
   */
  private void initializeDefaults() {
    properties.put("fileSystemRoots", File.listRoots());
    properties.put("homeDirectory", new File(System.getProperty("user.home")));
    properties.put("tempDirectory", new File(System.getProperty("java.io.tmpdir")));
    if (file != null) {
      properties.put("currentFile", file.getAbsolutePath());
    }
  }

  /**
   * Retrieves the value associated with the given key.
   *
   * @param key the key for the requested property
   * @return the value associated with the key, or null if the key does not exist
   */
  public Object get(String key) {
    return properties.get(key);
  }

  /**
   * Creates a DefaultShellFolder instance for the given file.
   *
   * @param parent the parent DefaultShellFolder (can be null)
   * @param file the file to wrap in a DefaultShellFolder
   * @return a DefaultShellFolder instance
   * @throws IOException if the file's attributes cannot be read
   */
  public DefaultShellFolder createShellFolder(DefaultShellFolder parent, File file)
      throws IOException {
    if (file == null) {
      throw new IllegalArgumentException("File cannot be null.");
    }
    if (!file.exists()) {
      throw new IllegalArgumentException("File does not exist: " + file);
    }
    return new DefaultShellFolder(parent, file);
  }

  /**
   * Creates a DefaultShellFolder instance for the given file.
   *
   * @param file the file to wrap in a DefaultShellFolder
   * @return a DefaultShellFolder instance
   * @throws FileNotFoundException if the file does not exist
   */
  public DefaultShellFolder createShellFolder(File file) throws FileNotFoundException {
    if (file == null || !file.exists()) {
      throw new FileNotFoundException("File not found: " + file);
    }
    try {
      return new DefaultShellFolder(null, file);
    } catch (IOException e) {
      throw new RuntimeException("Error creating DefaultShellFolder for file: " + file, e);
    }
  }

  /**
   * Checks if the given file is a root folder.
   *
   * @param file the file to check
   * @return true if the file is a root folder, false otherwise
   */
  public boolean isRoot(File file) {
    return file != null && file.getParentFile() == null;
  }

  /**
   * Checks if the given file is a file system root.
   *
   * @param file the file to check
   * @return true if the file is a file system root, false otherwise
   */
  public boolean isFileSystemRoot(File file) {
    if (file == null) {
      return false;
    }
    File[] roots = File.listRoots();
    for (File root : roots) {
      if (file.equals(root)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns the root folders of the file system.
   *
   * @return an array of root files
   */
  public File[] getRoots() {
    return File.listRoots();
  }

  /**
   * Checks if a file is hidden.
   *
   * @param file the file to check
   * @return true if the file is hidden, false otherwise
   */
  public boolean isHidden(File file) {
    return file != null && file.isHidden();
  }

  /**
   * Gets the parent folder for this manager, if specified.
   *
   * @return the parent DefaultShellFolder, or null if no parent is associated
   */
  public DefaultShellFolder getParent() {
    return parent;
  }

  /**
   * Gets the file associated with this manager, if specified.
   *
   * @return the associated file, or null if no file is associated
   */
  public File getFile() {
    return file;
  }
}
