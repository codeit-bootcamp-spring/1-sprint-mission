package com.spirnt.mission.discodeit.mapper;

import com.spirnt.mission.discodeit.dto.user.UserDto;
import com.spirnt.mission.discodeit.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = BinaryContentMapper.class)
public interface UserMapper {

    @Mapping(target = "online", expression = "java(isOnline)")
    UserDto toDto(User user, boolean isOnline);
}
