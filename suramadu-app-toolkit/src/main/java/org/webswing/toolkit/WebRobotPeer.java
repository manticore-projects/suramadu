package org.webswing.toolkit;

import org.webswing.model.SyncObjectResponse;
import org.webswing.model.app.out.AppToServerFrameMsgOut;
import org.webswing.model.appframe.in.AppFrameMsgIn;
import org.webswing.model.appframe.in.InputEventMsgIn;
import org.webswing.model.appframe.in.KeyboardEventMsgIn;
import org.webswing.model.appframe.in.MouseEventMsgIn;
import org.webswing.model.appframe.out.AppFrameMsgOut;
import org.webswing.model.appframe.out.PixelsAreaRequestMsgOut;
import org.webswing.toolkit.util.Services;
import org.webswing.toolkit.util.Util;
import org.webswing.util.AppLogger;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.peer.RobotPeer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

public class WebRobotPeer implements RobotPeer {

  GraphicsDevice device;
  private Point mousePos = new Point(0, 0);
  private int button;
  private int buttons;
  HashSet<Integer> pressedKeys = new HashSet<Integer>();

  public WebRobotPeer(Robot robot, GraphicsDevice device) {
    this.device = device;
  }

  @Override
  public void mouseMove(int x, int y) {
    mousePos = getValidPoint(x, y);
    sendMouseEvent(MouseEventMsgIn.MouseEventType.mousemove, 0, mousePos);
  }

  @Override
  public void mousePress(int swingmask) {
    buttons = toButtonsWebEventMask(swingmask);
    button = getButtonPressed(swingmask);
    sendMouseEvent(MouseEventMsgIn.MouseEventType.mousedown, 0, mousePos);
  }

  @Override
  public void mouseRelease(int swingmask) {
    buttons = toButtonsWebEventMask(swingmask);
    button = getButtonPressed(swingmask);
    sendMouseEvent(MouseEventMsgIn.MouseEventType.mouseup, 0, mousePos);
  }

  @Override
  public void mouseWheel(int wheelAmt) {
    sendMouseEvent(MouseEventMsgIn.MouseEventType.mousewheel, wheelAmt, mousePos);
  }

  @Override
  public void keyPress(int keycode) {
    pressedKeys.add(keycode);
    sendKeyEvent(KeyboardEventMsgIn.KeyEventType.keydown, keycode);
    sendKeyEvent(KeyboardEventMsgIn.KeyEventType.keypress, keycode);
  }

  @Override
  public void keyRelease(int keycode) {
    pressedKeys.remove(keycode);
    sendKeyEvent(KeyboardEventMsgIn.KeyEventType.keyup, keycode);
  }

  @Override
  public int getRGBPixel(int x, int y) {
    return getRGBPixels(new Rectangle(x, y, 1, 1))[0];
  }

  @Override
  public int[] getRGBPixels(Rectangle bounds) {
    BufferedImage resultImage =
        new BufferedImage(bounds.width, bounds.height, BufferedImage.TYPE_INT_ARGB);
    AppFrameMsgOut frameOut = new AppFrameMsgOut();
    PixelsAreaRequestMsgOut pixelsRequest = new PixelsAreaRequestMsgOut();
    pixelsRequest.setCorrelationId(UUID.randomUUID().toString());
    pixelsRequest.setX(bounds.x);
    pixelsRequest.setY(bounds.y);
    pixelsRequest.setW(bounds.width);
    pixelsRequest.setH(bounds.height);
    frameOut.setPixelsRequest(pixelsRequest);
    try {
      AppToServerFrameMsgOut msgOut = new AppToServerFrameMsgOut();
      SyncObjectResponse result = Services.getConnectionService().sendObjectSync(msgOut, frameOut,
          pixelsRequest.getCorrelationId());
      if (result.getFrame() != null) {
        AppFrameMsgIn frame = result.getFrame();
        if (frame != null && frame.getPixelsResponse() != null) {
          Image image =
              Services.getImageService().readFromDataUrl(frame.getPixelsResponse().getPixels());
          Graphics g = resultImage.getGraphics();
          g.drawImage(image, 0, 0, null);
          g.dispose();
        }
      }
    } catch (Exception e) {
      AppLogger.error("Failed to get pixels area from browser.", e);
    }
    return ((DataBufferInt) resultImage.getRaster().getDataBuffer()).getData();
  }

