package org.webswing.util;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

public class CpuMonitor {
  static long previousCPUTime;
  static long previousTime;

  static Method getProcessCpuTimeMethod;

  static {
    try {
      Class<?> osMXBeanClass = Class.forName("com.sun.management.OperatingSystemMXBean");
      getProcessCpuTimeMethod = osMXBeanClass.getMethod("getProcessCpuTime");
    } catch (ReflectiveOperationException sun) {
      try {
        Class<?> osMXBeanClass = Class.forName("com.ibm.lang.management.OperatingSystemMXBean");
        getProcessCpuTimeMethod = osMXBeanClass.getMethod("getProcessCpuTime");
      } catch (ReflectiveOperationException ibm) {
        getProcessCpuTimeMethod = null;
        AppLogger.warn("Class not found to monitor CPU utilization. Monitoring CPU disabled.", sun);
      }
    }
  }

  public static double getCpuUtilization() {
    if (getProcessCpuTimeMethod == null) {
      return -1;
    }

    try {
      OperatingSystemMXBean osMXBean = ManagementFactory.getOperatingSystemMXBean();
      long currentCpuTime = (Long) getProcessCpuTimeMethod.invoke(osMXBean);

      long now = ManagementFactory.getRuntimeMXBean().getUptime();

      long cpuTimeDelta = currentCpuTime - previousCPUTime;
      previousCPUTime = currentCpuTime;

      long timeDelta = now - previousTime;
      previousTime = now;

      int processors = Runtime.getRuntime().availableProcessors();
      if (timeDelta == 0 || processors == 0) {
        return 0;
      }
      double cpuUsage = (double) TimeUnit.NANOSECONDS.toMillis(cpuTimeDelta) / (double) timeDelta;
      cpuUsage = cpuUsage / processors;
      return Math.max(0, cpuUsage) * 100;
    } catch (Throwable e) {
      getProcessCpuTimeMethod = null;
      AppLogger.warn("Failed to load CPU utilization. Monitoring CPU disabled.", e);
      return -1;
    }
  }

  private CpuMonitor() {}
}
