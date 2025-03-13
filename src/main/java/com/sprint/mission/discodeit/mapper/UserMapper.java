package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.request.UserRequest;
import com.sprint.mission.discodeit.dto.response.UserResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "isOnline" , expression = "java(userStatus.isOnline())")
    @Mapping(target = "id", source = "user.id")
    UserResponse toDto(User user, UserStatus userStatus);

    User toEntity (UserRequest request);
}
