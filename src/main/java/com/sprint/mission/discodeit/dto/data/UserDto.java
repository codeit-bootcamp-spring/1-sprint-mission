package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.util.UUID;

public record UserDto(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String username,
    String email,
    UUID profileId,
    Boolean online
) {

  public UserDto(User createdUser) {
    this(createdUser.getId(), createdUser.getCreatedAt(), createdUser.getUpdatedAt(),
        createdUser.getUsername(), createdUser.getEmail(), createdUser.getProfileId(), null);
  }

}
