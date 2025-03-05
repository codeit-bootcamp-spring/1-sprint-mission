package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.util.UUID;

/*

  private UUID id;
  private Instant createdAt;
  private Instant updatedAt;
  //
  private String username;
  private String email;
  private String password;
  private UUID profileId;
 */
public record UserCreateResponse(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String username,
    String email,
    String password,
    UUID profileId
) {

  public static UserCreateResponse from(User user) {
    return new UserCreateResponse(
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
