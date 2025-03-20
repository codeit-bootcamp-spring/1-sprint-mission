package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.UserStatusDto;
import com.sprint.mission.discodeit.entity.status.user.UserStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserStatusDtoMapper {

  @Mapping(source = "user.id", target = "userId")
  UserStatusDto toDto(UserStatus userStatus);
}
