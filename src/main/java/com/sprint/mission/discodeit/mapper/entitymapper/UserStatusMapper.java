package com.sprint.mission.discodeit.mapper.entitymapper;

import com.sprint.mission.discodeit.dto.response.UserStatusDto;
import com.sprint.mission.discodeit.entity.status.user.UserStatus;

public class UserStatusMapper {

  public static UserStatusDto toDto(UserStatus userStatus) {
    return new UserStatusDto(userStatus.getId(), userStatus.getUser().getId(),
        userStatus.getLastActiveAt());
  }

}
