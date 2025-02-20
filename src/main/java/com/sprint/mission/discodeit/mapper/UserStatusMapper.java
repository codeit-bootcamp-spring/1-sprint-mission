package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import org.springframework.stereotype.Component;

@Component
public class UserStatusMapper {

  public UserStatusDto userStatusEntityToDto(UserStatus userStatus) {
    return new UserStatusDto(
        userStatus.getId(),
        userStatus.getUserId(),
        userStatus.getLastActiveAt(),
        userStatus.getCreatedAt(),
        userStatus.getUpdatedAt()
    );
  }
}
