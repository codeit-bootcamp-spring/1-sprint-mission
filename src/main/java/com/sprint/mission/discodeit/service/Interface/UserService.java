package com.sprint.mission.discodeit.service.Interface;

import com.sprint.mission.discodeit.dto.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User createUser(UserCreateRequestDto request);
    Optional<UserResponseDto> getUserById(UUID id);
    List<UserResponseDto> getAllUsers();
    List<User> findAllUsers();
    User updateUser(UserUpdateRequestDto request);
    void deleteUser(UUID id);
}

