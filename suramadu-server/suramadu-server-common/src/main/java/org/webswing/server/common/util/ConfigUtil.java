package org.webswing.server.common.util;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webswing.server.common.model.Config;
import org.webswing.server.common.model.meta.*;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ConfigUtil {
  private static final Logger log = LoggerFactory.getLogger(ConfigUtil.class);

  public static MetaObject getConfigMetadata(Object o, ClassLoader cl, ConfigContext ctx)
      throws Exception {
    MetadataGenerator<Object> generator = new MetadataGenerator<Object>();
    generator.setContext(ctx);
    return generator.getMetadata(o, cl);
  }

  public static <T> T instantiateConfig(Map<String, Object> c, final Class<T> clazz,
      final Object... context) {
    if (c == null) {
      c = new HashMap();
    }
    final Map<String, Object> config = c;
    return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] {clazz},
        new InvocationHandler() {

          @Override
          public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            BeanInfo info = Introspector.getBeanInfo(method.getDeclaringClass());
            PropertyDescriptor[] pds = info.getPropertyDescriptors();
            if ("getValueAs".equals(method.getName()) && method.getParameterTypes().length == 2
                && args[0] instanceof String s && args[1] instanceof Class c) {
              Object o = config.get(s);
              Map<String, Object> subConfig =
                  (Map<String, Object>) (o instanceof HashMap ? o : new HashMap());
              return instantiateConfig(subConfig, c, context);
            }
            if ("asMap".equals(method.getName()) && method.getParameterTypes().length == 0) {
              return config;
            }
            for (PropertyDescriptor pd : pds) {
              if (pd.getReadMethod().equals(method)) {
                for (Object o : context) {
                  if (method.getReturnType().isAssignableFrom(o.getClass())) {
                    return o;
                  }
                }
                Object value = config.get(pd.getName());
                if (value != null) {
                  if (ClassUtils.isAssignable(value.getClass(), method.getReturnType(), true)) {
                    if (value instanceof Map valueMap) {
                      Class generic = getGenericClass(method.getGenericReturnType(), 1);
                      if (generic != null && generic.isInterface()) {
                        Map resultMap = new HashMap();
                        for (Object key : valueMap.keySet()) {
                          Object entryValue = valueMap.get(key);
                          resultMap.put(key, instantiateConfig((Map<String, Object>) entryValue,
                              generic, context));
                        }
                        return resultMap;
                      }
                    }
                    if (value instanceof List valuelist) {
                      Class generic = getGenericClass(method.getGenericReturnType(), 0);
                      if (generic != null && generic.isInterface()) {
                        List resultList = new ArrayList();
                        for (Object item : valuelist) {
                          resultList
                              .add(instantiateConfig((Map<String, Object>) item, generic, context));
                        }
                        return resultList;
                      }
                    }
                    if (value instanceof Number number) {
                      return convertNumberToTargetClass(number, method.getReturnType());
                    }
                    return value;
                  } else if (ClassUtils.isAssignable(value.getClass(), Number.class, true)
                      && ClassUtils.isAssignable(method.getReturnType(), Number.class, true)) {
                    return convertNumberToTargetClass((Number) value, method.getReturnType());
                  } else if (value instanceof Map map && method.getReturnType().isInterface()
                      && !Collection.class.isAssignableFrom(method.getReturnType())) {
                    return instantiateConfig(map, method.getReturnType(), context);
                  } else if (value instanceof String string && method.getReturnType().isEnum()) {
                    try {
                      return Enum.valueOf((Class<Enum>) method.getReturnType(), string);
                    } catch (Exception e) {
                      return null;
                    }
                  } else {
                    log.error("Invalid configuration. Type of {}.{} is not {}", clazz.getName(),
                        pd.getName(), method.getReturnType());
                    return null;
                  }
                } else {
                  // value is null, check if default value is defined
                  Class<?> returnType = method.getReturnType();
                  Object generated = getDefaultGeneratedValue(method, clazz, proxy);
                  if (generated != null
                      && ClassUtils.isAssignable(generated.getClass(), returnType)) {
                    return generated;
                  }
                  if (ClassUtils.isAssignable(returnType, String.class)) {
                    String defaultStringValue = getDefaultStringValue(method);
                    config.put(pd.getName(), defaultStringValue);
                    return defaultStringValue;
                  }
                  if (ClassUtils.isAssignable(returnType, Enum.class)) {
                    String enumName = getDefaultStringValue(method);
                    if (enumName != null) {
                      config.put(pd.getName(), enumName);
                      return Enum.valueOf((Class<Enum>) returnType, enumName);
                    } else {
                      return null;
                    }
                  }
                  if (ClassUtils.isAssignable(returnType, Number.class)) {
                    Double number = getDefaultNumberValue(method);
                    Number converted = convertNumberToTargetClass(number, returnType);
                    config.put(pd.getName(), converted);
                    return converted;
                  }
                  if (ClassUtils.isAssignable(returnType, Boolean.class)) {
                    Boolean bool = getDefaultBooleanValue(method);
                    config.put(pd.getName(), bool);
                    return bool;
                  }
                  if (ClassUtils.isAssignable(returnType, Config.class)) {
                    ConfigFieldDefaultValueObject defaultObject = isDefaultObjectValue(method);
                    if (defaultObject != null) {
                      config.put(pd.getName(), new HashMap<String, Object>());
                      return instantiateConfig(null, returnType, context);
                    }
                  }
                  if (ClassUtils.isAssignable(returnType, Object.class)) {
                    ConfigFieldDefaultValueObject defaultObject = isDefaultObjectValue(method);
                    if (defaultObject != null) {
                      Object newInstance;
                      if (Void.class.equals(defaultObject.value())) {
                        newInstance = returnType.getDeclaredConstructor().newInstance();
                      } else {
                        newInstance = defaultObject.value().getDeclaredConstructor().newInstance();
                      }
                      config.put(pd.getName(), newInstance);
                      return newInstance;
                    }
                  }
                }
              }
            }
            return null;
          }

        });
  }

  protected static <T> Object getDefaultGeneratedValue(Method method, Class<?> currentConfigType,
      Object currentConfig) {
    ConfigFieldDefaultValueGenerator defaultGeneratorAnnotation =
        CommonUtil.findAnnotation(method, ConfigFieldDefaultValueGenerator.class);
    if (defaultGeneratorAnnotation != null) {
      String methodName = defaultGeneratorAnnotation.value();
      try {
        Method m = method.getDeclaringClass().getDeclaredMethod(methodName, currentConfigType);
        Object value = m.invoke(null, currentConfig);
        return value;
      } catch (Exception e) {
        log.error("Default Value Generator method '{}' is not valid.", methodName, e);
      }
    }
    return null;
  }

  protected static ConfigFieldDefaultValueObject isDefaultObjectValue(Method method) {
    ConfigFieldDefaultValueObject defaultObjectAnnotation =
        CommonUtil.findAnnotation(method, ConfigFieldDefaultValueObject.class);
    return defaultObjectAnnotation;
  }

  protected static Boolean getDefaultBooleanValue(Method method) {
    ConfigFieldDefaultValueBoolean bool =
        CommonUtil.findAnnotation(method, ConfigFieldDefaultValueBoolean.class);
    if (bool != null) {
      return bool.value();
    }
    return null;
  }

  protected static Double getDefaultNumberValue(Method method) {
    ConfigFieldDefaultValueNumber defaultString =
        CommonUtil.findAnnotation(method, ConfigFieldDefaultValueNumber.class);
    if (defaultString != null) {
      return defaultString.value();
    }
    return null;
  }

  protected static String getDefaultStringValue(Method method) {
    ConfigFieldDefaultValueString defaultString =
        CommonUtil.findAnnotation(method, ConfigFieldDefaultValueString.class);
    if (defaultString != null) {
      return defaultString.value();
    }
    return null;
  }

  private static Class<?> getGenericClass(Type genericType, int index) {
    if (genericType instanceof ParameterizedType type) {
      Type[] generics = type.getActualTypeArguments();
      if (generics[index] instanceof Class<?> class1) {
        return class1;
      }
    }
    return null;
  }

  public static Number convertNumberToTargetClass(Number number, Class targetClass)
      throws IllegalArgumentException {
    if (number == null) {
      return null;
    }
    if (targetClass.isInstance(number)) {
      return number;
    } else if (targetClass.equals(Short.class) || targetClass.equals(Short.TYPE)) {
      long value = number.longValue();
      if (value < Short.MIN_VALUE || value > Short.MAX_VALUE) {
        raiseOverflowException(number, targetClass);
      }
      return number.shortValue();
    } else if (targetClass.equals(Integer.class) || targetClass.equals(Integer.TYPE)) {
      long value = number.longValue();
      if (value < Integer.MIN_VALUE || value > Integer.MAX_VALUE) {
        raiseOverflowException(number, targetClass);
      }
      return number.intValue();
    } else if (targetClass.equals(Long.class) || targetClass.equals(Long.TYPE)) {
      return number.longValue();
    } else if (targetClass.equals(Float.class) || targetClass.equals(Float.TYPE)) {
      return number.floatValue();
    } else if (targetClass.equals(Double.class) || targetClass.equals(Double.TYPE)) {
      return number.doubleValue();
    } else if (targetClass.equals(BigInteger.class)) {
      return BigInteger.valueOf(number.longValue());
    } else if (targetClass.equals(BigDecimal.class)) {
      return new BigDecimal(number.toString());
    } else {
      throw new IllegalArgumentException(
          "Could not convert number [" + number + "] of type [" + number.getClass().getName()
              + "] to unknown target class [" + targetClass.getName() + "]");
    }
  }

  public static Map<String, Object> fixPaths(File config, Map<String, Object> json) {
    Map<String, Object> result = new LinkedHashMap<>();
    boolean changed = false;
    for (String key : json.keySet()) {
      String path = CommonUtil.toPath(key).toLowerCase();
      path = StringUtils.isEmpty(path) ? "/" : path;
      result.put(path, json.get(key));
      if (!StringUtils.equals(path, key)) {
        changed = true;
      }
    }
    if (changed) {
      try {
        WebswingObjectMapper.get().writerWithDefaultPrettyPrinter().writeValue(config, result);
      } catch (Exception e) {
        log.error("Failed save fixed paths in configuration file. ", e);
      }
    }
    return result;
  }

  private static void raiseOverflowException(Number number, Class targetClass) {
    throw new IllegalArgumentException(
        "Could not convert number [" + number + "] of type [" + number.getClass().getName()
            + "] to target class [" + targetClass.getName() + "]: overflow");
  }

  private ConfigUtil() {}
}
