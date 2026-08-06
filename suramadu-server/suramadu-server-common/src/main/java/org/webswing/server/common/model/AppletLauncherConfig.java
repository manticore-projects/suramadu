package org.webswing.server.common.model;

import org.webswing.server.common.model.meta.*;

import java.util.Map;

@ConfigType
@ConfigFieldOrder({"appletClass", "parameters"})
public interface AppletLauncherConfig {

  @ConfigField(label = "Applet Class",
      description = "Applet's fully qualiffied main class name. Must implement the java.applet.Applet interface. (ie. 'com.mypackage.MyApplet')")
  @ConfigFieldVariables(VariableSetName.SwingInstance)
  public String getAppletClass();

  @ConfigField(label = "Applet Parameters",
      description = "Applets start parameters. Set of key-value pairs accessible to applet at startup.")
  @ConfigFieldVariables(VariableSetName.SwingInstance)
  public Map<String, String> getParameters();
}
