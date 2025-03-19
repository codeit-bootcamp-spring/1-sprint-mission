package com.sprint.mission.discodeit.dto.userStatus;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

public record UserStatusCreateRequest(

    @NotNull
    UUID userId,

    @NotNull
    Instant lastActiveAt
) {

  public static UserStatusCreateRequest from(UUID userId, Instant lastActiveAt) {
    return new UserStatusCreateRequest(userId, lastActiveAt);
  }
}
