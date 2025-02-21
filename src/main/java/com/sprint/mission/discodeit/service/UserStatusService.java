package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreate;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateByUserId;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdate;
import com.sprint.mission.discodeit.entity.OnlineStatus;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatus create(UserStatusCreate userStatusCreate);
    UserStatus find(UUID userStatusId);
    UserStatus findByUserId(UUID userId);
    List<UserStatus> findAll();
    OnlineStatus getOnlineStatus(UUID userId);
    UserStatus update(UserStatusUpdate userStatusUpdate);
    UserStatus updateByUserUd(UserStatusUpdateByUserId userStatusUpdateByUserId);
    void delete(UUID userStatusId);
    void deleteByUserId(UUID userId);
}
