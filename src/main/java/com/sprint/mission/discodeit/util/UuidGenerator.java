package com.sprint.mission.discodeit.util;

import java.util.UUID;

public class UuidGenerator {

  public static String generateid() {
    return UUID.randomUUID().toString();
  }
}
