package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Map;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User createUser(UserCreateRequest userCreateRequest, Optional<BinaryContentCreateRequest> profileCreateRequest);
    UserDto readUserById(UUID userId);
    List<UserDto> readAll();
    User updateUserField(UUID userId, UserUpdateRequest userUpdateRequest, Optional<BinaryContentCreateRequest> profileCreateRequest);
    void deleteUserById(UUID userId);

}
