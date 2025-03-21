package com.sprint.mission.repository;

import com.sprint.mission.entity.addOn.UserStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository {
    UserStatus save(UserStatus userStatus);

    Optional<UserStatus> findById(UUID id);

    Optional<UserStatus> findByUserId(UUID userId);

    List<UserStatus> findAll();

    boolean existsById(UUID id);

    void deleteById(UUID id);

    void deleteByUserId(UUID userId);
}
