package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserStatusUpdateByUserIdRequest;
import com.sprint.mission.discodeit.dto.user.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatus createUserStatus(UserStatusCreateRequest userStatusCreateRequest);

    UserStatus findUserStatusById(UUID userStatusId);
    UserStatus findUserStatusByUserId(UUID userId);
    List<UserStatus> findAllUserStatus();

    UserStatus updateUserStatus(UserStatusUpdateRequest userStatusUpdateRequest);
    UserStatus updateUserStatusByUserId(UUID userId, UserStatusUpdateByUserIdRequest userStatusUpdateByUserIdRequest);

    void deleteUserStatusById(UUID userStatusId);
    void delteUserStatusByUserId(UUID userId);
}
