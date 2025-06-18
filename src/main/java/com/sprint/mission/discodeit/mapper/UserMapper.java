package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.common.Phone;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserSignupRequestDto;
import com.sprint.mission.discodeit.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

  private final BinaryContentMapper binaryContentMapper;

  public UserResponseDto toResponseDto(User user, boolean isOnline) {
    return UserResponseDto.builder()
        .id(user.getId())
        .username(user.getUsername())
        .nickname(user.getNickname())
        .email(user.getEmail())
        .role(user.getRole())
        .profile(
            user.getProfile() == null ? null : binaryContentMapper.toResponseDto(user.getProfile()))
        .online(isOnline)
        .build();
  }

  public User toEntity(UserSignupRequestDto userSignupRequestDto, String hashedPassword) {
    return new User(
        userSignupRequestDto.userName(),
        userSignupRequestDto.nickname(),
        userSignupRequestDto.email(),
        hashedPassword,
        new Phone(userSignupRequestDto.phone(), userSignupRequestDto.regionCode()),
        userSignupRequestDto.role(),
        userSignupRequestDto.introduce(),
        null
    );
  }
}