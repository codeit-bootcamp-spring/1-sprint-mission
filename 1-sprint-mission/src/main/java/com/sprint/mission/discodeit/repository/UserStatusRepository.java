package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.Map;
import java.util.UUID;

public interface UserStatusRepository {
    public Map<UUID, UserStatus> loadUserStatusText();
    public void saveUserStatusText();
}
