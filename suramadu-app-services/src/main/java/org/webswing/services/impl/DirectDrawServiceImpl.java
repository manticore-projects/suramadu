package org.webswing.services.impl;

import org.webswing.directdraw.DirectDraw;
import org.webswing.directdraw.toolkit.VolatileWebImageWrapper;
import org.webswing.directdraw.toolkit.WebImage;
import org.webswing.ext.services.DirectDrawService;
import org.webswing.services.impl.ddutil.FastDirectDrawServicesAdapter;

import java.awt.Image;
import java.awt.ImageCapabilities;
import java.awt.image.VolatileImage;

public class DirectDrawServiceImpl implements DirectDrawService {

  private static DirectDrawServiceImpl impl;
  protected DirectDraw dd = new DirectDraw(new FastDirectDrawServicesAdapter());

  public static DirectDrawServiceImpl getInstance() {
    if (impl == null) {
      impl = new DirectDrawServiceImpl();
    }
    return impl;
  }

  private DirectDrawServiceImpl() {}

  @Override
  public Image createImage(int width, int height) {
    return dd.createImage(width, height);
  }

  @Override
  public Image extractWebImage(Image webImage) {
    if (webImage instanceof WebImage image) {
      return image.extractReadOnlyWebImage(true);
    } else if (webImage instanceof VolatileWebImageWrapper wrapper) {
      return wrapper.getWebImage().extractReadOnlyWebImage(true);
    }

    return null;
  }

  @Override
  public void resetCache() {
    dd.resetConstantCache();
  }

  @Override
  public byte[] buildWebImage(Image webImage) {
    if (webImage instanceof WebImage image) {
      return image.toMessage(dd).toByteArray();
    } else if (webImage instanceof VolatileWebImageWrapper wrapper) {
      return wrapper.getWebImage().toMessage(dd).toByteArray();
    }
    return null;
  }

  @Override
  public boolean isDirty(Image webImage) {
    if (webImage instanceof WebImage image) {
      return image.isDirty();
    } else if (webImage instanceof VolatileWebImageWrapper wrapper) {
      return wrapper.getWebImage().isDirty();
    }
    return false;
  }

  @Override
  public VolatileImage createVolatileImage(int width, int height, ImageCapabilities caps,
      int transparency) {
    return dd.createVolatileImage(width, height, caps);
  }

  @Override
  public void resetImage(Image webImage) {
    if (webImage instanceof WebImage image) {
      image.reset();
    } else if (webImage instanceof VolatileWebImageWrapper wrapper) {
      wrapper.getWebImage().reset();
    }
  }

  @Override
  public void resetImageBeforeRepaint(Image webImage) {
    if (webImage instanceof WebImage image) {
      image.resetBeforeRepaint();
    } else if (webImage instanceof VolatileWebImageWrapper wrapper) {
      wrapper.getWebImage().resetBeforeRepaint();
    }
  }
}
