package com.sprint.mission.discodeit.service.validate;

import java.util.Map;
import java.util.UUID;

public interface ServiceValidator<T> {

  T entityValidate(T t);

  default Map<UUID, T> entityValidate(Map<UUID, T> map) {
    return map;
  }

  default boolean isNullParam(String... str) {
    for (String s : str) {
      if (s == null) {
        return true;
      }
    }

    return false;
  }

  //boolean isNullParam(String param, T t);
}
