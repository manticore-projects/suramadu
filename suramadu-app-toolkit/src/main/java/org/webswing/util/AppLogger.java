package org.webswing.util;

import org.webswing.Constants;

import java.io.PrintWriter;
import java.io.StringWriter;

public class AppLogger {

  public static final int TRACE = 0;
  public static final int DEBUG = 1;
  public static final int INFO = 2;
  public static final int WARNING = 3;
  public static final int ERROR = 4;
  public static final int FATAL = 5;

  private static final AppLogger log = new AppLogger();
  private int threshold = Integer.getInteger(Constants.SWING_START_SYS_PROP_LOG_LEVEL, 2);

  public static void trace(String message, Object... o) {
    log(TRACE, "TRACE: " + message, o);
  }

  public static void debug(String message, Object... o) {
    log(DEBUG, "DEBUG: " + message, o);
  }

  public static void info(String message, Object... o) {
    log(INFO, "INFO: " + message, o);
  }

  public static void warn(String message, Object... o) {
    log(WARNING, "WARNING: " + message, o);
  }

  public static void error(String message, Object... o) {
    log(ERROR, "ERROR: " + message, o);
  }

  public static void fatal(String message, Object... o) {
    log(FATAL, "FATAL: " + message, o);
  }

  public static void log(int level, String message, Object... o) {
    if (log.threshold <= level) {
      if (o.length > 0) {
        StringBuilder sb = new StringBuilder(message);
        sb.append(" -> ");
        for (Object obj : o) {
          if (obj == null) {
            sb.append("null");
          } else if (obj instanceof Throwable throwable) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            throwable.printStackTrace(pw);
            sb.append(sw);
          } else {
            sb.append(obj);
          }
          sb.append(" | ");
        }
        log.log(sb.toString());
      } else {
        log.log(message);
      }
    }
  }

  public static void setThreshold(int t) {
    log.threshold = t;
  }

  private void log(String s) {
    System.out.println(s);
  }
}
