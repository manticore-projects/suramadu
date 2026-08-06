package org.webswing.server.common.model.meta;

public enum ConfigGroup {
  General("General"), Java("Java"), Extension("Extensions"), Server("Server"), Swing(
      "Swing"), Session("Session"), Logging("Logging"), Features("Features");

  String label;

  ConfigGroup(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }

}
