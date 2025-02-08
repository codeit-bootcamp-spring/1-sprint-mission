package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.Map;
import java.util.UUID;

public interface UserStatusRepository {

    UserStatus save(UserStatus userStatus);
    UserStatus findById(UUID id);
    Map<UUID, UserStatus> load();
    void delete(UUID id);
}
