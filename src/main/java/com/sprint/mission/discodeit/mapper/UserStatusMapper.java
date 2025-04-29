package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserStatusMapper {

  public UserStatusDto toDto(UserStatus userStatus) {
    return UserStatusDto.builder()
        .id(userStatus.getId())  // UUID
        .userid(userStatus.getUser().getId())  // UUID
        .lastActiveAt(userStatus.getLastAccessedAt())  // Instant
        .build();
  }


}
