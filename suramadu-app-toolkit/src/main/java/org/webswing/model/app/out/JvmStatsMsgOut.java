package org.webswing.model.app.out;

import org.webswing.model.MsgOut;

import java.io.Serial;

public class JvmStatsMsgOut implements MsgOut {

  @Serial
  private static final long serialVersionUID = 4505813670370834105L;

  private double heapSize;
  private double heapSizeUsed;
  private double cpuUsage;
  private int edtPingSeconds;

  public double getHeapSize() {
    return heapSize;
  }

  public void setHeapSize(double heapSize) {
    this.heapSize = heapSize;
  }

  public double getHeapSizeUsed() {
    return heapSizeUsed;
  }

  public void setHeapSizeUsed(double heapSizeUsed) {
    this.heapSizeUsed = heapSizeUsed;
  }

  public double getCpuUsage() {
    return cpuUsage;
  }

  public void setCpuUsage(double cpuUsage) {
    this.cpuUsage = cpuUsage;
  }

  public void setEdtPingSeconds(int edtPingSeconds) {
    this.edtPingSeconds = edtPingSeconds;
  }

  public int getEdtPingSeconds() {
    return edtPingSeconds;
  }
}
