package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.status.UserStatusResponse;
import com.sprint.mission.discodeit.entity.UserStatus;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserStatusMapper {

  UserStatusMapper INSTANCE = Mappers.getMapper(UserStatusMapper.class);

  UserStatusResponse userStatusToUserStatusResponse(UserStatus userStatus);
}
