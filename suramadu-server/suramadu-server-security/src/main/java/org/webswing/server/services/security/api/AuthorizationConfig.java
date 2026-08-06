package org.webswing.server.services.security.api;

import org.webswing.server.common.model.Config;
import org.webswing.server.common.model.meta.ConfigField;
import org.webswing.server.common.model.meta.ConfigFieldDefaultValueObject;
import org.webswing.server.common.model.meta.ConfigFieldOrder;
import org.webswing.server.common.model.meta.ConfigFieldVariables;

import java.util.ArrayList;
import java.util.List;

@ConfigFieldOrder({"users", "roles"})
public interface AuthorizationConfig extends Config {

  @ConfigField(label = "Authorized Users", description = "List of authorized user ids.")
  @ConfigFieldDefaultValueObject(ArrayList.class)
  @ConfigFieldVariables
  List<String> getUsers();

  @ConfigField(label = "Authorized Roles", description = "List of authorized roles.")
  @ConfigFieldDefaultValueObject(ArrayList.class)
  @ConfigFieldVariables
  List<String> getRoles();
}
