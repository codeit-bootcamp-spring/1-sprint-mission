package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserStatusDTO;
import com.sprint.mission.discodeit.entity.UserStatusType;

import java.time.Instant;

public interface UserStatusService {
    UserStatusDTO create(UserStatusDTO userStatusDTO);
    UserStatusDTO find(String userId);
    void delete(String userId);
    UserStatusType getUserOnlineStatus(String userId);
}