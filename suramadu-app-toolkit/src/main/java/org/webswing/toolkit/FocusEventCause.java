package org.webswing.toolkit;

public enum FocusEventCause {

  UNKNOWN, MOUSE_EVENT, TRAVERSAL, TRAVERSAL_UP, TRAVERSAL_DOWN, TRAVERSAL_FORWARD, TRAVERSAL_BACKWARD, MANUAL_REQUEST, AUTOMATIC_TRAVERSE, ROLLBACK, NATIVE_SYSTEM, ACTIVATION, CLEAR_GLOBAL_FOCUS_OWNER, RETARGETED;

  public static FocusEventCause getValue(Enum<?> enumvalue) {
    if (enumvalue == null) {
      return null;
    } else {
      try {
        return valueOf(enumvalue.name());
      } catch (IllegalArgumentException e) {
        return null;
      }
    }
  }

  public static <T extends Enum<T>> Enum<T> convert(FocusEventCause value,
      Class<Enum<T>> enumtype) {
    if (enumtype == null || value == null) {
      return null;
    } else {
      for (Enum<T> e : enumtype.getEnumConstants()) {
        if (e.name().equals(value.name())) {
          return e;
        }
      }
      return null;
    }
  }
}
