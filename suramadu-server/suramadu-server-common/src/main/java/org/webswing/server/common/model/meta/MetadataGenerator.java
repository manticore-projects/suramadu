package org.webswing.server.common.model.meta;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.ClassUtils.Interfaces;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.server.common.model.Config;
import org.webswing.server.common.model.meta.ConfigFieldEditorType.EditorType;
import org.webswing.server.common.util.CommonUtil;
import org.webswing.server.common.util.ConfigUtil;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class MetadataGenerator<T> {
  private static final Logger log = LoggerFactory.getLogger(MetadataGenerator.class);
  private static final List<EditorType> TABLE_COMPATIBLE_TYPES = Arrays.asList(EditorType.String,
      EditorType.Number, EditorType.Boolean, EditorType.StringList);
  private ConfigContext context;

  public MetaObject getMetadata(T config, ClassLoader cl) throws Exception {
    MetadataGenerator<T> generator = findGenerator(config);
    return generator.getMetadata(config, cl, null);
  }

  protected MetaObject getMetadata(T config, ClassLoader cl, Object parent) throws Exception {
    MetaObject metaObj = new MetaObject();
    if (config != null) {
      List<MetaField> metaFields = new ArrayList<MetaField>();
      LinkedHashSet<String> properties = getPropertyNames(config, cl);
      for (String propertyName : properties) {
        MetaField meta = getPropertyMetadata(config, cl, propertyName);
        if (meta != null) {
          metaFields.add(meta);
        }
      }
      metaObj.setFields(metaFields);
    }
    return metaObj;
  }

  protected LinkedHashSet<String> getPropertyNames(T config, ClassLoader cl) throws Exception {
    LinkedHashSet<String> properties = new LinkedHashSet<String>();
    BeanInfo info = Introspector.getBeanInfo(config.getClass());
    PropertyDescriptor[] pds = info.getPropertyDescriptors();
    for (PropertyDescriptor field : pds) {
      String fieldName = field.getName();
      Method readMethod = field.getReadMethod();
      if (readMethod != null) {
        ConfigField configField = CommonUtil.findAnnotation(readMethod, ConfigField.class);
        if (configField != null) {
          properties.add(fieldName);
        }
      }
    }
    ConfigFieldOrder fieldOrder = findAnnotation(config.getClass(), ConfigFieldOrder.class);
    if (fieldOrder != null) {
      LinkedHashSet<String> order = new LinkedHashSet<String>(Arrays.asList(fieldOrder.value()));
      properties.removeAll(order);
      order.addAll(properties);
      return order;
    }
    return properties;
  }

  protected MetaField getPropertyMetadata(T config, ClassLoader cl, String propertyName)
      throws Exception {
    BeanInfo info = Introspector.getBeanInfo(config.getClass());
    PropertyDescriptor[] pds = info.getPropertyDescriptors();
    for (PropertyDescriptor field : pds) {
      String fieldName = field.getName();
      if (fieldName.equals(propertyName)) {
        Method readMethod = field.getReadMethod();
        if (readMethod != null) {
          ConfigField configField = CommonUtil.findAnnotation(readMethod, ConfigField.class);
          if (configField != null) {
            try {
              Object value = getValue(config, cl, propertyName, readMethod);
              MetaField metadata = new MetaField();
              setMetadataProperties(metadata, fieldName, config, cl, propertyName, readMethod,
                  value, false);
              return metadata;
            } catch (Exception e) {
              log.error("Failed to generate metadata for field '{}' in object {}", fieldName,
                  config.getClass().getName(), e);
              MetaField metadata = new MetaField();
              setMetadataProperties(metadata, fieldName, config, cl, propertyName, readMethod, null,
                  true);
              return metadata;
            }
          }
        }
      }
    }
    return null;
  }

  private void setMetadataProperties(MetaField metadata, String fieldName, T config, ClassLoader cl,
      String propertyName, Method readMethod, Object value, boolean safeMode) throws Exception {
    metadata.setName(fieldName);
    metadata.setTab(getTab(config, cl, propertyName, readMethod));
    metadata.setLabel(getLabel(config, cl, propertyName, readMethod));
    metadata.setDescription(getDescription(config, cl, propertyName, readMethod));
    metadata.setVariables(isVariables(config, cl, propertyName, readMethod));
    metadata.setDiscriminator(isDiscriminator(config, cl, propertyName, readMethod));
    metadata.setPresets(getPresets(config, cl, propertyName, readMethod));

    if (safeMode) {
      metadata.setType(EditorType.Generic);
      metadata.setValue(getSafeValue(config, readMethod));
    } else {
      metadata.setType(getEditorType(config, cl, propertyName, readMethod));
      if (EditorType.ObjectListAsTable.equals(metadata.getType())) {
        metadata.setTableColumns(getColumnsDefinitions(config, cl, propertyName, readMethod));
      }
      metadata.setValue(value);
    }
  }

  protected List<MetaField> getColumnsDefinitions(T config, ClassLoader cl, String propertyName,
      Method readMethod) throws Exception {
    Class<?> type = getReturnTypeGeneric(readMethod, 0);
    Object instance = ConfigUtil.instantiateConfig(null, type);
    MetaObject metaobject = toMetaObject(config, cl, instance, type);
    List<MetaField> result = new ArrayList<MetaField>();
    for (MetaField field : metaobject.getFields()) {
      if (TABLE_COMPATIBLE_TYPES.contains(field.getType())) {
        result.add(field);
      }
    }
    return result;
  }

  protected Object getSafeValue(T config, Method readMethod) {
    Object value;
    try {
      value = readMethod.invoke(config);
    } catch (Exception e) {
      value = null;
    }
    return value;
  }

  @SuppressWarnings("unchecked")
  protected Object getValue(T config, ClassLoader cl, String propertyName, Method readMethod)
      throws Exception {
    Object value = readMethod.invoke(config);
    if (value == null) {
      return null;
    }
    EditorType type = getEditorType(config, cl, propertyName, readMethod);
    switch (type) {
      case Generic:
        return value;
      case Boolean:
        return (Boolean) value;
      case Number:
        return (Number) value;
      case String:
        return value instanceof String ? value : value.toString();
      case StringList:
        return (List<String>) value;
      case StringMap:
        return (Map<String, String>) value;
      case Object:
        return getMetaObject(config, cl, propertyName, readMethod, value);
      case ObjectList:
        return getMetaObjectList(config, cl, propertyName, readMethod, (List<?>) value);
      case ObjectListAsTable:
        return (List<Object>) value;
      case ObjectMap:
        return getMetaObjectMap(config, cl, propertyName, readMethod, (Map<String, ?>) value);
      default:
        return null;
    }
  }

  protected MetaObject getMetaObject(T config, ClassLoader cl, String propertyName,
      Method readMethod, Object value) throws Exception {
    Class<?> type = getExplicitType(config, cl, propertyName, readMethod, value);
    if (type == null) {
      type = readMethod.getReturnType();
    }
    MetaObject meta = toMetaObject(config, cl, value, type);
    return meta;
  }

  protected List<MetaObject> getMetaObjectList(T config, ClassLoader cl, String propertyName,
      Method readMethod, List<?> value) throws Exception {
    Class<?> type = getExplicitType(config, cl, propertyName, readMethod, value);
    if (type == null) {
      type = getReturnTypeGeneric(readMethod, 0);
    }
    List<MetaObject> objects = new ArrayList<MetaObject>();
    for (Object o : value) {
      MetaObject meta = toMetaObject(config, cl, o, type);
      objects.add(meta);
    }
    return objects;
  }

  private Object getMetaObjectMap(T config, ClassLoader cl, String propertyName, Method readMethod,
      Map<String, ?> value) throws Exception {
    Class<?> type = getExplicitType(config, cl, propertyName, readMethod, value);
    if (type == null) {
      type = getReturnTypeGeneric(readMethod, 1);
    }
    Map<String, MetaObject> objects = new LinkedHashMap<String, MetaObject>();
    for (String key : value.keySet()) {
      Object entryValue = value.get(key);
      MetaObject meta = toMetaObject(config, cl, entryValue, type);
      objects.put(key, meta);
    }
    return objects;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  protected MetaObject toMetaObject(T config, ClassLoader cl, Object value, Class<?> type)
      throws Exception {
    if (!ClassUtils.isAssignable(value.getClass(), type)) {
      value = ConfigUtil.instantiateConfig((Map) value, type);
    }
    MetadataGenerator generator = findGenerator(value);
    MetaObject metadata = generator.getMetadata(value, cl, config);
    return metadata;
  }

  @SuppressWarnings({"unchecked"})
  protected MetadataGenerator<T> findGenerator(Object obj) {
    MetadataGenerator<T> result = new MetadataGenerator<T>();
    try {
      if (obj != null) {
        ConfigType configType = findAnnotation(obj.getClass(), ConfigType.class);
        if (configType != null && configType.metadataGenerator() != null) {
          result = configType.metadataGenerator().getDeclaredConstructor().newInstance();
        }
      }
    } catch (Exception e) {
      log.error("Failed to initialize Metadata generator", e);
    }
    result.setContext(context);
    return result;
  }

  protected Class<?> getExplicitType(T config, ClassLoader cl, String propertyName,
      Method readMethod, Object value) throws ClassNotFoundException {
    ConfigFieldEditorType editor =
        CommonUtil.findAnnotation(readMethod, ConfigFieldEditorType.class);
    Class<?> type = null;
    if (editor != null && !StringUtils.isEmpty(editor.className())) {
      type = cl.loadClass(editor.className());
    }
    return type;
  }

  protected EditorType getEditorType(T config, ClassLoader cl, String propertyName,
      Method readMethod) {
    ConfigFieldEditorType editor =
        CommonUtil.findAnnotation(readMethod, ConfigFieldEditorType.class);
    if (editor != null) {
      return editor.editor();
    } else {
      return guessEditorType(config, cl, propertyName, readMethod);
    }
  }

  protected EditorType guessEditorType(T config, ClassLoader cl, String propertyName,
      Method readMethod) {
    Class<?> returnType = readMethod.getReturnType();
    if (ClassUtils.isAssignable(returnType, String.class)) {
      return EditorType.String;
    }
    if (returnType.isEnum()) {
      return EditorType.String;
    }
    if (ClassUtils.isAssignable(returnType, Number.class, true)) {
      return EditorType.Number;
    }
    if (ClassUtils.isAssignable(returnType, Boolean.class, true)) {
      return EditorType.Boolean;
    }
    if (ClassUtils.isAssignable(returnType, List.class)) {
      Class<?> genericClass = getReturnTypeGeneric(readMethod, 0);
      if (genericClass != null && ClassUtils.isAssignable(genericClass, String.class)) {
        return EditorType.StringList;
      } else {
        if (genericClass != null && findAnnotation(genericClass, ConfigType.class) != null) {
          return EditorType.ObjectList;
        }
      }
    }
    if (ClassUtils.isAssignable(returnType, Map.class)) {
      Class<?> genericClassKey = getReturnTypeGeneric(readMethod, 0);
      Class<?> genericClassValue = getReturnTypeGeneric(readMethod, 1);
      if (genericClassKey != null && ClassUtils.isAssignable(genericClassKey, String.class)) {
        if (genericClassValue != null && ClassUtils.isAssignable(genericClassValue, String.class)) {
          return EditorType.StringMap;
        } else {
          if (genericClassValue != null
              && findAnnotation(genericClassValue, ConfigType.class) != null) {
            return EditorType.ObjectMap;
          }
        }
      }
    }
    if (ClassUtils.isAssignable(returnType, Object.class)) {
      if (findAnnotation(returnType, ConfigType.class) != null) {
        return EditorType.Object;
      }
    }
    return EditorType.Generic;
  }

  protected ConfigGroup getTab(T config, ClassLoader cl, String propertyName, Method readMethod) {
    ConfigField configField = CommonUtil.findAnnotation(readMethod, ConfigField.class);
    return configField.tab();
  }

  protected String getLabel(T config, ClassLoader cl, String propertyName, Method readMethod) {
    ConfigField configField = CommonUtil.findAnnotation(readMethod, ConfigField.class);
    if (!StringUtils.isEmpty(configField.label())) {
      return configField.label();
    } else {
      return propertyName;
    }
  }

  protected String getDescription(T config, ClassLoader cl, String propertyName,
      Method readMethod) {
    ConfigField configField = CommonUtil.findAnnotation(readMethod, ConfigField.class);
    if (!StringUtils.isEmpty(configField.description())) {
      return configField.description();
    } else {
      return null;
    }
  }

  protected VariableSetName isVariables(T config, ClassLoader cl, String propertyName,
      Method readMethod) {
    ConfigFieldVariables variables =
        CommonUtil.findAnnotation(readMethod, ConfigFieldVariables.class);
    if (variables != null) {
      return variables.value();
    } else {
      return null;
    }
  }

  protected boolean isDiscriminator(T config, ClassLoader cl, String propertyName,
      Method readMethod) {
    ConfigFieldDiscriminator discriminator =
        CommonUtil.findAnnotation(readMethod, ConfigFieldDiscriminator.class);
    return discriminator != null;
  }

  @SuppressWarnings("rawtypes")
  protected String[] getPresets(T config, ClassLoader cl, String propertyName, Method readMethod) {
    ConfigFieldPresets presets = CommonUtil.findAnnotation(readMethod, ConfigFieldPresets.class);
    if (presets != null) {
      List<String> values = new ArrayList<String>(Arrays.asList(presets.value()));
      Class<? extends Enum> enumClass = presets.enumClass();
      Enum[] constants = enumClass.getEnumConstants();
      if (constants != null) {
        for (Enum c : constants) {
          values.add(c.name());
        }
      }
      return values.toArray(new String[values.size()]);
    } else {
      return guessPresets(config, cl, propertyName, readMethod);
    }
  }

  protected String[] guessPresets(T config, ClassLoader cl, String propertyName,
      Method readMethod) {
    if (readMethod.getReturnType().isEnum()) {
      Object[] constants = readMethod.getReturnType().getEnumConstants();
      if (constants != null) {
        String[] result = new String[constants.length];
        for (int i = 0; i < constants.length; i++) {
          Enum<?> enumConstant = (Enum<?>) constants[i];
          result[i] = enumConstant.name();
        }
        return result;
      }
    }
    return null;
  }

  public static <T extends Annotation> T findAnnotation(Class<?> type, Class<T> ann) {
    T annotation = type.getAnnotation(ann);
    if (annotation != null) {
      return annotation;
    } else {
      List<Class<?>> superClasses = new ArrayList<Class<?>>();
      superClasses.addAll(ClassUtils.getAllSuperclasses(type));
      superClasses.addAll(ClassUtils.getAllInterfaces(type));
      for (Class<?> c : superClasses) {
        annotation = c.getAnnotation(ann);
        if (annotation != null) {
          return annotation;
        }
      }
    }
    return null;
  }

  public static Class<?> getReturnTypeGeneric(Method m, int index) {
    Class<?> generic = getGenericClass(m.getGenericReturnType(), index);
    if (generic == null) {
      Set<Method> overrideHierarchy = MethodUtils.getOverrideHierarchy(m, Interfaces.INCLUDE);
      for (Method om : overrideHierarchy) {
        generic = getGenericClass(om.getGenericReturnType(), index);
        if (generic != null) {
          return generic;
        }
      }
    }
    return null;
  }

  public static Class<?> getGenericClass(Type genericType, int index) {
    if (genericType instanceof ParameterizedType parameterizedType) {
      Type[] generics = parameterizedType.getActualTypeArguments();
      if (generics != null && generics[index] instanceof Class<?> class1) {
        return class1;
      }
    }
    return null;
  }

  public static Class<?> getConfigTypeFromConstructor(Class<?> moduleClass) {
    Class<?> configClass = null;
    for (Constructor<?> constructor : moduleClass.getConstructors()) {
      Class<?>[] parameterTypes = constructor.getParameterTypes();
      if (parameterTypes.length == 1) {
        if (Config.class.isAssignableFrom(parameterTypes[0])) {
          configClass = parameterTypes[0];
          break;
        }
      }
    }
    return configClass;
  }

  public ConfigContext getContext() {
    return context;
  }

  public void setContext(ConfigContext context) {
    this.context = context;
  }

}
