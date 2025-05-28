package com.sprint.mission.discodeit.security.role;

import java.util.Arrays;

public enum Role {
  ADMIN,
  CHANNEL_MANAGER,
  USER;

  public static Role fromString(String value) {
    return Arrays.stream(Role.values())
        .filter(role -> role.name().equalsIgnoreCase(value))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Invalid role: " + value));
  }

}
