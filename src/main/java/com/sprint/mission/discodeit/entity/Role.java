package com.sprint.mission.discodeit.entity;

public enum Role {
  ADMIN,
  CHANNEL_MANAGER,
  USER;

  public static String getHierarchy() {
    return String.format("%s > %s > %s",
        ADMIN.name(),
        CHANNEL_MANAGER.name(),
        USER.name()
    );
  }
}
