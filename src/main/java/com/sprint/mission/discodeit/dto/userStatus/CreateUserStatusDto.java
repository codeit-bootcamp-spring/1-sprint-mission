package com.sprint.mission.discodeit.dto.userStatus;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record CreateUserStatusDto(
    @NotBlank
    String userId
) {

  public UUID getUserId() {
    return UUID.fromString(userId);
  }
}
