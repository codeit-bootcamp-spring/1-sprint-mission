package com.sprint.mission.discodeit.service.Interface;

import com.sprint.mission.discodeit.dto.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    UserDto createUser(UserCreateRequestDto request);
    UserDto getUserById(UUID id);
    List<UserDto> getAllUsers();
    List<User> findAllUsers();
    UserDto updateUser(UUID userId,UserUpdateRequestDto request);
    void deleteUser(UUID id);
}

