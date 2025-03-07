package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.binarycontetnt.BinaryContentResponse;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.entity.User;

public class UserStatusMapper {

  public static UserResponse toDto(User user) {
    return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getUserStatus(),
        BinaryContentResponse.fromEntity(user.getProfileImage()));
  }
}
