package org.webswing.server.common.service.stats.logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Warning {
  private static final DateFormat format = new SimpleDateFormat("dd MMM HH:mm:ss");

  private final Date start = new Date();
  private String metric;
  private String message;

  public Warning(String metric, String message) {
    this.metric = metric;
    this.message = message;
  }

  public void update(Warning w) {
    this.metric = w.metric;
    this.message = w.message;
  }

  @Override
  public String toString() {
    return "WARNING (" + format.format(start) + ") " + metric + ": " + message;
  }


}
