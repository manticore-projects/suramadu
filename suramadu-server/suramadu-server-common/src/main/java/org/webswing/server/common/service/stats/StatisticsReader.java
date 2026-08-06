package org.webswing.server.common.service.stats;

import org.apache.commons.lang3.tuple.Pair;
import org.webswing.server.common.service.stats.logger.Aggregation;
import org.webswing.server.common.service.stats.logger.InstanceStats;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface StatisticsReader {

  Map<String, List<Aggregation>> summaryRulesMap = Map.of(StatisticsLogger.MEMORY_ALLOCATED_METRIC,
      List.of(Aggregation.SUM), StatisticsLogger.MEMORY_USED_METRIC, List.of(Aggregation.SUM),
      StatisticsLogger.INBOUND_SIZE_METRIC, List.of(Aggregation.SUM),
      StatisticsLogger.OUTBOUND_SIZE_METRIC, List.of(Aggregation.SUM),
      StatisticsLogger.CPU_UTIL_METRIC, List.of(Aggregation.SUM), StatisticsLogger.LATENCY_PING,
      List.of(Aggregation.AVG), StatisticsLogger.LATENCY_NETWORK_TRANSFER, List.of(Aggregation.AVG),
      StatisticsLogger.LATENCY_CLIENT_RENDERING, List.of(Aggregation.AVG),
      StatisticsLogger.LATENCY_SERVER_RENDERING, List.of(Aggregation.AVG), StatisticsLogger.LATENCY,
      List.of(Aggregation.MAX));

  Map<String, Map<String, Pair<BigDecimal, Integer>>> getSummaryStats();

  Map<String, List<String>> getSummaryWarnings();

  /**
   * @return Map &lt; name_of_metric, Map &lt; timestamp, value &gt; &gt;
   */
  Map<String, Map<Long, Number>> getInstanceStats(String instance);

  /**
   * @return Map &lt; name_of_metric, value &gt;
   */
  Map<String, Number> getInstanceMetrics(String instanceId);

  List<String> getInstanceWarnings(String instance);

  List<String> getInstanceWarningHistory(String instance);

  Collection<InstanceStats> getAllInstanceStats();

}
