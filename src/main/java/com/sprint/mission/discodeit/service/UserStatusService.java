package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateDto;
import com.sprint.mission.discodeit.entity.OnlineStatus;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserStatusService {
    UserStatus create(UserStatusCreateDto userStatusCreateDto);
    UserStatus find(UUID userStatusId);
    UserStatus findByUserId(UUID userId);
    List<UserStatus> findAll();
    OnlineStatus getOnlineStatus(UUID userId);
    void deleteByUserId(UUID userId);
}
