package org.webswing.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class NamedThreadFactory implements ThreadFactory {

  private final String name;
  private final boolean daemon;

  private NamedThreadFactory(String name, boolean daemon) {
    this.name = name;
    this.daemon = daemon;
  }

  public static ThreadFactory getInstance(String name) {
    return new NamedThreadFactory(name, true);
  }

  public static ThreadFactory getInstance(String name, boolean daemon) {
    return new NamedThreadFactory(name, daemon);
  }

  @Override
  public Thread newThread(Runnable r) {
    Thread t = Executors.defaultThreadFactory().newThread(r);
    t.setDaemon(daemon);
    t.setName(name);
    return t;
  }
}