  private void sendKeyEvent(KeyboardEventMsgIn.KeyEventType type, int keycode) {
    AppFrameMsgIn msgIn = new AppFrameMsgIn();
    KeyboardEventMsgIn kme = new KeyboardEventMsgIn();
    kme.setType(type);
    kme.setShift(isShift());
    kme.setMeta(isMeta());
    kme.setAlt(isAlt());
    kme.setCtrl(isCtrl());
    kme.setKeycode(keycode);
    int character = !isShift() && (keycode >= 65 && keycode <= 90) ? keycode + 32 : keycode;
    kme.setCharacter(character);
    InputEventMsgIn input = new InputEventMsgIn();
    input.setKey(kme);
    msgIn.setEvents(Arrays.asList(input));
    Util.getWebToolkit().getEventDispatcher().dispatchEvent(msgIn);
  }

  private void sendMouseEvent(MouseEventMsgIn.MouseEventType type, int wheelDelta, Point mousePos) {
    AppFrameMsgIn msgIn = new AppFrameMsgIn();
    MouseEventMsgIn mme = new MouseEventMsgIn();
    mme.setShift(isShift());
    mme.setAlt(isAlt());
    mme.setCtrl(isCtrl());
    mme.setMeta(isMeta());
    mme.setButton(button);
    mme.setButtons(buttons);
    mme.setType(type);
    mme.setWheelDelta(wheelDelta);
    mme.setX(mousePos.x);
    mme.setY(mousePos.y);
    InputEventMsgIn input = new InputEventMsgIn();
    input.setMouse(mme);
    msgIn.setEvents(Arrays.asList(input));
    Util.getWebToolkit().getEventDispatcher().dispatchEvent(msgIn);
  }

  private boolean isMeta() {
    return pressedKeys.contains(KeyEvent.VK_WINDOWS);
  }

  private boolean isCtrl() {
    return pressedKeys.contains(KeyEvent.VK_CONTROL);
  }

  private boolean isAlt() {
    return pressedKeys.contains(KeyEvent.VK_ALT) || pressedKeys.contains(KeyEvent.VK_ALT_GRAPH);
  }

  private boolean isShift() {
    return pressedKeys.contains(KeyEvent.VK_SHIFT);
  }

  private Point getValidPoint(int x, int y) {
    Rectangle bounds = device.getDefaultConfiguration().getBounds();
    x = Math.max(x, bounds.x);
    x = Math.min(x, bounds.x + bounds.width);
    y = Math.max(y, bounds.y);
    y = Math.min(y, bounds.y + bounds.height);
    return new Point(x, y);
  }

  private int toButtonsWebEventMask(int swingMask) {
    int result = 0;
    if ((swingMask & MouseEvent.BUTTON1_MASK) == MouseEvent.BUTTON1_MASK) {
      result = result | 2;
    }
    if ((swingMask & MouseEvent.BUTTON2_MASK) == MouseEvent.BUTTON2_MASK) {
      result = result | 4;
    }
    if ((swingMask & MouseEvent.BUTTON3_MASK) == MouseEvent.BUTTON3_MASK) {
      result = result | 8;
    }
    return result;
  }

  private int getButtonPressed(int swingMask) {
    if ((swingMask & MouseEvent.BUTTON1_MASK) == MouseEvent.BUTTON1_MASK) {
      return 1;
    }
    if ((swingMask & MouseEvent.BUTTON2_MASK) == MouseEvent.BUTTON2_MASK) {
      return 2;
    }
    if ((swingMask & MouseEvent.BUTTON3_MASK) == MouseEvent.BUTTON3_MASK) {
      return 3;
    }
    return 0;
  }

}
