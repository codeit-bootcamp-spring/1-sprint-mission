package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserDto create(UserCreateRequest request);
    UserDto findByUserId(UUID userId);
    List<UserDto> findAll();
    UserDto update(UserUpdateRequest request);
    void delete(UUID userId);
}
