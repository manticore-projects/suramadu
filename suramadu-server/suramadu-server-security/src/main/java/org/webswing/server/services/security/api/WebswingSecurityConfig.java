package org.webswing.server.services.security.api;

import org.webswing.Constants;
import org.webswing.server.common.model.Config;
import org.webswing.server.common.model.meta.*;
import org.webswing.server.common.model.meta.ConfigFieldEditorType.EditorType;
import org.webswing.server.common.util.CommonUtil;
import org.webswing.server.services.security.api.WebswingSecurityConfig.WebswingSecurityMetadataGenerator;
import org.webswing.util.ClasspathUtil;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.stream.Stream;

@ConfigType(metadataGenerator = WebswingSecurityMetadataGenerator.class)
@ConfigFieldOrder({"classPath", "module", "config", "authorizationConfig"})
public interface WebswingSecurityConfig extends Config {

  @ConfigField(label = "Security Module Class Path",
      description = "Additional classpath for built-in Security module or for defining custom security module. ")
  @ConfigFieldVariables(VariableSetName.SwingApp)
  @ConfigFieldDiscriminator
  public List<String> getClassPath();

  @ConfigField(label = "Security Module Name",
      description = "Select one of built-in modules or enter full class name of custom security module (has to implement org.webswing.server.services.security.api.WebswingSecurityModule interface). Note the class and its dependencies has to be on classpath defined above.")
  @ConfigFieldPresets(enumClass = BuiltInModules.class)
  @ConfigFieldDefaultValueString("INHERITED")
  @ConfigFieldDiscriminator
  public String getModule();

  @ConfigField(label = "Security Module Config",
      description = "Security module specific configuration.")
  @ConfigFieldEditorType(editor = EditorType.Object)
  @ConfigFieldDefaultValueObject(HashMap.class)
  public Map<String, Object> getConfig();

  @ConfigField(label = "Authorization Config",
      description = "Define users and roles authorized to access this application")
  @ConfigFieldDefaultValueObject(AuthorizationConfig.class)
  public AuthorizationConfig getAuthorizationConfig();

  public static class WebswingSecurityMetadataGenerator
      extends MetadataGenerator<WebswingSecurityConfig> {

    private Object parent;

    @Override
    public MetaObject getMetadata(WebswingSecurityConfig config, ClassLoader cl, Object parent)
        throws Exception {
      this.parent = parent;
      if (config.getClassPath() != null && config.getClassPath().size() > 0) {
        // need to create temporary classloader with configured classpath
        // 1.resolve base dir for classpath
        File homeFile = getContext().resolveFile(".");
        String home = homeFile == null ? "." : homeFile.getCanonicalPath();
        // 2. construct the class loader
        String classPath = CommonUtil.generateClassPathString(config.getClassPath());
        classPath = getContext().replaceVariables(classPath);
        URL[] urls = ClasspathUtil.populateClassPath(classPath, home);
        try (URLClassLoader customCL = new URLClassLoader(urls, cl)) {
          return super.getMetadata(config, customCL, parent);
        } finally {
          this.parent = null;
        }
      } else {
        MetaObject metadata = super.getMetadata(config, cl, parent);
        this.parent = null;
        return metadata;
      }
    }

    @Override
    protected String[] getPresets(WebswingSecurityConfig config, ClassLoader cl,
        String propertyName, Method readMethod) {
      if ("classPath".equals(propertyName)) {
        try {
          String securityRootString = "${" + Constants.ROOT_DIR_PATH + "}/security/";
          File securityRoot = new File(
              getContext().replaceVariables("${" + Constants.ROOT_DIR_PATH + "}/security"));
          if (securityRoot.exists() && securityRoot.isDirectory()) {
            return Arrays.stream(securityRoot.list()).map(f -> securityRootString + f + "/*")
                .toArray(String[]::new);
          } else {
            return new String[] {};
          }
        } catch (Exception e) {
          // do nothing
        }
      } else if ("module".equals(propertyName)) {
        ArrayList<String> discovered = new ArrayList<>();
        ServiceLoader<WebswingSecurityModuleProvider> loader =
            ServiceLoader.load(WebswingSecurityModuleProvider.class, cl);
        for (Iterator<WebswingSecurityModuleProvider> i = loader.iterator(); i.hasNext();) {
          discovered.addAll(i.next().getSecurityModuleClassNames());
        }
        Stream<String> builtin =
            Arrays.stream(super.getPresets(config, cl, propertyName, readMethod))
                .filter(sm -> !(isRootPath() && sm.equals(BuiltInModules.INHERITED.name())));
        return Stream.concat(builtin, discovered.stream()).toArray(String[]::new);
      }
      return super.getPresets(config, cl, propertyName, readMethod);
    }

    @Override
    public Class<?> getExplicitType(WebswingSecurityConfig config, ClassLoader cl,
        String propertyName, Method readMethod, Object value) throws ClassNotFoundException {
      if ("config".equals(propertyName)) {
        String securityModuleClassName =
            BuiltInModules.getSecurityModuleClassName(config.getModule());
        if (securityModuleClassName != null) {
          Class<?> configClass;
          try {
            Class<?> moduleClass = cl.loadClass(securityModuleClassName);
            configClass = getConfigTypeFromConstructor(moduleClass);
            if (configClass != null) {
              return configClass;
            }
          } catch (Throwable e) {
            return null;
          }
        }
      }
      return super.getExplicitType(config, cl, propertyName, readMethod, value);
    }

    @Override
    protected LinkedHashSet<String> getPropertyNames(WebswingSecurityConfig config, ClassLoader cl)
        throws Exception {
      LinkedHashSet<String> names = super.getPropertyNames(config, cl);
      if (isRootPath()) {
        names.remove("authorizationConfig");
      }
      return names;
    }

    private boolean isRootPath() {
      if (parent instanceof Config config) {
        Object path = config.asMap().get("path");
        if ("/".equals(path) || "".equals(path)) {
          return true;
        }
      }
      return false;
    }
  }
}
