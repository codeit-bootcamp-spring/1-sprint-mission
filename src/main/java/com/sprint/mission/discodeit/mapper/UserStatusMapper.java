package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.UserStatusDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@NoArgsConstructor
// MapStruct 이용해보기
public class UserStatusMapper {

  public UserStatusDto toDto(UserStatus userStatus) {
    return new UserStatusDto(
        userStatus.getId(),
        userStatus.getUser().getId(),
        userStatus.getLastActiveAt()
    );
  }
}
