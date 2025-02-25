package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.domain.UserStatus;
import com.sprint.mission.discodeit.dto.userstatus.CreateUserStatusRequest;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatus create(CreateUserStatusRequest request);

    UserStatus find(UUID id);

    UserStatus findByUserId(UUID userID);

    List<UserStatus> findAll();

    UserStatus updateByUserId(UUID userId);

    void delete(UUID id);
}
