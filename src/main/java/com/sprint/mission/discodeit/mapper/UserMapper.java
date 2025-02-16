package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.util.BinaryContentUtil;
import com.sprint.mission.discodeit.util.PasswordEncryptor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.Base64;
/*
@Component
public class UserMapper {
  public User from(CreateUserRequest dto){
    return new User.UserBuilder(dto.username(), PasswordEncryptor.hashPassword(dto.password()), dto.email(), dto.phoneNumber())
        .nickname(dto.nickname())
        .description(dto.description())
        .build();
  }



  public UserResponseDto from(User user, UserStatus userStatus, BinaryContent profilePicture){
    return new UserResponseDto(
        user.getUUID(),
        user.getUsername(),
        user.getEmail(),
        user.getNickname(),
        user.getPhoneNumber(),
        user.getCreatedAt(),
        userStatus.getLastOnlineAt(),
        user.getDescription(),
        userStatus.getUserStatus().toString(),
        profilePicture != null && profilePicture.getData() != null
            ? Base64.getEncoder().encodeToString(profilePicture.getData())
            : null
    );
  }

}*/

@Mapper(componentModel = "spring", uses = BinaryContentMapper.class, imports = BinaryContentUtil.class)
public interface UserMapper {
  @Mapping(target = "profileImage", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "password", source = "password", qualifiedByName = "hashPassword")
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  User from(CreateUserRequest dto);

  @Named("hashPassword")
  default String hashPassword(String password) {
    return PasswordEncryptor.hashPassword(password);
  }


  // TODO : User 만 받도록 리팩토링
  @Mapping(target = "userId", source = "user.UUID")
  @Mapping(target = "username", source = "user.username")
  @Mapping(target = "email", source = "user.email")
  @Mapping(target = "nickname", source = "user.nickname")
  @Mapping(target = "phoneNumber", source = "user.phoneNumber")
  @Mapping(target = "createdAt", source = "user.createdAt")
  @Mapping(target = "lastOnlineAt", source = "userStatus.lastOnlineAt")
  @Mapping(target = "description", source = "user.description")
  @Mapping(target = "userStatus", source = "userStatus.userStatus")
  @Mapping(target = "profilePictureBase64", expression = "java(BinaryContentUtil.convertToBase64(profile))")
  UserResponseDto from(User user, UserStatus userStatus, BinaryContent profile);

}
