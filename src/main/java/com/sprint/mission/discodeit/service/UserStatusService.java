package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatus create(UserStatusCreateRequest request);

    UserStatus find(UUID userStatusId);

    List<UserStatus> findAllByUserId(UUID userId);

    void update(UUID userStatusId, UserStatusUpdateRequest request);

    void updateByUserId(UUID userId, UserStatusUpdateRequest request);

    void delete(UUID userStatusId);
}
