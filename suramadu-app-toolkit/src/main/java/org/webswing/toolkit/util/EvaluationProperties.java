package org.webswing.toolkit.util;

public class EvaluationProperties {

  private final boolean enabled;
  private final String mainText;
  private final String linkText;
  private final String linkUrl;
  private final long timeout;
  private final String dismissText;
  private final int height;

  public EvaluationProperties(boolean enabled, String mainText, String linkText, String linkUrl,
      long timeout, String dismissText, int height) {
    super();
    this.enabled = enabled;
    this.mainText = mainText;
    this.linkText = linkText;
    this.linkUrl = linkUrl;
    this.timeout = timeout;
    this.dismissText = dismissText;
    this.height = height;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public String getMainText() {
    return mainText;
  }

  public String getLinkText() {
    return linkText;
  }

  public String getLinkUrl() {
    return linkUrl;
  }

  public long getTimeout() {
    return timeout;
  }

  public String getDismissText() {
    return dismissText;
  }

  public int getHeight() {
    return height;
  }

}
