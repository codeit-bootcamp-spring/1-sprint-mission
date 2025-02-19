package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserInfoDto;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User createUser(UserDto userDto);

    UserInfoDto readUser(UUID userId);

    List<UserInfoDto> readAll();

    void updateUser(UUID userId, UserDto userDto);

    void deleteUser(UUID userId);
}