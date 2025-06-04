package com.spirnt.mission.discodeit.mapper;

import com.spirnt.mission.discodeit.dto.user.UserDto;
import com.spirnt.mission.discodeit.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = BinaryContentMapper.class)
public interface UserMapper {

    // user만 하는 경우, 온라인 여부는 스킵
    @Mapping(target = "online", ignore = true)
    UserDto toDto(User user);

    @Mapping(target = "online", expression = "java(isOnline)")
    UserDto toDto(User user, boolean isOnline);
}
