package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserStatusDto;
import com.sprint.mission.discodeit.entity.UserStatusType;

import java.util.UUID;

public interface UserStatusService {
    UserStatusDto create(UserStatusDto userStatusDTO);
    UserStatusDto find(UUID userId);
    void delete(UUID userId);
    UserStatusType getUserOnlineStatus(UUID userId);
}