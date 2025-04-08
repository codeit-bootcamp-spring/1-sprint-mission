package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.entity.status.UserStatus;
import org.springframework.stereotype.Component;

@Component
public class UserStatusMapper {

  public UserStatusDto toDto(UserStatus userStatus) {
    return new UserStatusDto(
        userStatus.getId(),
        userStatus.getUser().getId(),
        userStatus.getLastActiveAt()
    );
  }
}
