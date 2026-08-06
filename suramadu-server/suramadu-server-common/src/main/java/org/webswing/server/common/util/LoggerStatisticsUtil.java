package org.webswing.server.common.util;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.server.common.service.stats.StatisticsReader;
import org.webswing.server.common.service.stats.logger.Aggregation;
import org.webswing.server.common.service.stats.logger.InstanceStats;

import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;

public class LoggerStatisticsUtil {

  private static final Logger log = LoggerFactory.getLogger(LoggerStatisticsUtil.class);

  public static Map<String, Map<String, Pair<BigDecimal, Integer>>> mergeSummaryInstanceStats(
      Collection<InstanceStats> stats) {
    Map<String, Map<String, Pair<BigDecimal, Integer>>> summaryLog = new HashMap<>();

    for (String name : StatisticsReader.summaryRulesMap.keySet()) {
      List<Aggregation> summaryAggreg = StatisticsReader.summaryRulesMap.get(name);
      for (Aggregation aggregation : summaryAggreg) {
        aggregate(summaryLog, stats, name, aggregation);
      }
    }

    return summaryLog;
  }

  private static void aggregate(Map<String, Map<String, Pair<BigDecimal, Integer>>> summaryLog,
      Collection<InstanceStats> instances, String name, Aggregation aggregation) {
    Set<Long> timestamps = new HashSet<>();
    Map<String, Pair<BigDecimal, Integer>> metric = new LinkedHashMap<>();
    for (InstanceStats instance : instances) {
      Map<Long, Number> valueMap = instance.getStatistics().get(name);
      if (valueMap != null) {
        timestamps.addAll(new ArrayList<>(valueMap.keySet()));
      }
    }
    for (Long key : timestamps) {
      List<Number> values = new ArrayList<>();
      for (InstanceStats instance : instances) {
        Map<Long, Number> valueMap = instance.getStatistics().get(name);
        if (valueMap != null && valueMap.get(key) != null) {
          values.add(valueMap.get(key));
        }
      }
      metric.put(key.toString(), Pair.of(calculateValue(aggregation, values), values.size()));
    }
    summaryLog.put(name + "." + aggregation.name(), metric);
  }

  private static BigDecimal calculateValue(Aggregation rule, List<Number> list) {
    Number result = 0;
    if (list != null && list.size() > 0) {
      for (Iterator<Number> iterator = list.iterator(); iterator.hasNext();) {
        result = calculateValue(rule, result.doubleValue(), iterator.next().doubleValue());
      }
      if (rule.equals(Aggregation.AVG)) {
        result = result.doubleValue() / list.size();
      }
    }
    return new BigDecimal(result.toString());
  }

  private static Number calculateValue(Aggregation rule, Number first, Number second) {
    switch (rule) {
      case MIN:
        return Math.min(first.doubleValue(), second.doubleValue());
      case MAX:
        return Math.max(first.doubleValue(), second.doubleValue());
      case SUM:
      case AVG:
      default:
        return first.doubleValue() + second.doubleValue();
    }
  }

  private static Number mergeAverages(Number first, Integer firstWeight, Number second,
      Integer secondWeight) {
    int weights = firstWeight + secondWeight;
    return ((double) firstWeight / (double) weights) * first.doubleValue()
        + ((double) secondWeight / (double) weights) * second.doubleValue();
  }

  public static Map<String, Map<String, Pair<BigDecimal, Integer>>> mergeSummaryStats(
      Map<String, Map<String, Pair<BigDecimal, Integer>>> map,
      Map<String, Map<String, Pair<BigDecimal, Integer>>> otherMap) {
    if (otherMap == null || otherMap.isEmpty()) {
      return map;
    }
    if (map == null || map.isEmpty()) {
      return otherMap;
    }

    Map<String, Map<String, Pair<BigDecimal, Integer>>> resultMap = new HashMap<>();

    for (Entry<String, Map<String, Pair<BigDecimal, Integer>>> entry : map.entrySet()) {
      String ruleName = entry.getKey();

      resultMap.put(ruleName, mergeRules(ruleName, entry.getValue(), otherMap.get(ruleName)));
    }

    return resultMap;
  }

  private static Map<String, Pair<BigDecimal, Integer>> mergeRules(String ruleName,
      Map<String, Pair<BigDecimal, Integer>> map, Map<String, Pair<BigDecimal, Integer>> otherMap) {
    if (otherMap == null || otherMap.isEmpty()) {
      return map;
    }
    if (map == null || map.isEmpty()) {
      return otherMap;
    }

    Map<String, Pair<BigDecimal, Integer>> resultMap = new HashMap<>();

    for (Entry<String, Pair<BigDecimal, Integer>> entry : map.entrySet()) {
      String timestamp = entry.getKey();

      resultMap.put(timestamp,
          mergeTimestamps(ruleName, entry.getValue(), otherMap.get(timestamp)));
    }

    return resultMap;
  }

  private static Pair<BigDecimal, Integer> mergeTimestamps(String ruleName,
      Pair<BigDecimal, Integer> pair, Pair<BigDecimal, Integer> otherPair) {
    if (otherPair == null) {
      return pair;
    }
    if (pair == null) {
      return otherPair;
    }

    Aggregation aggregation = null;
    try {
      aggregation = Aggregation.valueOf(ruleName.substring(ruleName.lastIndexOf('.') + 1));
    } catch (Exception e) {
      log.warn("Could not parse aggregation rule from rule name [{}]!", ruleName, e);
      return pair;
    }

    int counts = pair.getRight() + otherPair.getRight();

    Number result = null;
    switch (aggregation) {
      case AVG:
        result = mergeAverages(pair.getLeft(), pair.getRight(), otherPair.getLeft(),
            otherPair.getRight());
        break;
      default:
        result = calculateValue(aggregation, pair.getLeft().doubleValue(),
            otherPair.getLeft().doubleValue());
    }

    return Pair.of(new BigDecimal(result.toString()), counts);
  }

  private LoggerStatisticsUtil() {}

}
