package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserResponse;
import com.sprint.mission.discodeit.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResponse create(UserCreateRequest request);
    UserResponse find(UUID userId);
    List<UserResponse> findAll();
    UserResponse update(UUID userId, UserUpdateRequest request);
    void delete(UUID userId);
}
