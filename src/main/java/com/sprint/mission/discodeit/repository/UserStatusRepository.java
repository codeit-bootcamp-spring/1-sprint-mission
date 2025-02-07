package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.UserStatus;

import java.util.HashMap;
import java.util.UUID;

public interface UserStatusRepository {

    void save(UserStatus userStatus);

    UserStatus findById(UUID uuid);

    UserStatus findByUserId(UUID userId);

    HashMap<UUID, UserStatus> findAll();

    void delete(UUID uuid);
}
