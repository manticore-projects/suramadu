package org.webswing.model.appframe.out;

import org.webswing.model.MsgOut;
import org.webswing.model.SyncMsg;

import java.io.Serial;
import java.util.List;

public class JsEvalRequestMsgOut implements MsgOut, SyncMsg {

  @Serial
  private static final long serialVersionUID = -1135676700302370879L;

  public enum JsEvalRequestType {
    eval, call, setMember, getMember, deleteMember, setSlot, getSlot;
  }

  private String correlationId;
  private String thisObjectId;
  private JsEvalRequestType type;
  private String evalString;
  private List<JsParamMsgOut> params;
  private List<String> garbageIds;

  public String getThisObjectId() {
    return thisObjectId;
  }

  public void setThisObjectId(String thisObjectId) {
    this.thisObjectId = thisObjectId;
  }

  public JsEvalRequestType getType() {
    return type;
  }

  public void setType(JsEvalRequestType type) {
    this.type = type;
  }

  public String getEvalString() {
    return evalString;
  }

  public void setEvalString(String evalString) {
    this.evalString = evalString;
  }

  public List<JsParamMsgOut> getParams() {
    return params;
  }

  public void setParams(List<JsParamMsgOut> params) {
    this.params = params;
  }

  public List<String> getGarbageIds() {
    return garbageIds;
  }

  public void setGarbageIds(List<String> garbageIds) {
    this.garbageIds = garbageIds;
  }

  public String getCorrelationId() {
    return correlationId;
  }

  public void setCorrelationId(String correlationId) {
    this.correlationId = correlationId;
  }

}
