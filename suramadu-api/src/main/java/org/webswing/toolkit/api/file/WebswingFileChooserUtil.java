package org.webswing.toolkit.api.file;

import javax.swing.JFileChooser;

public class WebswingFileChooserUtil {

  public static final String ALLOW_DOWNLOAD_OVERRIDE = "webswing.allowDownload";
  public static final String ALLOW_UPLOAD_OVERRIDE = "webswing.allowUpload";
  public static final String ALLOW_DELETE_OVERRIDE = "webswing.allowDelete";
  public static final String CUSTOM_FILE_CHOOSER = "webswing.customFileChooser";

  public static void setDownloadAllowed(JFileChooser chooser, Boolean canDownload) {
    chooser.putClientProperty(ALLOW_DOWNLOAD_OVERRIDE, canDownload);
  }

  public static void setUploadAllowed(JFileChooser chooser, Boolean canUpload) {
    chooser.putClientProperty(ALLOW_UPLOAD_OVERRIDE, canUpload);
  }

  public static void setDeleteAllowed(JFileChooser chooser, Boolean canDelete) {
    chooser.putClientProperty(ALLOW_DELETE_OVERRIDE, canDelete);
  }

  public static void overridePermissions(JFileChooser chooser, Boolean canDownload,
      Boolean canUpload, Boolean canDelete) {
    setDownloadAllowed(chooser, canDownload);
    setUploadAllowed(chooser, canUpload);
    setDeleteAllowed(chooser, canDelete);
  }

  public static void resetPermissions(JFileChooser chooser) {
    setDownloadAllowed(chooser, null);
    setUploadAllowed(chooser, null);
    setDeleteAllowed(chooser, null);
  }

  private WebswingFileChooserUtil() {}
}
