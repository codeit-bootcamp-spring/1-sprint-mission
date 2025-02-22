package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.OnlineStatus;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatus create(UserStatusCreateRequest userStatusCreateRequest);
    UserStatus find(UUID userStatusId);
    UserStatus findByUserId(UUID userId);
    List<UserStatus> findAll();
    OnlineStatus getOnlineStatus(UUID userId);
    UserStatus update(UUID userStatusId, UserStatusUpdateRequest userStatusUpdateRequest);
    UserStatus updateByUserUd(UUID userId, UserStatusUpdateRequest userStatusUpdateRequest);
    void delete(UUID userStatusId);
    void deleteByUserId(UUID userId);
}
