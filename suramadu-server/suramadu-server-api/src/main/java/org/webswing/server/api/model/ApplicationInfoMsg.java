package org.webswing.server.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class ApplicationInfoMsg {

  private String name;
  private String url;
  private byte[] base64Icon;

  /**
   **/
  public ApplicationInfoMsg name(String name) {
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
  public ApplicationInfoMsg url(String url) {
    this.url = url;
    return this;
  }

  @JsonProperty("url")
  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  /**
   **/
  public ApplicationInfoMsg base64Icon(byte[] base64Icon) {
    this.base64Icon = base64Icon;
    return this;
  }

  @JsonProperty("base64Icon")
  public byte[] getBase64Icon() {
    return base64Icon;
  }

  public void setBase64Icon(byte[] base64Icon) {
    this.base64Icon = base64Icon;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApplicationInfoMsg applicationInfoMsg = (ApplicationInfoMsg) o;
    return Objects.equals(this.name, applicationInfoMsg.name)
        && Objects.equals(this.url, applicationInfoMsg.url)
        && Objects.equals(this.base64Icon, applicationInfoMsg.base64Icon);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, url, base64Icon);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApplicationInfoMsg {\n");

    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    base64Icon: ").append(toIndentedString(base64Icon)).append("\n");
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
