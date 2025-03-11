package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.UserStatusResponse;
import com.sprint.mission.discodeit.entity.UserStatus;
import org.springframework.stereotype.Component;

@Component
public class UserStatusMapper {

  public UserStatusResponse entityToDto(UserStatus userStatus) {
    return UserStatusResponse.builder()
        .id(userStatus.getId())
        .userId(userStatus.getUser().getId())
        .lastActiveAt(userStatus.getLastActiveAt())
        .build();
  }
}
