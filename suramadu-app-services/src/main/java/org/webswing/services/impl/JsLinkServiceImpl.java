package org.webswing.services.impl;

import netscape.javascript.JSException;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.webswing.ext.services.JsLinkService;
import org.webswing.model.appframe.in.JavaEvalRequestMsgIn;
import org.webswing.model.appframe.in.JsParamMsgIn;
import org.webswing.model.appframe.out.JSObjectMsgOut;
import org.webswing.model.appframe.out.JavaObjectRefMsgOut;
import org.webswing.model.appframe.out.JsParamMsgOut;
import org.webswing.model.appframe.out.JsResultMsgOut;
import org.webswing.toolkit.jslink.WebJSObject;
import tools.jackson.databind.json.JsonMapper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JsLinkServiceImpl implements JsLinkService {
  private static final JsonMapper mapper = new JsonMapper();
  private static final Class<?>[] supportedPrimitives = {Number.class, String.class, Boolean.class,
      Byte.class, Character.class, Number[].class, String[].class, Boolean[].class, Byte[].class,
      Character[].class, int[].class, byte[].class, short[].class, char[].class, int[].class,
      long[].class, float[].class, double[].class, byte[].class};

  private static JsLinkServiceImpl impl;

  public static JsLinkServiceImpl getInstance() {
    if (impl == null) {
      impl = new JsLinkServiceImpl();
    }
    return impl;
  }

  private JsLinkServiceImpl() {}

  public JsParamMsgOut generateParam(Object arg) throws Exception {
    return generateParam(arg, true);
  }

  private JsParamMsgOut generateParam(Object arg, boolean includeArrays) throws Exception {
    JsParamMsgOut result = new JsParamMsgOut();
    if (arg != null) {
      if (isPrimitive(arg)) {
        result.setPrimitive(mapper.writeValueAsString(arg));
      } else if (arg instanceof WebJSObject jsobj) {
        result.setJsObject(new JSObjectMsgOut(jsobj.getThisId().getId()));
      } else if (arg instanceof Iterable<?> iterable && includeArrays) {
        List<JsParamMsgOut> array = new ArrayList<>();
        for (Object o : iterable) {
          array.add(generateParam(o, false));
        }
        result.setArray(array);
      } else if (arg.getClass().isArray() && includeArrays) {
        List<JsParamMsgOut> array = new ArrayList<JsParamMsgOut>();
        for (Object o : (Object[]) arg) {
          array.add(generateParam(o, false));
        }
        result.setArray(array);
      } else {
        JavaObjectRefMsgOut ref = toJavaObjectRef(arg);
        result.setJavaObject(ref);
      }
    }
    return result;
  }

  public Object parseValue(JsParamMsgIn value) throws JSException {
    try {
      if (value == null) {
        return null;
      } else if (value.getPrimitive() != null) {
        return mapper.readValue(value.getPrimitive(), Object.class);
      } else if (value.getJsObject() != null) {
        return new WebJSObject(value.getJsObject());
      } else if (value.getJavaObject() != null) {
        Object obj = WebJSObject.getJavaReference(value.getJavaObject().getId());
        if (obj == null) {
          throw new JSException(
              "Referred Java object not found. Make sure you keep a reference to Java objects sent to Javascript to prevent from being garbage collected.");
        }
        return obj;
      } else if (value.getArray() != null) {
        Object[] array = new Object[value.getArray().size()];
        for (int i = 0; i < array.length; i++) {
          array[i] = parseValue(value.getArray().get(i));
        }
        return array;
      } else {
        return null;
      }
    } catch (Exception e) {
      throw new JSException(ExceptionUtils.getStackTrace(e));
    }
  }

  public Object[] getCompatibleParams(JavaEvalRequestMsgIn javaReq, Method m) throws Exception {
    Object[] params = new Object[m.getParameterTypes().length];
    for (int i = 0; i < m.getParameterTypes().length; i++) {
      Class<?> type = m.getParameterTypes()[i];
      Object value = parseValue(javaReq.getParams().get(i));
      if (ClassUtils.isAssignable(value == null ? null : value.getClass(), type, true)) {
        params[i] = value;
      } else if (value instanceof String string) {
        try {
          params[i] = mapper.readValue(string, type);
        } catch (Exception e) {
          throw new RuntimeException(
              "Method " + m + " has incompatible parameter " + i + ". Expected " + type.getName()
                  + ". Error when reading as json:" + e.getLocalizedMessage());
        }
      } else {
        throw new RuntimeException(
            "Method " + m + " has incompatible parameter " + i + ". Expected " + type.getName()
                + ", got " + (value == null ? "null" : value.getClass().getName()));
      }
    }
    return params;
  }

  public JsResultMsgOut generateJavaResult(String correlationId, Object result) throws Exception {
    JsResultMsgOut msg = new JsResultMsgOut();
    msg.setCorrelationId(correlationId);
    msg.setValue(generateParam(result));
    return msg;
  }

  public JsResultMsgOut generateJavaErrorResult(String correlationId, Throwable result) {
    JsResultMsgOut msg = new JsResultMsgOut();
    msg.setCorrelationId(correlationId);
    msg.setError(ExceptionUtils.getStackTrace(result));
    return msg;
  }

  private static boolean isPrimitive(Object arg) {
    Class<?> argClass = arg.getClass();
    for (Class<?> c : supportedPrimitives) {
      if (c.isAssignableFrom(argClass)) {
        return true;
      }
    }
    return false;
  }

  private static JavaObjectRefMsgOut toJavaObjectRef(Object arg) {
    JavaObjectRefMsgOut result = new JavaObjectRefMsgOut();
    Set<String> methods = new HashSet<String>();
    for (Method m : arg.getClass().getDeclaredMethods()) {
      methods.add(m.getName());
    }
    result.setMethods(new ArrayList<String>(methods));
    String id = WebJSObject.createJavaReference(arg);
    result.setId(id);
    return result;
  }
}
