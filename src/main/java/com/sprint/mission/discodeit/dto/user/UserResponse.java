package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.dto.binarycontetnt.BinaryContentResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.UUID;

public record UserResponse(UUID id, String username, String email, UserStatus status,
                           BinaryContentResponse profile) {

  public static UserResponse fromEntity(User user) {
    return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getUserStatus(),
        BinaryContentResponse.fromEntity(user.getProfileImage()));
  }
}
