package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binarycontetnt.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    UserResponse createUser(CreateUserRequest request, Optional<CreateBinaryContentRequest> optionalRequest);

    List<UserResponse> findAllUsers();

    Optional<UserResponse> findUserById(UUID userId);

    Optional<UserResponse> updateUser(UpdateUserRequest request, Optional<CreateBinaryContentRequest> optionalRequest);

    void deleteUser(UUID uuid);
}
