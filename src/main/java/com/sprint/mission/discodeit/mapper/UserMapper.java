package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.CreateUserResponse;
import com.sprint.mission.discodeit.dto.user.LoginResponseDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user_status.UserStatusResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.util.BinaryContentUtil;
import com.sprint.mission.discodeit.util.PasswordEncryptor;
import com.sprint.mission.discodeit.util.UserStatusType;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper(componentModel = "spring", uses = BinaryContentMapper.class, imports = {UUID.class, PasswordEncryptor.class, BinaryContentUtil.class}, builder = @Builder(disableBuilder = false))
public interface UserMapper {

  @Mapping(target = "id", expression = "java(UUID.randomUUID().toString())")
  @Mapping(target = "username", source = "dto.username")
  @Mapping(target = "password", expression = "java(PasswordEncryptor.hashPassword(dto.password()))")
  @Mapping(target = "email", source = "dto.email")
  @Mapping(target = "profileId", ignore = true)
  @Mapping(target = "status", ignore = true)
  User toEntity(CreateUserRequest dto);


  @Mapping(target = "id", source = "id")
  @Mapping(target = "createdAt", source = "createdAt")
  @Mapping(target = "updatedAt", source = "updatedAt")
  @Mapping(target = "username", source = "username")
  @Mapping(target = "email", source = "email")
  @Mapping(target = "profileId", source = "profileId")
  @Mapping(target = "online", source = "status", qualifiedByName = "userStatusSetter")
  UserResponseDto toDto(User user);

  @Mapping(target = "id", source = "id")
  CreateUserResponse toCreateUserResponse(User user);

  @Mapping(target = "id", source = "status.id")
  @Mapping(target = "createdAt", source = "status.createdAt")
  @Mapping(target = "updatedAt", source = "status.updatedAt")
  @Mapping(target = "userId", source = "id")
  @Mapping(target = "lastActivityAt", source = "status.lastOnlineAt")
  @Mapping(target = "online", source = "status", qualifiedByName = "userStatusSetter")
  UserStatusResponseDto withStatus(User user);

  @Mapping(target = "id", source = "id")
  LoginResponseDto toLoginResponse(User user);
  @Named("userStatusSetter")
  default boolean userStatusToBoolean(UserStatus status){

    if(status.getUserStatus().equals(UserStatusType.ONLINE)) return true;
    else return false;
  }
}
