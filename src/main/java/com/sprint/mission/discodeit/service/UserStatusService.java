package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.UserStatusCreate;
import com.sprint.mission.discodeit.dto.user.UserStatusUpdate;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatus create(UserStatusCreate userStatusCreate);
    UserStatus findById(UUID userStatusId);
    UserStatus findByUserId(UUID userId);

    List<UserStatus> findAll();
    UserStatus update(UUID userStatusId, UserStatusUpdate userStatusUpdate);
    UserStatus updateByUserId(UUID userId, UserStatusUpdate userStatusUpdate);
    void delete(UUID userStatusId);
    void deleteByUserId(UUID userId);
}
