package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.util.PasswordEncryptor;
import org.springframework.stereotype.Component;

import java.util.Base64;

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
        userStatus.getUserStatus().toString(),
        profilePicture != null && profilePicture.getData() != null
            ? Base64.getEncoder().encodeToString(profilePicture.getData())
            : null
    );
  }

}
