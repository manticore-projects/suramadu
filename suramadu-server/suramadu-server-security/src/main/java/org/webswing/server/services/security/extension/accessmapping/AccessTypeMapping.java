package org.webswing.server.services.security.extension.accessmapping;

import org.webswing.server.common.model.Config;
import org.webswing.server.common.model.meta.*;
import org.webswing.server.common.model.security.WebswingAction;

import java.util.ArrayList;
import java.util.List;

@ConfigFieldOrder({"accessType", "everyone", "roles", "users"})
public interface AccessTypeMapping extends Config {

  @ConfigField(label = "Access Type",
      description = "Webswing Access type. Admin: can do everything. Support: can only see everything. Basic: regular user access.")
  WebswingAction.AccessType getAccessType();

  @ConfigField(label = "Everyone?", description = "Every authenticated user has this Access type.")
  @ConfigFieldDefaultValueBoolean(false)
  boolean isEveryone();

  @ConfigField(label = "Roles", description = "User Roles that has the defined Access type.")
  @ConfigFieldDefaultValueObject(ArrayList.class)
  @ConfigFieldVariables
  List<String> getRoles();

  @ConfigField(label = "Users", description = "Users that has the defined Access type.")
  @ConfigFieldDefaultValueObject(ArrayList.class)
  @ConfigFieldVariables
  List<String> getUsers();

}
