package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.util.UUID;

public record UserDTO(
    UUID userId,
    Instant createdAt,
    Instant updatedAt,
    String userName,
    String email,
    UUID profileId,
    boolean isOnline
) {

  public static UserDTO createEntity(User user) {
    return new UserDTO(
        user.getId(),
        user.getCreatedAt(),
        user.getUpdatedAt(),
        user.getUserName(),
        user.getEmail(),
        user.getProfile().getId(),
        false
    );
  }

  public static UserDTO fromEntity(User user, UserStatus userStatus) {
    return new UserDTO(
        user.getId(),
        user.getCreatedAt(),
        user.getUpdatedAt(),
        user.getUserName(),
        user.getEmail(),
        user.getProfile().getId(),
        userStatus.isUserOnline()
    );
  }
}
