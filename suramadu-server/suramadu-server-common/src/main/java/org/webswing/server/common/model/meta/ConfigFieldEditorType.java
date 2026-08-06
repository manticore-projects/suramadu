package org.webswing.server.common.model.meta;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface ConfigFieldEditorType {
  public enum EditorType {
    String, Number, Boolean, Object, StringList, StringMap, ObjectList, ObjectListAsTable, ObjectMap, Generic;
  }

  EditorType editor() default EditorType.Object;

  String className() default "";

}
