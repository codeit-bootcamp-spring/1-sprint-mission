package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.dto.binarycontetnt.BinaryContentResponse;
import com.sprint.mission.discodeit.dto.status.UserStatusResponse;
import com.sprint.mission.discodeit.entity.User;
import java.util.Optional;
import java.util.UUID;

public record UserResponse(UUID id, String username, String email, UserStatusResponse status,
                           BinaryContentResponse profile) {

  public static UserResponse fromEntity(User user) {
    return new UserResponse(user.getId(), user.getUsername(), user.getEmail(),
        UserStatusResponse.fromEntity(user.getUserStatus()),
        Optional.ofNullable(user.getProfileImage()).map(BinaryContentResponse::fromEntity)
            .orElse(null));
  }
}
