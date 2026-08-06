package org.webswing.server.services.security.extension.api;

import org.webswing.server.common.model.meta.*;
import org.webswing.server.common.model.meta.ConfigFieldEditorType.EditorType;
import org.webswing.server.services.security.api.WebswingSecurityModuleConfig;
import org.webswing.server.services.security.extension.api.WebswingExtendableSecurityModuleConfig.ExtensionMetadataGenerator;

import java.util.LinkedHashSet;
import java.util.List;

@ConfigType(metadataGenerator = ExtensionMetadataGenerator.class)
public interface WebswingExtendableSecurityModuleConfig extends WebswingSecurityModuleConfig {

  @ConfigField(tab = ConfigGroup.Extension, label = "Extensions",
      description = "List of security extensions enabled. Use one of build in names or custom class name.")
  @ConfigFieldDiscriminator
  @ConfigFieldPresets(enumClass = BuiltInModuleExtensions.class)
  List<String> getExtensions();

  public static class ExtensionMetadataGenerator<T extends WebswingExtendableSecurityModuleConfig>
      extends MetadataGenerator<T> {
    @Override
    protected LinkedHashSet<String> getPropertyNames(T config, ClassLoader cl) throws Exception {
      LinkedHashSet<String> propertyNames = super.getPropertyNames(config, cl);
      List<String> extensions = config.getExtensions();
      if (extensions != null) {
        propertyNames.addAll(extensions);
      }
      return propertyNames;
    }

    @Override
    protected MetaField getPropertyMetadata(T config, ClassLoader cl, String propertyName)
        throws Exception {
      if (config.getExtensions() != null && config.getExtensions().contains(propertyName)) {
        String extensionType = BuiltInModuleExtensions.getExtensionClassName(propertyName);
        try {
          Class<?> extensionClass = cl.loadClass(extensionType);
          Class<?> configType = getConfigTypeFromConstructor(extensionClass);
          Object value = config.getValueAs(propertyName, configType);
          MetaField metadata = new MetaField();
          metadata.setName(propertyName);
          metadata.setTab(ConfigGroup.Extension);
          metadata.setLabel(propertyName);
          metadata.setType(EditorType.Object);
          metadata.setValue(toMetaObject(config, cl, value, configType));
          return metadata;
        } catch (Throwable e) {
          return null;
        }
      } else {
        return super.getPropertyMetadata(config, cl, propertyName);
      }
    }
  }
}
