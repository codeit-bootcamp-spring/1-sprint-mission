package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserRequest;
import com.sprint.mission.discodeit.dto.UserResponse;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResponse createUser(UserRequest request);
    List<UserResponse> findAll();
    UserResponse findById(UUID id);
    User findByIdOrThrow(UUID id);
    void update(UUID id, UserRequest request);
    void deleteById(UUID id);
}
