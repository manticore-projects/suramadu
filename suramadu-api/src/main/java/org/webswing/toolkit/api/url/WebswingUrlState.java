package org.webswing.toolkit.api.url;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Object representing the URL State. State is encoded to hash Fragment part of URL as set of
 * parameters
 */
public class WebswingUrlState {

  String path;
  Map<String, String> parameters = new LinkedHashMap<String, String>();

  public WebswingUrlState() {}

  public WebswingUrlState(String path) {
    this.path = path;
  }

  public WebswingUrlState(WebswingUrlState path) {
    this.path = path.path;
    if (path.getParameters() != null) {
      this.parameters.putAll(path.getParameters());
    }
  }

  public WebswingUrlState(String path, Map<String, String> parameters) {
    this.path = path;
    if (parameters != null) {
      this.parameters.putAll(parameters);
    }
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public Map<String, String> getParameters() {
    return parameters;
  }

  public void setParameters(Map<String, String> parameters) {
    this.parameters = parameters;
  }

  public boolean isEmpty() {
    return path == null && (parameters == null || parameters.isEmpty());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    WebswingUrlState that = (WebswingUrlState) o;

    if (path != null ? !path.equals(that.path) : that.path != null) {
      return false;
    }
    return parameters != null ? parameters.equals(that.parameters) : that.parameters == null;
  }

  @Override
  public int hashCode() {
    int result = path != null ? path.hashCode() : 0;
    result = 31 * result + (parameters != null ? parameters.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "WebswingUrlState{" + "path='" + path + '\'' + ", parameters=" + parameters + '}';
  }
}
