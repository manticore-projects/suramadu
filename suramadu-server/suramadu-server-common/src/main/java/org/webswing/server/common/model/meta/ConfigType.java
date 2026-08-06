package org.webswing.server.common.model.meta;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@SuppressWarnings("rawtypes")
public @interface ConfigType {
  Class<? extends MetadataGenerator> metadataGenerator() default MetadataGenerator.class;
}
