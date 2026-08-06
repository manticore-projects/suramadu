package com.sun.prism.web;

import com.sun.glass.ui.Screen;
import com.sun.javafx.font.FontFactory;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.ResourceFactory;
import com.sun.prism.sw.SWPipeline;
import org.webswing.javafx.toolkit.WebsinwgFxToolkitFactory;

import java.util.HashMap;
import java.util.List;

public class WEBPipeline extends GraphicsPipeline {

  @Override
  public boolean init() {
    return true;
  }

  private static WEBPipeline theInstance;
  private static GraphicsPipeline pipeline;

  private WEBPipeline() {}

  public static WEBPipeline getInstance() {
    if (theInstance == null) {
      theInstance = new WEBPipeline();
      pipeline = SWPipeline.getInstance();
    }
    return theInstance;
  }

  private final HashMap<Integer, WebResourceFactory> factories =
      new HashMap<Integer, WebResourceFactory>(1);

  @Override
  public int getAdapterOrdinal(Screen screen) {
    return Screen.getScreens().indexOf(screen);
  }

  @Override
  public ResourceFactory getResourceFactory(Screen screen) {
    Integer index = Integer.valueOf(screen.getAdapterOrdinal());
    WebResourceFactory factory = factories.get(index);
    if (factory == null) {
      ResourceFactory originalFactory = pipeline.getResourceFactory(screen);
      factory = new WebResourceFactory(screen, originalFactory);
      factories.put(index, factory);
    }
    return factory;
  }

  @Override
  public ResourceFactory getDefaultResourceFactory(List<Screen> screens) {
    return getResourceFactory(Screen.getMainScreen());
  }

  @Override
  public boolean is3DSupported() {
    return false;
  }

  @Override
  public boolean isVsyncSupported() {
    return false;
  }

  @Override
  public boolean supportsShaderType(ShaderType type) {
    return false;
  }

  @Override
  public boolean supportsShaderModel(ShaderModel model) {
    return false;
  }

  @Override
  public void dispose() {
    super.dispose();
    pipeline.dispose();
  }

  private FontFactory webFontFactory;

  public FontFactory getFontFactory() {
    if (webFontFactory == null) {
      webFontFactory =
          WebsinwgFxToolkitFactory.getFactory().createWebFontFactory(pipeline.getFontFactory());
    }
    return webFontFactory;
  }

  @Override
  public boolean isUploading() {
    return true;
  }
}
