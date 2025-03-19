package com.spirnt.mission.discodeit.mapper;

import com.spirnt.mission.discodeit.dto.user.UserDto;
import com.spirnt.mission.discodeit.enity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = BinaryContentMapper.class)
public interface UserMapper {

  @Mapping(source = "status.online", target = "online")
  UserDto toDto(User user);
}
