package org.webswing.server.services.security.extension.accessmapping;

import org.webswing.server.common.model.meta.ConfigField;
import org.webswing.server.common.model.meta.ConfigFieldDefaultValueObject;
import org.webswing.server.common.model.meta.ConfigFieldEditorType;
import org.webswing.server.common.model.meta.ConfigFieldEditorType.EditorType;
import org.webswing.server.services.security.extension.api.SecurityModuleExtensionConfig;

import java.util.ArrayList;
import java.util.List;

public interface AccessMappingExtensionConfig extends SecurityModuleExtensionConfig {

  @ConfigField(label = "Webswing Access Mapping",
      description = "Overrides the default Roles to Access Type mapping.")
  @ConfigFieldEditorType(editor = EditorType.ObjectListAsTable)
  @ConfigFieldDefaultValueObject(ArrayList.class)
  List<AccessTypeMapping> getAccessMapping();

}
