package org.webswing.model.appframe.out;

import org.webswing.model.MsgOut;

import java.io.Serial;
import java.util.List;

public class JsParamMsgOut implements MsgOut {
  @Serial
  private static final long serialVersionUID = 5967292655638399242L;

  private String primitive;
  private JSObjectMsgOut jsObject;
  private JavaObjectRefMsgOut javaObject;
  private List<JsParamMsgOut> array;

  public String getPrimitive() {
    return primitive;
  }

  public void setPrimitive(String primitive) {
    this.primitive = primitive;
  }

  public JSObjectMsgOut getJsObject() {
    return jsObject;
  }

  public void setJsObject(JSObjectMsgOut jsObject) {
    this.jsObject = jsObject;
  }

  public JavaObjectRefMsgOut getJavaObject() {
    return javaObject;
  }

  public void setJavaObject(JavaObjectRefMsgOut javaObject) {
    this.javaObject = javaObject;
  }

  public List<JsParamMsgOut> getArray() {
    return array;
  }

  public void setArray(List<JsParamMsgOut> array) {
    this.array = array;
  }

}
