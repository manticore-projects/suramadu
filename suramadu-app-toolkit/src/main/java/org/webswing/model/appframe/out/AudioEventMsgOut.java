package org.webswing.model.appframe.out;

import org.webswing.model.MsgOut;

import java.io.Serial;

public class AudioEventMsgOut implements MsgOut {

  @Serial
  private static final long serialVersionUID = -8172693669771579808L;

  public enum AudioEventType {
    play, stop, update, dispose
  }

  private String id;
  private AudioEventType eventType;
  private byte[] data;
  private Float time;
  private Integer loop;

  public AudioEventMsgOut(String id, AudioEventType eventType, byte[] data, Float time,
      Integer loop) {
    this.id = id;
    this.eventType = eventType;
    this.data = data;
    this.time = time;
    this.loop = loop;
  }

  public AudioEventMsgOut(String id, AudioEventType eventType, Float time, Integer loop) {
    super();
    this.id = id;
    this.eventType = eventType;
    this.time = time;
    this.loop = loop;
  }

  public AudioEventMsgOut(String id, AudioEventType eventType) {
    super();
    this.id = id;
    this.eventType = eventType;
  }

  public AudioEventMsgOut() {}

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public byte[] getData() {
    return data;
  }

  public void setData(byte[] data) {
    this.data = data;
  }

  public Float getTime() {
    return time;
  }

  public void setTime(Float time) {
    this.time = time;
  }

  public AudioEventType getEventType() {
    return eventType;
  }

  public void setEventType(AudioEventType eventType) {
    this.eventType = eventType;
  }

  public Integer getLoop() {
    return loop;
  }

  public void setLoop(Integer loop) {
    this.loop = loop;
  }

}
