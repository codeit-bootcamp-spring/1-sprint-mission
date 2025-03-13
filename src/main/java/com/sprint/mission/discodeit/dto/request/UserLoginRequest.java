package com.sprint.mission.discodeit.dto.request;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.util.UUID;

public record UserLoginRequest(
    UUID id,
    String username,
    String email,
    UserStatus status
) {

  public UserLoginRequest(User user) {
    this(user.getId(), user.getUsername(), user.getEmail(), user.getUserStatus());
  }
}
