package org.webswing.server.api.services.security.modules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.server.common.extension.ExtensionClassLoader;
import org.webswing.server.common.service.security.AbstractWebswingUser;
import org.webswing.server.common.service.security.AuthenticatedWebswingUser;
import org.webswing.server.common.util.CommonUtil;
import org.webswing.server.common.util.ConfigUtil;
import org.webswing.server.model.exception.WsInitException;
import org.webswing.server.services.security.api.*;
import org.webswing.util.ClasspathUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.concurrent.Callable;

public class SecurityModuleWrapper implements WebswingSecurityModule {
  private static final Logger log = LoggerFactory.getLogger(SecurityModuleWrapper.class);

  private WebswingSecurityModule custom;
  private final WebswingSecurityConfig config;
  private URLClassLoader customCL;
  private final SecurityContext context;

  public SecurityModuleWrapper(SecurityContext context, WebswingSecurityConfig config) {
    this.context = context;
    this.config = config;
  }

  @Override
  public void init() {
    try {
      String classPath = CommonUtil.generateClassPathString(config.getClassPath());
      classPath = context.replaceVariables(classPath);
      URL[] urls =
          ClasspathUtil.populateClassPath(classPath, context.resolveFile(".").getAbsolutePath());
      customCL = new URLClassLoader(urls, new ExtensionClassLoader());
      String securityModuleClassName =
          BuiltInModules.getSecurityModuleClassName(config.getModule());
      Class<?> moduleClass = customCL.loadClass(securityModuleClassName);

      Constructor<?> defaultConstructor = null;
      Constructor<?> configConstructor = null;
      for (Constructor<?> constructor : moduleClass.getConstructors()) {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        if (parameterTypes.length == 1) {
          if (WebswingSecurityModuleConfig.class.isAssignableFrom(parameterTypes[0])) {
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
          custom = (WebswingSecurityModule) configConstructor
              .newInstance(ConfigUtil.instantiateConfig(config.getConfig(), configClass, context));
        } catch (Exception e) {
          ex = new WsInitException(
              "Could not construct custom security module class (using WebswingSecurityModuleConfig constructor).",
              e);
          log.error("Initialization failed.", ex);
        }
      }
      if (custom == null && defaultConstructor != null) {
        try {
          custom = (WebswingSecurityModule) defaultConstructor.newInstance();
        } catch (Exception e) {
          ex = new WsInitException(
              "Could not construct custom security module class (using Default constructor).", e);
          log.error("Initialization failed.", ex);
        }
      }
      if (custom != null) {
        runWithContextClassLoader(new Callable<Object>() {

          @Override
          public Object call() throws Exception {
            custom.init();
            return null;
          }
        });
      } else {
        log.error(
            "Custom security module class should define a default or WebswingSecurityModuleConfig constructor!");
        throw ex;
      }
    } catch (Exception e) {
      log.error("Failed to initialize security module. ", e);
      throw new RuntimeException("Failed to initialize Secuirty module.", e);
    }
  }

  @Override
  public AuthenticatedWebswingUser doLogin(final HttpServletRequest request,
      final HttpServletResponse response, String securedPath) throws ServletException, IOException {
    if (custom != null) {
      AuthenticatedWebswingUser user;
      try {
        user = runWithContextClassLoader(new Callable<AuthenticatedWebswingUser>() {

          @Override
          public AuthenticatedWebswingUser call() throws Exception {
            return custom.doLogin(request, response, securedPath);
          }
        });
      } catch (Exception e1) {
        if (e1 instanceof ServletException exception1) {
          throw exception1;
        } else if (e1 instanceof IOException exception) {
          throw exception;
        } else {
          throw new IOException("Failed to get user. Unexpected exception", e1);
        }
      }
      return user;
    }
    return null;

  }

  public void doLogout(final HttpServletRequest req, final HttpServletResponse res,
      final AbstractWebswingUser user) {
    if (custom != null) {
      try {
        runWithContextClassLoader(new Callable<Object>() {

          @Override
          public Object call() throws Exception {
            try {
              custom.doLogout(req, res, user);
            } catch (Throwable e) {
              throw new Exception(e);
            }
            return null;
          }
        });
      } catch (Exception e1) {
        log.error("Logout by SecurityModule failed.", e1);
      }
    }
  }

  public void doServeAuthenticated(AbstractWebswingUser user, String path, HttpServletRequest req,
      HttpServletResponse resp) {
    if (custom != null) {
      try {
        runWithContextClassLoader(new Callable<Object>() {

          @Override
          public Object call() throws Exception {
            try {
              custom.doServeAuthenticated(user, path, req, resp);
            } catch (Throwable e) {
              throw new Exception(e);
            }
            return null;
          }
        });
      } catch (Exception e1) {
        log.error("Authenticated request processing by SecurityModule failed.", e1);
      }
    }
  }

  @Override
  public void destroy() {
    if (custom != null) {
      try {
        runWithContextClassLoader(new Callable<Object>() {

          @Override
          public Object call() throws Exception {
            try {
              custom.destroy();
            } catch (Throwable e) {
              throw new Exception(e);
            }
            return null;
          }
        });
      } catch (Exception e1) {
        log.error("Destroying SecurityModule failed.", e1);
      }
    }
    if (customCL != null) {
      try {
        customCL.close();
      } catch (IOException e) {
        log.error("Closing Custom SecurityModule classloader failed.", e);
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
