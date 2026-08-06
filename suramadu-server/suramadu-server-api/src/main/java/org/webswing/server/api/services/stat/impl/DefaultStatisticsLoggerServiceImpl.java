package org.webswing.server.api.services.stat.impl;

import com.google.inject.Singleton;
import org.webswing.server.api.services.stat.StatisticsLoggerService;
import org.webswing.server.common.service.stats.StatisticsLogger;
import org.webswing.server.common.service.stats.logger.impl.DefaultStatisticsLogger;

@Singleton
public class DefaultStatisticsLoggerServiceImpl implements StatisticsLoggerService {

  public StatisticsLogger createLogger() {
    return new DefaultStatisticsLogger();
  }

}
