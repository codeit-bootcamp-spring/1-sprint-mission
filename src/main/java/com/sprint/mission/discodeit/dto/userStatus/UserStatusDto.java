package com.sprint.mission.discodeit.dto.userStatus;

import com.sprint.mission.discodeit.entity.status.UserStatus;
import java.util.UUID;

public record UserStatusDto(
    UUID id,
    boolean isOnline
) {

  public static UserStatusDto from(UserStatus userStatus) {
    return new UserStatusDto(
        userStatus.getId(),
        userStatus.isActive()
    );
  }
}
