package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.BinaryContentRequestDto;
import com.sprint.mission.discodeit.dto.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResponseDto create(UserCreateRequestDto userRequestDto, BinaryContentRequestDto binaryContentRequestDto);
    UserResponseDto find(UUID userId);
    List<UserResponseDto> findAll();
    UserResponseDto getUserInfo(User user);
    UserResponseDto update(UserUpdateRequestDto userUpdateRequestDto, BinaryContentRequestDto binaryContentRequestDto);
    void delete(UUID userId);
    void validateUserExists(User user);
    void validateDuplicateName(String name);
    void validateDuplicateEmail(String email);
}
