package org.webswing.server.services.security.modules.configtest;

import org.webswing.server.common.model.meta.*;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import static org.webswing.server.services.security.modules.configtest.ExampleForm.ExampleFormMetadataGenerator;

@ConfigFieldOrder({"string", "number", "boolean", "stringList", "stringMap", "aggregationType",
    "embededObject", "listOfObject", "mapOfObject", "listOfObjectAsTable", "stringWithEditor",
    "stringWithVars"})
@ConfigType(metadataGenerator = ExampleFormMetadataGenerator.class)
public interface ExampleForm {

  @ConfigField(label = "String", tab = ConfigGroup.General,
      description = "This field demonstrates simple String text input.")
  @ConfigFieldDefaultValueString("Default string value")
  String getString();

  @ConfigField(label = "String with variables", tab = ConfigGroup.Features,
      description = "This field demonstrates simple String text input.")
  @ConfigFieldVariables()
  String getStringWithVars();

  @ConfigField(label = "String with editor", tab = ConfigGroup.Features,
      description = "This field demonstrates simple String text input.")
  @ConfigFieldEditorType(editor = ConfigFieldEditorType.EditorType.Generic)
  String getStringWithEditor();

  @ConfigField(label = "Boolean", tab = ConfigGroup.General,
      description = "This field demonstrates simple Boolean switch.")
  @ConfigFieldDefaultValueBoolean(false)
  boolean isBoolean();

  @ConfigField(label = "Number", tab = ConfigGroup.General,
      description = "This field demonstrates simple Number input.")
  @ConfigFieldDefaultValueNumber(3)
  int getNumber();

  @ConfigField(label = "String List", tab = ConfigGroup.Server,
      description = "This is a list of strings.")
  @ConfigFieldDefaultValueObject(ArrayList.class)
  @ConfigFieldVariables()
  List<String> getStringList();

  @ConfigField(label = "String Map", tab = ConfigGroup.Server,
      description = "This is a map with presets.")
  @ConfigFieldPresets({"value1", "value2"})
  @ConfigFieldVariables()
  Map<String, String> getStringMap();

  @ConfigField(label = "Aggregation example", tab = ConfigGroup.Extension,
      description = "This field demonstrates enum based dropdown.")
  @ConfigFieldDiscriminator
  AggregationType getAggregationType();

  @ConfigField(label = "Embeded Object", tab = ConfigGroup.Extension,
      description = "Self embeded object")
  ComplexForm getEmbededObject();

  @ConfigField(label = "List of Objects", tab = ConfigGroup.Extension,
      description = "List of embeded object")
  List<SimpleForm> getListOfObject();

  @ConfigField(label = "List of Objects as Table", tab = ConfigGroup.Extension,
      description = "List of embeded object")
  @ConfigFieldEditorType(editor = ConfigFieldEditorType.EditorType.ObjectListAsTable)
  List<SimpleForm> getListOfObjectAsTable();

  @ConfigField(label = "Map of Objects", tab = ConfigGroup.Extension,
      description = "Map of embeded object")
  Map<String, SimpleForm> getMapOfObject();

  enum AggregationType {
    Object, ListOfObjects, MapOfObjects, ListOfObjectsAsTable
  }

  public static class ExampleFormMetadataGenerator extends MetadataGenerator<ExampleForm> {
    protected LinkedHashSet<String> getPropertyNames(ExampleForm config, ClassLoader cl)
        throws Exception {
      LinkedHashSet<String> names = super.getPropertyNames(config, cl);
      if (config.getAggregationType() == null) {
        names.remove("embededObject");
        names.remove("listOfObject");
        names.remove("mapOfObject");
        names.remove("listOfObjectAsTable");
      }
      if (config.getAggregationType() == AggregationType.Object) {
        names.remove("listOfObject");
        names.remove("mapOfObject");
        names.remove("listOfObjectAsTable");
      }
      if (config.getAggregationType() == AggregationType.ListOfObjects) {
        names.remove("embededObject");
        names.remove("mapOfObject");
        names.remove("listOfObjectAsTable");

      }
      if (config.getAggregationType() == AggregationType.MapOfObjects) {
        names.remove("embededObject");
        names.remove("listOfObject");
        names.remove("listOfObjectAsTable");
      }
      if (config.getAggregationType() == AggregationType.ListOfObjectsAsTable) {
        names.remove("embededObject");
        names.remove("listOfObject");
        names.remove("mapOfObject");
      }
      return names;
    }

  }
}
