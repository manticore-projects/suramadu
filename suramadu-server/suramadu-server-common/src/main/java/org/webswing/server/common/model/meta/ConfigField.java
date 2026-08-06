package org.webswing.server.common.model.meta;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface ConfigField {

  ConfigGroup tab() default ConfigGroup.General;

  String label() default "";

  String description() default "";
}
