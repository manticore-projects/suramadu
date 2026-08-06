package org.webswing.server.services.security.modules.embeded;

import org.webswing.server.common.model.Config;
import org.webswing.server.common.model.meta.ConfigField;
import org.webswing.server.common.model.meta.ConfigFieldDefaultValueObject;
import org.webswing.server.common.model.meta.ConfigFieldOrder;
import org.webswing.server.common.model.meta.ConfigFieldVariables;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vikto on 06-Feb-17.
 */
@ConfigFieldOrder({"username",})
public interface EmbededUserEntry extends Config {

  @ConfigField(label = "Username")
  @ConfigFieldVariables
  String getUsername();

  @ConfigField(label = "Password")
  @ConfigFieldVariables
  String getPassword();

  @ConfigField(label = "Roles")
  @ConfigFieldDefaultValueObject(ArrayList.class)
  @ConfigFieldVariables
  List<String> getRoles();

}
