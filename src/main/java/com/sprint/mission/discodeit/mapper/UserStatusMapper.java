package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface UserStatusMapper {

  /*public UserStatusDto toDto(UserStatus entity) {
    if (entity == null) {
      return null;
    }
    UserStatusDto dto = new UserStatusDto();
    dto.setId(entity.getId());
    dto.setUserId(entity.getUser().getId());
    dto.setLastActiveAt(entity.getLastActiveAt());
    return dto;
  }*/
  UserStatusMapper INSTANCE = Mappers.getMapper(UserStatusMapper.class);

  @Mapping(source = "user.id", target = "userId")
  UserStatusDto toDto(UserStatus entity);
}
