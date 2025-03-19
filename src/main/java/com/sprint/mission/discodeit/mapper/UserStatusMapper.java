package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.UserStatusDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserStatusMapper {

  UserStatusMapper INSTANCE = Mappers.getMapper(UserStatusMapper.class);

  UserStatusDto toDto(UserStatus userStatus);
}