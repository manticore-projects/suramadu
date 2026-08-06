package org.webswing.toolkit.jslink;

import netscape.javascript.JSException;
import netscape.javascript.JSObject;
import org.webswing.Constants;
import org.webswing.model.SyncObjectResponse;
import org.webswing.model.app.out.AppToServerFrameMsgOut;
import org.webswing.model.appframe.in.AppFrameMsgIn;
import org.webswing.model.appframe.in.JSObjectMsgIn;
import org.webswing.model.appframe.in.JavaEvalRequestMsgIn;
import org.webswing.model.appframe.out.AppFrameMsgOut;
import org.webswing.toolkit.util.JsLinkUtil;
import org.webswing.toolkit.util.Services;
import org.webswing.toolkit.util.WeakValueHashMap;
import org.webswing.util.NamedThreadFactory;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeoutException;

public class WebJSObject extends JSObject {

  private static final Map<String, WeakReference<JSObjectMsgIn>> JS_GARBAGE_COLLECTION_MAP =
      new HashMap<>();
  private static final WeakValueHashMap<String, Object> JAVA_REFERENCES = new WeakValueHashMap<>();
  private static final boolean JS_LINK_ALLOWED =
      Boolean.getBoolean(Constants.SWING_START_SYS_PROP_ALLOW_JSLINK);
  private static final String JS_LINK_WHITELIST_PROP =
      System.getProperty(Constants.SWING_START_SYS_PROP_JSLINK_WHITELIST, "");
  private static List<String> JS_LINK_WHITE_LIST;
  private static final ScheduledExecutorService JAVA_EVAL_THREAD =
      Executors.newSingleThreadScheduledExecutor(
          NamedThreadFactory.getInstance("Webswing JsLink Processor"));
  private final JSObjectMsgIn jsThis;

  static {
    JS_LINK_WHITE_LIST = Arrays.asList(JS_LINK_WHITELIST_PROP.split(","));
  }

  public WebJSObject(JSObjectMsgIn jsThis) {
    this.jsThis = jsThis;
    if (jsThis != null) {
      synchronized (JS_GARBAGE_COLLECTION_MAP) {
        JS_GARBAGE_COLLECTION_MAP.put(jsThis.getId() + "",
            new WeakReference<JSObjectMsgIn>(jsThis));
      }
    }
  }

  @Override
  public Object call(String methodName, Object[] args) throws JSException {
    AppFrameMsgOut msg = JsLinkUtil.generateCallRequest(jsThis, methodName, args);
    return sendJsRequest(msg);
  }

  @Override
  public Object eval(String s) throws JSException {
    AppFrameMsgOut msg = JsLinkUtil.generateEvalRequest(jsThis, s);
    return sendJsRequest(msg);
  }

  @Override
  public Object getMember(String name) throws JSException {
    AppFrameMsgOut msg = JsLinkUtil.generateGetMemberRequest(jsThis, name);
    return sendJsRequest(msg);
  }

  @Override
  public void setMember(String name, Object value) throws JSException {
    AppFrameMsgOut msg = JsLinkUtil.generateSetMemberRequest(jsThis, name, value);
    sendJsRequest(msg);
  }

  @Override
  public void removeMember(String name) throws JSException {
    AppFrameMsgOut msg = JsLinkUtil.generateRemoveMemberRequest(jsThis, name);
    sendJsRequest(msg);
  }

  @Override
  public Object getSlot(int index) throws JSException {
    AppFrameMsgOut msg = JsLinkUtil.generateGetSlotRequest(jsThis, index);
    return sendJsRequest(msg);
  }

  @Override
  public void setSlot(int index, Object value) throws JSException {
    AppFrameMsgOut msg = JsLinkUtil.generateSetSlotRequest(jsThis, index, value);
    sendJsRequest(msg);
  }

  private static Object sendJsRequest(AppFrameMsgOut frame) {
    try {
      AppToServerFrameMsgOut msgOut = new AppToServerFrameMsgOut();
      SyncObjectResponse result = Services.getConnectionService().sendObjectSync(msgOut, frame,
          frame.getJsRequest().getCorrelationId());
      if (result.getFrame() != null) {
        AppFrameMsgIn frameIn = result.getFrame();
        return JsLinkUtil.parseResponse(frameIn);
      }
      return null;
    } catch (TimeoutException e) {
      throw new JSException(e.getMessage());
    } catch (Exception e) {
      throw new JSException(e.getMessage());
    }
  }

  public static List<String> getGarbage() {
    ArrayList<String> result = new ArrayList<String>();
    synchronized (JS_GARBAGE_COLLECTION_MAP) {
      for (Iterator<String> i = JS_GARBAGE_COLLECTION_MAP.keySet().iterator(); i.hasNext();) {
        String key = i.next();
        if (JS_GARBAGE_COLLECTION_MAP.get(key).isEnqueued()) {
          result.add(key);
          i.remove();
        }
      }
    }
    return result;
  }

  public JSObjectMsgIn getThisId() {
    return jsThis;
  }

  public static String createJavaReference(Object arg) {
    return createJavaReference(arg, UUID.randomUUID().toString());
  }

  public static String createJavaReference(Object arg, String newId) {
    if (JAVA_REFERENCES.containsValue(arg)) {
      String id = null;
      for (String key : JAVA_REFERENCES.keySet()) {
        if (JAVA_REFERENCES.get(key) == arg) {
          id = key;
        }
      }
      return id;
    } else {
      String id = newId;
      JAVA_REFERENCES.put(id, arg);
      return id;
    }
  }

  public static Object getJavaReference(String id) {
    Object o = JAVA_REFERENCES.get(id);
    return o;
  }

  public static Future<?> evaluateJava(final JavaEvalRequestMsgIn javaReq) {
    return JAVA_EVAL_THREAD.submit(new Runnable() {
      @Override
      public void run() {
        AppToServerFrameMsgOut msgOut = new AppToServerFrameMsgOut();
        AppFrameMsgOut frame;

        if (JS_LINK_ALLOWED) {
          Object javaRef = JAVA_REFERENCES.get(javaReq.getObjectId());
          frame = JsLinkUtil.callMatchingMethod(javaReq, javaRef, JS_LINK_WHITE_LIST);
        } else {
          frame = JsLinkUtil.getErrorResponse(javaReq.getCorrelationId(),
              "JsLink is not allowed for this application. Set the 'allowJsLink' to true in webswing.config to enable it.");
        }

        Services.getConnectionService().sendObject(msgOut, frame);
      }
    });
  }

}
