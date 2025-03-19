package com.spirnt.mission.discodeit.mapper;

import com.spirnt.mission.discodeit.dto.userStatus.UserStatusDto;
import com.spirnt.mission.discodeit.enity.UserStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserStatusMapper {

  @Mapping(source = "user.id", target = "userId")
  UserStatusDto toDto(UserStatus userStatus);
}
