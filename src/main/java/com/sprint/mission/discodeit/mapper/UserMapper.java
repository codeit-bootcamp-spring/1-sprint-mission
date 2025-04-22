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

    public UserResponseDto toResponseDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .profile(user.getProfile() == null ? null : binaryContentMapper.toResponseDto(user.getProfile()))
                .online(user.getUserStatus().isOnline())
                .build();
    }

    public User toEntity(UserSignupRequestDto userSignupRequestDto) {
        return new User(
                userSignupRequestDto.username(),
                userSignupRequestDto.nickname(),
                userSignupRequestDto.email(),
                userSignupRequestDto.password(),
                new Phone(userSignupRequestDto.phone(), userSignupRequestDto.regionCode()),
                userSignupRequestDto.userType(),
                userSignupRequestDto.introduce(),
                null
        );
    }
}