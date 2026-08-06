package org.webswing.toolkit;

import org.webswing.model.appframe.out.FocusEventMsgOut;
import org.webswing.toolkit.api.component.HtmlPanel;
import org.webswing.toolkit.util.Util;

import javax.swing.JPasswordField;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.peer.KeyboardFocusManagerPeer;

@SuppressWarnings("restriction")
public class WebKeyboardFocusManagerPeer implements KeyboardFocusManagerPeer {

  private static boolean optimizeCaret = Boolean.getBoolean("webswing.optimizeCaret");

  private static CaretListener caretListener = new CaretListener() {
    @Override
    public void caretUpdate(CaretEvent e) {
      Object c = e.getSource();
      Component o = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();

      if (o != c) {
        // do not send caret update focus event if the current focus owner is not the source of this
        // event
        // without this check a focus event for previous owner is sent when moving focus from one
        // field to another
        return;
      }

      Util.getWebToolkit().getPaintDispatcher().notifyFocusEvent(getFocusEvent());
    }
  };

  @Override
  public void clearGlobalFocusOwner(Window activeWindow) {}

  @Override
  public Component getCurrentFocusOwner() {
    Window window = Util.getWebToolkit().getWindowManager().getActiveWindow();
    if (window != null) {
      return window.getFocusOwner();
    }
    return null;
  }

  @Override
  public Window getCurrentFocusedWindow() {
    return Util.getWebToolkit().getWindowManager().getActiveWindow();
  }

  @Override
  public void setCurrentFocusOwner(Component comp) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        Component o = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
        if (o instanceof JTextComponent tc) {
          tc.removeCaretListener(caretListener);
          tc.addCaretListener(caretListener);
        }
        Util.getWebToolkit().getPaintDispatcher().notifyFocusEvent(getFocusEvent());
      }
    });
  }

  private static FocusEventMsgOut getFocusEvent() {
    Component o = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
    FocusEventMsgOut msg = new FocusEventMsgOut();
    if (o != null && o.isShowing() && !(o instanceof HtmlPanel)) {
      msg.setType(FocusEventMsgOut.FocusEventType.focusGained);
      Point l = o.getLocationOnScreen();
      msg.setX(l.x);
      msg.setY(l.y);
      Rectangle b = o.getBounds();
      msg.setW(b.width);
      msg.setH(b.height);
      if (o instanceof JTextComponent tc) {
        if (tc.isEditable() && tc.getWidth() > 0 && tc.getHeight() > 0) {
          if (optimizeCaret && tc.getCaret().getBlinkRate() != 0) {
            tc.getCaret().setBlinkRate(0);
          }
          int position = tc.getCaretPosition();
          try {
            Rectangle location = tc.modelToView(position);
            if (location != null) {
              msg.setType(FocusEventMsgOut.FocusEventType.focusWithCarretGained);
              msg.setCaretX(location.x);
              msg.setCaretY(location.y);
              msg.setCaretH(location.height);
              msg.setEditable(tc.isEditable());
            }
          } catch (BadLocationException e) {
            e.printStackTrace();
          }
        }
      }
      if (o instanceof JPasswordField) {
        msg.setType(FocusEventMsgOut.FocusEventType.focusPasswordGained);
      }
    } else {
      msg.setType(FocusEventMsgOut.FocusEventType.focusLost);
    }
    return msg;
  }

  @Override
  public void setCurrentFocusedWindow(Window win) {}

}
