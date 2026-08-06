package org.webswing.server.common.datastore;

import org.webswing.Constants;
import org.webswing.server.common.datastore.WebswingDataStoreConfig.WebswingDataStoreMetadataGenerator;
import org.webswing.server.common.model.Config;
import org.webswing.server.common.model.meta.*;
import org.webswing.server.common.model.meta.ConfigFieldEditorType.EditorType;
import org.webswing.server.common.util.CommonUtil;
import org.webswing.util.ClasspathUtil;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.stream.Stream;

@ConfigType(metadataGenerator = WebswingDataStoreMetadataGenerator.class)
@ConfigFieldOrder({"classPath", "module", "config"})
public interface WebswingDataStoreConfig extends Config {

  @ConfigField(label = "Data Store Module Class Path",
      description = "Additional classpath for built-in data store module or for defining custom data store module.")
  @ConfigFieldDiscriminator
  @ConfigFieldVariables
  public List<String> getClassPath();

  @ConfigField(label = "Data Store Module Name",
      description = "Select one of built-in modules or enter full class name of custom data store module (has to implement org.webswing.server.common.datastore.WebswingDataStoreModule interface). Note the class and its dependencies has to be on classpath defined above.")
  @ConfigFieldPresets(enumClass = BuiltInDataStoreModules.class)
  @ConfigFieldDefaultValueString("INHERITED")
  @ConfigFieldDiscriminator
  public String getModule();

  @ConfigField(label = "Data Store Module Config",
      description = "Data store module specific configuration.")
  @ConfigFieldEditorType(editor = EditorType.Object)
  @ConfigFieldDefaultValueObject(HashMap.class)
  public Map<String, Object> getConfig();

  public static class WebswingDataStoreMetadataGenerator
      extends MetadataGenerator<WebswingDataStoreConfig> {

    private Object parent;

    @Override
    public MetaObject getMetadata(WebswingDataStoreConfig config, ClassLoader cl, Object parent)
        throws Exception {
      this.parent = parent;
      if (config.getClassPath() != null && config.getClassPath().size() > 0) {
        // need to create temporary classloader with configured classpath
        // FIXME test this
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
    protected String[] getPresets(WebswingDataStoreConfig config, ClassLoader cl,
        String propertyName, Method readMethod) {
      if ("classPath".equals(propertyName)) {
        // FIXME test this
        try {
          File dataStoreRoot = new File(
              getContext().replaceVariables("${" + Constants.ROOT_DIR_PATH + "}/datastore"));
          if (dataStoreRoot.exists() && dataStoreRoot.isDirectory()) {
            return Arrays.stream(dataStoreRoot.list())
                .map(f -> "${" + Constants.ROOT_DIR_PATH + "}/datastore/" + f + "/*")
                .toArray(String[]::new);
          } else {
            return new String[] {};
          }
        } catch (Exception e) {
          // do nothing
        }
      } else if ("module".equals(propertyName)) {
        ArrayList<String> discovered = new ArrayList<>();
        ServiceLoader<WebswingDataStoreModuleProvider> loader =
            ServiceLoader.load(WebswingDataStoreModuleProvider.class, cl);
        for (Iterator<WebswingDataStoreModuleProvider> i = loader.iterator(); i.hasNext();) {
          discovered.addAll(i.next().getDataStoreModuleClassNames());
        }
        Stream<String> builtin =
            Arrays.stream(super.getPresets(config, cl, propertyName, readMethod)).filter(
                sm -> !(isRootPath() && sm.equals(BuiltInDataStoreModules.INHERITED.name())));
        return Stream.concat(builtin, discovered.stream()).toArray(String[]::new);
      }
      return super.getPresets(config, cl, propertyName, readMethod);
    }

    @Override
    public Class<?> getExplicitType(WebswingDataStoreConfig config, ClassLoader cl,
        String propertyName, Method readMethod, Object value) throws ClassNotFoundException {
      if ("config".equals(propertyName)) {
        String dataStoreModuleClassName =
            BuiltInDataStoreModules.getDataStoreModuleClassName(config.getModule());
        if (dataStoreModuleClassName != null) {
          Class<?> configClass;
          try {
            Class<?> moduleClass = cl.loadClass(dataStoreModuleClassName);
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
