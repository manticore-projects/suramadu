package org.webswing.toolkit.ge;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;

@SuppressWarnings("restriction")
public class WebScreenDevice extends GraphicsDevice {

  GraphicsDevice imageDevice;
  GraphicsConfiguration c;

  public WebScreenDevice(GraphicsDevice imageDevice, GraphicsConfiguration c) {
    this.imageDevice = imageDevice;
    this.c = c;
  }

  @Override
  public int getType() {
    return 0;
  }

  @Override
  public String getIDstring() {
    return imageDevice.getIDstring();
  }

  @Override
  public GraphicsConfiguration[] getConfigurations() {
    return new GraphicsConfiguration[] {c};
  }

  @Override
  public GraphicsConfiguration getDefaultConfiguration() {
    return c;
  }

}
