package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.entity.status.UserStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface UserStatusMapper {

  @Mapping(target = "userId", source = "user.id")
  UserStatusDto toDto(UserStatus userStatus);
}
