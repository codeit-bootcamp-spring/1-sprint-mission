package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.UUID;

public interface UserStatusService {
    UserStatus create(UUID userId);
}
