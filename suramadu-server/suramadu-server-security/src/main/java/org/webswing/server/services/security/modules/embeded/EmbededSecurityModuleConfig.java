package org.webswing.server.services.security.modules.embeded;

import org.webswing.server.common.model.meta.*;
import org.webswing.server.services.security.extension.api.WebswingExtendableSecurityModuleConfig;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@ConfigType(metadataGenerator = EmbededSecurityModuleConfig.MetadataGenerator.class)
@ConfigFieldOrder({"users"})
public interface EmbededSecurityModuleConfig extends WebswingExtendableSecurityModuleConfig {

  @ConfigField(label = "Users", description = "User definitions")
  @ConfigFieldEditorType(editor = ConfigFieldEditorType.EditorType.ObjectListAsTable)
  @ConfigFieldDefaultValueObject(ArrayList.class)
  @ConfigFieldDiscriminator
  List<EmbededUserEntry> getUsers();

  public class MetadataGenerator extends ExtensionMetadataGenerator<EmbededSecurityModuleConfig> {
    @Override
    protected Object getValue(EmbededSecurityModuleConfig config, ClassLoader cl,
        String propertyName, Method readMethod) throws Exception {
      Object value = super.getValue(config, cl, propertyName, readMethod);
      if ("users".equals(propertyName) && value instanceof List) {
        List<EmbededUserEntry> users = (List<EmbededUserEntry>) value;
        for (EmbededUserEntry user : users) {
          if (user.getPassword() != null && !user.getPassword().startsWith(HashUtil.PREFIX)) {
            user.asMap().put("password", HashUtil.hash(user.getPassword().toCharArray()));
          }
        }
      }
      return value;
    }

  }
}
