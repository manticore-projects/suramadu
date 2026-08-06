package org.webswing.model.appframe.out;

import org.webswing.model.MsgOut;
import org.webswing.toolkit.api.file.WebswingFileChooserUtil;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.Serial;

public class FileDialogEventMsgOut implements MsgOut {

  @Serial
  private static final long serialVersionUID = -7470385173647106699L;

  public enum FileDialogEventType {
    AutoUpload, AutoSave, Open, Close;
  }

  private FileDialogEventType eventType;
  private boolean allowDownload;
  private boolean allowUpload;
  private boolean allowDelete;
  private boolean customDialog;
  private String selection;
  private String filter = "";
  private boolean isMultiSelection;

  public FileDialogEventMsgOut() {}

  public FileDialogEventMsgOut(JFileChooser chooser, boolean allowDownload, boolean allowUpload,
      boolean allowDelete) {
    this.allowDownload = allowDownload;
    this.allowUpload = allowUpload;
    this.allowDelete = allowDelete;

    if (chooser != null) {
      Boolean downloadOverride =
          (Boolean) chooser.getClientProperty(WebswingFileChooserUtil.ALLOW_DOWNLOAD_OVERRIDE);
      Boolean uploadOverride =
          (Boolean) chooser.getClientProperty(WebswingFileChooserUtil.ALLOW_UPLOAD_OVERRIDE);
      Boolean deleteOverride =
          (Boolean) chooser.getClientProperty(WebswingFileChooserUtil.ALLOW_DELETE_OVERRIDE);
      this.allowDownload = downloadOverride != null ? downloadOverride : allowDownload;
      this.allowUpload = uploadOverride != null ? uploadOverride : allowUpload;
      this.allowDelete = deleteOverride != null ? deleteOverride : allowDelete;
      Boolean customDialog =
          (Boolean) chooser.getClientProperty(WebswingFileChooserUtil.CUSTOM_FILE_CHOOSER);
      this.customDialog = customDialog != null ? customDialog : false;
    }
  }

  public void addFilter(FileFilter filterArr[]) {
    for (FileFilter ff : filterArr) {
      if (ff instanceof FileNameExtensionFilter fe) {
        for (String s : fe.getExtensions()) {
          filter += "." + s + ", ";
        }
      }
    }
    if (filter.length() > 2) {
      filter = filter.substring(0, filter.length() - 2);
    }
  }

  public FileDialogEventType getEventType() {
    return eventType;
  }

  public void setEventType(FileDialogEventType eventType) {
    this.eventType = eventType;
  }

  public boolean isAllowDownload() {
    return allowDownload;
  }

  public void setAllowDownload(boolean allowDownload) {
    this.allowDownload = allowDownload;
  }

  public boolean isAllowUpload() {
    return allowUpload;
  }

  public void setAllowUpload(boolean allowUpload) {
    this.allowUpload = allowUpload;
  }

  public boolean isAllowDelete() {
    return allowDelete;
  }

  public void setAllowDelete(boolean allowDelete) {
    this.allowDelete = allowDelete;
  }

  public String getFilter() {
    return filter;
  }

  public void setFilter(String filter) {
    this.filter = filter;
  }

  public boolean isMultiSelection() {
    return isMultiSelection;
  }

  public void setMultiSelection(boolean isMultiSelection) {
    this.isMultiSelection = isMultiSelection;
  }

  public String getSelection() {
    return selection;
  }

  public void setSelection(String selection) {
    this.selection = selection;
  }

  public boolean isCustomDialog() {
    return customDialog;
  }

  public void setCustomDialog(boolean customDialog) {
    this.customDialog = customDialog;
  }

}
