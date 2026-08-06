package org.webswing.server.common.model.meta;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@SuppressWarnings("rawtypes")
public @interface ConfigFieldPresets {

  String[] value() default {};

  Class<? extends Enum> enumClass() default Enum.class;
}
