package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.domain.entity.UserStatus;
import com.sprint.mission.discodeit.web.dto.UserStatusUpdateDto;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatus create(UserStatus userStatus);
    UserStatus find(UUID userStatusId);
    List<UserStatus> findAll();
    UserStatus update(UUID userStatusId, UserStatusUpdateDto updateUserStatus);
    UserStatus updateByUserId(UUID userId, UserStatusUpdateDto updateUserStatus);
    void delete(UUID userStatusId);
}
