package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.UserStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository {

    UserStatus save(UserStatus userStatus);

    Optional<UserStatus> findById(UUID uuid);

    Optional<UserStatus> findByUserId(UUID userId);

    List<UserStatus> findAll();

    void delete(UUID uuid);
}
