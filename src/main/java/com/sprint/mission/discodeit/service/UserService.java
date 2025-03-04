package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResponse createUser(UserCreateRequest request);

    UserResponse createUserWithProfileImage(UserCreateRequest userRequest, BinaryContentCreateRequest imageRequest);

    UserResponse getUserById(UUID userId);

    List<UserResponse> getAllUsers();

    UserResponse updateUser(UUID userId, UserUpdateRequest request);

    UserResponse updateUserWithProfileImage(UUID userId, UserUpdateRequest userRequest, BinaryContentCreateRequest imageRequest);

    boolean deleteUser(UUID userId);
}


