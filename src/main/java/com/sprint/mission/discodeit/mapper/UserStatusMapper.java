package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.UserStatusDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface UserStatusMapper {
  UserStatusMapper INSTANCE= Mappers.getMapper(UserStatusMapper.class);

  UserStatusDto userStatusToDto(UserStatus userStatus);

  UserStatus dtoToUserStatus(UserStatusDto userStatusDto);

}
