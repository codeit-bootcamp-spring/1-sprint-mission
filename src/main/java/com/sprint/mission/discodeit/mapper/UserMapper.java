package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.CreateUserResponse;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.basic.UserOnlineStatusService;
import com.sprint.mission.discodeit.util.PasswordEncryptor;
import java.util.List;
import java.util.UUID;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.crypto.password.PasswordEncoder;


@Mapper(uses = BinaryContentMapper.class, imports = {UUID.class, PasswordEncryptor.class})
public interface UserMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "username", source = "dto.username")

  @Mapping(target = "password", expression = "java(encoder.encode(dto.password()))")
  @Mapping(target = "email", source = "dto.email")
  @Mapping(target = "profile", ignore = true)
  @Mapping(target = "role", expression = "java(com.sprint.mission.discodeit.entity.UserRole.ROLE_USER)")
  User toEntity(CreateUserRequest dto, @Context PasswordEncoder encoder);

  @Mapping(target = "username", source = "newUsername")
  @Mapping(target = "email", source = "newEmail")
  @Mapping(target = "password", expression = "java(encoder.encode(dto.newPassword()))")
  @Mapping(target = "role", expression = "java(com.sprint.mission.discodeit.entity.UserRole.ROLE_USER)")
  User toEntity(UserUpdateDto dto, @Context PasswordEncoder encoder);


  @Mapping(target = "id", source = "id")
  @Mapping(target = "username", source = "username")
  @Mapping(target = "email", source = "email")
  @Mapping(target = "profile", source = "profile")
  @Mapping(target = "online", expression = "java(onlineStatusService.isUserOnline(user.getId()))")
  UserDto toDto(User user, @Context UserOnlineStatusService onlineStatusService);


  @Mapping(target = "id", source = "id")
  @Mapping(target = "profileId", source = "profile.id")
  CreateUserResponse toCreateUserResponse(User user);

  List<UserDto> toDtoList(List<User> users,
      @Context UserOnlineStatusService onlineStatusService);

}
