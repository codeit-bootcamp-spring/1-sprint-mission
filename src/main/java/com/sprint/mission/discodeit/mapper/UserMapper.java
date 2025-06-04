package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.request.CreateUserRequest;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {BinaryContentMapper.class})
public interface UserMapper {

    @Mapping(target = "name", source = "createUserRequest.username")
    @Mapping(target = "profileImage", ignore = true)
    @Mapping(target = "password", source = "hashedPassword")
    @Mapping(target = "role", ignore = true)
    User toEntity(CreateUserRequest createUserRequest, String hashedPassword);

    @AfterMapping
    default void setDefaultRole(@MappingTarget User user) {
        user.updateRole(Role.USER);
    }

    @Mapping(target = "username", source = "user.name")
    @Mapping(target = "profile", source = "user.profileImage")
    @Mapping(target = "online", source = "online")
    UserDto toUserDto(User user, boolean online);
}
