package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.util.UUID;

public record UserDto(
    UUID userId,
    Instant createdAt,
    Instant updatedAt,
    String userName,
    String email,
    UUID profileId,
    boolean isOnline
) {

  public static UserDto createEntity(User user) {
    return new UserDto(
        user.getId(),
        user.getCreatedAt(),
        user.getUpdatedAt(),
        user.getUsername(),
        user.getEmail(),
        user.getProfile() != null ? user.getProfile().getId() : null,
        false
    );
  }

  public static UserDto fromEntity(User user, UserStatus userStatus) {
    return new UserDto(
        user.getId(),
        user.getCreatedAt(),
        user.getUpdatedAt(),
        user.getUsername(),
        user.getEmail(),
        user.getProfile() != null ? user.getProfile().getId() : null,
        userStatus.isUserOnline()
    );
  }
}
