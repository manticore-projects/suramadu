package org.webswing.toolkit.util;

import org.webswing.ext.services.*;
import org.webswing.util.AppLogger;

public class Services {

  private static ImageService imageService;
  private static PdfService pdfService;
  private static ServerConnectionService serverService;
  private static DataStoreService dataStoreService;
  private static SwingClassLoaderFactoryService classloaderService;
  private static DirectDrawService directDrawService;
  private static JsLinkService jsLinkService;
  private static ToolkitFXService toolkitFXService;

  public static void initialize(ImageService imageServiceImpl, PdfService pdfServiceImpl,
      ServerConnectionService serverServiceImpl, DataStoreService dataStoreServiceImpl,
      SwingClassLoaderFactoryService classloaderServiceImpl,
      DirectDrawService directDrawServiceImpl, JsLinkService jsLinkServiceImpl) {
    imageService = imageServiceImpl;
    pdfService = pdfServiceImpl;
    serverService = serverServiceImpl;
    dataStoreService = dataStoreServiceImpl;
    classloaderService = classloaderServiceImpl;
    directDrawService = directDrawServiceImpl;
    jsLinkService = jsLinkServiceImpl;
  }

  public static ImageService getImageService() {
    if (imageService == null) {
      AppLogger.fatal("ImageService has not been initialized. Exiting...");
      System.exit(1);
    }
    return imageService;
  }

  public static PdfService getPdfService() {
    if (pdfService == null) {
      AppLogger.fatal("PdfService has not been initialized. Exiting...");
      System.exit(1);
    }
    return pdfService;
  }

  public static ServerConnectionService getConnectionService() {
    if (serverService == null) {
      AppLogger.fatal("Connection service has not been initialized. Exiting...");
      System.exit(1);
    }
    return serverService;
  }

  public static DataStoreService getDataStoreService() {
    if (dataStoreService == null) {
      AppLogger.fatal("Data store service has not been initialized. Exiting...");
      System.exit(1);
    }
    return dataStoreService;
  }

  public static SwingClassLoaderFactoryService getClassLoaderService() {
    if (classloaderService == null) {
      AppLogger.fatal("Classloader service has not been initialized. Exiting...");
      System.exit(1);
    }
    return classloaderService;
  }

  public static DirectDrawService getDirectDrawService() {
    if (directDrawService == null) {
      AppLogger.fatal("DirectDraw service has not been initialized. Exiting...");
      System.exit(1);
    }
    return directDrawService;
  }

  public static JsLinkService getJsLinkService() {
    if (jsLinkService == null) {
      AppLogger.fatal("JsLinkService service has not been initialized. Exiting...");
      System.exit(1);
    }
    return jsLinkService;
  }

  public static ToolkitFXService getToolkitFXService() {
    if (toolkitFXService == null) {
      AppLogger.debug("ToolkitFXService service has not been initialized. Ignoring...");
    }
    return toolkitFXService;
  }

  public static void initializeToolkitFXService(ToolkitFXService toolkitFXService) {
    Services.toolkitFXService = toolkitFXService;
  }

  private Services() {}

}
