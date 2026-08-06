package org.webswing.server.common.service.stats.logger;

public class MetricRule {
  private Aggregation aggregation;
  private long interval;
  private int metricHistoryLimit;

  public MetricRule(Aggregation aggregation, long interval, int metricHistoryLimit) {
    super();
    this.aggregation = aggregation;
    this.interval = interval;
    this.metricHistoryLimit = metricHistoryLimit;
  }

  public Aggregation getAggregation() {
    return aggregation;
  }

  public void setAggregation(Aggregation aggregation) {
    this.aggregation = aggregation;
  }

  public long getInterval() {
    return interval;
  }

  public void setInterval(long interval) {
    this.interval = interval;
  }

  public int getMetricHistoryLimit() {
    return metricHistoryLimit;
  }

  public void setMetricHistoryLimit(int metricHistoryLimit) {
    this.metricHistoryLimit = metricHistoryLimit;
  }

}
