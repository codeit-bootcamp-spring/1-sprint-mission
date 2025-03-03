package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserReadResponse;
import com.sprint.mission.discodeit.dto.UserUpdateRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    UserReadResponse create(UserCreateRequest userDTO);
    Optional<UserReadResponse> read(UUID id);
    List<UserReadResponse> readAll();
    void update(UUID id, UserUpdateRequest userDTO);
    void delete(UUID id);
    boolean updateLastSeen(UUID userId);
    void updateProfileImage(UUID userId, UUID imageId);
}
