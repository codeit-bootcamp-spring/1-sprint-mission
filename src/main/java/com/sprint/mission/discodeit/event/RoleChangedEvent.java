package com.sprint.mission.discodeit.event;

import lombok.Getter;

@Getter
public class RoleChangedEvent {

  private final String userId;
  private final String oldRole;
  private final String newRole;

  public RoleChangedEvent(String userId, String oldRole, String newRole) {
    this.userId = userId;
    this.oldRole = oldRole;
    this.newRole = newRole;
  }
}
