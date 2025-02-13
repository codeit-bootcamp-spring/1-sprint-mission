package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatus createUserStatus(UserStatusCreateRequest userStatusCreateRequest);

    UserStatus findUserStatusById(UUID userStatusId);
    List<UserStatus> findAllUserStatus();

    UserStatus updateUserStatus(UserStatusUpdateRequest userStatusUpdateRequest);
    UserStatus updateUserStatusByUserId(UUID userId, Instant lastConnectTime);

    void deleteUserStatusById(UUID userStatusId);
}
