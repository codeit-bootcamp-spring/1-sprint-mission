package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import org.springframework.stereotype.Component;

@Component
public class UserStatusMapper {

  public UserStatusDto toDto(UserStatus userStatus) {
    return new UserStatusDto(
        userStatus.getId(),

        //여기 laze 인데, userstatus 객체 넘길때 fetch join 으로 user 같이 쿼리하자
        userStatus.getUser().getId(),
        userStatus.getLastActiveAt());
  }
}
