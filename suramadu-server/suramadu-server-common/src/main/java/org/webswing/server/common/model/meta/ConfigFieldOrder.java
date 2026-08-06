package org.webswing.server.common.model.meta;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface ConfigFieldOrder {
  String[] value() default {};
}
