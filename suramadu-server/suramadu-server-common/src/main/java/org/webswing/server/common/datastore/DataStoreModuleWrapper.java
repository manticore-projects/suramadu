package org.webswing.server.common.datastore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.server.common.extension.ExtensionClassLoader;
import org.webswing.server.common.util.CommonUtil;
import org.webswing.server.common.util.ConfigUtil;
import org.webswing.server.model.exception.WsInitException;
import org.webswing.util.ClasspathUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.concurrent.Callable;

public class DataStoreModuleWrapper implements WebswingDataStoreModule {

  private static final Logger log = LoggerFactory.getLogger(DataStoreModuleWrapper.class);

  private final WebswingDataStoreConfig config;
  private WebswingDataStoreModule custom;
  private URLClassLoader customCL;

  public DataStoreModuleWrapper(WebswingDataStoreConfig config) {
    this.config = config;
    init();
  }

  private void init() {
    try {
      String classPath = CommonUtil.generateClassPathString(config.getClassPath());
      URL[] urls = ClasspathUtil.populateClassPath(classPath, resolveFile("."));
      customCL = new URLClassLoader(urls, new ExtensionClassLoader());
      String dataStoreModuleClassName =
          BuiltInDataStoreModules.getDataStoreModuleClassName(config.getModule());
      Class<?> moduleClass = customCL.loadClass(dataStoreModuleClassName);

      Constructor<?> defaultConstructor = null;
      Constructor<?> configConstructor = null;
      for (Constructor<?> constructor : moduleClass.getConstructors()) {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        if (parameterTypes.length == 1) {
          if (WebswingDataStoreModuleConfig.class.isAssignableFrom(parameterTypes[0])) {
            configConstructor = constructor;
            break;
          }
        } else if (parameterTypes.length == 0) {
          defaultConstructor = constructor;
        }
      }
      Exception ex = null;
      if (configConstructor != null) {
        Class<?> configClass = configConstructor.getParameterTypes()[0];
        try {
          custom = (WebswingDataStoreModule) configConstructor
              .newInstance(ConfigUtil.instantiateConfig(config.getConfig(), configClass));
        } catch (Exception e) {
          ex = new WsInitException(
              "Could not construct custom dataStore module class (using WebswingDataStoreModuleConfig constructor)!",
              e);
          log.error("Initialization failed.", ex);
        }
      }
      if (custom == null && defaultConstructor != null) {
        try {
          custom = (WebswingDataStoreModule) defaultConstructor.newInstance();
        } catch (Exception e) {
          ex = new WsInitException(
              "Could not construct custom dataStore module class (using Default constructor)!", e);
          log.error("Initialization failed.", ex);
        }
      }
      if (custom == null) {
        log.error(
            "Custom dataStore module class should define a default or WebswingDataStoreModuleConfig constructor!");
        throw ex;
      }
    } catch (Exception e) {
      log.error("Failed to initialize dataStore module!", e);
      throw new RuntimeException("Failed to initialize dataStore module!", e);
    }
  }

  private String resolveFile(String name) {
    File absolute = new File(name).getAbsoluteFile();
    if (absolute.exists()) {
      return absolute.getAbsolutePath();
    }
    return null;
  }

  @Override
  public InputStream readData(String type, String id) {
    if (custom != null) {
      try {
        return runWithContextClassLoader(() -> {
          try {
            return custom.readData(type, id);
          } catch (Throwable e) {
            throw new Exception(e);
          }
        });
      } catch (Exception e) {
        log.error("Read data by DataStoreModule failed!", e);
      }
    }
    return null;
  }

  @Override
  public InputStream readData(String type, String id, long timeoutMillis) throws IOException {
    if (custom != null) {
      try {
        return runWithContextClassLoader(() -> {
          try {
            return custom.readData(type, id, timeoutMillis);
          } catch (Throwable e) {
            throw new Exception(e);
          }
        });
      } catch (Exception e) {
        log.error("Read data by DataStoreModule failed!", e);
      }
    }
    return null;
  }

  @Override
  public void storeData(String type, String id, InputStream is, boolean deleteIfExists) {
    if (custom != null) {
      try {
        runWithContextClassLoader(() -> {
          try {
            custom.storeData(type, id, is, deleteIfExists);
          } catch (Throwable e) {
            throw new Exception(e);
          }
          return null;
        });
      } catch (Exception e) {
        log.error("Store data by DataStoreModule failed!", e);
      }
    }
  }

  @Override
  public boolean dataExists(String type, String id) {
    if (custom != null) {
      try {
        return runWithContextClassLoader(() -> {
          try {
            return custom.dataExists(type, id);
          } catch (Throwable e) {
            throw new Exception(e);
          }
        });
      } catch (Exception e) {
        log.error("Data exists check in DataStoreModule failed!", e);
      }
    }
    return false;
  }

  @Override
  public void deleteData(String type, String id) throws IOException {
    if (custom != null) {
      try {
        runWithContextClassLoader(() -> {
          try {
            custom.deleteData(type, id);
          } catch (Throwable e) {
            throw new Exception(e);
          }
          return null;
        });
      } catch (Exception e) {
        log.error("Delete data in DataStoreModule failed!", e);
      }
    }
  }

  private <T> T runWithContextClassLoader(Callable<T> contextCallback) throws Exception {
    ClassLoader original = Thread.currentThread().getContextClassLoader();
    try {
      Thread.currentThread().setContextClassLoader(customCL);
      return contextCallback.call();
    } finally {
      Thread.currentThread().setContextClassLoader(original);
    }
  }

}
