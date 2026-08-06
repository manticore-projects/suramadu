package org.webswing.model.appframe.in;

import org.webswing.model.MsgIn;
import org.webswing.model.SyncMsg;

import java.io.Serial;

public class PixelsAreaResponseMsgIn implements MsgIn, SyncMsg {
  @Serial
  private static final long serialVersionUID = 3478808080093729712L;

  private String correlationId;
  private String pixels;

  public String getCorrelationId() {
    return correlationId;
  }

  public void setCorrelationId(String correlationId) {
    this.correlationId = correlationId;
  }

  public String getPixels() {
    return pixels;
  }

  public void setPixels(String pixels) {
    this.pixels = pixels;
  }
}
