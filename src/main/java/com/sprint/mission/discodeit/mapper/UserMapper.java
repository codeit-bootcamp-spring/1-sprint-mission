package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.util.BinaryContentUtil;
import com.sprint.mission.discodeit.util.PasswordEncryptor;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = BinaryContentMapper.class, imports = {PasswordEncryptor.class, BinaryContentUtil.class}, builder = @Builder(disableBuilder = false))
public interface UserMapper {

  @Mapping(target = "UUID", ignore = true)
  @Mapping(target = "username", source = "username")
  @Mapping(target = "password", expression = "java(PasswordEncryptor.hashPassword(dto.password()))")
  @Mapping(target = "email", source = "email")
  @Mapping(target = "phoneNumber", source = "phoneNumber")
  @Mapping(target = "description", source = "description")
  @Mapping(target = "profileImage", ignore = true)
  @Mapping(target = "status", ignore = true)
  User toEntity(CreateUserRequest dto);


  @Mapping(target = "userId", source = "user.UUID")
  @Mapping(target = "username", source = "user.username")
  @Mapping(target = "email", source = "user.email")
  @Mapping(target = "nickname", source = "user.nickname")
  @Mapping(target = "phoneNumber", source = "user.phoneNumber")
  @Mapping(target = "createdAt", source = "user.createdAt")
  @Mapping(target = "lastOnlineAt", source = "userStatus.lastOnlineAt")
  @Mapping(target = "description", source = "user.description")
  @Mapping(target = "profilePictureBase64", expression = "java(BinaryContentUtil.convertToBase64(profile))")
  UserResponseDto toDto(User user, UserStatus userStatus, BinaryContent profile);


}
