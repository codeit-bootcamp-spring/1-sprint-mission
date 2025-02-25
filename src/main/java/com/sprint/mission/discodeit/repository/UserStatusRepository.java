package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.domain.UserStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository {
    UserStatus save(UserStatus userStatus);

    Optional<UserStatus> findByUserId(UUID userID);

    Optional<UserStatus> findById(UUID id);

    List<UserStatus> findByIsOnlineTrue();

    List<UserStatus> findAll();

    void deleteById(UUID id);

    void deleteByUserId(UUID userID);
}
