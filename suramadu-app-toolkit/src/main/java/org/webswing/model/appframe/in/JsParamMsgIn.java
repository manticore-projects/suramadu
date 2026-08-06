package org.webswing.model.appframe.in;

import org.webswing.model.MsgIn;

import java.io.Serial;
import java.util.List;

public class JsParamMsgIn implements MsgIn {
  @Serial
  private static final long serialVersionUID = 5967292655638399242L;

  private String primitive;
  private JSObjectMsgIn jsObject;
  private JavaObjectRefMsgIn javaObject;
  private List<JsParamMsgIn> array;

  public String getPrimitive() {
    return primitive;
  }

  public void setPrimitive(String primitive) {
    this.primitive = primitive;
  }

  public JSObjectMsgIn getJsObject() {
    return jsObject;
  }

  public void setJsObject(JSObjectMsgIn jsObject) {
    this.jsObject = jsObject;
  }

  public JavaObjectRefMsgIn getJavaObject() {
    return javaObject;
  }

  public void setJavaObject(JavaObjectRefMsgIn javaObject) {
    this.javaObject = javaObject;
  }

  public List<JsParamMsgIn> getArray() {
    return array;
  }

  public void setArray(List<JsParamMsgIn> array) {
    this.array = array;
  }

}
