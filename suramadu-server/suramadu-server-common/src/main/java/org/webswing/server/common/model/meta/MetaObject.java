package org.webswing.server.common.model.meta;

import java.util.List;
import java.util.Map;

public class MetaObject {

  private String message;
  private List<MetaField> fields;
  private Map<String, Object> data;

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public List<MetaField> getFields() {
    return fields;
  }

  public void setFields(List<MetaField> fields) {
    this.fields = fields;
  }

  public Map<String, Object> getData() {
    return data;
  }

  public void setData(Map<String, Object> data) {
    this.data = data;
  }

}
