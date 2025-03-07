package com.sprint.mission.discodeit.dto.userStatus;

import java.util.UUID;

public record CreateUserStatusDto(
    String userId
) {

  public UUID getUserId() {
    return UUID.fromString(userId);
  }
}
