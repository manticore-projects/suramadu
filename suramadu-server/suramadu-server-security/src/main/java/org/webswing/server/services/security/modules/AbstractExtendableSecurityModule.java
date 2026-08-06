package org.webswing.server.services.security.modules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.server.common.service.security.AbstractWebswingUser;
import org.webswing.server.common.service.security.AuthenticatedWebswingUser;
import org.webswing.server.services.security.api.LoginResponseClosedException;
import org.webswing.server.services.security.api.WebswingAuthenticationException;
import org.webswing.server.services.security.extension.api.BuiltInModuleExtensions;
import org.webswing.server.services.security.extension.api.SecurityModuleExtension;
import org.webswing.server.services.security.extension.api.SecurityModuleExtensionConfig;
import org.webswing.server.services.security.extension.api.WebswingExtendableSecurityModuleConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Adds extensions support to {@link AbstractSecurityModule}. Four extension points are provided (
 * See {@link SecurityModuleExtension}) prototype.
 * <p>
 * A list of extension can be registered using Security Module's JSON configuration. Extension must
 * be one of the {@link BuiltInModuleExtensions} or a custom subclass of
 * {@link SecurityModuleExtension}.
 * </p>
 * JSON configuration example :
 * 
 * <pre>
 * "securityConfig" : {
 *   "securityModule" : "MyExtendableModule",
 *   "config" : {
 *     "extensions" : [ "org.webswing.MyExtension", "oneTimeUrl" ],
 *     "org.webswing.MyExtension" : {
 *       "myExtensionParam1" : "value"
 *     },
 *     "oneTimeUrl" : {
 *       "apiKeys" : {}
 *     }
 *   }
 * },
 * </pre>
 *
 * @param <T> configuration which extends {@link WebswingExtendableSecurityModuleConfig}
 */
public abstract class AbstractExtendableSecurityModule<T extends WebswingExtendableSecurityModuleConfig>
    extends AbstractSecurityModule<T> {
  private static final Logger log = LoggerFactory.getLogger(AbstractExtendableSecurityModule.class);

  private List<SecurityModuleExtension<?>> extensions = new ArrayList<>();

  public AbstractExtendableSecurityModule(T config) {
    super(config);
  }

  @Override
  public void init() {
    super.init();
    if (getConfig().getExtensions() != null) {
      for (String extensionName : getConfig().getExtensions()) {
        SecurityModuleExtension<?> extension = null;
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        try {
          String extensionClassName = BuiltInModuleExtensions.getExtensionClassName(extensionName);

          // Validate that the resolved class name is not null/empty
          if (extensionClassName == null || extensionClassName.isEmpty()) {
            log.error("Failed to resolve extension class name for: {}",
                sanitizeForLog(extensionName));
            continue;
          }

          Class<?> extensionClass = cl.loadClass(extensionClassName);

          // Verify the loaded class actually implements SecurityModuleExtension
          // to prevent arbitrary class instantiation
          if (!SecurityModuleExtension.class.isAssignableFrom(extensionClass)) {
            log.error("Extension class [{}] does not implement SecurityModuleExtension, skipping.",
                sanitizeForLog(extensionClassName));
            continue;
          }

          Constructor<?> defaultConstructor = null;
          Constructor<?> configConstructor = null;
          for (Constructor<?> constructor : extensionClass.getConstructors()) {
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            if (parameterTypes.length == 1) {
              if (SecurityModuleExtensionConfig.class.isAssignableFrom(parameterTypes[0])) {
                configConstructor = constructor;
                break;
              }
            } else if (parameterTypes.length == 0) {
              defaultConstructor = constructor;
            }
          }

          if (configConstructor != null) {
            Class<?> configClass = configConstructor.getParameterTypes()[0];
            try {
              Object instance =
                  configConstructor.newInstance(getConfig().getValueAs(extensionName, configClass));
              if (instance instanceof SecurityModuleExtension<?> moduleExtension) {
                extension = moduleExtension;
              } else {
                log.error("Constructed instance is not a SecurityModuleExtension: {}",
                    sanitizeForLog(extensionClassName));
              }
            } catch (Exception e) {
              log.error(
                  "Could not construct security module extension class (using SecurityModuleExtensionConfig constructor).",
                  e);
            }
          }
          if (extension == null && defaultConstructor != null) {
            try {
              Object instance = defaultConstructor.newInstance();
              if (instance instanceof SecurityModuleExtension<?> moduleExtension) {
                extension = moduleExtension;
              } else {
                log.error("Constructed instance is not a SecurityModuleExtension: {}",
                    sanitizeForLog(extensionClassName));
              }
            } catch (Exception e) {
              log.error(
                  "Could not construct security module extension class (using Default constructor).",
                  e);
            }
          }
          if (extension != null) {
            extensions.add(extension);
          } else {
            log.warn("No suitable constructor found for extension: {}",
                sanitizeForLog(extensionClassName));
          }
        } catch (ClassNotFoundException e) {
          throw new RuntimeException("Failed to load Security module extensions.", e);
        }
      }
    }
    // Make the extensions list unmodifiable after initialization
    extensions = Collections.unmodifiableList(extensions);
  }

  @Override
  public AuthenticatedWebswingUser doLogin(HttpServletRequest request, HttpServletResponse response,
      String securedPath) throws IOException {
    for (SecurityModuleExtension<?> extension : extensions) {
      try {
        AuthenticatedWebswingUser result =
            extension.doSufficientPreValidation(this, request, response);
        if (result != null) {
          onAuthenticationSuccess(result, request, response, securedPath);
          return result;
        }
      } catch (WebswingAuthenticationException e) {
        log.error("Extension failed to authenticate:", e);
      } catch (LoginResponseClosedException e) {
        return null;
      }
    }

    return super.doLogin(request, response, securedPath);
  }

  @Override
  protected void preVerify(HttpServletRequest request, HttpServletResponse response)
      throws WebswingAuthenticationException, LoginResponseClosedException {
    for (SecurityModuleExtension<?> extension : extensions) {
      extension.doRequiredPreValidation(this, request, response);
    }
  }

  @Override
  protected void postVerify(AuthenticatedWebswingUser user, HttpServletRequest request,
      HttpServletResponse response)
      throws LoginResponseClosedException, WebswingAuthenticationException {
    for (SecurityModuleExtension<?> extension : extensions) {
      extension.doRequiredPostValidation(this, user, request, response);
    }
  }

  @Override
  protected AuthenticatedWebswingUser decorateUser(AuthenticatedWebswingUser user,
      HttpServletRequest request, HttpServletResponse response) {
    for (SecurityModuleExtension<?> extension : extensions) {
      user = extension.decorateUser(user, request, response);
    }
    return user;
  }

  @Override
  protected void serveAuthenticated(AbstractWebswingUser user, String path, HttpServletRequest req,
      HttpServletResponse res) {
    for (SecurityModuleExtension<?> extension : extensions) {
      boolean served = extension.serveAuthenticated(user, path, req, res);
      if (served) {
        break;
      }
    }
  }

  /**
   * Sanitize a string for safe inclusion in log messages.
   */
  private static String sanitizeForLog(String input) {
    if (input == null) {
      return "null";
    }
    return input.replaceAll("[\\r\\n\\t]", "_");
  }
}
