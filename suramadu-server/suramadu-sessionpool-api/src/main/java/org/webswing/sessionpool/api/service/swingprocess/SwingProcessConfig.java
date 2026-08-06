package org.webswing.sessionpool.api.service.swingprocess;

import org.webswing.sessionpool.api.service.swingprocess.impl.SwingProcessServiceImpl.SessionLogAppenderParams;

import java.util.HashMap;
import java.util.Map;

public class SwingProcessConfig {
  private String path;
  private String name;
  private String applicationName;
  private String jreExecutable;
  private String baseDir;
  private String mainClass;
  private String classPath;
  private String jvmArgs;
  private final Map<String, String> properties = new HashMap<String, String>();
  private String args;
  private SessionLogAppenderParams sessionLogAppenderParams;

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getApplicationName() {
    return applicationName;
  }

  public void setApplicationName(String applicationName) {
    this.applicationName = applicationName;
  }

  public String getJreExecutable() {
    return jreExecutable;
  }

  public void setJreExecutable(String jreExecutable) {
    this.jreExecutable = jreExecutable;
  }

  public String getBaseDir() {
    return baseDir;
  }

  public void setBaseDir(String baseDir) {
    this.baseDir = baseDir;
  }

  public String getMainClass() {
    return mainClass;
  }

  public void setMainClass(String mainClass) {
    this.mainClass = mainClass;
  }

  public String getClassPath() {
    return classPath;
  }

  public void setClassPath(String classPath) {
    this.classPath = classPath;
  }

  public String getJvmArgs() {
    return jvmArgs;
  }

  public void setJvmArgs(String jvmArgs) {
    this.jvmArgs = jvmArgs;
  }

  public Map<String, String> getProperties() {
    return properties;
  }

  public void addProperty(String name, String value) {
    if (value == null) {
      properties.remove(name);
    } else {
      properties.put(name, value);
    }
  }

  public String getArgs() {
    return args;
  }

  public void setArgs(String args) {
    this.args = args;
  }

  public void addProperty(String name) {
    this.properties.put(name, null);
  }

  public void addProperty(String name, boolean value) {
    addProperty(name, Boolean.toString(value));
  }

  public void addProperty(String name, int value) {
    addProperty(name, Integer.toString(value));
  }

  public SessionLogAppenderParams getSessionLogAppenderParams() {
    return sessionLogAppenderParams;
  }

  public void setSessionLogAppenderParams(SessionLogAppenderParams sessionLogAppenderParams) {
    this.sessionLogAppenderParams = sessionLogAppenderParams;
  }

}
