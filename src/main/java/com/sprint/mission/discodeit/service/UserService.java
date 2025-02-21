package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.BinaryContentRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User create(UserCreateRequest userRequest, Optional<BinaryContentRequest> binaryContentRequest);
    UserResponse find(UUID userId);
    List<UserResponse> findAll();
    UserResponse getUserInfo(User user);
    User update(UserUpdateRequest userUpdateRequest, BinaryContentRequest binaryContentRequest);
    void delete(UUID userId);
    void validateDuplicateName(String name);
    void validateDuplicateEmail(String email);
}
