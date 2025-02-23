package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserDetailDto;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User createUser(UserDto userDto);

    UserDetailDto readUser(UUID userId);

    List<UserDetailDto> readAll();

    void updateUser(UUID userId, UserDto userDto);

    void deleteUser(UUID userId);
}