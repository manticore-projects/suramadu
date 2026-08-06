package org.webswing.server.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * PWA manifest
 **/
public class Manifest {

  private String shortName;
  private String name;
  private List<ManifestIcons> icons = new ArrayList<ManifestIcons>();
  private String startUrl;
  private String backgroundColor;
  private String display = "fullscreen";
  private String scope;
  private String themeColor;

  /**
   **/
  public Manifest shortName(String shortName) {
    this.shortName = shortName;
    return this;
  }

  @JsonProperty("short_name")
  public String getShortName() {
    return shortName;
  }

  public void setShortName(String shortName) {
    this.shortName = shortName;
  }

  /**
   **/
  public Manifest name(String name) {
    this.name = name;
    return this;
  }

  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   **/
  public Manifest icons(List<ManifestIcons> icons) {
    this.icons = icons;
    return this;
  }

  @JsonProperty("icons")
  public List<ManifestIcons> getIcons() {
    return icons;
  }

  public void setIcons(List<ManifestIcons> icons) {
    this.icons = icons;
  }

  /**
   **/
  public Manifest startUrl(String startUrl) {
    this.startUrl = startUrl;
    return this;
  }

  @JsonProperty("start_url")
  public String getStartUrl() {
    return startUrl;
  }

  public void setStartUrl(String startUrl) {
    this.startUrl = startUrl;
  }

  /**
   **/
  public Manifest backgroundColor(String backgroundColor) {
    this.backgroundColor = backgroundColor;
    return this;
  }

  @JsonProperty("background_color")
  public String getBackgroundColor() {
    return backgroundColor;
  }

  public void setBackgroundColor(String backgroundColor) {
    this.backgroundColor = backgroundColor;
  }

  /**
   **/
  public Manifest display(String display) {
    this.display = display;
    return this;
  }

  @JsonProperty("display")
  public String getDisplay() {
    return display;
  }

  public void setDisplay(String display) {
    this.display = display;
  }

  /**
   **/
  public Manifest scope(String scope) {
    this.scope = scope;
    return this;
  }

  @JsonProperty("scope")
  public String getScope() {
    return scope;
  }

  public void setScope(String scope) {
    this.scope = scope;
  }

  /**
   **/
  public Manifest themeColor(String themeColor) {
    this.themeColor = themeColor;
    return this;
  }

  @JsonProperty("theme_color")
  public String getThemeColor() {
    return themeColor;
  }

  public void setThemeColor(String themeColor) {
    this.themeColor = themeColor;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Manifest manifest = (Manifest) o;
    return Objects.equals(this.shortName, manifest.shortName)
        && Objects.equals(this.name, manifest.name) && Objects.equals(this.icons, manifest.icons)
        && Objects.equals(this.startUrl, manifest.startUrl)
        && Objects.equals(this.backgroundColor, manifest.backgroundColor)
        && Objects.equals(this.display, manifest.display)
        && Objects.equals(this.scope, manifest.scope)
        && Objects.equals(this.themeColor, manifest.themeColor);
  }

  @Override
  public int hashCode() {
    return Objects.hash(shortName, name, icons, startUrl, backgroundColor, display, scope,
        themeColor);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Manifest {\n");

    sb.append("    shortName: ").append(toIndentedString(shortName)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    icons: ").append(toIndentedString(icons)).append("\n");
    sb.append("    startUrl: ").append(toIndentedString(startUrl)).append("\n");
    sb.append("    backgroundColor: ").append(toIndentedString(backgroundColor)).append("\n");
    sb.append("    display: ").append(toIndentedString(display)).append("\n");
    sb.append("    scope: ").append(toIndentedString(scope)).append("\n");
    sb.append("    themeColor: ").append(toIndentedString(themeColor)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
