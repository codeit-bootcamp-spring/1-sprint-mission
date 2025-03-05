package com.sprint.mission.discodeit.dto.request;

import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;

public record UserStatusUpdateRequest(
    Instant newLastActiveAt
) {

  public static UserStatusUpdateRequest from(UserStatus status) {
    return new UserStatusUpdateRequest(status.getLastActiveAt());
  }
}
