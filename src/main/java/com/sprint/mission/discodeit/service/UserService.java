package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.BinaryContentRequestDto;
import com.sprint.mission.discodeit.dto.UserRequestDto;
import com.sprint.mission.discodeit.dto.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User create(UserRequestDto userRequestDto, BinaryContentRequestDto binaryContentRequestDto);
    UserResponseDto find(UUID userId);
    List<UserResponseDto> findAll();
    void update(UUID userId, String name, String email);
    void updatePassword(UUID userId, String originalPassword, String newPassword);
    void delete(UUID userId);
    void validateUserExists(User user);
    void validateDuplicateName(String name);
    void validateDuplicateEmail(String email);
}
