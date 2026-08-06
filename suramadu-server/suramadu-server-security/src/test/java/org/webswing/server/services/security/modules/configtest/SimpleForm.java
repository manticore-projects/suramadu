package org.webswing.server.services.security.modules.configtest;

import org.webswing.server.common.model.Config;
import org.webswing.server.common.model.meta.*;

import java.util.ArrayList;
import java.util.List;

@ConfigFieldOrder({"string", "boolean", "stringList"})
public interface SimpleForm extends Config {
  @ConfigField(label = "String", description = "This field demonstrates simple String text input.")
  @ConfigFieldDefaultValueString("Default string value")
  String getString();

  @ConfigField(label = "Boolean", description = "This field demonstrates simple Boolean switch.")
  @ConfigFieldDefaultValueBoolean(false)
  boolean isBoolean();

  @ConfigField(label = "String List", description = "This is a list of strings.")
  @ConfigFieldDefaultValueObject(ArrayList.class)
  @ConfigFieldPresets(enumClass = OptionsEnum.class)
  List<String> getStringList();

  public enum OptionsEnum {
    Option1, Option2, Option3
  }
}
