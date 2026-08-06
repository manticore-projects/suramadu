package org.webswing.toolkit.extra;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;

// Drop-in replacement for DefaultShellFolder
public class DefaultShellFolder extends File {
  private final BasicFileAttributes attributes;
  private final DefaultShellFolder parent;

  /**
   * Constructor that accepts a parent folder and a file.
   *
   * @param parent the parent DefaultShellFolder
   * @param file the file represented by this folder
   * @throws IOException if the file's attributes cannot be read
   */
  public DefaultShellFolder(DefaultShellFolder parent, File file) throws IOException {
    super(file.getAbsolutePath());
    this.parent = parent;
    this.attributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
  }

  /**
   * Returns the parent folder of this folder.
   *
   * @return the parent DefaultShellFolder, or null if this is a root
   */
  public DefaultShellFolder getParentFolder() {
    return parent;
  }

  /**
   * Checks if this folder is a root folder.
   *
   * @return true if this folder has no parent, false otherwise
   */
  public boolean isRoot() {
    return parent == null;
  }

  @Override
  public boolean isDirectory() {
    return attributes.isDirectory();
  }

  @Override
  public boolean isFile() {
    return attributes.isRegularFile();
  }

  /**
   * Gets the creation time of this folder.
   *
   * @return the creation time in milliseconds
   */
  public long getCreationTime() {
    return attributes.creationTime().toMillis();
  }

  /**
   * Gets the last modified time of this folder.
   *
   * @return the last modified time in milliseconds
   */
  public long getLastModifiedTime() {
    return attributes.lastModifiedTime().toMillis();
  }

  /**
   * Gets the size of this folder.
   *
   * @return the size in bytes
   */
  public long getSize() {
    return attributes.size();
  }

  /**
   * Gets the display name of this folder.
   *
   * @return the system display name
   */
  public String getDisplayName() {
    FileSystemView fsv = FileSystemView.getFileSystemView();
    return fsv.getSystemDisplayName(this);
  }

  /**
   * Checks if this folder is hidden.
   *
   * @return true if this folder is hidden, false otherwise
   */
  @Override
  public boolean isHidden() {
    return super.isHidden();
  }

  /**
   * Retrieves the absolute path of this folder.
   *
   * @return the absolute path as a String
   */
  @Override
  public String toString() {
    return getAbsolutePath();
  }

  // Add more methods to ensure full compatibility if needed
}
