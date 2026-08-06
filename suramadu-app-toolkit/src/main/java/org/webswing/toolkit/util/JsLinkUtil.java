package org.webswing.toolkit.util;

import netscape.javascript.JSException;
import org.webswing.model.appframe.in.*;
import org.webswing.model.appframe.out.AppFrameMsgOut;
import org.webswing.model.appframe.out.JsEvalRequestMsgOut;
import org.webswing.model.appframe.out.JsEvalRequestMsgOut.JsEvalRequestType;
import org.webswing.model.appframe.out.JsParamMsgOut;
import org.webswing.model.appframe.out.JsResultMsgOut;
import org.webswing.toolkit.jslink.WebJSObject;
import org.webswing.util.AppLogger;

import javax.naming.OperationNotSupportedException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JsLinkUtil {

  public static AppFrameMsgOut generateEvalRequest(JSObjectMsgIn jsThis, String s) {
    JsEvalRequestMsgOut msg = new JsEvalRequestMsgOut();
    msg.setType(JsEvalRequestType.eval);
    msg.setEvalString(s);
    return generateAppFrame(msg);
  }

  public static AppFrameMsgOut generateCallRequest(JSObjectMsgIn jsThis, String methodName,
      Object[] args) {
    try {
      JsEvalRequestMsgOut msg = new JsEvalRequestMsgOut();
      msg.setType(JsEvalRequestType.call);
      msg.setThisObjectId(jsThis == null ? null : jsThis.getId());
      msg.setEvalString(methodName);
      if (args != null && args.length > 0) {
        List<JsParamMsgOut> params = new ArrayList<JsParamMsgOut>();
        for (Object arg : args) {
          params.add(Services.getJsLinkService().generateParam(arg));
        }
        msg.setParams(params);
      }
      return generateAppFrame(msg);
    } catch (Exception e) {
      AppLogger.error("Failed to generate js Call request:", e);
      throw new JSException("Failed to generate js Call request:" + e.getMessage());
    }
  }

  public static AppFrameMsgOut generateGetMemberRequest(JSObjectMsgIn jsThis, String name) {
    JsEvalRequestMsgOut msg = new JsEvalRequestMsgOut();
    msg.setType(JsEvalRequestType.getMember);
    msg.setEvalString(name);
    msg.setThisObjectId(jsThis == null ? null : jsThis.getId());
    return generateAppFrame(msg);
  }

  public static AppFrameMsgOut generateSetMemberRequest(JSObjectMsgIn jsThis, String name,
      Object value) {
    try {
      JsEvalRequestMsgOut msg = new JsEvalRequestMsgOut();
      msg.setType(JsEvalRequestType.setMember);
      msg.setThisObjectId(jsThis == null ? null : jsThis.getId());
      msg.setEvalString(name);
      if (value != null) {
        List<JsParamMsgOut> params = new ArrayList<JsParamMsgOut>();
        params.add(Services.getJsLinkService().generateParam(value));
        msg.setParams(params);
      }
      return generateAppFrame(msg);
    } catch (Exception e) {
      AppLogger.error("Failed to generate js Call request:", e);
      throw new JSException("Failed to generate js Call request:" + e.getMessage());
    }
  }

  public static AppFrameMsgOut generateRemoveMemberRequest(JSObjectMsgIn jsThis, String name) {
    JsEvalRequestMsgOut msg = new JsEvalRequestMsgOut();
    msg.setType(JsEvalRequestType.deleteMember);
    msg.setThisObjectId(jsThis == null ? null : jsThis.getId());
    msg.setEvalString(name);
    return generateAppFrame(msg);
  }

  public static AppFrameMsgOut generateSetSlotRequest(JSObjectMsgIn jsThis, int index,
      Object value) {
    AppFrameMsgOut msg = generateSetMemberRequest(jsThis, "" + index, value);
    msg.getJsRequest().setType(JsEvalRequestType.setSlot);
    return msg;
  }

  public static AppFrameMsgOut generateGetSlotRequest(JSObjectMsgIn jsThis, int index) {
    AppFrameMsgOut msg = generateGetMemberRequest(jsThis, "" + index);
    msg.getJsRequest().setType(JsEvalRequestType.getSlot);
    return msg;
  }

  public static Object parseResponse(AppFrameMsgIn result) throws JSException {
    if (result.getJsResponse() != null) {
      JsResultMsgIn param = result.getJsResponse();
      if (param.getError() != null) {
        throw new JSException(param.getError());
      } else {
        if (param.getValue() != null) {
          JsParamMsgIn value = param.getValue();
          return Services.getJsLinkService().parseValue(value);
        } else {
          return null;
        }
      }
    }
    return null;
  }

  private static AppFrameMsgOut generateAppFrame(JsEvalRequestMsgOut msg) {
    AppFrameMsgOut msgout = new AppFrameMsgOut();
    msgout.setJsRequest(msg);
    msg.setGarbageIds(WebJSObject.getGarbage());
    msg.setCorrelationId(UUID.randomUUID().toString());
    return msgout;
  }

  private static AppFrameMsgOut generateAppFrame(JsResultMsgOut msg) {
    AppFrameMsgOut msgout = new AppFrameMsgOut();
    msgout.setJavaResponse(msg);
    return msgout;
  }

  public static AppFrameMsgOut getErrorResponse(String correlationId, String error) {
    return generateAppFrame(Services.getJsLinkService().generateJavaErrorResult(correlationId,
        new OperationNotSupportedException(error)));
  }

  public static AppFrameMsgOut callMatchingMethod(JavaEvalRequestMsgIn javaReq, Object javaRef,
      List<String> jsLinkWhitelist) {
    if (javaRef != null) {
      Exception exception = null;
      List<Method> candidates = new ArrayList<Method>();
      Class<?> cls = javaRef.getClass();

      if (!isObjectClassAllowed(cls.getCanonicalName(), jsLinkWhitelist)) {
        exception = new RuntimeException(
            "Public method named " + javaReq.getMethod() + " with " + javaReq.getParams().size()
                + " parameters in class " + javaRef.getClass().getCanonicalName()
                + " is not allowed to be executed due to whitelist!");
        AppLogger.error("Error while calling java from javascript:", exception);
        return generateAppFrame(Services.getJsLinkService()
            .generateJavaErrorResult(javaReq.getCorrelationId(), exception));
      }

      for (Method m : cls.getDeclaredMethods()) {
        int requestParamCount = javaReq.getParams() != null ? javaReq.getParams().size() : 0;
        if (m.getName().equals(javaReq.getMethod())
            && m.getParameterTypes().length == requestParamCount) {
          candidates.add(m);
        }
      }
      if (candidates.size() > 0) {
        for (Method m : candidates) {
          try {
            Object[] params = Services.getJsLinkService().getCompatibleParams(javaReq, m);
            try {
              Object result = m.invoke(javaRef, params);
              return generateAppFrame(Services.getJsLinkService()
                  .generateJavaResult(javaReq.getCorrelationId(), result));
            } catch (Exception invEx) {
              AppLogger.error("Error while calling java from javascript:", invEx);
              return generateAppFrame(Services.getJsLinkService()
                  .generateJavaErrorResult(javaReq.getCorrelationId(), invEx));
            }
          } catch (Exception x) {
            exception = new RuntimeException(
                "Parameters of method " + javaReq.getMethod() + " are not compatible with request.",
                x);
          }
        }
      } else {
        exception = new RuntimeException(
            "Public method named " + javaReq.getMethod() + " with " + javaReq.getParams().size()
                + " parameters not found in class " + javaRef.getClass().getCanonicalName());
      }
      AppLogger.error("Error while calling java from javascript:", exception);
      return generateAppFrame(Services.getJsLinkService()
          .generateJavaErrorResult(javaReq.getCorrelationId(), exception));
    } else {
      NullPointerException e = new NullPointerException(
          "Caller object reference is not valid any more.  Make sure you keep a reference to Java objects sent to Javascript to prevent from being garbage collected. ");
      AppLogger.error("Error while calling java from javascript:", e);
      return generateAppFrame(
          Services.getJsLinkService().generateJavaErrorResult(javaReq.getCorrelationId(), e));
    }
  }

  private static boolean isObjectClassAllowed(String cls, List<String> jsLinkWhitelist) {
    if (jsLinkWhitelist == null || jsLinkWhitelist.isEmpty()) {
      return false;
    }

    for (String entry : jsLinkWhitelist) {
      if (cls.matches(entry.replace(".", "\\.").replace("*", ".*"))) {
        return true;
      }
    }

    return false;
  }

  private JsLinkUtil() {}
}
