package org.webswing.util;

import org.webswing.Constants;
import org.webswing.model.common.in.RecordingStatusEnum;
import org.webswing.toolkit.util.Services;
import org.webswing.toolkit.util.Util;

import javax.swing.JOptionPane;
import java.io.*;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.util.Date;

import static org.webswing.toolkit.util.Util.async;

public class SessionRecorder {

  private final String instanceId;

  private RecordingStatusEnum recordingStatus = RecordingStatusEnum.NOT_RECORDING;
  private OutputStream outputStream;
  private File tempFile;
  private SessionRecordingHeader header;

  private String fileName;

  private RunnableWithException doAfterAllowingRecording;
  private boolean showingApprovalDialog;
  private boolean consentApproved;

  private long lastFrame;

  public SessionRecorder(String instanceId) {
    this.instanceId = instanceId;
  }

  private static byte[] serializeObject(Serializable o) throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutput out = null;
    try {
      out = new ObjectOutputStream(bos);
      out.writeObject(o);
      return bos.toByteArray();
    } catch (IOException ex) {
      AppLogger.error("Failed to serialize object.", ex);
      throw ex;
    } finally {
      try {
        out.close();
      } catch (IOException e) {
        // ignore close exception
      }
      try {
        bos.close();
      } catch (IOException e) {
        // ignore close exception
      }
    }
  }

  public void saveFrame(byte[] serialized) {
    if (recordingStatus.equals(RecordingStatusEnum.RECORDING)) {
      try {
        if (outputStream != null) {
          byte[] length = ByteBuffer.allocate(4).putInt(serialized.length).array();
          long now = new Date().getTime();
          this.lastFrame = this.lastFrame == 0 ? now : this.lastFrame;
          byte[] delay = ByteBuffer.allocate(4).putInt((int) (now - this.lastFrame)).array();
          this.lastFrame = now;
          try {
            outputStream.write(delay);
            outputStream.write(length);
            outputStream.write(serialized);
            outputStream.flush();
          } catch (IOException e) {
            AppLogger.error("Failed to write to session recording file.", e);
            throw e;
          }
        }
      } catch (Exception e) {
        AppLogger.error("Writing recorded frame failed. Stopping recording session.", e);
        try {
          stopRecording();
        } catch (Exception e1) {
          AppLogger.error("Stopping recording session failed.", e1);
        }
      }
    }
  }

  private void startRecordingInternal() throws Exception {
    try {
      String fileName = URLEncoder.encode(instanceId, "UTF-8") + ".wss";
      this.fileName = fileName;
      AppLogger.info("Starting session recording for " + instanceId + " into file:" + fileName);
      // make sure previous out stream is closed
      if (outputStream != null) {
        try {
          outputStream.close();
        } catch (Exception e) {
          AppLogger.error("Failed to close previous recording!", e);
        }
      }

      String tempDir = System.getProperty(Constants.TEMP_DIR_PATH);
      tempFile = new File(URI.create(tempDir + fileName));
      if (tempFile.exists()) {
        tempFile.delete();
      } else if (!tempFile.getParentFile().exists()) {
        tempFile.getParentFile().mkdirs();
      }

      outputStream = new FileOutputStream(tempFile);
      header = new SessionRecordingHeader();
      header.setClientId(instanceId);
      header.setStartDate(new Date());
      this.lastFrame = new Date().getTime();
      byte[] version = ByteBuffer.allocate(4).putInt(SessionRecordingHeader.version).array();
      byte[] headerbytes = serializeObject(header);
      outputStream.write(version);
      setRecordingStatus(RecordingStatusEnum.RECORDING);
      saveFrame(headerbytes);
    } catch (FileNotFoundException e) {
      AppLogger.error("Failed to create session recording file.", e);
      setRecordingStatus(RecordingStatusEnum.NOT_RECORDING);
      throw new Exception("Failed to create session recording file.", e);
    } catch (IOException e) {
      AppLogger.error("Failed to start recording.", e);
      setRecordingStatus(RecordingStatusEnum.NOT_RECORDING);
      throw new Exception("Failed to start recording.", e);
    }
  }

  private void stopRecordingInternal() throws Exception {
    try {
      AppLogger.info("Stopping session recording for " + instanceId);
      if (outputStream != null) {
        try {
          outputStream.flush();
        } catch (IOException e) {
          AppLogger.error("Failed to close recording file.", e);
          throw e;
        }

        try (InputStream is = new FileInputStream(tempFile)) {
          Services.getDataStoreService().storeData("recording", fileName, is, true);
        } catch (Exception e) {
          AppLogger.error(
              "Failed to store recording file ["
                  + (tempFile == null ? "null" : tempFile.getAbsolutePath()) + "] to data store!",
              e);
          throw e;
        }
      }
    } catch (Exception e) {
      throw new Exception("Exception while finishing recording.", e);
    } finally {
      setRecordingStatus(RecordingStatusEnum.NOT_RECORDING);
    }
  }

  public void startRecording() throws Exception {
    if (recordingStatus.equals(RecordingStatusEnum.DENIED_RECORDING_BY_USER)) {
      setRecordingStatus(RecordingStatusEnum.NOT_RECORDING);
    }

    if (recordingStatus.equals(RecordingStatusEnum.NOT_RECORDING)) {
      doAfterAllowingRecording = this::startRecordingInternal;

      if (isRecordingAskNeeded()) {
        setRecordingStatus(RecordingStatusEnum.WAITING_FOR_RECORDING_APPROVAL);
        async(this::showApprovalDialog);
      } else {
        doAfterAllowingRecording.run();
      }
    } else {
      throw new Exception("Already recording.");
    }
  }

  public void stopRecording() throws Exception {
    if (recordingStatus.equals(RecordingStatusEnum.RECORDING)) {
      stopRecordingInternal();
    } else if (recordingStatus.equals(RecordingStatusEnum.WAITING_FOR_RECORDING_APPROVAL)) {
      setRecordingStatus(RecordingStatusEnum.NOT_RECORDING);
    } else {
      throw new Exception("Recording not started.");
    }
  }

  private void setRecordingStatus(RecordingStatusEnum recordingStatus) {
    this.recordingStatus = recordingStatus;
    notifyStateChange();
  }

  private void notifyStateChange() {
    Util.getWebToolkit().getPaintDispatcher().notifySessionDataChanged();
  }

  private void showApprovalDialog() {
    if (showingApprovalDialog) {
      return;
    }

    Object[] approvalDialogButtons = new Object[] {
        System.getProperty(Constants.SWING_START_SYS_PROP_RECORDING_ALLOW_BUTTON,
            Constants.SWING_START_SYS_PROP_RECORDING_ALLOW_BUTTON_DEFAULT),
        System.getProperty(Constants.SWING_START_SYS_PROP_RECORDING_DENY_BUTTON,
            Constants.SWING_START_SYS_PROP_RECORDING_DENY_BUTTON_DEFAULT)};

    showingApprovalDialog = true;

    int result = JOptionPane.showOptionDialog(null,
        System.getProperty(Constants.SWING_START_SYS_PROP_RECORDING_MESSAGE,
            Constants.SWING_START_SYS_PROP_RECORDING_MESSAGE_DEFAULT),
        System.getProperty(Constants.SWING_START_SYS_PROP_RECORDING_DIALOG_TITLE,
            Constants.SWING_START_SYS_PROP_RECORDING_DIALOG_TITLE_DEFAULT),
        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, // no custom icon
        approvalDialogButtons, // button titles
        approvalDialogButtons[0] // default button
    );

    showingApprovalDialog = false;

    resolveApprovalDialog(result == JOptionPane.YES_OPTION);
  }

  private void resolveApprovalDialog(boolean approved) {
    if (getRecordingStatus() == RecordingStatusEnum.WAITING_FOR_RECORDING_APPROVAL) {
      if (approved) {
        setRecordingStatus(RecordingStatusEnum.NOT_RECORDING);
        try {
          consentApproved = true;
          doAfterAllowingRecording.run();
        } catch (Exception e) {
          AppLogger.error("Cannot resolve approval dialog, error occurred.");
        }
      } else {
        setRecordingStatus(RecordingStatusEnum.DENIED_RECORDING_BY_USER);
      }
    }
    doAfterAllowingRecording = () -> {
    };
  }

  private boolean isRecordingAskNeeded() {
    return Boolean.getBoolean(Constants.SWING_START_SYS_PROP_RECORDING_ASK_NEEDED)
        && !consentApproved;
  }

  public RecordingStatusEnum getRecordingStatus() {
    return recordingStatus;
  }

  public String getFileName() {
    return fileName;
  }

  @FunctionalInterface
  interface RunnableWithException {
    void run() throws Exception;
  }
}
