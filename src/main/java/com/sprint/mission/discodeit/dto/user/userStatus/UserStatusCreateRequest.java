package com.sprint.mission.discodeit.dto.user.userStatus;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatusCreateRequest {

  private UUID userId;
  private Instant lastActiveAt;

  public UserStatusCreateRequest(UUID userId, Instant lastActiveAt) {
    this.userId = userId;
    this.lastActiveAt = lastActiveAt;
  }
}
