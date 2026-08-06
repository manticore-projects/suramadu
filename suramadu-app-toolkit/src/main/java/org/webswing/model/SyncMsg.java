package org.webswing.model;

public interface SyncMsg extends Msg {
  String getCorrelationId();
}
