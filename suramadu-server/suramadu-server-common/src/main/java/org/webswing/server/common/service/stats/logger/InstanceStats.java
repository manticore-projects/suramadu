package org.webswing.server.common.service.stats.logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public class InstanceStats {
  private static final int MAX_WARNING_HISTORY_SIZE = 20;
  private final DateFormat format = new SimpleDateFormat("HH:mm:ss");
  private final Map<String, Map<Long, Number>> statisticsLog = new ConcurrentHashMap<>();
  private final Map<String, Long> lastTimestampMap = new ConcurrentHashMap<String, Long>();
  private final Map<String, List<Number>> lastTimestampNumbers =
      new ConcurrentHashMap<String, List<Number>>();
  private final Map<String, Number> lastMetrics = new ConcurrentHashMap<>();
  private final Map<String, Warning> warnings = new ConcurrentHashMap<>();
  private final ConcurrentLinkedQueue<String> warningHistory = new ConcurrentLinkedQueue<>();
  private final Map<String, Number[]> sessionStats = new ConcurrentHashMap<String, Number[]>();

  public void processMetric(MetricRule rule, String name, Number value, WarningRule warnRule) {
    // round timestamp to interval milis
    long timestamp = rule.getInterval() == 0 ? System.currentTimeMillis()
        : ((System.currentTimeMillis() / rule.getInterval()) * rule.getInterval());

    // create map if null
    Map<Long, Number> valueMap = statisticsLog.get(name);
    if (valueMap == null) {
      valueMap = new LinkedHashMap<Long, Number>() {
        private static final long serialVersionUID = 3552039647099141391L;

        @Override
        protected boolean removeEldestEntry(Map.Entry<Long, Number> eldest) {
          if (rule.getInterval() == 0) {
            return this.size() > rule.getMetricHistoryLimit();
          }
          long current = (System.currentTimeMillis() / rule.getInterval()) * rule.getInterval();
          long maxAge = rule.getMetricHistoryLimit() * rule.getInterval();
          return eldest.getKey() < (current - maxAge);
        }
      };
      statisticsLog.put(name, valueMap);
    }

    if (rule.getInterval() == 0) {
      valueMap.put(timestamp, value);
      lastMetrics.put(name, value);
      processWarningRule(name, warnRule);
    } else {
      // flush last timestamp entry if interval passed
      Long last = lastTimestampMap.get(name);
      if (last != null && last != timestamp && lastTimestampNumbers.get(name) != null) {
        List<Number> list = lastTimestampNumbers.remove(name);
        Number aggregated = calculateValue(rule, list);
        valueMap.put(last, aggregated);
        calculateSessionValue(rule, name, aggregated);
        lastMetrics.put(name, aggregated);
        processWarningRule(name, warnRule);
      }

      // store current value to temp map
      lastTimestampMap.put(name, timestamp);
      if (lastTimestampNumbers.get(name) == null) {
        lastTimestampNumbers.put(name, new ArrayList<Number>());
      }
      lastTimestampNumbers.get(name).add(value);
    }
  }

  private void calculateSessionValue(MetricRule rule, String name, Number aggregated) {
    String key = name + "." + rule.getAggregation() + ".SESSION";
    if (!sessionStats.containsKey(key)) {
      sessionStats.put(key, new Number[] {aggregated, 1});
    } else {
      Number result = 0;
      Number[] values = sessionStats.get(key);
      Number lastValue = values[0];
      Number count = values[1];

      switch (rule.getAggregation()) {
        case MIN:
          result = Math.min(aggregated.doubleValue(), lastValue.doubleValue());
          break;
        case MAX:
          result = Math.max(aggregated.doubleValue(), lastValue.doubleValue());
          break;
        case SUM:
          result = lastValue.doubleValue() + aggregated.doubleValue();
          break;
        case AVG:
        default:
          result = lastValue.doubleValue()
              + (aggregated.doubleValue() - lastValue.doubleValue()) / count.doubleValue();
          break;
      }

      sessionStats.replace(key, new Number[] {result, count.doubleValue() + 1});
    }
  }

  private void processWarningRule(String name, WarningRule warnRule) {
    if (warnRule != null) {
      Warning warning = warnRule.checkWarning(lastMetrics);

      if (warning == null && warnings.containsKey(name)) {
        Warning value = warnings.remove(name);
        String date = format.format(new Date());
        warningHistory.add(value + " (until " + date + ")");
        if (warningHistory.size() > MAX_WARNING_HISTORY_SIZE) {
          warningHistory.poll();
        }
      }

      if (warning != null) {
        if (warnings.containsKey(name)) {
          warnings.get(name).update(warning);
        } else {
          warnings.put(name, warning);
        }
      }
    }
  }

  private Number calculateValue(MetricRule rule, List<Number> list) {
    // store value
    Number result = 0;
    if (list != null && list.size() > 0) {
      for (Iterator<Number> iterator = list.iterator(); iterator.hasNext();) {
        Number number = iterator.next();
        switch (rule.getAggregation()) {
          case MIN:
            result = Math.min(number.doubleValue(), result.doubleValue());
            break;
          case MAX:
            result = Math.max(number.doubleValue(), result.doubleValue());
            break;
          case SUM:
          case AVG:
          default:
            result = result.doubleValue() + number.doubleValue();
            break;
        }
      }
      if (rule.getAggregation().equals(Aggregation.AVG)) {
        result = result.doubleValue() / list.size();
      }
      if (rule.getAggregation().equals(Aggregation.AVG_PER_SEC)) {
        result = result.doubleValue() / (rule.getInterval() / 1000);
      }
    }
    return result;
  }

  public Map<String, Number> getMetrics() {
    List<Aggregation> aggregations =
        Arrays.asList(Aggregation.MIN, Aggregation.MAX, Aggregation.AVG);
    Map<String, Number> metrics = new HashMap<>(lastMetrics);
    MetricRule rule = new MetricRule(Aggregation.MIN, 0, 0);
    for (String name : statisticsLog.keySet()) {
      List<Number> valueList = new ArrayList<>(statisticsLog.get(name).values());
      for (Aggregation a : aggregations) {
        rule.setAggregation(a);
        metrics.put(name + "." + a, calculateValue(rule, valueList));
      }
    }
    metrics.putAll(getSessionStatistics());
    return metrics;
  }

  public List<String> getWarnings() {
    ArrayList<String> result = new ArrayList<>();
    for (Warning w : warnings.values()) {
      result.add(w.toString());
    }
    return result;
  }

  public Map<String, Map<Long, Number>> getStatistics() {
    return statisticsLog;
  }

  public Map<String, Number> getSessionStatistics() {
    Map<String, Number> stats = sessionStats.entrySet().stream().collect(Collectors
        .<Entry<String, Number[]>, String, Number>toMap(p -> p.getKey(), p -> p.getValue()[0]));
    return stats;
  }

  public List<String> getWarningHistory() {
    ArrayList<String> result = new ArrayList<>(warningHistory);
    Collections.reverse(result);
    return result;
  }
}
