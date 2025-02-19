package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.domain.UserStatus;
import com.sprint.mission.discodeit.dto.userstatus.CreateUserStatusRequest;
import com.sprint.mission.discodeit.dto.userstatus.UpdateUserStatusRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusService {
    UserStatus create(CreateUserStatusRequest request);

    Optional<UserStatus> find(UUID id);

    Optional<UserStatus> findByUserId(UUID userID);

    List<UserStatus> findAll();

    UserStatus update(UpdateUserStatusRequest request);

    UserStatus updateByUserId(UUID userId, UpdateUserStatusRequest request);

    void delete(UUID id);
}
