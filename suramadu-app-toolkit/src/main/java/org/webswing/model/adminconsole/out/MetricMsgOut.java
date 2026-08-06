package org.webswing.model.adminconsole.out;

import org.webswing.model.MsgOut;

import java.io.Serial;

public class MetricMsgOut implements MsgOut {

  @Serial
  private static final long serialVersionUID = -5556290122276899717L;

  private String key;
  private long value;
  private int aggregatedCount;

  public MetricMsgOut() {}

  public MetricMsgOut(String key, long value, int aggregatedCount) {
    this.key = key;
    this.value = value;
    this.aggregatedCount = aggregatedCount;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public long getValue() {
    return value;
  }

  public void setValue(long value) {
    this.value = value;
  }

  public int getAggregatedCount() {
    return aggregatedCount;
  }

  public void setAggregatedCount(int aggregatedCount) {
    this.aggregatedCount = aggregatedCount;
  }

}
