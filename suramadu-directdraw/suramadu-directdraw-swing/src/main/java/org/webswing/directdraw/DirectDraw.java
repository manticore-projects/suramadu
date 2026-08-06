package org.webswing.directdraw;

import org.webswing.directdraw.model.FontFaceConst;
import org.webswing.directdraw.toolkit.DrawInstructionFactory;
import org.webswing.directdraw.toolkit.VolatileWebImageWrapper;
import org.webswing.directdraw.toolkit.WebImage;
import org.webswing.directdraw.util.DirectDrawUtils;
import org.webswing.directdraw.util.DrawConstantPool;

import java.awt.Font;
import java.awt.ImageCapabilities;

public class DirectDraw {

  public static final String SERVER_SIDE_GRADIENTS = "directdraw.serverSideGradients";
  public static final String DRAW_STRING_AS_PATH = "directdraw.drawStringAsPath";
  public static final String FONTS_PROVIDED = "directdraw.fontsProvided";

  private DirectDrawServicesAdapter services = new DirectDrawServicesAdapter();
  private DrawInstructionFactory instructionFactory = new DrawInstructionFactory(this);

  private DrawConstantPool constantPool;

  public DirectDraw() {
    resetConstantCache();
  }

  public DirectDraw(DirectDrawServicesAdapter services) {
    this();
    this.services = services;
  }

  public void resetConstantCache() {
    constantPool = new DrawConstantPool();
  }

  public DrawConstantPool getConstantPool() {
    return constantPool;
  }

  public DirectDrawServicesAdapter getServices() {
    return services;
  }

  public WebImage createImage(int w, int h) {
    return new WebImage(this, w, h);
  }

  public VolatileWebImageWrapper createVolatileImage(int w, int h, ImageCapabilities caps) {
    return new VolatileWebImageWrapper(caps, createImage(w, h));
  }

  public DrawInstructionFactory getInstructionFactory() {
    return instructionFactory;
  }

  public boolean requestFont(Font font) {
    String file = getServices().getFileForFont(font);
    if (file != null) {
      if (!getConstantPool().isFontRegistered(file)) {
        getConstantPool().requestFont(file, new FontFaceConst(this, font));
      }
      return true;
    } else {
      // use native rendering for logical fonts even if not set up in font config
      return DirectDrawUtils.WEB_FONTS.containsKey(font.getFamily());
    }
  }

}
