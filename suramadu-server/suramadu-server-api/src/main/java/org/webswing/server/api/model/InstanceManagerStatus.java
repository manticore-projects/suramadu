package org.webswing.server.api.model;

import java.util.Objects;

public class InstanceManagerStatus {

  private StatusEnum status;
  private String error;
  private String errorDetails;

  public enum StatusEnum {

    STARTING(String.valueOf("Starting")), RUNNING(String.valueOf("Running")), STOPPED(
        String.valueOf("Stopped")), STOPPING(
            String.valueOf("Stopping")), ERROR(String.valueOf("Error"));

    private String value;

    StatusEnum(String v) {
      value = v;
    }

    public String value() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  public InstanceManagerStatus status(StatusEnum status) {
    this.status = status;
    return this;
  }

  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public InstanceManagerStatus error(String error) {
    this.error = error;
    return this;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  /**
   **/
  public InstanceManagerStatus errorDetails(String errorDetails) {
    this.errorDetails = errorDetails;
    return this;
  }

  public String getErrorDetails() {
    return errorDetails;
  }

  public void setErrorDetails(String errorDetails) {
    this.errorDetails = errorDetails;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InstanceManagerStatus instanceManagerStatus = (InstanceManagerStatus) o;
    return Objects.equals(this.status, instanceManagerStatus.status)
        && Objects.equals(this.error, instanceManagerStatus.error)
        && Objects.equals(this.errorDetails, instanceManagerStatus.errorDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, error, errorDetails);
  }

}
