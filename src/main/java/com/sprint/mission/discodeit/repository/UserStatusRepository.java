package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.Map;
import java.util.UUID;

public interface UserStatusRepository {

    UserStatus save(UserStatus userStatus);
    UserStatus findById(UUID id);
    UserStatus findByUserId(UUID userId);
    Map<UUID, UserStatus> load();

    Boolean existsByUserId(UUID userId);
    void delete(UUID id);
}
