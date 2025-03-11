package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import org.springframework.stereotype.Component;

@Component
public class UserStatusMapper {

  public UserStatusDto toDto(UserStatus entity) {
    if (entity == null) {
      return null;
    }
    UserStatusDto dto = new UserStatusDto();
    dto.setId(entity.getId());
    dto.setUserId(entity.getUser().getId());
    dto.setLastActiveAt(entity.getLastActiveAt());
    return dto;
  }
}
