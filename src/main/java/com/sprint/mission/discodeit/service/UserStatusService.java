package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.userStatus.UserStatusCreate;
import com.sprint.mission.discodeit.dto.user.userStatus.UserStatusUpdate;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatus create(UserStatusCreate userStatusCreate);
    UserStatus findById(UUID userStatusId);
    List<UserStatus> findAll();
    UserStatus upate (UUID userStatusId, UserStatusUpdate userStatusUpdate);
    UserStatus updateByUserId(UUID userId, UserStatusUpdate userStatusUpdate);
    void delete(UUID userStatusId);
}
