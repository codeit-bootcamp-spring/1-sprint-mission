package com.sprint.mission.discodeit.repository;


import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository {

    UUID save(UserStatus userStatus);
    UserStatus find(UUID id);
    List<UserStatus> findAll();
    UUID update(UserStatus userStatus);
    UUID delete(UUID id);

    Optional<UserStatus> findByUserId(UUID userId);

    void deleteByUserId(UUID userId);
}
