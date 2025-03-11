package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.UserStatusDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;

public class UserStatusMapper {
  public UserStatusDto toDto(UserStatus userStatus) {
    if(userStatus == null) return null;
    return new UserStatusDto(userStatus.getId(), userStatus.getUser().getId(), userStatus.getLastActiveAt());
  }
}
