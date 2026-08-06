package org.webswing.server.common.service.stats.logger;

import org.webswing.server.common.service.stats.StatisticsLogger;

import java.util.Map;

public abstract class WarningRule {

  public abstract Warning checkWarning(Map<String, Number> lastMetrics);

  public static WarningRule memoryUtilizationRule(final double threshold) {
    return new WarningRule() {
      @Override
      public Warning checkWarning(Map<String, Number> lastMetrics) {
        Number allocated = lastMetrics.get(StatisticsLogger.MEMORY_ALLOCATED_METRIC);
        Number used = lastMetrics.get(StatisticsLogger.MEMORY_USED_METRIC);
        if (allocated != null && allocated.doubleValue() != 0 && used != null) {
          double utilization = used.doubleValue() / allocated.doubleValue();
          if (utilization >= threshold) {
            return warning(StatisticsLogger.MEMORY_USED_METRIC,
                "Utilization is too high! (%.1f)".formatted(utilization * 100));
          }
        }
        return null;
      }
    };
  }

  public static WarningRule thresholdRule(final String metric, final double threshold) {
    return WarningRule.thresholdRule(metric, threshold, "Value is too high (%.1f)");
  }

  public static WarningRule thresholdRule(final String metric, final double threshold,
      final String msg) {
    return new WarningRule() {
      @Override
      public Warning checkWarning(Map<String, Number> lastMetrics) {
        Number value = lastMetrics.get(metric);
        if (metric != null) {
          if (value.doubleValue() >= threshold) {
            return warning(metric, msg.formatted(value));
          }
        }
        return null;
      }
    };
  }

  private static Warning warning(String metric, String message) {
    return new Warning(metric, message);
  }
}
