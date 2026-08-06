package org.webswing.server.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;

public class WebswingObjectMapper {

  private static final ObjectMapper mapper = JsonMapper.builder()
      .changeDefaultPropertyInclusion(v -> v.withValueInclusion(JsonInclude.Include.NON_NULL))
      .disable(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS).build();

  public static ObjectMapper get() {
    return mapper;
  }

  private WebswingObjectMapper() {}
}
