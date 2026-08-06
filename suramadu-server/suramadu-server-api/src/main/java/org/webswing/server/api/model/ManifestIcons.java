package org.webswing.server.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


public class ManifestIcons {

  private String src;
  private String type = "image/png";
  private String sizes;

  /**
   **/
  public ManifestIcons src(String src) {
    this.src = src;
    return this;
  }


  @JsonProperty("src")
  public String getSrc() {
    return src;
  }

  public void setSrc(String src) {
    this.src = src;
  }

  /**
   **/
  public ManifestIcons type(String type) {
    this.type = type;
    return this;
  }


  @JsonProperty("type")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  /**
   **/
  public ManifestIcons sizes(String sizes) {
    this.sizes = sizes;
    return this;
  }


  @JsonProperty("sizes")
  public String getSizes() {
    return sizes;
  }

  public void setSizes(String sizes) {
    this.sizes = sizes;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ManifestIcons manifestIcons = (ManifestIcons) o;
    return Objects.equals(this.src, manifestIcons.src)
        && Objects.equals(this.type, manifestIcons.type)
        && Objects.equals(this.sizes, manifestIcons.sizes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(src, type, sizes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ManifestIcons {\n");

    sb.append("    src: ").append(toIndentedString(src)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    sizes: ").append(toIndentedString(sizes)).append("\n");
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
