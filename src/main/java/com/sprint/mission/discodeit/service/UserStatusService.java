package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatus create(UserStatusCreateRequest request);

    UserStatus findById(UUID id);

    List<UserStatus> findAll();

    UserStatus update(UUID id, UserStatusUpdateRequest request);

    UserStatus updateByUserId(UUID userId, UserStatusUpdateRequest request);

    void deleteById(UUID id);

    UserStatus findByUserId(UUID userId);
}