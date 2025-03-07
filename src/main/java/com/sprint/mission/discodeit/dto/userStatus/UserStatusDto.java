package com.sprint.mission.discodeit.dto.userStatus;

import com.sprint.mission.discodeit.entity.status.UserStatus;
import java.time.Instant;
import java.util.UUID;

public record UserStatusDto(
    UUID id,
    UUID userId,
    Instant lastActiveAt
) {

  public static UserStatusDto from(UserStatus userStatus) {
    return new UserStatusDto(
        userStatus.getId(),
        userStatus.getUser().getId(),
        userStatus.getLastActiveAt()
    );
  }
}
