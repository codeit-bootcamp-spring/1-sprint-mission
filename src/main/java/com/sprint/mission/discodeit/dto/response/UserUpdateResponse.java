package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.util.UUID;

  /*
  UUID id,
    Instant createdAt,
    Instant updatedAt,
    String username,
    String email,
    String password,
    UUID profileId
   */

public record UserUpdateResponse(
    UUID id, Instant createdAt,
    Instant updateAt, String username, String email, String password, UUID profileId
) {

  public static UserUpdateResponse from(User user) {
    return new UserUpdateResponse(
        user.getId(),
        user.getCreatedAt(),
        user.getUpdatedAt(),
        user.getUsername(),
        user.getEmail(),
        user.getPassword(),
        user.getProfileId()
    );
  }
}
