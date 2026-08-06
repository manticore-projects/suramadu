package org.webswing.util;

import org.webswing.Constants;
import org.webswing.model.common.in.MirroringStatusEnum;
import org.webswing.toolkit.util.Util;

import javax.swing.JOptionPane;

import static org.webswing.toolkit.util.Util.async;

public class SessionMirror {

  private MirroringStatusEnum mirroringStatus = MirroringStatusEnum.NOT_MIRRORING;

  private Runnable doAfterAllowingMirroring;
  private boolean showingApprovalDialog;

  public MirroringStatusEnum getMirroringStatus() {
    return mirroringStatus;
  }

  public void startMirroring() throws Exception {
    if (mirroringStatus.equals(MirroringStatusEnum.DENIED_MIRRORING_BY_USER)) {
      setMirroringStatus(MirroringStatusEnum.NOT_MIRRORING);
    }

    if (mirroringStatus.equals(MirroringStatusEnum.NOT_MIRRORING)) {
      doAfterAllowingMirroring = () -> {
        setMirroringStatus(MirroringStatusEnum.MIRRORING);
      };

      if (isMirroringAskNeeded()) {
        setMirroringStatus(MirroringStatusEnum.WAITING_FOR_MIRRORING_APPROVAL);
        async(this::showApprovalDialog);
      } else {
        doAfterAllowingMirroring.run();
      }
    } else {
      throw new Exception("Already mirroring.");
    }
  }

  public void stopMirroring() {
    setMirroringStatus(MirroringStatusEnum.NOT_MIRRORING);
  }

  private void setMirroringStatus(MirroringStatusEnum mirroringStatus) {
    this.mirroringStatus = mirroringStatus;
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
        System.getProperty(Constants.SWING_START_SYS_PROP_MIRRORING_ALLOW_BUTTON,
            Constants.SWING_START_SYS_PROP_MIRRORING_ALLOW_BUTTON_DEFAULT),
        System.getProperty(Constants.SWING_START_SYS_PROP_MIRRORING_DENY_BUTTON,
            Constants.SWING_START_SYS_PROP_MIRRORING_DENY_BUTTON_DEFAULT)};

    showingApprovalDialog = true;

    int result = JOptionPane.showOptionDialog(null,
        System.getProperty(Constants.SWING_START_SYS_PROP_MIRRORING_MESSAGE,
            Constants.SWING_START_SYS_PROP_MIRRORING_MESSAGE_DEFAULT),
        System.getProperty(Constants.SWING_START_SYS_PROP_MIRRORING_DIALOG_TITLE,
            Constants.SWING_START_SYS_PROP_MIRRORING_DIALOG_TITLE_DEFAULT),
        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, // no custom icon
        approvalDialogButtons, // button titles
        approvalDialogButtons[0] // default button
    );

    showingApprovalDialog = false;

    resolveApprovalDialog(result == JOptionPane.YES_OPTION);
  }

  private void resolveApprovalDialog(boolean approved) {
    if (getMirroringStatus() == MirroringStatusEnum.WAITING_FOR_MIRRORING_APPROVAL) {
      if (approved) {
        setMirroringStatus(MirroringStatusEnum.NOT_MIRRORING);
        try {
          doAfterAllowingMirroring.run();
        } catch (Exception e) {
          AppLogger.error("Cannot resolve approval dialog, error occurred.");
        }
      } else {
        setMirroringStatus(MirroringStatusEnum.DENIED_MIRRORING_BY_USER);
      }
    }
    doAfterAllowingMirroring = () -> {
    };
  }

  private boolean isMirroringAskNeeded() {
    return Boolean.getBoolean(Constants.SWING_START_SYS_PROP_MIRRORING_ASK_NEEDED);
  }
}
