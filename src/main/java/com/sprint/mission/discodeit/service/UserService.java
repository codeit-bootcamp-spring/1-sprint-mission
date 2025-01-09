package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User createUser(UserDto userDto);

    User readUser(UUID userId);

    List<User> readAll();

    void updateUser(UUID userId, UserDto userDto);

    void deleteUser(UUID userId);
}
