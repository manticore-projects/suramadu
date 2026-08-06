package org.webswing.server.common.model;

import org.webswing.server.common.model.meta.ConfigField;
import org.webswing.server.common.model.meta.ConfigFieldDefaultValueObject;
import org.webswing.server.common.model.meta.ConfigFieldOrder;
import org.webswing.server.common.model.meta.ConfigType;

@ConfigType(metadataGenerator = SecuredPathConfig.SecuredPathConfigurationMetadataGenerator.class)
@ConfigFieldOrder({"enabled", "path", "name", "homeDir", "webFolder", "restrictedResources",
    "langFolder", "icon", "security", "allowedCorsOrigins", /*"adminConsoleUrl",*/
    "uploadMaxSize", "maxClients", "sessionMode", "monitorEdtEnabled", "loadingAnimationDelay",
    "allowStealSession", "autoLogout", "goodbyeUrl", "allowStatisticsLogging", "swingConfig"})
public interface CompoundSecuredPathConfig extends SecuredPathConfig {

  @ConfigField(label = "Application")
  @ConfigFieldDefaultValueObject
  SwingConfig getSwingConfig();

}
