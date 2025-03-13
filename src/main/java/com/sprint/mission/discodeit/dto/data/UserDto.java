package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.UUID;

public record UserDto(
    UUID id,
    @NotBlank String username,
    String email,
    BinaryContent profile,
    Boolean online
) {
  public UserDto(User user){
    this(user.getId(), user.getUsername(), user.getEmail(), user.getProfile(), user.getUserStatus().isOnline());
  }

}
